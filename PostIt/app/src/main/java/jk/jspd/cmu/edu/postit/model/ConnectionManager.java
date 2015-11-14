package jk.jspd.cmu.edu.postit.model;

import org.json.JSONObject;

import jk.jspd.cmu.edu.postit.ws.remote.ConnectionInterface;
import jk.jspd.cmu.edu.postit.ws.remote.FacebookInterface;

/**
 * Created by lavalake on 11/13/15.
 */
public class ConnectionManager implements FacebookInterface, ConnectionInterface {
    @Override
    public void getFriendsList() {

    }

    @Override
    public void getProfile() {

    }

    @Override
    public int send(JSONObject obj, String url) {
        return 0;
    }

    @Override
    public JSONObject receive(String url) {
        return null;
    }
}
