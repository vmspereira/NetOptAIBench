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
package pt.uminho.netopt.aibench.gui.utils.text;

import javax.swing.text.NavigationFilter;


public class NumberNavigationFilter extends NavigationFilter
{

    public NumberNavigationFilter(TextFilterInterface ke1)
    {
        a = ke1;
    }

    public void setDot(javax.swing.text.NavigationFilter.FilterBypass filterbypass, int i, javax.swing.text.Position.Bias bias)
    {
        a.a(filterbypass, i, bias);
    }

    public void moveDot(javax.swing.text.NavigationFilter.FilterBypass filterbypass, int i, javax.swing.text.Position.Bias bias)
    {
        a.b(filterbypass, i, bias);
    }

    public TextFilterInterface a;
}
