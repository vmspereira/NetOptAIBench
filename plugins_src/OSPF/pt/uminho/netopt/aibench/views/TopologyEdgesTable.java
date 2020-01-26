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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import pt.uminho.algoritmi.netopt.ospf.graph.Graph;
import pt.uminho.algoritmi.netopt.ospf.graph.Graph.Status;
import pt.uminho.algoritmi.netopt.ospf.listener.ITopologyChangeListener;
import pt.uminho.algoritmi.netopt.ospf.listener.TopologyEvent;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetGraph;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode.NodeType;
import pt.uminho.netopt.aibench.datatypes.NetworkTopologyBox;
import pt.uminho.netopt.aibench.views.TopologyNodesTable.TableMouseListener;

@SuppressWarnings("serial")
public class TopologyEdgesTable extends javax.swing.JPanel implements
		ITopologyChangeListener {

	private NetworkTopology topology;
	private NetGraph graph;
	private String[] columns;
	private JTable edgesTable;
	private UpdatableModel model;
	private JLabel lnedges;

	public TopologyEdgesTable(NetworkTopologyBox box) {
		this.topology = box.getNetworkTopology();
		topology.addTopologyChangeListener(this);
		this.graph = topology.getNetGraph();
		initGUI();
	}

	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(600, 600));
			this.setSize(600, 600);

		
			JPanel header = new JPanel();
			BoxLayout HeaderLayout = new BoxLayout(header,
					javax.swing.BoxLayout.Y_AXIS);
			this.add(header, BorderLayout.NORTH);
			header.setLayout(HeaderLayout);
			header.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.LOWERED));
			JLabel jLabel1;
			jLabel1 = new JLabel();
			header.add(jLabel1);
			jLabel1.setText("Topology Edges");
			jLabel1.setIcon(new ImageIcon(getClass().getClassLoader()
					.getResource("images/netTop32.png")));

			lnedges = new JLabel();
			header.add(lnedges);
			lnedges.setText("Number of Edges: " + graph.getNEdges());
		//	JLabel lfile = new JLabel("File: " + topology.getFilenameEdges());
		//	header.add(lfile);

			JPanel bottom = new JPanel();
			bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
			JButton b1 = new JButton("Connectedness");
			bottom.add(b1);
			b1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					boolean b = topology.getGraph().isConnected();
					String msg;
					if (b)
						msg = "The graph is connected.";
					else
						msg = "The graph is not connected.";
					JOptionPane.showMessageDialog(edgesTable, msg);
				}

			});
			this.add(bottom, BorderLayout.SOUTH);

			columns = new String[] { "Id", "From", "To", "Length", "Delay",
					"Bandwidth", "ASfrom", "ASto", "Status" };

			model = new UpdatableModel() {

				public int getColumnCount() {
					return columns.length;
				}

				public int getRowCount() {
					return graph.getNEdges();
				}

				public String getColumnName(int columnIndex) {
					return columns[columnIndex];
				}

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					if (columnIndex == 5 || columnIndex == 3
							|| columnIndex == 4)
						return true;
					else
						return false;
				}

				public Object getValueAt(int rowIndex, int columnIndex) {
					// int
					// index=edgesTable.getMainTable().getRowSorter().convertRowIndexToModel(rowIndex);
					int index = rowIndex;
					switch (columnIndex) {
					case 0:
						return graph.getEdges()[index].getEdgeId();
					case 1:
						return graph.getEdges()[index].getFrom();
					case 2:
						return graph.getEdges()[index].getTo();
					case 3:
						return graph.getEdges()[index].getLength();
					case 4:
						return graph.getEdges()[index].getDelay();
					case 5:
						return graph.getEdges()[index].getBandwidth();
					case 6:
						return graph.getEdges()[index].getASfrom();
					case 7:
						return graph.getEdges()[index].getASto();
					case 8:
						int n1 = graph.getEdges()[index].getFrom();
						int n2 = graph.getEdges()[index].getTo();
						Graph.Status s = topology.getGraph().getConnection(n1,
								n2);
						if (s.equals(Graph.Status.UP))
							return "UP";
						else if (s.equals(Graph.Status.DOWN))
							return "DOWN";
						else
							return "";
					default:
						return null;
					}
				}

				public Class<?> getColumnClass(int columnIndex) {
					switch (columnIndex) {
					case 3:
					case 4:
					case 5:
						return Double.class;
					default:
						return Integer.class;
					}
				}

				public void setValueAt(Object aValue, int rowIndex,
						int columnIndex) {
					NetEdge[] e = graph.getEdges();
					double value = ((Double) aValue).doubleValue();
					int index = rowIndex;
					if (columnIndex == 3) {
						e[index].setLength(value);
					} else if (columnIndex == 4) {
						e[index].setDelay(value);
					} else if (columnIndex == 5) {
						e[index].setBandwidth(value);
					}
					topology.updateGraph();
					boolean b = graph.isConnected(false);
					if (!b)
						JOptionPane.showMessageDialog(edgesTable,
								"Graph is not connected");
					this.fireTableDataChanged();
				}

				public void update() {
					this.fireTableDataChanged();
				}

			};

			edgesTable = new JTable(model);
			// edgesTable.setPreferredSize(new java.awt.Dimension(600, 600));
			FailedCellRenderer renderer = new FailedCellRenderer();
			edgesTable.setDefaultRenderer(Double.class, renderer);
			edgesTable.setDefaultRenderer(Integer.class, renderer);
			edgesTable.setAutoCreateRowSorter(true);
			JScrollPane js = new JScrollPane(edgesTable);
			this.add(js, BorderLayout.CENTER);
								
			edgesTable.addMouseListener(new TableMouseListener());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
public class TableMouseListener extends MouseAdapter {
	     
	    
	    private JPopupMenu popupMenuUP;
	    private JPopupMenu popupMenuDOWN;
	    
	     
	    public TableMouseListener() {
	    
	        
	        popupMenuUP =new JPopupMenu();
			JMenuItem menuItem;
			menuItem = new JMenuItem("Set link UP");
			menuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int i = edgesTable.getSelectedRow();
					int n = edgesTable.getRowSorter().convertRowIndexToModel(i);
					NetEdge e = graph.getEdges()[n];
					topology.setEdgeStatus(e, Status.UP);
					model.fireTableDataChanged();
				}

			});
			popupMenuUP.add(menuItem);

			popupMenuDOWN =new JPopupMenu();
			JMenuItem menuItem2;
			menuItem2 = new JMenuItem("Set link DOWN");
			menuItem2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int i = edgesTable.getSelectedRow();
					int n = edgesTable.getRowSorter().convertRowIndexToModel(i);
					NetEdge e = graph.getEdges()[n];
					topology.setEdgeStatus(e, Status.DOWN);
					model.fireTableDataChanged();
				}

			});
			popupMenuDOWN.add(menuItem2);

			
	    }
	     
	    @Override
	    public void mousePressed(MouseEvent event) {
	        // selects the row at which point the mouse is clicked
	        Point point = event.getPoint();
	        int currentRow = edgesTable.rowAtPoint(point);
	        edgesTable.setRowSelectionInterval(currentRow, currentRow);
	        int i = edgesTable.getSelectedRow();
			int n = edgesTable.getRowSorter().convertRowIndexToModel(i);
			NetEdge e = graph.getEdges()[n];
			if(e.isUP())
				edgesTable.setComponentPopupMenu(popupMenuDOWN);
			else
				edgesTable.setComponentPopupMenu(popupMenuUP);
			
	    }
	}

	
	
	
	
	
	class FailedCellRenderer extends DefaultTableCellRenderer {

		public FailedCellRenderer() {
			super();
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus,
				int rowIndex, int vColIndex) {

			Component c = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, rowIndex, vColIndex);
			// int index=table.getRowSorter().convertRowIndexToModel(rowIndex);
			int index = rowIndex;
			if (graph.getEdges()[index].getBandwidth() <= 0.0)
				c.setForeground(Color.RED);
			else
				c.setForeground(Color.BLACK);

			return c;
		}

	}


	class UpdatableModel extends DefaultTableModel {
		public void update() {
			this.fireTableDataChanged();
		}
	}

	@Override
	public void updateTopology(TopologyEvent evt) {
		try {
			if (evt.getSource() instanceof TopologyEdgesTable){
				return;
			}
			else{
				this.graph = topology.getNetGraph();
				lnedges.setText("Number of Edges: " + graph.getNEdges());
				model.update();
			}
		} catch (Exception ex) {
		}
	}

}
