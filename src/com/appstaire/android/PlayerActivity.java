package com.appstaire.android;

import java.io.IOException;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.appstaire.android.utils.Utils;
import com.appstaire.models.Playlist;
import com.loopj.android.image.SmartImageView;
// import com.google.android.youtube.player.YouTubeInitializationResult;
// import com.google.android.youtube.player.YouTubePlayer;
// import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
// import com.google.android.youtube.player.YouTubePlayerSupportFragment;
// import com.google.android.youtube.player.YouTubePlayer.Provider;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
// import android.widget.MediaController;
// import android.widget.VideoView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerActivity extends SherlockFragmentActivity
  implements OnCompletionListener, OnPreparedListener, OnBufferingUpdateListener, OnErrorListener, OnInfoListener/*YouTubePlayer.OnInitializedListener*/ {
  public ActionBar actionBar = null;
  private Playlist playlist = null;
  // private YouTubePlayerSupportFragment youTubePlayerFragment = null;
  private MediaPlayer mediaPlayer;
  private TextView currentTrackTitle;
  private TextView currentTrackElapsed;
  private TextView currentTrackDuration;
  private SmartImageView currentTrackCover;
  private SeekBar currentTrackSeeker;

  private ImageButton currentTrackPrevious;
  private ImageButton currentTrackPlay;
  private ImageButton currentTrackPause;
  private ImageButton currentTrackNext;
  
  private final Handler handler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    
    playlist = (Playlist) getIntent().getExtras().get("playlist");
    
    // youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
    // youTubePlayerFragment.setMenuVisibility(false);
    // youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
    
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    
    try {
      mediaPlayer.setDataSource(this, Uri.parse(playlist.tracks.get(0).url));
      mediaPlayer.setOnCompletionListener(this);
      mediaPlayer.setOnPreparedListener(this);
      mediaPlayer.setOnBufferingUpdateListener(this);
      mediaPlayer.prepareAsync();
    } catch (IllegalArgumentException e) {
      Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    } catch (SecurityException e) {
      Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    } catch (IllegalStateException e) {
      Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    } catch (IOException e) {
      Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    } catch (Exception e) {
      Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
    
    currentTrackTitle = (TextView) findViewById(R.id.player_current_track_title);
    currentTrackTitle.setText("");
    
    currentTrackElapsed = (TextView) findViewById(R.id.player_current_track_elapsed);
    currentTrackElapsed.setText("0:00");
    
    currentTrackDuration = (TextView) findViewById(R.id.player_current_track_duration);
    currentTrackDuration.setText("");
    
    currentTrackSeeker = (SeekBar) findViewById(R.id.player_current_track_seek);
    currentTrackSeeker.setProgress(0);
    
    currentTrackPrevious = (ImageButton) findViewById(R.id.player_current_track_controls_previous);
    currentTrackPlay = (ImageButton) findViewById(R.id.player_current_track_controls_play);
    currentTrackPause = (ImageButton) findViewById(R.id.player_current_track_controls_pause);
    currentTrackNext = (ImageButton) findViewById(R.id.player_current_track_controls_next);
    
    currentTrackCover = (SmartImageView) findViewById(R.id.player_current_track_cover);
  }
  
  @Override
  protected void onStart() {
    this.actionBar = this.getSupportActionBar();
    this.actionBar.setTitle(playlist.name);
    this.actionBar.setHomeButtonEnabled(true);
    this.actionBar.setDisplayShowHomeEnabled(true);
    
    this.setSupportProgressBarIndeterminateVisibility(false);
    
    super.onStart();
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    mp.start();
    currentTrackTitle.setText(playlist.tracks.get(0).title);
    currentTrackDuration.setText(playlist.tracks.get(0).getDuration());
    currentTrackElapsed.setText(Utils.getDuration(mp.getCurrentPosition() / 1000));
    currentTrackSeeker.setMax(playlist.tracks.get(0).duration);
    
    currentTrackPlay.setVisibility(View.GONE);
    currentTrackPause.setVisibility(View.VISIBLE);
    
    if (!playlist.image.equals(null) && !playlist.image.equals("")) {
      currentTrackCover.setImageUrl(playlist.image);
    }
    
    startPlayProgressUpdater();
  }
  
  public void startPlayProgressUpdater() {
    if (mediaPlayer != null) {
      Log.i("APPSTAIRE_DURATION", mediaPlayer.getCurrentPosition() / 1000 + "");
      currentTrackSeeker.setProgress(mediaPlayer.getCurrentPosition() / 1000);
      currentTrackElapsed.setText(Utils.getDuration(mediaPlayer.getCurrentPosition() / 1000));
      
      if (mediaPlayer.isPlaying()) {
        Runnable notification = new Runnable() {
          
          @Override
          public void run() {
            startPlayProgressUpdater();
          }
        };
        
        handler.postDelayed(notification, 1000);
      }
      else {
        pause();
      }
    }
    else {
      Toast.makeText(getBaseContext(), "mediaPlayer is null", Toast.LENGTH_SHORT).show();
    }
  }
  
  public void pause() {
    if (mediaPlayer.isPlaying()) {
      mediaPlayer.pause();

      currentTrackPlay.setVisibility(View.VISIBLE);
      currentTrackPause.setVisibility(View.GONE);
    }
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    Toast.makeText(getBaseContext(), what, Toast.LENGTH_LONG).show();
    return false;
  }

  @Override
  public boolean onInfo(MediaPlayer mp, int what, int extra) {
    Toast.makeText(getBaseContext(), what, Toast.LENGTH_LONG).show();
    return false;
  }
  
  @Override
  protected void onDestroy() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
      mediaPlayer.release();
      mediaPlayer = null;
    }
    
    super.onDestroy();
  }
  /*
  @Override
  public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
    if (!wasRestored) {
      player.setPlayerStyle(PlayerStyle.MINIMAL);
      if (playlist != null) {
        player.loadVideo(playlist.tracks.get(0).youtube_id);
        currentTrackTitle.setText(playlist.tracks.get(0).artist + " - " + playlist.tracks.get(0).title);
      }
    }
  }
  */
}
