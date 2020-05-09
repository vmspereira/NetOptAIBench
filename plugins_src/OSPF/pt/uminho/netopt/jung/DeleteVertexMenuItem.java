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

import edu.uci.ics.jung.visualization.VisualizationViewer;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.netopt.aibench.views.TopologyGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


@SuppressWarnings("serial")
public class DeleteVertexMenuItem<NetNode> extends JMenuItem implements
		VertexMenuListener<NetNode> {
	private NetNode vertex;
	private VisualizationViewer<NetNode, NetEdge> visComp;
	private TopologyGraph topology;

	/** Creates a new instance of DeleteVertexMenuItem */
	public DeleteVertexMenuItem(final TopologyGraph topology) {
		super("Delete");
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int conf = JOptionPane.showConfirmDialog(topology,
						"Please confirme removal", "Remove Vertex",
						JOptionPane.YES_NO_OPTION);
				if (conf == JOptionPane.YES_OPTION) {
					
					//TopologyEvent evt = new TopologyEvent(this,
					//		TopologyEvent.REMOVE_NODE, vertex);
					//topology.getNetworkTopology().fireTopologyChanged(evt);
					
					visComp.getPickedVertexState().pick(vertex, false);
					visComp.getGraphLayout().getGraph().removeVertex(vertex);
					visComp.repaint();

				}
			}
		});
	}

	/**
	 * Implements the VertexMenuListener interface.
	 * 
	 * @param v
	 * @param visComp
	 */
	public void setVertexAndView(NetNode v,	VisualizationViewer<NetNode, NetEdge> visComp) {
		this.vertex = v;
		this.visComp = visComp;
		this.setText("Delete " + v.toString());
	}

}
