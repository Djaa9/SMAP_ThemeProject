package djaa9.dk.thepage.smap_themeproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void NavigateToNext(View view) {
        Intent intent = new Intent(this, IntroActivity.class);

        startActivity(intent);
    }
}