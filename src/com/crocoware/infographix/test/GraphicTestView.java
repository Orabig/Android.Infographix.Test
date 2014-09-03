package com.crocoware.infographix.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;

import com.crocoware.infographix.AbstractBorderedDrawable;
import com.crocoware.infographix.ComposedBordered;
import com.crocoware.infographix.IBorderedDrawable;
import com.crocoware.infographix.shapes.DownRightArcShape;
import com.crocoware.infographix.shapes.HSegment;
import com.crocoware.infographix.shapes.PipeShape;
import com.crocoware.infographix.shapes.VSegment;

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
		HSegment startH = new HSegment(x, x + width, y - height);
		HSegment endH = new HSegment(x, x + width, y);

		VSegment startV = new VSegment(x + width2, y + height, y + height2);
		VSegment endV = new VSegment(x + width + width2, y + height, y
				+ height2);

		AbstractBorderedDrawable pipe0 = new PipeShape(startH, endH);
		AbstractBorderedDrawable pipe1 = new DownRightArcShape(endH, startV);
		AbstractBorderedDrawable pipe2 = new PipeShape(startV, endV);

		pipe = new ComposedBordered(pipe0, pipe1, pipe2);

		// mDrawable.getPaint().setColor(0xff74AC23);
	}

	protected void onDraw(Canvas canvas) {

		float elapsedTime = System.currentTimeMillis() - startTime;
		if (elapsedTime > DURATION)
			elapsedTime = DURATION;
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
