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
import es.uvigo.ei.aibench.core.datatypes.annotation.Clipboard;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.algoritmi.netopt.ospf.simulation.AverageEndtoEndDelays;
import pt.uminho.algoritmi.netopt.ospf.simulation.DelayRequests;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.ResultSimul;

@SuppressWarnings("serial")
@Datatype(structure = Structure.COMPLEX, namingMethod = "getName", setNameMethod = "setName", removable=true)
public class ResultSimulType implements Serializable{
	private ResultSimul results;	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResultSimulType(ResultSimul results) {
		this.results = results;
	}

	public ResultSimul getResults() {
		return results;
	}

	public void setResults(ResultSimul results) {
		this.results = results;
	}

	@Clipboard(name = "Network Loads",order=1)
	public NetworkLoads getNetworkLoads() {
	try{
		return results.getNetworkLoads().get(0);
	}catch(Exception e){return null;}
	}
	
	public Demands getDemands() {
		try{
			return results.getDemands().get(0);
		}catch(Exception e){return null;}
	}
	
	public DelayRequests getDelayReqs() {
		return results.getDelayReqs();
	}
	
	public OSPFWeights getWeights() {
	try{
		return results.getWeights().get(0);
	}catch(Exception e){return null;}
	}

	@Clipboard(name = "Average End to End Delays",order=2)
	public AverageEndtoEndDelays getEndToEndDelays() {
		return results.getEndToEndDelays();
	}

}
