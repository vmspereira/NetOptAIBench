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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import pt.uminho.algoritmi.netopt.ospf.simulation.AverageEndtoEndDelays;
import pt.uminho.netopt.aibench.gui.utils.table.RowHeaderRenderer;


@SuppressWarnings("serial")
public class AverageDelaysView extends JPanel {

	private AverageEndtoEndDelays delays;
    private String[] columns;
	
	public AverageDelaysView(AverageEndtoEndDelays delays) {
		this.delays = delays;
		initComponents();
	}

	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();

		setLayout(new java.awt.BorderLayout());

		jPanel1 = new JPanel();
		this.add(jPanel1, BorderLayout.NORTH);
		BoxLayout jPanel1Layout = new BoxLayout(jPanel1,
				javax.swing.BoxLayout.Y_AXIS);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		JLabel jLabel1 = new JLabel();
		jLabel1.setText("Average End to End Delays");
		
		JLabel jLabel2 = new JLabel("Delay Penalties:"+delays.getDelayPenalties());
		JLabel jLabel3 = new JLabel("Sum Delay:"+delays.getSumDelays());
		
		jPanel1.add(jLabel2);
		jPanel1.add(jLabel3);

		
		
		jScrollPane1.setName("jScrollPane1");

		columns = new String[delays.getEndToEndDelays().length];
		for (int i = 0; i < delays.getEndToEndDelays().length; i++) {
			columns[i] = "Node " + i;
		}
		
		JList<String> rowHeader = new JList<String>(columns);    
	    rowHeader.setFixedCellWidth(60);
	    rowHeader.setFixedCellHeight(jTable1.getRowHeight());
	    rowHeader.setBackground(jTable1.getTableHeader().getBackground());
	    rowHeader.setCellRenderer(new RowHeaderRenderer(jTable1));
	    jScrollPane1.setRowHeaderView(rowHeader);
	    
	    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTable1.setModel(new DefaultTableModel()
		{
			
			public String getColumnName(int column){
				return columns[column];
			}
			
			public int getColumnCount(){return columns.length;}
			public int getRowCount(){return columns.length;}
			
			public Object getValueAt(int row, int column)
			{
				return delays.getEndToEndDelays()[row][column];
			}
			
			public Class getColumnClass(int n) {
				return Double.class;
			}
		});

		jScrollPane1.setViewportView(jTable1);
		add(jScrollPane1, java.awt.BorderLayout.CENTER);
	}

	
	
	private javax.swing.JScrollPane jScrollPane1;
	private JPanel jPanel1;
	private javax.swing.JTable jTable1;
	
	
	

}
