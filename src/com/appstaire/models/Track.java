package com.appstaire.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appstaire.android.utils.Utils;

public class Track implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public Integer id;
  public String title;
  public String artist;
  public Integer duration;
  public String url;
  public String youtube_id;
  public boolean loved;
  
  public boolean isPlayable() {
    return this.url.length() > 0;
  }
  
  public String getDuration() {
    return Utils.getDuration(this.duration);
  }
  
  public static Track fromJSON(JSONObject jsonItem) throws JSONException {
    Track item = new Track();
    
    item.id = jsonItem.optInt("id");
    item.title = jsonItem.getString("title");
    item.artist = jsonItem.getString("artist");
    item.duration = jsonItem.getInt("duration");
    item.url = jsonItem.getString("url");
    item.youtube_id = jsonItem.getString("youtube_id");
    if (jsonItem.has("loved") && !jsonItem.isNull("loved")) {
      item.loved = jsonItem.getBoolean("loved");
    }
    
    return item;
  }
  
  public static ArrayList<Track> fromJSON(JSONArray jsonCollection) throws JSONException {
    ArrayList<Track> collection = new ArrayList<Track>();
    
    int count = jsonCollection.length(), i = 0;
    
    for(i = 0; i < count; i++) {
      Track item = Track.fromJSON((JSONObject) jsonCollection.get(i));
      collection.add(item);
    }
    
    return collection;
  }

}
