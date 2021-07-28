package com.picovr.vr.ui.xview;

import org.rajawali3d.Object3D;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.scene.Scene;
import org.rajawali3d.vr.renderer.VRRenderer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jeffrey.liu on 2017/6/26.
 */

public class XView {

    protected Plane mBasePlane;
    protected String mName;
    //protected float mAlpha;
    protected Vector3 mPosition;
    protected Quaternion mRotation;
    protected Vector3 mScale;
    protected float mWidth;
    protected float mHeight;
    protected boolean mVisible;
    protected boolean mIsParentScene;
    protected boolean mIsNeedMeasure;
    protected boolean mIsNeedDraw;
    protected Object3D mParent;
    protected Scene mScene;
    protected XOnClickListener mOnClickListener;
    protected XOnLookingListener mOnLookingListener;


    public XView() {
        mBasePlane = new Plane();
//        mPosition = new Vector3();
//        mRotation = new Quaternion();
//        mScale = new Vector3().ONE;
    }

    public XView(String name) {
        mName = name;
        mBasePlane = new Plane();
//        mPosition = new Vector3();
//        mRotation = new Quaternion();
//        mScale = new Vector3().ONE;
        mIsNeedMeasure = true;
        mIsNeedDraw = true;
    }

    public XView(float width, float height, int segmentsW, int segmentsH) {
        mBasePlane = new Plane(width, height, segmentsW, segmentsH);
//        mPosition = new Vector3();
//        mRotation = new Quaternion();
//        mScale = new Vector3().ONE;
        mIsNeedMeasure = true;
        mIsNeedDraw = true;
    }

    public Plane getBasePlane() {
        return  mBasePlane;
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

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public Vector3 getPosition() {
        return mBasePlane.getPosition();
    }

    public void setPosition(double x, double y, double z) {
        mBasePlane.setPosition(x, y, z);
    }

    public void setPosition(Vector3 position) {
        mBasePlane.setPosition(position);
    }

    public Vector3 getScale() {
        return mBasePlane.getScale();
    }

    public void setScale(double x, double y, double z) {
        mBasePlane.setScale(x, y, z);
    }

    public void setLookingAt(double x, double y, double z) {
        mBasePlane.setLookAt(x, y, z);
    }

    public Vector3 getLookingAt(){
        return mBasePlane.getLookAt();
    }

    public Vector3 getRotation(){
        return new Vector3(mBasePlane.getRotX(),mBasePlane.getRotY(),mBasePlane.getRotZ());
    }

    public void setRotation(float x, float y, float z) {
        mBasePlane.setRotation(x, y, z);
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

    public void addToViewGroup(XViewGroup viewGroup) {
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

    public boolean getVisible() {
        return mVisible;
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
        mBasePlane.setVisible(visible);
    }

    public boolean removeFromContainer(Object3D obj) {
        return obj.removeChild(mBasePlane);
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

    private boolean mHasBeingLooking = false;

    public boolean processLookingAt(VRRenderer renderer) {
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


    public void setOnClickListener(XOnClickListener listener) {
        mOnClickListener = listener;
    }


    public void setOnLookingListener(XOnLookingListener listener) {
        mOnLookingListener = listener;
    }

}
