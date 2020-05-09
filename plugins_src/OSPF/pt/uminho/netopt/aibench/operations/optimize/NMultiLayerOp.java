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

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import pt.uminho.algoritmi.netopt.ospf.optimization.Params;
import pt.uminho.algoritmi.netopt.ospf.optimization.ResultOptim;
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliOSPF;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultOptimType;
import pt.uminho.netopt.aibench.gui.StatusHandler;

@Operation(name = "MultiLayerOp", description = "Multi Topology OSPF")
public class NMultiLayerOp {

	private ProjectBox projB;
	private Demands demands;
	private int ntopologies = 2;
	private Params params = new Params();
	private int nruns;
	private JecoliOSPF eaOspf;
	private boolean cancel=false;
	
	private Params.AlgorithmSelectionOption algorithm;
	public StatusHandler status = new StatusHandler("", "");

	@Port(direction = Direction.INPUT, name = "Project", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "Total Demands", order = 2)
	public void setDemands(Demands demands) {
		this.demands = demands;
	}

	@Port(direction = Direction.INPUT, name = "Number of Topologies", order = 3, defaultValue = "2")
	public void setTopologies(int n) {
		this.ntopologies = n;
	}

	/*
	 * @Port(direction = Direction.INPUT, name = "Optimize for Link Failure",
	 * order = 4) public void getLinkFailure(boolean linkFailure) {
	 * this.params.setLinkFailure(linkFailure); }
	 */
	@Port(direction = Direction.INPUT, name = "Population Size", order = 5, defaultValue = "100")
	public void setPopSize(int popul) {
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
	public void setNumberOfRuns(int runs) throws Exception{
		this.nruns = runs;
		this.run();
	}

	//@Port(direction = Direction.OUTPUT,order =1000)
	public void run() throws Exception {
		NetworkTopology topology = projB.getNetworkTopologyBox()
				.getNetworkTopology();//.copy();
		int n = ntopologies;
		Demands[] demand = new Demands[n];

		Demands d = new Demands(demands.getDimension());
		d.setFilename(demands.getFilename() + " 1/" + n + " part");

		for (int i = 0; i < demands.getDimension(); i++) {
			for (int j = 0; j < demands.getDimension(); j++) {
				d.setDemands(i, j, demands.getDemands(i, j) / n);
			}
		}

		for (int i = 0; i < n; i++) {
			demand[i] = d;
		}

		for (int i = 0; i < nruns &&!cancel; i++) {
			JecoliOSPF eaOspf = new JecoliOSPF(topology, demand, null);
			eaOspf.configureMultiLayerAlgorithm(params, n);
			
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

			ResultOptim results = new ResultOptim();
			
			results.addDemands(demands);
			results.addDemands(d);
			

			
			int[] sol = eaOspf.getBestSolutionWeights();
			
			
			
			
			NetworkLoads loads = new NetworkLoads(topology);

			for(int j=0;j<n;j++){
				int[] sol1 = Arrays.copyOfRange(sol,
						j * topology.getNumberEdges(),
						(j + 1) * topology.getNumberEdges());

				OSPFWeights weights = new OSPFWeights(topology.getDimension());
				weights.setWeights(sol1, topology);
				weights.setCreationMethod("SingleObjective - Topology " + j);
				results.addWeights(weights);

				projB.getSimul().computeLoads(weights,d);
				NetworkLoads ld = projB.getSimul().getLoads();
				ld.setName("Topology "+i+" with Partial demands");
				loads.addLoads(ld.getLoads());
			}
		
			double f=projB.getSimul().congestionMeasure(loads, demands);
			
			loads.setCongestion(f);
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
