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
package pt.uminho.netopt.aibench.views;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.netopt.aibench.datatypes.NetworkTopologyBox;


@SuppressWarnings("serial")
public class NetworkTopologyView extends JPanel {
	private NetworkTopology topology;
	private JLabel nnodes;
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private JPanel Tables;
	private JLabel nedges;
	private JLabel jLabel1;
	private JPanel header;
	private JLabel filenameNodes;
	private JLabel filenameEdges;

	public NetworkTopologyView(NetworkTopologyBox box) {
		this.topology = box.getNetworkTopology();
		initGUI();
	}


	private void initGUI() {
		try {
			{
				BorderLayout thisLayout = new BorderLayout();
				this.setLayout(thisLayout);
				this.setPreferredSize(new java.awt.Dimension(600, 600));
				{
					header = new JPanel();
					BoxLayout HeaderLayout = new BoxLayout(header,
							javax.swing.BoxLayout.Y_AXIS);
					header.setLayout(HeaderLayout);
					this.add(header, BorderLayout.NORTH);
					header.setPreferredSize(new java.awt.Dimension(600, 98));
					header.setBorder(BorderFactory
							.createEtchedBorder(BevelBorder.LOWERED));
					{
						jLabel1 = new JLabel();
						header.add(jLabel1);
						jLabel1.setText("Network Topology");
						jLabel1.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										"images/netTop32.png")));
					}
					{
						nnodes = new JLabel();
						header.add(nnodes);
						nnodes.setText("Number of nodes: "
								+ topology.getDimension());
					}
					{
						nedges = new JLabel();
						header.add(nedges);
						nedges.setText("Number of edges: "
								+ topology.getNumberEdges());
					}
					{
						filenameNodes = new JLabel();
						header.add(filenameNodes);
						filenameNodes.setText("Nodes File: "
								+ topology.getFilenameNodes());
					}
					{
						filenameEdges = new JLabel();
						header.add(filenameEdges);
						filenameEdges.setText("Edges Filename: "
								+ topology.getFilenameEdges());
					}
				}
				{
					Tables = new JPanel();
					BorderLayout TablesLayout = new BorderLayout();
					this.add(Tables, BorderLayout.CENTER);
					Tables.setLayout(TablesLayout);
					Tables.setPreferredSize(new java.awt.Dimension(600, 10));
					{
						jPanel1 = new JPanel();
						BoxLayout jPanel1Layout = new BoxLayout(jPanel1,
								javax.swing.BoxLayout.Y_AXIS);
						jPanel1.setLayout(jPanel1Layout);
						Tables.add(jPanel1, BorderLayout.WEST);
						jPanel1.setPreferredSize(new java.awt.Dimension(139,
								502));
					}
					{
						jScrollPane1 = new JScrollPane();
						Tables.add(jScrollPane1, BorderLayout.CENTER);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
