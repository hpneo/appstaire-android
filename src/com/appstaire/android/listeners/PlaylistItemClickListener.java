package com.appstaire.android.listeners;

import com.appstaire.android.PlayerActivity;
import com.appstaire.models.Playlist;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

public class PlaylistItemClickListener implements AdapterView.OnItemClickListener {

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Playlist playlist = (Playlist) parent.getItemAtPosition(position);
    
    Intent intent = new Intent(parent.getContext(), PlayerActivity.class);
    intent.putExtra("playlist", playlist);
    
    parent.getContext().startActivity(intent);
  }

}
