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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.uminho.netopt.aibench.datatypes.ResultOptimType;

@SuppressWarnings("serial")
public class ResultOptimView extends JPanel {

	ResultOptimType result;

	/** Creates new form NewJPanel */
	public ResultOptimView(ResultOptimType r) {
		this.result = r;
		initComponents();
	}

	private void initComponents() {

		setLayout(new java.awt.BorderLayout());
		JPanel header = new JPanel();
		BoxLayout jPanel1Layout = new BoxLayout(header,
				javax.swing.BoxLayout.Y_AXIS);
		this.add(header, BorderLayout.NORTH);
		
		header.setLayout(jPanel1Layout);

		JLabel jLabel1 = new JLabel();
		header.add(jLabel1);
		jLabel1.setText("Optimization Results");
		jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
				"images/resultOpt32.png")));
		JLabel jLabel2 = new JLabel();
		header.add(jLabel2);
		jLabel2.setText("Demands file: "+result.getDemands().getDemands().get(0).getFilename());
		try{
			JLabel jLabel3 = new JLabel();
			header.add(jLabel3);
			jLabel3.setText("Delay Resquests file: "+result.getDelayReqs().getFilename());
		}catch(Exception e){}
		try{
			JLabel jLabel3 = new JLabel();
			header.add(jLabel3);
			jLabel3.setText("Second Demands file: "+result.getDemands().getDemands().get(1).getFilename());
		}catch(Exception e){}
		try{
			JLabel jlabel5=new JLabel("Algorithm :"+result.getAlgorithm().toString());
			header.add(jlabel5);
		}catch(Exception e){}
		if (result.getInfo() != null) {
			JLabel jLabel4 = new JLabel();
			header.add(jLabel4);
			String obs = result.getInfo().replace("\n", "</li><li>");
			jLabel4.setText("<html><Strong>Parameters</Strong>:<ul><li>" + obs + "</li></ul></html>");
		}
		}

}
