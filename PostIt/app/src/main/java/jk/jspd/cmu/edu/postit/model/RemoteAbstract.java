package jk.jspd.cmu.edu.postit.model;

import org.json.JSONObject;

import jk.jspd.cmu.edu.postit.ws.remote.ConnectionInterface;
import jk.jspd.cmu.edu.postit.ws.remote.FacebookInterface;

/**
 * Created by lavalake on 11/19/15.
 */
public abstract class RemoteAbstract  {

    public void getFriendsList() {

    }


    public void getProfile() {

    }

    public int send(JSONObject obj, String url) {
        return 0;
    }


    public JSONObject sycServer(String url) {
        return null;
    }
}
