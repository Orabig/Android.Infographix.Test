package com.crocoware.infographix.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;

import com.crocoware.infographix.Arrow;
import com.crocoware.infographix.IBorderedDrawable;
import com.crocoware.infographix.shapes.Pipeline;
import com.crocoware.infographix.shapes.Segment;

public class GraphicTestView extends View {

	// Write 'TEST'
	private IBorderedDrawable letterT1;
	private IBorderedDrawable letterE;
	private IBorderedDrawable letterS;
	private IBorderedDrawable letterT2;

	private static long DURATION = 100000;
	private static int FPS = 40;
	private long startTime;
	private float elapsedTime;

	private Interpolator ease = new AnticipateInterpolator(1);
	private boolean isAnimationPending = true;

	private float ratio;

	public GraphicTestView(Context context, AttributeSet sets) {
		super(context, sets);

		startTime = System.currentTimeMillis();
	}

	/**
	 * 
	 * @param ratio
	 *            This number goes from 0 to 1 once.
	 * @param cycle
	 *            This number loops from 0 to 1 (cosine)
	 */
	private void buildDrawable(float ratio, float cycle) {

		// Give me a T
		Segment startT = new Segment(40, 40, 40, 70);
		float len1 = 20 + 40 * cycle;
		float len2 = 80 - len1;
		letterT1 = new Pipeline(startT).forward(len1).setArrow(Arrow.SIMPLE)
				.setBodyColor(Color.LTGRAY).forward(len2)
				.setBodyColor(Color.MAGENTA).turnRight(180, 0)
				.setBodyGradient(Color.MAGENTA, Color.WHITE).forward(20)
				.turnLeft(80 + 20 * cycle, 0).forward(60).close().getDrawable();

		// Give me an E
		Segment startE = new Segment(200, 60, 200, 40); // Notice we're going
														// left
		letterE = new Pipeline(startE).forward(10)
				.turnLeft(180, 70 + 10 * cycle)
				.setBodyGradient(Color.WHITE, Color.CYAN).reverse().close()
				.turnLeft(180).setBodyGradient(Color.WHITE, Color.GREEN)
				.forward(20).setBodyColor(Color.GREEN).close().getDrawable();

	}

	private void drawGrid(Canvas canvas, int size) {
		Paint paint = new Paint();
		for (int i = 0; i <= 400; i += size) {
			canvas.drawLine(i, 0, i, 400, paint);
			canvas.drawLine(0, i, 400, i, paint);
		}
	}

	// PointF center = new PointF(SB.getX(), SB.getY1() - 50);
	//
	// int N=2;
	// Segment[] starts = SB.split(N);
	// ArcShape arcs[] = new ArcShape[N];
	// int colors[] = { Color.CYAN, Color.RED, Color.LTGRAY, Color.GREEN,
	// Color.BLUE };
	// for (int i = 0; i < N; i++) {
	// arcs[i] = new ArcShape(starts[i], center, -100 - 15 * i);
	// arcs[i].setSweepShader(Color.WHITE, colors[i]);
	// }

	protected void onDraw(Canvas canvas) {

		float ratio = computeAnimationStep();
		float cycle = (float) (1 + Math.cos(elapsedTime / 200)) / 2;
		buildDrawable(ratio, cycle);

		drawGrid(canvas, 50);
		letterT1.draw(canvas);
		letterE.draw(canvas);

		if (isAnimationPending)
			this.postInvalidateDelayed(1000 / FPS);
	}

	private float computeAnimationStep() {
		elapsedTime = System.currentTimeMillis() - startTime;
		if (elapsedTime > DURATION) {
			isAnimationPending = false;
			elapsedTime = DURATION;
		}
		ratio = ease.getInterpolation(elapsedTime / DURATION);
		return ratio;
	}

}
