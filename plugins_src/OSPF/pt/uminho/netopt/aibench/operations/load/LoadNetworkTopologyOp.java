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
package pt.uminho.netopt.aibench.operations.load;

import java.io.File;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.netopt.aibench.datatypes.NetworkTopologyBox;

@Operation(description = "Load NetworkTopology")
public class LoadNetworkTopologyOp {
	private File fileNodes;
	private File fileEdges;
	private NetworkTopologyBox out;

	@Port(direction = Direction.INPUT, name = "fileNodes")
	public void setFileNodes(File in) {
		this.fileNodes = new File(in.getAbsolutePath());
	}

	@Port(direction = Direction.INPUT, name = "fileEdges")
	public void setFileEdges(File in) {
		this.fileEdges = new File(in.getAbsolutePath());
	}

	@Port(direction = Direction.OUTPUT)
	public NetworkTopologyBox run() {
		out = new NetworkTopologyBox(fileNodes.getAbsolutePath(), fileEdges
				.getAbsolutePath());

		return out;
	}

}
