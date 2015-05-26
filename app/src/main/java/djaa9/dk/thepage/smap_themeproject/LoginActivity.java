package djaa9.dk.thepage.smap_themeproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoginActivity extends Activity {


    private CallbackManager facebookCallbackManager;

    //Handle callback from facebook login.
    private FacebookCallback<LoginResult> facebookLoginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            //Go to introActivity when logged in succesfully
            NavigateToNext();
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
        setContentView(R.layout.activity_login);

        //Initialize callback manager for facebook login
        facebookCallbackManager = CallbackManager.Factory.create();

        //Find Facebook login button and register callback
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(facebookCallbackManager, facebookLoginCallback);

        //Check if user is already logged in
        Profile profile = Profile.getCurrentProfile();
        if (profile != null){
            //Automaticly open the intro activity if already logged in
            NavigateToNext();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void NavigateToNext() {
        //Start next activity
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }
}
