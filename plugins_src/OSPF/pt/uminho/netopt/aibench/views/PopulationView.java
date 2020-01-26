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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import pt.uminho.algoritmi.netopt.ospf.listener.PopulationChangedListener;
import pt.uminho.algoritmi.netopt.ospf.simulation.NodeTypeConfiguration;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population.SolutionType;
import pt.uminho.algoritmi.netopt.ospf.simulation.exception.DimensionErrorException;
import pt.uminho.algoritmi.netopt.ospf.simulation.solution.IntegerSolution;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.gui.MOEAPlotterPanel;
import pt.uminho.netopt.aibench.gui.utils.text.NumberTextField;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;


public class PopulationView extends JPanel implements PopulationChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Population population;
	private JPopupMenu popupMenu;
	private JTable jTable1;
	private PopulationTableModel model;
	private PopulationView populationView;
	private double beta=0.5;
	NumberTextField betaFld;

	public PopulationView(Population p) {
		this.population = p;
		p.addPopulationChangedListener(this);
		initComponent();
	}

	private void initComponent() {
		populationView = this;
		JPanel pheader=new JPanel();
		pheader.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		JPanel jPanel1 = new JPanel();
		pheader.add(jPanel1, BorderLayout.CENTER);
		this.add(pheader, BorderLayout.NORTH);
		BoxLayout jPanel1Layout = new BoxLayout(jPanel1,
				javax.swing.BoxLayout.Y_AXIS);
		jPanel1.setLayout(jPanel1Layout);
		JLabel jLabel1 = new JLabel();
		jPanel1.add(jLabel1);
		jLabel1.setText("Population");
		jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
				"images/population32.png")));

		JLabel jLabel2 = new JLabel();
		jPanel1.add(jLabel2);
		jLabel2.setText("Elements: " + population.getNumberOfSolutions());
		JLabel jLabel3 = new JLabel();
		jPanel1.add(jLabel3);
		int nobj = population.getNumberOfObjectives();
		jLabel3.setText("Number of Objectives: " + nobj);
		if(nobj==2)
		{
			JPanel ptrade=new JPanel();
			ptrade.setLayout(new FlowLayout(FlowLayout.LEFT));
			ptrade.add(new JLabel("Trade off:"));
			betaFld = new NumberTextField("",2);
			betaFld.setDouble(0.5);
			betaFld.setColumns(6);
			ptrade.add(betaFld);
			JButton bt=new JButton("Apply");
			bt.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					double d=betaFld.getDouble();
					if(d>1.0 || d<0.0)
						betaFld.setDouble(beta);
					else{
						beta=d;
						model.fireTableDataChanged();
					}
				}
			});
			ptrade.add(bt);
			pheader.add(ptrade,BorderLayout.EAST);
		}

		jTable1 = new JTable();
		model = new PopulationTableModel();
		jTable1.setModel(model);
		jTable1.setAutoCreateRowSorter(true);
		JScrollPane jScrollPane1 = new JScrollPane(jTable1);

		this.add(jScrollPane1, BorderLayout.CENTER);

		popupMenu = new JPopupMenu();
		int t=this.population.getNumberOfTypes();
		JMenuItem menuItem;
		if(t==0){
			menuItem = new JMenuItem("Export Weights");
			menuItem.addActionListener(new WeightAction());
			popupMenu.add(menuItem);
		}
		else{
			for(int i=0;i<t;i++){
				SolutionType type =population.getType(i);
				switch(type.getEncoding()){
				case NODETYPES:
					menuItem = new JMenuItem("Apply NodeType Configuration");
					menuItem.addActionListener(new NodeTypeAction(type.getStartPosition(),type.getEndPosition()));
					popupMenu.add(menuItem);
					break;
				case WEIGHTS:
				default:
					menuItem = new JMenuItem("Export Weights");
					menuItem.addActionListener(new WeightAction(type.getStartPosition(),type.getEndPosition()));
					popupMenu.add(menuItem);
					break;
				}
			}
			
			
		}
		
		
		
		MouseListener popupListener = new PopupListener();
		jTable1.addMouseListener(popupListener);

		if (population.getNumberOfObjectives() == 2) {
			MOEAPlotterPanel<ILinearRepresentation<Integer>> plotter = new MOEAPlotterPanel<ILinearRepresentation<Integer>>(
					null);
			plotter.setXLabel(population.getObjectiveName()[0]);
			plotter.setYLabel(population.getObjectiveName()[1]);
			try {
				plotter.createPlot();
				plotter.plot(population);
				this.add(plotter, BorderLayout.SOUTH);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private class PopulationTableModel extends
			javax.swing.table.AbstractTableModel {

		String[] columns;

		public PopulationTableModel() {
			int ncols = 0;
			int nobj = population.getNumberOfObjectives();
			if (nobj > 1)
				ncols = nobj + 2;
			else
				ncols = 2;
			columns = new String[ncols];
			columns[0] = "ID";
			columns[1] = "Fitness";
			if(nobj > 1)
			{
			for (int i = 1; i <= nobj; i++) {
				try {
					columns[i] = population.getObjectiveName()[i - 1];
				} catch (Exception e) {
					columns[i] = "Objective Fitness " + i;
				}
			}
			columns[ncols - 1] = "Trade-off";
			}

		}

		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			return population.getNumberOfSolutions();
		}
		
		/**
		 * Distance to minimum possible value (1,1)
		 * 
		 * @param s
		 * @return
		 */
		private double getDistance(ISolution s){
			
			try{
				int nobjective = s.getNumberOfObjectives();
				double sum=0.0;
				for(int i=0;i<nobjective;i++)
					sum+=((s.getFitnessValue(i)-1)*(s.getFitnessValue(i)-1));
				return Math.sqrt(sum);
			}catch(Exception e){return Double.MAX_VALUE;}
		}

		
		private double getTradeOff(IntegerSolution s, double b){
			if(b>1.0 || b<0.0)
				return Double.MAX_VALUE;
			try{
				return (b*s.getFitnessValue(0)+(1-b)*s.getFitnessValue(1));
			}catch(Exception e){return Double.MAX_VALUE;}
		}
		
		@Override
		public Object getValueAt(int row, int col) {

			IntegerSolution s = population.getSolution(row);
			if (col == 0)
				return row;
			else {
				if (population.getNumberOfObjectives() == 1) {
					if (col == 1)
						return s.getFitnessValue(0);
					else
						return null;
				} else {
					if (col != columns.length - 1) {
						try {
							return s.getFitnessValue(col - 1);

						} catch (Exception e) {
							return null;
						}
					} else {
						return getTradeOff(s,beta);
					}

				}
			}
		}

		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return Integer.class;
			default:
				return Double.class;
			}
		}

		@Override
		public String getColumnName(int index) {
			return columns[index];
		}
		
	}

	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			showPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	@Override
	public void populationChanged() {
		model.fireTableDataChanged();
	}
	
	private class WeightAction implements ActionListener {

		int from;
		int to;
		
		public WeightAction(int from,int to){
			this.from=from;
			this.to=to;
		}
		
		public WeightAction(){
			this.from=0;
			this.to = Integer.MAX_VALUE;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int i = jTable1.getSelectedRow();
			int j = jTable1.getRowSorter().convertRowIndexToModel(i);
			int[] s;
			if(to==Integer.MAX_VALUE && from==0)
				s = population.getLinearSolution(j);
			else
				s = Arrays.copyOfRange(population.getLinearSolution(j),from,to);

			System.out.println(Arrays.toString(s));
			JPanel p = new JPanel();
			List<ClipboardItem> l = Core.getInstance().getClipboard()
					.getItemsByClass(ProjectBox.class);
			ProjectBox pb;
			if (l.size() == 1) {
				pb = (ProjectBox) l.get(0).getUserData();
			} else if (l.size() > 1) {
				ClipboardItem[] list = new ClipboardItem[l.size()];
				JComboBox<ClipboardItem> b = new JComboBox<ClipboardItem>(l
						.toArray(list));
				p.add(b);
				JOptionPane.showMessageDialog(populationView, p,
						"Topology Box", JOptionPane.PLAIN_MESSAGE);
				pb = (ProjectBox) l.get(b.getSelectedIndex()).getUserData();
			} else
				return;
			OSPFWeights w = new OSPFWeights(pb.getNetworkTopologyBox()
					.getNetworkTopology().getDimension());
			try {
				w.setWeights(s , pb.getNetworkTopologyBox()
						.getNetworkTopology());
				pb.addOSPFWeights(w);
			} catch (DimensionErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	
	
	private class NodeTypeAction implements ActionListener {

		int from;
		int to;
		
		public NodeTypeAction(int from,int to){
			this.from=from;
			this.to=to;
		}
		
		public NodeTypeAction(){
			this.from = 0;
			this.to   = Integer.MAX_VALUE;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int i = jTable1.getSelectedRow();
			int j = jTable1.getRowSorter().convertRowIndexToModel(i);
			int[] s;
			if(to==Integer.MAX_VALUE && from==0)
				s = population.getLinearSolution(j);
			else
				s = Arrays.copyOfRange(population.getLinearSolution(j),from,to);

			JPanel p = new JPanel();
			List<ClipboardItem> l = Core.getInstance().getClipboard()
					.getItemsByClass(ProjectBox.class);
			ProjectBox pb;
			if (l.size() == 1) {
				pb = (ProjectBox) l.get(0).getUserData();
			} else if (l.size() > 1) {
				ClipboardItem[] list = new ClipboardItem[l.size()];
				JComboBox<ClipboardItem> b = new JComboBox<ClipboardItem>(l
						.toArray(list));
				p.add(b);
				JOptionPane.showMessageDialog(populationView, p,
						"Topology Box", JOptionPane.PLAIN_MESSAGE);
				pb = (ProjectBox) l.get(b.getSelectedIndex()).getUserData();
			} else
				return;
			NodeTypeConfiguration c = new NodeTypeConfiguration(s);
			try {
				pb.getNetworkTopologyBox().getNetworkTopology().applyNodeTypeConfiguration(c);
			} catch (DimensionErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//pb.addNodeTypeConfiguration(c);
			
		}
	}

	

}
