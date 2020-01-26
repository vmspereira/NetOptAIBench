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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import pt.uminho.algoritmi.netopt.ospf.listener.TopologyEvent;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.netopt.aibench.views.TopologyGraph;

@SuppressWarnings("serial")
public class DeleteEdgeMenuItem<E> extends JMenuItem implements
		EdgeMenuListener<E> {
	private E edge;
	private VisualizationViewer visComp;
	private TopologyGraph topology;

	/** Creates a new instance of DeleteEdgeMenuItem */
	public DeleteEdgeMenuItem(final TopologyGraph topology) {
		super("Delete Edge");
		this.topology = topology;
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int conf = JOptionPane.showConfirmDialog(topology,
						"Please confirme removal", "Remove Edge",
						JOptionPane.YES_NO_OPTION);
				if (conf == JOptionPane.YES_OPTION) {
					TopologyEvent evt = new TopologyEvent(this,
							TopologyEvent.REMOVE_EDGE, edge);
			//		topology.getNetworkTopology().fireTopologyChanged(evt);
					visComp.getPickedEdgeState().pick(edge, false);
					visComp.getGraphLayout().getGraph().removeEdge(edge);
					visComp.repaint();
				}
			}
		});
	}

	/**
	 * Implements the EdgeMenuListener interface to update the menu item with
	 * info on the currently chosen edge.
	 * 
	 * @param edge
	 * @param visComp
	 */
	public void setEdgeAndView(E edge, VisualizationViewer visComp) {
		this.edge = edge;
		this.visComp = visComp;
		this.setText("Delete link " + ((NetEdge) edge).getEdgeId());
	}

}
