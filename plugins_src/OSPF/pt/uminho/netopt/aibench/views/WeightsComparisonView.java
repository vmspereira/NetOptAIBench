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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import pt.uminho.algoritmi.netopt.ospf.utils.MathUtils;
import pt.uminho.netopt.aibench.datatypes.WeightsComparison;
import pt.uminho.netopt.aibench.gui.utils.table.RowHeaderRenderer;

@SuppressWarnings("serial")
public class WeightsComparisonView extends JPanel {

	WeightsComparison comp;
	private JPanel Header;
	private String[] columns;

	/** Creates new form NewJPanel */
	public WeightsComparisonView(WeightsComparison c) {
		this.comp = c;
		initComponents();
	}

	private void initComponents() {

		setName("Form");
		setLayout(new java.awt.BorderLayout());
		
		Header = new JPanel();
		BoxLayout jPanel1Layout = new BoxLayout(Header,
				javax.swing.BoxLayout.Y_AXIS);
		this.add(Header, BorderLayout.NORTH);
		Header.setLayout(jPanel1Layout);
		Header.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	
		JLabel jLabel1 = new JLabel();
		Header.add(jLabel1);
		jLabel1.setText("OSPF Weights Comparison");
		jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
				"images/resultOpt32.png")));
			
		JLabel jLabel3 = new JLabel();
		Header.add(jLabel3);
		jLabel3.setText("Shortest Path edge change average:"+comp.getComparator().getAverage());
				
				
		JLabel jLabel2 = new JLabel();
		Header.add(jLabel2);
		jLabel2.setText("Weigths Linear correlation: " + this.comp.getCorelation());
		
		JLabel jLabel4 = new JLabel();
		Header.add(jLabel4);
		jLabel2.setText("Number of Changed Weigths: " + this.comp.getNumberOfChangedWeights());

		
		
		 XYSeries xyseries1 = new XYSeries("Weights 1");
		 XYSeries xyseries2 = new XYSeries("Weights 2");
		 int counter=1;
		 for (int i = 0; i < comp.getSerie1().length; i++) {
			 if(comp.getSerie1()[i]!=0 && comp.getSerie2()[i]!=0 ){
				 xyseries1.add(counter, comp.getSerie1()[i]);
				 xyseries2.add(counter, comp.getSerie2()[i]);
				 counter++;
			 }
		 }

		 
	    XYSeriesCollection xyseriescollection = new XYSeriesCollection();
	    xyseriescollection.addSeries(xyseries1);
	    xyseriescollection.addSeries(xyseries2);
	    
	    JFreeChart jfreechart = ChartFactory.createXYLineChart("Weigths Difference", "Edge", "Weight", xyseriescollection, PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        java.awt.geom.Ellipse2D.Double double1 = new java.awt.geom.Ellipse2D.Double(-4D, -4D, 8D, 8D);
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)xyplot.getRenderer();
        
        xylineandshaperenderer.setBaseShapesVisible(true);
        
        xylineandshaperenderer.setSeriesShape(0, double1);
        xylineandshaperenderer.setSeriesPaint(0, Color.red);
        xylineandshaperenderer.setSeriesFillPaint(0, Color.red);
        xylineandshaperenderer.setSeriesOutlinePaint(0, Color.black);
        
        xylineandshaperenderer.setSeriesShape(1, double1);
        xylineandshaperenderer.setSeriesPaint(1, Color.BLUE);
        xylineandshaperenderer.setSeriesFillPaint(1, Color.BLUE);
        xylineandshaperenderer.setSeriesOutlinePaint(1, Color.BLACK);
        xyplot.setRenderer(0,xylineandshaperenderer);
        
        int dimension=comp.getDimension();
        columns = new String[dimension];
		for (int i = 0; i < dimension; i++) {
			columns[i] = "Node " + i;
		}
		
		
        
        JTable table1=new JTable();
        table1.setModel(new DefaultTableModel(){
        	
        	public String getColumnName(int column){
				return columns[column];
			}
			
			public int getColumnCount(){return columns.length;}
			public int getRowCount(){return columns.length;}
			
			public Object getValueAt(int row, int column)
			{
				return comp.getComparator().getComparison(row, column);
			}
			
			public Class getColumnClass(int n) {
				return Double.class;
			}
        });
        
        JScrollPane jScrollPane1=new JScrollPane(table1);
        JList rowHeader = new JList(columns);    
	    rowHeader.setFixedCellWidth(60);
	    rowHeader.setFixedCellHeight(table1.getRowHeight());
	    rowHeader.setBackground(table1.getTableHeader().getBackground());
	    rowHeader.setCellRenderer(new RowHeaderRenderer(table1));
	    jScrollPane1.setRowHeaderView(rowHeader);
	    table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    table1.setDefaultRenderer(Double.class, new WeightsCellRenderer());
        
		ChartPanel mypan = new ChartPanel(jfreechart);

		this.add(mypan, BorderLayout.CENTER);
		this.add(jScrollPane1, BorderLayout.SOUTH);

	}// </editor-fold>

	
	
	/*
	 * CellRender
	 */
	public class WeightsCellRenderer extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected Color BACKGROUND_BLACK = new Color(185,185,185);
		protected Color BACKGROUND_GREEN = new Color(155,250,155);
		protected Color BACKGROUND_RED = new Color(250,155,155);
		
		public WeightsCellRenderer() {
			super();
		}


		public void setValue(Object value) {
			if (value instanceof Double && value!=null) {
				Double d=(Double)value;
				if (d==0){
					setBackground(this.BACKGROUND_RED);
				}else if (d==1){
					setBackground(this.BACKGROUND_GREEN);
				}else{
					setBackground(Color.WHITE);
				}
				this.setHorizontalAlignment(JLabel.CENTER);
				this.setText(MathUtils.doubleToString(d,2));
				
			} else{
				setBackground(this.BACKGROUND_BLACK);
				setText("");
			}
		}
	}

	
	
	
	
	
	
	
}
