package com.picovr.vr.ui.control;

import com.picovr.vr.ui.view.ViewGroup;

/**
 * Created by yhc on 16-7-14.
 */
public abstract class MenuItem {

    private ViewGroup mMenuItemBaseView;

    public ViewGroup getBaseView() {
        return mMenuItemBaseView;
    }

    public abstract void onMeasure();
    public abstract void onDraw();
}
