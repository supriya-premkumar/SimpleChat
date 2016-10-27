package com.supriyapremkumar.simplechat.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.supriyapremkumar.simplechat.Model.MessageModel;

import java.util.List;

/**
 * Created by supriya on 10/27/16.
 */
public class ChatListAdapter extends ArrayAdapter<MessageModel> {
    private String mUserId;

    public ChatListAdapter(Context context, String userId, List<MessageModel> message) {
        super(context,0,message);
        this.mUserId = userId;
    }
}
