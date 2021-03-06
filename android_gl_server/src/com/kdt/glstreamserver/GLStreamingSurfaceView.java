/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kdt.glstreamserver;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;
import android.view.*;
import android.opengl.GLES20;

/**
 * A simple GLSurfaceView sub-class that demonstrate how to perform
 * OpenGL ES 2.0 rendering into a GL Surface. Note the following important
 * details:
 *
 * - The class must use a custom context factory to enable 2.0 rendering.
 *   See ContextFactory class definition below.
 *
 * - The class must use a custom EGLConfigChooser to be able to select
 *   an EGLConfig that supports 2.0. This is done by providing a config
 *   specification to eglChooseConfig() that has the attribute
 *   EGL10.ELG_RENDERABLE_TYPE containing the EGL_OPENGL_ES2_BIT flag
 *   set. See ConfigChooser class definition below.
 *
 * - The class must select the surface's format, then choose an EGLConfig
 *   that matches it exactly (with regards to red/green/blue/alpha channels
 *   bit depths). Failure to do so would result in an EGL_BAD_MATCH error.
 */
public class GLStreamingSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private int mServerPort = 18145;
	private String mClientAddr = "127.0.0.1";
	private int mClientPort = 18146;
	
    public GLStreamingSurfaceView(Context context) {
        this(context, null);
    }

    public GLStreamingSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	@Override
	public void surfaceCreated(SurfaceHolder p1) {
		GLStreamingJNIWrapper.initServer(mServerPort, mClientAddr, mClientPort);
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, int format, final int width, final int height)
	{
		System.out.println("Intializing GLStreamServer");
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					// System.out.println("w=" + width + ",h=" + height);
					GLStreamingJNIWrapper.setGLSize(width, height, holder.getSurface());
				}
			}, "GLStreaming").start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder p1) {
		
	}
	
    public void init(boolean translucent, int serverPort, String clientAddr, int clientPort) {
		mServerPort = serverPort;
		mClientAddr = clientAddr;
		mClientPort = clientPort;
        if (translucent) {
            this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }

		getHolder().addCallback(this);
    }

}
