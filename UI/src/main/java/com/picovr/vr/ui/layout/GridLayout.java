package com.picovr.vr.ui.layout;

import com.picovr.vr.ui.view.View;
import com.picovr.vr.ui.view.ViewGroup;

import org.rajawali3d.math.vector.Vector3;

public class GridLayout extends ViewGroup {

    public final float mItemRadiusX = 10f;
    public final float mItemRadiusZ = 8f;
    public final float mItemSpace = 2f;
    public final double mItemMaxAngle = Math.PI / 5;

    public Vector3[][] calItemCoordTable() {
        Vector3[][] itemCoordTable = new Vector3[mRowNum][mColNum];
        final double eachItemAngle = mItemMaxAngle / (mColNum - 1);
        final double startItemAngle = -mItemMaxAngle / 2;
        for (int i = 0; i < mRowNum; i++) {
            for (int j = 0; j < mColNum; j++) {
                itemCoordTable[i][j] = new Vector3(
                        mItemRadiusX * Math.sin(startItemAngle + eachItemAngle * j),
                        mItemSpace * (mRowNum - i - 1),
                        -mItemRadiusZ * Math.cos(startItemAngle + eachItemAngle * j)
                );
            }
        }
        return itemCoordTable;
    }

    public Vector3[][] mItemCoordTable = calItemCoordTable();

    private int mRowNum = 4;
    private int mColNum = 2;

    public GridLayout(int rowNum, int colNum) {
        mRowNum = rowNum;
        mColNum = colNum;
    }

    @Override
    public void onMeasure() {
        for (int i = 0; i < mViews.size(); i++) {
            View view = mViews.get(i);
            int viewPosX = i % mColNum;
            int viewPosY = i / mColNum;
            view.setPosition(mItemCoordTable[viewPosY][viewPosX]);
            view.setLookingAt(0, 0, 0);
        }
        super.onMeasure();
    }

    @Override
    public void onDraw() {
        super.onDraw();
    }
}
