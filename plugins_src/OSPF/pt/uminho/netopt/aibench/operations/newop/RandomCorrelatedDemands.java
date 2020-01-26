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
package pt.uminho.netopt.aibench.operations.newop;

import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;

public class RandomCorrelatedDemands {

	public Demands generate(Demands seedDemand, double r, double f,
			NetworkTopology topology) throws Exception {
		int dimension = seedDemand.getDimension();

		double[] x = linearize(seedDemand.getDemands());
		double averagex = avg(x);
		double[] _x = add(x, -1 * averagex);
		double norma_x = norm(_x);

		// we are looking for an double[] array _y such
		// that _x * _y = p where * is the usual scalar product

		// randomly generate a demand
		Demands dy = new Demands(dimension);
		dy.setRandomDemands(0.1, topology);

		double[] y1 = linearize(dy.getDemands());
		double[] y=mult(y1,f*norma_x/norm(y1));
		
		double averagey = avg(y);
		double[] _y = add(x, -1 * averagey);
		double norma_y = norm(_y);
		
		
		

		double[] diag = linearize(diag(dimension));

		// ...

		
		
		//

		// double y[] = add(_y,average*f);
		delinearize(y, dimension);
		Demands d = new Demands(dimension);
		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++)
				d.setDemands(i, j, y[i * dimension + j]);

		return d;
	}

	public double[] linearize(double[][] matrix) {

		int l = 0, c = 0, n = 0;
		try {
			l = matrix.length;
			c = matrix[0].length;
			n = l * c;
		} catch (Exception e) {
		}
		;

		double[] v = new double[n];
		if (n > 0)
			for (int i = 0; i < l; i++)
				for (int j = 0; j < c; j++)
					v[i * c + j] = matrix[i][j];
		return v;
	}

	public double[][] delinearize(double[] y, int lines) throws Exception {

		int l = lines, n = y.length;
		if (n % l != 0)
			throw new Exception("wrong matrix dimensions");
		int c = n / l;

		double[][] v = new double[l][c];
		for (int i = 0; i < l; i++)
			for (int j = 0; j < c; j++)
				v[i][j] = y[i * c + j];
		return v;
	}

	public double norm(double[] v) {
		double n = 0.0;
		for (int i = 0; i < v.length; i++)
			n += v[i] * v[i];
		return Math.sqrt(n);
	}

	public double avg(double[] v) {
		double n = 0.0;
		for (int i = 0; i < v.length; i++)
			n += v[i];
		return n / v.length;
	}

	public double[] add(double[] v, double value) {

		double[] r = new double[v.length];
		for (int i = 0; i < v.length; i++)
			r[i] = v[i] + value;
		return r;
	}

	
	public double[] add(double[] v, double[] u) {

		double[] r = new double[v.length];
		for (int i = 0; i < v.length; i++)
			r[i] = v[i] + u[i];
		return r;
	}

	public double[][] diag(int dim) {

		double[][] d = new double[dim][dim];
		for (int i = 0; i < dim; i++)
			for (int j = 0; j < dim; j++)
				if (i == j)
					d[i][j] = 1.0;
				else
					d[i][j] = 0.0;

		return d;
	}

	public double[] mult(double[] v, double value) {

		double[] r = new double[v.length];
		for (int i = 0; i < v.length; i++)
			r[i] = v[i] * value;
		return r;
	}

	public double scalar(double[] x, double[] y) throws Exception {
		if (x.length != y.length)
			throw new Exception("vectors have different size");
		double r = 0;
		for (int i = 0; i < x.length; i++)
			r += x[i] * y[i];
		return r;
	}

}
