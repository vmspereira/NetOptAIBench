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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import pt.uminho.algoritmi.netopt.SystemConf;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.PDEFTSimul;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode.NodeType;
import pt.uminho.algoritmi.netopt.ospf.utils.MathUtils;
import pt.uminho.netopt.aibench.datatypes.WeightsGraph;
import pt.uminho.netopt.jung.*;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.Clipboard;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;

public class WeightsGraphPathView extends javax.swing.JPanel implements MouseMenuListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VisualizationViewer<NetNode, NetEdge> vv;
	private NetOptEditingModalGraphMouse<NetNode, NetEdge> gm;
	private ComboBoxModel<NetNode> from;
	private ComboBoxModel<NetNode> to;
	private WeightsGraph wg;
	// current load
	double[][] loads;
	private LoadBalancer lb;
	private double THRESHOLD = 0.01;
	private PValues pvalues;
	WeightsGraphPathView view;

	
	
	
	public WeightsGraphPathView(WeightsGraph wg) {
		this.wg = wg;
		loads = new double[wg.getTopology().getDimension()][wg.getTopology().getDimension()];
		for (int i = 0; i < loads.length; i++)
			for (int j = 0; j < loads.length; j++)
				loads[i][j] = 0.0;
		this.lb = LoadBalancer.ECMP;
		if(SystemConf.getPropertyBoolean("deft.applythreshold", true))
			THRESHOLD = SystemConf.getPropertyDouble("deft.threshold", 0.01);
		pvalues = new PValues(wg.getTopology().getDimension());
		double p=SystemConf.getPropertyDouble("pvalue.default", 1.0);
		pvalues.setValue(p);
		view =this;
		initGUI();
	}

	
	
		
	protected void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);

			this.setPreferredSize(new java.awt.Dimension(900, 900));
			JungGraph graph = new JungGraph(wg.getTopology().getNetGraph());
			graph.populateGraph();
			Layout<NetNode, NetEdge> layout = new StaticLayout<NetNode, NetEdge>(graph.getGraph(),
					graph.getLocationTransformer());
			layout.setSize(new Dimension(800, 800)); // sets the initial
			vv = new VisualizationViewer<NetNode, NetEdge>(layout);

			vv.setBackground(Color.WHITE);

			// Show vertex and edge labels
			vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<NetNode>());
			vv.getRenderContext().setEdgeLabelTransformer(new EdgeStringRenderer());
			vv.getRenderContext().setEdgeFontTransformer(new ConstantTransformer(new Font("Monospaced", Font.BOLD, 14)));
			vv.getRenderContext().setEdgeDrawPaintTransformer(new EdgePaintFunction());
			vv.getRenderContext().setEdgeStrokeTransformer(new EdgeStrokeFunction());
			vv.getRenderContext().setVertexFillPaintTransformer(new VertexPaintFunction());
			
			@SuppressWarnings("serial")
			DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(Color.white) {
				@Override
				public <V> Component getVertexLabelRendererComponent(JComponent vv, Object value, Font font,
						boolean isSelected, V vertex) {
					super.getVertexLabelRendererComponent(vv, value, font, isSelected, vertex);
					setForeground(Color.white);
					return this;
				}
			};
			vv.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
			vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
			vv.setPreferredSize(new Dimension(850, 850));

			gm = new NetOptEditingModalGraphMouse<NetNode, NetEdge>(vv.getRenderContext(), null, null);
			gm.setMode(Mode.TRANSFORMING);
			//gm.setEditingPlugin(new NetOptEditingGraphMousePlugin<NetNode, NetEdge>(null,null));
			
			
			
			PopupVertexEdgeMenuMousePlugin<NetNode, NetEdge> myPlugin = new PopupVertexEdgeMenuMousePlugin<NetNode, NetEdge>(vv);

			// Add some popup menus for the edges and vertices to our mouse
			MouseMenu menu= new MouseMenu(wg.getTopology(),this);
			menu.addMouseMenuListener(this);
			JPopupMenu edgeMenu = menu.getEdgeMenu();
			myPlugin.setEdgePopup(edgeMenu);
			JPopupMenu vertexMenu = menu.getVertexMenu();
			myPlugin.setVertexPopup(vertexMenu);

			gm.remove(gm.getPopupEditingPlugin());

			gm.add(myPlugin); // Add our new plugin to the mouse
			vv.setGraphMouse(gm);

			
			this.add(vv, BorderLayout.CENTER);
			this.add(buildToolBar(), BorderLayout.SOUTH);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * print ospf weights??
	 */
	public class EdgeStringRenderer implements Transformer<NetEdge, String> {

		public String transform(NetEdge i) {
			//if (path1 != null && getPathType(i) == EdgeType.FIRST) {
				double d = loads[i.getFrom()][i.getTo()] +loads[i.getTo()][i.getFrom()];
				if(d>= THRESHOLD)
					return "" + MathUtils.doubleToString((d), 2) + "%";
				else
					return "";
		}
	}

	public class EdgePaintFunction implements Transformer<NetEdge, Paint> {

		public Paint transform(NetEdge e) {
			double d = loads[e.getFrom()][e.getTo()] +loads[e.getTo()][e.getFrom()];
			if(d>= THRESHOLD)
				return Color.RED;	
			else
				return Color.LIGHT_GRAY;
		}
	}

	public class EdgeStrokeFunction implements Transformer<NetEdge, Stroke> {
		protected final Stroke THIN = new BasicStroke(1);
		protected final Stroke THICK = new BasicStroke(2);
		protected final Stroke DASHED= new BasicStroke(1,                      // Width
                BasicStroke.CAP_SQUARE,    // End cap
                BasicStroke.JOIN_MITER,    // Join style
                10.0f,                     // Miter limit
                new float[] {16.0f,20.0f}, // Dash pattern
                0.0f);                     // Dash phase

		public Stroke transform(NetEdge e) {
			double d = loads[e.getFrom()][e.getTo()] +loads[e.getTo()][e.getFrom()];
			
			if(d>= THRESHOLD)
				return THICK;
			else if(!e.isUP())
				return DASHED;
			else
				return THIN;		 
		}

	}
	
	
	public class VertexPaintFunction implements Transformer<NetNode, Paint> {

		public Paint transform(NetNode n) {
			if (n.getNodeType().equals(NodeType.LEGACY)) {
				return Color.black;
			} else
				return Color.RED;
		}
	}




	
	private void updatePath() {
		switch (this.lb) {
		case DEFT:
			updateDEFTPath();
			break;
		case PEFT:
			updatePEFTPath();
			break;
		case NOLB:
			updateSSPath();
			break;	
		default:
			updateECMPPath();
			break;
		}

	}

	private void updateECMPPath() {
		NetNode bfrom = (NetNode) from.getSelectedItem();
		NetNode bto = (NetNode) to.getSelectedItem();
		LoadGraph lg = new LoadGraph(this.wg.getTopology().getDimension());
		this.loads = lg.distributeDemandECMP(bfrom.getNodeId(), bto.getNodeId(), 100.0);
	}
	
	
	private void updateSSPath() {
		NetNode bfrom = (NetNode) from.getSelectedItem();
		NetNode bto = (NetNode) to.getSelectedItem();
		LoadGraph lg = new LoadGraph(this.wg.getTopology().getDimension());
		this.loads = lg.distributeDemandSSP(bfrom.getNodeId(), bto.getNodeId(), 100.0);
	}

	private void updateDEFTPath() {
		NetNode bfrom = (NetNode) from.getSelectedItem();
		NetNode bto = (NetNode) to.getSelectedItem();
		LoadGraph lg = new LoadGraph(this.wg.getTopology().getDimension());
		this.loads = lg.distributeDemandDEFT(bfrom.getNodeId(), bto.getNodeId(), 100.0);
		
	}

	
	private void updatePEFTPath() {
		NetNode bfrom = (NetNode) from.getSelectedItem();
		NetNode bto = (NetNode) to.getSelectedItem();
		LoadGraph lg = new LoadGraph(this.wg.getTopology().getDimension());
		this.loads = lg.distributeDemandPEFT(bfrom.getNodeId(), bto.getNodeId(), 100.0);
	}
	
	
	
	private JToolBar buildToolBar() {

		JPanel toolbar = new JPanel();
		Border raisedBorder = BorderFactory.createRaisedBevelBorder();
		toolbar.setBorder(raisedBorder);

		JPanel lbControls = new JPanel();
		lbControls.setBorder(BorderFactory.createTitledBorder("Load Balancing"));
		JComboBox<LoadBalancer> lbCombo = new JComboBox<LoadBalancer>(LoadBalancer.values());
		lbCombo.setPreferredSize(new Dimension(100, 20));
		lbCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lb = (LoadBalancer) lbCombo.getSelectedItem();
				updatePath();
				vv.repaint();
			}
		});
		lbControls.add(lbCombo);

		final ScalingControl scaler = new CrossoverScalingControl();

		JPanel zoomControls = new JPanel();
		zoomControls.setBorder(BorderFactory.createTitledBorder("Zoom"));
		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1 / 1.1f, vv.getCenter());
			}
		});
		zoomControls.add(plus);
		zoomControls.add(minus);

		JPanel pathControls = new JPanel();
		pathControls.setBorder(BorderFactory.createTitledBorder("Path"));

		from = new DefaultComboBoxModel<NetNode>(this.wg.getTopology().getNetGraph().getNodes());
		JComboBox<NetNode> cfrom = new JComboBox<NetNode>(from);
		cfrom.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				updatePath();
				vv.repaint();
			}
		});

		to = new DefaultComboBoxModel<NetNode>(this.wg.getTopology().getNetGraph().getNodes());
		JComboBox<NetNode> cto = new JComboBox<NetNode>(to);
		cto.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				updatePath();
				vv.repaint();
			}
		});
		pathControls.add(new JLabel("From:"));
		pathControls.add(cfrom);
		pathControls.add(new JLabel(" to:"));
		pathControls.add(cto);

		JPanel nodepControls = new JPanel();
		nodepControls.setBorder(BorderFactory.createTitledBorder("Node-p Values"));

		JButton jnodep=new JButton("Load configuration");
		jnodep.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel p = new JPanel();
				Clipboard clipboard = Core.getInstance().getClipboard();
				List<ClipboardItem> pvalueslist = clipboard.getItemsByClass(PValues.class);

				JComboBox<ClipboardItem> pValuesComboBox = new JComboBox<ClipboardItem>();
				pValuesComboBox.setModel(new DefaultComboBoxModel(pvalueslist.toArray()));

				p.add(new JLabel("Node-p Configuration"));
				p.add(pValuesComboBox);

				int n = JOptionPane.showOptionDialog(view, p, "Input", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, // do not use a
							  						// custom Icon
						null, // the titles of buttons
						null); // default button title

				if (n == JOptionPane.OK_OPTION) {
					ClipboardItem item1 = (ClipboardItem) (pValuesComboBox.getSelectedItem());
					if (item1 != null) {
						pvalues = (PValues) (item1.getUserData());
						updatePath();
						vv.repaint();
					}
				}

			}

			
		});
		nodepControls.add(jnodep);
		
		toolbar.add(lbControls);
		toolbar.add(zoomControls);
		toolbar.add(pathControls);
		toolbar.add(nodepControls);
		
		JToolBar toolb = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
		toolb.add(toolbar);

		toolb.setFloatable(false);
		return toolb;
	}

	
	
	
	private class LoadGraph {

		// load de links
		double[][] graphLoads;
		public LoadGraph(int d) {
			graphLoads = new double[d][d];
			for (int i = 0; i < d; i++)
				for (int j = 0; j < d; j++)
					graphLoads[i][j] = 0.0;
		}
	
		
		public double[][] distributeDemandSSP(int source, int dest, double demand) {
			Simul simul=new Simul(wg.getTopology());
			simul.setLoadBalancer(LoadBalancer.NOLB);
			double[][] d = new double[wg.getTopology().getDimension()][wg.getTopology().getDimension()];
			d[source][dest]=demand;
			double[][] result=simul.partialLoads(dest,new Demands(d));
			return result;
		}


		/**
		 * Distribute traffic between src and dst using SP EMCP load balancing
		 * routing
		 * 
		 */
		public double[][] distributeDemandECMP(int source, int dest, double demand) {
			Simul simul=new Simul(wg.getTopology());
			double[][] d = new double[wg.getTopology().getDimension()][wg.getTopology().getDimension()];
			d[source][dest]=demand;
			double[][] result=simul.partialLoads(dest,new Demands(d));
			return result;
		}

		
		
		
		
		public double[][] distributeDemandDEFT(int source, int dest, double demand) {

			PDEFTSimul simul=new PDEFTSimul(wg.getTopology(),true);
			double[][] d = new double[wg.getTopology().getDimension()][wg.getTopology().getDimension()];
			d[source][dest]=demand;
			double[][] result=simul.partialLoads(dest,new Demands(d),pvalues.getPValues());
			return result;
		}

		
		public double[][] distributeDemandPEFT(int source, int dest, double demand) {

			PDEFTSimul simul=new PDEFTSimul(wg.getTopology(),false);
			double[][] d = new double[wg.getTopology().getDimension()][wg.getTopology().getDimension()];
			d[source][dest]=demand;
			double[][] result=simul.partialLoads(dest,new Demands(d),pvalues.getPValues());
			return result;
		}
		
		
		
	}





	@Override
	public void mouseMenuActionPerformed() {
		updatePath();
		vv.repaint();
	}

}
