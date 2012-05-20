package com.laposte.lisa;

import com.codegoogle.twitterandroid.TwitterApp;
import com.codegoogle.twitterandroid.TwitterAuthListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SuiviColisActivity extends Activity {
	  //
	  // This code is based on
	  // http://abhinavasblog.blogspot.com/2011/06/for-all-my-code-thirsty-friends-twitter.html
	  //
	private static final String CONSUMER_KEY    = "UwQNKcjYgCnVuELKgasdw"; //"GPjUBdt9yn3FYTO9RcW2w"
	private static final String CONSUMER_SECRET = "IKzTtbHcUu5HkSYnUJ7NQV1slTKtWxNw7kztPUzMOM"; //"M0uxQG8r4f5ogfMDH0phU0FQvaYBOo2Bv6hP98xiag"
	  
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        final TwitterApp twitter = new TwitterApp(this, CONSUMER_KEY, CONSUMER_SECRET);
        twitter.setListener(new TwitterAuthListener() {
          @Override
          public void onError(String value) {
            Toast.makeText(SuiviColisActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
            Log.e("TWITTER", value);
            twitter.resetAccessToken();
          }
          @Override
          public void onComplete(String value) {
            tweetWithValidAuth(twitter);
          }
        });
        Button tweetButton = (Button) findViewById(R.id.tweet_button);
        tweetButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            twitter.resetAccessToken();
            if (twitter.hasAccessToken()) {
              tweetWithValidAuth(twitter);
            } else {
              twitter.authorize();
            }
          }
        });        
    }
    private void tweetWithValidAuth(TwitterApp twitter) {
        try {
          EditText tweetTextView = (EditText) findViewById(R.id.tweet_text);
          String tweet = tweetTextView.getText().toString();
          twitter.updateStatus(tweet);
          Toast.makeText(this, "Posted Successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
          if (e.getMessage().toString().contains("duplicate")) {
            Toast.makeText(this, "Post failed because of duplicates...", Toast.LENGTH_LONG).show();
          }
          e.printStackTrace();
        }
        twitter.resetAccessToken();
      }

}