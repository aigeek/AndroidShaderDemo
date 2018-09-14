package com.andev.androidshaderdemo.activity;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.render.MultiTextureRender;
import com.andev.androidshaderdemo.render.SimpleRender;
import com.andev.androidshaderdemo.render.SimpleTextureRender;
import com.andev.androidshaderdemo.render.TwoFilterRender;
import com.andev.androidshaderdemo.render.TwoTextureRender;


public class FilterActivity extends AppCompatActivity {
	private int type;

	private GLSurfaceView glSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_layout);

		type = getIntent().getIntExtra("type", 0);

		glSurfaceView = (GLSurfaceView)findViewById(R.id.glsurfaceView);

		ActivityManager activityManager =
				(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager
				.getDeviceConfigurationInfo();
		final boolean supportsEs2 =
				configurationInfo.reqGlEsVersion >= 0x20000
						|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
						&& (Build.FINGERPRINT.startsWith("generic")
						|| Build.FINGERPRINT.startsWith("unknown")
						|| Build.MODEL.contains("google_sdk")
						|| Build.MODEL.contains("Emulator")
						|| Build.MODEL.contains("Android SDK built for x86")));

		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			glSurfaceView.setEGLContextClientVersion(2);
			initRender();
		}else {
			Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
					Toast.LENGTH_LONG).show();
		}
	}

	private void initRender(){
		if(type == 0){
			setSimpleRender();
		}else if(type == 1){
			setSampleTextureRender();
		}else if(type == 2){
			setMultiTextureRender();
		}else if(type == 3){
			setTwoTextureRender();
		}else{
			setTwoFilterRender();
		}
	}

	private void setSimpleRender(){
		SimpleRender simpleRender = new SimpleRender(this);
		glSurfaceView.setRenderer(simpleRender);
	}

	private void setSampleTextureRender(){
		SimpleTextureRender simpleTextureRender = new SimpleTextureRender(this);
		glSurfaceView.setRenderer(simpleTextureRender);
	}

	private void setMultiTextureRender(){
		MultiTextureRender multiTextureRender = new MultiTextureRender(this);
		glSurfaceView.setRenderer(multiTextureRender);
	}

	private void setTwoTextureRender(){
		TwoTextureRender twoTextureRender = new TwoTextureRender(this);
		glSurfaceView.setRenderer(twoTextureRender);
	}

	private void setTwoFilterRender(){
		TwoFilterRender twoFilterRender = new TwoFilterRender(this);
		glSurfaceView.setRenderer(twoFilterRender);
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
	}
}
