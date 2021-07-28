package com.picovr.vr.ui.xview;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.picovr.vr.ui.util.BitmapUtil;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.materials.textures.TextureManager;

/**
 * Created by jeffrey.liu on 2017/6/26.
 */

public class XTextView extends XView {

    private Texture mTexture = null;
    private int mTextureId;
    private String currentText = "";
    private int currentColor = Color.BLACK;
    private float currentSize = -1;
    private int currentWidth = -1;
    private int currentHeight = -1;
    private int currentAlign = 0;

    private Material mTextMaterial;

    private Bitmap mDrawBitmap;

//    private Texture oldTexture;

    public XTextView(String text) {
        super("");
        Log.d("RockVRUI", "TextView");
        currentText = text;
        init();
    }

    public XTextView(String name, String text) {
        super(name);
        Log.d("RockVRUI", "TextView");
        currentText = text;
        init();
    }

    private void init() {
        mBasePlane.setTransparent(true);
        mTextMaterial = new Material();
        mTextMaterial.setColorInfluence(0);
        mBasePlane.setMaterial(mTextMaterial);
    }

    public void setText(String text) {
        if(currentText.equals(text)){
            return;
        }
        currentText = text;
        updateTexture(false);
        delayVisible(10);
    }

    public String getText() {
        return currentText;
    }

    public void setTextSize(float size) {
        currentSize = size;
        updateTexture(true);
    }

    public void setTextColor(int color) {
        this.currentColor = color;
        updateTexture(true);
    }

    public void setTextViewWH(int width, int height) {
        this.currentWidth = width;
        this.currentHeight = height;
        updateTexture(true);
    }

    public void setTextConfig(int color, float textsize, int width, int height, int align) {
        this.currentColor = color;
        this.currentSize = textsize;
        this.currentWidth = width;
        this.currentHeight = height;
        this.currentAlign = align;
        updateTexture(true);
    }

//    private void updateTexture(boolean force) {
//        Log.d("RockVRUI", "TextView - updateTexture");
//        if (force || this.mTexture == null) {
//
//            Texture oldTexture = mTexture;
////            Bitmap oldbitmap = mDrawBitmap;
//
//            mDrawBitmap = BitmapUtil.createTextView(this.currentText, this.currentSize,
//                    this.currentWidth, this.currentHeight, this.currentColor, this.currentAlign);
//
//            mTexture = new Texture("TextView"+mName, mDrawBitmap);
//
//            if(mTextMaterial.getTextureList().size() == 0) {
//                try {
//                    mTextMaterial.addTexture(mTexture);
//                } catch (ATexture.TextureException e) {
//                    e.printStackTrace();
//                }
//                return;
//            }
//
////            if (oldbitmap != null) {
////                Log.d("RockVRUI", "TextView - oldBitMap != null, recycle it");
////                oldbitmap.recycle();
////            }
//            System.gc();
//        }
//    }


    private void updateTexture(boolean force) {
        Log.d("RockVRUI", "TextView - updateTexture - force: " + force);
        force = true;
        if (force || this.mTexture == null) {
            if(mTexture != null) {
                mTextMaterial.removeTexture(mTexture);
                mTexture.shouldRecycle(true);
                mDrawBitmap.recycle();
                System.gc();
            }

            mDrawBitmap = BitmapUtil.createTextView(this.currentText, this.currentSize,
                    this.currentWidth, this.currentHeight, this.currentColor, this.currentAlign);

            mTexture = new Texture("TextView"+mName, mDrawBitmap);
            try {
                mTextMaterial.addTexture(mTexture);
            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }
        }
    }

    public Texture getTexture() {
        return mTexture;
    }

    @Override
    public void destroy() {
        Log.d("RockVRUI", "TextView - destroy");
        if (mTexture != null) {
            TextureManager.getInstance().removeTexture(mTexture);
            mTexture.shouldRecycle(true);
            mTexture = null;
        }
        if (mDrawBitmap != null) {
            mDrawBitmap.recycle();
            mDrawBitmap = null;
        }
        System.gc();
        super.destroy();
    }
}
