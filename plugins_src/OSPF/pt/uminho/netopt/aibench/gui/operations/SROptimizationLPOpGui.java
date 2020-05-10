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
* @author V�tor Pereira
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import pt.uminho.algoritmi.netopt.ospf.optimization.Params.AlgorithmSecondObjective;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode.NodeType;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.gui.utils.text.NumberTextField;


public class SROptimizationLPOpGui extends JDialog implements ActionListener,
		InputGUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private NumberTextField achiveSizeFld;
	private JRadioButton demandsRadioBt;
	private JRadioButton mluRadioBt;
	private JComboBox<ClipboardItem> firstObjectiveBox;
	private JCheckBox invcapCheckBox;
	private NumberTextField iterationsFld;
	private JCheckBox l2CheckBox;
	private NumberTextField percentPopFld;
	private NumberTextField populSizeFld;
	private JComboBox<ClipboardItem> populationBox;
	private JComboBox<ClipboardItem> projectBox;
	private NumberTextField runsFld;
	private JCheckBox unitCheckBox;
	private JComboBox<ClipboardItem> secondDemandsBox;
	

	JLabel alphaLabel;

	public static final String OK_BUTTON_ACTION_COMMAND = "okButtonActionCommand";
	public static final String CANCEL_BUTTON_ACTION_COMMAND = "cancelButtonActionCommand";
	protected JButton okButton;
	protected JButton cancelButton;

	private ParamsReceiver rec;

	public SROptimizationLPOpGui() {
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
		JLabel secondDemandsLabel;
		JLabel secondObjectiveLabel;
		JPanel secondObjectivePanel;
		JTabbedPane seedingPanel;
		JPanel traditionalPanel;
		

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
				.createTitledBorder("First Objective"));
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

		secondObjectivePanel = new JPanel();
		secondObjectivePanel.setLayout(new BoxLayout(secondObjectivePanel,
				BoxLayout.Y_AXIS));
		secondObjectivePanel.setBorder(BorderFactory
				.createTitledBorder("Second Objective"));

		demandsRadioBt = new JRadioButton("Congestion");
		demandsRadioBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				secondDemandsBox.setEnabled(true);
			}
		});
		
		secondObjectiveLabel = new JLabel("Objective");
		secondObjectiveLabel.setPreferredSize(preferredSize);

		mluRadioBt = new JRadioButton("MLU");
		mluRadioBt.setToolTipText("Minimize Maximum Link Utilization");
		mluRadioBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				secondDemandsBox.setEnabled(false);
			}
		});
		ButtonGroup secondGroup = new ButtonGroup();
		secondGroup.add(mluRadioBt);
		secondGroup.add(demandsRadioBt);
		
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(secondObjectiveLabel);
		p1.add(mluRadioBt);
		p1.add(demandsRadioBt);
		secondObjectivePanel.add(p1);

		secondDemandsLabel = new JLabel("Demands");
		secondDemandsLabel.setPreferredSize(preferredSize);
		secondDemandsBox = new JComboBox<ClipboardItem>(
				this.getItemsByClass(Demands.class));
		secondDemandsBox.setPreferredSize(preferredSizeCombo);
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p2.add(secondDemandsLabel);
		p2.add(secondDemandsBox);
		
		
		secondObjectivePanel.add(p2);
	
		this.getContentPane().add(
				secondObjectivePanel,
				new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
		
		demandsRadioBt.setSelected(false);
		mluRadioBt.setSelected(true);
		secondDemandsBox.setEnabled(false);

		
				
	
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
		okButton.setActionCommand(SROptimizationLPOpGui.OK_BUTTON_ACTION_COMMAND);
		cancelButton = new JButton("Cancel");
		cancelButton
				.setActionCommand(SROptimizationLPOpGui.CANCEL_BUTTON_ACTION_COMMAND);
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
	
		this.setResizable(true);
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
				.equals(SROptimizationLPOpGui.OK_BUTTON_ACTION_COMMAND)) {
			try {
				if (projectBox.getSelectedItem() == null)
					Workbench.getInstance().warn("Empty ProjectBox Set");
				else if (firstObjectiveBox.getSelectedItem() == null)
					Workbench.getInstance().warn("Empty First Demands Set");
				else{
					ClipboardItem item = (ClipboardItem) this.projectBox
							.getSelectedItem();
					ProjectBox project = (ProjectBox) item.getUserData();
					boolean connected = project.getNetworkTopologyBox().getNetworkTopology().getNetGraph().isConnected(NodeType.SDN_SR,false);
					if(!connected){
						int option=JOptionPane.showConfirmDialog(this, "SR nodes do not constitute an island.", "Attention", JOptionPane.WARNING_MESSAGE);
						if(option== JOptionPane.CANCEL_OPTION)
							return;
					}
					termination();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (actionCommand
				.equals(SROptimizationLPOpGui.CANCEL_BUTTON_ACTION_COMMAND)) {
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

		Demands demands2 = new Demands(0);
		try {
			ClipboardItem item = (ClipboardItem) this.secondDemandsBox
					.getSelectedItem();
			demands2 = (Demands) item.getUserData();
		} catch (NullPointerException e) {
		}

		

		
		
		
		Params.AlgorithmSecondObjective secondObjective = Params.AlgorithmSecondObjective.DEMANDS;
		if (this.mluRadioBt.isSelected()){
			secondObjective = Params.AlgorithmSecondObjective.MLU;
		}
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

		
		Integer populationSize = this.populSizeFld.getDouble().intValue();
		Integer archiveSize = this.achiveSizeFld.getDouble().intValue();
		Integer iterations = this.iterationsFld.getDouble().intValue();
		Integer runs = this.runsFld.getDouble().intValue();

		rec.paramsIntroduced(new ParamSpec[] {
				new ParamSpec("ProjectBox", ProjectBox.class, project, null),
				new ParamSpec("FirstDemands", Demands.class, demands1, null),
				new ParamSpec("SecondDemands", Demands.class, demands2, null),
				new ParamSpec("SecondObjective",
						AlgorithmSecondObjective.class, secondObjective,
						null),
				new ParamSpec("Population", Population.class, population, null),
				new ParamSpec("PopulationPercentage", Double.class, popPercent,
						null),
				new ParamSpec("InvCap", Boolean.class, invcap, null),
				new ParamSpec("L2", Boolean.class, l2, null),
				new ParamSpec("Unit", Boolean.class, unit, null),
				new ParamSpec("PopulationSize", Integer.class, populationSize,
						null),
				new ParamSpec("ArchiveSize", Integer.class, archiveSize, null),

				new ParamSpec("Iterations", Integer.class, iterations, null),
				new ParamSpec("Runs", Integer.class, runs, null)
		});
		
	}

}
