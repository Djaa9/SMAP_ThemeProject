package djaa9.dk.thepage.InMySteps_201270097;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;


public class LoginActivity extends Activity {


    private CallbackManager facebookCallbackManager;
    private ProfileTracker facebookProfileTracker;
    private TextView loginMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        //Set content view
        setContentView(R.layout.activity_login);

        //Initialize callback manager for facebook login
        facebookCallbackManager = CallbackManager.Factory.create();

        //Find instructions textView
        loginMessage = (TextView) findViewById(R.id.login_message);

        //Set up profileTracker to track changes in current Profile
        facebookProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null)
                {
                    //Change text and go to next activity if user is logged in
                    loginMessage.setText(getString(R.string.loggedin_message) + " " + currentProfile.getFirstName());
                    NavigateToNext();
                }
                else
                {
                    //Change text
                    loginMessage.setText(getString(R.string.login_message));
                }
            }
        };

    }

    public void NavigateToNext() {
        //Start next activity
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    public void NextButtonOnClick(View view){
        //Check that user is logged in
        Profile profile = Profile.getCurrentProfile();

        //Go to next activity if user is logged in - else inform user.
        if (profile != null){
            //Start next activity
            NavigateToNext();
        }
        else
        {
            //Show toast that tells the user to log in.
            Toast.makeText(getApplicationContext(),
                    "Du skal logge ind først...",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Handle log in
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Clean up on destroy
        facebookProfileTracker.stopTracking();
    }

}
