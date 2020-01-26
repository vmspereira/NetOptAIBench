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
package pt.uminho.netopt.aibench.operations.optimize;

import java.util.Arrays;


import pt.uminho.algoritmi.netopt.ospf.optimization.Params;
import pt.uminho.algoritmi.netopt.ospf.optimization.ResultOptim;
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliOSPF;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultOptimType;
import pt.uminho.netopt.aibench.gui.StatusHandler;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(name = "MultiLayerOp", description = "Multi Topology OSPF")
public class MultiLayerOp {

	private ProjectBox projB;
	private Demands demands;
	private Demands demands2;
	private Params params;
	private int runs;
	private boolean cancel=false;
	private JecoliOSPF eaOspf;
	
	private Params.AlgorithmSelectionOption algorithm;
	public StatusHandler status = new StatusHandler("", "");

	@Port(direction = Direction.INPUT, name = "Project", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "First Layer Demands", order = 2)
	public void setDemands(Demands demands) {
		this.demands = demands;
	}

	@Port(direction = Direction.INPUT, name = "Second Layer Demands", order = 3)
	public void setDRs(Demands demands2) throws Exception {
		this.demands2 = demands2;
	}
/*
	@Port(direction = Direction.INPUT, name = "Optimize for Link Failure", order = 4)
	public void getLinkFailure(boolean linkFailure) {
		this.params.setLinkFailure(linkFailure);
	}
*/
	@Port(direction = Direction.INPUT, name = "Population Size", order = 5, defaultValue = "100")
	public void setPopSize(int popul) {
		this.params = new Params();
		this.params.setPopulationSize(popul);
	}

	@Port(direction = Direction.INPUT, name = "Archive Size", order = 6, defaultValue = "100")
	public void setArchiveSize(int archiveSize) {
		this.params.setArchiveSize(archiveSize);
	}

	@Port(direction = Direction.INPUT, name = "Number of generations", order = 7, defaultValue = "10")
	public void setNumberGenerations(int numberGenerations) {
		this.params.setNumberGenerations(numberGenerations);
	}

	@Port(direction = Direction.INPUT, name = "Number of runs", order = 8, defaultValue = "1")
	public void setNumberOfRuns(int runs) throws Exception {
		this.runs = runs;
		this.run();
	}

	//@Port(direction = Direction.OUTPUT,order =1000)
	public void run() throws Exception {
		NetworkTopology topology = projB.getNetworkTopologyBox()
				.getNetworkTopology();//.copy();

		Demands[] demand = new Demands[2];
		demand[0] = demands;
		demand[1] = demands2;

		Demands totalDemands = new Demands(topology.getDimension());
		totalDemands.add(demands);
		totalDemands.add(demands2);
		totalDemands.setFilename("d1+d2");

		for (int i = 0; i < runs && !cancel; i++) {

			eaOspf = new JecoliOSPF(topology, demand, null);
			eaOspf.configureMultiLayerAlgorithm(params);
			
			status.setLabels("Iteration", "Fitness");
			this.algorithm =Params.AlgorithmSelectionOption.SOEA;
			status.getMonitorPanel().setLogarithmScale(true);
			eaOspf.configureDefaultArchive(params);
			eaOspf.getEvaluationFunction()
					.addEvaluationFunctionListener(status);
		
			status.setNumberOfFunctionEvaluations(params.getNumberGenerations());
			eaOspf.setPlotter(status.getMonitorPanel());
			status.getMonitorPanel().setEvaluationFunction(
					eaOspf.getEvaluationFunction());

			
			eaOspf.run();
			
			
			int[] sol = eaOspf.getBestSolutionWeights();
			int[] sol1 = Arrays.copyOfRange(sol, 0, topology.getNumberEdges());
			int[] sol2 = Arrays.copyOfRange(sol, topology.getNumberEdges(),
					topology.getNumberEdges() * 2);

			ResultOptim results = new ResultOptim();
			results.addDemands(demands);
			results.addDemands(demands2);
			results.addDemands(totalDemands);

			results.setSolution(eaOspf.getBestSolution());
			results.setPopulation(new Population(eaOspf.getSolutionSet()));

			NetworkLoads loads = new NetworkLoads(topology);

			OSPFWeights weights = new OSPFWeights(topology.getDimension());
			weights.setWeights(sol1, topology);
			weights.setCreationMethod("SingleObjective - Topology 1");
			results.addWeights(weights);
			projB.getSimul().computeLoads(weights, demands);
			NetworkLoads ld = projB.getSimul().getLoads();
			ld.setName("Topology 1 with Partial demands");
			results.addNetworkLoads(ld);
			// first loads
			loads.addLoads(ld.getLoads());

			OSPFWeights weights2 = new OSPFWeights(topology.getDimension());
			weights2.setWeights(sol2, topology);
			weights2.setCreationMethod("SingleObjective - Topology 2");
			results.addWeights(weights2);
			projB.getSimul().computeLoads(weights2, demands2);
			ld = projB.getSimul().getLoads();
			ld.setName("Topology 2 with Partial demands");
			results.addNetworkLoads(ld);
			// second loads
			loads.addLoads(ld.getLoads());

			projB.getSimul().computeLoads(weights, totalDemands);
			ld = projB.getSimul().getLoads();
			ld.setName("Topology 1 with Total demands");
			results.addNetworkLoads(ld);

			projB.getSimul().computeLoads(weights2, totalDemands);
			ld = projB.getSimul().getLoads();
			ld.setName("Topology 2 with Total demands");
			results.addNetworkLoads(ld);

			loads.computeU(topology.getGraph());
			double d = eaOspf.getBestSolution().getFitnessValue();
			loads.setCongestion(d);
			loads.setName("Total loads");
			results.addNetworkLoads(loads);

			ResultOptimType resultsT = new ResultOptimType(results);
			resultsT.setInfo(eaOspf.getInfo());
			resultsT.setAlgorithm(this.algorithm);
			projB.addResultOptim(resultsT);
		}

	}

	@Progress
	public StatusHandler getStatus() {
		return status;
	}
	
	@Cancel
	public synchronized void cancel(){
		this.cancel = true;
		eaOspf.cancel();	
	}
}
