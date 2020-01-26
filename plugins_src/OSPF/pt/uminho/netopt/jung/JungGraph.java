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
package pt.uminho.netopt.jung;

import java.awt.geom.Point2D;

import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetGraph;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode;

public class JungGraph {
	private Graph<NetNode, NetEdge> g;
	private NetGraph bGraph;
	private Transformer<NetNode,Point2D> locationTransformer=null; 
	

	public JungGraph(NetworkTopology topology) {
		g = new OSPFSparseGraph();
		this.bGraph = topology.getNetGraph();
	}


	public JungGraph(NetGraph briteGraph) {
		g = new OSPFSparseGraph();
		this.bGraph = briteGraph;
	}


	public boolean addVertex(NetNode node) {
		return g.addVertex(node);
	}

	public boolean addEdge(NetEdge edge)
	{
		return g.addEdge(edge, bGraph.getNodeByID(edge.getFrom()), bGraph
				.getNodeByID(edge.getTo()));
	}

	public boolean populateGraph() {
		boolean res = true;
		for (NetNode node : this.bGraph.getNodes()) {
			res = res && addVertex(node);
		}
		for (NetEdge edge : this.bGraph.getEdges()) {
			res = res && addEdge(edge);
		}
		return res;
	}

	public Graph<NetNode, NetEdge> getGraph() {
		return this.g;
	}

	public Transformer<NetNode, Point2D> getLocationTransformer() {
		
		if(this.locationTransformer==null){
			locationTransformer = new Transformer<NetNode, Point2D>() {

	            @Override
	            public Point2D transform(NetNode node) {
	                return new Point2D.Double(node.getXpos(), node.getYpos());
	            }
	        };
		}
		return this.locationTransformer;
	}
	
	public void rebuild(){
		g = new OSPFSparseGraph();
		populateGraph();
	}
	
	
	//vitor
	
	public class OSPFSparseGraph extends SparseGraph<NetNode,NetEdge>{
	
		
		int nodeCounter=0;
		int edgeCounter=0;
		
		
		
		public void initNodeCounter(int n){
			nodeCounter=n;
		}
		
		public void initEdgeCounter(int n){
			edgeCounter=n;
		}
		
		
		public boolean addEdge(NetEdge e, NetNode v1, NetNode v2){
			
			 if(v1.getNodeId()!=v2.getNodeId()){
				e.setFrom(v1.getNodeId());
			 	e.setTo(v2.getNodeId());
				return super.addEdge(e,v1,v2);
			}
			else{ 
				return false;
			}
		 } 
		 
		
		
		 public boolean addEdge(NetEdge e, NetNode v1, NetNode v2,EdgeType edgeType){
			 
			 
			 if(v1.getNodeId()!=v2.getNodeId()){
				 	e.setFrom(v1.getNodeId());
				 	e.setTo(v2.getNodeId());
					return super.addEdge(e,v1,v2,edgeType);
				}
				else{ 
					return false;
				}
		 }
		 
		
		  
		 public boolean addVertex(NetNode vertex){
			if(vertex==null){
				return false;
			}
			else{ 
				vertex.setNodeId(nodeCounter++);
				return super.addVertex(vertex);
			}
				
			
		 }
		 
		
	}
}
