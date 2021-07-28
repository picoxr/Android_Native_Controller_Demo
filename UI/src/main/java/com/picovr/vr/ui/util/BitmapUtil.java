package com.picovr.vr.ui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

import org.rajawali3d.materials.textures.TextureManager;

public class BitmapUtil {

	public static Bitmap createTextView(String title, float textsize, int width, int height,
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
        } else if(textAlign == 1) {
            x = (width - textLength) / 2;
        } else if(textAlign == 2) {
            x = width - textLength;
        }
        float y = (height - textHeight) / 2;
        bitmapCanvas.drawText(title, x, y, paint);
        return bitmap;
    }

    public static Bitmap createTextViewWithBackground(Bitmap bitmap, int backgroundid) {
        Context context = TextureManager.getInstance().getContext();
//        BitmapFactory.Options bitmapScalingOptions = new BitmapFactory.Options();
//        bitmapScalingOptions.inScaled = false;
//        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), backgroundid, bitmapScalingOptions);
        Bitmap backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), backgroundid);
        int backWidth = backgroundBitmap.getWidth();
        int backHeight = backgroundBitmap.getHeight();
        int frontWidth = bitmap.getWidth();
        int frontHeight = bitmap.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(backWidth, backHeight, Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(newBitmap);
        bitmapCanvas.drawBitmap(backgroundBitmap, 0, 0, null);

        int left = (backWidth - frontWidth)/2;
        int top = (backHeight - frontHeight)/2;
        bitmapCanvas.drawBitmap(bitmap, left, top, null);
//        bitmapCanvas.save(Canvas.ALL_SAVE_FLAG);
        bitmapCanvas.save();
        bitmapCanvas.restore();
        return newBitmap;
    }

    public static Bitmap createBackgroundWithSize(int backgroundid, int w, int h) {
        Context context = TextureManager.getInstance().getContext();
        Bitmap backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), backgroundid);

        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(newBitmap);
        bitmapCanvas.drawBitmap(backgroundBitmap, 0, 0, null);

        return newBitmap;
    }
    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInsampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int caculateInsampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;

            }

        }
        return inSampleSize;
    }
}
