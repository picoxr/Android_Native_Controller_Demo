/**
 * Copyright 2015 Dennis Ippel
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.rajawali3d.vr;

import android.os.Bundle;
import android.util.Log;

import com.picovr.vractivity.Eye;
import com.picovr.vractivity.HmdState;
import com.picovr.vractivity.RenderInterface;

import org.rajawali3d.view.ISurface;
import org.rajawali3d.vr.renderer.VRRenderer;

/**
 * @author dennis.ippel
 */
public class VRActivity extends com.picovr.vractivity.VRActivity implements RenderInterface {

	private static final String TAG = "VRActivityjohn";
	private VRRenderer mRenderer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void setRenderer(VRRenderer renderer) {
	    mRenderer = renderer;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onFrameBegin(HmdState hmdState) {
		if (mRenderer!=null) {
//			Log.d(TAG, "onNewFrame: ");
			mRenderer.onNewFrame(hmdState);
		}
	}

	@Override
	public void onDrawEye(Eye eye) {
		if (mRenderer!=null) {
			if (mRenderer.getSceneInitialized()) {
//				Log.d(TAG, "onDrawEye: " + eye.getType());
				mRenderer.onDrawEye(eye);
			}
		}
	}

	@Override
	public void onFrameEnd() {
		if (mRenderer!=null) {
			mRenderer.onFinishFrame(null);
		}
	}

	@Override
	public void onTouchEvent() {
		if (mRenderer!=null) {
//			mRenderer.onTouchEvent();
		}
	}

	@Override
	public void onRenderPause() {
		if (mRenderer!=null) {
			mRenderer.onPause();
		}
	}

	@Override
	public void onRenderResume() {
		if (mRenderer!=null) {
			mRenderer.onResume();
		}
	}

	@Override
	public void onRendererShutdown() {
		if (mRenderer!=null) {
			Log.d(TAG,"onRendererShutdown()");
			mRenderer.onRenderSurfaceDestroyed(null);
		}
	}

	@Override
	public void renderEventCallBack(int i) {

	}

	protected double               mFrameRate          = 60.0;
	protected int                  mRenderMode         = ISurface.RENDERMODE_WHEN_DIRTY;
	protected ISurface.ANTI_ALIASING_CONFIG mAntiAliasingConfig = ISurface.ANTI_ALIASING_CONFIG.NONE;
	protected boolean              mIsTransparent      = false;
	protected int                  mBitsRed            = 5;
	protected int                  mBitsGreen          = 6;
	protected int                  mBitsBlue           = 5;
	protected int                  mBitsAlpha          = 0;
	protected int                  mBitsDepth          = 16;
	protected int                  mMultiSampleCount   = 0;

	@Override
	public void initGL(int i, int i1) {
		Log.d(TAG, "initGL: ");
		if ( mRenderer!=null) {
			Log.d(TAG,i+" "+i1);
			mRenderer.onRenderSurfaceCreated(null,null,i,i1);
			mRenderer.onRenderSurfaceSizeChanged(null,i,i1);
		}
//		final int glesMajorVersion = Capabilities.getGLESMajorVersion();
////		mSurfaceView.setEGLContextClientVersion(glesMajorVersion);
//
//		if (mIsTransparent) {
////			mSurfaceView.setEGLConfigChooser(new RajawaliEGLConfigChooser(glesMajorVersion, mAntiAliasingConfig, mMultiSampleCount,
////					8, 8, 8, 8, mBitsDepth));
//
//			mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//			mSurfaceView.setZOrderOnTop(true);
//		} else {
////			mSurfaceView.setEGLConfigChooser(new RajawaliEGLConfigChooser(glesMajorVersion, mAntiAliasingConfig, mMultiSampleCount,
////					mBitsRed, mBitsGreen, mBitsBlue, mBitsAlpha, mBitsDepth));
//
//			mSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
//			mSurfaceView.setZOrderOnTop(false);
//		}

	}

	@Override
	public void deInitGL() {
		
	}

	@Override
	public void surfaceChangedCallBack(int w,int h) {
			Log.d(TAG,"surfaceChangedCallBack");
		if (mRenderer != null){
//			mRenderer.onRenderSurfaceSizeChanged(null,w,h);
		}
	}

}
