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
package pt.uminho.netopt.aibench.gui.options;


public class EAGeneralBean {

	 
	public EAGeneralBean(){
		_lfRecovery = new LFRecoveryProperty("",-1);
	}
    
    public void setMaxWeight(Integer w){
    	this._maxWeight=w;
    }
    
    
    public Integer getMaxWeight(){
    	return this._maxWeight;
    }
   
    
    public void setMinWeight(Integer w){
    	this._minWeight=w;
    }
    
    
    public Integer getMinWeight(){
    	return this._minWeight;
    }
   
    
    public void setDefaultBandWidth(Double d){
    	this._defaultBandWidth=d;
    }
    public Double getDefaultBandWidth(){
    	return this._defaultBandWidth;
    }
    
    
    public void setDefaultDelay(Double d){
    	this._defaultDelay=d;
    }
    
    public Double getDefaultDelay(){
    	return this._defaultDelay;
    }
    
    public void setDeftThreshold(Double d){
    	this._deftThreshold=d;
    }
    
    public Double getDeftThreshold(){
    	return this._deftThreshold;
    }
  

    public void setPDefault(Double d){
    	this._deftDiviser =d;
    }
    
    public Double getPDefault(){
    	return this._deftDiviser;
    }
  
    
    public Double getPMin(){
    	return this._pmin;
    }
    
    public void setPMin(Double d){
    	this._pmin=d;
    }
 
    
    
    public Double getPMax(){
    	return this._pmax;
    }
    
    public void setPMax(Double d){
    	this._pmax=d;
    }
    
    
    public int getPIntScaller(){
    	return this._pIntScaller;
    } 
    
    
    public void setPIntScaller(int n){
    	this._pIntScaller=n;
    }
    
    
    public double getTwoPointCrossover() {
		return _twoPointCrossover;
	}


   public void setTwoPointCrossover(double _twopointCrossover) {
		this._twoPointCrossover = _twopointCrossover;
	}


   public double getUniformCrossover() {
		return _uniformCrossover;
	}


   public void setUniformCrossover(double _uniformCrossover) {
		this._uniformCrossover = _uniformCrossover;
	}


   public double getRandomMutation() {
		return _randomMutation;
	}


	public void setRandomMutation(double _randomMutation) {
		this._randomMutation = _randomMutation;
	}


	public double getIncrementalMutation() {
		return _incrementalMutation;
	}


	public void setIncrementalMutation(double _incrementalCrossover) {
		this._incrementalMutation = _incrementalCrossover;
	}


	public void setDeftApplyThreshold(boolean propertyBoolean) {
		this._deftApplyThreshold=propertyBoolean;
	}
	
	public boolean getDeftApplyThreshold() {
		return this._deftApplyThreshold;
	}
	
	public int getNumberParallelThreads(){
		return this._numberParallelThreads;
	}
	
	public void setNumberParallelThreads(int n){
		this._numberParallelThreads =n;
	}
	
	
	public double getHybridSRPenaltyFactor(){
		return this._hybridSRPenaltyFactor;
	}
	
	public void setHybridSRPenaltyFactor(double d){
		this._hybridSRPenaltyFactor =d;
	}
	
	
	public LFRecoveryProperty getLFRecoveryProperty(){
		return this._lfRecovery;
	}
	
	public void setLFRecoveryProperty(LFRecoveryProperty property){
		this._lfRecovery=property;
	}
	
	public int getSRNodepIterations() {
		return _srNodepIterations;
	}

	public void setSRNodepIterations(int _srNodepIterations) {
		this._srNodepIterations = _srNodepIterations;
	}

	//default MIN and MAX Weight
    private int _minWeight=1;
    private int _maxWeight=20;
    // Default arc configurations
    private double _defaultBandWidth=1000.0;
    private double _defaultDelay=1.0;
    // DEFT /PEFT /SR
    private double _deftDiviser = 1.0;
    private double _deftThreshold = 0.01;
    private boolean _deftApplyThreshold =false;
    // P values
    private int _pIntScaller=10;
    private double _pmin=0.1;
    private double _pmax=10.0;
    // Operators rate
    private double _twoPointCrossover= 0.25;
    private double _uniformCrossover = 0.25;
    private double _randomMutation = 0.25;
    private double _incrementalMutation =0.25;
	// multi threading
    private int _numberParallelThreads = 4;
	// Hybrid IP/SR penalty for non SP closeness over SR domain
    private double _hybridSRPenaltyFactor = 0.0;
    // SR Simulator LF Recovery
    private LFRecoveryProperty _lfRecovery;
    private int _srNodepIterations;
}

