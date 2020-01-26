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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import pt.uminho.algoritmi.netopt.ospf.graph.Graph;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode.NodeType;
import pt.uminho.netopt.aibench.views.TopologyGraph;





public class MouseMenu {

	private static NetworkTopology topology;
	private static Component parent;

	@SuppressWarnings("serial")
	
	private EdgeMenu edgeMenu;
	private VertexMenu vertexMenu;
	
	private List<MouseMenuListener> mouseMenuListeners = new ArrayList<MouseMenuListener>();
	
	public MouseMenu(NetworkTopology topol,Component par){
		topology = topol;
		parent = par;
		edgeMenu = new EdgeMenu();
		vertexMenu = new VertexMenu();
	}
	
	
	
	
	public EdgeMenu getEdgeMenu(){ return edgeMenu;}
	public VertexMenu getVertexMenu(){ return vertexMenu;}
	
	
	public void addMouseMenuListener(MouseMenuListener listener){
		mouseMenuListeners.add(listener);
	}
	
	private void fireListeners(){
		Iterator<MouseMenuListener> it=mouseMenuListeners.iterator();
		while(it.hasNext())
			it.next().mouseMenuActionPerformed();
	}
	
	
	private class EdgeMenu extends JPopupMenu {	
		
		// private JFrame frame;
		public EdgeMenu() {
			
			super("Link Menu");
			
			this.add(new BWDisplay());
			this.add(new DelayDisplay());
			this.add(new WeightDisplay());
			this.addSeparator();
			this.add(new EdgePropItem());
			// this.add(new DeleteEdgeMenuItem<NetEdge>(topology));
			this.addSeparator();
			this.add(new EdgeStatusItem());
		}

		
		
		
		private class EdgePropItem extends JMenuItem implements EdgeMenuListener<NetEdge>, MenuPointListener {
		
			NetEdge edge;
			VisualizationViewer visComp;
			Point2D point;

			public void setEdgeAndView(NetEdge edge, VisualizationViewer visComp) {
				this.edge = edge;
				this.visComp = visComp;
			}

			public void setPoint(Point2D point) {
				this.point = point;
			}

			public EdgePropItem() {
				super("Edit link Properties...");
				this.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						EdgePropertyDialog dialog = new EdgePropertyDialog(edge);
						JOptionPane option = new JOptionPane(dialog, JOptionPane.PLAIN_MESSAGE,
								JOptionPane.OK_CANCEL_OPTION);
						JDialog d = option.createDialog(parent, "Edit Edge ID " + edge.getEdgeId());
						d.setVisible(true);
						int n = -1;
						try {
							n = ((Number) option.getValue()).intValue();
						} catch (Exception exp) {
						}
						if (n == JOptionPane.OK_OPTION) {
							System.out.println("Changing edge definition");
							edge.setBandwidth(dialog.getBw());
							edge.setDelay(dialog.getDelay());
							edge.setLength(dialog.getLength());
						}
						fireListeners();
					}
					

				});
			}
		}

		
		
		
		
		private  class EdgeStatusItem extends JMenuItem implements EdgeMenuListener<NetEdge>, MenuPointListener {
			NetEdge edge;
			VisualizationViewer visComp;
			Point2D point;

			public void setEdgeAndView(NetEdge edge, VisualizationViewer visComp) {
				this.edge = edge;
				if (topology.getGraph().getConnection(edge.getFrom(), edge.getTo())
						.equals(Graph.Status.DOWN))
					this.setText("Set Edge UP");
				else
					this.setText("Set Edge DOWN");
				this.visComp = visComp;
			}

			public void setPoint(Point2D point) {
				this.point = point;
			}

			public EdgeStatusItem() {
				super();
				this.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (topology.getGraph().getConnection(edge.getFrom(), edge.getTo())
								.equals(Graph.Status.DOWN)) {
							System.out.println("Edge " + edge.getEdgeId() + " set to UP");
							topology.setEdgeStatus(edge,Graph.Status.UP);
						} else if (topology.getGraph().getConnection(edge.getFrom(), edge.getTo())
								.equals(Graph.Status.UP)) {
							System.out.println("Edge " + edge.getEdgeId() + " set to DOWN");
							topology.setEdgeStatus(edge,Graph.Status.DOWN);
						}
						visComp.repaint();
						fireListeners();
					}

				});
			}

		}

		
		
	
		
		
		private  class WeightDisplay extends JMenuItem implements EdgeMenuListener<NetEdge> {
			public void setEdgeAndView(NetEdge e, VisualizationViewer visComp) {
				this.setText("Length  = " + e.getLength());
			}
		}

		private  class BWDisplay extends JMenuItem implements EdgeMenuListener<NetEdge> {
			public void setEdgeAndView(NetEdge e, VisualizationViewer visComp) {
				this.setText("BandWidth  = " + e.getBandwidth() + "Mbit/s");
			}
		}

		private  class DelayDisplay extends JMenuItem implements EdgeMenuListener<NetEdge> {
			public void setEdgeAndView(NetEdge e, VisualizationViewer visComp) {
				this.setText("Delay  = " + e.getDelay() + "ms");
			}
		}

	} // end edgemenu
	

	
	
	
	@SuppressWarnings("serial")
	private  class VertexMenu extends JPopupMenu {
	
		public VertexMenu() {
			super("Node Menu");
			this.add(new NodeLabel());
			this.addSeparator();
			this.add(new NodeTypeItem());
		}
	}

	
	
	
	
	
	
	@SuppressWarnings("serial")
	private class NodeLabel extends JLabel implements VertexMenuListener<NetNode>{

		public NodeLabel(){
			this.setHorizontalAlignment(CENTER);
		}
		
		@Override
		public void setVertexAndView(NetNode v, VisualizationViewer<NetNode, NetEdge> visView) {
			this.setText(v.getLabel());
		}
		
	}
	
	
	
	
	
	
	@SuppressWarnings("serial")
	private  class NodeTypeItem extends JMenuItem implements VertexMenuListener<NetNode>, MenuPointListener {
		NetNode node;
		VisualizationViewer<NetNode, NetEdge> visComp;
		Point2D point;

		

		public void setPoint(Point2D point) {
			this.point = point;
		}

		public NodeTypeItem() {
			super();
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(node.getNodeType()==NodeType.LEGACY)
						node.setNodeType(NodeType.SDN_SR);
					else
						node.setNodeType(NodeType.LEGACY);
					visComp.repaint();
					fireListeners();
				}

			});
		}

		
		@Override
		public void setVertexAndView(NetNode node, VisualizationViewer<NetNode, NetEdge> visView) {
			this.node = node;
			if(node.getNodeType()==NodeType.LEGACY)
				this.setText("Change to "+NodeType.SDN_SR+" node");
			else
				this.setText("Change to "+NodeType.LEGACY+" node");
			this.visComp = visView;
		}

	}

	
	
	
	
}
