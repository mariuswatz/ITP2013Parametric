package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import processing.core.PGraphics3D;
import unlekker.mb2.util.UBase;

/**
 * TODO
 * - No dupl / no copy
 * - addID
 * 
 * @author marius
 *
 */
public class UVertexList extends UBase implements Iterable<UVertex> {
  public ArrayList<UVertex> v;
  public int options;
  public UBB bb;

  
  public UVertexList() {
    v=new ArrayList<UVertex>();
  }

  public UVertexList(int options) {
    this();
    this.options=options;
  }
  
  public UVertexList copy() {
    UVertexList cvl=new UVertexList();
    for(UVertex vv:v) cvl.add(vv);
    return cvl;
  }
  
  public void draw() {
    if(checkGraphicsSet()) {
      g.beginShape();
      for(UVertex vv:v) g.vertex(vv.x,vv.y,vv.z);
      g.endShape();
    }

  }

  public UBB bb() {
    return bb(false);
  }

  public UBB bb(boolean force) {
    if(bb==null) bb=new UBB();
    else {
      if(!force) return bb;
      bb.clear();
    }
    
    for(UVertex vv:v) bb.add(vv);
    
    return bb;

  }
  public UVertex centroid(boolean omitLast) {
    UVertex c=new UVertex();
    
    bb();
    
    
    
//    float UV[]=null;
//    if(first().V>-1 || first().U>-1) UV=new float[] {0,0};
//    
//    int id=-1;
//    int nn=size();
//    if(omitLast) nn--;
//
//    for(UVertex vv:v) {
//      if((++id)!=nn) {
//        c.add(vv);
//        if(UV!=null) {
//          UV[0]+=vv.U;
//          UV[1]+=vv.V;
//        }
//      }
//    }
//    c.div(nn);
//    if(UV!=null) 
//      c.setUV(UV[0]/(float)id,UV[1]/(float)id);
    
    return bb.c;
  }

  public UVertex centroid() {
    return centroid(false);
  }
  
  public boolean allowDupl() {
    return !((options & NODUPL)==NODUPL);
  }

  public boolean allowCopy() {
    return !((options & NOCOPY)==NOCOPY);
  }
  
  public UVertexList clear() {
    v.clear();
    return this;
  }
  
  public UVertex[] get(int vID[]) {
    return get(vID,null);
  }


  public UVertex[] get(int vID[],UVertex tmp[]) {
    if(tmp==null || tmp.length!=vID.length)
      tmp=new UVertex[vID.length];
    
    int id=0;    
    for(int vid:vID) tmp[id++]=get(vid);

    return tmp;
  }

  
  public int getVID(UVertex vv) {return indexOf(vv);}

  public UVertex get(int id) {
    return (id>size() ? null : v.get(id));
  }

  public UVertex last() {
    int n=size();
    return (n<1 ? null : v.get(n-1));
  }

  public UVertex first() {
    return (size()<1 ? null : v.get(0));
  }

  public int size() {
    return v.size();
  }

  public int indexOf(UVertex v2) {
    if(v2==null || size()<1) return -1;
    return v.indexOf(v2);
  }

  public UVertexList extract(int n1,int n2) {
    UVertexList l=new UVertexList();
    for(int i=n1; i<n2; i++) l.add(v.get(i));
    return l;
  }

  public UVertexList add(UVertex v1) {
    v.add(allowCopy() ? v1.copy() : v1);
    
    bb=null;
    return this;
  }

  
  public UVertexList add(float x, float y) {
    add(x,y,0);   
    return this;
  }
  
  public UVertexList add(float x, float y,float z) {
    add(new UVertex(x,y,z));   
    return this;
  }

  public UVertexList add(UVertexList v1) {
    for(UVertex vv:v1) add(vv);
    return this;
  }

  public int addID(float x,float y,float z) {
    return addID(new UVertex(x,y,z));
  }

  public int addID(UVertex v1) {
//    add(v);    
    bb=null;
    
    v1=allowCopy() ? v1.copy() : v1;
    v.add(v1);

    return indexOf(v1);
  }

  public UVertexList scale(float mx,float my,float mz) {
    for(UVertex vv:v) vv.mult(mx,my,mz);
    return this;    
  }

  public UVertexList scale(float m) {
    return scale(m,m,m);    
  }

  public UVertexList translateNeg(UVertex v1) {
    for(UVertex vv:v) vv.sub(v1);
    return this;    
  }

  public UVertexList translate(UVertex v1) {
    for(UVertex vv:v) vv.add(v1);
    return this;    
  }

  public UVertexList translate(float mx,float my) {
    return translate(mx,my,0);
  }
  
  public UVertexList translate(float mx,float my,float mz) {
    for(UVertex vv:v) vv.add(mx,my,mz);
    return this;    
  }

  
  public Iterator<UVertex> iterator() {
    // TODO Auto-generated method stub
    return v.iterator();
  }
  
  public UVertexList rotX(float deg) {
    for(UVertex vv:v) vv.rotAxis(X, deg);
    return this;
  }

  public UVertexList rotY(float deg) {
    for(UVertex vv:v) vv.rotAxis(Y, deg);
    return this;
  }

  public UVertexList rotZ(float deg) {
    for(UVertex vv:v) vv.rotAxis(Z, deg);
    return this;
  }

  public UVertexList close() {
    return add(first());
    
  }

  //////////////////////////////////////////
  // UV methods

  public UVertexList setULinear() {
    int id=0,n=size()-1;
    for(UVertex vv:v) vv.U=map(id++,0,n,0,1);
    return this;
  }

  public UVertexList setVLinear() {
    int id=0,n=size()-1;
    for(UVertex vv:v) vv.V=map(id++,0,n,0,1);
    return this;
  }

  public static void setUV(ArrayList<UVertexList> vl) {
    setVLinear(vl);
    setULinear(vl);
  }

  /**
   * Iterates through list of {@link UVertexList}, calling setULinear() for each.
   * @param vl
   */
  public static void setULinear(ArrayList<UVertexList> vl) {
    for(UVertexList vv:vl) vv.setULinear();
  }

  /**
   * Sets the V coordinate of vertices in a list of {@link UVertexList} so
   * that vl.get(0) has V==0 and vl.get(vl.size()-1) has V==1. Useful for
   * setting V as factor of list index for later coloring, or to keep track
   * of original list index after vertices are used in a mesh.
   * @param vl
   */
  public static void setVLinear(ArrayList<UVertexList> vl) {
    setVLinear(vl,1);
  }

  /**
   * Sets the V coordinate of vertices in a list of {@link UVertexList} so
   * that vl.get(0) has V==0 and vl.get(vl.size()-1) has V==maxV. Useful for
   * setting V as factor of list index for later coloring, or to keep track
   * of which list index a vertex belonged to after vertices are used in a mesh.
   * @param vl
   * @param maxV Value of V for last list.
   */
  public static void setVLinear(ArrayList<UVertexList> vl,float maxV) {
    int id=0,n=vl.size()-1;
    for(UVertexList vv:vl) vv.setV(map(id++,0,n,0,1));
  }

  public UVertexList setU(float U) {
    for(UVertex vv:v) vv.U=U;
    return this;
  }

  public UVertexList setV(float V) {
    for(UVertex vv:v) vv.V=V;
    return this;
  }
  
  //////////////////////////////////////////
  // COLOR

  public UVertexList setColor(int c) {
    for(UVertex vv:v) vv.setColor(c);
    return this;
  }

  public UVertexList setColor(int c, int a) {
    setColor(color(c, a));
    return this;
  }

  public UVertexList setColor(float a,float b,float c) {
    return setColor(color(a,b,c));
  }

  public String str() {
    return "["+size()+TAB+str(v)+"]";
  }

  public UVertexList reverse() {
    Collections.reverse(v);
    return this;
    
  }

  public int[] getVID(UVertex[] vv, int[] vid) {
    if(vid==null) vid=new int[vv.length];
    
    int id=0;
    for(UVertex vvv:vv) vid[id++]=getVID(vvv);
    
    Arrays.sort(vid);
    return vid;
  }

}
