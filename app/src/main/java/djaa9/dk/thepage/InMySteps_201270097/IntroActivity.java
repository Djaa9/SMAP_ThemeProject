package djaa9.dk.thepage.InMySteps_201270097;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.Profile;


public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //Get currently logged in facebook profile
        Profile profile = Profile.getCurrentProfile();

        //Set welcome message to include name of currently logged in facebook profile
        TextView welcomeMessage = (TextView) findViewById(R.id.welcome_message);
        welcomeMessage.setText("Velkommen " + profile.getFirstName());
    }

    public void StartWalk(View view) {
        //Start new activity
        Intent intent = new Intent(this, WalkingActivity.class);
        startActivity(intent);
    }
}
