/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.young.thrift.module;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2018-12-06")
public class Push2 implements org.apache.thrift.TBase<Push2, Push2._Fields>, java.io.Serializable, Cloneable, Comparable<Push2> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Push2");

  private static final org.apache.thrift.protocol.TField CID_FIELD_DESC = new org.apache.thrift.protocol.TField("cid", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField OID_FIELD_DESC = new org.apache.thrift.protocol.TField("oid", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new Push2StandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new Push2TupleSchemeFactory();

  public String cid; // required
  public String oid; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CID((short)1, "cid"),
    OID((short)2, "oid");

    private static final java.util.Map<String, _Fields> byName = new java.util.HashMap<String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // CID
          return CID;
        case 2: // OID
          return OID;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CID, new org.apache.thrift.meta_data.FieldMetaData("cid", org.apache.thrift.TFieldRequirementType.REQUIRED,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.OID, new org.apache.thrift.meta_data.FieldMetaData("oid", org.apache.thrift.TFieldRequirementType.REQUIRED,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Push2.class, metaDataMap);
  }

  public Push2() {
  }

  public Push2(
    String cid,
    String oid)
  {
    this();
    this.cid = cid;
    this.oid = oid;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Push2(Push2 other) {
    if (other.isSetCid()) {
      this.cid = other.cid;
    }
    if (other.isSetOid()) {
      this.oid = other.oid;
    }
  }

  public Push2 deepCopy() {
    return new Push2(this);
  }

  @Override
  public void clear() {
    this.cid = null;
    this.oid = null;
  }

  public String getCid() {
    return this.cid;
  }

  public Push2 setCid(String cid) {
    this.cid = cid;
    return this;
  }

  public void unsetCid() {
    this.cid = null;
  }

  /** Returns true if field cid is set (has been assigned a value) and false otherwise */
  public boolean isSetCid() {
    return this.cid != null;
  }

  public void setCidIsSet(boolean value) {
    if (!value) {
      this.cid = null;
    }
  }

  public String getOid() {
    return this.oid;
  }

  public Push2 setOid(String oid) {
    this.oid = oid;
    return this;
  }

  public void unsetOid() {
    this.oid = null;
  }

  /** Returns true if field oid is set (has been assigned a value) and false otherwise */
  public boolean isSetOid() {
    return this.oid != null;
  }

  public void setOidIsSet(boolean value) {
    if (!value) {
      this.oid = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case CID:
      if (value == null) {
        unsetCid();
      } else {
        setCid((String)value);
      }
      break;

    case OID:
      if (value == null) {
        unsetOid();
      } else {
        setOid((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case CID:
      return getCid();

    case OID:
      return getOid();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case CID:
      return isSetCid();
    case OID:
      return isSetOid();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Push2)
      return this.equals((Push2)that);
    return false;
  }

  public boolean equals(Push2 that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_cid = true && this.isSetCid();
    boolean that_present_cid = true && that.isSetCid();
    if (this_present_cid || that_present_cid) {
      if (!(this_present_cid && that_present_cid))
        return false;
      if (!this.cid.equals(that.cid))
        return false;
    }

    boolean this_present_oid = true && this.isSetOid();
    boolean that_present_oid = true && that.isSetOid();
    if (this_present_oid || that_present_oid) {
      if (!(this_present_oid && that_present_oid))
        return false;
      if (!this.oid.equals(that.oid))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetCid()) ? 131071 : 524287);
    if (isSetCid())
      hashCode = hashCode * 8191 + cid.hashCode();

    hashCode = hashCode * 8191 + ((isSetOid()) ? 131071 : 524287);
    if (isSetOid())
      hashCode = hashCode * 8191 + oid.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(Push2 other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetCid()).compareTo(other.isSetCid());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCid()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cid, other.cid);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetOid()).compareTo(other.isSetOid());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOid()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.oid, other.oid);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Push2(");
    boolean first = true;

    sb.append("cid:");
    if (this.cid == null) {
      sb.append("null");
    } else {
      sb.append(this.cid);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("oid:");
    if (this.oid == null) {
      sb.append("null");
    } else {
      sb.append(this.oid);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (cid == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'cid' was not present! Struct: " + toString());
    }
    if (oid == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'oid' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class Push2StandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public Push2StandardScheme getScheme() {
      return new Push2StandardScheme();
    }
  }

  private static class Push2StandardScheme extends org.apache.thrift.scheme.StandardScheme<Push2> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Push2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.cid = iprot.readString();
              struct.setCidIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // OID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.oid = iprot.readString();
              struct.setOidIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Push2 struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.cid != null) {
        oprot.writeFieldBegin(CID_FIELD_DESC);
        oprot.writeString(struct.cid);
        oprot.writeFieldEnd();
      }
      if (struct.oid != null) {
        oprot.writeFieldBegin(OID_FIELD_DESC);
        oprot.writeString(struct.oid);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class Push2TupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public Push2TupleScheme getScheme() {
      return new Push2TupleScheme();
    }
  }

  private static class Push2TupleScheme extends org.apache.thrift.scheme.TupleScheme<Push2> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Push2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.cid);
      oprot.writeString(struct.oid);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Push2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.cid = iprot.readString();
      struct.setCidIsSet(true);
      struct.oid = iprot.readString();
      struct.setOidIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

