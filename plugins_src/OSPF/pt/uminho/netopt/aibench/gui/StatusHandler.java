/**
* Copyright 2012-2017,
* Centro Algoritmi
* University of Minho
*
* This is free software: you can redistribute it and/or modify
* it under the terms of the GNU Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Public License for more details.
*
* You should have received a copy of the GNU Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
* 
* @author Vítor Pereira
*/
package pt.uminho.netopt.aibench.gui;

import jecoli.algorithm.components.evaluationfunction.EvaluationFunctionEvent;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunctionListener;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import es.uvigo.ei.aibench.core.operation.annotation.MonitorPanel;
import es.uvigo.ei.aibench.core.operation.annotation.ProgressProperty;


public class StatusHandler<T> implements IEvaluationFunctionListener<ILinearRepresentation<T>>{
	
	public float progress ;
	public String status = "0.0%";
	protected int current=0;
	private int numberOfRuns =1;
	private int currentRun =1;
	private int maxNumberOfFitnessFunctionEvaluations = 0;
	final public MOEAPlotterPanel<ILinearRepresentation<T>> monitorPanel = new MOEAPlotterPanel<ILinearRepresentation<T>>(this);
	

	
	public StatusHandler(){
		monitorPanel.setXLabel("X");
		monitorPanel.setYLabel("Y");
	}
	
	public StatusHandler(String xLabel,String yLabel){
		monitorPanel.setXLabel(xLabel);
		monitorPanel.setYLabel(yLabel);
	}
	
	public void setLabels(String xLabel,String yLabel){
		monitorPanel.setXLabel(xLabel);
		monitorPanel.setYLabel(yLabel);
	}

	@Override
	public void processEvaluationFunctionEvent(EvaluationFunctionEvent<ILinearRepresentation<T>> event) {
//		String id = event.getId();
//		if(id.equals(EvaluationFunctionEvent.SOLUTIONSET_EVALUATION_EVENT)){						
//			increment();
//		}
	}

	
	public void incrementProgress(){
			current++;
			float progress = (float)current/(float)maxNumberOfFitnessFunctionEvaluations;
			int progressRound = Math.round(progress*100);
			if(progressRound > 100){
				progressRound = 100;
				progress = 1;
			}
			setProgress(progress);
			setStatus(""+progressRound+"% ( run "+this.currentRun+" of "+this.numberOfRuns+")");	
	}
	
	@ProgressProperty(order = 1, label = "Total progress: ", showProgressBarLabel = true)
	synchronized public float getProgress() {
		return progress;
	}
	
	synchronized public void setProgress(float progress) {
		this.progress= progress;
	}   
	
	@ProgressProperty(order =2, label = "Status:",showProgressBarLabel = true)
	synchronized public String getStatus(){
		return status;
	}
	
	synchronized public void setStatus(String status){
		this.status = status;
	}
	
	
	@ProgressProperty(ignore = true)
	public int getNumberOfFunctionEvaluations() {
		return this.maxNumberOfFitnessFunctionEvaluations;
	}
	
	public void setNumberOfFunctionEvaluations(int maxNumberOfFitnessFunctionEvaluations) {
		this.maxNumberOfFitnessFunctionEvaluations = maxNumberOfFitnessFunctionEvaluations;
	}
	
	public void restartCounter(){
		this.current=0;
		this.progress=0;
		this.monitorPanel.clearPlot();
	} 
	
	public void setNumberOfRuns(int n){
		this.numberOfRuns=n;
	}
	
	
	@ProgressProperty(ignore = true)
	public int  getNumberOfRuns(){
		return this.numberOfRuns;
	}
	
	@ProgressProperty(ignore = true)
	public int getCurrentRun(){
		return this.currentRun;
	}
	
	public void setCurrentRun(int n){
		this.currentRun=n;
	}
	
	@MonitorPanel
	@ProgressProperty(ignore = true)
	public MOEAPlotterPanel<ILinearRepresentation<T>> getMonitorPanel() {	
		return monitorPanel;
	}
}
