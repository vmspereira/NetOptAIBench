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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.StandardGradientPaintTransformer;

import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul;
import pt.uminho.algoritmi.netopt.ospf.simulation.edgeSelectors.HigherCentralityEdgeSelector;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.algoritmi.netopt.ospf.utils.MathUtils;
import pt.uminho.netopt.aibench.gui.utils.table.RowHeaderRenderer;

@SuppressWarnings("serial")
public class NetworkLoadsView extends JPanel {

	private NetworkLoads loads;
	private String[] columns;
	private int[] totals;
	private int MAX_FROM, MAX_TO,MAXLOAD_FROM,MAXLOAD_TO;
	private Double MAX,MAXLOAD,AVGLOAD;

	public NetworkLoadsView(NetworkLoads loads) {
		this.loads = loads;
		initComponents();
	}

	private void initComponents() {
		
		MAX=0.0;MAX_FROM=0;MAX_TO=0;
		MAXLOAD=0.0;MAXLOAD_FROM=0;MAXLOAD_TO=0;
		double _AVGLOAD=0.0;
		// totals to draw graph
		totals = new int[7];
		totals[0] = totals[1] = totals[2] = 0;
		totals[3] = totals[4] = totals[5] = totals[6]=0;
		for (int i = 0; i < loads.getLoads().length; i++) {
			for (int j = 0; j < loads.getLoads().length; j++) {
				if(loads.getU(i, j)>MAX){
					MAX=loads.getU(i, j);MAX_FROM=i;MAX_TO=j;
				}
				if(loads.getLoads(i, j)>MAXLOAD){
					MAXLOAD=loads.getLoads(i, j);MAXLOAD_FROM=i;MAXLOAD_TO=j;
				}
				if (loads.getU(i, j) > 0.0) {
					_AVGLOAD += loads.getU(i, j);
					if (loads.getU(i, j) < 1.0 / 3.0)
						totals[1]++;
					else if (loads.getU(i, j) < 2.0 / 3.0)
						totals[2]++;
					else if (loads.getU(i, j) < 0.9)
						totals[3]++;
					else if (loads.getU(i, j) < 1.0)
						totals[4]++;
					else if (loads.getU(i, j) < 1.1)
						totals[5]++;
					else
						totals[6]++;
				}
			}
		}
		totals[0]=loads.getTopology().getNumberUpEdges()-totals[1]-totals[2]-totals[3]-totals[4]-totals[5]-totals[6];
		AVGLOAD=_AVGLOAD/loads.getTopology().getNumberUpEdges();
		System.out.println(" "+_AVGLOAD+"/"+loads.getTopology().getNumberUpEdges()+" = "+AVGLOAD);
		
		
		System.out.println(Arrays.toString(totals));
		JScrollPane jScrollPane1;
		JPanel jPanel1;
		JTable jTable1;

		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();

		setLayout(new java.awt.BorderLayout());

		jPanel1 = new JPanel();
		this.add(jPanel1, BorderLayout.NORTH);
		jPanel1.setLayout(new BorderLayout());		
		jPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		JLabel jLabel1 = new JLabel();
		jPanel1.add(jLabel1,BorderLayout.NORTH);
		String ns="Network Loads";
		if(loads.getName()!=null)
			ns=ns+" - "+loads.getName();
		jLabel1.setText(ns);
		String edgeInMorePath=""; 
		if(loads.getTopology()!=null)
		{	
			HigherCentralityEdgeSelector edgselector=new HigherCentralityEdgeSelector(new Simul(loads.getTopology()));
			Iterator<NetEdge> it=edgselector.getIterator();
			try{
				NetEdge edg=it.next();
				edgeInMorePath=""+edg.getEdgeId()+" ("+edg.getFrom()+"->"+edg.getTo()+")";
			}catch(Exception e){
				
			};
			
		}	
		JTextPane tp=new JTextPane();
		tp.setContentType("text/html");
		tp.setEditable(false);
		tp.setOpaque(false);
		
		StringBuilder tps=new StringBuilder();
		tps.append("<html><table>");
		tps.append("<tr><td>Congestion Measure (&Phi;*):</td><td>"+loads.getCongestion()+"</td><td></td><td></td></tr>");
		//tps.append("Routing Protocol:</td><td>"+loads.getLoadBalancer().toString()+"</td>");
		tps.append("<tr><td>Maximum Link Usage (MLU):</td><td>"+MathUtils.roundToMil(MAX*100)+"% (Edge from "+MAX_FROM+" to "+ MAX_TO +")</td></tr>");
		//tps.append("<td>Greatest Direct Load:</td><td>Edge from "+MAXLOAD_FROM+" to "+ MAXLOAD_TO +" ("+MathUtils.roundToMil(MAXLOAD)+")</td></tr>");
		tps.append("<tr><td>Average Link Usage (ALU):</td><td>"+ MathUtils.roundToMil(AVGLOAD*100)+"% </td>");
		tps.append("<td>Edge Centrality (in more Paths):</td><td>"+edgeInMorePath+"</td></tr>");
		tps.append("<table><html>");
		
		tp.setText(tps.toString());
		jPanel1.add(tp);
		
		//System.out.println("MLU="+loads.getMLU()+"   "+MAX);
		
		jScrollPane1.setName("jScrollPane1");

		columns = new String[loads.getLoads().length];
		for (int i = 0; i < loads.getLoads().length; i++) {
			columns[i] = "Node " + i;
		}

		JList<String> rowHeader = new JList<String>(columns);
		rowHeader.setFixedCellWidth(60);
		rowHeader.setFixedCellHeight(jTable1.getRowHeight());
		rowHeader.setBackground(jTable1.getTableHeader().getBackground());
		rowHeader.setCellRenderer(new RowHeaderRenderer(jTable1));
		jScrollPane1.setRowHeaderView(rowHeader);

		jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTable1.setDefaultRenderer(String.class, new LoadCellRenderer());
		jTable1.setModel(new DefaultTableModel() {

			public String getColumnName(int column) {
				return columns[column];
			}

			public int getColumnCount() {
				return columns.length;
			}

			public int getRowCount() {
				return columns.length;
			}

			public Object getValueAt(int row, int column) {
				
				try{
					NetEdge e=loads.getTopology().getNetGraph().getEdge(row, column);
				    if(!e.isUP())
				    	return null;
				}catch(NullPointerException ex){
					return null;
				}
				
				return new LinkLoad(loads.getLoads(row, column), loads.getU(
						row, column));
			}

			public Class<String> getColumnClass(int n) {
				return String.class;
			}

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		jScrollPane1.setViewportView(jTable1);
		add(jScrollPane1, java.awt.BorderLayout.CENTER);

		BarChart chart = new BarChart();
		JPanel chartPanel= chart.createPanel();
		chartPanel.setPreferredSize(new Dimension(600,200));
		add(chartPanel, java.awt.BorderLayout.SOUTH);
	}

	private class LinkLoad {

		double l;
		double u;

		public LinkLoad(double l, double u) {
			this.l = l;
			this.u = u;
		}

		public Double getLoad() {
			return l;
		}

		public Double getU() {
			return u;
		}

		/**
		 * Need to distinguish inexistent edges from edges with no load.
		 */
		public String toString() {
				DecimalFormat df = new DecimalFormat("#0.00");
				String su = df.format(u * 100);
				String color = "";
				if (u == 0.0)
					color = "404040";
				else if (u < (1.0 / 3.0))
					color = "00cd00";
				else if (u < 2.0 / 3.0)
					color = "008b00";
				else if (u < 0.9)
					color = "008b45";
				else if (u < 1)
					color = "23238e";
				else if (u < 1.1)
					color = "8b2252";
				else
					color = "ff0000";
				StringBuffer bf = new StringBuffer();
				bf.append("<html><strong>");
				bf.append("<font color=\"");
				bf.append(color);
				bf.append("\">");
				bf.append(su);
				bf.append("%");
				bf.append("</font>");
				bf.append("</strong></html>");
				return bf.toString();
			}
	}

	class LoadCellRenderer extends DefaultTableCellRenderer
	{

		public LoadCellRenderer() {
			super();
		}

		public void setValue(Object value) {

			this.setHorizontalAlignment(JLabel.RIGHT);
			super.setValue(value);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus,
				int rowIndex, int vColIndex) {

			Component c = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, rowIndex, vColIndex);
			if (c instanceof JLabel && value instanceof LinkLoad) {
				LinkLoad l = (LinkLoad) value;
				((JLabel) c).setToolTipText(l.getLoad().toString());

			}
			return c;
		}

	}

	// graph

	private class BarChart {
		private class CustomBarRenderer extends BarRenderer {

			private Paint colors[];

			public Paint getItemPaint(int i, int j) {
				return colors[j % colors.length];
			}

			public CustomBarRenderer(Paint apaint[]) {
				colors = apaint;
			}
		}

		public BarChart() {

			CategoryDataset categorydataset = createDataset();
			JFreeChart chart = createChart(categorydataset);
			chart.setBackgroundPaint(getBackground());
			chart.getPlot().setBackgroundPaint(Color.BLACK);
		}

		private CategoryDataset createDataset() {
			DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
			defaultcategorydataset.addValue(totals[0], "Network Loads",
					"unused");
			defaultcategorydataset.addValue(totals[1], "Network Loads",
					"[0 , 1/3[");
			defaultcategorydataset.addValue(totals[2], "Network Loads",
					"[1/3 , 2/3[");
			defaultcategorydataset.addValue(totals[3], "Network Loads",
					"[2/3 , 9/10[");
			defaultcategorydataset.addValue(totals[4], "Network Loads",
					"[9/10 , 1 [");
			defaultcategorydataset.addValue(totals[5], "Network Loads",
					"[1, 11/10 [");
			defaultcategorydataset.addValue(totals[6], "Network Loads",
					"More than 11/10");
			return defaultcategorydataset;
		}

		private JFreeChart createChart(CategoryDataset categorydataset) {
			JFreeChart jfreechart = ChartFactory.createBarChart(
					"Network Links Load", null, "Number of Links", categorydataset,
					PlotOrientation.VERTICAL, false, true, false);
			TextTitle texttitle = jfreechart.getTitle();
			//texttitle.setBorder(0.0D, 0.0D, 1.0D, 0.0D);
			// texttitle.setBackgroundPaint(new GradientPaint(0.0F, 0.0F,
			// Color.red, 350F, 0.0F, Color.white, true));
			texttitle.setExpandToFitSpace(true);
			// jfreechart.setBackgroundPaint(new GradientPaint(0.0F, 0.0F,
			// Color.yellow, 350F, 0.0F, Color.white, true));
			CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
			categoryplot.setNoDataMessage("NO DATA!");
			categoryplot.setBackgroundPaint(null);
			categoryplot.setInsets(new RectangleInsets(10D, 5D, 5D, 5D));
			categoryplot.setOutlinePaint(Color.black);
			categoryplot.setRangeGridlinePaint(Color.gray);
			categoryplot.setRangeGridlineStroke(new BasicStroke(1.0F));
			Paint apaint[] = createPaint();
			CustomBarRenderer custombarrenderer = new CustomBarRenderer(apaint);
			custombarrenderer.setDrawBarOutline(true);
			custombarrenderer
					.setGradientPaintTransformer(new StandardGradientPaintTransformer(
							GradientPaintTransformType.CENTER_HORIZONTAL));
			categoryplot.setRenderer(custombarrenderer);
			NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
			numberaxis
					.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			int max = 0;
			for (int i = 0; i < 7; i++) {
				if (totals[i] > max)
					max = totals[i];
			}
			numberaxis.setRange(0.0D, max + 2);
			numberaxis.setTickMarkPaint(Color.black);
			return jfreechart;
		}

		private Paint[] createPaint() {
			Paint apaint[] = new Paint[6];
			apaint[0] = new GradientPaint(0.0F, 0.0F, Color.white, 0.0F, 0.0F,
					new Color(64,64,64));
			apaint[1] = new GradientPaint(0.0F, 0.0F, Color.white, 0.0F, 0.0F,
					new Color(0,139,0));
			apaint[2] = new GradientPaint(0.0F, 0.0F, Color.white, 0.0F, 0.0F,
					new Color(0,139,69));
			apaint[3] = new GradientPaint(0.0F, 0.0F, Color.white, 0.0F, 0.0F,
					new Color(35,35,142));
			apaint[4] = new GradientPaint(0.0F, 0.0F, Color.white, 0.0F, 0.0F,
					new Color(139,34,82));
			apaint[5] = new GradientPaint(0.0F, 0.0F, Color.white, 0.0F, 0.0F,
					new Color(255,0,0));
			return apaint;
		}

		public JPanel createPanel() {
			JFreeChart jfreechart = createChart(createDataset());
			JPanel p= new ChartPanel(jfreechart);
			return p;
		}

	}

}
