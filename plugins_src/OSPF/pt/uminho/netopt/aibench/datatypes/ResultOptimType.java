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
package pt.uminho.netopt.aibench.datatypes;

import java.io.Serializable;
import pt.uminho.algoritmi.netopt.ospf.optimization.Params;
import pt.uminho.algoritmi.netopt.ospf.optimization.Params.AlgorithmSelectionOption;
import pt.uminho.algoritmi.netopt.ospf.optimization.ResultOptim;
import pt.uminho.algoritmi.netopt.ospf.simulation.DelayRequests;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import es.uvigo.ei.aibench.core.datatypes.annotation.Clipboard;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@SuppressWarnings("serial")
@Datatype(structure = Structure.COMPLEX, namingMethod = "getName", setNameMethod = "setName",removable=true)
public class ResultOptimType implements Serializable{
	private ResultOptim results;
	private OSPFWeightsBox weightsB =null;
	private PValuesBox pvaluesB =null;
	private DemandsBox demandsB =null;;
	private NetworkLoadBox loadsB = null;;
	private AverageEndToEndDelayBox endToEndDelayB =null;
	private String name;
	private String info;
	private Params.AlgorithmSelectionOption algorithm;

	public ResultOptimType(ResultOptim results) {
		this.results = results;
		if(results.getDemands().size()>0){
			demandsB = new DemandsBox();
			demandsB.setDemands(results.getDemands());
		}
		if(results.getNetworkLoads().size()>0){
			loadsB = new NetworkLoadBox();
			loadsB.setLoads(results.getNetworkLoads());
		}
		if(results.getAverageEndtoEndDelays().size()>0){
			endToEndDelayB=new AverageEndToEndDelayBox();
			endToEndDelayB.setAverageEndToEndDelays(results.getAverageEndtoEndDelays());
		}
		if(results.getWeights().size()>0){
			weightsB = new OSPFWeightsBox();
			weightsB.setWeights(results.getWeights());
		}
		if(results.getPValues()!=null){
			this.pvaluesB= new PValuesBox();
			this.pvaluesB.add(results.getPValues());
		}
	
	
		this.name="Optimization Results";
		
	}

	@Clipboard(name = "Network Loads",order=1)
	public NetworkLoadBox getNetworkLoads() {
		return loadsB;
	}
	
	
	@Clipboard(name = "Demands",order=3)
	public DemandsBox getDemands() {
		return demandsB;
	}
	
	@Clipboard(name = "Average End to End Delays",order=5)
	public AverageEndToEndDelayBox getEndToEndDelays() {
		return endToEndDelayB;
	}
	
	@Clipboard(name = "Delay Requests",order=6)
	public DelayRequests getDelayReqs() {
		return results.getDelayReqs();
	}

	@Clipboard(name = "Weights",order=7)
	public OSPFWeightsBox getWeights() {
		return weightsB;
	}

	
	@Clipboard(name = "Population",order=8)
	public Population getPopulation() {
		return results.getPopulation();
	}
	
	@Clipboard(name = "P Values",order=9)
	public PValuesBox getPValues() {
		return this.pvaluesB;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
	public AlgorithmSelectionOption getAlgorithm() {
		return this.algorithm;
	}

	public void setAlgorithm(Params.AlgorithmSelectionOption algorithm) {
		this.algorithm=algorithm;
	}
	
	
	
}
