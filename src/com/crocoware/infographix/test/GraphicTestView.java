package com.crocoware.infographix.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;

import com.crocoware.infographix.AbstractBorderedDrawable;
import com.crocoware.infographix.ComposedBordered;
import com.crocoware.infographix.IBorderedDrawable;
import com.crocoware.infographix.shapes.DownRightArcShape;
import com.crocoware.infographix.shapes.PipeShape;

public class GraphicTestView extends View {

	private IBorderedDrawable pipe;

	private static long DURATION = 1000;
	private static int FPS = 40;
	private long startTime;
	private Interpolator ease = new AnticipateInterpolator(1);

	public GraphicTestView(Context context, AttributeSet sets) {
		super(context, sets);

		buildDrawable();

		startTime = System.currentTimeMillis();
		// postInvalidate();
	}

	private float width = 100;
	private float height = 100;
	private float height2 = 150;

	private void buildDrawable() {
		float x = 10;
		float y = 150;
		float width2 = 300;
		AbstractBorderedDrawable pipe0 = new PipeShape(x, x + width,
				y - height, y, false);
		AbstractBorderedDrawable pipe1 = new DownRightArcShape(x, x + width, y,
				x + width2, y + height, y + height2);
		AbstractBorderedDrawable pipe2 = new PipeShape(x + width2, x + width
				+ width2, y + height, y + height2, true);
		pipe = new ComposedBordered(pipe0, pipe1, pipe2);
		// mDrawable.getPaint().setColor(0xff74AC23);
	}

	protected void onDraw(Canvas canvas) {

		float elapsedTime = System.currentTimeMillis() - startTime;
		float ratio = ease.getInterpolation(elapsedTime / DURATION);
		width = 100 + ratio * 100;
		height = 100 + ratio * 80;
		height2 = height + 60 - ratio * 30;

		buildDrawable();

		pipe.draw(canvas);

		Log.e("test", "width=" + width);

		if (elapsedTime < DURATION)
			this.postInvalidateDelayed(1000 / FPS);
	}

}
