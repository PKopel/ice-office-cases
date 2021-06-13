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

public class CaseAck implements java.lang.Cloneable,
                                java.io.Serializable
{
    public String caseID;

    public long expectedReplyTime;

    public CaseAck()
    {
        this.caseID = "";
    }

    public CaseAck(String caseID, long expectedReplyTime)
    {
        this.caseID = caseID;
        this.expectedReplyTime = expectedReplyTime;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CaseAck r = null;
        if(rhs instanceof CaseAck)
        {
            r = (CaseAck)rhs;
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
            if(this.expectedReplyTime != r.expectedReplyTime)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::CaseOffice::CaseAck");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, caseID);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, expectedReplyTime);
        return h_;
    }

    public CaseAck clone()
    {
        CaseAck c = null;
        try
        {
            c = (CaseAck)super.clone();
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
        ostr.writeLong(this.expectedReplyTime);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.caseID = istr.readString();
        this.expectedReplyTime = istr.readLong();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, CaseAck v)
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

    static public CaseAck ice_read(com.zeroc.Ice.InputStream istr)
    {
        CaseAck v = new CaseAck();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<CaseAck> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, CaseAck v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<CaseAck> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(CaseAck.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final CaseAck _nullMarshalValue = new CaseAck();

    /** @hidden */
    public static final long serialVersionUID = 3228044735436836898L;
}
