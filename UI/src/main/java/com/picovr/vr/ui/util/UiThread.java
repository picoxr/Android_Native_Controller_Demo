package com.picovr.vr.ui.util;

/**
 * Created by zhisheng on 2016/6/8.
 */
public class UiThread extends Thread{

    public interface UiThreadCallback {
        public abstract void uiUpdateRun();
    }

    private UiThreadCallback mCallback;
    private boolean forceUpdate = false;

    public UiThread() {
        super();
    }

    public UiThread(Runnable runnable) {
        super(runnable);
    }

    public UiThread(Runnable runnable, String threadName) {
        super(runnable, threadName);
    }

    public UiThread(String threadName) {
        super(threadName);
    }

    public void setThreadCallback(UiThreadCallback callback) {
        mCallback = callback;
    }

    public void needForceUpdate(boolean need) {
        forceUpdate = need;
    }

    @Override
    public void run() {
        while (true) {
            if (forceUpdate) {
                mCallback.uiUpdateRun();
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
