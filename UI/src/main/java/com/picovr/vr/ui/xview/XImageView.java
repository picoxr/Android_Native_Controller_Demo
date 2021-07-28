package com.picovr.vr.ui.xview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.picovr.vr.ui.PVRRenderer;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.materials.textures.TextureManager;

/**
 * Created by jeffrey.liu on 2017/6/26.
 */

public class XImageView extends XView {

    private Material mMaterial;
    private Texture mTexture;
    private String mImageName;

    private Bitmap mBitmap;
    private int mLastResId;

    private float mWidth;
    private float mHeight;

    public XImageView(String imageName) {
        mImageName = imageName;
        init();
    }

    public XImageView(String imageName, float width, float height, int segmentsW, int segmentsH) {
        super(width, height, segmentsW, segmentsH);
        mImageName = imageName;
        mWidth = width;
        mHeight = height;
        init();
        //mBasePlane.setScale(width,height,1.0f);
    }

    private void init() {
        mBasePlane.setTransparent(true);
        mMaterial = new Material();
        mMaterial.setColorInfluence(0);
        mBasePlane.setMaterial(mMaterial);
    }

    public void setImage(Context context, int resId) throws ATexture.TextureException {
        mBitmap = BitmapFactory.decodeResource(
                TextureManager.getInstance().getContext().getResources(),
                resId);
        mTexture = new Texture(mImageName, mBitmap);
        mMaterial.addTexture(mTexture);
        mLastResId = resId;
    }

    public void setImage(Bitmap bitmap) throws ATexture.TextureException {
        mTexture = new Texture(mImageName, bitmap);
        mMaterial.addTexture(mTexture);
        mLastResId = -1;
    }


    public void updateImage(Context context, PVRRenderer renderer, int resId) {
        if (mLastResId == resId) {
            return;
        }
        mLastResId = resId;
        if (mTexture != null) {
            mMaterial.removeTexture(mTexture);
            mTexture.shouldRecycle(true);
            mBitmap.recycle();
            System.gc();
        }
        mBitmap = BitmapFactory.decodeResource(
                TextureManager.getInstance().getContext().getResources(),
                resId);
        mTexture = new Texture(mImageName, mBitmap);
        try {
            mMaterial.addTexture(mTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
//        delayVisible(10);
    }

    @Override
    public void destroy() {
        if (mMaterial != null && mTexture != null) {
            mMaterial.removeTexture(mTexture);
            TextureManager.getInstance().removeTexture(mTexture);
            mTexture.shouldRecycle(true);
            mTexture = null;
        }
        super.destroy();
        System.gc();
    }
}
