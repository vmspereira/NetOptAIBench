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
package pt.uminho.netopt.aibench.operations.simulate;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.DelayRequests;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.ResultSimul;
import pt.uminho.algoritmi.netopt.ospf.simulation.exception.DimensionErrorException;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultSimulType;

@Operation(name = "ComputeDelaysOp", description = "Compute Average Delays")
public class ComputeDelaysOp {
	private ProjectBox projB;
	private OSPFWeights weights;
	private DelayRequests delayReqs;

	@Port(direction = Direction.INPUT, name = "Project", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "OSPFWeights", order = 2)
	public void setWeights(OSPFWeights weights) {
		this.weights = weights;
	}

	@Port(direction = Direction.INPUT, name = "Delay Requests", order = 3)
	public void setDelayRequests(DelayRequests delayReqs)
			throws DimensionErrorException {
		this.delayReqs = delayReqs;
	}

	@Port(direction = Direction.OUTPUT,order =1000)
	public void computeDelays() throws DimensionErrorException {
		ResultSimul results = new ResultSimul();
		results.setDelayReqs(delayReqs);
		results.addWeights(weights);
		projB.getSimul().computeDelays(weights, delayReqs);
		results.setEndToEndDelays(projB.getSimul().getAverageEndToEndDelays());
		ResultSimulType resultsT = new ResultSimulType(results);
		projB.addResultSimul(resultsT);

	}
}
