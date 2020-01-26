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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;

import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.netopt.aibench.gui.utils.table.RowHeaderRenderer;




public class DemandsView extends JPanel {

	private Demands demands;
	private String[] columns;

	/** Creates new form NewJPanel */
	public DemandsView(Demands demands) {
		this.demands = demands;
		initComponents();
	}

	private void initComponents() {


		setLayout(new java.awt.BorderLayout());

		jPanel1 = new JPanel();
		this.add(jPanel1, BorderLayout.NORTH);
		BoxLayout jPanel1Layout = new BoxLayout(jPanel1,
				javax.swing.BoxLayout.Y_AXIS);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));


		jLabel1 = new JLabel();
		jPanel1.add(jLabel1);
		jLabel1.setText("Demands");
		jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
				"images/demands32.png")));

		jLabel2 = new JLabel();
		jPanel1.add(jLabel2);
		jLabel2.setText("Demands: " + demands.getFilename());

		jScrollPane1 = new javax.swing.JScrollPane();
		this.add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.setName("jScrollPane1");

		jTable1 = new javax.swing.JTable();
		jScrollPane1.setViewportView(jTable1);
		jTable1.setName("jTable1"); // NOI18N
		jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		columns = new String[demands.getDimension()];
		for (int i = 0; i < demands.getDimension(); i++) {
			columns[i] = "Node " + i;
		}
		
		JList rowHeader = new JList(columns);    
	    rowHeader.setFixedCellWidth(60);
	    rowHeader.setFixedCellHeight(jTable1.getRowHeight());
	    rowHeader.setBackground(jTable1.getTableHeader().getBackground());
	    rowHeader.setCellRenderer(new RowHeaderRenderer(jTable1));
	    jScrollPane1.setRowHeaderView(rowHeader);
	    
		Double[][] demObjs = demands.getDemandsObj();

		jTable1.setModel(new DemandsTableModel());
			
			
		
		
		
		
		

	}// </editor-fold>

	
	private class DemandsTableModel extends AbstractTableModel{
 
		
		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			return demands.getDimension();
		}

		@Override
		public Object getValueAt(int row, int column) {
			return demands.getDemands(row, column);
		}
		
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex){	
			demands.setDemands(rowIndex, columnIndex, (Double)aValue);
		}
		
		@Override
		public Class getColumnClass(int columnIndex){
			
			return Double.class;
		}
		
		
		public String getColumnName(int column){
			return "Node "+column;
		}
		
		@Override
		public boolean isCellEditable(int rowIndex,int columnIndex){
			return true;
		}

		
		
	}
	

	private javax.swing.JScrollPane jScrollPane1;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel jPanel1;
	private javax.swing.JTable jTable1;

	
	

}
