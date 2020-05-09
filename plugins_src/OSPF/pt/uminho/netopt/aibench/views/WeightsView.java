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
import java.awt.Color;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;

import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.netopt.aibench.gui.utils.table.RowHeaderRenderer;




@SuppressWarnings("serial")
public class WeightsView extends JPanel {

	private OSPFWeights weights;

	/** Creates new form NewJPanel */
	public WeightsView(OSPFWeights weights) {
		this.weights = weights;
		initComponents();
	}

	@SuppressWarnings("unchecked")
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

		jLabel1 = new JLabel();
		jPanel1.add(jLabel1);
		jLabel1.setText("Weights");
		jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
				"images/weights32.png")));

		jLabel2 = new JLabel();
		jPanel1.add(jLabel2);
		jLabel2.setText("Creation method: " + weights.getCreationMethod());

		jScrollPane1.setName("jScrollPane1");

		String[] columns = new String[weights.getDimension()];
		for (int i = 0; i < weights.getDimension(); i++) {
			columns[i] = "Node " + i;
		}

		Double[][] weiObjs = weights.getWeightsObj();

		
		jTable1.setModel(new javax.swing.table.DefaultTableModel(weiObjs,columns) {
			//Define os objectos como sendo Double para ser utilizado pelo renderer.
			public Class<Double> getColumnClass(int n) {
				return Double.class;
			}
			
			public void setValueAt(Object aValue,
		              int rowIndex,
		              int columnIndex){
				super.setValueAt(aValue, rowIndex, columnIndex);
				if(aValue instanceof Double)
				weights.setWeight(rowIndex, columnIndex, ((Double)aValue).doubleValue());
			}
		});
		
		// row header 
		JList<String> rowHeader = new JList<String>(columns);    
	    rowHeader.setFixedCellWidth(60);
	    rowHeader.setFixedCellHeight(jTable1.getRowHeight());
	    rowHeader.setBackground(jTable1.getTableHeader().getBackground());
	    rowHeader.setCellRenderer(new RowHeaderRenderer(jTable1));
	    
		
	    
		jTable1.setDefaultRenderer(Double.class, new WeightsCellRenderer());
		jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jScrollPane1.setViewportView(jTable1);
		//adiciona o row header
		jScrollPane1.setRowHeaderView(rowHeader);
		add(jScrollPane1, java.awt.BorderLayout.CENTER);
	}


	private javax.swing.JScrollPane jScrollPane1;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel jPanel1;
	private javax.swing.JTable jTable1;

	// End of variables declaration

	
	
	/*
	 * CellRender
	 */
	public class WeightsCellRenderer extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected Color FOREGROUND_COLOR = Color.black;
		protected Color BACKGROUND_COLOR = Color.black;

		public WeightsCellRenderer() {
			super();
		}

		public WeightsCellRenderer(Color color) {
			super();
			FOREGROUND_COLOR = color;
		}

		public WeightsCellRenderer(Color forground, Color background) {
			super();
			FOREGROUND_COLOR = forground;
			BACKGROUND_COLOR = background;
			this.setBackground(background);
		}

		public void setValue(Object value) {
			if (value instanceof Number) {
				// os pesos OSPF são inteiros
				NumberFormat nf = NumberFormat.getIntegerInstance();
				setForeground(FOREGROUND_COLOR);
				setText(nf.format(value));
				this.setHorizontalAlignment(JLabel.CENTER);
				if (getText().trim().equals("0"))
					setText("");
			} else
				setText("");
		}
	}

	
	
	
	
}
