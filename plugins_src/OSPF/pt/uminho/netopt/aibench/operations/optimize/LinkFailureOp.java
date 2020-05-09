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




import java.util.Iterator;

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import pt.uminho.algoritmi.netopt.ospf.graph.Graph.Status;
import pt.uminho.algoritmi.netopt.ospf.optimization.Params;
import pt.uminho.algoritmi.netopt.ospf.optimization.ResultOptim;
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliOSPF;
import pt.uminho.algoritmi.netopt.ospf.simulation.DelayRequests;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.NondominatedPopulation;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.edgeSelectors.CongestionFitnessEdgeSelector;
import pt.uminho.algoritmi.netopt.ospf.simulation.edgeSelectors.DefaultEdgeSelector;
import pt.uminho.algoritmi.netopt.ospf.simulation.edgeSelectors.HigherLoadDirectEdgeSelector;
import pt.uminho.algoritmi.netopt.ospf.simulation.edgeSelectors.HigherLoadEdgeSelector;
import pt.uminho.algoritmi.netopt.ospf.simulation.edgeSelectors.HigherLoadRacioDirectEdgeSelector;
import pt.uminho.algoritmi.netopt.ospf.simulation.edgeSelectors.IDEdgeSelector;
import pt.uminho.algoritmi.netopt.ospf.simulation.edgeSelectors.INetEdgeSelector;
import pt.uminho.algoritmi.netopt.ospf.simulation.edgeSelectors.HigherCentralityEdgeSelector;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultOptimType;
import pt.uminho.netopt.aibench.gui.StatusHandler;

@Operation(name = "LinkFailureOp", description = "Link Failure OSPF Optimization")
public class LinkFailureOp {

	private ProjectBox projB;
	private Demands demands;
	private DelayRequests delays=null;
	private Params params=new Params();
	private int nruns;
	private Params.AlgorithmSelectionOption algorithm;
	public StatusHandler status = new StatusHandler("", "");
	private JecoliOSPF eaOspf;
	private boolean cancel=false;


	@Port(direction = Direction.INPUT, name = "ProjectBox", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "FirstDemands", order = 2)
	public void setFirstDemands(Demands demands) {
		this.demands = demands;
	}

	@Port(direction = Direction.INPUT, name = "LinkFailureSelectionMethod", order = 3)
	public void setLinkFailureSelectionMethod(Params.EdgeSelectionOption edgeSelectionOption) {
		params.setEdgeSelectionOption(edgeSelectionOption);
	}
	
	@Port(direction = Direction.INPUT, name = "LinkFailureID", order = 4)
	public void setLinkFailureID(Integer edgeFailureId) {
		params.addEdgeFailureId(edgeFailureId);
	}
	
	@Port(direction = Direction.INPUT, name = "LoadBalancer", order = 5)
	public void setLoadBalancer(LoadBalancer loadBalancer) {
		this.params.setLoadBalancer(loadBalancer);
	}
	
	
	@Port(direction = Direction.INPUT, name = "Initial Population", order = 6)
	public void setPopulation(Population population) {
		this.params.setInitialPopulation(population);
	}

	@Port(direction = Direction.INPUT, name = "Preserve", order = 7, defaultValue = "100.0", description = "% from initial population")
	public void setPercentage(double percentage){
		this.params.setInitialPopulationPercentage(percentage);
	}

	@Port(direction = Direction.INPUT, name = "Use InvCap in Population", order = 8, defaultValue = "false")
	public void setUseInvCap(Boolean invCap){
		this.params.setUseInvCap(invCap);
	}

	@Port(direction = Direction.INPUT, name = "Use L2 in Population", order = 9, defaultValue = "false")
	public void setUseL2(Boolean l2)  {
		this.params.setUseL2(l2);
	}

	@Port(direction = Direction.INPUT, name = "Use Unit in Population", order = 10, defaultValue = "false")
	public void setUseUnit(Boolean unit) {
		this.params.setUseUnit(unit);
	}

	@Port(direction = Direction.INPUT, name = "Algorithm", order = 11)
	public void setAlgorithm(Params.AlgorithmSelectionOption algorithm){
		this.algorithm = algorithm;
	}

	@Port(direction = Direction.INPUT, name = "Alfa", order = 12, defaultValue = "0.5", description = "Objective Ponderation factor")
	public void setAlfa(Double alfa) {
		this.params.setAlfa(alfa);
	}

	@Port(direction = Direction.INPUT, name = "Population Size", order = 13, defaultValue = "100")
	public void setPopulationSize(int popul) {
		this.params.setPopulationSize(popul);
	}

	@Port(direction = Direction.INPUT, name = "Achive Size", order = 14, defaultValue = "100")
	public void setArchiveSize(int archiveSize) {
		this.params.setArchiveSize(archiveSize);
	}

	@Port(direction = Direction.INPUT, name = "Number of generations", order = 15, defaultValue = "10")
	public void setNumberGenerations(int numberGenerations)  {
		this.params.setNumberGenerations(numberGenerations);
	}

	@Port(direction = Direction.INPUT, name = "Number os runs", order = 16, defaultValue = "1")
	public void setNumberOfRuns(int nruns) throws Exception {
		this.nruns = nruns;
		this.run();
	}



	//@Port(direction = Direction.OUTPUT,order =1000)
	public void run() throws Exception {
		NetworkTopology topology = projB.getNetworkTopologyBox()
				.getNetworkTopology();// .copy();


		Demands[] demand = new Demands[1];
		demand[0] = demands;
		params.setLinkFailure(true);
		
		for (int i = 0; i < nruns && !cancel; i++) {

			eaOspf = new JecoliOSPF(topology, demand, delays);
			String xLabel = "Congestion (Phi N)";
			String yLabel = "Congestion (Phi N-1)";
			

			if (this.algorithm == Params.AlgorithmSelectionOption.SOEA) {
				eaOspf.configureLinkFailureAlgorithm(params);
				xLabel="Iterations";
				yLabel="Fitness";

			} else if (this.algorithm == Params.AlgorithmSelectionOption.NSGAII) {
				eaOspf.configureNSGAIILinkFailure(params);								

			} else {
				eaOspf.configureSPEA2LinkFailure(params);
			}
			
			
			status.setLabels(xLabel, yLabel);
			if(this.algorithm == Params.AlgorithmSelectionOption.SOEA)
				status.getMonitorPanel().setLogarithmScale(true);
			eaOspf.configureDefaultArchive(params);
			eaOspf.getEvaluationFunction()
					.addEvaluationFunctionListener(status);
		
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
			results.addDemands(demands);
			
			
			Population p;
			if(this.algorithm == Params.AlgorithmSelectionOption.SOEA){
				p=new Population(eaOspf.getSolutionSet());
			}
			else
				p=(Population)(new NondominatedPopulation(eaOspf.getArchive()));
			
			String[] labels;
			if(this.algorithm == Params.AlgorithmSelectionOption.SOEA){
				labels=new String[1];labels[0]=yLabel; 
			}
			else{
				labels=new String[2];labels[0]=xLabel;labels[1]=yLabel;
			}
			p.setObjectiveName(labels);	
			results.setPopulation(p);
			//Simul simulator = projB.getSimul();
			Simul simulator = new Simul(projB.getNetworkTopologyBox().getNetworkTopology());
			simulator.setLoadBalancer(params.getLoadBalancer());
			
			if(this.algorithm == Params.AlgorithmSelectionOption.SOEA)
			{	
				int[] sol = eaOspf.getBestSolutionWeights();
				OSPFWeights weights = new OSPFWeights(topology.getDimension());
				weights.setWeights(sol, topology);
				weights.setCreationMethod("SingleObjective");
				results.addWeights(weights);
				
				simulator.computeLoads(weights, demands);
				results.addNetworkLoads(simulator.getLoads());
				
				
				INetEdgeSelector selector;
				switch(params.getEdgeSelectionOption()){
					case HIGHERLOAD : selector= new HigherLoadEdgeSelector(simulator);break;
					case MOSTUSEDPATH: selector= new HigherCentralityEdgeSelector(simulator);break; 
					case HIGHERDIRECTLOAD:selector = new HigherLoadDirectEdgeSelector(simulator);break;
					case HIGHERFAILUREFITNESSIMPACT: selector = new CongestionFitnessEdgeSelector(simulator);
					case MOSTDIRECTUSEDRACIO: selector= new HigherLoadRacioDirectEdgeSelector(simulator);break;
					case USERSELECTED: selector=new IDEdgeSelector(simulator,params.getEdgeFailureId());break;
					default: selector= new DefaultEdgeSelector(simulator);break;
				}
				
				
				Iterator<NetEdge> it = selector.getIterator();
				NetEdge e = it.next();
				int lf_from = e.getFrom();
				int lf_to = e.getTo();
				
				Iterator<NetEdge> it2 = selector.getIterator();
				while(it2.hasNext())
					it2.next().print();

				// altera o estado do link para down
				topology.getGraph()
						.setConnection(lf_from, lf_to, Status.DOWN);
				topology.getGraph()
						.setConnection(lf_to, lf_from, Status.DOWN);
				// ------------------------------------------------------
				simulator.computeLoads(weights, demands);
				NetworkLoads loads = simulator.getLoads();
				loads.setName("With Link Failure ("+params.getEdgeSelectionOption().name()+" "+lf_from+"-"+lf_to+")");
				results.addNetworkLoads(loads);

				
				// --------------------------------------------------------------
				// altera o estado do link para UP
				topology.getGraph()
						.setConnection(lf_from, lf_to, Status.UP);
				topology.getGraph()
						.setConnection(lf_to, lf_from, Status.UP);
				
			}
				
			ResultOptimType resultsT = new ResultOptimType(results);
			resultsT.setInfo(params.toString());
			resultsT.setAlgorithm(this.algorithm);
			projB.addResultOptim(resultsT);
			
			
			

		}

	}

	
	@Cancel
	public synchronized void cancel(){
		this.cancel = true;
		eaOspf.cancel();	
	}
	
	@Progress
	public StatusHandler getStatus() {
		return status;
	}
}
