package com.supriyapremkumar.simplechat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;
import com.supriyapremkumar.simplechat.Model.MessageModel;

/**
 * Created by supriya on 10/26/16.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Register your parse models here
        ParseObject.registerSubclass(MessageModel.class);
        // Existing initialization happens after all classes are registered

        // For open-source Parse backend
        // set applicationId and server based on the values in the Heroku settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myChatAppId")// should correspond to APP_ID env variable

                .clientKey("null")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://android-chat-client.herokuapp.com/parse/").build());
    }
}
