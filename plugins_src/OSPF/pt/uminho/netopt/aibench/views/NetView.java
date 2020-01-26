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

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetGraph;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode;



public class NetView extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NetGraph brite;
	private JScrollPane jScrollPane1;
	private JTable jTable1;
	private JLabel Title;

	public NetView(NetGraph brite) {
		super();
		this.brite = brite;
		initGUI();
	}

	private void initGUI() {
		try {
			BoxLayout thisLayout = new BoxLayout(this,
					javax.swing.BoxLayout.Y_AXIS);
			this.setLayout(thisLayout);
			this.setPreferredSize(new Dimension(800, 600));
			this.setSize(800, 600);
			int nnodes = this.brite.getNNodes();
			NetNode[] nodes = this.brite.getNodes();
			{
				Title = new JLabel();
				this.add(Title);
				Title.setText("Nodes");
			}
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1);
				jScrollPane1.setPreferredSize(new java.awt.Dimension(587, 381));
				{
					String[] columns = new String[nnodes];
					for (int i = 0; i < nnodes; i++) {
						columns[i] = "Node " + (i + 1);
					}
					String[][] cells = new String[5][nnodes];
					for (int i = 0; i < nnodes; i++) {
						cells[0][i] = Integer.toString(nodes[i].getNodeId());
						cells[1][i] = Double.toString(nodes[i].getXpos());
						cells[2][i] = Double.toString(nodes[i].getYpos());
						cells[3][i] = Integer.toString(nodes[i].getIndegree());
						cells[4][i] = Integer.toString(nodes[i].getOutdegree());
					}
					TableModel jTable1Model = new DefaultTableModel(cells,
							columns);
					jTable1 = new JTable();
					jScrollPane1.setViewportView(jTable1);
					jTable1.setModel(jTable1Model);
					jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				}
				{
					JLabel Title2 = new JLabel();
					this.add(Title2);
					Title.setText("Edges");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
