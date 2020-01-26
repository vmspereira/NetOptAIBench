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
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.exception.DimensionErrorException;
import pt.uminho.algoritmi.netopt.ospf.utils.MathUtils;
import pt.uminho.algoritmi.netopt.ospf.utils.DefaultSPComparator;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.WeightsComparison;;

@Operation(name = "WeightCorrelationOp", description = "Calculate Correlation beetween Weights")
public class WeightsComparisonOp {
	
	private ProjectBox pb;
	private OSPFWeights weights1;
	private OSPFWeights weights2;
	
	
	@Port(direction = Direction.INPUT, name = "Topology", order = 1)
	public void setNetworkTopology(ProjectBox pb) {
		this.pb = pb;
	}
	
	@Port(direction = Direction.INPUT, name = "Weights 1", order = 2)
	public void setWeights1(OSPFWeights weights) {
		this.weights1 = weights;
	}
	
	@Port(direction = Direction.INPUT, name = "Weights 2", order = 3)
	public void setWeights2(OSPFWeights weights) throws DimensionErrorException {
		this.weights2 = weights;
		run();
	}

	//@Port(direction=Direction.OUTPUT,order=3)
	public void run() throws DimensionErrorException{
		// weights correlacao
		double[] s1=toArray(this.weights1);
		double[] s2=toArray(this.weights2);
		double c=MathUtils.getPearsonCorrelation(s1,s2);
		// path 
		DefaultSPComparator cp= new DefaultSPComparator(pb.getNetworkTopologyBox().getNetworkTopology().getNetGraph(),weights1,weights2);
		cp.compare();
		
		WeightsComparison wc=new WeightsComparison(s1,s2,c,cp);
		pb.addWeightsComparation(wc);
	}
	
	
	
	private double[] toArray(OSPFWeights w)
	{
		int dimension = w.getDimension();
		double[] weights= new double[dimension*dimension];
		for(int i=0;i<dimension;i++)
			for(int j=0;j<dimension;j++)
				weights[i*dimension+j]= w.getWeight(i,j);
		return weights;
	}
	
	
	
	
	
}
