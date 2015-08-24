package net.za.dyndns.gerd.uhr;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by hanno on 24.08.15.
 */
public final class DochOderNicht {
  private int intRessource;

  private DochOderNicht(int intRessource) {
    this.intRessource = intRessource;
  }

  public int toInt() {
    return this.intRessource;
  }

  public DochOderNicht toRes() {
    return this;
  }

  public static final DochOderNicht DOCH = new DochOderNicht(1);
  public static final DochOderNicht NICHT = new DochOderNicht(2);
  public static final DochOderNicht VIELLEICHT = new DochOderNicht(3);
}
