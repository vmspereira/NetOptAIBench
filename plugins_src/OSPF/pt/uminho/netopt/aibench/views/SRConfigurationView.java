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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.LabelPath;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.SRConfiguration;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.SRNodeConfiguration;
import pt.uminho.netopt.aibench.gui.utils.table.PercentageRenderer;

@SuppressWarnings("serial")
public class SRConfigurationView extends javax.swing.JPanel {

	private SRConfiguration conf;
	private JPanel mainPanel;

	public SRConfigurationView(SRConfiguration configuration) {
		this.conf = configuration;
		mainPanel=this;
		initGUI();
	}

	private void initGUI() {
		BorderLayout thisLayout = new BorderLayout();
		this.setLayout(thisLayout);
		JPanel mainHeader = new JPanel();
		mainHeader.setLayout(new BoxLayout(mainHeader, javax.swing.BoxLayout.X_AXIS));
		mainHeader.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		JPanel header = new JPanel();
		BoxLayout HeaderLayout = new BoxLayout(header, javax.swing.BoxLayout.Y_AXIS);
		this.add(mainHeader, BorderLayout.NORTH);
		header.setLayout(HeaderLayout);
		mainHeader.add(header);
		JLabel jLabel1;
		jLabel1 = new JLabel();
		header.add(jLabel1);
		jLabel1.setText("SR Configuration");
		jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/tags32.png")));

		SRModel m = new SRModel(conf);
		JTable table = new JTable(m);
		table.setAutoCreateRowSorter(true);
		table.setShowHorizontalLines(false);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(new PercentageRenderer(new DefaultTableCellRenderer(), 1));
		JScrollPane js = new JScrollPane(table);
		DecimalFormat df = new DecimalFormat("#.#%");
		this.add(js, BorderLayout.CENTER);
		JLabel jLabel2 = new JLabel("Total Number of Label Paths: " + m.getRowCount());
		JLabel jLabel3 = new JLabel("1-Segment: " + m.getDephts(0) + " (" + df.format(m.getDephtsPercentage(0)) + ")");
		JLabel jLabel4 = new JLabel("2-Segments: " + m.getDephts(1) + " (" + df.format(m.getDephtsPercentage(1)) + ")");
		JLabel jLabel5 = new JLabel("3-Segments: " + m.getDephts(2) + " (" + df.format(m.getDephtsPercentage(2)) + ")");
		header.add(jLabel2);
		header.add(jLabel3);
		header.add(jLabel4);
		header.add(jLabel5);
		JButton button = new JButton("Analyse Demands");
		JPanel p_demands = new JPanel();
		p_demands.setLayout(new FlowLayout(FlowLayout.LEFT));
		JComboBox<ClipboardItem> combo = new JComboBox<ClipboardItem>(this.getItemsByClass(Demands.class));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ClipboardItem item = (ClipboardItem) combo.getSelectedItem();
					Demands demands = (Demands) item.getUserData();
					Collection<SRNodeConfiguration> n = conf.getNodesConfigurations();
					double p1= 0;
					double p2= 0;
					double p3= 0;
					for (SRNodeConfiguration nc : n) {
						Collection<ArrayList<LabelPath>> u = nc.getConfiguration().values();
						for (ArrayList<LabelPath> a : u) {
							for (LabelPath b : a) {
								int src = b.getSource().getNodeId();
								int dst= b.getDestination().getNodeId();
								double v= demands.getDemands(src,dst) * b.getFraction();;
								switch (b.getLabelStackLength()) {
								case 1:
									p1+=v;
									break;
								case 2:
									p2+=v;
									break;
								case 3:
									p3+=v;
									break;
								default:
									break;
								}
							}
						}
					}
					
					NumberFormat defaultFormat = NumberFormat.getPercentInstance();
					defaultFormat.setMinimumFractionDigits(1);
					double sum =p1+p2+p3;
					String s = "1-segment: "+defaultFormat.format(p1/sum)
							+"\n2-segments: "+defaultFormat.format(p2/sum) 
							+"\n3-segments: "+defaultFormat.format(p3/sum);
					JOptionPane.showMessageDialog(mainPanel, s,"Demands Splitting",JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {

				}
			}

		});
		p_demands.add(combo);
		p_demands.add(button);
		mainHeader.add(p_demands);
	}

	private ClipboardItem[] getItemsByClass(Class<?> type) {
		List<ClipboardItem> l = Core.getInstance().getClipboard().getItemsByClass(type);
		ClipboardItem[] items = new ClipboardItem[l.size()];
		return l.toArray(items);
	}

	private class SRModel extends AbstractTableModel {

		private Object[][] values;
		private int[] depht;

		public SRModel(SRConfiguration c) {
			this.values = new Object[c.getSize()][5];
			depht = new int[3];
			depht[0] = 0;
			depht[1] = 0;
			depht[2] = 0;
			int line = 0;
			Collection<SRNodeConfiguration> n = c.getNodesConfigurations();
			for (SRNodeConfiguration nc : n) {
				Collection<ArrayList<LabelPath>> u = nc.getConfiguration().values();
				for (ArrayList<LabelPath> a : u) {
					for (LabelPath b : a) {
						values[line][0] = b.getSource().toString();
						values[line][1] = b.getDestination().toString();
						values[line][2] = b.getLabelStackLength();
						values[line][3] = b.getLabelStackString();
						values[line][4] = b.getFraction();
						depht[(int) (values[line][2]) - 1]++;
						line++;
					}
				}
			}
		}

		public int getDephts(int i) {
			return this.depht[i];
		}

		public double getDephtsPercentage(int i) {
			return (double) this.depht[i] / getRowCount();
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public int getRowCount() {
			return values.length;
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			return values[arg0][arg1];
		}

		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return "Source Node";
			case 1:
				return "Destination Node";
			case 2:
				return "Segment List Length";
			case 3:
				return "Segments List";
			case 4:
				return "Parallel Path Fraction";
			default:
				return "";
			}
		}

		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 4:
				return Double.class;
			case 2:
				return Integer.class;
			default:
				return String.class;
			}
		}

	}
	
	
	

}
