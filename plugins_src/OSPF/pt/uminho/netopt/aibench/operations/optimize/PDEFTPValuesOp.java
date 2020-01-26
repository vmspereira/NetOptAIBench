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

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import pt.uminho.algoritmi.netopt.ospf.optimization.Params;
import pt.uminho.algoritmi.netopt.ospf.optimization.ResultOptim;
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliOSPF;
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliPValueInteger;
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliPValueReal;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.PDEFTSimul;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.SRSimul;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultOptimType;
import pt.uminho.netopt.aibench.gui.StatusHandler;

@Operation(name = "SRPValuesOp", description = "PEFT/DEFT P Values Optimization ")
public class PDEFTPValuesOp {

	private ProjectBox projB;
	private Demands demands;
	private OSPFWeights weights;
	private Params params = new Params();
	private int nruns;
	private boolean minimizeMaxUsage = false;
	private JecoliPValueInteger eaOspf;
	private boolean cancel = false;

	public StatusHandler status = new StatusHandler("", "");

	@Port(direction = Direction.INPUT, name = "ProjectBox", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "Demands", order = 2)
	public void setDemands(Demands demands) {
		this.demands = demands;
	}

	@Port(direction = Direction.INPUT, name = "OSPFWeights", order = 3)
	public void setWeights(OSPFWeights weights) {
		this.weights = weights;
	}

	@Port(direction = Direction.INPUT, name = "Use DEFT", order = 4, defaultValue = "true")
	public void setUseUnit(boolean unit) {
		if (unit)
			this.params.setLoadBalancer(LoadBalancer.DEFT);
		else
			this.params.setLoadBalancer(LoadBalancer.PEFT);
	}

	//@Port(direction = Direction.INPUT, name = "Use Integer Encoding", order = 5, defaultValue = "false")
	//public void setIntegerEncoding(boolean b) {
	//}

	@Port(direction = Direction.INPUT, name = "Population Size", order = 6, defaultValue = "100")
	public void setPopulationSize(int popul) {
		this.params.setPopulationSize(popul);
	}

	@Port(direction = Direction.INPUT, name = "Achive Size", order = 7, defaultValue = "100")
	public void setArchiveSize(int archiveSize) {
		this.params.setArchiveSize(archiveSize);
	}

	@Port(direction = Direction.INPUT, name = "Number of generations", order = 8, defaultValue = "100")
	public void setNumberGenerations(int numberGenerations) {
		this.params.setNumberGenerations(numberGenerations);
	}

	@Port(direction = Direction.INPUT, name = "Minimize Max. Link Usage", order = 9, defaultValue = "false")
	public void setMinimizeMaxUsage(boolean b) {
		this.minimizeMaxUsage = b;
	}

	@Port(direction = Direction.INPUT, name = "Number os runs", order = 10, defaultValue = "1")
	public void setNumberOfRuns(int nruns) throws Exception {
		this.nruns = nruns;
		run();
	}

	
	
	public void run() throws Exception {
		NetworkTopology topology = projB.getNetworkTopologyBox().getNetworkTopology();
		for (int i = 0; i < nruns && !cancel; i++) {

	//		if (this.integerEncoding) 
	//		{
				eaOspf = new JecoliPValueInteger(topology.copy(), demands, weights);
				String xLabel = "Iteration";
				String yLabel = "Congestion";
				if (minimizeMaxUsage) {
					xLabel = "Congestion";
					yLabel = "MLU";
					eaOspf.configureNSGAIIAlgorithm(params);
					status.setLabels(xLabel, yLabel);
					status.getMonitorPanel().setLogarithmScale(false);
					eaOspf.configureDefaultArchive(params);
					eaOspf.getEvaluationFunction().addEvaluationFunctionListener(status);
				} else {
					eaOspf.configureEvolutionaryAlgorithm(params);
					status.setLabels(xLabel, yLabel);
					status.getMonitorPanel().setLogarithmScale(true);
					eaOspf.configureDefaultArchive(params);
					eaOspf.getEvaluationFunction().addEvaluationFunctionListener(status);
				}
				status.setNumberOfFunctionEvaluations(params.getNumberGenerations());
				status.restartCounter();
				status.setNumberOfRuns(nruns);
				status.setCurrentRun(i + 1);

				eaOspf.setPlotter(status.getMonitorPanel());
				status.getMonitorPanel().setEvaluationFunction(eaOspf.getEvaluationFunction());

				eaOspf.run();

				double[] pvalues = eaOspf.getBestSolutionReal();
				
				ResultOptim results = new ResultOptim();
				results.addDemands(demands);
				results.addWeights(weights);
				results.setPValues(new PValues(pvalues));

				boolean b = params.getLoadBalancer().equals(LoadBalancer.DEFT) ? true : false;
				PDEFTSimul simul = new PDEFTSimul(topology, b);// params.getLoadBalancer().equals(LoadBalancer.DEFT));
				double fitness = simul.evalPValues(pvalues, weights.asIntArray(), this.demands);
				System.out.println(fitness);
				NetworkLoads loads = simul.getLoads();

				results.addNetworkLoads(loads);
				ResultOptimType resultsT = new ResultOptimType(results);
				resultsT.setInfo("PDEFT\n" + params.toString());
				projB.addResultOptim(resultsT);
		/*	
			} else {
				JecoliPValueReal eaOspf = new JecoliPValueReal(topology, demands, weights);
				String xLabel = "Iteration";
				String yLabel = "Congestion";

				eaOspf.configurePDEFTEvolutionaryAlgorithm(params);
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

				double[] pvalues = eaOspf.getBestSolution();
				System.out.print("[");
				for (int k = 0; k < pvalues.length; k++)
					System.out.print(pvalues[k] + " ; ");
				System.out.println("]");
				ResultOptim results = new ResultOptim();
				results.addDemands(demands);
				results.addWeights(weights);
				results.setPValues(new PValues(pvalues));

				boolean b = params.getLoadBalancer().equals(LoadBalancer.DEFT) ? true : false;
				PDEFTSimul simul = new PDEFTSimul(topology, b);// params.getLoadBalancer().equals(LoadBalancer.DEFT));
				double fitness = simul.evalPValues(pvalues, weights.asIntArray(), this.demands);
				System.out.println(fitness);
				NetworkLoads loads = simul.getLoads();

				results.addNetworkLoads(loads);
				ResultOptimType resultsT = new ResultOptimType(results);
				resultsT.setInfo("Segment Routing\n" + params.toString());
				projB.addResultOptim(resultsT);

			}
*/
		} // end for
	}

	@Progress
	public StatusHandler<Double> getStatus() {
		return status;
	}

	@Cancel
	public synchronized void cancel() {
		this.cancel = true;
		eaOspf.cancel();
	}
}
