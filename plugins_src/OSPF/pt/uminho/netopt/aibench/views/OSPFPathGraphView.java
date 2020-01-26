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
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;


import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JToolBar;
import javax.swing.border.Border;

import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetPath;
import pt.uminho.netopt.aibench.datatypes.WeightsComparison;
import pt.uminho.netopt.jung.JungGraph;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class OSPFPathGraphView extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WeightsComparison wc;
	private VisualizationViewer<NetNode, NetEdge> vv;
	private DefaultModalGraphMouse gm;
	private ComboBoxModel from;
	private ComboBoxModel to;

	private NetPath path1;
	private NetPath path2;

	public enum EdgeType {
		NONE, FIRST, SECOND, BOTH;

		public static EdgeType defaultType() {
			return NONE;
		}
	}

	public OSPFPathGraphView(WeightsComparison wc) {
		this.wc = wc;
		initGUI();
	}

	protected void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);

			this.setPreferredSize(new java.awt.Dimension(900, 900));
			JungGraph graph = new JungGraph(wc.getBriteGraph());
			graph.populateGraph();
			Layout<NetNode, NetEdge> layout = new StaticLayout<NetNode, NetEdge>(
					graph.getGraph(), graph.getLocationTransformer());
			layout.setSize(new Dimension(800, 800)); // sets the initial
			vv = new VisualizationViewer<NetNode, NetEdge>(layout);

			vv.setBackground(Color.WHITE);

			// Show vertex and edge labels
			vv.getRenderContext().setVertexLabelTransformer(
					new ToStringLabeller<NetNode>());
			vv.getRenderContext().setEdgeLabelTransformer(
					new EdgeStringRenderer());
			vv.getRenderContext().setEdgeDrawPaintTransformer(
					new EdgePaintFunction());
			vv.getRenderContext().setEdgeStrokeTransformer(
					new EdgeStrokeFunction());

			vv.getRenderer().getVertexLabelRenderer()
					.setPosition(Renderer.VertexLabel.Position.CNTR);

			vv.setPreferredSize(new Dimension(850, 850));

			gm = new DefaultModalGraphMouse();
			gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1_MASK));
			gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(),
					0, 1.1f, 0.9f));

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
			return ""; 
		}
	}

	public class EdgePaintFunction implements Transformer<NetEdge, Paint> {

		public Paint transform(NetEdge e) {
			if (getPathType(e) == EdgeType.FIRST)
				return Color.RED;
			else if (getPathType(e) == EdgeType.SECOND)
				return Color.BLUE;
			else if (getPathType(e) == EdgeType.BOTH)
				return Color.GREEN;
			else
				return Color.LIGHT_GRAY;
		}
	}

	public class EdgeStrokeFunction implements Transformer<NetEdge, Stroke> {
		protected final Stroke THIN = new BasicStroke(1);
		protected final Stroke THICK = new BasicStroke(2);

		public Stroke transform(NetEdge e) {
			if (getPathType(e) != EdgeType.NONE)
				return THICK;
			return THIN;
		}

	}

	/**
	 * Não direcional
	 * 
	 * @param e
	 * @return
	 */
	private EdgeType getPathType(NetEdge e) {

		EdgeType p = EdgeType.NONE;
		if (path1 != null && path2 != null) {
			if (path1.contains(e) || path1.containsInverse(e))
				p = EdgeType.FIRST;
			if (path2.contains(e) || path2.containsInverse(e)) {
				if (p == EdgeType.FIRST)
					p = EdgeType.BOTH;
				else
					p = EdgeType.SECOND;
			}
		}

		return p;
	}

	private void updatePath() {
		NetNode bfrom = (NetNode) from.getSelectedItem();
		NetNode bto = (NetNode) to.getSelectedItem();
		if (bfrom == null || bto == null) {
			path1 = null;
			path2 = null;
		} else {
			int nfrom = bfrom.getNodeId();
			int nto = bto.getNodeId();
			if (nfrom == nto) {
				path1 = null;
				path2 = null;
			} else {
				path1 = new NetPath(wc.getComparator().getSP1()
						.getAllPaths(nfrom, nto));
				path2 = new NetPath(wc.getComparator().getSP2()
						.getAllPaths(nfrom, nto));
			}
		}
	}

	private JToolBar buildToolBar() {

		JPanel toolbar = new JPanel();
		Border raisedBorder = BorderFactory.createRaisedBevelBorder();
		toolbar.setBorder(raisedBorder);

		JPanel optionp = new JPanel();
		optionp.setLayout(new BoxLayout(optionp, BoxLayout.LINE_AXIS));
		optionp.setOpaque(false);
		optionp.setBorder(BorderFactory.createTitledBorder("Mode"));

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
		zoomControls.add(plus);
		zoomControls.add(minus);
		controls.add(zoomControls);

		JPanel modeControls = new JPanel();
		modeControls.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));

		//modeControls.add(gm.getModeComboBox());
		//controls.add(modeControls);

		toolbar.add(controls);
		
		JPanel pathControls = new JPanel();
		pathControls.setBorder(BorderFactory.createTitledBorder("Path"));
		

		from = new DefaultComboBoxModel(wc.getBriteGraph().getNodes());
		JComboBox cfrom = new JComboBox(from);
		cfrom.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				updatePath();
				vv.repaint();
			}
		});

		to = new DefaultComboBoxModel(wc.getBriteGraph().getNodes());
		JComboBox cto = new JComboBox(to);
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
		
		toolbar.add(pathControls);

		JToolBar toolb = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
		toolb.add(toolbar);
		toolb.setFloatable(false);
		return toolb;
	}

}
