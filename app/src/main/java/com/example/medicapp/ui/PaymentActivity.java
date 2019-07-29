package com.example.medicapp.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.example.medicapp.App;
import com.example.medicapp.R;
import com.example.medicapp.adapters.PaymentAdapter;
import com.example.medicapp.model.PaymentModel;
import com.example.medicapp.networking.data.DataApiHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PaymentActivity extends AppCompatActivity {
    @BindView(R.id.recycler_payment)
    RecyclerView recyclerView;
    private String TAG = "PaymentActivity";
    private App app;
    private BillingClient mBillingClient;
    private Map<String, SkuDetails> mSkuDetailsMap = new HashMap<>();

    private String mSkuId = "toast_hello_world";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        app = (App) getApplication();
        init();
        initBilling();
    }

    private void initBilling() {
        mBillingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                Log.d(TAG, "onPurchasesUpdated: called");
                if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
                    //here when purchase completed
                    payComplete();
                }
            }
        }).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                Log.d(TAG, "onBillingSetupFinished: called");
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    //below you can query information about products and purchase
                    querySkuDetails(); //query for products
                    List<Purchase> purchasesList = queryPurchases(); //query for purchases

                    //if the purchase has already been made to give the goods
//                    for (int i = 0; i < purchasesList.size(); i++) {
//                        String purchaseId = purchasesList.get(i).getSku();
//                        if(TextUtils.equals(mSkuId, purchaseId)) {
//                            payComplete();
//                        }
//                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {    
                Log.d(TAG, "onBillingServiceDisconnected: called");
                //here when something went wrong, e.g. no internet connection
            }
        });
    }

    private void payComplete() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    private List<Purchase> queryPurchases() {
        Log.d(TAG, "queryPurchases: called");
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        return purchasesResult.getPurchasesList();
    }

    private void querySkuDetails() {
        Log.d(TAG, "querySkuDetails: called");
        SkuDetailsParams.Builder skuDetailsParamsBuilder = SkuDetailsParams.newBuilder();
        List<String> skuList = new ArrayList<>();
        skuList.add(mSkuId);
        skuDetailsParamsBuilder.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(skuDetailsParamsBuilder.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                Log.d(TAG, "onSkuDetailsResponse: called");
                if (responseCode == 0) {
                    for (SkuDetails skuDetails : skuDetailsList) {
                        mSkuDetailsMap.put(skuDetails.getSku(), skuDetails);
                    }
                }
            }
        });
    }

    private void init(){
        List<PaymentModel> models = new LinkedList<>();
        models.add(new PaymentModel(PaymentModel.TYPE_TWENTY_MESSAGES));
        models.add(new PaymentModel(PaymentModel.TYPE_FOURTY_MESSAGES));
        models.add(new PaymentModel(PaymentModel.TYPE_SIXTY_MESSAGES));
        PaymentAdapter adapter = new PaymentAdapter(models, this, this::onPaymentClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void onPaymentClicked(PaymentModel paymentModel) {
        Log.d(TAG, "onPaymentClicked: "+ paymentModel.getPrice());
        launchBilling(paymentModel.getmSkuId());
    }

    public void launchBilling(String skuId) {
        Log.d(TAG, "launchBilling: called");
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(mSkuDetailsMap.get(skuId))
                .build();
        mBillingClient.launchBillingFlow(this, billingFlowParams);
    }

    private void requestMoreMessages(long count){ //when payed
        DataApiHelper helper = new DataApiHelper();
        Disposable d = helper.addMessages(app.getmToken(),app.getmUserID(), count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful())
                        Toast.makeText(this, "Got 20 new messages", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Smth. went wrong", Toast.LENGTH_SHORT).show();
                },throwable -> Toast.makeText(this, "Smth. went wrong", Toast.LENGTH_SHORT).show());

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkToken();
    }

    private void checkToken() {
        if (app.getmUserID().equals("") || app.getmToken().equals("")) {
            finish();
        }
    }

}
