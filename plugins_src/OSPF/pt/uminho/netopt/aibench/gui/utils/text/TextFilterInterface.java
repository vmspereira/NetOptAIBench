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

import javax.swing.text.*;

public interface TextFilterInterface
{

    public abstract DocumentFilter getDocumentFilter();


    public abstract NavigationFilter getNavigationFilter();


    public abstract String getString();

    public abstract void setString(String s);

    public abstract void a(javax.swing.text.DocumentFilter.FilterBypass filterbypass, int i, String s, AttributeSet attributeset)
        throws BadLocationException;

    public abstract void a(javax.swing.text.DocumentFilter.FilterBypass filterbypass, int i, int j, String s, AttributeSet attributeset)
        throws BadLocationException;

    public abstract void a(javax.swing.text.DocumentFilter.FilterBypass filterbypass, int i, int j)
        throws BadLocationException;

    public abstract void a(javax.swing.text.NavigationFilter.FilterBypass filterbypass, int i, javax.swing.text.Position.Bias bias);

    public abstract void b(javax.swing.text.NavigationFilter.FilterBypass filterbypass, int i, javax.swing.text.Position.Bias bias);
}
