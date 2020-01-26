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
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;

import pt.uminho.algoritmi.netopt.ospf.listener.TopologyEvent;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetGraph;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode.NodeType;
import pt.uminho.netopt.aibench.datatypes.NetworkTopologyBox;

@SuppressWarnings("serial")
public class TopologyNodesTable extends javax.swing.JPanel  {

	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel header;
	private NetworkTopology topology;
	private NodesTableModel modelo;
	private JTable nodesTable;
	private NetGraph graph;

	public TopologyNodesTable(NetworkTopologyBox box) {
		this.topology = box.getNetworkTopology();
		initGUI();
	}

	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(600, 600));
			this.setSize(600, 600);
			this.graph = topology.getNetGraph();
			modelo=new NodesTableModel();
			nodesTable = new JTable(modelo);
			nodesTable.setAutoCreateRowSorter(true);
			JScrollPane js=new JScrollPane(nodesTable);
			this.add(js, BorderLayout.CENTER);

			header = new JPanel();
			BoxLayout HeaderLayout = new BoxLayout(header,
					javax.swing.BoxLayout.Y_AXIS);
			this.add(header, BorderLayout.NORTH);
			header.setLayout(HeaderLayout);
			header.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.LOWERED));

			jLabel1 = new JLabel();
			header.add(jLabel1);
			jLabel1.setText("Topology Nodes");
			jLabel1.setIcon(new ImageIcon(getClass().getClassLoader()
					.getResource("images/netTop32.png")));

			jLabel2 = new JLabel();
			header.add(jLabel2);
			jLabel2.setText("Number of Nodes: "
					+ topology.getNetGraph().getNNodes());
//			JLabel lfile = new JLabel("File: " + topology.getFilenameNodes());
//			header.add(lfile);
			
			JPanel bottom = new JPanel();
			bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
			
			JButton b1 = new JButton("All Legacy");
			bottom.add(b1);
			b1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					topology.getNetGraph().setAllNodesType(NetNode.NodeType.LEGACY);
					modelo.fireTableDataChanged();
				}

			});
			
			
			JButton b2 = new JButton("All SDN/SR");
			bottom.add(b2);
			b2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					topology.getNetGraph().setAllNodesType(NetNode.NodeType.SDN_SR);
					modelo.fireTableDataChanged();
				}

			});
			
			this.add(bottom, BorderLayout.SOUTH);

			
			
			nodesTable.addMouseListener(new TableMouseListener());



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	public class TableMouseListener extends MouseAdapter {
	     
	    
	    private JPopupMenu popupMenuLegacy;
	    private JPopupMenu popupMenuSDN;
	    
	     
	    public TableMouseListener() {
	    
	        
	        popupMenuLegacy =new JPopupMenu();
			JMenuItem menuItem;
			menuItem = new JMenuItem("Change to LEGACY");
			menuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int i = nodesTable.getSelectedRow();
					int n = nodesTable.getRowSorter().convertRowIndexToModel(i);
					NetNode node = graph.getNodes()[n];
					node.setNodeType(NodeType.LEGACY);
					modelo.fireTableDataChanged();
				}

			});
			popupMenuLegacy.add(menuItem);

			popupMenuSDN =new JPopupMenu();
			JMenuItem menuItem2;
			menuItem2 = new JMenuItem("Change to SDN/SR");
			menuItem2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int i = nodesTable.getSelectedRow();
					int n = nodesTable.getRowSorter().convertRowIndexToModel(i);
					NetNode node = graph.getNodes()[n];
					node.setNodeType(NodeType.SDN_SR);
					modelo.fireTableDataChanged();
				}

			});
			popupMenuSDN.add(menuItem2);

			
	    }
	     
	    @Override
	    public void mousePressed(MouseEvent event) {
	        // selects the row at which point the mouse is clicked
	        Point point = event.getPoint();
	        int currentRow = nodesTable.rowAtPoint(point);
	        nodesTable.setRowSelectionInterval(currentRow, currentRow);
	        int i = nodesTable.getSelectedRow();
			int n = nodesTable.getRowSorter().convertRowIndexToModel(i);
			NetNode node = graph.getNodes()[n];
			if(node.getNodeType().equals(NodeType.LEGACY))
				nodesTable.setComponentPopupMenu(popupMenuSDN);
			else
				nodesTable.setComponentPopupMenu(popupMenuLegacy);
			
	    }
	}
	
	
	private class NodesTableModel extends AbstractTableModel {

		String[] columns = new String[] { "Id", "Label", "XPos", "YPos", "InDegree",
				"OutDegree", "Type" };

		@Override
		public int getColumnCount() {
			return 7;
		}

		@Override
		public int getRowCount() {
			return topology.getNetGraph().getNodes().length;
		}

		@Override
		public Object getValueAt(int row, int column) {
			NetNode node = topology.getNetGraph().getNodeAt(row);
			try{
			switch (column) {
			case 0:
				return node.getNodeId();
			case 1:
				return node.getLabel();	
			case 2:
				return node.getXpos();
			case 3:
				return node.getYpos();
			case 4:
				return node.getIndegree();
			case 5:
				return node.getOutdegree();
			case 6:
				return node.getNodeType();
			default:
				return null;
			}
			}catch(Exception e){ e.printStackTrace(); return null;}

		}

		
		public String getColumnName(int col){
			return columns[col];
		}
	}

	public void updateTopology(TopologyEvent evt) {
		try {
			if (evt.getSource() instanceof TopologyNodesTable){
				return;
			}
			else{
				modelo.fireTableDataChanged();
				jLabel2.setText("Number of Nodes: "	+ topology.getNetGraph().getNNodes());
			}
		}catch(Exception e){}
	}

}
