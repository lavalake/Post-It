package jk.jspd.cmu.edu.postit.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.facebook.share.widget.ShareDialog;
import com.facebook.CallbackManager;
import com.facebook.share.Sharer;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.model.ShareLinkContent;

import com.getbase.floatingactionbutton.FloatingActionButton;
import android.net.Uri;
import android.util.Log;

import jk.jspd.cmu.edu.postit.R;

public class PostNewActivity extends Activity {
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    private final String TAG="new post";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_new_activity);
        //Button send = (Button) findViewById(R.id.SendButton);
        FloatingActionButton send = (FloatingActionButton) findViewById(R.id.SendButton);
        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new

                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Log.e(TAG,"post to facebook succeed" );
                    }

                    @Override
                    public void onCancel() {}

                    @Override
                    public void onError(FacebookException error) {
                        Log.e(TAG,"post to facebook error: "+error.toString() );
                    }
                });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        ShareLinkContent shareContent = new ShareLinkContent.Builder()
                                .setContentTitle("PostIt").setContentDescription("test from PostIt")
                                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                                .build();




                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                            shareDialog.show(shareContent);
                        }


                EditText input = (EditText) findViewById(R.id.NewPostEditView);

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                intent.putExtra("input",input.getText().toString());
                System.out.println(input.getText().toString());
                startActivity(intent);
            }
        });
    }

}