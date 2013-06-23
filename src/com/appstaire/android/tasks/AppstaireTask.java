package com.appstaire.android.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appstaire.android.interfaces.TaskListener;
import com.appstaire.android.utils.Error;
import com.appstaire.android.utils.RestClient;
import com.appstaire.android.utils.RestClient.RequestMethod;
import com.appstaire.models.Playlist;

import android.os.AsyncTask;

public class AppstaireTask extends AsyncTask<String, Void, String> {
  private RestClient client;
  private RequestMethod requestMethod = RequestMethod.GET;
  private TaskListener.OnTaskProgress onTaskProgress;
  private TaskListener.OnTaskCompleted onTaskCompleted;
  private TaskListener.OnTaskError onTaskError;
  private TaskListener.OnTaskCancelled onTaskCancelled;

  @Override
  protected String doInBackground(String... urls) {
    try{
      return loadFromNetwork(urls[0]);
    }
    catch (Exception e) {
      if (onTaskError != null) {
        onTaskError.run(new Error("network_error", "An error occured while connecting to the server: " + e.getMessage()));
      }
      return null;
    }
  }
  
  @Override
  protected void onProgressUpdate(Void... values) {
    onTaskProgress.run();
  }
  
  @Override
  protected void onCancelled() {
    if (onTaskCancelled != null) {
      onTaskCancelled.run();
    }
  }
  
  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);
    
    try {
      if (client.getResponseCode() == 200 && !result.equals(null)) {
        if (result.startsWith("[{")) {
          ArrayList<Playlist> playlists = Playlist.fromJSON(new JSONArray(result));
          onTaskCompleted.run(playlists);
        }
        else {
          if (result.contains("\"error\":")) {
            JSONObject message = new JSONObject(result);
            onTaskError.run(new Error("api_error", message.getString("error")));
          }
        }
      }
      else {
        if (client.getErrorMessage() != null) {
          onTaskError.run(new Error("internal_error", client.getErrorMessage()));
        }
      }
    } catch (JSONException jsone) {
      if (onTaskError != null) {
        onTaskError.run(new Error("json_error", "An error occured while fetching the data: " + jsone.getMessage()));
      }
    } catch (Exception e) {
      if (onTaskError != null) {
        onTaskError.run(new Error("network_error", "An error occured while connecting to the server: " + e.getMessage()));
      }
    }
  }
  
  public void setRequestMethod(RequestMethod requestMethod) {
    this.requestMethod = requestMethod;
  }
  
  public void setOnTaskCompleted(TaskListener.OnTaskCompleted onTaskCompleted) {
    this.onTaskCompleted = onTaskCompleted;
  }
  
  public void setOnTaskError(TaskListener.OnTaskError onTaskError) {
    this.onTaskError = onTaskError;
  }
  
  public void setOnTaskCancelled(TaskListener.OnTaskCancelled onTaskCancelled) {
    this.onTaskCancelled = onTaskCancelled;
  }
  
  private String loadFromNetwork(String url) throws Exception {
    client = new RestClient(url);
    client.Execute(requestMethod);
    
    return client.getResponse();
  }

}
