package com.appstaire.android.utils;

import com.appstaire.models.Track;

public class Utils {

  public static String getDuration(Integer duration) {
    Integer minutes = (int) Math.floor(duration /  60);
    String seconds = String.format("%2s", (duration %  60) + "").replace(' ', '0');
    
    return minutes + ":" + seconds;
  }
}
