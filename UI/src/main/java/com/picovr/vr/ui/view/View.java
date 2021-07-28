package com.picovr.vr.ui.view;


import org.rajawali3d.Object3D;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.scene.Scene;
import org.rajawali3d.vr.renderer.VRRenderer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yhc on 16-7-14.
 */
public class View {

    protected Plane mBasePlane;
    protected String mName;

    protected OnClickListener mOnClickListener;
    protected OnLookingListener mOnLookingListener;

    protected boolean mIsNeedMeasure;
    protected boolean mIsNeedDraw;

    protected Object3D mParent;
    protected boolean mIsParentScene = false;
    protected Scene mScene;

    public View() {
        mBasePlane = new Plane();
    }

    public View(String name) {
        mName = name;
        mBasePlane = new Plane();
        mIsNeedMeasure = true;
        mIsNeedDraw = true;
    }

    public View(float width, float height, int segmentsW, int segmentsH) {
        mBasePlane = new Plane(width, height, segmentsW, segmentsH);
        mIsNeedMeasure = true;
        mIsNeedDraw = true;
    }

    public void setPosition(double x, double y, double z) {
        mBasePlane.setPosition(x, y, z);
    }

    public void setPosition(Vector3 position) {
        mBasePlane.setPosition(position);
    }

    public void setScale(double x, double y, double z) {
        mBasePlane.setScale(x, y, z);
    }

    public void setLookingAt(double x, double y, double z) {
        mBasePlane.setLookAt(x, y, z);
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean ismIsNeedMeasure() {
        return mIsNeedMeasure;
    }

    public void setmIsNeedMeasure(boolean isNeed) {
        this.mIsNeedMeasure = isNeed;
    }

    public boolean ismIsNeedDraw() {
        return mIsNeedDraw;
    }

    public void setmIsNeedDraw(boolean isNeed) {
        this.mIsNeedDraw = isNeed;
    }

    public void addToScence(Scene scene) {
        mIsParentScene = true;
        scene.addChild(mBasePlane);
        mScene = scene;
    }

    public void addToViewGroup(ViewGroup viewGroup) {
        viewGroup.addView(this);
    }

    public void addToContainer(Object3D obj) {
        mIsParentScene = false;
        obj.addChild(mBasePlane);
        mParent = obj;
    }

    public void removeFromParent() {
        if(mIsParentScene) {
            mScene.removeChild(mBasePlane);
        } else {
            mParent.removeChild(mBasePlane);
        }
    }

    public void recreateBasePlane() {
        Vector3 pos = mBasePlane.getPosition();
        Vector3 scale = mBasePlane.getScale();
        removeFromParent();
        mBasePlane.destroy();
        mBasePlane = new Plane();
        if(mIsParentScene) {
            mScene.addChild(mBasePlane);
        } else {
            mParent.addChild(mBasePlane);
        }
        mBasePlane.setPosition(pos);
        mBasePlane.setScale(scale);
    }

    public void setVisible(boolean visible) {
        mBasePlane.setVisible(visible);
    }

    public boolean removeFromContainer(Object3D obj) {
        return obj.removeChild(mBasePlane);
    }

    public Plane getBasePlane() {
        return mBasePlane;
    }

    public void destroy() {
        mBasePlane.destroy();
    }

    public boolean isBeingLookAt(VRRenderer renderer) {
        return renderer.isLookingAtObject(mBasePlane);
    }

    public void delayVisible(long ms) {
        mBasePlane.setVisible(false);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            public void run(){
                mBasePlane.setVisible(true);
            }
        };
        timer.schedule(timerTask, ms);
    }

    public boolean onClick() {
        if(mOnClickListener != null) {
            return mOnClickListener.onClick(this);
        }
        return false;
    }

    private boolean mHasBeingLooking = false;
    public boolean processLooking(VRRenderer renderer) {
        boolean isBeingLooking = isBeingLookAt(renderer);
        if(!mHasBeingLooking && isBeingLooking) {
            mHasBeingLooking = true;
            mOnLookingListener.onLookingAt();
        } else if(mHasBeingLooking && !isBeingLooking){
            mHasBeingLooking = false;
            mOnLookingListener.onLookingOut();
        }
        return isBeingLooking;
    }

    public void onMeasure() {
        mIsNeedMeasure = false;
    }

    public void onDraw() {
        mIsNeedDraw = false;
    }

    public interface OnClickListener {
        boolean onClick(View view);
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public interface OnLookingListener {
        void onLookingAt();
        void onLookingOut();
    }

    public void setOnLookingListener(OnLookingListener listener) {
        mOnLookingListener = listener;
    }
}
