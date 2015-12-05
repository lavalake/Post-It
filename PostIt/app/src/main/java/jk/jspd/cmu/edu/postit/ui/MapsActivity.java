package jk.jspd.cmu.edu.postit.ui;

import java.net.MalformedURLException;
import java.util.HashMap;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.telephony.TelephonyManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.os.AsyncTask;

//import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.net.URL;

import com.facebook.ProfileTracker;
import com.facebook.Profile;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.CallbackManager;
import com.facebook.GraphRequestBatch;

import org.json.JSONException;
import org.json.JSONArray;

import jk.jspd.cmu.edu.postit.model.AccelerometerSensor;
import jk.jspd.cmu.edu.postit.model.ConnectionManager;
import jk.jspd.cmu.edu.postit.ws.local.GPSTracker;
import jk.jspd.cmu.edu.postit.R;
import jk.jspd.cmu.edu.postit.model.AccelerometerSensor;

public class MapsActivity extends FragmentActivity implements InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapClickListener, AccelerometerSensor.Callbacks {
    private final String TAG = "Map Activity";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    GPSTracker gps;
    private Location location_current;
    double l_1 = 0;
    double l_2 = 0;
    private String url ="https://glacial-springs-4597.herokuapp.com/";
    String imei;
    Marker ownMarker;

    String fb_user_id;
    Bitmap pf_pic;
    private CallbackManager callbackManager;
    AccessToken accesstoken=AccessToken.getCurrentAccessToken();
    private AccelerometerSensor accelerationSensor;

    PopupWindow popupWindow;
    ConnectionManager mConn;

    //set the default number of words for small display
    int len = 20;

    //For same latitude and longitude adjustment
    int max=20;

    boolean hasPop = false;

    List<PostInfo> posts = new ArrayList<>();
    HashSet<Double> Latitude = new HashSet<Double>();
    HashSet<Double> Longitude = new HashSet<Double>();
    //Used to store the post Info the user just type in
    String postInfoFull = null;
    String addressInfo = null;
    String Title = null;

    //hashmap to store the marker and post id
    HashMap<Marker, Integer> markerPostMap;
    private Integer currentId;
    private  long timeStamp;

    //final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
    Geocoder geocoder;
    private String fb_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(this.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        timeStamp = -1;
        setUpMapIfNeeded();

        geocoder = new Geocoder(this, Locale.getDefault());

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }else{
            System.out.println("mMap null");
        }

        gps = new GPSTracker(this);
        accelerationSensor = new AccelerometerSensor(getApplicationContext());

        if(gps.canGetLocation()) { // gps enabled} // return boolean true/false
            location_current = gps.getLocation();
            if(location_current != null) {
                l_1 = location_current.getLatitude();
                l_2 = location_current.getLongitude();
            }else{
                System.out.println("location null");
                l_1 = 40.4489771;
                l_2 = -79.9309191;
            }

            Latitude.add(l_1);
            Longitude.add(l_2);
            //l_1 = 40.4489771, l_2 = -79.9309191
            MarkerOptions mark = new MarkerOptions().position(new LatLng(l_1,l_2))
                    .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            String Title = "You are here";
            mark.title(Title);
            //mMap.addMarker(mark);

            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(l_1, l_2))
                    .radius(600)
                    .strokeColor(0x2000ff00)
                    .fillColor(0x2000ff00));
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(new LatLng(l_1,
                            l_2));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo((float)15);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            System.out.println(l_1);
        }

        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);



        //set click listener for button
        FloatingActionButton postNew = (FloatingActionButton) findViewById(R.id.marker_something_new);
        postNew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PostNewActivity.class);
                startActivity(intent);
            }
        });

        GraphRequest graphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */

                            Log.e(TAG, response.getRawResponse());
                        try {
                            fb_user_id = response.getJSONObject().getString("id");
                            fb_name = response.getJSONObject().getString("name");
                            new AsyncTask<Void, Void, Bitmap>()
                            {
                                @Override
                                protected Bitmap doInBackground(Void... params)
                                {
                                    Log.i(TAG,"try to get profile pic");
                                    return getFacebookProfilePicture(fb_user_id);
                                }

                                @Override
                                protected void onPostExecute(Bitmap bitmap)
                                {
                                    // safety check
                                    Log.i(TAG,"got profile pic");
                                    pf_pic = bitmap;
                                }
                            }.execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,gender,last_name,link,locale,name,timezone,updated_time,verified,age_range,friends");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
        callbackManager = CallbackManager.Factory.create();
        new GetFriendList().execute();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        System.out.println("resume");
        accelerationSensor.addListener(this);
        if(getIntent().getStringExtra("input") != null) {
            String input = getIntent().getStringExtra("input").toString();
            postInfoFull = input;
            long newTime = System.currentTimeMillis();
            if (timeStamp == -1 || (newTime - timeStamp > 60000)) {
                l_1 = gps.getLatitude();
                l_2 = gps.getLongitude();
                //test for address
                String addressLine = null;
                String city = null;
                try {
                    List<Address> addresses = geocoder.getFromLocation(l_1, l_2, 1);
                    if (addresses.size() > 0) {
                        addressLine = addresses.get(0).getAddressLine(0);
                        city = addresses.get(0).getLocality();
                        System.out.println(addressLine);
                        addressInfo = addressLine;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                MarkerOptions mark = new MarkerOptions().position(new LatLng(l_1, l_2))
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(postInfoFull).snippet(addressInfo);
                                */
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
                Canvas canvas1 = new Canvas(bmp);

// paint defines the text color,
// stroke width, size
                Paint color = new Paint();
                color.setTextSize(35);
                color.setColor(Color.BLACK);

//modify canvas
                canvas1.drawBitmap(Bitmap.createScaledBitmap(pf_pic,80,80,false), 0,0, color);
                canvas1.drawText("Jason", 30, 40, color);
                MarkerOptions mark = new MarkerOptions().position(new LatLng(l_1, l_2))
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(bmp)).title(postInfoFull).snippet(addressInfo);
                String Title = input;


                PostInfo post_new = new PostInfo(l_1, l_2, Title, "Carnegie Mellon University, Pittsburgh");
                posts.add(post_new);
                mark.title(Title);

                ownMarker = mMap.addMarker(mark);
                System.out.println(Title);
                timeStamp = newTime;
            }
        }






    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(R.layout.pop_info,null);
        TextView title = (TextView) infoWindow.findViewById(R.id.marker_title);

        String titleString = marker.getTitle();
        //When click the marker, remember the postInfo for toast display
        postInfoFull = titleString;
        addressInfo = marker.getSnippet();
        //If the text exceeds the default length, break it
        String[] words = titleString.split(" ");
        if (words.length > len) {
            titleString = "";
            for (int i = 0; i < len; i++) {
                titleString += words[i] + " ";
            }
            titleString += "......>>";
            //snippet.setText("Tab to read more");

        }
        if (TextUtils.isEmpty(titleString)) {
            titleString = "no title";
        }

        title.setText(titleString);


        return infoWindow;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        /*
        Toast.makeText(getApplicationContext(), postInfoFull,
                Toast.LENGTH_LONG).show();
        */
        System.out.println("marker");
        //currentId = markerPostMap.get(marker);
        if (popupWindow == null) {
            View infoWindow = getLayoutInflater().inflate(R.layout.postinfo_activity, null);
            TextView address = (TextView) infoWindow.findViewById(R.id.address);
            /*
            ProfilePictureView profilePictureView;
            profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
            profilePictureView.setProfileId(fb_user_id);
            */
            ImageView pic = (ImageView) infoWindow.findViewById(R.id.imageView1);
            pic.setImageBitmap(pf_pic);
            address.setText(fb_name);
            TextView title = (TextView) infoWindow.findViewById(R.id.show_something);
            title.setText(postInfoFull);
            popupWindow = new PopupWindow(infoWindow);
            //popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            popupWindow.setAnimationStyle(R.style.PopupAnimation);
            //popupWindow.showAtLocation(findViewById(R.id.map), Gravity.CENTER_VERTICAL, 200, 200);
            popupWindow.showAsDropDown(infoWindow,720,720);


        }
        else {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
                return;
            }
        }


/*
        TextView textView = new TextView(this);
        textView.setText("This is a toast");
*/
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.activity_main, menu);

        inflater.inflate(R.menu.menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, Preferences.class);
                startActivity(intent);
                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onMoveChanged(boolean move, double value) {
        Log.e(TAG, "shake value: "+value);
    }

    @Override
    public void onMoveAccuracyChanged(int accuracy) {

    }

    public static Bitmap getFacebookProfilePicture(String userID){
        Bitmap bitmap = null;
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");

                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }
    @Override
    protected void onStart() {
        super.onStart();

        accelerationSensor.start();

    }
    @Override
    protected void onStop() {

        accelerationSensor.stop();

        super.onStop();
    }
    @Override
    protected void onPause(){
        super.onPause();
        accelerationSensor.removeListener(this);
    }

    class GetFriendList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "Working in background...");
            //LoginManager.getInstance().logInWithReadPermissions(FriendList.this, Arrays.asList("user_friends"));
            //Log.i(TAG,"Having token for: "+String.valueOf(AccessToken.getCurrentAccessToken().getPermissions()));

            GraphRequestBatch batch = new GraphRequestBatch(
                    GraphRequest.newMyFriendsRequest(accesstoken,
                            new GraphRequest.GraphJSONArrayCallback() {
                                @Override
                                public void onCompleted(JSONArray jarray,
                                                        GraphResponse response) {
                                    Log.i(TAG, "onCompleted: jsonArray "
                                            + jarray);
                                    Log.i(TAG, "onCompleted: response "
                                            + response);
                                    Log.i(TAG, "firends "+response.getRawResponse());

                                }
                            }));
            batch.addCallback(new GraphRequestBatch.Callback() {

                @Override
                public void onBatchCompleted(GraphRequestBatch batch) {
                    Log.i(TAG, "onbatchCompleted: jsonArray "
                            + batch);

                }
            });
            batch.executeAsync();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {

            Log.i(TAG, "Starting...");
            super.onPreExecute();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
