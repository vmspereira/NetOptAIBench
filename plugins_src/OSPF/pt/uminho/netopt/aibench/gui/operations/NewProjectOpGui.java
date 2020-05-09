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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import es.uvigo.ei.aibench.core.ParamSource;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class NewProjectOpGui extends JDialog implements ActionListener,
InputGUI  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static ImageIcon ICON_FILE_OPEN = new ImageIcon(NewProjectOpGui.class.getResource("images/fileopen.png"));	
	Preferences prefs = Preferences.userRoot().node(getClass().getName());
	private JTextField jnodes;
	private JTextField jedges;
	private JTextField jgml;
	private JTextField jxml;
	
	public static final String OK_BUTTON_ACTION_COMMAND = "okButtonActionCommand";
	public static final String CANCEL_BUTTON_ACTION_COMMAND = "cancelButtonActionCommand";
	protected JButton okButton;
	protected JButton cancelButton;
	protected String LAST_USED_FOLDER ="LAST_USED_FOLDER";
			
	private ParamsReceiver rec;
	
	
	public NewProjectOpGui() {
		super(Workbench.getInstance().getMainFrame());
		initComponents();
	}
	
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		rec = arg0;
		setTitle(arg1.getName());
		pack();
		Utilities.centerOnOwner(this);
		setVisible(true);
	}

	
	public void initComponents() {
		
		java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JTabbedPane jTabbedPane1 = new javax.swing.JTabbedPane();
        javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        jgml = new javax.swing.JTextField();
        javax.swing.JButton bgml = new javax.swing.JButton(ICON_FILE_OPEN);
        javax.swing.JPanel jPanel5 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        jxml = new javax.swing.JTextField();
        JButton bxml = new javax.swing.JButton(ICON_FILE_OPEN);
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        jnodes = new javax.swing.JTextField();
        javax.swing.JButton bnodes = new javax.swing.JButton(ICON_FILE_OPEN);
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        jedges = new javax.swing.JTextField();
        javax.swing.JButton bedges = new javax.swing.JButton(ICON_FILE_OPEN);

        this.getContentPane().setLayout(new javax.swing.BoxLayout(this.getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("File: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 10;
        jPanel4.add(jLabel4, gridBagConstraints);

        jgml.setPreferredSize(new java.awt.Dimension(200, 20));
        bgml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFileChooser chooser = new JFileChooser(prefs.get(LAST_USED_FOLDER,
            			new File(".").getAbsolutePath()));
                
                FileNameExtensionFilter filter = new FileNameExtensionFilter("GML file",
                    "gml");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   jgml.setText(chooser.getSelectedFile().getAbsolutePath());
                   prefs.put(LAST_USED_FOLDER, chooser.getSelectedFile().getParent());
                }
            }
        });
        jPanel4.add(jgml, new java.awt.GridBagConstraints());

        
        jPanel4.add(bgml, new java.awt.GridBagConstraints());

        jTabbedPane1.addTab("GML", jPanel4);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("File: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 10;
        jPanel5.add(jLabel5, gridBagConstraints);

        jxml.setPreferredSize(new java.awt.Dimension(200, 20));
        bxml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFileChooser chooser = new JFileChooser(prefs.get(LAST_USED_FOLDER,
            			new File(".").getAbsolutePath()));
                
                FileNameExtensionFilter filter = new FileNameExtensionFilter("GraphML file",
                    "graphml");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   jxml.setText(chooser.getSelectedFile().getAbsolutePath());
                   prefs.put(LAST_USED_FOLDER, chooser.getSelectedFile().getParent());
                }
            }
        });
        jPanel5.add(jxml, new java.awt.GridBagConstraints());

    
        jPanel5.add(bxml, new java.awt.GridBagConstraints());

        jTabbedPane1.addTab("GraphML", jPanel5);

        java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
        jPanel1Layout.columnWidths = new int[] {0, 0, 0, 0, 0};
        jPanel1Layout.rowHeights = new int[] {0, 3, 0};
        jPanel1.setLayout(jPanel1Layout);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Nodes File: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 10;
        jPanel1.add(jLabel3, gridBagConstraints);

        jnodes.setPreferredSize(new java.awt.Dimension(200, 20));
        bnodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFileChooser chooser = new JFileChooser(prefs.get(LAST_USED_FOLDER,
            			new File(".").getAbsolutePath()));
                
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Brite nodes file",
                    "nodes");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   jnodes.setText(chooser.getSelectedFile().getAbsolutePath());
                   prefs.put(LAST_USED_FOLDER, chooser.getSelectedFile().getParent());
                }
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jnodes, gridBagConstraints);

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel1.add(bnodes, gridBagConstraints);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Edges  File: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 10;
        jPanel1.add(jLabel6, gridBagConstraints);

        jedges.setPreferredSize(new java.awt.Dimension(200, 20));
        bedges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFileChooser chooser = new JFileChooser(prefs.get(LAST_USED_FOLDER,
            			new File(".").getAbsolutePath()));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Brite edges file",
                    "edges");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(getParent());
                
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   jedges.setText(chooser.getSelectedFile().getAbsolutePath());
                   prefs.put(LAST_USED_FOLDER, chooser.getSelectedFile().getParent());
                }
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel1.add(jedges, gridBagConstraints);

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        jPanel1.add(bedges, gridBagConstraints);

        jTabbedPane1.addTab("Brite", jPanel1);


		////////////////
		this.add(jTabbedPane1);
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
		
		this.add(buttonPanel);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(400,170));
		pack();
		this.setResizable(false);
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
				if (jgml.getText().trim().equals("") &&
				    jxml.getText().trim().equals("") &&
				    (jnodes.getText().trim().equals("") ||
				    jedges.getText().trim().equals("")))
						Workbench.getInstance().warn("Missing file selection.");
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

	
	

	private void termination() {
		
		File f1 = new File(""),f2 =new File(""),f3=new File(""),f4=new File("");
		if(jnodes.getText().trim().length()>0)
			f1=new File(jnodes.getText());
		if(jedges.getText().trim().length()>0)
			f2=new File(jedges.getText());
		if(jgml.getText().trim().length()>0)
			f3=new File(jgml.getText());
		if(jxml.getText().trim().length()>0)
			f4=new File(jxml.getText());
		
		rec.paramsIntroduced(new ParamSpec[] {
				new ParamSpec("FileNodes", File.class, f1, null),
				new ParamSpec("FileEdges", File.class, f2, null),	
				new ParamSpec("GML"      , File.class, f3, null),
				new ParamSpec("GraphML"  , File.class, f4, null)
		});
		
	}


	@Override
	public void finish() {
		setVisible(false);
		dispose();
	}
}
