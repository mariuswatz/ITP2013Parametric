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

  public UFace set(UVertex v1, UVertex v2, UVertex v3) {
    if(parent!=null) {
      getVID(v1, v2, v3);
    }
    else {
      if(v==null) v=new UVertex[3];
      v[0]=v1;
      v[1]=v2;
      v[2]=v3;
    }
    
   
    return this;
  }

  public UFace getVID() {
    return getVID(v[0],v[1],v[2]);
    
  }
  
  private UFace getVID(UVertex v1, UVertex v2, UVertex v3) {
    vID[0]=parent.addVertex(v1);
    vID[1]=parent.addVertex(v2);
    vID[2]=parent.addVertex(v3);
    return this;
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

  public UVertex[] getV() {
    if(parent==null || v!=null) return v;
    
    if(v==null) v=new UVertex[3];
 
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
    getV()[0].add(mx,my,mz);
    v[1].add(mx,my,mz);
    v[2].add(mx,my,mz);
    return reset();
  }
  
  public UFace rotX(float deg) {
    getV()[0].rotX(deg);
    v[1].rotX(deg);
    v[2].rotX(deg);
    return reset();
  }

  public UFace rotY(float deg) {
    getV()[0].rotY(deg);
    v[1].rotY(deg);
    v[2].rotY(deg);
    return reset();
  }

  public UFace rotZ(float deg) {
    getV();
    v[0].rotZ(deg);
    v[1].rotZ(deg);
    v[2].rotZ(deg);
    return reset();
  }


  public UFace scale(float m) {return scale(m,m,m);}

  public UFace scale(float mx,float my,float mz) {
    getV()[0].mult(mx,my,mz);
    v[1].mult(mx,my,mz);
    v[2].mult(mx,my,mz);
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
    vID=new int[] {v.vID[0],v.vID[1],v.vID[2]};
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
      getV();
      centroid=new UVertex().
          add(v[0]).add(v[1]).add(v[2]);
      log(UVertex.str(v)+" -> "+centroid.str());
      centroid.div(3);
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

}
