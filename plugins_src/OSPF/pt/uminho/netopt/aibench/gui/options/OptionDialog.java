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
package pt.uminho.netopt.aibench.gui.options;



import java.beans.BeanInfo;

import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.algoritmi.netopt.SystemConf;

import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

import java.util.ArrayList;
import java.util.Properties;



import java.awt.Rectangle;
import java.awt.event.WindowListener;


@SuppressWarnings("serial")
public class OptionDialog  extends JDialog implements InputGUI{



    public OptionDialog()
    {
        super(Workbench.getInstance().getMainFrame(), "NetOpt Configuration Options",true);
        _mainFrame = Workbench.getInstance().getMainFrame();
        this.setSize(new Dimension(700, 530));
        this.setResizable(false);
        
        _generalBean = new EAGeneralBean();
        _generalSheet = new PropertySheetPanel();
        _generalTab = new JPanel();
        
        initDialog();
        Rectangle screenRect = _mainFrame.getGraphicsConfiguration().getBounds();
	    setLocation(
        screenRect.x + screenRect.width/2 - getSize().width/2,
		screenRect.y + screenRect.height/2 -getSize().height/2);

        addWindowListener(
            new WindowListener(){
	             public void windowActivated(WindowEvent e){}
				 public void windowClosed(WindowEvent e){}
 				 public void windowClosing(WindowEvent e){
						close();
 				 }
 				 public void windowDeactivated(WindowEvent e){}
                 public void windowDeiconified(WindowEvent e){}
                 public void windowIconified(WindowEvent e){}
 				 public void windowOpened(WindowEvent e){}
        	}
 		);
        this.setVisible(true);
    }

    @SuppressWarnings("deprecation")
	private void initGeneralTab()
    {
        
    	ArrayList<LFRecoveryProperty> arraylist = new ArrayList<LFRecoveryProperty>();
        arraylist.add(new LFRecoveryProperty("TI-LFA",0));
        arraylist.add(new LFRecoveryProperty("Edge to Edge SP",1));
        arraylist.add(new LFRecoveryProperty("SALP",2));
        arraylist.add(new LFRecoveryProperty("SALP-LP",3));
        ComboBoxPropertyEditor comboboxpropertyeditor = new ComboBoxPropertyEditor();
        Object[] recoveryOptions=arraylist.toArray();
        comboboxpropertyeditor.setAvailableValues(recoveryOptions);
        _generalSheet.getEditorRegistry().registerEditor(pt.uminho.netopt.aibench.gui.options.LFRecoveryProperty.class, comboboxpropertyeditor);
              
        BeanInfo beaninfo = (BeanInfo)(new EAGeneralBeanBeanInfo());
        
        _generalBean.setMinWeight(SystemConf.getPropertyInt("ospf.minweight", 1));
        _generalBean.setMaxWeight(SystemConf.getPropertyInt("ospf.maxweight", 20));
        _generalBean.setDefaultBandWidth(SystemConf.getPropertyDouble("topology.defaultBW",1000.0));
        _generalBean.setDefaultDelay(SystemConf.getPropertyDouble("topology.defaultDelay", 1.0));
        _generalBean.setDeftThreshold(SystemConf.getPropertyDouble("deft.threshold", 0.01));
        _generalBean.setDeftApplyThreshold(SystemConf.getPropertyBoolean("deft.applythreshold",false));
        _generalBean.setHybridSRPenaltyFactor(SystemConf.getPropertyDouble("sr.hybridpenalty",0.0));
        _generalBean.setPDefault(SystemConf.getPropertyDouble("pvalue.default", 1.0));
        _generalBean.setPMax(SystemConf.getPropertyDouble("pvalue.max", 10.0));
        _generalBean.setPMin(SystemConf.getPropertyDouble("pvalue.min", 0.1));
        _generalBean.setPIntScaller(SystemConf.getPropertyInt("pvalue.divider", 100));
        _generalBean.setNumberParallelThreads(SystemConf.getPropertyInt("threads.number", 4));
        _generalBean.setTwoPointCrossover(SystemConf.getPropertyDouble("ea.twoPointCrossover", 0.25));
        _generalBean.setUniformCrossover(SystemConf.getPropertyDouble("ea.uniformCrossover", 0.25));
        _generalBean.setRandomMutation(SystemConf.getPropertyDouble("ea.randomMutation", 0.25));
        _generalBean.setIncrementalMutation(SystemConf.getPropertyDouble("ea.incrementalMutation", 0.25));
      
        int recovery=SystemConf.getPropertyInt("srsimulator.lf", 0);
        _generalBean.setLFRecoveryProperty((LFRecoveryProperty)recoveryOptions[recovery]);
        _generalBean.setSRNodepIterations(SystemConf.getPropertyInt("srsimulator.piterations", 100));
        
        _generalSheet.setProperties(beaninfo.getPropertyDescriptors());
        _generalSheet.readFromObject(_generalBean);
         
        _generalSheet.setDescriptionVisible(true);
        _generalSheet.setToolBarVisible(false);
        _generalSheet.setMode(1);
        
        _generalTab.setLayout(LookAndFeelTweaks.createVerticalPercentLayout());
        _generalTab.add(_generalSheet, "*");

    }

    public void initDialog()
    {
        initGeneralTab();
        this.addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent windowevent)
            {

            }

        });
        _btnOk = new JButton("Save Configuration");
        _btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                onOk();
            }

        });
        _btnCancel = new JButton("Cancel");
        _btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                onCancel();
            }

        });
        JPanel jpanel = new JPanel(new FlowLayout(2));
        jpanel.add(_btnOk);
        jpanel.add(_btnCancel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(_generalTab, "Center");
        getContentPane().add(jpanel, "South");
    }

	private void onOk()
    {

        _generalSheet.writeToObject(_generalBean);
        Properties properties = SystemConf.getProperties();
        properties.setProperty("ospf.minweight",String.valueOf(_generalBean.getMinWeight()));
        properties.setProperty("ospf.maxweight",String.valueOf(_generalBean.getMaxWeight()));
        properties.setProperty("topology.defaultBW",String.valueOf(_generalBean.getDefaultBandWidth()));
        properties.setProperty("topology.defaultDelay",String.valueOf(_generalBean.getDefaultDelay()));
        properties.setProperty("deft.threshold",String.valueOf(_generalBean.getDeftThreshold()));
        properties.setProperty("deft.applythreshold",String.valueOf(_generalBean.getDeftApplyThreshold()));
        properties.setProperty("sr.hybridpenalty",String.valueOf(_generalBean.getHybridSRPenaltyFactor()));
        properties.setProperty("pvalue.default",String.valueOf(_generalBean.getPDefault()));
        properties.setProperty("pvalue.min", String.valueOf(_generalBean.getPMin()));
        properties.setProperty("pvalue.max", String.valueOf(_generalBean.getPMax()));
        properties.setProperty("pvalue.divider", String.valueOf(_generalBean.getPIntScaller()));
        properties.setProperty("threads.number", String.valueOf(_generalBean.getNumberParallelThreads()));
        properties.setProperty("ea.twoPointCrossover", String.valueOf(_generalBean.getTwoPointCrossover()));
        properties.setProperty("ea.uniformCrossover", String.valueOf(_generalBean.getUniformCrossover()));
        properties.setProperty("ea.randomMutation", String.valueOf(_generalBean.getRandomMutation()));
        properties.setProperty("ea.incrementalMutation", String.valueOf(_generalBean.getIncrementalMutation()));
        properties.setProperty("srsimulator.lf", String.valueOf(_generalBean.getLFRecoveryProperty().getRecoveryValue()));
        properties.setProperty("srsimulator.piterations", String.valueOf(_generalBean.getSRNodepIterations()));
        
        SystemConf.setProperties(properties);
        close();
    }

    private void onCancel()
    {
        close();
    }

    private void close(){
            dispose();
    }

    private pt.uminho.netopt.aibench.gui.options.EAGeneralBean _generalBean;
    private PropertySheetPanel _generalSheet;
    private JPanel _generalTab;
    private JButton _btnOk;
    private JButton _btnCancel;
    private JFrame _mainFrame;

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

}
