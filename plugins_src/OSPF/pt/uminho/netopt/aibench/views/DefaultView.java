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
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DefaultView extends JPanel{
	
	JLabel ltitle;
	
	public DefaultView(String title){
		this(title, null);
	}
	
	public DefaultView(String title, Icon icon){
		setLayout(new java.awt.BorderLayout());
		JPanel ptitle = new JPanel();
		this.add(ptitle, BorderLayout.NORTH);
		BoxLayout jPanel1Layout = new BoxLayout(ptitle, javax.swing.BoxLayout.Y_AXIS);
		ptitle.setLayout(jPanel1Layout);
		ltitle = new JLabel();
		ltitle.setFont(new Font(ltitle.getFont().getName(), Font.BOLD, ltitle.getFont().getSize()+2));
		ltitle.setText(title);
		ltitle.setIcon(icon);
	}
	
	public void setIcon(Icon icon){
		ltitle.setIcon(icon);
	}
	

}
