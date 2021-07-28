package com.picovr.vr.ui.view;

import android.content.Context;
import android.util.Log;

import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhc on 16-9-6.
 */
public class SwitchImageView extends View {

    private List<ImageView> mImageList;
    private ImageView mCurShowImageView;
    private String mName;

    public SwitchImageView(String name) {
        mName = name;
        init();
    }

    public SwitchImageView(String imageName, float width, float height, int segmentsW, int segmentsH) {
        super(width, height, segmentsW, segmentsH);
        mName = imageName;
        init();
    }

    private void init() {
        mBasePlane.isContainer(true);
        mBasePlane.setTransparent(true);
        mImageList = new ArrayList<>();
    }

    public void setImages(Context contexts, int[] resids) throws ATexture.TextureException {
        int i = 0;
        for (int id : resids) {
            ImageView image = new ImageView(mName + i);
            image.setImage(contexts, id);
            mImageList.add(image);
            image.addToContainer(mBasePlane);
            i++;
        }
        if(mImageList.size() != 0) {
            mCurShowImageView = mImageList.get(0);
            switchView(0);
        }
    }

    public void switchView(int index) {
        if(mImageList.get(index) != null) {
            mCurShowImageView.setVisible(false);
            mImageList.get(index).setVisible(true);
            mCurShowImageView = mImageList.get(index);
        } else {
            Log.e("RockVRUI", "SwitchImageView - ImageList on " + index + " is null");
        }
    }
}
