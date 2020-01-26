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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import pt.uminho.algoritmi.netopt.ospf.simulation.ResultSimul;
import pt.uminho.netopt.aibench.datatypes.ResultSimulType;

@SuppressWarnings("serial")
public class ResultSimulView extends JPanel {

	private ResultSimul result;
	private JLabel jLabel4;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel jPanel2;
	private JPanel Header;

	/** Creates new form NewJPanel */
	public ResultSimulView(ResultSimulType r) {
		this.result = r.getResults();
		initComponents();
	}

	private void initComponents() {

		setName("Form");
		setLayout(new java.awt.BorderLayout());
		this.setPreferredSize(new java.awt.Dimension(600, 600));
		this.setSize(600, 600);
		{
			Header = new JPanel();
			BoxLayout jPanel1Layout = new BoxLayout(Header,
					javax.swing.BoxLayout.Y_AXIS);
			this.add(Header, BorderLayout.NORTH);
			Header.setLayout(jPanel1Layout);
			Header.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.LOWERED));
			{
				jLabel1 = new JLabel();
				Header.add(jLabel1);
				jLabel1.setText("Simulation Results");
				jLabel1.setIcon(new ImageIcon(getClass().getClassLoader()
						.getResource("images/resultSim32.png")));
			}
			{
				jLabel2 = new JLabel();
				Header.add(jLabel2);
				jLabel2.setText("Demands file: ");
			}
			{
				jLabel3 = new JLabel();
				Header.add(jLabel3);
				jLabel3.setText("Delay Resquests file: ");
			}
			{
				jLabel4 = new JLabel();
				Header.add(jLabel4);
				jLabel4.setText("Weights file: ");
			}
		}
		{
			jPanel2 = new JPanel();
			this.add(jPanel2, BorderLayout.CENTER);
		}

	}// </editor-fold>

	// Variables declaration - do not modify
	// End of variables declaration

}
