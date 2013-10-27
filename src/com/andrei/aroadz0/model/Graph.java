package com.andrei.aroadz0.model;

import java.util.Observable;
import java.util.Observer;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.andrei.aroadz0.R;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class Graph implements Observer{

	private static final String LOG_TAG = "zGraph"; 
	private static final int SIZE = 100;            // number of points to plot in history
	private static final int BOUNDRY_BOTTOM = -24;  // y-min
	private static final int BOUNDRY_TOP = 24;		// y-max
	
	private static final int THRESHOLD_1 = 4;
	private static final int THRESHOLD_2 = 9;
	private static final int THRESHOLD_3 = 16;
	
	
    private XYPlot graph = null;
	
//    private SimpleXYSeries x = null;
//    private SimpleXYSeries y = null;
    private SimpleXYSeries z = null;
    
    private SimpleXYSeries th1 = null;
    private SimpleXYSeries th1M = null;
    
    
	private View view = null;
    
	
	public Graph(View view) {
		this.view  = view;
		create();
	}
	
	 
    private void create() {
        // setup the APR History plot:
        graph = (XYPlot) view.findViewById(R.id.aprHistoryPlot);
 
//        x = new SimpleXYSeries("Acc X");
//        x.useImplicitXVals();
//        y = new SimpleXYSeries("Acc Y");
//        y.useImplicitXVals();
        z = new SimpleXYSeries("Acc Z");
        z.useImplicitXVals();
 
        LineAndPointFormatter l = new LineAndPointFormatter(Color.rgb(255, 100, 100), null, null, null);
        Paint p = l.getLinePaint();
        p.setStrokeWidth(5);
        l.setLinePaint(p);
//        graph.addSeries(x, new LineAndPointFormatter(Color.rgb(100, 100, 200), null, null, null));
//        graph.addSeries(y, new LineAndPointFormatter(Color.rgb(100, 200, 100), null, null, null));
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
       	
	}
    
    public void redraw(Data data) {
    	
    	if (z.size() > SIZE) {
            z.removeFirst();
        }
        
        // add the latest history sample:
    	z.addLast(null, data.getAcczaR());
    	Log.d(LOG_TAG, ""+data.getAcczaR());
        graph.redraw();
    }

	
	@Override
	public void update(Observable observable, Object data) {
		this.redraw(((Data) data));
		
	}
}
