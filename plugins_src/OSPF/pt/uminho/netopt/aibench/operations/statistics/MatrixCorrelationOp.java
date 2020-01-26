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

import javax.swing.JOptionPane;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.utils.MathUtils;

@Operation(name = "MatrixCorrelationOp", description = "Matrices Pearson Correlation")
public class MatrixCorrelationOp {

	Demands demands1;
	Demands demands2;
	
	
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
		
		if(demands1.getDimension()!=demands2.getDimension())
			return;
		
		int n=demands1.getDimension();

		double[] s1=new double[n*n];
		double[] s2=new double[n*n];
		
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++){
				s1[(n*i+j)]=demands1.getDemands(i, j);
				s2[(n*i+j)]=demands2.getDemands(i, j);
			}
		
		double c=MathUtils.getPearsonCorrelation(s1,s2);
		JOptionPane.showMessageDialog(null, "Correlation = "+c);
	}
}
