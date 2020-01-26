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
package pt.uminho.netopt.jung;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetEdge;


@SuppressWarnings("serial")
public class EdgePropertyDialog extends JPanel {
	
	
    
    
    public EdgePropertyDialog(NetEdge edge) {
       
        initComponents();
        this.bwFormattedTextField.setValue(edge.getBandwidth() );
        this.dlFormattedTextField.setValue(edge.getDelay());
        this.weightFormattedTextField.setValue(edge.getLength());
    }
    
                          
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        bwFormattedTextField = new javax.swing.JFormattedTextField();
        dlFormattedTextField = new javax.swing.JFormattedTextField();
        weightFormattedTextField = new javax.swing.JFormattedTextField();

        

        jLabel1.setText("BandWidth (Mbits/s):");

        jLabel2.setText("Delay:");

        jLabel3.setText("Lenght:");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
      
        hGroup.addGroup(layout.createParallelGroup().
                 addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel3));
        hGroup.addGroup(layout.createParallelGroup().
                 addComponent(bwFormattedTextField).addComponent(dlFormattedTextField).addComponent(weightFormattedTextField));
        layout.setHorizontalGroup(hGroup);
        
        
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        
        
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
                 addComponent(jLabel1).addComponent(bwFormattedTextField));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
                 addComponent(jLabel2).addComponent(dlFormattedTextField));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
                addComponent(jLabel3).addComponent(weightFormattedTextField));
        layout.setVerticalGroup(vGroup);
        
    }// </editor-fold>                        


    public double getBw(){
    	return ((Number)bwFormattedTextField.getValue()).doubleValue();
    }
    
    public double getDelay(){
    	return ((Number)dlFormattedTextField.getValue()).doubleValue();
    } 
    
    public double getLength(){
    	return ((Number)weightFormattedTextField.getValue()).doubleValue();
    }
    
    
    // Variables declaration - do not modify                     
    private javax.swing.JFormattedTextField bwFormattedTextField;
    private javax.swing.JFormattedTextField dlFormattedTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JFormattedTextField weightFormattedTextField;
    // End of variables declaration                   
    
}
