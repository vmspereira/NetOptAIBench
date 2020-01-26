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


import pt.uminho.algoritmi.netopt.ospf.optimization.Params;
import pt.uminho.algoritmi.netopt.ospf.optimization.ResultOptim;
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliOSPF;
import pt.uminho.algoritmi.netopt.ospf.simulation.DelayRequests;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.ISimulator;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.PDEFTSimul;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultOptimType;
import pt.uminho.netopt.aibench.gui.StatusHandler;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(name = "EvoAlgoSingleObjectiveOp", description = "Evolutionary Algorithm Single Objective")
public class CongestionOp {

	private ProjectBox projB;
	private Demands[] demands = new Demands[2];
	private Params.AlgorithmSecondObjective secondObjective;
	private Params.AlgorithmSelectionOption algorithm;
	private DelayRequests delays;
	private Params params = new Params();
	private int nruns;
	private JecoliOSPF eaOspf;
	private boolean cancel=false;

	public StatusHandler status = new StatusHandler("", "");

	@Port(direction = Direction.INPUT, name = "ProjectBox", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "FirstDemands", order = 2)
	public void setFirstDemands(Demands demands) {
		this.demands[0] = demands;
	}

	@Port(direction = Direction.INPUT, name = "SecondDemands", order = 3)
	public void setSecond(Demands demands) {
		this.demands[1] = demands;
	}

	@Port(direction = Direction.INPUT, name = "DelayRequests", order = 4)
	public void setDemands(DelayRequests delays){ 
		this.delays = delays;
	}

	@Port(direction = Direction.INPUT, name = "SecondObjective", order = 5)
	public void setSecondObjective(Params.AlgorithmSecondObjective objective){
		this.secondObjective = objective;
	}


	@Port(direction = Direction.INPUT, name = "LoadBalancer", order = 6)
	public void setLoadBalancer(LoadBalancer loadBalancer) {
		this.params.setLoadBalancer(loadBalancer);
	}
	
	@Port(direction = Direction.INPUT, name = "LoadBalancerFunction", order = 7)
	public void setLoadBalancerFunction(String loadBalancerf) {
		this.params.setLoadBalancerFunction(loadBalancerf);
	}
	
	@Port(direction = Direction.INPUT, name = "Initial Population", order = 8)
	public void setPopulation(Population population) {
		this.params.setInitialPopulation(population);
	}

	@Port(direction = Direction.INPUT, name = "Preserve", order = 9, defaultValue = "100.0", description = "% from initial population")
	public void setPercentage(double percentage){
		this.params.setInitialPopulationPercentage(percentage);
	}

	@Port(direction = Direction.INPUT, name = "Use InvCap in Population", order = 10, defaultValue = "false")
	public void setUseInvCap(Boolean invCap){
		this.params.setUseInvCap(invCap);
	}

	@Port(direction = Direction.INPUT, name = "Use L2 in Population", order = 11, defaultValue = "false")
	public void setUseL2(Boolean l2)  {
		this.params.setUseL2(l2);
	}

	@Port(direction = Direction.INPUT, name = "Use Unit in Population", order = 12, defaultValue = "false")
	public void setUseUnit(Boolean unit) {
		this.params.setUseUnit(unit);
	}

	@Port(direction = Direction.INPUT, name = "Algorithm", order = 13)
	public void setAlgorithm(Params.AlgorithmSelectionOption algorithm){
		this.algorithm = algorithm;
	}

	@Port(direction = Direction.INPUT, name = "Alfa", order = 14, defaultValue = "0.5", description = "Objective Ponderation factor")
	public void setAlfa(Double alfa) {
		try{
			this.params.setAlfa(alfa);
		}catch(NullPointerException ex){
			if(this.algorithm == Params.AlgorithmSelectionOption.SOEA)
				throw ex;
		}
	}

	@Port(direction = Direction.INPUT, name = "Population Size", order = 15, defaultValue = "100")
	public void setPopulationSize(int popul) {
		this.params.setPopulationSize(popul);
	}

	@Port(direction = Direction.INPUT, name = "Achive Size", order = 16, defaultValue = "100")
	public void setArchiveSize(int archiveSize) {
		this.params.setArchiveSize(archiveSize);
	}

	@Port(direction = Direction.INPUT, name = "Number of generations", order = 17, defaultValue = "10")
	public void setNumberGenerations(int numberGenerations)  {
		this.params.setNumberGenerations(numberGenerations);
	}

	@Port(direction = Direction.INPUT, name = "Number os runs", order = 18, defaultValue = "1")
	public void setNumberOfRuns(int nruns) throws Exception {
		this.nruns = nruns;
		this.run();
	}

	
	//@Port(direction = Direction.OUTPUT,order =1000)
	public void run() throws Exception {
		NetworkTopology topology = projB.getNetworkTopologyBox()
				.getNetworkTopology();// .copy();
		for (int i = 0; i < nruns && !cancel; i++) {

			eaOspf = new JecoliOSPF(topology, demands, delays);
			String xLabel = "Congestion";
			String yLabel = "Delays";
		
			params.setSecondObjective(secondObjective);
			
			if (this.secondObjective == Params.AlgorithmSecondObjective.DEMANDS) {
				xLabel=xLabel+" 1";
				yLabel="Congestion 2";
			}else if (this.secondObjective == Params.AlgorithmSecondObjective.MLU)
			{
				yLabel="MLU";
			}
			else if (this.secondObjective == Params.AlgorithmSecondObjective.ALU)
			{
				yLabel="ALU";
			}

			if (this.algorithm == Params.AlgorithmSelectionOption.SOEA) {
				eaOspf.configureEvolutionaryAlgorithm(params);
				xLabel="Iterations";
				yLabel="Fitness";

			} else if (this.algorithm == Params.AlgorithmSelectionOption.NSGAII) {
				eaOspf.configureNSGAII(params);								

			} else {
				eaOspf.configureSPEA2(params);
			}
			
			status.setLabels(xLabel, yLabel);
			if(this.algorithm == Params.AlgorithmSelectionOption.SOEA)
				status.getMonitorPanel().setLogarithmScale(true);
			eaOspf.configureDefaultArchive(params);
			eaOspf.getEvaluationFunction().addEvaluationFunctionListener(status);
		
			status.setNumberOfFunctionEvaluations(params.getNumberGenerations());
			status.restartCounter();
			status.setNumberOfRuns(nruns);
			status.setCurrentRun(i+1);
			eaOspf.setPlotter(status.getMonitorPanel());
			status.getMonitorPanel().setEvaluationFunction(
					eaOspf.getEvaluationFunction());
		
			eaOspf.run();

			
			//ISolutionContainer<ILinearRepresentation<Integer>> solutions = eaOspf
			//		.getSolutions();
			
			ResultOptim results = new ResultOptim();
			results.addDemands(demands[0]);
			if(this.secondObjective == Params.AlgorithmSecondObjective.DEMANDS){
				results.addDemands(demands[1]);
			}else
				results.setDelayReqs(delays);
			//results.setSolutions(solutions);
			
			Population p;
			if(this.algorithm == Params.AlgorithmSelectionOption.SOEA)
				p=new Population(eaOspf.getSolutionSet());
			else
				p=new Population(eaOspf.getArchive());
			
			String[] labels;
			if(this.algorithm == Params.AlgorithmSelectionOption.SOEA){
				labels=new String[1];labels[0]=yLabel; 
			}
			else{
				labels=new String[2];labels[0]=xLabel;labels[1]=yLabel;
			}
			p.setObjectiveName(labels);	
			results.setPopulation(p);
			
			
			ISimulator simulator;
			if(params.getLoadBalancer()==LoadBalancer.DEFT)
				simulator= new PDEFTSimul(topology,true);
			else 
				if(params.getLoadBalancer()==LoadBalancer.PEFT)
					simulator= new PDEFTSimul(topology,false);
			else
				simulator= new Simul(topology);
			
			
			
					
			if(this.algorithm == Params.AlgorithmSelectionOption.SOEA)
			{	
				int[] sol = eaOspf.getBestSolutionWeights();
				OSPFWeights weights = new OSPFWeights(topology.getDimension());
				weights.setWeights(sol, topology);
				weights.setCreationMethod("SingleObjective");
				results.addWeights(weights);
				simulator.computeLoads(weights, demands[0]);
				results.addNetworkLoads(simulator.getLoads());
				if(this.secondObjective == Params.AlgorithmSecondObjective.DEMANDS){
					simulator.computeLoads(weights, demands[1]);
					results.addNetworkLoads(simulator.getLoads());
				}
				else if(this.secondObjective == Params.AlgorithmSecondObjective.DELAY)
				{
					simulator.computeDelays(weights, delays);
					results.setEndToEndDelays(simulator
						.getAverageEndToEndDelays());
				}
			}
				
			ResultOptimType resultsT = new ResultOptimType(results);
			resultsT.setInfo(params.toString());
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
