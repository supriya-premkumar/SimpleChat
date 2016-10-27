package com.supriyapremkumar.simplechat.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by supriya on 10/27/16.
 */

@ParseClassName("Message")
public class MessageModel extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }


}
