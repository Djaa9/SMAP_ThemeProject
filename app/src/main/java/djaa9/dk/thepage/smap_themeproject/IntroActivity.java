package djaa9.dk.thepage.smap_themeproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void StartWalk(View view) {
        Intent intent = new Intent(this, WalkingActivity.class);

        startActivity(intent);
    }
}
