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
package pt.uminho.netopt.aibench.datatypes;

import jecoli.algorithm.multiobjective.MOUtils;
import jecoli.algorithm.multiobjective.PerformanceIndex;
import jecoli.algorithm.multiobjective.QualityEvaluation;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;


public class PopulationsComparison {

	protected Population population1;
	protected Population population2;
	
	private double[][] values=new double[7][2];
	
	public static final int Epsilon = 0;
	public static final int GeneralizedSpread = 1;
	public static final int GenerationalDistance = 2;
	public static final int HyperVolume = 3;
	public static final int InvertedGenerationalDistance = 4;
	public static final int Spread = 5;
	public static final int CMeasure = 6;

	public PopulationsComparison(Population population1, Population population2)
			throws Exception {
		this.population1 = population1;
		this.population2 = population2;
		evaluate();
	}

	public void evaluate() throws Exception {
		if (population1.getSolution(0).getNumberOfObjectives() != population2
				.getSolution(0).getNumberOfObjectives())
			throw new Exception("Populations with distincts objectives numbers");
		int n = population1.getSolution(0).getNumberOfObjectives();
		QualityEvaluation evaluation = new QualityEvaluation();
		
		
		values[PopulationsComparison.Epsilon][0] = evaluation
				.evaluate(PerformanceIndex.EPSILON,
						population1.getParetoMatrix(),
						population2.getParetoMatrix(), n);
		values[PopulationsComparison.Epsilon][1] = evaluation
				.evaluate(PerformanceIndex.EPSILON,
						population2.getParetoMatrix(),
						population1.getParetoMatrix(), n);
		
		
		values[PopulationsComparison.GeneralizedSpread][0] = evaluation
				.evaluate(PerformanceIndex.GENERALIZED_SPREAD,
						population1.getParetoMatrix(),
						population2.getParetoMatrix(), n);
		values[PopulationsComparison.GeneralizedSpread][1] = evaluation
				.evaluate(PerformanceIndex.GENERALIZED_SPREAD,
						population2.getParetoMatrix(),
						population1.getParetoMatrix(), n);
		
		
		values[PopulationsComparison.GenerationalDistance][0] = evaluation
				.evaluate(PerformanceIndex.GENERATIONAL_DISTANCE,
						population1.getParetoMatrix(),
						population2.getParetoMatrix(), n);
		values[PopulationsComparison.GenerationalDistance][1] = evaluation
				.evaluate(PerformanceIndex.GENERATIONAL_DISTANCE,
						population2.getParetoMatrix(),
						population1.getParetoMatrix(), n);
		
		
		values[PopulationsComparison.HyperVolume][0] = evaluation
				.evaluate(PerformanceIndex.HYPERVOLUME,
						population1.getParetoMatrix(),
						population2.getParetoMatrix(), n);
		values[PopulationsComparison.HyperVolume][1] = evaluation
				.evaluate(PerformanceIndex.HYPERVOLUME,
						population2.getParetoMatrix(),
						population1.getParetoMatrix(), n);
		
		values[PopulationsComparison.InvertedGenerationalDistance][0] = evaluation
				.evaluate(PerformanceIndex.INVERTED_GENERATIONAL_DISTANCE,
						population1.getParetoMatrix(),
						population2.getParetoMatrix(), n);
		values[PopulationsComparison.InvertedGenerationalDistance][1] = evaluation
				.evaluate(PerformanceIndex.INVERTED_GENERATIONAL_DISTANCE,
						population2.getParetoMatrix(),
						population1.getParetoMatrix(), n);
		
		values[PopulationsComparison.Spread][0] = evaluation
				.evaluate(PerformanceIndex.SPREAD,
						population1.getParetoMatrix(),
						population2.getParetoMatrix(), n);
		values[PopulationsComparison.Spread][1] = evaluation
				.evaluate(PerformanceIndex.SPREAD,
						population2.getParetoMatrix(),
						population1.getParetoMatrix(), n);
		
		values[PopulationsComparison.CMeasure][0] = MOUtils.cMeasure(
				population1.getParetoMatrix(),
				population2.getParetoMatrix());
		values[PopulationsComparison.CMeasure][1] = MOUtils.cMeasure(
						population2.getParetoMatrix(),
						population1.getParetoMatrix());

	}

	public double getEpsilon(int n) {
		return this.values[PopulationsComparison.Epsilon][n];
	}

	public double getGeneralizedSpread(int n) {
		return this.values[PopulationsComparison.GeneralizedSpread][n];
	}

	public double getGenerationalDistance(int n) {
		return this.values[PopulationsComparison.GenerationalDistance][n];
	}

	public double getHyperVolume(int n) {
		return this.values[PopulationsComparison.HyperVolume][n];
	}

	public double getInvertedGenerationalDistance(int n) {
		return this.values[PopulationsComparison.InvertedGenerationalDistance][n];
	}

	public double getSpread(int n) {
		return this.values[PopulationsComparison.Spread][n];
	}
	
	public double getCMeasure(int n) {
		return this.values[PopulationsComparison.CMeasure][n];
	}
	
	
	public Population getFirstPopulation(){
		return this.population1;
	}
	
	public Population getSecondPopulation(){
		return this.population2;
	}

}
