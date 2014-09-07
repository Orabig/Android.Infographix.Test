package com.crocoware.infographix.test;

import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;

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
import com.crocoware.infographix.utils.Segment;

public class GraphicTestView extends View {

	// Write 'TEST'
	private IBorderedDrawable letterT1;
	private IBorderedDrawable letterE;
	private IBorderedDrawable letterS;
	private IBorderedDrawable letterT2;
	private IBorderedDrawable test;

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
				.setBodyGradient(Color.MAGENTA, Color.WHITE).forward(25, 15)
				.turnLeft(80 + 20 * cycle, 0).forward(60, 20).close()
				.getDrawable();

		// Give me an E
		Segment startE = new Segment(200, 60, 200, 40); // Notice we're going
														// left
		letterE = new Pipeline(startE).forward(10)
				.turnLeft(180, 70 + 10 * cycle)
				.setBodyGradient(Color.WHITE, Color.CYAN).reverse().close()
				.turnLeft(180).setBodyGradient(Color.WHITE, Color.GREEN)
				.forward(20).setBodyColor(Color.GREEN).close().getDrawable();

		// Give me an S
		Segment startS = new Segment(252, 60, 270, 60);
		letterS = new Pipeline(startS).turnLeft(90, 0).setBodyColor(Color.BLUE)
				.turnLeft().turnLeft().setBodyGradient(Color.YELLOW)
				.turnRight().setWidth(25).turnRight(90 + 20 * cycle)
				.turnLeft(90, 0).close().getDrawable();

		test = new Pipeline(new Segment(50, 300, 50, 400))
				.forward(50)
				.split(50,
						new float[] { 0.2f, 0.3f + cycle / 20,
								0.2f + cycle / 20, 0.1f, 0.2f - cycle / 10 })
				//
				.tag("split5")
				.select(0)
				.setBodyGradient(Color.YELLOW)
				.forward(40)
				.turnLeft(180)
				.setArrow(Arrow.STANDARD)
				//
				.back("split5")
				.select(1)
				.setBodyGradient(Color.RED)
				.forward(70)
				.turnLeft().setArrow(Arrow.INNER).forward(40)
				.close()
				//
				.back("split5").select(2).setBodyGradient(Color.MAGENTA)
				.forward(80).forward(50, 30).setArrow(Arrow.SIMPLE).forward(50)
				.setBodyColor(Color.GRAY).turnLeft().forward(30)
				.setArrow(Arrow.SIMPLE).forward(60).setBodyColor(Color.LTGRAY)
				.setArrow(Arrow.SIMPLE).forward(40)
				.setBodyColor(Color.CYAN)
				.close()
				//
				.back("split5").select(3).setBodyGradient(Color.BLUE)
				.forward(60).turnRight().turnLeft()
				//
				.back("split5").select(4).setBodyGradient(Color.GREEN)
				.forward(20).turnRight(180).close().forward(20).turnLeft().setArrow(Arrow.NARROW)
				//
				.getDrawable();
		
		// TODO : This does not work at all !!! FIX
//		test.resize(50, 200, 400, 200);
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
		letterS.draw(canvas);

		test.draw(canvas);

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
