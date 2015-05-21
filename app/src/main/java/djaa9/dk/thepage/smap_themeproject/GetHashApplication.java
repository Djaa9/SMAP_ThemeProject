package djaa9.dk.thepage.smap_themeproject;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
This java class application is only used to get the unique hash key of the app, as this key is
required to be entered to the facebook developer page of the app.
This application is only used during development and is removed from the manifest in the final app.
 */
public class GetHashApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        printHashKey();
    }

    public void printHashKey(){

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "djaa9.dk.thepage.smap_themeproject",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

}


