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
import java.util.Observable;

import es.uvigo.ei.aibench.core.datatypes.annotation.Clipboard;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.algoritmi.netopt.ospf.simulation.DelayRequests;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetGraph;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.SRConfiguration;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.SRSimulator;

@SuppressWarnings("serial")
@Datatype(structure = Structure.COMPLEX, namingMethod = "getName", setNameMethod = "setName")
public class ProjectBox extends Observable implements Serializable {
	private NetworkTopologyBox topolB;
	private DemandsBox demandsB;
	private DelayRequestsBox delayReqsB;
	private OSPFWeightsBox weightsB;
	private ResultOptimBox resultsOptimB;
	private ResultSimulBox resultsSimulB;
	private WeightsComparisonBox weightsComparationB;
	private PopulationBox populationBox;
	private PopulationsComparisonBox populationsComparationB;
	private WeightsGraphBox weightsGraphB;
	private PValuesBox pvaluesB;
	private SRConfigurationBox srConfigurationBox;
	private SRSimulatorBox srSimutarorBox;
	private Simul simul;
	private String name;
	

	
	public ProjectBox(){
		try {
			topolB = new NetworkTopologyBox();
			demandsB = new DemandsBox();
			delayReqsB = new DelayRequestsBox();
			weightsB = new OSPFWeightsBox();
			pvaluesB = new PValuesBox();
			resultsSimulB = new ResultSimulBox();
			simul = new Simul(topolB.getNetworkTopology());
			resultsOptimB = new ResultOptimBox();
			weightsComparationB=new WeightsComparisonBox();
			populationsComparationB=new PopulationsComparisonBox();
			weightsGraphB=new WeightsGraphBox();
			srSimutarorBox= new SRSimulatorBox();
			srConfigurationBox = new SRConfigurationBox();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public ProjectBox(NetGraph graph){
		try {
			topolB = new NetworkTopologyBox(graph);
			demandsB = new DemandsBox();
			delayReqsB = new DelayRequestsBox();
			weightsB = new OSPFWeightsBox();
			pvaluesB = new PValuesBox();
			resultsSimulB = new ResultSimulBox();
			simul = new Simul(topolB.getNetworkTopology());
			resultsOptimB = new ResultOptimBox();
			weightsComparationB=new WeightsComparisonBox();
			populationsComparationB=new PopulationsComparisonBox();
			weightsGraphB=new WeightsGraphBox();
			srSimutarorBox= new SRSimulatorBox();
			srConfigurationBox = new SRConfigurationBox();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ProjectBox(String filenameNodes, String filenameEdges) {
		try {
			topolB = new NetworkTopologyBox(filenameNodes, filenameEdges);
			demandsB = new DemandsBox();
			delayReqsB = new DelayRequestsBox();
			weightsB = new OSPFWeightsBox();
			pvaluesB = new PValuesBox();
			resultsSimulB = new ResultSimulBox();
			simul = new Simul(topolB.getNetworkTopology());
			resultsOptimB = new ResultOptimBox();
			weightsComparationB=new WeightsComparisonBox();
			populationsComparationB=new PopulationsComparisonBox();
			weightsGraphB=new WeightsGraphBox();
			srSimutarorBox= new SRSimulatorBox();
			srConfigurationBox = new SRConfigurationBox();
			populationBox =new PopulationBox();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public ProjectBox(NetworkTopology tp) {
		try {
			topolB = new NetworkTopologyBox(tp);
			demandsB = new DemandsBox();
			delayReqsB = new DelayRequestsBox();
			weightsB = new OSPFWeightsBox();
			pvaluesB = new PValuesBox();
			resultsSimulB = new ResultSimulBox();
			simul = new Simul(topolB.getNetworkTopology());
			resultsOptimB = new ResultOptimBox();
			weightsComparationB=new WeightsComparisonBox();
			weightsGraphB=new WeightsGraphBox();
//			flowManagerBox= new FlowManagerBox();
			populationBox =new PopulationBox();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	@Clipboard(name = "Network Topology Box",order=1)
	public NetworkTopologyBox getNetworkTopologyBox() {
		return this.topolB;
	}

	@Clipboard(name = "Demands Box",order=2)
	public DemandsBox getDemands() {
		return this.demandsB;
	}

	@Clipboard(name = "Delay Requests Box",order=3)
	public DelayRequestsBox getDelayReqs() {
		return this.delayReqsB;
	}

	@Clipboard(name = "OSPFWeights Box",order=4)
	public OSPFWeightsBox getOSPFWeights() {
		return this.weightsB;
	}

	@Clipboard(name = "ResultSimul Box",order=5)
	public ResultSimulBox getResultSimul() {
		return this.resultsSimulB;
	}

	@Clipboard(name = "ResultOptim Box",order=6)
	public ResultOptimBox getResultOptim() {
		return this.resultsOptimB;
	}
	
	@Clipboard(name = "Weights Comparisons Box",order=7)
	public WeightsComparisonBox getWeightsComparisons() {
		return this.weightsComparationB;
	}
	
	@Clipboard(name = "Custom Populations Box",order=8)
	public PopulationBox getPopulations() {
		return this.populationBox;
	}
	
	@Clipboard(name = "Populations Comparisons Box",order=9)
	public PopulationsComparisonBox getPopulationsComparisons() {
		return this.populationsComparationB;
	}
	
	@Clipboard(name = "Weights Graph Box",order=10)
	public WeightsGraphBox getWeightsGraphs() {
		return this.weightsGraphB;
	}
	
	
	@Clipboard(name = "SR Simulator Box",order=11)
	public SRSimulatorBox getSRSimulators() {
		return this.srSimutarorBox;
	}
	
	
	@Clipboard(name = "P Values Box",order=12)
	public PValuesBox getPValues() {
		return this.pvaluesB;
	}
	
	@Clipboard(name = "SR Configuration Box",order=13)
	public SRConfigurationBox getSRConfigurations() {
		return this.srConfigurationBox;
	}
	
	//**************************************************************************
	
	public void addDemands(Demands demands) {
		this.demandsB.addDemands(demands);
	}

	public void addDelayRequests(DelayRequests delayRequests) {
		this.delayReqsB.addDelayRequests(delayRequests);
	}

	public void addOSPFWeights(OSPFWeights weights) {
		this.weightsB.addOSPFWeights(weights);
	}

	public void addResultSimul(ResultSimulType resultsSimulT) {
		this.resultsSimulB.addResultSimul(resultsSimulT);
	}

	public void addResultOptim(ResultOptimType resultsOptimT) {
		this.resultsOptimB.addResultOptim(resultsOptimT);
	}

	public void setSimul(Simul simul) {
		this.simul = simul;
	}

	public Simul getSimul() {
		return simul;
	}

	public void addWeightsComparation(WeightsComparison wc) {
		this.weightsComparationB.addWeightsComparation(wc);
	}
	
	public void addPopulationComparation(PopulationsComparison pc) {
		this.populationsComparationB.addPopulationsComparation(pc);
	}

	public void addSRSimulator(SRSimulator sim) {
		this.srSimutarorBox.add(sim);		
	}

	
	public void addCustomPopulation(Population population) {
		this.populationBox.add(population);
	}
	
	   
	public void addWeightGraph(WeightsGraph wg) {
		this.weightsGraphB.addWeightsGraph(wg);
	}


	public void addPValues(PValues p) {
		pvaluesB.add(p);	
	}
	
	public void addSRConfiguration(SRConfiguration c) {
		this.srConfigurationBox.addSRConfiguration(c);	
	}


	
	
	
}
