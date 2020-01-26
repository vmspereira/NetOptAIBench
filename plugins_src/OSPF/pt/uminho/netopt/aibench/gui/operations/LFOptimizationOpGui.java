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
package pt.uminho.netopt.aibench.gui.operations;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.algoritmi.netopt.ospf.optimization.Params;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.gui.utils.text.NumberTextField;


public class LFOptimizationOpGui extends JDialog implements ActionListener,
		InputGUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private NumberTextField alphaFld;
	private NumberTextField achiveSizeFld;
	private JComboBox<ClipboardItem> firstObjectiveBox;
	private JCheckBox invcapCheckBox;
	private NumberTextField iterationsFld;
	private JCheckBox l2CheckBox;
	private JRadioButton nsgaiiRadioBt;
	private NumberTextField percentPopFld;
	private NumberTextField populSizeFld;
	private JComboBox<ClipboardItem> populationBox;
	private JComboBox<ClipboardItem> projectBox;
	private NumberTextField runsFld;
	private JCheckBox unitCheckBox;
	private JComboBox<Params.EdgeSelectionOption> linkFailureMethod;
	private JComboBox<LoadBalancer> loadBalancer;
	private JComboBox<NetEdge> link;
	private JRadioButton soeaRadioBt;
	private JRadioButton spea2RadioBt;

	JLabel alphaLabel;

	public static final String OK_BUTTON_ACTION_COMMAND = "okButtonActionCommand";
	public static final String CANCEL_BUTTON_ACTION_COMMAND = "cancelButtonActionCommand";
	protected JButton okButton;
	protected JButton cancelButton;

	private ParamsReceiver rec;

	public LFOptimizationOpGui() {
		super(Workbench.getInstance().getMainFrame());
		initComponents();
	}

	private void initComponents() {

		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
		thisLayout.rowHeights = new int[] { 0, 7, 0, 7 };
		thisLayout.columnWeights = new double[] { 0.1 };
		thisLayout.columnWidths = new int[] { 7 };
		getContentPane().setLayout(thisLayout);

		Dimension preferredSize = new Dimension(150, 20);
		Dimension preferredSizeCombo = new Dimension(220, 20);
		JPanel projectPanel;
		JPanel algoritmPanel;
		JLabel populSizeLabel;
		JLabel archSizeLabel;
		JPanel firstObjPanel;
		JLabel firstObjectiveLabel;
		JLabel iterationsLabel;
		JLabel percentPopLabel;
		JLabel populationLabel;
		JPanel prevRunsPanel;
		JLabel projecBoxLabel;
		JLabel runsLabel;
		JLabel linkFailureMehtodLabel;
		final JLabel linkLabel;
		JPanel linkFailurePanel;
		JTabbedPane seedingPanel;
		JPanel traditionalPanel;
		JPanel loadBalancerPanel;
		JLabel loadBalancerLabel;

		projectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		projectPanel.setBorder(BorderFactory.createTitledBorder("Project"));
		projecBoxLabel = new JLabel();

		projectBox = new JComboBox<ClipboardItem>(
				this.getItemsByClass(ProjectBox.class));
		projectBox.setPreferredSize(preferredSizeCombo);
		projecBoxLabel.setText("Project");
		projecBoxLabel.setPreferredSize(preferredSize);
		projectPanel.add(projecBoxLabel);
		projectPanel.add(projectBox);
		this.getContentPane().add(
				projectPanel,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

		firstObjPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		firstObjPanel.setBorder(BorderFactory
				.createTitledBorder("Traffic Demands"));
		firstObjPanel.setDoubleBuffered(true);
		firstObjectiveLabel = new JLabel();
		firstObjectiveLabel.setText("Demands");
		firstObjectiveLabel.setPreferredSize(preferredSize);
		firstObjectiveBox = new JComboBox<ClipboardItem>(
				this.getItemsByClass(Demands.class));
		firstObjectiveBox.setPreferredSize(preferredSizeCombo);
		firstObjPanel.add(firstObjectiveLabel);
		firstObjPanel.add(firstObjectiveBox);
		this.getContentPane().add(
				firstObjPanel,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

		//---------------------------------------------------
		
		
		linkFailurePanel = new JPanel();
		linkFailurePanel.setLayout(new BoxLayout(linkFailurePanel,
				BoxLayout.Y_AXIS));
		linkFailurePanel.setBorder(BorderFactory
				.createTitledBorder("Link Failure Selection"));



		linkFailureMehtodLabel = new JLabel("Selection Method");
		linkFailureMehtodLabel.setPreferredSize(preferredSize);
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		linkFailureMethod = new JComboBox<Params.EdgeSelectionOption>(Params.EdgeSelectionOption.values());
		linkFailureMethod.setPreferredSize(preferredSizeCombo);
		
		p1.add(linkFailureMehtodLabel);
		p1.add(linkFailureMethod);
		linkFailurePanel.add(p1);
		
		
		linkLabel = new JLabel("Link");
		linkLabel.setPreferredSize(preferredSize);
		
		link = new JComboBox<NetEdge>();
		link.setPreferredSize(preferredSizeCombo);
		
		if(projectBox.getSelectedItem()!=null){
			ClipboardItem item = (ClipboardItem)(projectBox.getSelectedItem());
			pt.uminho.netopt.aibench.datatypes.ProjectBox project = (pt.uminho.netopt.aibench.datatypes.ProjectBox) item.getUserData();
			link.setModel(new DefaultComboBoxModel<NetEdge>(project.getNetworkTopologyBox().getNetworkTopology().getNetGraph().getEdges()));
		}
		
		projectBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(projectBox.getSelectedItem()!=null){
					ClipboardItem item = (ClipboardItem)(projectBox.getSelectedItem());
					pt.uminho.netopt.aibench.datatypes.ProjectBox project = (pt.uminho.netopt.aibench.datatypes.ProjectBox) item.getUserData();
					link.setModel(new DefaultComboBoxModel<NetEdge>(project.getNetworkTopologyBox().getNetworkTopology().getNetGraph().getEdges()));
				}
			}
		});
		
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p2.add(linkLabel);
		p2.add(link);
		linkFailurePanel.add(p2);

		link.setEnabled(false);
		linkLabel.setEnabled(false);
	
		linkFailureMethod.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				if(linkFailureMethod.getSelectedItem().equals(Params.EdgeSelectionOption.USERSELECTED)){
					link.setEnabled(true);
					linkLabel.setEnabled(true);
				}else{
					link.setEnabled(false);
					linkLabel.setEnabled(false);
				}
			}
		});
		
		this.getContentPane().add(
				linkFailurePanel,
				new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
		
		
		
		//----------------------------------------------------------------------
		
		loadBalancerPanel = new JPanel();
		loadBalancerPanel.setLayout(new BoxLayout(loadBalancerPanel,
				BoxLayout.Y_AXIS));
		loadBalancerPanel.setBorder(BorderFactory
				.createTitledBorder("Routing"));


		loadBalancerLabel= new JLabel();
		loadBalancerLabel.setText("Protocol");
		loadBalancerLabel.setPreferredSize(preferredSize);
		
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		loadBalancer = new JComboBox<LoadBalancer>(LoadBalancer.values());
		loadBalancer.setPreferredSize(preferredSizeCombo);
		p3.add(loadBalancerLabel);
		p3.add(loadBalancer);
		loadBalancerPanel.add(p3);
		
		this.getContentPane().add(
				loadBalancerPanel,
				new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
		
		
		//----------------------------------------------------------------------
		
		seedingPanel = new JTabbedPane();
		seedingPanel.setBorder(BorderFactory.createTitledBorder("Seeding"));

		prevRunsPanel = new JPanel();
		prevRunsPanel.setLayout(new BoxLayout(prevRunsPanel, BoxLayout.Y_AXIS));
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		populationLabel = new JLabel("Population");
		populationLabel.setPreferredSize(preferredSize);
		populationBox = new JComboBox<ClipboardItem>(
				this.getItemsByClass(Population.class));
		populationBox.setPreferredSize(preferredSizeCombo);
		p5.add(populationLabel);
		p5.add(populationBox);

		JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		percentPopLabel = new JLabel("Percentage");
		percentPopLabel.setPreferredSize(preferredSize);
		percentPopFld = new NumberTextField("%",0);
		percentPopFld.setDouble(0.0);
		percentPopFld.setColumns(10);

		p6.add(percentPopLabel);
		p6.add(percentPopFld);

		prevRunsPanel.add(p5);
		prevRunsPanel.add(p6);
		seedingPanel.addTab("From Previous Runs", prevRunsPanel);

		invcapCheckBox = new JCheckBox("InvCapOSPF");
		unitCheckBox = new JCheckBox("UnitOSPF");
		l2CheckBox = new JCheckBox("L2OSPF");
		traditionalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		traditionalPanel.add(invcapCheckBox);
		traditionalPanel.add(unitCheckBox);
		traditionalPanel.add(l2CheckBox);
		seedingPanel.addTab("Traditional Configurations", traditionalPanel);
		this.getContentPane().add(
				seedingPanel,
				new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

		algoritmPanel = new JPanel();
		algoritmPanel.setLayout(new BoxLayout(algoritmPanel, BoxLayout.Y_AXIS));
		algoritmPanel.setBorder(BorderFactory.createTitledBorder("Algorithm"));
		soeaRadioBt = new JRadioButton("SOEA");
		spea2RadioBt = new JRadioButton("SPEA2");
		nsgaiiRadioBt = new JRadioButton("NSGAII");
		alphaLabel = new JLabel("Alpha");
		alphaFld = new NumberTextField(2);
		alphaFld.setColumns(4);
		alphaFld.setDouble(0.5);
		ActionListener action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (soeaRadioBt.isSelected()) {
					alphaLabel.setEnabled(true);
					alphaFld.setEnabled(true);
				} else {
					alphaLabel.setEnabled(false);
					alphaFld.setEnabled(false);
				}
			}
		};
		this.soeaRadioBt.addActionListener(action);
		this.spea2RadioBt.addActionListener(action);
		this.nsgaiiRadioBt.addActionListener(action);
		this.soeaRadioBt.setSelected(true);

		JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p4.add(spea2RadioBt);
		p4.add(nsgaiiRadioBt);
		p4.add(soeaRadioBt);
		p4.add(alphaLabel);
		p4.add(alphaFld);
		algoritmPanel.add(p4);

		ButtonGroup algorithmGroup = new ButtonGroup();
		algorithmGroup.add(this.nsgaiiRadioBt);
		algorithmGroup.add(this.spea2RadioBt);
		algorithmGroup.add(this.soeaRadioBt);

		populSizeLabel = new JLabel("Population Size");
		populSizeLabel.setPreferredSize(preferredSize);
		archSizeLabel = new JLabel("Archive Size");
		archSizeLabel.setPreferredSize(preferredSize);
		populSizeFld = new NumberTextField(0);
		populSizeFld.setDouble(100.0);
		populSizeFld.setColumns(10);
		achiveSizeFld = new NumberTextField(0);
		achiveSizeFld.setText("100");
		achiveSizeFld.setColumns(10);
		JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p7.add(populSizeLabel);
		p7.add(populSizeFld);
		p7.add(archSizeLabel);
		p7.add(achiveSizeFld);

		JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		iterationsLabel = new JLabel("N. of Generations");
		iterationsLabel.setPreferredSize(preferredSize);
		iterationsFld = new NumberTextField(0);
		iterationsFld.setText("1000");
		iterationsFld.setColumns(10);
		runsLabel = new JLabel("Number of Runs");
		runsLabel.setPreferredSize(preferredSize);
		runsFld = new NumberTextField(0);
		runsFld.setText("1");
		runsFld.setColumns(10);
		p8.add(iterationsLabel);
		p8.add(iterationsFld);
		p8.add(runsLabel);
		p8.add(runsFld);

		algoritmPanel.add(p7);
		algoritmPanel.add(p8);

		this.getContentPane().add(
				algoritmPanel,
				new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		okButton = new JButton("Ok");
		okButton.addActionListener(this);
		okButton.setActionCommand(LFOptimizationOpGui.OK_BUTTON_ACTION_COMMAND);
		cancelButton = new JButton("Cancel");
		cancelButton
				.setActionCommand(LFOptimizationOpGui.CANCEL_BUTTON_ACTION_COMMAND);
		cancelButton.addActionListener(this);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		this.getContentPane().add(
				buttonPanel,
				new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
		this.setResizable(false);
	}

	@Override
	public void finish() {
		setVisible(false);
		dispose();
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		rec = arg0;
		setTitle(arg1.getName());
		pack();
		Utilities.centerOnOwner(this);
		setVisible(true);
	}

	@Override
	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand
				.equals(LFOptimizationOpGui.OK_BUTTON_ACTION_COMMAND)) {
			try {
				if (projectBox.getSelectedItem() == null)
					Workbench.getInstance().warn("Empty ProjectBox Set");
				else if (firstObjectiveBox.getSelectedItem() == null)
					Workbench.getInstance().warn("Empty First Demands Set");
				else
					termination();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (actionCommand
				.equals(LFOptimizationOpGui.CANCEL_BUTTON_ACTION_COMMAND)) {
			finish();
		}

	}

	private ClipboardItem[] getItemsByClass(Class<?> type) {
		List<ClipboardItem> l = Core.getInstance().getClipboard()
				.getItemsByClass(type);
		ClipboardItem[] items = new ClipboardItem[l.size()];
		return l.toArray(items);
	}

	
	
	private void termination() {

		ProjectBox project = null;
		try {
			ClipboardItem item = (ClipboardItem) this.projectBox
					.getSelectedItem();
			project = (ProjectBox) item.getUserData();
		} catch (NullPointerException e) {
		}

		Demands demands1 =new Demands(0);;
		try {
			ClipboardItem item = (ClipboardItem) this.firstObjectiveBox
					.getSelectedItem();
			demands1 = (Demands) item.getUserData();
		} catch (NullPointerException e) {
		}


		Params.EdgeSelectionOption selectionOption= (Params.EdgeSelectionOption)(this.linkFailureMethod.getSelectedItem());
		Integer edgeID=((NetEdge)(link.getSelectedItem())).getEdgeId();

		LoadBalancer loadBalancerMethod = (LoadBalancer) this.loadBalancer.getSelectedItem();
		
		Population population = new Population();
		try {
			ClipboardItem item = (ClipboardItem) this.populationBox
					.getSelectedItem();
			population = (Population) item.getUserData();
		} catch (NullPointerException e) {
		}

		Double popPercent = this.percentPopFld.getDouble();
		if(popPercent>100)
			popPercent=100.0;
		Boolean invcap = this.invcapCheckBox.isSelected();
		Boolean l2 = this.l2CheckBox.isSelected();
		Boolean unit = this.unitCheckBox.isSelected();

		Params.AlgorithmSelectionOption algorithm;
		if (this.nsgaiiRadioBt.isSelected())
			algorithm = Params.AlgorithmSelectionOption.NSGAII;
		else if (this.spea2RadioBt.isSelected())
			algorithm = Params.AlgorithmSelectionOption.SPEA2;
		else
			algorithm = Params.AlgorithmSelectionOption.SOEA;
		
		Double alpha=this.alphaFld.getDouble();

		Integer populationSize = this.populSizeFld.getDouble().intValue();
		Integer archiveSize = this.achiveSizeFld.getDouble().intValue();
		Integer iterations = this.iterationsFld.getDouble().intValue();
		Integer runs = this.runsFld.getDouble().intValue();

		rec.paramsIntroduced(new ParamSpec[] {
				new ParamSpec("ProjectBox", ProjectBox.class, project, null),
				new ParamSpec("FirstDemands", Demands.class, demands1, null),	
				new ParamSpec("LinkFailureSelectionMethod", Params.EdgeSelectionOption.class, selectionOption, null),
				new ParamSpec("LinkFailureID", Integer.class, edgeID, null),
				new ParamSpec("LoadBalancer",LoadBalancer.class,loadBalancerMethod,null),
				new ParamSpec("Population", Population.class, population, null),
				new ParamSpec("PopulationPercentage", Double.class, popPercent,
						null),
				new ParamSpec("InvCap", Boolean.class, invcap, null),
				new ParamSpec("L2", Boolean.class, l2, null),
				new ParamSpec("Unit", Boolean.class, unit, null),
				new ParamSpec("Algorithm",
						Params.AlgorithmSelectionOption.class, algorithm, null),
				new ParamSpec("Alpha",
								Double.class, alpha, null),		
				new ParamSpec("PopulationSize", Integer.class, populationSize,
						null),
				new ParamSpec("ArchiveSize", Integer.class, archiveSize, null),
				new ParamSpec("Iterations", Integer.class, iterations, null),
				new ParamSpec("Runs", Integer.class, runs, null)
		});
		
	}

}
