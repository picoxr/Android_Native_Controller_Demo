package com.picovr.vr.ui.view;

import org.rajawali3d.primitives.Plane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhc on 16-7-14.
 */
public class ViewGroup extends View {

    protected List<View> mViews;
//    protected Plane mPlaneContainer;

    public ViewGroup() {
        mViews = new ArrayList<>();
//        mPlaneContainer = new Plane();
        mBasePlane.isContainer(true);
    }

    public void addView(View view) {
        mViews.add(view);
        mBasePlane.addChild(view.getBasePlane());
    }

    public void addViews(List<View> views) {
        for (View v : views) {
            addView(v);
        }
    }

    public void removeView(View view) {
        mViews.remove(view);
        mBasePlane.removeChild(view.getBasePlane());
    }

    public void removeAllViews() {
        mViews.clear();
        mBasePlane = new Plane();
    }

    @Override
    public void onMeasure() {
        super.onMeasure();
        for (View view : mViews) {
            view.onMeasure();
        }
    }

    @Override
    public void onDraw() {
        super.onDraw();
        for (View view : mViews) {
            view.onDraw();
        }
    }
}
