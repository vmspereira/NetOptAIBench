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
import pt.uminho.algoritmi.netopt.SystemConf;
import pt.uminho.algoritmi.netopt.ospf.optimization.Params;
import pt.uminho.algoritmi.netopt.ospf.optimization.ResultOptim;
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliPValueInteger;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.NondominatedPopulation;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.SRSimul;
import pt.uminho.algoritmi.netopt.ospf.simulation.solution.IntegerSolution;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultOptimType;
import pt.uminho.netopt.aibench.gui.StatusHandler;

@Operation(name = "SRPValuesOp", description = "SR Node-p Values Optimization ")
public class SRPValuesOp {

	private ProjectBox projB;
	private Demands demands;
	private OSPFWeights weights;
	private Params params = new Params();
	private int nruns;
	private boolean minimizeMaxUsage = false;
	private JecoliPValueInteger eaOspf;
	private boolean cancel=false;

	public StatusHandler<Integer> status = new StatusHandler<Integer>("", "");

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
	public void setUseDeft(boolean unit) {
		if (unit)
			this.params.setLoadBalancer(LoadBalancer.DEFT);
		else
			this.params.setLoadBalancer(LoadBalancer.PEFT);
	}

	@Port(direction = Direction.INPUT, name = "Population Size", order = 5, defaultValue = "100")
	public void setPopulationSize(int popul) {
		this.params.setPopulationSize(popul);
	}

	@Port(direction = Direction.INPUT, name = "Achive Size", order = 6, defaultValue = "100")
	public void setArchiveSize(int archiveSize) {
		this.params.setArchiveSize(archiveSize);
	}

	@Port(direction = Direction.INPUT, name = "Number of generations", order = 7, defaultValue = "100")
	public void setNumberGenerations(int numberGenerations) {
		this.params.setNumberGenerations(numberGenerations);
	}

	@Port(direction = Direction.INPUT, name = "Minimize Max. Link Usage", order = 8, defaultValue = "false")
	public void setMinimizeMaxUsage(boolean b) {
		this.minimizeMaxUsage = b;
	}

	@Port(direction = Direction.INPUT, name = "Number of runs", order = 9, defaultValue = "1")
	public void setNumberOfRuns(int nruns) throws Exception {
		this.nruns = nruns;
		run();
	}

	//@Port(direction = Direction.OUTPUT,order =1000)
	public void run() throws Exception {
		NetworkTopology topology = projB.getNetworkTopologyBox().getNetworkTopology().copy();

		if (!this.minimizeMaxUsage) {
			for (int i = 0; i < nruns && !cancel; i++) {
				eaOspf = new JecoliPValueInteger(topology, demands, weights);
				String xLabel = "Iteration";
				String yLabel = "Congestion";

				eaOspf.configureEvolutionaryAlgorithm(params);
			
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

				Population p= new NondominatedPopulation(eaOspf.getSolutionSet());
				IntegerSolution s=p.getLowestValuedSolutions(0, 1).get(0);
				double divider=SystemConf.getPropertyInt("pvalue.divider",100);
				double[] pvalues = new double[s.getNumberOfVariables()];
				for(int k=0;k<pvalues.length;k++)
					pvalues[k]=s.getVariableValue(k)/divider;
				
				
				System.out.print("[");
				for (int k = 0; k < pvalues.length; k++)
					System.out.print(pvalues[k] + " ; ");
				System.out.println("]");
				ResultOptim results = new ResultOptim();
				results.addDemands(demands);
				results.addWeights(weights);
				results.setPValues(new PValues(pvalues));

				SRSimul simul = new SRSimul(topology,params.getLoadBalancer());// params.getLoadBalancer().equals(LoadBalancer.DEFT));
				double fitness = simul.evalPValues(pvalues, weights.asIntArray(), this.demands);
				NetworkLoads loads = simul.getLoads();

				results.addNetworkLoads(loads);
				ResultOptimType resultsT = new ResultOptimType(results);
				resultsT.setInfo("Segment Routing\n" + params.toString());
				projB.addResultOptim(resultsT);
			}
		} else {
			for (int i = 0; i < nruns; i++) {
				eaOspf = new JecoliPValueInteger(topology, demands, weights);
				String xLabel = "Congestion";
				String yLabel = "Max Link Usage";

				eaOspf.configureSRNSGAIIAlgorithm(params);
				status.getMonitorPanel().setNumberOfObjectives(2);
				status.getMonitorPanel().setLogarithmScale(false);
				eaOspf.configureDefaultArchive(params);
				eaOspf.getEvaluationFunction().addEvaluationFunctionListener(status);
				
				status.setLabels(xLabel, yLabel);
				status.setNumberOfFunctionEvaluations(params.getNumberGenerations());
				status.restartCounter();
				status.setNumberOfRuns(nruns);
				status.setCurrentRun(i + 1);

				eaOspf.setPlotter(status.getMonitorPanel());
				status.getMonitorPanel().setEvaluationFunction(eaOspf.getEvaluationFunction());
				
				
				eaOspf.run();
				Population p= new NondominatedPopulation(eaOspf.getSolutionSet());
				IntegerSolution s=p.getLowestValuedSolutions(0, 1).get(0);
				double divider=SystemConf.getPropertyInt("pvalue.divider",100);
				double[] pvalues = new double[s.getNumberOfVariables()];
				for(int k=0;k<pvalues.length;k++)
					pvalues[k]=s.getVariableValue(k)/divider;
					
				
				ResultOptim results = new ResultOptim();
				results.addDemands(demands);
				results.addWeights(weights);
				results.setPValues(new PValues(pvalues));

				
				SRSimul simul = new SRSimul(topology);
				double fitness = simul.evalPValues(pvalues, weights.asIntArray(), this.demands);
				NetworkLoads loads = simul.getLoads();

				results.addNetworkLoads(loads);
				ResultOptimType resultsT = new ResultOptimType(results);
				resultsT.setInfo("Segment Routing\n" + params.toString());
				projB.addResultOptim(resultsT);
			}
			

		}

	}

	@Progress
	public StatusHandler<Integer> getStatus() {
		return status;
	}

	
	@Cancel
	public synchronized void cancel(){
		this.cancel = true;
		eaOspf.cancel();	
	}
}
