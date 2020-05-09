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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;


import pt.uminho.algoritmi.netopt.cplex.SRLoadBalancingPhiSolver;
import pt.uminho.algoritmi.netopt.cplex.SRLoadBalancingSolver;
import pt.uminho.algoritmi.netopt.ospf.graph.Graph;
import pt.uminho.algoritmi.netopt.ospf.graph.Graph.Status;
import pt.uminho.algoritmi.netopt.ospf.listener.TopologyChangeListener;
import pt.uminho.algoritmi.netopt.ospf.listener.TopologyEvent;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.algoritmi.netopt.ospf.simulation.ResultSimul;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.Flow;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.SRConfiguration;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.SRSimulator;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.Flow.FlowType;
import pt.uminho.algoritmi.netopt.ospf.utils.MathUtils;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode.NodeType;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultSimulType;
import pt.uminho.netopt.jung.*;
import org.apache.commons.collections15.Transformer;

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
import es.uvigo.ei.aibench.core.clipboard.ClipboardListener;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench; 

@Datatype(structure = Structure.COMPLEX, viewable = true, namingMethod = "getName", setNameMethod = "setName", autoOpen = true)
public class SRSimulatorView extends javax.swing.JPanel implements InputGUI {

	/**
	 * 
	 */
	private String name;
	private static final long serialVersionUID = 1L;
	private VisualizationViewer<NetNode, NetEdge> vv;
	private NetOptEditingModalGraphMouse<NetNode, NetEdge> gm;
	private JComboBox<ClipboardItem> srConfComboBox;
	private JComboBox<ClipboardItem> demandsComboBox;
	private EdgeModel emodel;
	private FlowModel fmodel;
	private boolean[] selectedEdge;
	private JLabel congestionLabel;
	private JLabel mluLabel;
	private SRSimulator srSimul;
	protected SRSimulatorView view;

	public SRSimulatorView(SRSimulator simul) {
		this.srSimul = simul;
		this.view = this;
		this.selectedEdge = new boolean[srSimul.getTopology().getNumberEdges() / 2];
		initGUI();
	}

	protected void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);

			this.setPreferredSize(new java.awt.Dimension(900, 900));
			JungGraph graph = new JungGraph(srSimul.getTopology().getNetGraph());
			graph.populateGraph();
			Layout<NetNode, NetEdge> layout = new StaticLayout<NetNode, NetEdge>(graph.getGraph(),
					graph.getLocationTransformer());
			layout.setSize(new Dimension(800, 800)); // sets the initial
			vv = new VisualizationViewer<NetNode, NetEdge>(layout);

			vv.setBackground(Color.WHITE);

			// Show vertex and edge labels
			vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<NetNode>());
			vv.getRenderContext().setEdgeLabelTransformer(new EdgeStringRenderer());
			vv.getRenderContext().setEdgeDrawPaintTransformer(new EdgePaintFunction());
			vv.getRenderContext().setEdgeStrokeTransformer(new EdgeStrokeFunction());
			vv.getRenderContext().setVertexFillPaintTransformer(new VertexPaintFunction());

			DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(Color.white) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

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
			// gm.setEditingPlugin(new NetOptEditingGraphMousePlugin<NetNode,
			// NetEdge>(null,null));
			gm.remove(gm.getPopupEditingPlugin());

			vv.setGraphMouse(gm);

			PopupVertexEdgeMenuMousePlugin<NetNode, NetEdge> mousePlugin = new PopupVertexEdgeMenuMousePlugin<NetNode, NetEdge>(
					vv);
			JPopupMenu edgeMenu = new EdgeMenu();
			mousePlugin.setEdgePopup(edgeMenu);

			gm.add(mousePlugin); // Add our new plugin to the mouse

			srSimul.getTopology().addTopologyChangeListener(new TopologyChangeListener() {

				@Override
				public void updateTopology(TopologyEvent evt) {
					if (evt.getType() == TopologyEvent.EDGE_DOWN || evt.getType() == TopologyEvent.EDGE_UP) {
						NetEdge e = (NetEdge) (evt.getObject());
						srSimul.failLink(e);
						updateView();
					}
				}

			});

			Clipboard clipboard = Core.getInstance().getClipboard();
			List<ClipboardItem> confs = clipboard.getItemsByClass(SRConfiguration.class);
			List<ClipboardItem> demands = clipboard.getItemsByClass(Demands.class);

			srConfComboBox = new JComboBox<ClipboardItem>();
			srConfComboBox.setModel(new DefaultComboBoxModel(confs.toArray()));

			demandsComboBox = new JComboBox<ClipboardItem>();
			demandsComboBox.setModel(new DefaultComboBoxModel(demands.toArray()));

			clipboard.addClipboardListener(new ClipboardListener() {

				@Override
				public void elementAdded(ClipboardItem item) {
					// if(item.getUserData().getClass().equals(SRConfiguration.class)){
					List<ClipboardItem> confs = Core.getInstance().getClipboard()
							.getItemsByClass(SRConfiguration.class);
					srConfComboBox.setModel(new DefaultComboBoxModel(confs.toArray()));
					// }
					// else
					// if(item.getUserData().getClass().equals(Demands.class)){
					List<ClipboardItem> demands = Core.getInstance().getClipboard().getItemsByClass(Demands.class);
					demandsComboBox.setModel(new DefaultComboBoxModel(demands.toArray()));
					// }
				}

				@Override
				public void elementRemoved(ClipboardItem item) {
					// if(item.getUserData().getClass().equals(SRConfiguration.class)){
					List<ClipboardItem> confs = Core.getInstance().getClipboard()
							.getItemsByClass(SRConfiguration.class);
					srConfComboBox.setModel(new DefaultComboBoxModel(confs.toArray()));
					// }
					// else
					// if(item.getUserData().getClass().equals(Demands.class)){
					List<ClipboardItem> demands = Core.getInstance().getClipboard().getItemsByClass(Demands.class);
					demandsComboBox.setModel(new DefaultComboBoxModel(demands.toArray()));
					// }
				}
			});

			emodel = new EdgeModel();
			JTable table = new JTable();
			table.setModel(emodel);
			table.setAutoCreateRowSorter(true);
			table.setShowHorizontalLines(false);
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent event) {
					try {
						int index = table.getRowSorter().convertRowIndexToModel(table.getSelectedRow());
						selectedEdge = new boolean[srSimul.getTopology().getNumberEdges() / 2];
						selectedEdge[index / 2] = true;
						vv.repaint();
					} catch (Exception e) {
					}
				}
			});

			table.addMouseListener(new EdgeTableMouseListener(table));
			table.getColumnModel().getColumn(2).setCellRenderer(new UsageRenderer());
			JScrollPane jse = new JScrollPane(table);
			jse.setPreferredSize(new Dimension(200, 100));
			jse.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Links"));

			fmodel = new FlowModel();
			JTable ftable = new JTable();
			ftable.setModel(fmodel);
			ftable.setAutoCreateRowSorter(true);
			ftable.setShowHorizontalLines(false);

			ftable.addMouseListener(new FlowTableMouseListener(ftable));
			ftable.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent event) {
					int keyCode = event.getKeyCode();
					if (keyCode == KeyEvent.VK_DELETE) {
						int i = ftable.getSelectedRow();
						int n = ftable.getRowSorter().convertRowIndexToModel(i);
						Flow f = fmodel.getFlowAt(n);
						try {
							srSimul.removeFlow(f);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						updateView();
					}
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					// TODO Auto-generated method stub

				}
			});

			JScrollPane jsf = new JScrollPane(ftable);
			jsf.setPreferredSize(new Dimension(250, 100));
			jsf.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Flows"));

			JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vv, jse);
			sp.setResizeWeight(0.8);

			JSplitPane sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsf, sp);
			sp.setResizeWeight(0.8);

			this.add(sp2, BorderLayout.CENTER);
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
			return "";// ("+i.getFrom()+"-"+i.getTo()+")
						// "+MathUtils.doubleToString((srSimul.getLoad(i.getFrom(),i.getTo())/i.getBandwidth()),2)+"%"
						// +"("+i.getTo()+"-"+i.getFrom()+")
						// "+MathUtils.doubleToString((srSimul.getLoad(i.getTo(),i.getFrom())/i.getBandwidth()),2)+"%";
		}
	}

	public class EdgePaintFunction implements Transformer<NetEdge, Paint> {

		public Paint transform(NetEdge e) {
			int idx = e.getEdgeId();
			if (selectedEdge[idx]) {
				return Color.GREEN;
			} else if (!e.isUP())
				return Color.RED;
			else
				return Color.LIGHT_GRAY;
		}
	}

	public class EdgeStrokeFunction implements Transformer<NetEdge, Stroke> {
		protected final Stroke THIN = new BasicStroke(1);
		protected final Stroke THICK = new BasicStroke(2);

		public Stroke transform(NetEdge e) {
			int idx = e.getEdgeId();
			if (selectedEdge[idx] || !e.isUP())
				return THICK;
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

	private JToolBar buildToolBar() {

		JPanel toolbar = new JPanel();
		Border raisedBorder = BorderFactory.createRaisedBevelBorder();
		toolbar.setBorder(raisedBorder);

		JLabel l1 = new JLabel("\u03A6* = ");
		l1.setFont(l1.getFont().deriveFont(Font.BOLD, 16));
		toolbar.add(l1);

		congestionLabel = new JLabel();
		congestionLabel.setFont(congestionLabel.getFont().deriveFont(Font.BOLD, 16));
		congestionLabel.setText("0.0");
		congestionLabel.setPreferredSize(new Dimension(80, 20));
		toolbar.add(congestionLabel);

		JLabel l2 = new JLabel("MLU = ");
		l2.setFont(l2.getFont().deriveFont(Font.BOLD, 16));
		toolbar.add(l2);

		mluLabel = new JLabel();
		mluLabel.setFont(mluLabel.getFont().deriveFont(Font.BOLD, 16));
		mluLabel.setText("0.0");
		mluLabel.setPreferredSize(new Dimension(80, 20));
		toolbar.add(mluLabel);

		
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

		toolbar.add(zoomControls);

		JPanel buttons = new JPanel();
		buttons.setBorder(BorderFactory.createTitledBorder("SR Configuration"));
		JButton jb = new JButton("Load");
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JPanel p = new JPanel();
				GridLayout layout = new GridLayout(2, 2, 3, 3);
				p.setLayout(layout);
				p.add(new JLabel("SR Configuration"));
				p.add(srConfComboBox);
				p.add(new JLabel("Demands"));
				p.add(demandsComboBox);
				int n = JOptionPane.showOptionDialog(view, p, "Input", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, // do not use a
															// custom Icon
						null, // the titles of buttons
						null); // default button title

				if (n == JOptionPane.OK_OPTION) {
					ClipboardItem item1 = (ClipboardItem) (srConfComboBox.getSelectedItem());
					ClipboardItem item2 = (ClipboardItem) (demandsComboBox.getSelectedItem());
					if (item1 != null && item2 != null) {
						SRConfiguration conf = (SRConfiguration) (item1.getUserData());
						Demands dem = (Demands) (item2.getUserData());
						srSimul.apply(conf, dem);
						updateView();
					}
				}

			}

		});

		JButton bclean = new JButton("Clear");
		bclean.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				srSimul.clear();
				updateView();
			}

		});

		JPanel panelPvalues = new JPanel();
		panelPvalues.setBorder(BorderFactory.createTitledBorder("Load Balancing"));
		JButton bp = new JButton("Load");
		panelPvalues.add(bp);
		bp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel p = new JPanel();
				Clipboard clipboard = Core.getInstance().getClipboard();
				List<ClipboardItem> pvalues = clipboard.getItemsByClass(PValues.class);

				JComboBox<ClipboardItem> pValuesComboBox = new JComboBox<ClipboardItem>();
				pValuesComboBox.setModel(new DefaultComboBoxModel(pvalues.toArray()));

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
						PValues conf = (PValues) (item1.getUserData());
						try {
							srSimul.apply(conf);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						updateView();
					}
				}

			}
		});

		JButton bp2 = new JButton("Optimize");
		panelPvalues.add(bp2);

		JRadioButton lb_ea_bt = new JRadioButton("\u03A6*");
		lb_ea_bt.setSelected(true);
		JRadioButton lb_lp_bt = new JRadioButton("MLU");
		lb_lp_bt.setSelected(false);
		ButtonGroup group = new ButtonGroup();
		group.add(lb_ea_bt);
		group.add(lb_lp_bt);
		panelPvalues.add(lb_ea_bt);
		panelPvalues.add(lb_lp_bt);
		bp2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (lb_ea_bt.isSelected()) {

					int n = JOptionPane.showOptionDialog(view, "Run \u03A6* optimization?", "Input",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (n == JOptionPane.OK_OPTION) {
						try{
							Workbench.getInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							Demands d = srSimul.getDemands(FlowType.SALP);
							OSPFWeights weights = srSimul.getWeights();
							SRLoadBalancingPhiSolver solver = new SRLoadBalancingPhiSolver(srSimul.getTopology().copy(),weights,d);
							solver.setSaveLoads(true);
							solver.setUpdateConfiguration(true);
							solver.setDebug(false);
							solver.optimize();
							srSimul.clear(FlowType.SALP);
							srSimul.apply(solver.getSRConfiguration(), d);
							updateView();
							Workbench.getInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
						catch(Exception exp){
							exp.printStackTrace();
						}
						synchronized (srSimul) {
							srSimul.notify();
						}


					}

				}
				else{
					int n = JOptionPane.showOptionDialog(view, "Run MLU optimization?", "Input",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (n == JOptionPane.OK_OPTION) {
					
						try{
							Workbench.getInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							Demands d = srSimul.getDemands(FlowType.SALP);
							OSPFWeights weights = srSimul.getWeights();
							SRLoadBalancingSolver solver = new SRLoadBalancingSolver(srSimul.getTopology().copy(),weights,d);
							solver.setSaveLoads(true);
							solver.setUpdateConfiguration(true);
							solver.setDebug(false);
							solver.optimize();
							srSimul.clear(FlowType.SALP);
							srSimul.apply(solver.getSRConfiguration(), d);
							updateView();
							Workbench.getInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
						catch(Exception exp){
							exp.printStackTrace();
						}
						synchronized (srSimul) {
							srSimul.notify();
						}
						
					}
				}
			}
		});

	buttons.add(jb);buttons.add(bclean);toolbar.add(buttons);toolbar.add(panelPvalues);

	JPanel p_loads = new JPanel();
	p_loads.setBorder(BorderFactory.createTitledBorder("Loads"));
	JButton save_loads = new JButton("Save");
	save_loads.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			NetworkLoads l = new NetworkLoads(srSimul.getLoads(),srSimul.getTopology().copy());
			l.setCongestion(srSimul.getCongestionValue());
			List<ClipboardItem> items =Core.getInstance().getClipboard()
	        .getItemsByClass(ProjectBox.class);
			ResultSimul results = new ResultSimul();
			results.addNetworkLoads(l);
			ResultSimulType resultsT = new ResultSimulType(results);
			if(items.size()>0)
				((ProjectBox)(items.get(0).getUserData())).addResultSimul(resultsT);
			
		}});

	p_loads.add(save_loads);
	toolbar.add(p_loads);
	
	
	JToolBar toolb = new JToolBar("Toolbar", JToolBar.HORIZONTAL);toolb.add(toolbar);
	toolb.setFloatable(false);return toolb;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onValidationError(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	private void updateView() {
		this.congestionLabel.setText("" + MathUtils.roundToDecimals(this.srSimul.getCongestionValue(), 2));
		this.mluLabel.setText("" + MathUtils.roundToDecimals(this.srSimul.getMLU()*100, 1)+"%");
		//System.out.println("Average Delay: "+this.srSimul.getAverageDelay());
		super.repaint();
		vv.repaint();
		emodel.fireTableDataChanged();
		fmodel.fireTableDataChanged();
	}

	private class EdgeModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EdgeModel() {
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public int getRowCount() {
			return srSimul.getTopology().getNumberEdges();
		}

		@Override
		public Object getValueAt(int row, int j) {
			int i = row / 2;
			NetEdge e = srSimul.getTopology().getNetGraph().getEdge(i);
			int src, dst;
			if (row % 2 == 0) {
				src = e.getFrom();
				dst = e.getTo();
			} else {
				src = e.getTo();
				dst = e.getFrom();
			}
			switch (j) {
			case 0:
				return src;
			case 1:
				return dst;
			case 2:
				if (e.isUP())
					return srSimul.getUsage(src, dst);
				else
					return null;
			default:
				return null;
			}
		}

		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return "Src";
			case 1:
				return "Dst";
			case 2:
				return "Usage";
			default:
				return "";
			}
		}

		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 2:
				return Double.class;
			default:
				return Integer.class;
			}
		}

	}

	private class FlowModel extends AbstractTableModel {

		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		public FlowModel() {
		}

		@Override
		public int getColumnCount() {
			return 6;
		}

		@Override
		public int getRowCount() {
			return srSimul.getNumberOfFlows();
		}

		@Override
		public Object getValueAt(int row, int j) {
			Flow f = srSimul.getFlow(row);
			switch (j) {
			case 0:
				return f.getSource();
			case 1:
				return f.getDestination();
			case 2:
				return MathUtils.roundToDecimals(f.getFraction(), 4) * 100;
			case 3:
				return f.getDemand();
			case 4:
				return srSimul.getPath(f).getLabelStackString();
			case 5:
				return f.getFlowType().toString();
			default:
				return null;
			}
		}

		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return "Src";
			case 1:
				return "Dst";
			case 2:
				return "Frc";
			case 3:
				return "Demands";
			case 4:
				return "Path";
			case 5:
				return "Type";
			default:
				return "";
			}
		}

		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
			case 1:
				return Integer.class;
			case 2:
			case 3:
				return Double.class;
			default:
				return String.class;
			}
		}

		public Flow getFlowAt(int row) {
			return srSimul.getFlow(row);
		}

	}

	class UsageRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (value == null) {
				this.setForeground(Color.red);
				this.setText("Down");

			} else if (value instanceof Number) {
				Number numberValue = (Number) value;
				NumberFormat formatter = NumberFormat.getPercentInstance();
				formatter.setMinimumFractionDigits(2);
				String valueString = formatter.format(numberValue.doubleValue());
				setText(valueString);
				this.setHorizontalAlignment(RIGHT);
				double u = numberValue.doubleValue();
				Color color;
				if (u == 0.0)
					color = new Color(64, 64, 64);
				else if (u < (1.0 / 3.0))
					color = new Color(0, 205, 0);
				else if (u < 2.0 / 3.0)
					color = new Color(0, 139, 0);
				else if (u < 0.9)
					color = new Color(0, 139, 69);
				else if (u < 1)
					color = new Color(35, 35, 142);
				else if (u < 1.1)
					color = new Color(139, 34, 82);
				else
					color = Color.RED;

				this.setForeground(color);
			}

			return this;
		}
	}

	public class FlowTableMouseListener extends MouseAdapter {

		private JPopupMenu popupMenu;
		private JTable table;

		public FlowTableMouseListener(JTable table) {

			this.setTable(table);
			popupMenu = new JPopupMenu();
			JMenuItem menuItem;
			menuItem = new JMenuItem("Remove Flow");
			menuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int i = table.getSelectedRow();
					int n = table.getRowSorter().convertRowIndexToModel(i);
					Flow f = fmodel.getFlowAt(n);
					try {
						srSimul.removeFlow(f);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					updateView();
				}
			});
			popupMenu.add(menuItem);

			table.setComponentPopupMenu(popupMenu);
		}

		@Override
		public void mousePressed(MouseEvent event) {
			// selects the row at which point the mouse is clicked
		}

		public JTable getTable() {
			return table;
		}

		public void setTable(JTable table) {
			this.table = table;
		}
	}

	public class EdgeTableMouseListener extends MouseAdapter {

		private JPopupMenu popupMenuUP;
		private JPopupMenu popupMenuDOWN;
		private JTable edgesTable;

		public EdgeTableMouseListener(JTable table) {

			this.edgesTable = table;
			popupMenuUP = new JPopupMenu();
			JMenuItem menuItem;
			menuItem = new JMenuItem("Set link UP");
			menuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int i = edgesTable.getSelectedRow();
					int n = edgesTable.getRowSorter().convertRowIndexToModel(i) / 2;
					NetEdge e = srSimul.getTopology().getNetGraph().getEdge(n);
					srSimul.getTopology().setEdgeStatus(e, Status.UP);
					updateView();
				}

			});
			popupMenuUP.add(menuItem);

			popupMenuDOWN = new JPopupMenu();
			JMenuItem menuItem2;
			menuItem2 = new JMenuItem("Set link DOWN");
			menuItem2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int i = edgesTable.getSelectedRow();
					int n = edgesTable.getRowSorter().convertRowIndexToModel(i) / 2;
					NetEdge e = srSimul.getTopology().getNetGraph().getEdge(n);
					srSimul.getTopology().setEdgeStatus(e, Status.DOWN);
					updateView();
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
			int n = edgesTable.getRowSorter().convertRowIndexToModel(i) / 2;
			NetEdge e = srSimul.getTopology().getNetGraph().getEdge(n);
			if (e.isUP())
				edgesTable.setComponentPopupMenu(popupMenuDOWN);
			else
				edgesTable.setComponentPopupMenu(popupMenuUP);

		}
	}

	public void setName(String s) {
		this.name = s;
	}

	public String getName() {
		return this.name;
	}

	public SRSimulator getSimulator() {
		return this.srSimul;
	}

	@SuppressWarnings("serial")
	public class EdgeMenu extends JPopupMenu {
		// private JFrame frame;
		public EdgeMenu() {
			super("Link Menu");
			this.add(new BWDisplay());
			this.add(new DelayDisplay());
			this.add(new WeightDisplay());
			this.addSeparator();
			this.add(new EdgePropItem());
			// this.add(new DeleteEdgeMenuItem<NetEdge>(topology));
			this.addSeparator();
			this.add(new EdgeStatusItem());
		}

		public class EdgePropItem extends JMenuItem implements EdgeMenuListener<NetEdge>, MenuPointListener {

			NetEdge edge;
			VisualizationViewer visComp;
			Point2D point;

			public void setEdgeAndView(NetEdge edge, VisualizationViewer visComp) {
				this.edge = edge;
				this.visComp = visComp;
			}

			public void setPoint(Point2D point) {
				this.point = point;
			}

			public EdgePropItem() {
				super("Edit link Properties...");
				this.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						EdgePropertyDialog dialog = new EdgePropertyDialog(edge);
						JOptionPane option = new JOptionPane(dialog, JOptionPane.PLAIN_MESSAGE,
								JOptionPane.OK_CANCEL_OPTION);
						JDialog d = option.createDialog(view, "Edit Edge ID " + edge.getEdgeId());
						d.setVisible(true);
						int n = -1;
						try {
							n = ((Number) option.getValue()).intValue();
						} catch (Exception exp) {
						}
						if (n == JOptionPane.OK_OPTION) {
							System.out.println("Changing edge definition");
							edge.setBandwidth(dialog.getBw());
							edge.setDelay(dialog.getDelay());
							edge.setLength(dialog.getLength());
						}
					}

				});
			}

		}

		public class EdgeStatusItem extends JMenuItem implements EdgeMenuListener<NetEdge>, MenuPointListener {
			NetEdge edge;
			VisualizationViewer visComp;
			Point2D point;

			public void setEdgeAndView(NetEdge edge, VisualizationViewer visComp) {
				this.edge = edge;
				if (srSimul.getTopology().getGraph().getConnection(edge.getFrom(), edge.getTo())
						.equals(Graph.Status.DOWN))
					this.setText("Set Edge UP");
				else
					this.setText("Set Edge DOWN");
				this.visComp = visComp;
			}

			public void setPoint(Point2D point) {
				this.point = point;
			}

			public EdgeStatusItem() {
				super();
				this.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (srSimul.getTopology().getGraph().getConnection(edge.getFrom(), edge.getTo())
								.equals(Graph.Status.DOWN)) {
							System.out.println("Edge " + edge.getEdgeId() + " set to UP");
							srSimul.getTopology().setEdgeStatus(edge, Graph.Status.UP);
						} else if (srSimul.getTopology().getGraph().getConnection(edge.getFrom(), edge.getTo())
								.equals(Graph.Status.UP)) {
							System.out.println("Edge " + edge.getEdgeId() + " set to DOWN");
							srSimul.getTopology().setEdgeStatus(edge, Graph.Status.DOWN);
						}
						visComp.repaint();
					}

				});
			}

		}

		public class WeightDisplay extends JMenuItem implements EdgeMenuListener<NetEdge> {
			public void setEdgeAndView(NetEdge e, VisualizationViewer visComp) {
				this.setText("Length  = " + e.getLength());
			}
		}

		public class BWDisplay extends JMenuItem implements EdgeMenuListener<NetEdge> {
			public void setEdgeAndView(NetEdge e, VisualizationViewer visComp) {
				this.setText("BandWidth  = " + e.getBandwidth() + "Mbit/s");
			}
		}

		public class DelayDisplay extends JMenuItem implements EdgeMenuListener<NetEdge> {
			public void setEdgeAndView(NetEdge e, VisualizationViewer visComp) {
				this.setText("Delay  = " + e.getDelay() + "ms");
			}
		}

	}

}
