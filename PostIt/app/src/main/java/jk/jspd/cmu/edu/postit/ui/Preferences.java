package jk.jspd.cmu.edu.postit.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import jk.jspd.cmu.edu.postit.R;

public class Preferences extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}