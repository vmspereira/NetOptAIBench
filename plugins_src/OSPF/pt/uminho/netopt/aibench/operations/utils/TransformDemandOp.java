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
package pt.uminho.netopt.aibench.operations.utils;

import java.util.Random;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(name = "TransformDemandOp", description = "Change Demands by a factor")
public class TransformDemandOp {

	ProjectBox pb;
	Demands demands;
	Double factor;
	Double tolerance;
	
	@Port(direction = Direction.INPUT, name = "Topology", order = 1)
	public void setNetworkTopology(ProjectBox pb) {
		this.pb = pb;
	}
	
	@Port(direction = Direction.INPUT, name = "Demands", order = 2)
	public void setDemands(Demands demands) {
		this.demands = demands;
	}
	
	@Port(direction = Direction.INPUT, name = "Factor", order = 3,description = "Example: 0.1 for 10% increase")
	public void setWeights2(Double factor){
		this.factor = factor;
	}
	
	
	@Port(direction = Direction.INPUT, name = "Tolerance", order = 4,description = "Example: 0.05 for 5% tolerance")
	public void setTolerance(Double tolerance){
		this.tolerance = tolerance;
		run();
	}
	
	private void run(){
		
		Demands d=new Demands(demands.getDimension());
		d.setFilename(demands.getFilename()+" Increase "+factor+" Tolerande "+tolerance);
		Random randomGenerator = new Random();
		for(int i=0;i<demands.getDimension();i++)
			for(int j=0;j<demands.getDimension();j++){
				double r = randomGenerator.nextDouble();
				d.setDemands(i,j,(1+factor+2*tolerance*(r-0.5))*demands.getDemands(i, j));
			}
		pb.addDemands(d);
	}

}
