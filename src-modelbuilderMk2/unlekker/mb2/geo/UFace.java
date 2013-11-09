/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.Arrays;

import unlekker.mb2.util.UMB;

/**
 * TODO  
 * - UFace.equals(Object o)
 * 
 * @author marius
 *
 */
public class UFace extends UMB  {
  public static int globalID=0;

  public UGeo parent;
  public int ID;  
  public int vID[];
  public UVertex v[];
  
  public int col;
  public UVertex normal,centroid;
  
  public UFace() {
    ID=globalID++;
    vID=new int[] {-1,-1,-1};
    col=Integer.MAX_VALUE;
  }

  public UFace(UFace v) {
    this();
    set(v);
  }

  public UFace(UVertex v1, UVertex v2, UVertex v3) {
    this(null,v1,v2,v3);
  }

  public UFace(UGeo model, UVertex v1, UVertex v2, UVertex v3) {
    this();
    if(model!=null) parent=model;
    
    set(v1,v2,v3);
    
//    log("F "+ID+" "+vID[0]+" "+vID[1]+" "+vID[2]);
  }

  public UFace(UGeo model, int[] ID) {
    this();
    parent=model;
    vID=new int[] {ID[0],ID[1],ID[2]};
  }

  public UFace set(UVertex vv[]) {
    if(parent!=null) {
      getVID(vv);
      getV();
    }
    else {
      if(v==null) v=new UVertex[vv.length];
      int cnt=0;
      for(UVertex vert:vv) v[cnt++]=vert;
    }
    return this;
  }

  public UFace set(UVertex v1, UVertex v2, UVertex v3) {
    if(parent!=null) {
      getVID(new UVertex[] {v1,v2,v3});
    }
    else {
      if(v==null) v=new UVertex[3];
      v[0]=v1;
      v[1]=v2;
      v[2]=v3;
    }
    
   
    return this;
  }

  public int[] getVID() {
    return getVID(v);    
  }

  private int[] getVID(UVertex vv[]) {
    vID=parent.getVID(vv);
    return vID;
  }

  public UFace drawNormal(float len) {
    return drawNormal(len,true);
  }

  public UFace drawNormal(float len,boolean centerOnly) {
    if(checkGraphicsSet()) {  
      UVertex n=normal();
      
      if(centerOnly) {
        ppush().ptranslate(centroid());
        g.line(0, 0, 0, n.x*len,n.y*len,n.z*len);
        ppop();
      }
      else {
        getV();
        for(UVertex vert:v) {
          ppush().ptranslate(vert);
          g.line(0, 0, 0, n.x*len,n.y*len,n.z*len);
          ppop();
        }

      }
    }

    return this;
  }

  public UFace draw() {
    if(checkGraphicsSet()) {
      if(col!=Integer.MAX_VALUE) g.fill(col);
      if(v==null) getV();
      g.beginShape(TRIANGLE);
      pvertex(v);
      g.endShape();
    }
    
    return this;
  }

  public UVertex[] getV(boolean force) {
    if(force) v=null;
    return getV();
  }

  public UVertex[] getMidV() {
    UVertex[] mid=new UVertex[3];
    getV();
    
    mid[0]=v[0].lerp(0.5f, v[1]);
    mid[1]=v[1].lerp(0.5f, v[2]);
    mid[2]=v[2].lerp(0.5f, v[0]);
    
    return mid;
  }

  public UVertex[] getV() {
    if(parent==null || v!=null) return v;
    
    if(v==null) v=new UVertex[vID.length];
 
    int id=0;    
    for(int vid:vID) v[id++]=parent.getVertex(vid);
    
    return v;
  }
  
  public UFace translate(UVertex v1) {    
    return translate(v1.x,v1.y,v1.z);    
  }

  public UFace translate(float mx,float my) {
    return translate(mx,my,0);
  }
  
  public UFace translate(float mx,float my,float mz) {
    for(UVertex vt:getV()) vt.add(mx,my,mz);
    return reset();
  }
  
  public UFace rotX(float deg) {
    for(UVertex vt:getV()) vt.rotX(deg);
    return reset();
  }

  public UFace rotY(float deg) {
    for(UVertex vt:getV()) vt.rotY(deg);
    return reset();
  }

  public UFace rotZ(float deg) {
    for(UVertex vt:getV()) vt.rotZ(deg);
    return reset();
  }


  public UFace scale(float m) {return scale(m,m,m);}

  public UFace scale(float mx,float my,float mz) {
    for(UVertex vt:getV()) vt.mult(mx,my,mz);
    return reset();
  }


  public UFace resetVertexID(UVertexList vl) {
    getV();
    int index=0;
    for(UVertex vv:v) {
      int id=vl.getVID(vv);
      if(id<0) id=vl.addID(vv);
      vID[index++]=id;
    }
    return reset();
  }
  
  public UFace copy() {
    return new UFace(this);
  }
    
  public UFace setParent(UGeo geo) {
    parent=geo;
    return this;
  }

  public UFace set(UFace v) {
    vID=new int[v.vID.length];
    int cnt=0;
    for(int i:v.vID) vID[cnt++]=i;
    
    col=v.col;
    parent=v.parent;
    
    if(v.normal!=null) {
      if(normal!=null) normal.set(v.normal.x,v.normal.y,v.normal.x);
    }
    
   return this;
  }

  public UFace setColor(int a) {
    col=a;
    return this;
  }

  public UFace setColor(float a,float b,float c) {
    return setColor(color(a,b,c));
  }

  public UVertex centroid() {
    if(centroid==null) {
      centroid=UVertex.centroid(getV());
    }
    
    return centroid;
  }

  public UFace reverse() {
    if(parent!=null) {
      vID=new int[] {vID[0],vID[2],vID[1]};
      v=null;
    }
    else {
      UVertex v2=v[2];
      v[2]=v[1];
      v[1]=v2;
    }
    
    if(normal!=null) {
      normal=null;
      normal();
    }
    return this;
  }

  /**
   * Returns face normal, with the option to force re-calculation  
   * @param force
   * @return
   */
  public UVertex normal(boolean force) {
    if(force) reset();    
    return normal();
  }

  /**
   * Resets internal data for this face, specifically {@see #normal}, {@see UFace#centroid} and {@see UFace#v} (provided {@see UFace#vID} is not null). 
   *
   * @return the u face
   */
  public UFace reset() {
    normal=null;
    if(parent!=null) v=null;
    centroid=null;
    return this;
  }

  public UVertex normal() {
    if(normal!=null) return normal;
        
    getV();
    
    normal=UVertex.cross(
        UVertex.delta(v[1],v[0]).norm(), 
        UVertex.delta(v[2],v[0]).norm());
    normal.norm();
    return normal;
  }
  
  public UVertex[] getMidEdges() {
    UVertex[] mid=new UVertex[3];
    getV();
    mid[0]=UVertex.lerp(0.5f, v[0], v[1]);
    mid[1]=UVertex.lerp(0.5f, v[1], v[2]);
    mid[2]=UVertex.lerp(0.5f, v[2], v[0]);
    
    return mid;
  }
  
  public boolean equals(Object o) {
    UFace of=(UFace)o;
    of.getV();
    if(parent!=null &&
        (of.parent!=null && parent==of.parent)) {
      return (vID[0]==of.vID[0] &&
          vID[1]==of.vID[1] && 
          vID[2]==of.vID[2]);
    }
    else {
      if(!v[0].equals(of.v[0])) return false;
      if(!v[1].equals(of.v[1])) return false;
      if(!v[2].equals(of.v[2])) return false;
    }
    
    return true;
  }

  public static boolean check(UVertex vv[]) {
    return check(vv[0],vv[1],vv[2]);
  }
  
  public static boolean check(UVertex v1, UVertex v2, UVertex v3) {
    if(v1.equals(v2) || v2.equals(v3) || v1.equals(v3)) return false;
    float d1=v1.distSimple(v2);
    float d2=v2.distSimple(v3);
    float d3=v1.distSimple(v3);
    
    if(d1<EPSILON || d2<EPSILON || d3<EPSILON) return false;
    return true;
  }

}
