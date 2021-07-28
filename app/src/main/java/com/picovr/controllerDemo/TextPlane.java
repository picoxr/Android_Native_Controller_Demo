package com.picovr.controllerDemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.scene.Scene;

public class TextPlane {
    private Plane mPlane;
    private String mText;
    private Scene mScene;
    private Bitmap bitmap;
    private Texture texture;
    private Material material;

    public TextPlane(Scene parent){
        mScene = parent;

        mPlane = new Plane(2f, 1f, 2, 1);
        mPlane.setTransparent(true);
        mPlane.setPosition(0, 0, -3);
        mPlane.setLookAt(0, 0, 0);
        mPlane.setVisible(true);

        material = new Material();
        material.setColorInfluence(0);
        bitmap = createTextView("idle ", 16, 200, 100,
                Color.WHITE, 0);
        texture = new Texture("textTex", bitmap);
        try {
            material.addTexture(texture);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlane.setMaterial(material);
        mScene.addChild(mPlane);
    }

    public void update(String content) {
        mText = content;

        bitmap.recycle();

        bitmap = createTextView(mText, 16, 200, 100,
                Color.WHITE, 0);

        try {
            texture.setBitmap(bitmap);
            texture.replace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(double x,double y,double z){
        mPlane.setPosition(x,y,z);
    }

    private Bitmap createTextView(String title, float textsize, int width, int height,
                                        int color, int textAlign) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(textsize);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(false);
        paint.setFakeBoldText(true);
        paint.setLinearText(true);
        paint.setShadowLayer(0.05f, 0, 0, color);

        Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(width, height, bitmapConfig);

        float textLength = paint.measureText(title);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = (float) (Math.ceil(fm.descent - fm.ascent));
        Canvas bitmapCanvas = new Canvas(bitmap);
        float x = 0;
        if (textAlign == 0) {
            x = 0;
        } else if (textAlign == 1) {
            x = (width - textLength) / 2;
        } else if (textAlign == 2) {
            x = width - textLength;
        }
        float y = (height - textHeight) / 2;
        bitmapCanvas.drawText(title, x, y, paint);
        return bitmap;
    }

}
