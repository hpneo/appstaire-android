package com.appstaire.android;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.appstaire.android.adapters.PlaylistItemAdapter;
import com.appstaire.android.interfaces.TaskListener;
import com.appstaire.android.listeners.PlaylistItemClickListener;
import com.appstaire.android.tasks.AppstaireTask;
import com.appstaire.models.Playlist;
import com.appstaire.android.utils.Error;

public class MainActivity extends SherlockActivity {
  public ActionBar actionBar = null;
  public HashMap<String, String> serviceValues = new HashMap<String, String>();
  public SharedPreferences preferences;
  public ArrayList<Playlist> playlistsCollection = new ArrayList<Playlist>(); 
  public ListView playlists;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    this.preferences = getSharedPreferences("AppstairePreferences", MODE_PRIVATE);
    
    if (preferences.contains("user_token")) {
      setContentView(R.layout.activity_main);
      
      playlists = (ListView) findViewById(R.id.playlists);
      
      PlaylistItemAdapter playlistsAdapter = new PlaylistItemAdapter(this, R.layout.item_playlist, playlistsCollection);
      playlistsAdapter.setNotifyOnChange(true);
      
      playlists.setAdapter(playlistsAdapter);
      playlists.setOnItemClickListener(new PlaylistItemClickListener());
      
      serviceValues.put("user_token", preferences.getString("user_token", null));
    }
    else {
      Intent intent = new Intent(this, AuthActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      
      startActivity(intent);
      finish();
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected void onStart() {
    this.actionBar = this.getSupportActionBar();
    this.actionBar.setTitle("Appstaire");
    this.actionBar.setHomeButtonEnabled(true);
    this.actionBar.setDisplayShowHomeEnabled(true);
    
    this.setSupportProgressBarIndeterminateVisibility(false);
    
    AppstaireTask task = new AppstaireTask();
    
    HashMap<String, String> values = serviceValues;
    final Context appContext = getApplicationContext();
    
    task.setOnTaskCompleted(new TaskListener.OnTaskCompleted() {
      
      @Override
      public void run(Object response) {
        ArrayList<Playlist> playlists = (ArrayList<Playlist>) response;
        
        Toast.makeText(appContext, "Total playlists: " + playlists.size(), Toast.LENGTH_LONG).show();
        populatePlaylists(playlists);
      }
    });
    task.setOnTaskError(new TaskListener.OnTaskError() {

      @Override
      public void run(Error error) {
        Toast.makeText(appContext, error.message, Toast.LENGTH_LONG).show();
      }
    });
    
    task.execute(ServiceEndpoints.format(ServiceEndpoints.USER_PLAYLISTS, values));
    
    super.onStart();
  }

  private void populatePlaylists(ArrayList<Playlist> collection) {
    playlistsCollection.clear();
    playlistsCollection.addAll(collection);
    
    ((PlaylistItemAdapter) playlists.getAdapter()).notifyDataSetChanged();
  }
}
