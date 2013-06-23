package com.appstaire.android.interfaces;

public interface TaskListener {
  public abstract class OnTaskProgress {
    public abstract void run(Object... progresses);
  }
  public abstract class OnTaskCompleted {
    public abstract void run(Object response);
  }
  
  public abstract class OnTaskError {
    public abstract void run(com.appstaire.android.utils.Error error);
  }
  
  public abstract class OnTaskCancelled {
    public abstract void run();
  }
  
  void onTaskCompleted(Object result);
  void onTaskError(Object result);
  void onTaskCancelled(Object result);
}
