package djaa9.dk.thepage.InMySteps_201270097;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.example.johnny.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;


import java.io.IOException;

/**
 * Created by Rahlff on 21/05/15.
 */
public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once

            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://articulate-fort-94709.appspot.com/_ah/api/");

            // end options for devappserver
            myApiService = builder.build();
        }
        context = params[0].first;
        String name = params[0].second;
        try {
            return myApiService.sayHi(name).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    @Override
    protected void onPostExecute(String result) {

    }
}