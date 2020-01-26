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
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import pt.uminho.algoritmi.netopt.SystemConf;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode.NodeType;
import pt.uminho.netopt.aibench.datatypes.NetworkTopologyBox;
import pt.uminho.netopt.jung.JungGraph;
import pt.uminho.netopt.jung.MouseMenu;
import pt.uminho.netopt.jung.NetOptEditingModalGraphMouse;
import pt.uminho.netopt.jung.PopupVertexEdgeMenuMousePlugin;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class TopologyGraph extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static NetworkTopology topology;
	private static VisualizationViewer<NetNode, NetEdge> vv;
	private GraphType type;
	private NetOptEditingModalGraphMouse<NetNode, NetEdge> gm;
	private JungGraph graph;
	// protected static LocationGraphMousePlugin locationPlugin;
	// private static TopologyGraph topologyGraph;

	public enum GraphType {
		BW, LENGTH, DELAY, ID;

		public static GraphType defaultType() {
			return ID;
		}
	}

	public NetworkTopology getNetworkTopology() {
		return topology;
	}

	public TopologyGraph(NetworkTopologyBox top) {
		this.topology = top.getNetworkTopology();
		type = GraphType.defaultType();
		initGUI();
		// topologyGraph=this;
	}

	protected void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);

			this.setPreferredSize(new java.awt.Dimension(900, 900));
			graph = new JungGraph(topology);
			graph.populateGraph();
			Layout<NetNode, NetEdge> layout = new StaticLayout<NetNode, NetEdge>(graph.getGraph(),
					graph.getLocationTransformer());

			layout.setSize(new Dimension(800, 800));
			vv = new VisualizationViewer<NetNode, NetEdge>(layout);

			vv.setBackground(Color.WHITE);

			// Show vertex and edge labels
			vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<NetNode>());
			vv.setVertexToolTipTransformer(new Transformer<NetNode, String>() {
		        public String transform(NetNode v) {
		        	return v.getLabel();
		            }
		        });
			vv.getRenderContext().setEdgeLabelTransformer(new EdgeStringRenderer());
			vv.getRenderContext().setEdgeDrawPaintTransformer(new EdgePaintFunction());
			vv.getRenderContext().setEdgeStrokeTransformer(new EdgeStrokeFunction());

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

			vv.getRenderContext().setVertexFillPaintTransformer(new VertexPaintFunction());

			vv.setPreferredSize(new Dimension(850, 850));

			NodeFactory nf = NodeFactory.getInstance();
			EdgeFactory ef = EdgeFactory.getInstance();

			gm = new NetOptEditingModalGraphMouse<NetNode, NetEdge>(vv.getRenderContext(), nf, ef);
			gm.setMode(Mode.TRANSFORMING);
			gm.setEditingPlugin(new NetOptEditingGraphMousePlugin<NetNode, NetEdge>(nf, ef));

			vv.addKeyListener(gm.getModeKeyListener());
			// locationPlugin =new LocationGraphMousePlugin();
			// gm.add(locationPlugin);

			PopupVertexEdgeMenuMousePlugin<NetNode, NetEdge> myPlugin = new PopupVertexEdgeMenuMousePlugin<NetNode, NetEdge>(vv);

			// Add some popup menus for the edges and vertices to our mouse
			MouseMenu menu= new MouseMenu(topology,this);
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

	public GraphType getType() {
		return type;
	}

	public void setType(GraphType type) {
		this.type = type;
	}

	public class EdgeStringRenderer implements Transformer<NetEdge, String> {

		public String transform(NetEdge i) {
			DecimalFormat df = new DecimalFormat("#.##");
			switch (getType()) {
			case BW:
				return "" + df.format(i.getBandwidth()) + " Mbit/s";
			case DELAY:
				return "" + df.format(i.getDelay()) + " ms";
			case LENGTH:
				return "" + df.format(i.getLength());
			default:
				return "" + i.getEdgeId();
			}
		}
	}

	public class EdgePaintFunction implements Transformer<NetEdge, Paint> {

		public Paint transform(NetEdge e) {
			if (e.isUP()) {
				return Color.DARK_GRAY;
			} else
				return Color.RED;
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

	/*
	 * Edge Stroke
	 */

	public class EdgeStrokeFunction implements Transformer<NetEdge, Stroke> {
		protected final Stroke THIN = new BasicStroke(1);
		protected final Stroke THICK = new BasicStroke(2);

		public Stroke transform(NetEdge e) {
			return THIN;
		}

	}

	private synchronized void redraw() {
		// graph = new JungGraph(topology);
		// graph.populateGraph();
		// vv.getGraphLayout().setGraph(graph.getGraph());
		vv.repaint();
	}

	private JToolBar buildToolBar() {

		JPanel toolbar = new JPanel();
		Border raisedBorder = BorderFactory.createRaisedBevelBorder();
		toolbar.setBorder(raisedBorder);

		JPanel optionp = new JPanel();
		optionp.setLayout(new BoxLayout(optionp, BoxLayout.LINE_AXIS));
		optionp.setOpaque(false);
		optionp.setBorder(BorderFactory.createTitledBorder("Edge Label"));

		Action action1 = new AbstractAction("Length") {
			public void actionPerformed(ActionEvent evt) {
				setType(GraphType.LENGTH);
				// createPath();
				vv.repaint();
			}
		};

		Action action2 = new AbstractAction("BandWidth") {
			public void actionPerformed(ActionEvent evt) {
				setType(GraphType.BW);
				// createPath();
				vv.repaint();
			}
		};

		Action action3 = new AbstractAction("Delay") {
			public void actionPerformed(ActionEvent evt) {
				setType(GraphType.DELAY);
				// createPath();
				vv.repaint();
			}
		};
		Action action4 = new AbstractAction("Id") {
			public void actionPerformed(ActionEvent evt) {
				setType(GraphType.ID);
				// createPath();
				vv.repaint();
			}
		};

		JRadioButton b1 = new JRadioButton(action1);
		b1.setOpaque(false);
		b1.setSelected(false);
		JRadioButton b2 = new JRadioButton(action2);
		b2.setOpaque(false);
		JRadioButton b3 = new JRadioButton(action3);
		b3.setOpaque(false);
		JRadioButton b4 = new JRadioButton(action4);
		b4.setOpaque(false);
		b4.setSelected(true);

		ButtonGroup group = new ButtonGroup();
		group.add(b1);
		group.add(b2);
		group.add(b3);
		group.add(b4);

		optionp.add(b1);
		optionp.add(b2);
		optionp.add(b3);
		optionp.add(b4);
		toolbar.add(optionp);
		toolbar.add(new JSeparator());

		final ScalingControl scaler = new CrossoverScalingControl();
		JPanel controls = new JPanel();
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

		/*
		 * JButton redraw = new JButton("Redraw"); redraw.addActionListener(new
		 * ActionListener() { public void actionPerformed(ActionEvent e) {
		 * redraw(); } });
		 */
		zoomControls.add(plus);
		zoomControls.add(minus);
		// zoomControls.add(redraw);
		controls.add(zoomControls);

		/*
		JPanel modeControls = new JPanel();
		modeControls.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));

		modeControls.add(gm.getModeComboBox());
		controls.add(modeControls);
		*/
		
		toolbar.add(controls);
		toolbar.add(new JSeparator());
	
		JToolBar toolb = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
		toolb.add(toolbar);
		toolb.setFloatable(false);
		return toolb;
	}

	public static class NodeFactory implements Factory<NetNode> {

		private static NodeFactory instance = new NodeFactory();

		public static void setNodeCount(int aNodeCount) {
		}

		private NodeFactory() {
		}

		public static NodeFactory getInstance() {
			return instance;
		}

		public NetNode create() {
			NetNode node = new NetNode();
			// topology.fireTopologyChanged(new
			// TopologyEvent(topologyGraph,TopologyEvent.ADD_NODE,node));
			return node;
		}

	}

	// Singleton factory for creating Edges...
	public static class EdgeFactory implements Factory<NetEdge> {

		// TODO: defaults may be set on configuration file
		private static double defaultBW;
		private static double defaultDelay;
		private static double defaultLength;

		private static EdgeFactory instance = new EdgeFactory();

		private EdgeFactory() {

			defaultBW = SystemConf.getPropertyDouble("topology.defaultBW", 1000.0);
			defaultDelay = SystemConf.getPropertyDouble("topology.defaultDelay", 1.0);
			defaultLength = SystemConf.getPropertyDouble("topology.defaultLenght", 100.0);
		}

		public static EdgeFactory getInstance() {
			return instance;
		}

		public NetEdge create() {
			NetEdge edge = new NetEdge();
			edge.setEdgeId(topology.getNetGraph().greatestEdgeID() + 1);
			edge.setBandwidth(defaultBW);
			edge.setDelay(defaultDelay);
			edge.setLength(defaultLength);
			// topology.fireTopologyChanged(new
			// TopologyEvent(topologyGraph,TopologyEvent.ADD_EDGE,edge));
			return edge;
		}

		/**
		 * @return the defaultWeight
		 */
		public static double getDefaultWeight() {
			return defaultLength;
		}

		/**
		 * @param aDefaultWeight
		 *            the defaultWeight to set
		 */
		public static void setDefaultWeight(double aDefaultWeight) {
			defaultLength = aDefaultWeight;
		}

		public static double getDefaultDelay() {
			return defaultDelay;
		}

		public static void setDefaultDelay(double aDefaultDelay) {
			defaultDelay = aDefaultDelay;
		}

		public static double getDefaultBW() {
			return defaultBW;
		}

		public static void setDefaultBW(double aDefaultBW) {
			defaultBW = aDefaultBW;
		}

	}

	private class NetOptEditingGraphMousePlugin<V, E> extends EditingGraphMousePlugin<V, E> {

		boolean newEdge = false;

		public NetOptEditingGraphMousePlugin(Factory<V> vertexFactory, Factory<E> edgeFactory) {
			super(vertexFactory, edgeFactory);
		}

		/**
		 * If the mouse is pressed in an empty area, create a new vertex there.
		 * If the mouse is pressed on an existing vertex, prepare to create an
		 * edge from that vertex to another
		 */
		@SuppressWarnings("unchecked")
		public void mousePressed(MouseEvent e) {
			final VisualizationViewer<V, E> vvv = (VisualizationViewer<V, E>) e.getSource();
			Point2D p = e.getPoint();

			GraphElementAccessor<V, E> pickSupport = vvv.getPickSupport();
			final V vertex = pickSupport.getVertex(vvv.getModel().getGraphLayout(), p.getX(), p.getY());

			if (pickSupport != null && vertex == null) // newedge
				newEdge = true;
			super.mousePressed(e);
			if (newEdge) {
				Point2D point = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint());
				newEdge = false;
				// NetNode
				// node=topology.getBrite().getNodeAt(topology.getBrite().getNNodes()-1);
				// node.setXpos(point.getX());
				// node.setYpos(point.getY());
			}
		}
	}

}
