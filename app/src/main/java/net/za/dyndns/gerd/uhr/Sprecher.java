package net.za.dyndns.gerd.uhr;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by hanno on 2015-08-03 20:41.
 */
class Sprecher implements MediaPlayer.OnCompletionListener {
  private Context context;
  int[] ansage;
  int currentTrack = 0;
  private MediaPlayer mePl = null;

  Sprecher(Context context) {
    this.context = context;
  }

  @Override
  public void onCompletion(MediaPlayer arg0) {
    arg0.release();
    if (currentTrack < ansage.length) {
      arg0 = MediaPlayer.create(context, ansage[currentTrack++]);
      arg0.setOnCompletionListener(this);
      arg0.start();
    }
  }

  void sprich(int[] tracks) {
    this.ansage = tracks;
    mePl = MediaPlayer.create(context, tracks[currentTrack++]);
    mePl.setOnCompletionListener(this);
    mePl.start();
  }
//W/MediaPlayer-JNI﹕ MediaPlayer finalized without being released
}

