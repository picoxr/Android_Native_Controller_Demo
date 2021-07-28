package com.picovr.controllerDemo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.picovr.client.HbController;
import com.picovr.client.HbListener;
import com.picovr.client.HbManager;
import com.picovr.client.HbTool;
import com.picovr.cvclient.CVController;
import com.picovr.cvclient.CVControllerListener;
import com.picovr.cvclient.CVControllerManager;
import com.picovr.picovrlib.cvcontrollerclient.ControllerClient;
import com.picovr.vractivity.HmdState;

import org.rajawali3d.vr.VRActivity;

public class MainActivity extends VRActivity {
    private static final String TAG = "CVActivity";
    private int mFPS;
    private long mTime0;
    private boolean useCV;
    private MainRender mRenderer;
    // init cv hbController service
    private CVControllerManager cvManager;
    private CVController mainController;
    private CVController subController;
    private CVControllerListener cvListener = new CVControllerListener() {
        @Override
        public void onBindSuccess() {

        }

        @Override
        public void onBindFail() {
            Log.d(TAG, "bind fail");
        }

        @Override
        public void onThreadStart() {
            mainController = cvManager.getMainController();
            subController = cvManager.getSubController();
            mRenderer.setcvController(mainController, subController);
        }

        @Override
        public void onConnectStateChanged(int i, int i1) {
            Log.d(TAG, "cvController " + i + " state is " + i1);
        }

        @Override
        public void onMainControllerChanged(int i) {
            mainController = cvManager.getMainController();
            subController = cvManager.getSubController();
            mRenderer.setcvController(mainController, subController);
        }

        @Override
        public void onChannelChanged(int i, int i1) {

        }


    };

    // init hummingbird hbController service
    private HbManager hbManager;
    private HbController hbController;
    private HbListener hbListener = new HbListener() {
        @Override
        public void onConnect() {
            hbController.startUpdateThread();
        }

        @Override
        public void onDisconnect() {
            hbController.stopUpdateThread();
        }

        @Override
        public void onDataUpdate() {
        }

        @Override
        public void onReCenter() {
        }

        @Override
        public void onBindService() {
            if (hbController == null) {
                return;
            } else if (hbController.getConnectState() == HbTool.STATE_CONNECTED) {
                hbController.startUpdateThread();
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        useCV = ControllerClient.isControllerServiceExisted(getApplicationContext());

        cvManager = new CVControllerManager(this.getApplicationContext());
        cvManager.setListener(cvListener);
        mainController = cvManager.getMainController();
        subController = cvManager.getSubController();


        hbManager = new HbManager(this.getApplicationContext());
        if (!useCV) {
            hbManager.InitServices();
        }
        hbManager.setHbListener(hbListener);
        hbController = hbManager.getHbController();

        mRenderer = new MainRender(this);
        mRenderer.setcvController(mainController, subController);
        mRenderer.sethbController(hbController);
        setRenderer(mRenderer);
        Log.d(TAG, "onCreate");
    }


    @Override
    public void onResume() {
        super.onResume();
        if (useCV) {
            cvManager.bindService();
        } else {
            hbManager.Resume();
        }
        mFPS = 0;   // Add by Enoch : FPS relative
        mTime0 = System.currentTimeMillis();     // Add by Enoch : FPS relative
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        if (useCV) {
            cvManager.unbindService();
        } else {
            hbController.stopUpdateThread();
            hbManager.Pause();
        }
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onFrameBegin(HmdState hmdState) {
        float[] hmdOrientation = hmdState.getOrientation();
        float[] hmdPosition = hmdState.getPos();
        float[] hmdData = new float[7];
        hmdData[0] = hmdOrientation[0];
        hmdData[1] = hmdOrientation[1];
        hmdData[2] = hmdOrientation[2];
        hmdData[3] = hmdOrientation[3];

        hmdData[4] = hmdPosition[0];
        hmdData[5] = hmdPosition[1];
        hmdData[6] = hmdPosition[2];

        cvManager.updateControllerData(hmdData);
        super.onFrameBegin(hmdState);
    }

    @Override
    public void onFrameEnd() {
        super.onFrameEnd();
        long time1 = System.currentTimeMillis();
        if (time1 - mTime0 > 1000) {
            mFPS = mFPS * 1000 / (int) (time1 - mTime0);
            Log.d(TAG, "mFPS " + mFPS);
            mTime0 = time1;
            mFPS = 1;
        } else {
            mFPS++;
        }
    }
}
