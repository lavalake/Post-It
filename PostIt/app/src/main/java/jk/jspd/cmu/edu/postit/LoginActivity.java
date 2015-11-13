package jk.jspd.cmu.edu.postit;

/**
 * Created by lavalake on 11/6/15.
 */


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.content.Intent;
import android.widget.Button;

public class LoginActivity extends Activity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        Button btn = (Button)findViewById(R.id.btnLogin);
        btn.setOnClickListener(this);
    }
    public void onClick(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
