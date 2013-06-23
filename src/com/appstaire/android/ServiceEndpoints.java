package com.appstaire.android;

import java.util.HashMap;
import java.util.Map;

public class ServiceEndpoints {
  public static final String ROOT = "http://appstaire.com";
  public static final String USER_PLAYLISTS = ROOT + "/api/lists?auth_token={user_token}";
  public static final String PLAYLIST = ROOT + "/api/lists/{list_id}?auth_token={user_token}";
  
  public static final String SEARCH_TRACKS = ROOT + "/api/tracks/search?auth_token={user_token}";
  public static final String ADD_TRACK = ROOT + "/api/tracks/add?auth_token={user_token}&list_id={list_id}&artist={artist}&title={title}";
  
  public static final String DELETE_TRACK = ROOT + "/api/lists/{list_id}/delete_track?auth_token={user_token}&track_id={track_id}";
  
  public static final String LOVE_TRACK = ROOT + "/api/tracks/{track_id}/love.json?auth_token={user_token}";
  public static final String UNLOVE_TRACK = ROOT + "/api/tracks/{track_id}/unlove.json?auth_token={user_token}";
  
  public static String format(String source, HashMap<String, String> values) {
    String formattedString = source;
    for(Map.Entry<String, String> entry : values.entrySet()) {
      String pattern = "{" + entry.getKey() + "}";
      String replacement = entry.getValue();
      
      if (formattedString.contains(pattern)) {
        formattedString = formattedString.replace(pattern, replacement);
      }
    }
    return formattedString;
  }
}
