package com.picovr.vr.ui.xview;

import org.rajawali3d.primitives.Plane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhc on 16-7-14.
 */
public class XViewGroup extends XView {

    protected List<XView> mXViews;
//    protected Plane mPlaneContainer;

    public XViewGroup() {
        mXViews = new ArrayList<>();
//        mPlaneContainer = new Plane();
        mBasePlane.isContainer(true);
    }

    public void addView(XView view) {
        mXViews.add(view);
        mBasePlane.addChild(view.getBasePlane());
    }

    public void addViews(List<XView> views) {
        for (XView v : views) {
            addView(v);
        }
    }

    public void removeView(XView view) {
        mXViews.remove(view);
        mBasePlane.removeChild(view.getBasePlane());
    }

    public void removeAllViews() {
        mXViews.clear();
        mBasePlane = new Plane();
    }

    @Override
    public void onMeasure() {
        super.onMeasure();
        for (XView view : mXViews) {
            view.onMeasure();
        }
    }

    @Override
    public void onDraw() {
        super.onDraw();
        for (XView view : mXViews) {
            view.onDraw();
        }
    }
}
