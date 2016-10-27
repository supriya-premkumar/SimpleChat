package com.supriyapremkumar.simplechat.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.supriyapremkumar.simplechat.Model.MessageModel;
import com.supriyapremkumar.simplechat.R;

public class ChatActivity extends AppCompatActivity {
    static final String TAG = ChatActivity.class.getSimpleName();
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";

    EditText etMessage;
    Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //User login
        if(ParseUser.getCurrentUser() != null){
            startWithCurrentUser(); //start with existing user
        }else{
            login();    //login as a new anonymous user
        }
    }

    public void messagePost(){
        etMessage = (EditText)findViewById(R.id.etMessage);
        btSend = (Button)findViewById(R.id.btSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = etMessage.getText().toString();
//                ParseObject message = ParseObject.create("Message");
//                message.put(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
//                message.put(BODY_KEY, data);

                //Using new 'Message' Parse-backed model now
                MessageModel message = new MessageModel();
                message.setBody(data);
                message.setUserId(ParseUser.getCurrentUser().getObjectId());
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(ChatActivity.this, "Successfully Created Message On Parse",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Log.e(TAG, "Failed to save message", e);
                        }
                    }
                });
                etMessage.setText(null);
            }
        });
    }

    // Get the userId from the cached currentUser object
    public void startWithCurrentUser() {
        messagePost();
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    public void login(){
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Anonymous login failed", e);
                }else{
                    startWithCurrentUser();
                }
            }
        });
    }

}
