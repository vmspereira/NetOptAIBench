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
import pt.uminho.algoritmi.netopt.ospf.optimization.jecoli.JecoliPValueInteger;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.netopt.aibench.gui.StatusHandler;

@Operation(name = "PValuesOp", description = "")
public class PValuesOp {

	private NetworkTopology topology;
	private Demands demands;
	private OSPFWeights weights;
	private Params params = new Params();
	private JecoliPValueInteger eaOspf;
	private boolean cancel = false;
	private PValues p;

	public StatusHandler status = new StatusHandler("", "");


	@Port(direction = Direction.INPUT, name = "Topology", order = 1)
	public void setTopology(NetworkTopology topology) {
		this.topology = topology;
	}
	
	@Port(direction = Direction.INPUT, name = "Demands", order = 2)
	public void setDemands(Demands demands) {
		this.demands = demands;
	}

	@Port(direction = Direction.INPUT, name = "OSPFWeights", order = 3)
	public void setWeights(OSPFWeights weights) {
		this.weights = weights;
		try {
			this.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//@Port(direction = Direction.OUTPUT,order =1000)
	public void run() throws Exception {
				this.params.setPopulationSize(100);
				this.params.setArchiveSize(100);
				this.params.setNumberGenerations(200);
				eaOspf = new JecoliPValueInteger(topology.copy(), demands, weights);
				String xLabel = "Iteration";
				String yLabel = "Congestion";
				xLabel = "Congestion";
				yLabel = "MLU";
				eaOspf.configureNSGAIIAlgorithm(params);
				status.setLabels(xLabel, yLabel);
				status.getMonitorPanel().setLogarithmScale(false);
				eaOspf.configureDefaultArchive(params);
				eaOspf.getEvaluationFunction().addEvaluationFunctionListener(status);
				
				status.setNumberOfFunctionEvaluations(params.getNumberGenerations());
				status.restartCounter();
				status.setNumberOfRuns(1);
				status.setCurrentRun(1);

				eaOspf.setPlotter(status.getMonitorPanel());
				status.getMonitorPanel().setEvaluationFunction(eaOspf.getEvaluationFunction());

				eaOspf.run();

				double[] pvalues = eaOspf.getBestSolutionReal();
				p = new PValues(pvalues);
	}

	
	@Port(direction = Direction.OUTPUT)
	public PValues p(){
		return p;
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
