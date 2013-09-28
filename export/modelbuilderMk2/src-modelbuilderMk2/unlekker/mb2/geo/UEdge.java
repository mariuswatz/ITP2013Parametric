/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;

import unlekker.mb2.util.UMbMk2;

public class UEdge extends UMbMk2 {
  public static int globalID=0;

  public UGeo parent;
  int vID[];
  public UVertex v[];
  public int ID;  

  public ArrayList<UFace> faces;
  
  public UEdge() {
    ID=globalID++;
    v=new UVertex[2];
  }

  public UEdge(UVertex v1, UVertex v2) {
    this();
    
    set(v1,v2);
  }

  public UEdge(UGeo model, UVertex v1, UVertex v2) {
    this();
    if(model!=null) parent=model;
    
    set(v1,v2);
  }

  public UEdge set(int id1,int id2) { 
    if(vID==null) vID=new int[2];
    vID[0]=(id1<id2 ? id2 : id1);
    vID[1]=(id1<id2 ? id2 : id1);
    
    return this;    
  }

  public UEdge set(UVertex v1, UVertex v2) {
    if(v==null) v=new UVertex[2];
    
    if(parent!=null) {
      vID=parent.getVID(v,vID);
    }
    else {
      v[0]=(v1.ID<v2.ID ? v1 : v2);
      v[1]=(v1.ID<v2.ID ? v2 : v1);
    }
    
    return this;
  }

  public UVertex[] getV(boolean force) {
    if(force) v=null;
    return getV();
  }

  public UVertex[] getV() {
    if(parent==null || v!=null) return v;
    
    v=parent.getVByID(vID,v);
    return v;
  }

  public boolean equals(UEdge edge) {
    if(vID!=null) return (edge.vID[0]==vID[0] && edge.vID[1]==vID[1]);
    return equals(edge.v[0], edge.v[1]);
  }
  
  public boolean equals(int id1,int id2) {
    if(id1>id2) {
      return id2==vID[0] && id1==vID[1];
    }
    
    return id1==vID[0] && id2==vID[1];
  }

  public boolean equals(UVertex v1,UVertex v2) {
    if(v1.ID>v2.ID) {
      UVertex tmp=v2;
      v2=v1;
      v1=tmp;
    }
    
    return (v1.equals(v[0]) && v2.equals(v[1]));
  }

}
