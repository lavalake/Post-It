package jk.jspd.cmu.edu.postit.exception;

import android.app.AlertDialog;
import android.util.Log;


public class CustomException {
    // Error title for AlertDialog
    private static final String ERROR_TITLE = "Error";

    // Constructor
    public CustomException(String s, AlertDialog.Builder b){
        Log.e(null, s);
        getErrorDialog(s,b);
    }

    /*
    * buildErrorDialog()
    *
    * build the AlertDialog with information and error title
    *
    * */
    public void getErrorDialog(String s,AlertDialog.Builder b){
        // set dialog title & message, and provide Button to dismiss
        b.setTitle(ERROR_TITLE);
        b.setMessage(s);
    }
}
