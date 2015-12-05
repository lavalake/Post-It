package jk.jspd.cmu.edu.postit.ui;

/**
 * Created by lavalake on 11/6/15.
 */


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.content.Intent;
import android.widget.Button;
import com.facebook.login.widget.LoginButton;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import java.util.List;
import java.util.Arrays;

import jk.jspd.cmu.edu.postit.R;

public class LoginActivity extends Activity implements View.OnClickListener {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private final String TAG="Login";
    public static final List<String> READ_PERMISSIONS = Arrays.asList(
            "public_profile",
            "user_friends",
            //"read_stream",
            "user_photos",
            "user_about_me");
    public static final List<String> WRITE_PERMISSIONS = Arrays.asList(
            "publish_actions",
            "publish_stream",
            "publish_pages",
            "manage_pages"
            );
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.login_activity);
        /*
        Button btn = (Button)findViewById(R.id.btnLogin);
        btn.setOnClickListener(this);
        */
        loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setPublishPermissions(WRITE_PERMISSIONS);
        loginButton.setReadPermissions(READ_PERMISSIONS);
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);


        // Other app specific specialization

        // Callback registration

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e(TAG, "Success");
                Log.e(TAG,"userid: "+loginResult.getAccessToken().getUserId());
                Log.e(TAG, "token: "+loginResult.getAccessToken().getToken());
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancel() {
                // App code
                Log.e(TAG, "canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e(TAG, "error: "+exception.toString());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void onClick(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
