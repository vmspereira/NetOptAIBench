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
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
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

@Operation(name = "HybridCongestionOp", description = "Hybrid IP/SDN Congestion Optimization")
public class HybridCongestionOp {

	private ProjectBox projB;
	private Demands[] demands = new Demands[2];
	private Params params = new Params();
	private int nruns;
	private JecoliOSPF eaOspf;
	private boolean cancel=false;

	public StatusHandler<Integer> status = new StatusHandler<Integer>("", "");

	@Port(direction = Direction.INPUT, name = "ProjectBox", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "Demands", order = 2)
	public void setFirstDemands(Demands demands) {
		this.demands[0] = demands;
	}

	@Port(direction = Direction.INPUT, name = "Number of SDN nodes", order = 3,defaultValue="0")
	public void setNumberSDNNodes(int nSDNNodes) {
		this.params.setNumberSDNNodes(nSDNNodes);
	}
	
	@Port(direction = Direction.INPUT, name = "LoadBalancer", order = 4)
	public void setLoadBalancer(LoadBalancer loadBalancer) {
		this.params.setLoadBalancer(loadBalancer);
	}
	
	
	@Port(direction = Direction.INPUT, name = "Inicial Population", order = 5,defaultValue="null", allowNull =true)
	public void setInicialPopulation(Population population) {
		this.params.setInitialPopulation(population);
	}
	
	@Port(direction = Direction.INPUT, name = "Inicial Population Percentage", order = 6, defaultValue="0")
	public void setInicialPopulationPercentage(double percentage) {
		this.params.setInitialPopulationPercentage(percentage);
	}
	

	@Port(direction = Direction.INPUT, name = "Population Size", order = 7, defaultValue = "100")
	public void setPopulationSize(int popul) {
		this.params.setPopulationSize(popul);
	}

	
	@Port(direction = Direction.INPUT, name = "Number of generations", order = 8, defaultValue = "1000")
	public void setNumberGenerations(int numberGenerations)  {
		this.params.setNumberGenerations(numberGenerations);
	}

	@Port(direction = Direction.INPUT, name = "Number os runs", order = 9, defaultValue = "1")
	public void setNumberOfRuns(int nruns) throws Exception {
		this.nruns = nruns;
		this.run();
	}

//	@Port(direction = Direction.OUTPUT,order =1000)
	public void run() throws Exception {
		NetworkTopology topology = projB.getNetworkTopologyBox()
				.getNetworkTopology();// .copy();
		for (int i = 0; i < nruns && !cancel; i++) {

			eaOspf = new JecoliOSPF(topology, demands, null);
			String xLabel = "Congestion";
			String yLabel = "MLU";
		
			params.setSecondObjective(Params.AlgorithmSecondObjective.MLU);
			eaOspf.configureHybridNSGAII(params); 								
			
			status.setLabels(xLabel, yLabel);
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

			
			ResultOptim results = new ResultOptim();
			results.addDemands(demands[0]);			
			Population p;
			p=new Population(eaOspf.getArchive());
			
			String[] labels;
			labels=new String[2];labels[0]=xLabel;labels[1]=yLabel;
			p.setObjectiveName(labels);	
			results.setPopulation(p);
			p.addType(p.new SolutionType(0,topology.getNumberEdges(),Population.Encoding.WEIGHTS)); 
			p.addType(p.new SolutionType(topology.getNumberEdges(),topology.getNumberEdges()+topology.getDimension(),Population.Encoding.NODETYPES));
			
				
			ResultOptimType resultsT = new ResultOptimType(results);
			resultsT.setInfo(params.toString());
			resultsT.setAlgorithm(Params.AlgorithmSelectionOption.NSGAII);
			projB.addResultOptim(resultsT);

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
