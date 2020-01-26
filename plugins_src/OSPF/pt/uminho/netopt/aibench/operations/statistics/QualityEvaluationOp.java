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
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.netopt.aibench.datatypes.PopulationsComparison;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
@Operation(name = "QualitiEvaluationOp", description = "Pareto Evaluation Measures")
public class QualityEvaluationOp {
	
	
	private ProjectBox pb;
	private Population population1;
	private Population population2;
	
	
	@Port(direction = Direction.INPUT, name = "Topology", order = 1)
	public void setNetworkTopology(ProjectBox pb) {
		this.pb = pb;
	}
	
	@Port(direction = Direction.INPUT, name = "Population 1", order = 2)
	public void setPopulation1(Population population) {
		this.population1 = population;
	}
	
	@Port(direction = Direction.INPUT, name = "Population 2", order = 3)
	public void sePopulation2(Population population){
		this.population2 = population;
		run();
	}

	//@Port(direction=Direction.OUTPUT,order=3)
	public void run(){
		
		try {
			PopulationsComparison pc=new PopulationsComparison(population1,population2);
			pb.addPopulationComparation(pc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
