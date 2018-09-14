package com.andev.androidshaderdemo.render;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.TwoTextureShaderProgram;
import com.andev.androidshaderdemo.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;


public class TwoTextureRender implements GLSurfaceView.Renderer{

	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT
			+ TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

	public static final float CUBE[] = {//翻转顶点信息中的纹理坐标,统一用1去减
			-1.0f, -1.0f, 0f, 1f - 0f,
			1.0f, -1.0f, 1f, 1f -0f,
			-1.0f, 1.0f, 0f, 1f -1f,
			1.0f, 1.0f, 1f, 1f -1f,
	};

	Context context;
	VertexArray vertexArray;
	TwoTextureShaderProgram twoTextureShaderProgram;
	private int originTexture;
	private int edgeTexture;
	private int hefeMapTexture;
	private int hefemetalTexture;
	private int hefesoftlightTexture;
	private int hefegradientmapTexture;
	private int[] textureIDs;

	private int width;
	private int height;

	public TwoTextureRender(Context context){
		this.context = context;
		vertexArray = new VertexArray(CUBE);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		twoTextureShaderProgram = new TwoTextureShaderProgram(context);
		originTexture = TextureHelper.loadTexture(context, R.drawable.lena);
		edgeTexture = TextureHelper.loadTexture(context, R.drawable.edgeburn);

		textureIDs = new int[]{originTexture, edgeTexture};
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);

		this.width = width;
		this.height = height;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);

//		Log.w("wf", width + "");
//		Log.w("wf", 1.0f / width + "");
//		Log.w("hf", 1.0f / height + "");

		twoTextureShaderProgram.useProgram();
		twoTextureShaderProgram.setUniforms(textureIDs, 1.0f / width, 1.0f / height);

		vertexArray.setVertexAttribPointer(
				0,
				twoTextureShaderProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				twoTextureShaderProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

	}
}
