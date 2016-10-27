package com.supriyapremkumar.simplechat.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.supriyapremkumar.simplechat.Adapter.ChatListAdapter;
import com.supriyapremkumar.simplechat.Model.MessageModel;
import com.supriyapremkumar.simplechat.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    static final String TAG = ChatActivity.class.getSimpleName();
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    // Create a handler which can run code periodically
    static final int POLL_INTERVAL = 1000; // milliseconds


    EditText etMessage;
    Button btSend;
    ListView lvChat;
    ArrayList<MessageModel> mMessages;
    ChatListAdapter mAdapter;
    boolean mFirstLoad;

    Handler mHandler = new Handler();  // android.os.Handler
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            mHandler.postDelayed(this, POLL_INTERVAL);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //User login
        if (ParseUser.getCurrentUser() != null) {
            startWithCurrentUser(); //start with existing user
        } else {
            login();    //login as a new anonymous user
        }
        mHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    public void messagePost() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<>();
        // Automatically scroll to the bottom when a data set
        // change notification is received and only if the last
        // item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new ChatListAdapter(ChatActivity.this, userId, mMessages);
        lvChat.setAdapter(mAdapter);
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
                        Toast.makeText(ChatActivity.this, "Successfully Created Message On Parse",
                                Toast.LENGTH_SHORT).show();
                        refreshMessages();
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
    public void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous login failed", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    // Query messages from Parse so we can load them into the chat adapter
    public void refreshMessages() {
        // Construct query to execute
        ParseQuery<MessageModel> query = ParseQuery.getQuery(MessageModel.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<MessageModel>() {
            public void done(List<MessageModel> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    Collections.reverse(messages);
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

}


