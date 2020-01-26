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

public class NumberText
    implements TextFilterInterface
{

    public NumberText(String s, String s1, String s2, JTextComponent jtextcomponent, int i, boolean flag)
    {
        b_java_lang_StringBuffer_fld = new StringBuffer();
        a_java_lang_StringBuffer_fld = new StringBuffer();
        b_java_lang_String_fld = null;
        a_int_fld = 0;
        b_char_array1d_fld = s.toCharArray();
        a_char_array1d_fld = s1.toCharArray();
        c = s2;
        a_javax_swing_text_JTextComponent_fld = jtextcomponent;
        b_int_fld = i;
        a_pb_fld = new NumberDocumentFilter(this);
        a_yz_fld = new NumberNavigationFilter(this);
        a_boolean_fld = flag;
    }

    public void a(long l)
    {
        a_long_fld = l;
    }

    public void a(String s, String s1, boolean flag)
    {
        a_java_lang_String_fld = s;
        b_java_lang_String_fld = s1;
        b_boolean_fld = flag;
    }

    public boolean a()
    {
        return b_java_lang_String_fld != null;
    }

    public int a_int()
    {
        return a() ? b_java_lang_String_fld.length() : 0;
    }

    /*
		Return the TextComponent DocumentFilter
    */
    public DocumentFilter getDocumentFilter()
    {
        return a_pb_fld;
    }

    /*
		Return the TextComponent NavigationFilter
    */
    public NavigationFilter getNavigationFilter()
    {
        return a_yz_fld;
    }

    public String getString()
    {
        return (b_java_lang_String_fld == null || !b_boolean_fld ? "" : b_java_lang_String_fld) + a_java_lang_StringBuffer_fld.toString();
    }

    public String a(int i, int j)
    {
        int k = b_java_lang_StringBuffer_fld.length() - i;
        k = k >= 0 ? k : 0;
        int l = b_java_lang_StringBuffer_fld.length() - j;
        l = l >= 0 ? l : 0;
        int i1 = a_int(k);
        int j1 = a_int(l);
        int k1 = a_java_lang_StringBuffer_fld.length() - i1;
        k1 = k1 >= 0 ? k1 : 0;
        int l1 = a_java_lang_StringBuffer_fld.length() - j1;
        l1 = l1 >= 0 ? l1 : 0;
        if(l1 > k1)
        {
            l1 = l1 >= a_java_lang_StringBuffer_fld.length() ? a_java_lang_StringBuffer_fld.length() : l1;
            return (b_java_lang_String_fld == null || k1 != 0 || !b_boolean_fld ? "" : b_java_lang_String_fld) + a_java_lang_StringBuffer_fld.substring(k1, l1);
        } else
        {
            return "";
        }
    }

    public void setString(String s)
    {
        a_javax_swing_text_JTextComponent_fld.setText(s);
    }

    public void a(int i)
    {
        b_int_fld = i;
    }

    private int a_int(int i)
    {
        int j = 0;
        for(int k = b_char_array1d_fld.length - 1; b_char_array1d_fld.length - k <= i; k--)
            if(b_char_array1d_fld[k] == '#')
                j++;

        return j;
    }

    public void a(javax.swing.text.DocumentFilter.FilterBypass filterbypass, int i, String s, AttributeSet attributeset)
        throws BadLocationException
    {
        if(b_java_lang_String_fld != null)
        {
            if(a_java_lang_String_fld.equals(s) && a_java_lang_StringBuffer_fld.length() > 0)
            {
                b_boolean_fld = !b_boolean_fld;
                a(filterbypass);
                a_javax_swing_text_JTextComponent_fld.setCaretPosition(i + (i == 0 ? 0 : b_boolean_fld ? 1 : -1));
                return;
            }
            if(a_java_lang_String_fld.equals(s))
                b_boolean_fld = false;
            else
            if(s != null && s.indexOf(a_java_lang_String_fld) == 0)
                s = s.substring(a_java_lang_String_fld.length(), s.length());
        }
        if(a_java_lang_StringBuffer_fld.length() + s.length() > b_int_fld)
            return;
        char ac[] = s.toCharArray();
        boolean flag = true;
        int j = 0;
        do
        {
            if(j >= ac.length)
                break;
            char c1 = ac[j];
            if(!Character.isDigit(c1))
            {
                flag = false;
                break;
            }
            j++;
        } while(true);
        if(flag)
        {
            int k = i;
            int l = b_java_lang_StringBuffer_fld.length() - k;
            l = l >= 0 ? l : 0;
            int i1 = a_int(l);
            int j1 = a_java_lang_StringBuffer_fld.length() - i1;
            j1 = j1 >= 0 ? j1 : 0;
            String s1 = a_java_lang_StringBuffer_fld.toString();
            a_java_lang_StringBuffer_fld.insert(j1, s);
            if(a_long_fld > 0L && a_java_lang_StringBuffer_fld.length() > 0 && Long.parseLong(a_java_lang_StringBuffer_fld.toString()) > a_long_fld)
                a_java_lang_StringBuffer_fld = new StringBuffer(s1);
            a(filterbypass);
            a_javax_swing_text_JTextComponent_fld.setCaretPosition(b_java_lang_StringBuffer_fld.length() - l);
        }
    }

    public void a(javax.swing.text.DocumentFilter.FilterBypass filterbypass, int i, int j, String s, AttributeSet attributeset)
        throws BadLocationException
    {
        boolean flag = b_boolean_fld;
        a(filterbypass, i, j);
        if(a_java_lang_StringBuffer_fld.length() == 0 && s != null && b_java_lang_String_fld != null && !s.equals(b_java_lang_String_fld))
        {
            if(s.indexOf(b_java_lang_String_fld) == 0)
                b_boolean_fld = true;
            else
                b_boolean_fld = false;
        } else
        {
            b_boolean_fld = flag;
        }
        
        try {
            a(filterbypass, i + j, s, attributeset);
        }
        catch ( java.lang.IllegalArgumentException e ) {
        }

    }

    public void a(javax.swing.text.DocumentFilter.FilterBypass filterbypass, int i, int j)
        throws BadLocationException
    {
        int k = b_java_lang_StringBuffer_fld.length() - i;
        int l = b_java_lang_StringBuffer_fld.length() - (i + j);
        int i1 = a_int(k);
        int j1 = a_int(l);
        int k1 = a_java_lang_StringBuffer_fld.length() - i1;
        int l1 = a_java_lang_StringBuffer_fld.length() - j1;
        k1 = k1 >= 0 ? k1 : 0;
        l1 = l1 >= 0 ? l1 : 0;
        if(j > 0 && k1 == l1)
        {
            l1++;
            if(--l < 0)
                l = 0;
        }
        a_java_lang_StringBuffer_fld.delete(k1, l1);
        if(a_java_lang_StringBuffer_fld.length() == 0)
            b_boolean_fld = false;
        a(filterbypass);
        a_javax_swing_text_JTextComponent_fld.setCaretPosition(b_java_lang_StringBuffer_fld.length() - l >= 0 ? b_java_lang_StringBuffer_fld.length() - l : 0);
    }

    public void a(javax.swing.text.DocumentFilter.FilterBypass filterbypass)
    {
        if(!a_boolean_fld)
            try
            {
                if(a_java_lang_StringBuffer_fld.length() > 0)
                {
                    long l = Long.parseLong(a_java_lang_StringBuffer_fld.toString());
                    a_java_lang_StringBuffer_fld = new StringBuffer(Long.toString(l));
                }
            }
            catch(NumberFormatException numberformatexception)
            {
                a_java_lang_StringBuffer_fld = new StringBuffer("");
//                numberformatexception.printStackTrace();
            }
        char ac[] = a_java_lang_StringBuffer_fld.toString().toCharArray();
        if(ac.length == 0)
        {
            b_java_lang_StringBuffer_fld = new StringBuffer(c);
        } else
        {
            b_java_lang_StringBuffer_fld = new StringBuffer();
            int i = ac.length - 1;
            int j = b_char_array1d_fld.length - 1;
            for(int k = a_char_array1d_fld.length - 1; j >= 0; k--)
            {
                if(i >= 0)
                {
                    if(b_char_array1d_fld[j] == '#')
                    {
                        b_java_lang_StringBuffer_fld.insert(0, ac[i]);
                        i--;
                    } else
                    {
                        b_java_lang_StringBuffer_fld.insert(0, b_char_array1d_fld[j]);
                    }
                } else
                if(k >= 0)
                    b_java_lang_StringBuffer_fld.insert(0, a_char_array1d_fld[k]);
                j--;
            }

        }
        if(b_java_lang_String_fld != null && b_boolean_fld)
            b_java_lang_StringBuffer_fld.insert(0, b_java_lang_String_fld);
        try
        {
            filterbypass.replace(0, filterbypass.getDocument().getLength(), b_java_lang_StringBuffer_fld.toString(), null);
        }
        catch(BadLocationException badlocationexception)
        {
            badlocationexception.printStackTrace();
        }
    }

    public void a(javax.swing.text.NavigationFilter.FilterBypass filterbypass, int i, javax.swing.text.Position.Bias bias)
    {
        if(i == 0 || b_char_array1d_fld[b_char_array1d_fld.length - (b_java_lang_StringBuffer_fld.length() - i) - 1] == '#')
        {
            a_int_fld = i;
            filterbypass.setDot(i, bias);
        } else
        if(i > 0)
        {
            if(i <= a_int_fld)
            {
                a_int_fld = i;
                a(filterbypass, i - 1, bias);
            } else
            {
                a_int_fld = i;
                if(i < b_java_lang_StringBuffer_fld.length() && b_char_array1d_fld[b_char_array1d_fld.length - (b_java_lang_StringBuffer_fld.length() - i)] == '#')
                    filterbypass.setDot(i + 1, bias);
                else
                    a(filterbypass, i - 1, bias);
            }
        } else
        {
            filterbypass.setDot(0, bias);
        }
    }

    public void b(javax.swing.text.NavigationFilter.FilterBypass filterbypass, int i, javax.swing.text.Position.Bias bias)
    {
        filterbypass.moveDot(i, bias);
    }

   

    public char[] b_char_array1d_fld;
    public char[] a_char_array1d_fld;
    public String c;
    public int b_int_fld;
    public StringBuffer b_java_lang_StringBuffer_fld;
    public StringBuffer a_java_lang_StringBuffer_fld;
    public JTextComponent a_javax_swing_text_JTextComponent_fld;
    public boolean a_boolean_fld;
    public long a_long_fld;
    public boolean b_boolean_fld;
    public String a_java_lang_String_fld;
    public String b_java_lang_String_fld;
    public NumberDocumentFilter a_pb_fld;
    public NumberNavigationFilter a_yz_fld;
    private int a_int_fld;
}
