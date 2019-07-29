package com.example.medicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.medicapp.R;
import com.example.medicapp.model.PaymentModel;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private List<PaymentModel> mData;
    private Context mContext;
    private IPaymentListener mListener;

    public PaymentAdapter(List<PaymentModel> mData, Context mContext, IPaymentListener listener) {
        this.mData = mData;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_payment, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button buy;
        TextView price;
        TextView descr;
        PaymentModel model;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buy = itemView.findViewById(R.id.button);
            descr = itemView.findViewById(R.id.textView26);
            price = itemView.findViewById(R.id.textView27);
            buy.setOnClickListener(this);
        }

        public void bind(PaymentModel paymentModel) {
            model = paymentModel;
            price.setText(model.getPrice());
            descr.setText(model.getDescr());
        }

        @Override
        public void onClick(View view) {
            mListener.onClickPayment(model);
        }
    }

    public interface IPaymentListener{
        void onClickPayment(PaymentModel model);
    }
}
