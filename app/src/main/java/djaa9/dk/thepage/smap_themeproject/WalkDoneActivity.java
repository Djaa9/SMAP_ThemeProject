package djaa9.dk.thepage.smap_themeproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.InputStream;
import java.net.URL;


public class WalkDoneActivity extends Activity {

    private CallbackManager facebookCallbackManager;

    private ShareDialog facebookShareDialog;

    private String distanceTraveled;
    private String donationAmount;
    private String routeMapUrl;

    private FacebookCallback<Sharer.Result> facebookShareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            //Successfully shared - currently not handled
        }

        @Override
        public void onCancel() {
            //Currently not handled
        }

        @Override
        public void onError(FacebookException e) {
            //Currently not handled
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        //Set content view
        setContentView(R.layout.activity_walk_done);

        //Get data from walk session from intent
        Bundle extras = getIntent().getExtras();
        distanceTraveled = String.format("%.2f", extras.getDouble(getPackageName() + ".DISTANCE_TRAVELED"));
        donationAmount = String.format("%.2f", extras.getDouble(getPackageName() + ".DONATION_AMOUNT"));
        routeMapUrl = extras.getString(getPackageName() + ".ROUTE_MAP_URL");

        //Get current Profile
        Profile profile = getCurrentProfile();

        //Save walk stats to the database
        SaveWalk(profile);

        //Set on screen message based on the data from the walk
        TextView walkMessage = (TextView) findViewById(R.id.walk_message);
        walkMessage.setText("Flot klaret " + profile.getFirstName() + "! Din tur har bidraget med " + donationAmount +
                " kroner til forskning mod kræft!");


        //Set map image to map from url (uses another thread)
        LoadMapFromUrlTask loadMapFromUrlTask = new LoadMapFromUrlTask();
        loadMapFromUrlTask.execute();

        //Initialize callback manager for facebook share functionality
        facebookCallbackManager = CallbackManager.Factory.create();

        //Handle share dialog
        facebookShareDialog = new ShareDialog(this);
        facebookShareDialog.registerCallback(facebookCallbackManager, facebookShareCallback);

    }

    private class LoadMapFromUrlTask extends AsyncTask<String, Void, Drawable> {


        @Override
        protected Drawable doInBackground(String... params) {
            try {
                //(Runs on another thread)
                //Get image from url.
                InputStream stream = (InputStream) new URL(routeMapUrl).getContent();
                Drawable mapDrawable = Drawable.createFromStream(stream, "src");

                return mapDrawable;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            //(Runs on main thread)
            //Find ImageView and let it show the map
            ImageView mapImage = (ImageView) findViewById(R.id.map_image);
            mapImage.setImageDrawable(drawable);
        }
    }

    public void ShareContent(View v){

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(getString(R.string.facebook_share_title))
                    .setContentDescription(
                            "Jeg har lige gået " + distanceTraveled +
                                    " kilometer og derved gratis doneret " + donationAmount +
                                    " kroner til forskning mod kræft. " +
                                    "Du kan gøre det samme! PS. Cancer er ikke spor sjovt :(")
                    .setImageUrl(Uri.parse(routeMapUrl))
                    .setContentUrl(Uri.parse(getString(R.string.facebook_share_link)))
                    .build();

            facebookShareDialog.show(linkContent);
        }
    };

    public Profile getCurrentProfile(){

        //Get current facebook profile
        return Profile.getCurrentProfile();
    };

    public void SaveWalk(Profile profile){
        //Save data with data with: profile.getId(); distanceTraveled; donationAmount;
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
