package com.appstaire.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Playlist implements Serializable {
  private static final long serialVersionUID = 1L;

  public Integer id;
  public String name;
  public String image;
  public String state;
  public Integer tracks_counter;
  public ArrayList<Track> tracks;
  
  public boolean isOpened() {
    return this.state.equals("opened");
  }
  
  public static Playlist fromJSON(JSONObject jsonItem) throws JSONException {
    Playlist item = new Playlist();
    
    item.id = jsonItem.getInt("id");
    item.name = jsonItem.getString("name");
    item.image = jsonItem.getString("image");
    item.state = jsonItem.getString("state");
    item.tracks_counter = jsonItem.getInt("tracks_counter");
    item.tracks = Track.fromJSON(jsonItem.getJSONArray("tracks"));
    
    return item;
  }
  
  public static ArrayList<Playlist> fromJSON(JSONArray jsonCollection) throws JSONException {
    ArrayList<Playlist> collection = new ArrayList<Playlist>();
    
    int count = jsonCollection.length(), i = 0;
    
    for(i = 0; i < count; i++) {
      Playlist item = Playlist.fromJSON((JSONObject) jsonCollection.get(i));
      collection.add(item);
    }
    
    return collection;
  }
}
