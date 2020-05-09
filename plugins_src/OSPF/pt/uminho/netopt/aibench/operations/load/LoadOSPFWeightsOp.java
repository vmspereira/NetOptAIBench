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
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(description = "Load OSPF Weights")
public class LoadOSPFWeightsOp {

	private File fileWeights;
	private ProjectBox projB;

	@Port(direction = Direction.INPUT, name = "Project Box", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "Filename", order = 2)
	public void setFileWeights(File in) throws Exception {
		fileWeights = new File(in.getAbsolutePath());
		try{
		 run();
		}catch(Exception e){
			run2();
		}
	}

	public void run() throws Exception {
		OSPFWeights weights = new OSPFWeights(fileWeights.getAbsolutePath(),
				this.projB.getNetworkTopologyBox().getNetworkTopology()
						.getDimension());
		weights.setCreationMethod("Loaded from "
				+ fileWeights.getAbsolutePath());

		projB.addOSPFWeights(weights);
	}
	
	
	public void run2() throws Exception {
		OSPFWeights weights = new OSPFWeights(fileWeights.getAbsolutePath(),
				this.projB.getNetworkTopologyBox().getNetworkTopology());
		weights.setCreationMethod("Loaded from "
				+ fileWeights.getAbsolutePath());
		projB.addOSPFWeights(weights);
	}
}
