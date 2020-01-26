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

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import pt.uminho.algoritmi.netopt.ospf.optimization.Params;
import pt.uminho.algoritmi.netopt.ospf.optimization.ResultOptim;
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliPValueInteger;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.PDEFTSimul;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultOptimType;
import pt.uminho.netopt.aibench.gui.StatusHandler;

@Operation(name = "SRPValuesOp", description = "PEFT/DEFT Weights and P Values Optimization ")
public class PDEFTWeightsAndPValuesOp {

	private ProjectBox projB;
	private Demands demands;
	private Params params = new Params();
	private int nruns;
	public StatusHandler status = new StatusHandler("", "");

	@Port(direction = Direction.INPUT, name = "ProjectBox", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "Demands", order = 2)
	public void setDemands(Demands demands) {
		this.demands = demands;
	}

	
	@Port(direction = Direction.INPUT, name = "Use DEFT", order = 3, defaultValue = "true")
	public void setUseUnit(boolean unit) {
		if (unit)
			this.params.setLoadBalancer(LoadBalancer.DEFT);
		else
			this.params.setLoadBalancer(LoadBalancer.PEFT);
	}

	
	@Port(direction = Direction.INPUT, name = "Population Size", order = 4, defaultValue = "100")
	public void setPopulationSize(int popul) {
		this.params.setPopulationSize(popul);
	}

	@Port(direction = Direction.INPUT, name = "Achive Size", order = 5, defaultValue = "100")
	public void setArchiveSize(int archiveSize) {
		this.params.setArchiveSize(archiveSize);
	}

	@Port(direction = Direction.INPUT, name = "Number of generations", order = 6, defaultValue = "100")
	public void setNumberGenerations(int numberGenerations) {
		this.params.setNumberGenerations(numberGenerations);
	}

	@Port(direction = Direction.INPUT, name = "Minimize Max. Link Usage", order = 7, defaultValue = "false")
	public void setMinimizeMaxUsage(boolean b) {
	}

	@Port(direction = Direction.INPUT, name = "Number os runs", order = 8, defaultValue = "1")
	public void setNumberOfRuns(int nruns) throws Exception {
		this.nruns = nruns;
		this.run();
	}

	//@Port(direction = Direction.OUTPUT,order =1000)
	public void run() throws Exception {
		NetworkTopology topology = projB.getNetworkTopologyBox().getNetworkTopology();
		for (int i = 0; i < nruns; i++) {

				JecoliPValueInteger eaOspf = new JecoliPValueInteger(topology, demands);
				String xLabel = "Iteration";
				String yLabel = "Congestion";

				eaOspf.configureWeightAndPValuesEA(params);
				status.setLabels(xLabel, yLabel);
				status.getMonitorPanel().setLogarithmScale(true);
				eaOspf.configureDefaultArchive(params);
				eaOspf.getEvaluationFunction().addEvaluationFunctionListener(status);

				status.setNumberOfFunctionEvaluations(params.getNumberGenerations());
				status.restartCounter();
				status.setNumberOfRuns(nruns);
				status.setCurrentRun(i + 1);

				eaOspf.setPlotter(status.getMonitorPanel());
				status.getMonitorPanel().setEvaluationFunction(eaOspf.getEvaluationFunction());

				eaOspf.run();

				double[] pvalues = eaOspf.getBestSolutionPvalues();
				int[] w = eaOspf.getBestSolutionWeights();
				OSPFWeights weights = new OSPFWeights(topology.getDimension());
				weights.setWeights(w, topology);
				weights.setCreationMethod("SingleObjective");
				
				ResultOptim results = new ResultOptim();
				results.addDemands(demands);
				results.addWeights(weights);
				results.setPValues(new PValues(pvalues));
					
				boolean b=params.getLoadBalancer().equals(LoadBalancer.DEFT)?true:false;
				PDEFTSimul simul = new PDEFTSimul(topology, b);// params.getLoadBalancer().equals(LoadBalancer.DEFT));
				double fitness = simul.evalPValues(pvalues,w, this.demands);
				System.out.println(fitness);
				NetworkLoads loads = simul.getLoads();

				results.addNetworkLoads(loads);
				ResultOptimType resultsT = new ResultOptimType(results);
				resultsT.setInfo("PDEFT\n" + params.toString());
				projB.addResultOptim(resultsT);

		} // end for
	}

	@Progress
	public StatusHandler<Double> getStatus() {
		return status;
	}

}
