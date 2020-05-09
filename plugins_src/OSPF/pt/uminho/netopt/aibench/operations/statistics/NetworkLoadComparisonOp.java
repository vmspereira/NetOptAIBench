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
package pt.uminho.netopt.aibench.operations.statistics;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;

@Operation(name = "NetworkLoadComparisonOp", description = "Network Load Comparison")
public class NetworkLoadComparisonOp {

	NetworkLoads l1;
	NetworkLoads l2;

	@Port(direction = Direction.INPUT, name = "First Loads", order = 2)
	public void setFirstDemands(NetworkLoads loads) {
		this.l1 = loads;
	}

	@Port(direction = Direction.INPUT, name = "Second Loads", order = 3)
	public void setSecondDemands(NetworkLoads loads) {
		this.l2 = loads;
		run();
	}

	private void run() {

		if (l1.getDimension() != l2.getDimension())
			return;

	}

}
