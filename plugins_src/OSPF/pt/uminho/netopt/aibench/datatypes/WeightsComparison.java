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


import java.io.Serializable;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetGraph;
import pt.uminho.algoritmi.netopt.ospf.utils.DefaultSPComparator;

@Datatype(structure=Structure.COMPLEX,viewable=false,namingMethod="getName",setNameMethod="setName")
public class WeightsComparison implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4536849453399271740L;
	private double[] serie1;
	private double[] serie2;
	private double corelation;
	private DefaultSPComparator sp;
	private String name;
	
	
	
	

	public WeightsComparison(double[] s1, double[] s2, double c, DefaultSPComparator cp) {
		this.serie1=s1;
		this.serie2=s2;
		this.corelation=c;
		this.sp=cp;
		this.name="WeightsComparison";
	}


	public int getNumberOfChangedWeights(){
		int count=0;
		for(int i=0;i<serie1.length;i++){
			if(((int)serie1[i])!=((int)serie2[i]))
				count++;
		}
		return count;
	}

	public double getCorelation() {
		return corelation;
	}
	public void setCorelation(double correlation) {
		this.corelation = correlation;
	}
	public double[] getSerie1() {
		return serie1;
	}
	public void setSerie1(double[] serie1) {
		this.serie1 = serie1;
	}
	public double[] getSerie2() {
		return serie2;
	}
	public void setSerie2(double[] serie2) {
		this.serie2 = serie2;
	}
	
	public DefaultSPComparator getComparator(){
			return this.sp;
	}

	public NetGraph getBriteGraph(){
		return sp.getBriteGraph();
	}

	public int getDimension() {
		return sp.getDimension();
	}
	
	
	public void setName(String s){
	   this.name = s;
	}


	public String getName(){
	   return this.name;
	}

}
