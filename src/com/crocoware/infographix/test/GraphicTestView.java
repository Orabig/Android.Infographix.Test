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
import com.crocoware.infographix.Pipeline;
import com.crocoware.infographix.utils.Position;
import com.crocoware.infographix.utils.Segment;
import com.crocoware.infographix.utils.Vector;

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

	// 1 = test, 2 = test2...
	private int state;

	public GraphicTestView(Context context, AttributeSet sets) {
		super(context, sets);

		startTime = System.currentTimeMillis();
	}

	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 
	 * @param ratio
	 *            This number goes from 0 to 1 once.
	 * @param cycle
	 *            This number loops from 0 to 1 (cosine)
	 */
	private void buildDrawable(float ratio, float cycle) {
		switch (state) {
		case 1:

			// / "letterT"
			// Give me a T
			Segment startT = new Segment(40, 40, 40, 70);
			float len1 = 20 + 40 * cycle;
			float len2 = 80 - len1;
			letterT1 = new Pipeline(startT).forward(len1)
					.setArrow(Arrow.SIMPLE).setBodyColor(Color.LTGRAY)
					.forward(len2).setBodyColor(Color.MAGENTA)
					.turnRight(180, 0)
					.setBodyGradient(Color.MAGENTA, Color.WHITE)
					.forward(25, 15).turnLeft(80 + 20 * cycle, 0)
					.forward(60, 20).close().getDrawable();

			// / "letterE"
			// Give me an E
			Segment startE = new Segment(200, 60, 200, 40); // Notice we're
															// going
															// left
			letterE = new Pipeline(startE).forward(10)
					.turnLeft(180, 70 + 10 * cycle)
					.setBodyGradient(Color.WHITE, Color.CYAN).reverse().close()
					.turnLeft(180).setBodyGradient(Color.WHITE, Color.GREEN)
					.forward(20).setBodyColor(Color.GREEN).close()
					.getDrawable();

			// Give me an S
			Segment startS = new Segment(252, 60, 270, 60);
			letterS = new Pipeline(startS).turnLeft(90, 0)
					.setBodyColor(Color.BLUE).turnLeft().turnLeft()
					.setBodyGradient(Color.YELLOW).turnRight().setWidth(25)
					.turnRight(90 + 20 * cycle).turnLeft(90, 0).close()
					.getDrawable();

			break;
		case 2:
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
					.turnLeft()
					.setArrow(Arrow.INNER)
					.forward(40)
					.close()
					//
					.back("split5").select(2).setBodyGradient(Color.MAGENTA)
					.forward(80).forward(50, 40).setArrow(Arrow.SIMPLE)
					.forward(50).setBodyColor(Color.GRAY).turnLeft()
					.forward(30).setArrow(Arrow.SIMPLE).forward(60)
					.setBodyColor(Color.LTGRAY).setArrow(Arrow.SIMPLE)
					.forward(40)
					.setBodyColor(Color.CYAN)
					.close()
					//
					.back("split5").select(3).setBodyGradient(Color.BLUE)
					.forward(60).turnRight()
					.turnLeft()
					//
					.back("split5").select(4).setBodyGradient(Color.GREEN)
					.forward(20).turnRight(180).close().forward(80).turnRight()
					.setArrow(Arrow.NARROW)
					//
					.getDrawable();

			break;
		case 3:
			Pipeline pipe = new Pipeline(new Segment(100, 330, 100, 400))
					.forward(50);
			Vector direction = pipe.getDirection();
			Position center = pipe.getOutputPosition().translate(80, direction)
					.translate(100, direction.rotate(-90));
			test = pipe
					.split(80, 8)
					//
					.tag("split5")
					.select(0)
					.setBodyGradient(Color.YELLOW)
					.turnAround(center, -55)
					.forward(100 + 15 * cycle)
					//
					.back("split5")
					.select(1)
					.setBodyGradient(Color.RED)
					.turnAround(center, -55)
					.forward(110 - 8 * cycle)
					//
					.back("split5")
					.select(2)
					.setBodyGradient(Color.CYAN)
					.turnAround(center, -55)
					.forward(80 + 55 * cycle)
					//
					.back("split5")
					.select(3)
					.setBodyGradient(Color.MAGENTA)
					.turnAround(center, -55)
					.forward(55 + 25 * cycle)
					//
					.back("split5").select(4)
					.setBodyGradient(Color.CYAN)
					.turnAround(center, -55)
					.forward(85 + 55 * cycle)
					//
					.back("split5").select(5).setBodyGradient(Color.RED)
					.turnAround(center, -55)
					.forward(75 + 28 * cycle)
					//
					.back("split5").select(6).setBodyGradient(Color.CYAN)
					.turnAround(center, -55).forward(109 - 38 * cycle)
					//
					.back("split5").select(7).setBodyGradient(Color.RED)
					.turnAround(center, -55).forward(79 - 8 * cycle)
					//
					.getDrawable();

			// TODO : This does not work at all !!! FIX
			// test.resize(50, 200, 400, 200);
			break;
		default:
			break;
		}

	}

	private void drawGrid(Canvas canvas, int size) {
		Paint paint = new Paint();
		for (int i = 0; i <= 600; i += size) {
			canvas.drawLine(i, 0, i, 600, paint);
			canvas.drawLine(0, i, 600, i, paint);
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

		switch (state) {
		case 1:
			// / "drawCanvas"
			letterT1.draw(canvas);
			// / "stop"
			letterE.draw(canvas);
			letterS.draw(canvas);
			break;
		default:
			test.draw(canvas);
		}
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
