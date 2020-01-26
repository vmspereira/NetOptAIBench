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
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;



import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.netopt.aibench.datatypes.PopulationsComparison;
import pt.uminho.netopt.aibench.gui.MOEAPlotterPanel;

@SuppressWarnings("serial")
public class PopulationsComparisonView extends JPanel {

	PopulationsComparison comp;

	/** Creates new form NewJPanel */
	public PopulationsComparisonView(PopulationsComparison c) {
		this.comp = c;
		initComponents();
	}

	private void initComponents() {

		this.setLayout(new BorderLayout());
		JPanel jPanel1 = new JPanel();
		this.add(jPanel1, BorderLayout.NORTH);
		BoxLayout jPanel1Layout = new BoxLayout(jPanel1,
				javax.swing.BoxLayout.Y_AXIS);
		jPanel1.setLayout(jPanel1Layout);
		JLabel jLabel1 = new JLabel();
		jPanel1.add(jLabel1);
		jLabel1.setText("Populations Comparison");
		// jLabel1.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource(
		// "images/ospfW32.png")));

		JLabel jLabel2 = new JLabel();
		jPanel1.add(jLabel2);
		jLabel2.setText("Elements: "
				+ comp.getFirstPopulation().getNumberOfSolutions()
				+ "(1st Pop.) "
				+ comp.getSecondPopulation().getNumberOfSolutions()
				+ "(2nd Pop.)");
		JLabel jLabel3 = new JLabel();
		jPanel1.add(jLabel3);
		int nobj = comp.getFirstPopulation().getNumberOfObjectives();
		jLabel3.setText("Number of Objectives: " + nobj);

		JTable table = new JTable(new ComparisonModel());
		JScrollPane js=new JScrollPane(table);
		this.add(js, BorderLayout.CENTER);

		if (comp.getFirstPopulation().getNumberOfObjectives() == 2) {
			MOEAPlotterPanel<ILinearRepresentation<Integer>> plotter = new MOEAPlotterPanel<ILinearRepresentation<Integer>>(
					null);
			plotter.setXLabel(comp.getFirstPopulation().getObjectiveName()[0]);
			plotter.setYLabel(comp.getFirstPopulation().getObjectiveName()[1]);
			ArrayList<Population> list = new ArrayList<Population>();
			list.add(comp.getFirstPopulation());
			list.add(comp.getSecondPopulation());
			try {
				plotter.createPlot();
				plotter.plot(list);
				this.add(plotter, BorderLayout.SOUTH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class ComparisonModel extends AbstractTableModel {

		private String[] measures = { "Epsilon", "Generalized Spread",
				"Generational Distance", "HyperVolume",
				"Inverted Generational Distance", "Spread", "CMeasure" };

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public int getRowCount() {
			return 7;
		}

		@Override
		public Object getValueAt(int linha, int coluna) {

			if (coluna == 0 && linha >= 0 && linha <= 6)
				return measures[linha];
			else if (coluna == 1 || coluna==2) {
				
				switch (linha) {
				case 0:
					return comp.getEpsilon(coluna-1);
				case 1:
					return comp.getGeneralizedSpread(coluna-1);
				case 2:
					return comp.getGenerationalDistance(coluna-1);
				case 3:
					return comp.getHyperVolume(coluna-1);
				case 4:
					return comp.getInvertedGenerationalDistance(coluna-1);
				case 5:
					return comp.getSpread(coluna-1);
				case 6:
					return comp.getCMeasure(coluna-1);
				default:
					return null;
				}
			}
			return null;
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex){
			if(columnIndex==1 || columnIndex==2)
				return Double.class;
			else return String.class;
		}

		@Override
		public String getColumnName(int columnIndex){
			if(columnIndex==0)
				return "Measure";
			else if(columnIndex==1)
				return "(P1,P2)";
			else if(columnIndex==2)
				return "(P2,P1)";
			else return "";
		}
	}
	
	

}
