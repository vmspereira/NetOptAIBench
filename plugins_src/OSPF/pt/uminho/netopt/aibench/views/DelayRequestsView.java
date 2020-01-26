/*******************************************************************************
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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;

import pt.uminho.algoritmi.netopt.ospf.simulation.DelayRequests;
import pt.uminho.netopt.aibench.gui.utils.table.RowHeaderRenderer;


public class DelayRequestsView extends JPanel {

	private DelayRequests delays;
	private javax.swing.JScrollPane jScrollPane1;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel Header;
	private javax.swing.JTable jTable1;

	public DelayRequestsView(DelayRequests delays) {
		this.delays = delays;
		initComponents();
	}

	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();

		setName("");
		setLayout(new java.awt.BorderLayout());
		this.setPreferredSize(new java.awt.Dimension(600, 600));
		this.setSize(600, 600);

		Header = new JPanel();
		BoxLayout HeaderLayout = new BoxLayout(Header,
				javax.swing.BoxLayout.Y_AXIS);
		Header.setLayout(HeaderLayout);
		this.add(Header, BorderLayout.NORTH);
		BoxLayout jLabel2Layout = new BoxLayout(jLabel2,
				javax.swing.BoxLayout.Y_AXIS);
		Header.setLayout(HeaderLayout);

		jLabel1 = new JLabel();
		Header.add(jLabel1);
		jLabel1.setText("Delay Requests");
		jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
				"images/delayR32.png")));
		jLabel1.setLayout(null);

		jLabel2 = new JLabel();
		Header.add(jLabel2);
		jLabel2.setText("Delay Requests: " + delays.getFilename());
		jLabel2.setLayout(null);

		jScrollPane1.setName("jScrollPane1");

		String[] columns = new String[delays.getDimension()];
		for (int i = 0; i < delays.getDimension(); i++) {
			columns[i] = "Node " + i;
		}

		Double[][] demObjs = delays.getDelayRequestsObj();

		
		JList rowHeader = new JList(columns);    
	    rowHeader.setFixedCellWidth(60);
	    rowHeader.setFixedCellHeight(jTable1.getRowHeight());
	    rowHeader.setBackground(jTable1.getTableHeader().getBackground());
	    rowHeader.setCellRenderer(new RowHeaderRenderer(jTable1));
	    jScrollPane1.setRowHeaderView(rowHeader);
		
		jTable1.setModel(new javax.swing.table.DefaultTableModel(demObjs,columns) {
			public Class getColumnClass(int n) {
				return Double.class;
			}
			public boolean isCellEditable(int rowIndex, int columnIndex){ return false;}
		});
		jTable1.setName("jTable1"); 
		jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jScrollPane1.setViewportView(jTable1);

		add(jScrollPane1, java.awt.BorderLayout.CENTER);
	}// </editor-fold>


	


}
