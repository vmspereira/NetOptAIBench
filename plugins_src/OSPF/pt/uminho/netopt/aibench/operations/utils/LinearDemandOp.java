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
* @author V�tor Pereira
*/
package pt.uminho.netopt.aibench.operations.utils;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(name = "LinearDemandOp", description = "Random Linear demands")
public class LinearDemandOp {

	ProjectBox pb;
	Demands demands1;
	Demands demands2;
	
	@Port(direction = Direction.INPUT, name = "Topology", order = 1)
	public void setNetworkTopology(ProjectBox pb) {
		this.pb = pb;
	}
	
	@Port(direction = Direction.INPUT, name = "First Demands", order = 2)
	public void setFirstDemands(Demands demands) {
		this.demands1 = demands;
	}
	
	@Port(direction = Direction.INPUT, name = "Second Demands", order = 3)
	public void setSecondDemands(Demands demands) {
		this.demands2 = demands;
		run();
	}
	
	
	private void run(){
		
		Demands d=new Demands(demands1.getDimension());
		d.setFilename(demands1.getFilename()+" + "+demands2.getFilename());
		for(int i=0;i<demands1.getDimension();i++)
			for(int j=0;j<demands1.getDimension();j++){
				double r=Math.random();
				double value = r*demands1.getDemands(i, j)+ (1-r) * demands2.getDemands(i, j);
				d.setDemands(i,j,value);
			}
		pb.addDemands(d);
	}

}
