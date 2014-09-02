package com.crocoware.infographix.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.crocoware.infographix.AbstractBorderedDrawable;
import com.crocoware.infographix.ComposedBordered;
import com.crocoware.infographix.IBorderedDrawable;
import com.crocoware.infographix.shapes.DownRightArcShape;
import com.crocoware.infographix.shapes.PipeShape;

public class GraphicTestView extends View {

	private IBorderedDrawable pipe;

	public GraphicTestView(Context context, AttributeSet sets) {
		super(context, sets);

		buildDrawable();
	}

	private void buildDrawable() {
		float x = 10;
		float y = 150;
		float width = 100;
		float width2 = 300;
		float height = 100;
		float height2 = 150;
		AbstractBorderedDrawable pipe0 = new PipeShape(x, x + width
, y - height, y , false);
		AbstractBorderedDrawable pipe1 = new DownRightArcShape(x, x + width, y,
				x + width2, y + height, y + height2);
		AbstractBorderedDrawable pipe2 = new PipeShape(x + width2, x + width
				+ width2, y + height, y + height2, true);
		pipe = new ComposedBordered(pipe0, pipe1, pipe2);
		// mDrawable.getPaint().setColor(0xff74AC23);
	}

	protected void onDraw(Canvas canvas) {
		pipe.draw(canvas);
	}

}
