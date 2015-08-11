/*
 * sudo studio.sh # sonst hat der Installer nicht die erforderlichen Schreibrechte
* >> File >> New >> New Project >> Uhr >> API 17 >> Add no Activity
* root@zoe:~$ chown -R hanno: /data6/AndroidStudioProjects/Uhr/
* hanno@zoe:~$ cp -auv  /zoe-home/zoe-hanno/android/Uhr0/uhr/src/main/java/net/za/dyndns/gerd/uhr0/uhr/
* package net.za.dyndns.gerd.uhr0.uhr
* -->  package net.za.dyndns.gerd.uhr;
* hanno@zoe:~$ cp -auv /zoe-home/zoe-hanno/android/Uhr0/uhr/src/main/res/* /data6/AndroidStudioProjects/Uhr/app/src/main/res/
* hanno@zoe:~$ cp -av  /zoe-home/zoe-hanno/android/Uhr0/uhr/src/main/res/values/strings.xml /data6/AndroidStudioProjects/Uhr/app/src/main/res/values/
* for i in hdpi mdpi xhdpi xxhdpi ; do cp -av  /zoe-home/zoe-hanno/android/Uhr0/uhr/src/main/res/drawable-$i/* /data6/AndroidStudioProjects/Uhr/app/src/main/res/mipmap-$i/ ; done
* public class MainActivity extends Activity {
* --> public class MainActivity extends AppCompatActivity {
* getActionBar geht in Version 1.1 unter android-22 nicht
* --> getSupportActionBar().setHomeButtonEnabled(true);
*
* */
package net.za.dyndns.gerd.uhr;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
  private int[] R_array_minuten;
  private int[] R_array_viertel;
  private int[] R_array_zahlen;
  private Empfänger receiver;
  private Ansager ansager;
  private int debug = 1;
  private boolean jedeMinute = false;
  private boolean jedeViertelstunde = true;
  private boolean kuckuckUndGong = true;
  private boolean einMal = false;
  private Button tast1;
  private Button tast2;
  private Button tast3;
  private Button tast4;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // enables the activity icon as a 'home' button.
    // required if "android:targetSdkVersion" > 14
        // getActionBar geht in Version 1.1 unter android-22 nicht
        getSupportActionBar().setHomeButtonEnabled(true);

    String versionName = "";
    int versionCode = 0;
    try {
      PackageInfo packageInfo;
      packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      versionName = packageInfo.versionName;
      versionCode = packageInfo.versionCode; // build.gradle hängt diesen "versionCode" an "versionName" an.
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    Date nun = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
    String anzeige = "Zeitansage "
        + versionName
        //+ versionCode
        + " von "
        + ft.format(nun);
    getSupportActionBar().setTitle(anzeige);
    Log.e("U000---------------", anzeige);

    setContentView(R.layout.activity_main); // für findViewById
    tast1 = (Button) findViewById(R.id.jedeMinute);
    tast2 = (Button) findViewById(R.id.jedeViertelstunde);
    tast3 = (Button) findViewById(R.id.einMal);
    tast4 = (Button) findViewById(R.id.kuckuck);
    TextView textViertel = (TextView) findViewById(R.id.textViertel);
    TextView textInfo = (TextView) findViewById(R.id.textInfo);
    //WebView textWeb = (WebView) findViewById(R.id.textWeb);
    TextView textMinute = (TextView) findViewById(R.id.textMinute);
    TextView textKuckuck = (TextView) findViewById(R.id.textKuckuck);

    ansager = new Ansager(this, textInfo, textViertel, textMinute, textKuckuck); //, textWeb);

  }

  class Empfänger extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      ansager.zeigeZeit(laut, einMal, jedeMinute, jedeViertelstunde, kuckuckUndGong);
    }
  }

  /*
  * Activity net.za.dyndns.gerd.uhr.uhr.MainActivity has leaked
  * IntentReceiver net.za.dyndns.gerd.uhr.uhr.MainActivity$Empfänger@41d96638
  * that was originally registered here.
  * Are you missing a call to unregisterReceiver()?
  *
  * */
  @Override
  protected void onStop() {
    this.unregisterReceiver(receiver);
    super.onStop();
  }

  boolean leise=false, laut=true;
  @Override
  protected void onStart() {
    super.onStart();

    ansager.zeigeZeit(leise, einMal, jedeMinute, jedeViertelstunde, kuckuckUndGong);

    // Register Empfänger to track minute ticks.
    IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
    receiver = new Empfänger();
    this.registerReceiver(receiver, filter);

    toggle(tast1, jedeMinute, R.string.doch, R.string.lieberNicht);
    tast1.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            jedeMinute = !jedeMinute;
            ansager.zeigeZeit(leise, einMal, jedeMinute, jedeViertelstunde, kuckuckUndGong);
            toggle(tast1, jedeMinute, R.string.doch, R.string.lieberNicht);
          }
        }
    );

    toggle(tast2, jedeViertelstunde, R.string.doch, R.string.lieberNicht);
    tast2.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            jedeViertelstunde = !jedeViertelstunde;
            ansager.zeigeZeit(leise, einMal, jedeMinute, jedeViertelstunde, kuckuckUndGong);
            toggle(tast2, jedeViertelstunde, R.string.doch, R.string.lieberNicht);
          }
        }
    );

    tast3.setText(R.string.sagDieZeitEinmal);
    tast3.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            ansager.zeigeZeit(laut, true, jedeMinute, jedeViertelstunde, kuckuckUndGong);
          }
        }
    );
    toggle(tast4, kuckuckUndGong, R.string.doch, R.string.lieberNicht);
    tast4.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            kuckuckUndGong = !kuckuckUndGong;
            ansager.zeigeZeit(leise, einMal, jedeMinute, jedeViertelstunde, kuckuckUndGong);
            toggle(tast4, kuckuckUndGong, R.string.doch, R.string.lieberNicht);
          }
        }
    );

  }
  void toggle(Button taste, boolean kriterium, int wennJa, int wennNein) {
            if (kriterium)
              taste.setText(wennNein);
            else
              taste.setText(wennJa);
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {


    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    int uniqueItemID = 1;
    menu.add(Menu.NONE, uniqueItemID, Menu.NONE, getString(R.string.programmaticallyAdded));
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {
      case android.R.id.home:
        // Berühre das DLF-Icon links oben auf dem Bildschirm
        if (debug > 1) Log.i("o030", "startActivityAfterCleanup(WahlActivity.class)");
        // ProjectsActivity is my 'home' activity
        startActivityAfterCleanup(MainActivity.class);
        return true;
      case R.id.action_settings: {
        return true;
      }
      case R.id.vorgaben: {
        return true;
      }
      default: {
        //return true;
      }
    }
    return super.onOptionsItemSelected(item);
  }

  private void startActivityAfterCleanup(Class<?> cls) {
    // für den Home-Button
    //if (projectsDao != null) projectsDao.close();
    Intent intent = new Intent(getApplicationContext(), cls);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }

}

