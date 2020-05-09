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

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

public class NumberTextField extends JTextField
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NumberTextField(){
		this("");
    }

    public NumberTextField(String classifier)
    {
        byte byte0 = 5;//12;
        a = new NumberText("#.###.###.###.###.###,##"+classifier, "0,00"+classifier, classifier, this, byte0,false);
        ((AbstractDocument)getDocument()).setDocumentFilter(a.getDocumentFilter());
        setNavigationFilter(a.getNavigationFilter());
        setColumns(byte0);
        setHorizontalAlignment(4);
        setMinimumSize(getPreferredSize());
    }



	public NumberTextField(int i){
		this("",i);
    }

    public NumberTextField(String classifier,int i)
    {
		if(i>=0)
            decimal=i;
        StringBuffer stringbuffer = new StringBuffer("#.###.###.###.###.### ");
        StringBuffer stringbuffer1 = new StringBuffer("");
        if(i > 0)
        {

            stringbuffer.append(",");
            stringbuffer1.append("0,");
            do
            {
                stringbuffer.append("#");
                stringbuffer1.append("0");
            } while(--i > 0);
        }
        stringbuffer.append(classifier);
        stringbuffer1.append(classifier);
        a = new NumberText(stringbuffer.toString(), stringbuffer1.toString(),classifier, this, stringbuffer.length() - 2, false);
        ((AbstractDocument)getDocument()).setDocumentFilter(a.getDocumentFilter());
        setNavigationFilter(a.getNavigationFilter());
        setColumns(stringbuffer.length() - 1);
        setHorizontalAlignment(4);
        setMinimumSize(getPreferredSize());
    }



    public void a(boolean flag)
    {
        a.a("-", "-", flag);
        setColumns(getColumns());
    }

    public void setColumns(int i)
    {
        a.a(i);
        super.setColumns(a(i));
        setMinimumSize(getPreferredSize());
    }


    private int a(int i)
    {
        return i + (a.a() ? a.a_int() : 0);
    }

	public Double getDouble()
    {
        try{
			if(decimal>0){
				long l=Long.parseLong(a.getString());
				Double d=new Double((double)l/Math.pow(10d,decimal));
				return d;
            }
            else
                return new Double(a.getString());

        }
        catch(Exception e){
			return null;
        }
    }

    public Long getLong(){
        try{
			return new Long(a.getString());
        }
        catch(Exception e){
			return null;
        }
    }

    public void setDouble(Double d){
        try{
			long l;
			if(decimal>0){
				l=(long)(d.doubleValue()*Math.pow(10d,decimal));
			}
			else{
				l=(long)d.doubleValue();
			}
			setString(""+l);
        }
        catch(Exception e){}
    }


    public String getString(){
		return getDouble().toString();
    }


    public void copy()
    {
        JTextField jtextfield = new JTextField(a.a(getSelectionStart(), getSelectionEnd()));
        jtextfield.selectAll();
        jtextfield.copy();
    }

    public void setString(String s)
    {
        a.setString(s);
    }


    public Object getCellValue()
	{
	  try{
		if(!getString().equals(""))
			return new Long(getString());
        return null;
      }
      catch(NumberFormatException numberformatexception){
		return null;
      }
	}


    private int decimal=2;

    public NumberText a;
}
