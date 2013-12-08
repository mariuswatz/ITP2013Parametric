/*[[ 
 * modelbuildeVrMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;
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
  
  protected UEdge edge[];
  protected UFace connected[];
  
  public int col;
  private UVertex normal,centroid;
  private int[] vIDSorted;
  
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
  
  public boolean inBB(UBB bb,boolean allVertices) {
    getV();
    if(!allVertices) {
      return (bb.inBB(v[0]) || bb.inBB(v[1]) || bb.inBB(v[2]));
    }
    
    int cnt=0;
    cnt=(bb.inBB(v[0]) ? cnt+1 : cnt);
    cnt=(bb.inBB(v[1]) ? cnt+1 : cnt);
    cnt=(bb.inBB(v[2]) ? cnt+1 : cnt);
    return (cnt==3);    
  }

  
  public boolean contains(UVertex vv) {
    getV();
    if(v[0]==vv) return true;
    if(v[1]==vv) return true;
    if(v[2]==vv) return true;
//    if(v[0].equals(vv)) return true;
//    if(v[1].equals(vv)) return true;
//    if(v[2].equals(vv)) return true;
    return false;
  }

  public UFace(UGeo model, int id1,int id2,int id3) {
    this();
    parent=model;
    vID=new int[] {id1,id2,id3};
  }

  public UFace(UGeo model, int[] ID) {
    this();
    parent=model;
    System.arraycopy(ID, 0, vID, 0, 3);
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

  public UFace setEdges(UEdge ed[]) {
    edge=ed;
    return this;
  }

  public int sizeConnected() {
    int n=0;
    if(connected!=null) {
      for(UFace fc:connected) n=(fc==null ? n : n+1);
    }
    return n;
  }

  public UEdge[] edges() {
    if(edge==null) {
      logErr("UFace.edges: Edges not calculated");
      return null;
    }
    
    return edge;
  }

  public UFace[] connected() {
    if(edge==null) {
      logErr("UFace.connected: Edges not calculated");
      return null;
    }
    
    if(connected==null) {
      connected=new UFace[3];
      for(int i=0; i<edge.length; i++) {
        if(edge[i]!=null) {
          ArrayList<UFace> ef=edge[i].getF();
          UFace ff=null;
          if(ef.size()>0) {
            ff=ef.get(0);
            if(ff.equals(this) && ef.size()>1) {
              ff=edge[i].faces.get(1);              
            }
            if(ff.equals(this)) ff=null;
          }

          connected[i]=ff;
        }
      }
    }
    
    return connected;
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
    if(v==null) getV();
    return getVID(v);    
  }

  private int[] getVID(UVertex vv[]) {
    vID=new int[] {
        parent.addVertex(vv[0]),  
        parent.addVertex(vv[1]),  
        parent.addVertex(vv[2])
    };
    v=null;
    
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

  public UFace remapVID() {
    getVID(v);
    getV(true);

    return this;
  }
  
  /**
   * <p>Determines the winding order of this vertex list by calculating the area of the polygon
   * it represents.Only works in the XY plane. The area calculation is <code>Area = Area + (X2 - X1) * (Y2 + Y1) / 2)</code>.</p>
   * 
   *  <p>Code by Jonathan Cooper, found at <a href="http://forums.esri.com/Thread.asp?c=2&f=1718&t=174277#513372">
   *  http://forums.esri.com/Thread.asp?c=2&f=1718&t=174277#513372</a>.</p>
   * @return
   */
   public boolean isClockwise(){
     getV();
     UVertex pt1 = v[0];
     UVertex firstPt = pt1;
     UVertex lastPt = null;
     double area = 0.0;
     for(int i=1; i<3; i++) {
       
       UVertex pt2 = v[i];
       area += (((pt2.x - pt1.x) * (pt2.y + pt1.y)) / 2);
       pt1 = pt2;
       lastPt = pt1;
     }
     area += (((firstPt.x - lastPt.x) * (firstPt.y + lastPt.y)) / 2);
     return area < 0;
   }

  public UVertex[] getV() {
    if(parent==null || v!=null) return v;
    
    if(v==null) v=new UVertex[vID.length];
 
    int id=0;    
    for(int vid:vID) v[id++]=parent.getVertex(vid);
    
    return v;
  }

  public UVertex[] getVNormals() {
    if(parent==null) return null;
    
    parent.vertexNormals();
    return new UVertex[] {
        parent.getVNormal(vID[0]),
        parent.getVNormal(vID[1]),
        parent.getVNormal(vID[2])
    };
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

  public UFace copy(UGeo newParent) {
    getV();
    return new UFace(newParent, v[0],v[1],v[2]);
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
    return setColor(pcolor(a,b,c));
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
    vIDSorted=null;
    centroid=null;
    return this;
  }
  
//  private boolean isSameClockDir(Vec3D a, Vec3D b, ReadonlyVec3D p, Vec3D norm) {
//    float bax = b.x - a.x;
//    float bay = b.y - a.y;
//    float baz = b.z - a.z;
//    float pax = p.x() - a.x;
//    float pay = p.y() - a.y;
//    float paz = p.z() - a.z;
//    float nx = bay * paz - pay * baz;
//    float ny = baz * pax - paz * bax;
//    float nz = bax * pay - pax * bay;
//    float dotprod = nx * norm.x + ny * norm.y + nz * norm.z;
//    return dotprod < 0;
//}


  public float area() {
    getV();
    float a=v[0].dist(v[1]);
    float b=v[1].dist(v[2]);
    float c=v[2].dist(v[0]);
    float s=(a+b+c)/2;
    float area=(float)Math.sqrt(s*(s-a)*(s-b)*(s-c));
    return area;
}

  public UVertex normal() {
    if(normal!=null) return normal;
        
    getV();
    
    normal=UVertex.cross(
        v[2].copy().sub(v[0]),
        v[1].copy().sub(v[0]));
//        UVertex.delta(v[0],v[1]).norm() 
//        UVertex.delta(v[0],v[2]).norm());
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

  public boolean equalsVID(UFace f) {
    
    if(f.vIDSorted==null) {
      int id[]=new int[3];
      System.arraycopy(f.vID, 0, id, 0, 3);
      Arrays.sort(id);
      f.vIDSorted=id;
    }
    if(vIDSorted==null) {
      int id[]=new int[3];
      System.arraycopy(vID, 0, id, 0, 3);
      Arrays.sort(id);
      vIDSorted=id;
    }

    if(vIDSorted[0]==f.vIDSorted[0] &&
       vIDSorted[1]==f.vIDSorted[1] && 
       vIDSorted[2]==f.vIDSorted[2]) return true;
    
    return false;
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
  
  public float dist(UFace ff) {
    return 0;
  }

  public boolean facingSameWay(UFace ff) {
    UVertex n=normal();
    UVertex n2=ff.normal();
    
    int cnt=0;
    cnt+=sign(n.x) == sign(n2.x) ? 1 : 0;
    cnt+=sign(n.y) == sign(n2.y) ? 1 : 0;
    cnt+=sign(n.z) == sign(n2.z) ? 1 : 0;
    
    return cnt==3;
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
