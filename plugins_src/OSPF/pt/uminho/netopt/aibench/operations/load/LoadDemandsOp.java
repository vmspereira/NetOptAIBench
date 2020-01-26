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
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(description = "Load Demands")
public class LoadDemandsOp {

	private File fileDemand;
	private ProjectBox proj;

	@Port(direction = Direction.INPUT, name = "Project", order = 1)
	public void setProject(ProjectBox proj) {
		this.proj = proj;
	}

	@Port(direction = Direction.INPUT, name = "Demands file", order = 2)
	public void setFileDemand(File in) throws Exception {
		fileDemand = new File(in.getAbsolutePath());
		run();
	}

	public void run() throws Exception {
		Demands dem = new Demands(proj.getNetworkTopologyBox()
				.getNetworkTopology().getDimension(), fileDemand
				.getAbsolutePath());

		proj.addDemands(dem);
	}
}
