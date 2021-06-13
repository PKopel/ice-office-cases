//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.5
//
// <auto-generated>
//
// Generated from file `CaseOffice.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package CaseOffice;

public class CaseResult implements java.lang.Cloneable,
                                   java.io.Serializable
{
    public String caseID;

    public String result;

    public CaseResult()
    {
        this.caseID = "";
        this.result = "";
    }

    public CaseResult(String caseID, String result)
    {
        this.caseID = caseID;
        this.result = result;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CaseResult r = null;
        if(rhs instanceof CaseResult)
        {
            r = (CaseResult)rhs;
        }

        if(r != null)
        {
            if(this.caseID != r.caseID)
            {
                if(this.caseID == null || r.caseID == null || !this.caseID.equals(r.caseID))
                {
                    return false;
                }
            }
            if(this.result != r.result)
            {
                if(this.result == null || r.result == null || !this.result.equals(r.result))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::CaseOffice::CaseResult");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, caseID);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, result);
        return h_;
    }

    public CaseResult clone()
    {
        CaseResult c = null;
        try
        {
            c = (CaseResult)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeString(this.caseID);
        ostr.writeString(this.result);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.caseID = istr.readString();
        this.result = istr.readString();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, CaseResult v)
    {
        if(v == null)
        {
            _nullMarshalValue.ice_writeMembers(ostr);
        }
        else
        {
            v.ice_writeMembers(ostr);
        }
    }

    static public CaseResult ice_read(com.zeroc.Ice.InputStream istr)
    {
        CaseResult v = new CaseResult();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<CaseResult> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, CaseResult v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<CaseResult> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(CaseResult.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final CaseResult _nullMarshalValue = new CaseResult();

    /** @hidden */
    public static final long serialVersionUID = -6927588182729363758L;
}