package pt.uminho.netopt.aibench.gui.options;

import com.l2fprod.common.propertysheet.DefaultProperty;

public class LFRecoveryProperty extends DefaultProperty
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LFRecoveryProperty(String s,int v)
    {
        _lfrecovery = s;
        _recoveryValue =v;
        
    }

    public String getName()
    {
        return "LookAndFeel";
    }

    public String getDisplayName()
    {
        return "Link Failure Recovery";
    }

    public String getShortDescription()
    {
        return "Link Failure Recovery method. Default TI-LFA";
    }

    public Class getType()
    {
        return getClass();
    }

    public Object getValue()
    {
        return _lfrecovery;
    }

    public void setValue(Object obj)
    {
       if(obj instanceof String)
            _lfrecovery = (String)obj;
        else
        if(obj instanceof LFRecoveryProperty)
            _lfrecovery = (String)((LFRecoveryProperty)obj).getValue();
        else
            _lfrecovery = obj.toString();
    }

    public boolean isEditable()
    {
        return true;
    }

    public String getCategory()
    {
        return "Aparência";
    }

    public String toString()
    {
        return _lfrecovery;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof LFRecoveryProperty)
            return _lfrecovery.equals(obj.toString());
        else
            return false;
    }

    public int getRecoveryValue() {
		return _recoveryValue;
	}

	public void setRecoveryValue(int recoveryValue) {
		this._recoveryValue = recoveryValue;
	}

	private String _lfrecovery;
    private int _recoveryValue;
}

