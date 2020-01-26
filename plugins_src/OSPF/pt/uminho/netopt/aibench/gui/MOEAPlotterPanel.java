/**
 * 
 */
package pt.uminho.netopt.aibench.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import jecoli.algorithm.components.evaluationfunction.EvaluationFunctionEvent;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunctionListener;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionSet;
import jecoli.algorithm.multiobjective.archive.components.AMFunctionType;
import jecoli.algorithm.multiobjective.archive.plotting.IPlotter;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RectangleInsets;




public class MOEAPlotterPanel<T extends IRepresentation> extends JPanel implements IPlotter<T>, IEvaluationFunctionListener<ILinearRepresentation<Integer>>{

	
	private static final long serialVersionUID = 1L;
	public static final String AXIS_COMBOS_ACTION_COMMAND = "axisCombosActionCommand";
	protected int numObjs = 0;

	protected int objectiveIndex1 = 0;
	protected int objectiveIndex2 = 1;
	protected IEvaluationFunction<T> evaluationFunction;
	protected JFreeChart chart = null;	
	protected ChartPanel chartPanel = null;
	protected JPanel centralPanel = null;	
	protected JPanel controlPanel;
	private String xLabel = "X";
	private String yLabel = "Y";
	private StatusHandler handler;
	
	private boolean logarithm=false;
	
	
	//single objective function only
	private boolean isSingleMaximization;
	private ArrayList<Double> bestSingleFitness = null;
	private double bestFit;
	
	public MOEAPlotterPanel(StatusHandler handler){
		super();	
		this.handler=handler;
		initGUI();
	}
	
	
	public void setLogarithmScale(boolean b){
		this.logarithm=b;
	}
	
	public void setEvaluationFunction(IEvaluationFunction<T> eval) throws Exception{
		this.evaluationFunction = eval;	
		numObjs = this.evaluationFunction.getNumberOfObjectives();
		
		//single
		if(numObjs==1){
			if(bestSingleFitness==null)
				bestSingleFitness = new ArrayList<Double>();
			isSingleMaximization = false;
			bestFit = Double.MAX_VALUE;
		}
		
		createPlot();		
		createPlotControlPanel();

	}
	private void initGUI() {
		setLayout(new BorderLayout());
		this.setPreferredSize(new java.awt.Dimension(500, 270));
		this.setSize(new java.awt.Dimension(500, 270));
		centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		centralPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		centralPanel.setSize(new java.awt.Dimension(500, 270));
		centralPanel.setMinimumSize(new java.awt.Dimension(500, 270));
		centralPanel.setBackground(Color.WHITE);
		add(centralPanel,BorderLayout.CENTER);
	}
	
	@SuppressWarnings("serial")
	public void createPlot() throws Exception {
		chart = createChart();
		rendererDefaults();
		chart.setTextAntiAlias(true);
		chart.setAntiAlias(true);
	
		chart.setBackgroundPaint(Color.WHITE);
		chart.getPlot().setBackgroundPaint(Color.BLACK);
		((XYPlot)chart.getPlot()).setAxisOffset(new RectangleInsets(0.2, 0.01, 0.2, 0.01));
		chartPanel = new ChartPanel(chart){
			
			@Override
			public void paintComponent(java.awt.Graphics g){
				try{
					super.paintComponent(g);
				}
				catch(java.util.ConcurrentModificationException e){
				}
			}
		};
		chartPanel.setBackground(Color.WHITE);
		chart.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		chartPanel.setSize(new java.awt.Dimension(500, 270));
		chartPanel.setMinimumDrawHeight(10);
		chartPanel.setMaximumDrawHeight(2000);
		chartPanel.setMinimumDrawWidth(20);
		chartPanel.setMaximumDrawWidth(2000);
		
		centralPanel.add(chartPanel,BorderLayout.CENTER);
	}
	
	private void createPlotControlPanel(){
		controlPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[]{0.1,0.1,0.1,0.1};
		layout.rowWeights = new double[]{0.1,0.1};		
		controlPanel.setLayout(layout);
		
		add(controlPanel,BorderLayout.SOUTH);
	}
	
	protected JFreeChart createChart() throws Exception{
		String label=yLabel;
		if(this.logarithm)
			label=yLabel+" (Log)";
		JFreeChart chart = ChartFactory.createScatterPlot(null,xLabel,label, null, PlotOrientation.VERTICAL ,true,true,true);				
		return chart;             
	}
	
	public void rendererDefaults(){

		boolean mo = (numObjs>1);
		Shape shape  = new Ellipse2D.Double(0, 0, 3, 3);
		
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(false);
        xyPlot.setRangeCrosshairVisible(false);	
        xyPlot.setAxisOffset(new RectangleInsets(0.2, 0.01, 0.2, 0.01));
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
        if(mo)
        	renderer.setSeriesLinesVisible(0, false);
        else
        	renderer.setSeriesLinesVisible(0, true);
        
        renderer.setSeriesShapesFilled(0, true);
        renderer.setSeriesShapesFilled(1, true);
        renderer.setSeriesPaint(0, Color.green);
        renderer.setSeriesOutlinePaint(0, Color.green);
        renderer.setSeriesShape(0, shape);
        renderer.setSeriesShape(1, shape);
        
	}
	
	public void clearPlot(){
		bestSingleFitness = new ArrayList<Double>();
		bestFit = Double.MAX_VALUE;
	}

	@Override
	public AMFunctionType getFunctionType() {	
		return AMFunctionType.PLOTTER;
	}

	
	@Override
	public void plot(double[][] data) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("data", data);
		rendererDefaults();
		((XYPlot) chart.getPlot()).setDataset(dataset);
		((XYPlot) chart.getPlot()).setAxisOffset(new RectangleInsets(0.2, 0.01, 0.2, 0.01));
		chart.removeLegend();
	}

	
	public void plot(List<Population> list){
		this.numObjs=2;
		DefaultXYDataset dataset = new DefaultXYDataset();
		for(int i=0;i<list.size();i++)		
			dataset.addSeries("data"+i, list.get(i).getParetoMatrixTranspose());
		rendererDefaults();
		((XYPlot) chart.getPlot()).setDataset(dataset);
		((XYPlot) chart.getPlot()).setAxisOffset(new RectangleInsets(0.2, 0.01, 0.2, 0.01));
		chart.removeLegend();
	}
	
	
	public void plot(Population population){
		this.numObjs=2;
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("data",population.getParetoMatrixTranspose());
		rendererDefaults();
		((XYPlot) chart.getPlot()).setDataset(dataset);
		((XYPlot) chart.getPlot()).setAxisOffset(new RectangleInsets(0.2, 0.01, 0.2, 0.01));
		chart.removeLegend();
	}
	
	
	@Override
	public  void plot(ISolutionSet<T> solset) {
		if(solset.getNumberOfSolutions()>0){

			if(numObjs <1)
				numObjs = solset.getNumberOfObjectives();
			
			if(numObjs==1){
				  double value= (isSingleMaximization) ? solset.getHighestValuedSolutions(1).get(0).getScalarFitnessValue() 
						 									: solset.getLowestValuedSolutions(1).get(0).getScalarFitnessValue();
				  double currentFit;
				  if(this.logarithm)
					  currentFit=Math.log10(value);
				  else
					  currentFit=value;

				 if(isSingleMaximization){
					 if(currentFit>bestFit)
						 bestFit = currentFit;
				 }
				 else 
					 if(currentFit<bestFit) 
						 bestFit = currentFit;
			 
				 bestSingleFitness.add(bestFit);
				 
				 double[][] data = new double[2][bestSingleFitness.size()];
				 for(int i=0; i<bestSingleFitness.size(); i++){
					 data[0][i] = (double) i;
					 data[1][i] = bestSingleFitness.get(i);
				 }
				 
				 this.plot(data);
				
			}
			else if(numObjs>1){

				if(numObjs > objectiveIndex1 && numObjs > objectiveIndex2){
					List<ISolution<T>> sollist = solset.getListOfSolutions();
					double[][] data = new double[2][sollist.size()];

					for(int i=0; i<sollist.size(); i++){
						data[0][i] = sollist.get(i).getFitnessValue(objectiveIndex1);
						data[1][i] = sollist.get(i).getFitnessValue(objectiveIndex2);
					}

					this.plot(data);
				

				}else throw new IllegalArgumentException("Selected objectives exceed number of objectives");
			}
		}
		if(this.handler!=null)
			this.handler.incrementProgress();
		else{
			this.revalidate();
			this.repaint();
		}
	}
	
	
	public void setYLabel(String label){
		this.yLabel=label;
	}
	
	public void setXLabel(String label){
		this.xLabel=label;
	}
	
	public void setNumberOfObjectives(int n){
		this.numObjs=n;
	}


	@Override
	public void processEvaluationFunctionEvent(EvaluationFunctionEvent<ILinearRepresentation<Integer>> event) {
		// TODO Auto-generated method stub	
	}

}
