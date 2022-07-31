
package com.Mayank.MML;






import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class Splash extends Activity {
	  private static String TAG;
	  private static long SLEEP_TIME = 1; // Sleep for some time
	  ImageView image;
	  AnimationDrawable Anim;

	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_splash);

		  image = (ImageView) findViewById(R.id.imageView1);
		  IntentLauncher launcher = new IntentLauncher();
		    launcher.start();
	}
	private class IntentLauncher extends Thread {

        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME * 2000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            
            // Start main activity
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}
    // Start main activity
    

