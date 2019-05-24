package com.example.medicapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medicapp.model.BaseMessage;
import com.example.medicapp.R;
import com.example.medicapp.adapters.MessageListAdapter;
import com.example.medicapp.custom.CircleMedicView;
import com.example.medicapp.presentation.view.IChatActivityView;
import com.example.medicapp.utils.DpToPx;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;


public class ChatActivity extends AppCompatActivity implements IChatActivityView {
    @BindView(R.id.recycler_chat) RecyclerView recyclerView;
    @BindView(R.id.imageButton2) ImageButton imageButton;
    @BindView(R.id.editText4) EditText text;
    @BindView(R.id.button3) Button buy;
    @BindView(R.id.textView7) TextView desc;
    private MessageListAdapter adapter;
    @BindView(R.id.circleMedicView2) CircleMedicView circleMedicView;
    @BindView(R.id.circleImageView2) CircleImageView circleImageView;
    @BindView(R.id.addBtnChat) ImageView addBtn;
    private static final int GALLERY_REQUEST_CODE = 65535;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);

        mSocket.connect();
        hideAll();
        LinearLayoutManager manager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        List<BaseMessage> messages = new ArrayList<>();
        adapter = new MessageListAdapter(this, messages);
        recyclerView.setAdapter(adapter);

        imageButton.setOnClickListener(l->{
            hideAll();
            BaseMessage baseMessage = new BaseMessage(text.getText().toString());
            String message = text.getText().toString();
            mSocket.emit("new message", message);
            text.setText("");
            adapter.addMessage(baseMessage);
            recyclerView.scrollToPosition(adapter.getItemCount()-1);
        });

        addBtn.setOnClickListener(l->{
            //Create an Intent with action as ACTION_PICK
            Intent intent = new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            // Launching the Intent
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        });
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    BaseMessage baseMessage = new BaseMessage();
                    baseMessage.messageType = BaseMessage.MESSAGE_TYPE_SENDER_IMAGE;
                    baseMessage.setUri(selectedImage);
                    adapter.addMessage(baseMessage);
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    //MVP
    @Override
    public void hideAll() {
        buy.setVisibility(View.GONE);
        desc.setVisibility(View.GONE);
        circleMedicView.setHeigh(DpToPx.dpToPx(51f,this));
        circleImageView.setVisibility(View.GONE);
    }

    @Override
    public void addMessage(BaseMessage message) {
        adapter.addMessage(message);

    }

    @Override
    public void initRecycler(List<BaseMessage> messages) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(() -> adapter.addMessage(new BaseMessage("connected")));
            mSocket.emit("add user", "ArtemMobile");
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                String username;
                String message;
                try {
                    username = data.getString("username");
                    message = data.getString("message");
                } catch (JSONException e) {
                    return;
                }
                BaseMessage baseMessage = new BaseMessage(message);
                baseMessage.setFrom(username);
                baseMessage.messageType = BaseMessage.MESSAGE_TYPE_RECIVER;
                adapter.addMessage(baseMessage);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            });
        }
    };

    private Emitter.Listener onUserJoined = args -> ChatActivity.this.runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        String username;
        int numUsers;
        try {
            username = data.getString("username");
            numUsers = data.getInt("numUsers");
        } catch (JSONException e) {
            return;
        }
        BaseMessage baseMessage = new BaseMessage("user joined!");
        baseMessage.setFrom(username);
        baseMessage.messageType = BaseMessage.MESSAGE_TYPE_RECIVER;
        adapter.addMessage(baseMessage);
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
    });
}
