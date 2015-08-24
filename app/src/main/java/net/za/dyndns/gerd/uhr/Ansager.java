package net.za.dyndns.gerd.uhr;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hanno on 2015-08-03 21:09.
 */
public class Ansager {
  private int[] R_array_0_60;
  private int[] R_array_viertel;
  private int[] R_array_0_12;
  private Context context;
  private TextView zeitKomplettView;
  private TextView inWortenView;
  private TextView heuteView;
  private TextView stundeView;
  private TextView zeitzoneView;
  private TextView einstellungenView;
  private TextView textViertel;
  private TextView textMinute;
  private TextView textKuckuck;
  int debug = 1;

  Ansager(Context context,
          TextView zeitKomplettView,
          TextView heuteView,
          TextView stundeView,
          TextView zeitzoneView,
          TextView einstellungenView,
          TextView inWortenView,
          TextView textViertel,
          TextView textMinute,
          TextView textKuckuck
  ) { // }, WebView webview) {
    this.context = context;
    this.zeitKomplettView = zeitKomplettView;
    this.inWortenView = inWortenView;
    this.heuteView = heuteView;
    this.stundeView = stundeView;
    this.zeitzoneView = zeitzoneView;
    this.einstellungenView = einstellungenView;
    this.textViertel = textViertel;
    this.textMinute = textMinute;
    this.textKuckuck = textKuckuck;
    // Lade die Tonaufnahmen für die Ansagen
    R_array_0_60 = holeArray(R.array.zahl_0_60);
    R_array_0_12 = holeArray(R.array.zahl_0_12);
    R_array_viertel = holeArray(R.array.viertel);

  }

  private int[] holeArray(int zahl_0_60) {
    // Lade die Tonaufnahmen für die Ansagen
    TypedArray mp3minuten = context.getResources().obtainTypedArray(zahl_0_60);
    int[] r_array_0_60 = new int[mp3minuten.length()];
    for (int ii = 0; ii < mp3minuten.length(); ii++) {
      r_array_0_60[ii] = mp3minuten.getResourceId(ii, 0);
    }
    mp3minuten.recycle();
    return r_array_0_60;
  }

  public void zeigeZeit() {
    //zeigeZeit(false, false);
    zeigeZeit(false, true, true, true, true);
  }

  public void zeigeZeit(boolean laut, boolean einMal,
                        boolean jedeMinute,
                        boolean jedeViertelstunde,
                        boolean kuckuckUndGong) {

// http://developer.android.com/reference/java/text/SimpleDateFormat.html

    Date nun = new Date();
    SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SSSZ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
    String zeitKomplett;
    zeitKomplett = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").format(nun);
    String heute = new SimpleDateFormat("yyyy-MM-dd").format(nun);
    String jetzt = new SimpleDateFormat("HH:mm:ss").format(nun);
    String zeitzone = new SimpleDateFormat("zzzz").format(nun);
    String wochentagsname = new SimpleDateFormat("EEEE").format(nun);
    String monatsname = new SimpleDateFormat("MMMM").format(nun);
    if (debug > 1) Log.i("U010", String.format("Tick %s", zeitKomplett));
    Toast.makeText(context, zeitKomplett, Toast.LENGTH_LONG).show();

    textMinute.setText(
      jedeMinute
        ? context.getString(R.string.sagJedeMinute)
        : context.getString(R.string.sagNichtJedeMinute)
    );
    textViertel.setText(
      jedeViertelstunde
        ? context.getString(R.string.sagJedeViertelstunde)
        : context.getString(R.string.sagNichtJedeViertelstunde)
    );
    textKuckuck.setText(
      kuckuckUndGong
        ? context.getString(R.string.mitKuckuck)
        : context.getString(R.string.ohneKuckuck)
    );
    /*
    inWortenView.setText("");
    inWortenView.append(Html.fromHtml(String.format(
      "<h2>%s</h2><h3>%s</h3><h3>%s</h3><h3>%s</h3><h3>%s</h3><br />",
      "Zeitansage", zeitKomplett, heute, jetzt, zeitzone
    )));
    inWortenView.append(String.format("%s\n%s\n%s\n",
      jedeMinute ? context.getString(R.string.sagJedeMinute) : context.getString(R.string.sagNichtJedeMinute),
      jedeViertelstunde ? context.getString(R.string.sagJedeViertelstunde) : context.getString(R.string.sagNichtJedeViertelstunde),
      kuckuckUndGong ? context.getString(R.string.mitKuckuck) : context.getString(R.string.ohneKuckuck)
    ));

    heuteView.setText(Html.fromHtml(String.format("<h3>%s</h3>", heute)));
    stundeView.setText(Html.fromHtml(String.format("<h3>%s</h3>", jetzt)));
    zeitzoneView.setText(Html.fromHtml(String.format("<h3>%s</h3>", zeitzone)));
    */

    int tagesnummer = Integer.valueOf(new SimpleDateFormat("d").format(nun));
    int wochennummer = Integer.valueOf(new SimpleDateFormat("w").format(nun));
    zeitKomplettView.setText(String.format("%s", zeitKomplett));
    heuteView.setText(String.format("%s", heute));
    inWortenView.setText(String.format("%s %s.%s %d.Woche", wochentagsname, tagesnummer, monatsname, wochennummer));
    stundeView.setText(String.format("%s", jetzt));
    zeitzoneView.setText(String.format("%s", zeitzone));
    einstellungenView.setText(String.format("%s\n%s\n%s\n",
      jedeMinute
        ? context.getString(R.string.sagJedeMinute)
        : context.getString(R.string.sagNichtJedeMinute),
      jedeViertelstunde
        ? context.getString(R.string.sagJedeViertelstunde)
        : context.getString(R.string.sagNichtJedeViertelstunde),
      kuckuckUndGong
        ? context.getString(R.string.mitKuckuck)
        : context.getString(R.string.ohneKuckuck)
    ));

    int minute = Integer.parseInt(new SimpleDateFormat("m").format(nun));

    Integer std_1_12 = new Integer(new SimpleDateFormat("h").format(nun));
    Integer std_0_23 = new Integer(new SimpleDateFormat("H").format(nun));
    Integer sekunde = new Integer(new SimpleDateFormat("s").format(nun));

    int viertel_0_3 = minute / 15 % 4;
    /*                                                  stundenansage    kuckuck
    ----------------------------------------------------minute +45 /60 (v+3)%4+1
    10:00 bis 10:14 : Ansage "genau 10"        std_1_12      0  45   0  0 3  3 4
    10:15 bis 10:29 : Ansage "viertel 11"      std_1_12 + 1 15  60   1  1 4  0 1
    10:30 bis 10:49 : Ansage "halb 11"         std_1_12 + 1 30  75   1  2 5  1 2
    10:45 bis 10:59 : Ansage "dreiviertel 11"  std_1_12 + 1 45  90   1  3 6  2 3
    *
    ----------------------------------------------------minute +30 /60
    10:00 bis 10:14 : Ansage "genau 10"        std_1_12      0  30   0
    10:15 bis 10:29 : Ansage "viertel nach 10" std_1_12     15  45   0
    10:30 bis 10:49 : Ansage "halb 11"         std_1_12 + 1 30  60   1
    10:45 bis 10:59 : Ansage "viertel vor 11"  std_1_12 + 1 45  75   1
    *
    * */
    int nächste_stunde = (std_1_12 + (minute + 45) / 60); // % 12;

    //if (nächste_stunde == 0) nächste_stunde = 12;
    if (nächste_stunde == 13) nächste_stunde = 1;

    int kuckucksrufe = ((viertel_0_3 + 3) % 4) + 1;

    //Log.i("U020", String.format("std_1_12 %x Viertel %x", R_array_0_12[std_1_12], R_array_viertel[viertel_0_3]));
    Log.i("U020", String.format(
      "%02d:%02d:%02d std12=%d nächste=%d viertel_0_3=%d kuckuck=%d",
      std_0_23, minute, sekunde, std_1_12, nächste_stunde, viertel_0_3, kuckucksrufe));

    String anzeige
      = (einMal ? "einMal" : "mehrmals") + " "
      + (laut ? "laut" : "leise") + " "
      + (jedeMinute ? "minuteJa" : "minuteNein") + " "
      + (jedeViertelstunde ? "viertelJa" : "viertelNein") + " "
      + (kuckuckUndGong ? "kuckuckJa" : "KuckuckNein");

    Log.i("U023", "Was tun ? " + anzeige);

    if (!laut) return;

    if ((!einMal && !jedeMinute && (minute % 15 != 0))) {
      return;
    }
    ArrayList<Integer> liste = new ArrayList<>();
    if (jedeMinute) {
      liste.add(R_array_0_60[std_0_23]);
      liste.add(R_array_0_60[61]);
      liste.add(R_array_0_60[minute]);
      //liste.add(new Integer(R_array_0_60[66]));
      boolean mitSekunde = true;
      if (mitSekunde) {
        liste.add(R_array_0_60[sekunde]);
      }
    }

    if (jedeViertelstunde) {
      liste.add(R_array_viertel[viertel_0_3]);
      liste.add(R_array_0_12[nächste_stunde]);
    }

    if (kuckuckUndGong) {
      for (int ii = 0; ii < kuckucksrufe; ii++) {
        liste.add(R.raw.cuckoo);
      }
      if (viertel_0_3 == 0) {
        for (int ii = 1; ii < std_1_12; ii++) {
          liste.add(R.raw.go);
        }
        liste.add(R.raw.gong);
      }
    }

    int schnipsel[] = new int[liste.size()];
    for (int ii = 0; ii < liste.size(); ii++) {
      schnipsel[ii] = liste.get(ii);
    }
    if (debug > 8) {
      String alleSchnipsel = "";
      for (int einSchnipsel : schnipsel) {
        alleSchnipsel += String.format("%x ", einSchnipsel);
      }

      //Log.i("U030", String.format("schnipsel %s", Arrays.toString(schnipsel)));
      Log.i("U030", String.format("schnipsel %s", alleSchnipsel));
    }
    Sprecher sprecher = new Sprecher(context);
    //sprecher.sprich(R.raw.genau, R.raw.dreiviertel, R.raw.gong);
    //sprecher.sprich(new int[]{R_array_viertel[viertel_0_3], R_array_0_12[std_1_12], R.raw.cuckoo, R.raw.gong});
    sprecher.sprich(schnipsel);
  }

}
