package com.supriyapremkumar.simplechat;

import android.app.Application;

import com.parse.Parse;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by supriya on 10/26/16.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myChatAppId")
                .clientKey("null")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://android-chat-client.herokuapp.com/parse/").build());
    }
}
