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
package pt.uminho.netopt.aibench.gui.utils.table;

import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;



public class PercentageRenderer implements TableCellRenderer {

	private TableCellRenderer delegate;
	  private NumberFormat formatter;

	  public PercentageRenderer(TableCellRenderer defaultRenderer,int fdigits)
	  {
	    this.delegate = defaultRenderer;
	    formatter = NumberFormat.getPercentInstance();
	    formatter.setMinimumFractionDigits(fdigits);
	    formatter.setMaximumFractionDigits(fdigits);
	  }

	  public Component getTableCellRendererComponent(JTable table, Object value, 
	                           boolean isSelected, boolean hasFocus, int row, int column) 
	  {
	    Component c = delegate.getTableCellRendererComponent(table, value, isSelected, 
	                                                                hasFocus, row, column);
	    String s = formatter.format((Double)value);
	    if (c instanceof JLabel){
	      ((JLabel)c).setText(s);
	      ((JLabel)c).setHorizontalAlignment(JLabel.RIGHT);
	    }
	    return c;
	  }
}
