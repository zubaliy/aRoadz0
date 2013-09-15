package com.example.aroadz0;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;


		
	
	 
	public class FragmentTab_Graph extends SherlockFragment implements SensorEventListener, Observer {
	 
		private static final String TAG = "zGraph"; 
		private static final int SIZE = 100;            // number of points to plot in history
		private static final int BOUNDRY_BOTTOM = -24;  // y-min
		private static final int BOUNDRY_TOP = 24;		// y-max
		
		private static final int THRESHOLD_1 = 4;
		private static final int THRESHOLD_2 = 9;
		private static final int THRESHOLD_3 = 16;
		
	    private SensorManager sensorManager = null;
	    private Sensor accelerometer, acceleration, gravity, gyroscope, magneticfield;
	    private int SENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;
	 
	    private XYPlot graph = null;
	
	    private SimpleXYSeries x = null;
	    private SimpleXYSeries y = null;
	    private SimpleXYSeries z = null;
	    
	    private SimpleXYSeries th1 = null;
	    private SimpleXYSeries th1M = null;
	    		
		private View view;
		
		
		private float[] zrotationMatrix = new float[16];
		private float[] dlin_acc = new float[3]; ;
		private float[] dgravity = new float[3];
		private float[] dmagfield = new float[3];

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Get the view from fragment_graph.xml
	        view = inflater.inflate(R.layout.fragment_graph, container, false);
	        
	        create();

	        return view;
	    }
	 
	    private void create() {
	        // setup the APR History plot:
	        graph = (XYPlot) view.findViewById(R.id.aprHistoryPlot);
	 
	        x = new SimpleXYSeries("Acc X");
	        x.useImplicitXVals();
	        y = new SimpleXYSeries("Acc Y");
	        y.useImplicitXVals();
	        z = new SimpleXYSeries("Acc Z");
	        z.useImplicitXVals();
	 
	        LineAndPointFormatter l = new LineAndPointFormatter(Color.rgb(200, 100, 100), null, null, null);
	        Paint p = l.getLinePaint();
	        p.setStrokeWidth(5);
	        l.setLinePaint(p);
//	        graph.addSeries(x, new LineAndPointFormatter(Color.rgb(100, 100, 200), null, null, null));
//	        graph.addSeries(y, new LineAndPointFormatter(Color.rgb(100, 200, 100), null, null, null));
	        graph.addSeries(z, l);
	        
/**Threshold**/
	     // Threshold to plot:
	        // Threshold 1:
	        th1 = new SimpleXYSeries("Series1"); 
	        th1.useImplicitXVals();
	        for(int i=0; i<SIZE+10; i++){
	        	th1.addLast(null, THRESHOLD_1);
	        }
	        
	        th1M = new SimpleXYSeries("Series1"); 
	        th1M.useImplicitXVals();
	        for(int i=0; i<SIZE+10; i++){
	        	th1M.addLast(null, -THRESHOLD_1);
	        }
	        
	        // Draw threshold
	        l = new LineAndPointFormatter(Color.GREEN, null, null, null);
	        p = l.getLinePaint();
	        p.setStrokeWidth(1);
	        l.setLinePaint(p);
	        
	        graph.addSeries(th1, l);
	        graph.addSeries(th1M, l);
	        
	     // Threshold 2:
	        th1 = new SimpleXYSeries("Series1"); 
	        th1.useImplicitXVals();
	        for(int i=0; i<SIZE+10; i++){
	        	th1.addLast(null, THRESHOLD_2);
	        }
	        
	        th1M = new SimpleXYSeries("Series1"); 
	        th1M.useImplicitXVals();
	        for(int i=0; i<SIZE+10; i++){
	        	th1M.addLast(null, -THRESHOLD_2);
	        }
	        
	        // Draw threshold
	        l = new LineAndPointFormatter(Color.rgb(255, 193, 37), null, null, null);
	        p = l.getLinePaint();
	        p.setStrokeWidth(1);
	        l.setLinePaint(p);
	        
	        graph.addSeries(th1, l);
	        graph.addSeries(th1M, l);
	        
		     // Threshold 3:

	        th1 = new SimpleXYSeries("Series1"); 
	        th1.useImplicitXVals();
	        for(int i=0; i<SIZE+10; i++){
	        	th1.addLast(null, THRESHOLD_3);
	        }
	        
	        th1M = new SimpleXYSeries("Series1"); 
	        th1M.useImplicitXVals();
	        for(int i=0; i<SIZE+10; i++){
	        	th1M.addLast(null, -THRESHOLD_3);
	        }
	        
	        // Draw threshold
	        l = new LineAndPointFormatter(Color.RED, null, null, null);
	        p = l.getLinePaint();
	        p.setStrokeWidth(1);
	        l.setLinePaint(p);
	        
	        graph.addSeries(th1, l);
	        graph.addSeries(th1M, l);
/**Threshold END**/	 
	        
	        
	        //aprHistoryPlot.setBackgroundColor(Color.WHITE);
	        graph.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
	        graph.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
	        

	        graph.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
	        graph.setPlotMargins(0, 0, 0, 0);
	        graph.setPlotPadding(0, 0, 0, 0);
	        

	        graph.getGraphWidget().setPaddingTop(20);
	        graph.getGraphWidget().setMarginLeft(-35);
	        
	        graph.getGraphWidget().position(
	                0, XLayoutStyle.ABSOLUTE_FROM_LEFT,
	                0, YLayoutStyle.ABSOLUTE_FROM_TOP,
	                AnchorPosition.LEFT_TOP);
	        
	        graph.getGraphWidget().setSize(new SizeMetrics(
	                0, SizeLayoutType.FILL,
	                0, SizeLayoutType.FILL));
	        
	        // Domain
	        graph.setDomainBoundaries(0, SIZE, BoundaryMode.FIXED);
	        graph.setDomainStepValue(1);
	        graph.getGraphWidget().setDomainOriginLinePaint(null);
	        
	        // Range
	        graph.setRangeBoundaries(BOUNDRY_BOTTOM, BOUNDRY_TOP, BoundaryMode.FIXED);
			graph.getGraphWidget().setRangeOriginLinePaint(null);
	        graph.setRangeStepValue( 1);
	
	        
	        
	        // Remove Legend
	        graph.getLayoutManager().remove(graph.getLegendWidget());
	        graph.getLayoutManager().remove(graph.getDomainLabelWidget());
	        graph.getLayoutManager().remove(graph.getRangeLabelWidget());
	        graph.getLayoutManager().remove(graph.getTitleWidget());
	        
	        //aprHistoryPlot.setMarkupEnabled(true);
	       
	        // register for  sensor events:
			sensorManager = (SensorManager) getActivity().getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

			acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
			gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
			magneticfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

	        addListeners();
			
		}
	    

		private void addListeners() {
			sensorManager.registerListener(this, acceleration, SENSOR_DELAY );
	        sensorManager.registerListener(this, gravity, SENSOR_DELAY );
	        sensorManager.registerListener(this, magneticfield, SENSOR_DELAY );
			
		}
		
		private void removeListeners() {
			sensorManager.unregisterListener(this);
		}

		@Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
	    
	    @Override
	    public void onResume() {
	    	Log.i(TAG, "Graph.onResume()");
	    	addListeners();
            super.onResume();
	    }
	    
	    @Override
	    public void onPause() {
	    	Log.i(TAG, "Graph.onPause()");
	    	removeListeners();
	    	super.onPause();
	    }
	    
	    @Override
	    public void onStart() {
	    	Log.i(TAG, "Graph.onStart()");
	    	super.onStart();
	    }
	    
	    @Override
		public void onStop() {
            Log.i(TAG, "Graph.onStop()");
            removeListeners();
            super.onStop();
        }
 
        @Override
		public void onDestroy() {
            Log.i(TAG, "Graph.onDestroy()");
            super.onDestroy();
        }


		// Called whenever a new sensor reading is taken.
	    @Override
	    public synchronized void onSensorChanged(SensorEvent event) {
	    	
	    	if(event.sensor.getType() == Sensor.TYPE_GRAVITY) {
	    		dgravity = event.values.clone();
	    	}
	    	
	    	if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
	    		dmagfield = event.values.clone();
	    	}
	    	
	    	if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
		        // get rid the oldest sample in history:
		        if (z.size() > SIZE) {
		            z.removeFirst();
		            y.removeFirst();
		            x.removeFirst();
		        }
		 
		        dlin_acc = event.values.clone();
		        z_computeOrientation();
		        
		        // add the latest history sample:
		        x.addLast(null, dlin_acc[0]);
		        y.addLast(null, dlin_acc[1]);
		        z.addLast(null, dlin_acc[2]);
	    	}
	    	
	    	
	 
	        // redraw the Plots:
	        graph.redraw();
	    }
	 
	    @Override
	    public void onAccuracyChanged(Sensor sensor, int i) {
	        // Not interested in this event
	    }
	    
	    private void z_computeOrientation() {
	    	
	    	if (SensorManager.getRotationMatrix(zrotationMatrix, null, dgravity, dmagfield) ){ 
	    		//zorientation = SensorManager.getOrientation(zrotationMatrix, zrotationVector); // verder niet meer gebruikt
	    	}
	     
	    	android.opengl.Matrix.invertM(zrotationMatrix, 0, zrotationMatrix, 0);
	    	
	    	dlin_acc = remap(dlin_acc);
	    	
	    }
		
		private float[] remap(float data[]){	
			float dacc[] = new float [4];
			dacc[0] = data[0];
			dacc[1] = data[1];
			dacc[2] = data[2];
			dacc[3] = 0;
			
			float[] resultVec = new float [4];
			Matrix.multiplyMV(resultVec, 0, zrotationMatrix, 0, dacc, 0);
			
			return resultVec;
		}

		@Override
		public void update(Observable arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

}
