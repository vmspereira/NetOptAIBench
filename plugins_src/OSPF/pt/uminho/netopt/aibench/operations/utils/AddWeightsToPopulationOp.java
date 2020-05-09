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
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;


@Operation(name = "AddWeightsToPopulationOp", description = "Add Weight Configuration to a Population")
public class AddWeightsToPopulationOp {

	private Population population;
	private OSPFWeights weights;
	
	@Port(direction = Direction.INPUT, name = "Custom Population", order = 1)
	public void setPopulation(Population p) {
		this.population = p;
	}
	
	@Port(direction = Direction.INPUT, name = "Weight Configuration", order = 2)
	public void setWeights(OSPFWeights w) {
		this.weights = w;
		run();
	}
	
	
	private void run(){
		this.population.add(this.weights);
	}
}
