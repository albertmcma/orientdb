/* Generated By:JJTree: Do not edit this line. OIdentifier.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class OIdentifier extends SimpleNode {

  protected String value;

  public OIdentifier(int id) {
    super(id);
  }

  public OIdentifier(OrientSql p, int id) {
    super(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(OrientSqlVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString(String prefix) {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}
/* JavaCC - OriginalChecksum=691a2eb5096f7b5e634b2ca8ac2ded3a (do not edit this line) */
