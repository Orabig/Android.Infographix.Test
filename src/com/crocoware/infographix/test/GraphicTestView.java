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
import com.crocoware.infographix.shapes.CurvedPipeShape;
import com.crocoware.infographix.shapes.DownRightArcShape;
import com.crocoware.infographix.shapes.HSegment;
import com.crocoware.infographix.shapes.PipeShape;
import com.crocoware.infographix.shapes.VSegment;

public class GraphicTestView extends View {

	private IBorderedDrawable pipe;

	private static long DURATION = 100000;
	private static int FPS = 40;
	private long startTime;
	private float elapsedTime;

	private Interpolator ease = new AnticipateInterpolator(1);
	private boolean isAnimationPending = true;

	private float ratio;

	public GraphicTestView(Context context, AttributeSet sets) {
		super(context, sets);

		computePositions(0);
		buildDrawable();

		startTime = System.currentTimeMillis();
		// postInvalidate();
	}

	private float width = 100;
	private float height = 100;
	private float height2 = 150;

	private void buildDrawable() {
		version2();
	}

	private void version1() {
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
	}

	private void version2() {
		float WIDTH = 300;
		float pX = WIDTH / 3; // On découpe l'espace H en bouts

		// Ecartement initial
		float HEIGHT = 100;
		// Position du haut du funnel (entrée)
		float X0 = 10;
		float Y0 = 200;

		float ratio = 0.5f + (float) Math.cos(elapsedTime / 1000) * 0.4f;

		float gap = 30;

		// Calcul des positions
		float Y1 = Y0 + HEIGHT;
		float XB = X0 + pX;
		float XC = XB + pX;
		float XD = XC + pX;

		float Ha = (Y1 - Y0) * ratio;
		float Hb = (Y1 - Y0) - Ha;
		float YB1 = Y0 + Ha;

		// Ce calcul répartit équitablement la largeur totale en deux parties
		// (ratio) et (1-ratio).
		float YCa1 = Y0 - gap * (1 - ratio);
		float YCa2 = YB1 - gap * (1 - ratio);
		float YCa3 = YB1 + gap * (ratio);
		float YCa4 = Y1 + gap * (ratio);

		VSegment SA = new VSegment(X0, Y0, Y1);
		VSegment SB = new VSegment(XB, Y0, Y1);

		VSegment SBa = new VSegment(XB, Y0, YB1);
		VSegment SBb = new VSegment(XB, YB1, Y1);

		VSegment SCa = new VSegment(XC, YCa1, YCa2);
		VSegment SCb = new VSegment(XC, YCa3, YCa4);

		pipe = new ComposedBordered(new CurvedPipeShape(SA, SB),
				new CurvedPipeShape(SBa, SCa), new CurvedPipeShape(SBb, SCb));
	}

	protected void onDraw(Canvas canvas) {

		float ratio = computeAnimationStep();
		computePositions(ratio);

		buildDrawable();

		pipe.draw(canvas);

		if (isAnimationPending)
			this.postInvalidateDelayed(1000 / FPS);
	}

	private void computePositions(float ratio) {
		width = 100 + ratio * 100;
		height = 100 + ratio * 80;
		height2 = height + 60 - ratio * 30;
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
