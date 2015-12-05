package jk.jspd.cmu.edu.postit.ws.remote;

/**
 * Created by lavalake on 11/13/15.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public interface ConnectionInterface {
    public int send(JSONObject obj, String url);
    public JSONObject sycServer(String url);
}
