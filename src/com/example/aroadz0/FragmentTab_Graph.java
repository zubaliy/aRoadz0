package com.example.aroadz0;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;



	
	 
	public class FragmentTab_Graph extends SherlockFragment {
	 
		private static final String TAG = "zGraph"; 
		
		private GraphView mGraphView;
		private SensorManager mSensorManager;
		private Sensor mAccelerometer;

		private int mSensorDelay = SensorManager.SENSOR_DELAY_FASTEST;
		private int mMaxHistorySize;
		
		private Thread mThread;
		private SurfaceHolder mHolder;

		
		private ConcurrentLinkedQueue<float[]> mHistory = new ConcurrentLinkedQueue<float[]>();
		// private TextView[] mAccValueViews = new TextView[3];
		private boolean[] mGraphs = { true, true, true, };
		private int[] mAxisColors = new int[3];

		private int mBGColor;
		private int mZeroLineColor;
		private int mStringColor;


		private boolean mDrawLoop = true;
		private int mDrawDelay = 100;
		private int mLineWidth = 2;
		private int mGraphScale = 6;
		private int mZeroLineY = 230;
		private int mZeroLineYOffset = 0;
		
		private float[] mCurrents = new float[3]; // array for storing current accelerometer readings
		
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Get the view from fragmenttab1.xml
	        View view = inflater.inflate(R.layout.fragment_graph, container, false);
	        
	     // get the frame layout for plotting the graphs
            FrameLayout frame = (FrameLayout) view.findViewById(R.id.frame);

            // set the colours for plots and text displays              
            Resources resources = getResources();
            mStringColor = resources.getColor(R.color.string);
            mBGColor = resources.getColor(R.color.background);
            mZeroLineColor = resources.getColor(R.color.zero_line);
            mAxisColors[0] = resources.getColor(R.color.accX);
            mAxisColors[1] = resources.getColor(R.color.accY);
            mAxisColors[2] = resources.getColor(R.color.accZ);
            
            // put the graphview into the framelayout
            mGraphView = new GraphView(getActivity());
            frame.addView(mGraphView, 0);
	        
            startGraph();
            
	        return view;
	    }
	 
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
	    
	    @Override
	    public void onResume() {
	    	Log.i(TAG, "Graph.onResume()");
	    	 
            startGraph();

            super.onResume();
	    }
	    
	    @Override
	    public void onPause() {

	    	Log.i(TAG, "Graph.onPause()");
	    	 
            stopGraph();
	    	
	    	super.onPause();
	    }
	    
	    @Override
	    public void onStart() {

	    	Log.i(TAG, "Graph.onStart()");
	    	 
            // Initialisation
            mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
            List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors.size() > 0) {
                    mAccelerometer = sensors.get(0);
            } else {
                    Log.e(TAG, "No Accelerometer Present");
            }
	    	
	    	super.onStart();
	    }
	    
	    @Override
		public void onStop() {
                Log.i(TAG, "Graph.onStop()");
 
                mSensorManager = null;
                mAccelerometer = null;
 
                super.onStop();
        }
 
        @Override
		public void onDestroy() {
                Log.i(TAG, "Graph.onDestroy()");
 
                super.onDestroy();
        }
	    
	    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        	
    		/*	SensorEventListener must implement two superclass methods:
    		 * 	onAccuracyChanged and on SensorChanged
    		 */
    	
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    Log.i(TAG, "mSensorEventListener.onAccuracyChanged()");
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                    Log.i(TAG, "mSensorEventListener.onSensorChanged()");
                                            
                    for (int axis = 0; axis < 3; axis++) {
                            float value = event.values[axis];
                            // put the current accelerometer data into an array
                            mCurrents[axis] = value;
                           
                    }
                                            
                    synchronized (this) {
                            /* 	Synchronized thread: if the size of the mHistory concurrent linked queue is greater
                             *  than the specified max history size, then .poll() gets the head item 
                             *  in the queue and removes it. Otherwise, add a clone of the current value 
                             *  to the the queue
                            */
                            if (mHistory.size() >= mMaxHistorySize) {
                                    mHistory.poll();
                            }
                            mHistory.add(mCurrents.clone());
                    }
            }
	    };
	    
	private void startGraph() {
            // Register a sensor listener
            if (mAccelerometer != null) {
                    mSensorManager.registerListener(mSensorEventListener, mAccelerometer, mSensorDelay);
            }

            if (!mDrawLoop) {
                    // resumes painting the graph
                    mDrawLoop = true;
                    mGraphView.surfaceCreated(mGraphView.getHolder());
            }
    }

    private void stopGraph() {
            // remove the sensor listener
            mSensorManager.unregisterListener(mSensorEventListener);

            // Stop drawing the graph
            mDrawLoop = false;
    }
	 
	
	
	
	private class GraphView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

		

		public GraphView(Context context) {
			super(context);

			Log.i(TAG, "GraphView.GraphView()");

			mHolder = getHolder();
			mHolder.addCallback(this);

			setFocusable(true);
			requestFocus();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.i(TAG, "GraphView.surfaceChanged()");
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.i(TAG, "GraphView.surfaceCreated()");

			mDrawLoop = true;
			mThread = new Thread(this);
			mThread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.i(TAG, "GraphView.surfaceDestroyed()");

			mDrawLoop = false;
			boolean roop = true;
			while (roop) {
				try {
					mThread.join();
					roop = false;
				} catch (InterruptedException e) {
					Log.e(TAG, e.getMessage());
				}
			}
			mThread = null;
		}

		@Override
		public void run() {
			Log.i(TAG, "GraphView.run()");

			int width = getWidth();
			mMaxHistorySize = (int) ((width - 20) / mLineWidth);

			Paint textPaint = new Paint();
			textPaint.setColor(mStringColor);
			textPaint.setAntiAlias(true);
			textPaint.setTextSize(14);

			Paint zeroLinePaint = new Paint();
			zeroLinePaint.setColor(mZeroLineColor);
			zeroLinePaint.setAntiAlias(true);

			Paint[] linePaints = new Paint[3];
			for (int i = 0; i < 3; i++) {
				linePaints[i] = new Paint();
				linePaints[i].setColor(mAxisColors[i]);
				linePaints[i].setAntiAlias(true);
				linePaints[i].setStrokeWidth(2);
			}

			while (mDrawLoop) {
				Canvas canvas = mHolder.lockCanvas();

				if (canvas == null) {
					break;
				}

				canvas.drawColor(mBGColor);

				float zeroLineY = mZeroLineY + mZeroLineYOffset;

				synchronized (mHolder) {
					float twoLineY = zeroLineY - (20 * mGraphScale);
					float oneLineY = zeroLineY - (10 * mGraphScale);
					float minasOneLineY = zeroLineY + (10 * mGraphScale);
					float minasTwoLineY = zeroLineY + (20 * mGraphScale);

					canvas.drawText("2", 5, twoLineY + 5, zeroLinePaint);
					canvas.drawLine(20, twoLineY, width, twoLineY, zeroLinePaint);

					canvas.drawText("1", 5, oneLineY + 5, zeroLinePaint);
					canvas.drawLine(20, oneLineY, width, oneLineY, zeroLinePaint);

					canvas.drawText("0", 5, zeroLineY + 5, zeroLinePaint);
					canvas.drawLine(20, zeroLineY, width, zeroLineY, zeroLinePaint);

					canvas.drawText("-1", 5, minasOneLineY + 5, zeroLinePaint);
					canvas.drawLine(20, minasOneLineY, width, minasOneLineY,
							zeroLinePaint);

					canvas.drawText("-2", 5, minasTwoLineY + 5, zeroLinePaint);
					canvas.drawLine(20, minasTwoLineY, width, minasTwoLineY,
							zeroLinePaint);

					if (mHistory.size() > 1) {
						Iterator<float[]> iterator = mHistory.iterator();
						float[] before = new float[3];
						int x = width - mHistory.size() * mLineWidth;
						int beforeX = x;
						x += mLineWidth;

						if (iterator.hasNext()) {
							float[] history = iterator.next();
							for (int axis = 0; axis < 3; axis++) {
								before[axis] = zeroLineY - (history[axis] * mGraphScale);
							}
							while (iterator.hasNext()) {
								history = iterator.next();
								for (int axis = 0; axis < 3; axis++) {
									float startY = zeroLineY
											- (history[axis] * mGraphScale);
									float stopY = before[axis];
									if (mGraphs[axis]) {
										canvas.drawLine(x, startY, beforeX, stopY, linePaints[axis]);
									}
									before[axis] = startY;
								}
								beforeX = x;
								x += mLineWidth;
							}
						}
					}
				}

				mHolder.unlockCanvasAndPost(canvas);

				try {
					Thread.sleep(mDrawDelay);
				} catch (InterruptedException e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
	}
	
}
