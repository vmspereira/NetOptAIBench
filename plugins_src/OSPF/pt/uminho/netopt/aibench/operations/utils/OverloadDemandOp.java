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

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(name = "TransformDemandOp", description = "Overload single link")
public class OverloadDemandOp {

	ProjectBox pb;
	Demands demands;
	Integer from;
	Integer to;
	
	@Port(direction = Direction.INPUT, name = "Topology", order = 1)
	public void setNetworkTopology(ProjectBox pb) {
		this.pb = pb;
	}
	
	@Port(direction = Direction.INPUT, name = "Demands", order = 2)
	public void setDemands(Demands demands) {
		this.demands = demands;
	}
	
	@Port(direction = Direction.INPUT, name = "Source Node", order = 3)
	public void setWeights2(Integer from){
		this.from = from;
	}
	
	
	@Port(direction = Direction.INPUT, name = "Destination Node", order = 4)
	public void setTolerance(Integer to){
		this.to = to;
		run();
	}
	
	private void run(){
		
		Demands d=new Demands(demands.getDimension());
		d.setFilename(demands.getFilename()+" Overload link "+from+"-"+to);
		Double bw=pb.getNetworkTopologyBox().getNetworkTopology().getNetGraph().getEdge(from, to).getBandwidth();
		for(int i=0;i<demands.getDimension();i++)
			for(int j=0;j<demands.getDimension();j++){
				if((i==from && j==to) || (i==to && j==from))
				{
					d.setDemands(i,j,bw+demands.getDemands(i, j));
				}
				else
				{
					d.setDemands(i,j,demands.getDemands(i, j));
				}
			}
		pb.addDemands(d);
	}

}
