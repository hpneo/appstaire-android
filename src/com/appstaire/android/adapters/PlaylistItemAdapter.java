package com.appstaire.android.adapters;

import java.util.ArrayList;

import com.appstaire.android.R;
import com.appstaire.models.Playlist;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlaylistItemAdapter extends ArrayAdapter<Playlist> {
  private ArrayList<Playlist> collection;
  
  public PlaylistItemAdapter(Context context, int layoutResourceId, ArrayList<Playlist> baseCollection) {
    super(context, layoutResourceId, baseCollection);
    collection = baseCollection;
  }
  
  @Override
  public View getView(int position, View view, ViewGroup parent) {
    Playlist playlist = collection.get(position);
    
    if (view == null) {
      LayoutInflater view_inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = view_inflater.inflate(R.layout.item_playlist, null);
    }
    
    TextView playlistName = (TextView) view.findViewById(R.id.item_playlist_name);
    TextView playlistTracksCounter = (TextView) view.findViewById(R.id.item_playlist_tracks_counter);
    SmartImageView playlistCover = (SmartImageView) view.findViewById(R.id.item_playlist_cover);
    
    playlistName.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Bold.ttf"));
    playlistTracksCounter.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Regular.ttf"));
    
    if (playlist != null) {
      playlistName.setText(playlist.name);
      playlistTracksCounter.setText(playlist.tracks_counter + " TRACKS");
      playlistCover.setImageUrl(playlist.image);
    }
    
    return view;
  }
}
