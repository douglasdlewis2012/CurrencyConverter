package com.douglaslewis.currencyconverter.myviews.dialogs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.douglaslewis.currencyconverter.Models.GraphData;
import com.douglaslewis.currencyconverter.R;

import java.text.DecimalFormat;

import timber.log.Timber;


public class CurrencyGraphView extends View {
	private static final String TAG = "CurrencyGraphView";
	private static final float AXIS_X_MIN = 0;
	private static final float AXIS_X_MAX = 120;
	private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG |
			Canvas.CLIP_SAVE_FLAG |
			Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
			Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
			Canvas.CLIP_TO_LAYER_SAVE_FLAG;
	private static final int mNumberBlocksVertical = 10;
	private static final int mNumberBlocksHorizontal = 12;
	private static float AXIS_Y_MIN = 0;
	private static float AXIS_Y_MAX = 100;
	private static GraphData sGraphData = null;//{1, 1, 1, 1};
	private static int DRAW_STEPS = 48;
	private static float[] mSeriesLinesBuffer = new float[(DRAW_STEPS + 1) * 4];
	private  RectF mCurrentView = new RectF (AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);
	private final AxisLabels mXStopsBuffer = new AxisLabels ();
	private final AxisLabels mYStopsBuffer = new AxisLabels ();
	@SuppressWarnings("FieldCanBeLocal")
	private final int TRANSLUCENT_ALPHA = 255 / 4;
	private char[][] mPrintTemperatures = null;
	//Attribute values and paints
	private float mLabelTextSize;
	private int mLabelTextColor;
	private Paint mLabelPaint;
	private int mMaxLabelWidth;
	private int mLabelHeight;
	private float mGridThickness;
	private int mGridColor;
	private Paint mGridPaint;
	private float mAxisThickness;
	private int mAxisColor;
	private Paint mAxisPaint;
	private float mDataThickness;
	private int mDataColor;
	private Paint mDataPaint;
	private Paint mDataPaintFill;
	private int mLabelSeparation;
	private Paint mClearBoardPaint;


	@SuppressWarnings("CanBeFinal")
	private Rect mContentRect = new Rect ();
	private float[] mAxisXPositionsBuffer = new float[]{};
	private float[] mAxisYPositionsBuffer = new float[]{};
	private float[] mAxisXLinesBuffer = new float[]{};
	private float[] mAxisYLinesBuffer = new float[]{};

	public CurrencyGraphView (Context context) {
		this (context, null, 0);
	}

	public CurrencyGraphView (Context context, AttributeSet attrs) {

		this (context, attrs, 0);
		Timber.d ("View being recreated");
	}

	private CurrencyGraphView (Context context, AttributeSet attrs, @SuppressWarnings({"SameParameterValue", "UnusedParameters"}) int defStyle) {
		super (context, attrs, 0);

		TypedArray typedArray = context.getTheme ().obtainStyledAttributes (attrs, R.styleable.CurrencyGraphView, 0, 0);

		try {
			mLabelTextColor = typedArray.getColor (R.styleable.CurrencyGraphView_labelTextColor, mLabelTextColor);
			mLabelTextSize = typedArray.getDimension (R.styleable.CurrencyGraphView_labelTextSize, mLabelTextSize);
			mLabelSeparation = typedArray.getDimensionPixelSize (R.styleable.CurrencyGraphView_labelSeparation, mLabelSeparation);
			mGridThickness = typedArray.getDimension (R.styleable.CurrencyGraphView_gridThickness, mGridThickness);
			mGridColor = typedArray.getColor (R.styleable.CurrencyGraphView_gridColor, mGridColor);
			mAxisThickness = typedArray.getDimension (R.styleable.CurrencyGraphView_axisThickness, mAxisThickness);
			mAxisColor = typedArray.getColor (R.styleable.CurrencyGraphView_axisColor, mAxisColor);
			mDataThickness = typedArray.getDimension (R.styleable.CurrencyGraphView_dataThickness, mDataThickness);
			mDataColor = typedArray.getColor (R.styleable.CurrencyGraphView_dataColor, mDataColor);

		} finally {
			typedArray.recycle ();
		}

		initializePaints ();
	}

	private static void computeAxis (float start, float stop, int steps, AxisLabels outStops) {
		double range = stop - start;
		if (steps == 0 || range <= 0) {
			outStops.labels = new float[]{};
			outStops.numLabels = 0;
			return;
		}

		double raw = range / steps;
		double interval = roundToOneFigure (raw);
		double magnitude = Math.pow (10, (int) Math.log10 (interval));
		int intervalSig = (int) (interval / magnitude);
		if (intervalSig > 5) {
			interval = Math.floor (10 * magnitude);
		}

		double first = Math.ceil (start / interval) * interval;
		double last = Math.nextUp (Math.floor (stop / interval) * interval);

		double f;
		int i;
		int n = 0;
		for (f = first; f <= last; f += interval) {
			++n;
		}

		outStops.numLabels = n;

		if (outStops.labels.length < n) {
			outStops.labels = new float[n];
		}

		for (f = first, i = 0; i < n; f += interval, ++i) {
			outStops.labels[i] = (float) f;
		}

		if (interval < 1) {
			outStops.decimals = (int) Math.ceil (-Math.log10 (interval));
		} else {
			outStops.decimals = 0;
		}

	}

	private static float roundToOneFigure (double num) {
		final float d = (float) Math.ceil ((float) Math.log10 (num < 0 ? -num : num));
		final int power = 1 - (int) d;
		final float magnitude = (float) Math.pow (10, power);
		final long shifted = Math.round (num * magnitude);
		return shifted / magnitude;
	}

	private void initializePaints () {
		mLabelPaint = new Paint ();
		mLabelPaint.setAntiAlias (true);
		mLabelPaint.setTextSize (mLabelTextSize);
		mLabelPaint.setColor (mLabelTextColor);
		mLabelHeight = (int) Math.abs (mLabelPaint.getFontMetrics ().top);
		mMaxLabelWidth = (int) mLabelPaint.measureText ("000000");

		mGridPaint = new Paint ();
		mGridPaint.setStrokeWidth (mGridThickness);
		mGridPaint.setColor (mGridColor);
		mGridPaint.setAlpha (TRANSLUCENT_ALPHA);
		mGridPaint.setStyle (Paint.Style.STROKE);

		mAxisPaint = new Paint ();
		mAxisPaint.setStrokeWidth (mAxisThickness);
		mAxisPaint.setColor (Color.BLACK); //mAxisColor);
		mAxisPaint.setStyle (Paint.Style.STROKE);

		mDataPaint = new Paint ();
		mDataPaint.setStrokeWidth (mDataThickness);
		mDataPaint.setColor (mDataColor);
		mDataPaint.setStyle (Paint.Style.STROKE);
		mDataPaint.setAntiAlias (true);

		mDataPaintFill = new Paint ();
		mDataPaintFill.setColor (mDataColor);
		mDataPaintFill.setAlpha (TRANSLUCENT_ALPHA);
		mDataPaintFill.setStyle (Paint.Style.FILL);

		mClearBoardPaint = new Paint ();
		mClearBoardPaint.setColor (Color.WHITE);
		mClearBoardPaint.setStyle (Paint.Style.FILL);

	}

	public void initWithData (GraphData data) {
		sGraphData = data;
		DRAW_STEPS = sGraphData.getEntries ().length - 1;
		mSeriesLinesBuffer = new float[(DRAW_STEPS + 1) * 4];
		AXIS_Y_MIN = (float) (sGraphData.getMin ().floatValue () - sGraphData.getMin ().floatValue () * .1);
		AXIS_Y_MAX = (float) (sGraphData.getMax ().floatValue () * .1 + sGraphData.getMax ().floatValue ());
		mPrintTemperatures = getYAxisLabels (sGraphData.getMin ().floatValue (), sGraphData.getMax ().floatValue ());


	}

	//Drawing methods

	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh) {
		super.onSizeChanged (w, h, oldw, oldh);
		mContentRect.set (mMaxLabelWidth + mLabelSeparation, getPaddingTop (), getWidth () - getPaddingRight (), getHeight () - getPaddingTop () * 2);

	}

	@Override
	protected void onMeasure (int widthMeasure, int heightMeasure) {
		int minSize = getResources ().getDimensionPixelSize (R.dimen.min_graph_size);
		int width = Math.max (getSuggestedMinimumWidth (), resolveSize (minSize + getPaddingLeft () + mMaxLabelWidth + mLabelSeparation + getPaddingRight (), widthMeasure));
		int height = Math.max (getSuggestedMinimumHeight (), resolveSize (minSize + getPaddingTop () + mLabelHeight * 3 + mLabelSeparation + getPaddingBottom (), heightMeasure));

		setMeasuredDimension (width, height);

	}

	@Override
	protected void onDraw (Canvas canvas) {
		DRAW_STEPS = sGraphData.getEntries ().length - 1;

		int restoreCount = canvas.save ();
		canvas.clipRect (mContentRect);
		canvas.drawPaint (mClearBoardPaint);
		canvas.restoreToCount (restoreCount);

		drawAxes (canvas);
		restoreCount = canvas.save ();
		canvas.clipRect (mContentRect);

		drawDataUnclipped (canvas);

		canvas.drawPath (linesToPath (), mDataPaintFill);

		canvas.restoreToCount (restoreCount);

		canvas.drawRect (mContentRect, mAxisPaint);

	}

	private void drawAxes (Canvas canvas) {
		int i;

		computeAxis (mCurrentView.left, mCurrentView.right, mNumberBlocksHorizontal, mXStopsBuffer);
		computeAxis (mCurrentView.top, mCurrentView.bottom, mNumberBlocksVertical, mYStopsBuffer);

		if (mAxisXPositionsBuffer.length < mXStopsBuffer.numLabels) {
			mAxisXPositionsBuffer = new float[mXStopsBuffer.numLabels];
		}
		if (mAxisYPositionsBuffer.length < mYStopsBuffer.numLabels) {
			mAxisYPositionsBuffer = new float[mYStopsBuffer.numLabels];
		}
		if (mAxisXLinesBuffer.length < mXStopsBuffer.numLabels * 4) {
			mAxisXLinesBuffer = new float[mXStopsBuffer.numLabels * 4];
		}
		if (mAxisYLinesBuffer.length < mYStopsBuffer.numLabels * 4) {
			mAxisYLinesBuffer = new float[mYStopsBuffer.numLabels * 4];
		}

//		 Compute positions
		for (i = 0; i < mXStopsBuffer.numLabels; i++) {
			mAxisXPositionsBuffer[i] = getDrawX (mXStopsBuffer.labels[i]);
		}
		for (i = 0; i < mYStopsBuffer.numLabels; i++) {
			mAxisYPositionsBuffer[i] = getDrawY (mYStopsBuffer.labels[i]);
		}

		// Draws grid lines using drawLines (faster than individual drawLine calls)
		for (i = 0; i < mXStopsBuffer.numLabels; i++) {
			mAxisXLinesBuffer[i * 4] = (float) Math.floor (mAxisXPositionsBuffer[i]);
			mAxisXLinesBuffer[i * 4 + 1] = mContentRect.top;
			mAxisXLinesBuffer[i * 4 + 2] = (float) Math.floor (mAxisXPositionsBuffer[i]);
			mAxisXLinesBuffer[i * 4 + 3] = mContentRect.bottom;
		}
		canvas.drawLines (mAxisXLinesBuffer, 0, mXStopsBuffer.numLabels * 4, mGridPaint);

		for (i = 0; i < mYStopsBuffer.numLabels; i++) {
			mAxisYLinesBuffer[i * 4] = mContentRect.left;
			mAxisYLinesBuffer[i * 4 + 1] = (float) Math.floor (mAxisYPositionsBuffer[i]);
			mAxisYLinesBuffer[i * 4 + 2] = mContentRect.right;
			mAxisYLinesBuffer[i * 4 + 3] = (float) Math.floor (mAxisYPositionsBuffer[i]);
		}
		canvas.drawLines (mAxisYLinesBuffer, 0, mYStopsBuffer.numLabels * 4, mGridPaint);

		int labelLength;

		// Draws Y labels
		mLabelPaint.setTextAlign (Paint.Align.RIGHT);
		for (i = 0; i < mYStopsBuffer.numLabels; i++) {

			labelLength = mPrintTemperatures[i].length;
			canvas.drawText (
					mPrintTemperatures[i], 0, labelLength,
					mContentRect.left - mLabelSeparation,
					mAxisYPositionsBuffer[i],
					mLabelPaint);

		}

	}


	private float getDrawX (float x) {
		return mContentRect.left + mContentRect.width () * (x - mCurrentView.left) / mCurrentView.width ();
	}

	private float getDrawY (float y) {
		return mContentRect.bottom - mContentRect.height () * (y - mCurrentView.top) / mCurrentView.height ();
	}

	private void drawDataUnclipped (Canvas canvas) {

		mSeriesLinesBuffer[0] = mContentRect.left;
		mSeriesLinesBuffer[1] = getDrawY (sGraphData.getEntries ()[0].floatValue ());// (0));//[0]);
		mSeriesLinesBuffer[2] = mSeriesLinesBuffer[0];
		mSeriesLinesBuffer[3] = mSeriesLinesBuffer[1];

		float x;
		for (int i = 1; i <= DRAW_STEPS; i++) {
			mSeriesLinesBuffer[i * 4] = mSeriesLinesBuffer[(i - 1) * 4 + 2];
			mSeriesLinesBuffer[i * 4 + 1] = mSeriesLinesBuffer[(i - 1) * 4 + 3];

			x = (mCurrentView.left + (mCurrentView.width () / DRAW_STEPS * i));
			mSeriesLinesBuffer[i * 4 + 2] = getDrawX (x);
			mSeriesLinesBuffer[i * 4 + 3] = getDrawY (sGraphData.getEntries ()[i].floatValue ());
		}
		canvas.drawLines (mSeriesLinesBuffer, mDataPaint);

	}


	private Path linesToPath () {
		Path p = new Path ();

		p.moveTo (mSeriesLinesBuffer[0], mSeriesLinesBuffer[1]);
		for (int i = 2; i < mSeriesLinesBuffer.length; i += 2) {
			p.lineTo (mSeriesLinesBuffer[i], mSeriesLinesBuffer[i + 1]);
		}
		p.lineTo (mContentRect.right, mContentRect.bottom);
		p.lineTo (mContentRect.left, mContentRect.bottom);
		p.lineTo (mSeriesLinesBuffer[0], mSeriesLinesBuffer[1]);

		return p;

	}

	private char[][] getYAxisLabels (float min, float max) {
		char[][] yLabels = new char[11][6];
		DecimalFormat df = new DecimalFormat ("###.##");
		float range = max + min / 2;
		float increments = range * .1f;
		float additive = range * .1f;
		for (int i = 0; i < 11; i++) {
			yLabels[i] = df.format (increments).toCharArray ();
			increments += additive;
		}

		return yLabels;
	}

	public void changeData (GraphData data) {
		sGraphData = data;
		DRAW_STEPS = sGraphData.getEntries ().length - 1;
		mSeriesLinesBuffer = new float[(DRAW_STEPS + 1) * 4];
		AXIS_Y_MIN = (float) (sGraphData.getMin ().floatValue () - sGraphData.getMin ().floatValue () * .1);
		AXIS_Y_MAX = (float) (sGraphData.getMax ().floatValue () * .1 + sGraphData.getMax ().floatValue ());
		mPrintTemperatures = getYAxisLabels (sGraphData.getMin ().floatValue (), sGraphData.getMax ().floatValue ());
		mCurrentView = new RectF (AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);


		invalidate ();
	}

	private static class AxisLabels {
		float[] labels = new float[]{};
		int numLabels;
		int decimals;
	}

}
