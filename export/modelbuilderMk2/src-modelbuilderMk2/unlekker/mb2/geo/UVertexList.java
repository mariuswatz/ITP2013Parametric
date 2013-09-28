package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import processing.core.PVector;
import processing.opengl.*;
import unlekker.mb2.util.UMbMk2;

/**
 * 
 * Auto-growing list of UVertex objects that are internally stored as an <code>ArrayList<UVertex></code>.. 
 * The default behavior is to add vertices by copy rather than reference, without checking for duplicate vertices. 
 * This is convenient for most cases but can cause problems for per-vertex transformations and STL output.
 * 
 * Adding by reference can be activated by calling <code>enable(NOCOPY);</code>, duplicate checking is activated by
 * <code>enable(NODUPL);</code> 
 * 
 * TODO
 * - NODUPL / NOCOPY is not consistently implemented.
 * 
 * @author marius
 *
 */
public class UVertexList extends UMbMk2 implements Iterable<UVertex> {
  public ArrayList<UVertex> v;
  public int options;
  public UBB bb;

  
  public UVertexList() {
    v=new ArrayList<UVertex>();
    disable(NOCOPY);
    disable(NODUPL);
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
  
  /**
   * Returns vertex data as array of <code>UVertex</code> instances.
   * @return vertices stored as <code>UVertex[]</code> array.
   */
  public UVertex[] toArray() {
    UVertex[] vv=v.toArray(new UVertex[size()]);
    return vv;
  }

  /**
   * Returns vertex data as an ArrayList<PVector>. 
   * @return ArrayList of vertices converted to PVector
   */
  public ArrayList<PVector> toPVectorList() {
    ArrayList<PVector> pv=new ArrayList<PVector>();
    for(UVertex vv:v) pv.add(vv.toPVector());
    return pv;
  }
  
  public static UVertexList lerp(float t,UVertexList l1,UVertexList l2) {
    UVertexList res=new UVertexList();
    
    int id=0;
    for(UVertex v1:l1) {
      res.add(v1.lerp(t, l2.get(id++)));
    }
    
    return res;
  }

  /**
   * Generates an ArrayList containing a series of <code>n</code> vertex lists that
   * are linear interpolations of <code>l1</code> and <code>l2</code>. 
   *  
   * @param n Number of interpolations to generate
   * @param l1
   * @param l2
   * @return ArrayList of interpolated vertex lists
   */
  public static ArrayList<UVertexList> lerpSeries(int n,UVertexList l1,UVertexList l2) {
    ArrayList<UVertexList> l=new ArrayList<UVertexList>();
    
    for(int i=0; i<n; i++) {
      float t=map(i,0,n-1,0,1);
      l.add(UVertexList.lerp(t, l1, l2));
    }
    
    return l;
  }

  public void draw() {
    if(checkGraphicsSet()) {
      g.beginShape();
      if(isGraphics3D) {
        for(UVertex vv:v) g3d.vertex(vv.x,vv.y,vv.z);        
      }
      else {
        for(UVertex vv:v) g.vertex(vv.x,vv.y);
      }
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
    
    return bb.calc();
  }
  
  /**
   * Returns UVertex instance where x,y,z represent the
   * size of this mesh in the X,Y,Z dimensions.  
   * @return
   */
  public UVertex dimensions() {
    return bb().dim;
  }

  public UVertex centroid() {
    bb();
    return bb.centroid;
  }
  
  /** 
   * Translates vertices so that their centroid lies at
   * the origin.
   */
  public UVertexList center() {
    translateNeg(centroid());
    return this;
  }

  /**
   * Translates vertices so that their centroid lies at
   * at the provided point in space.
   * @param v1 Point where mesh will be centered
   * @return
   */
  public UVertexList centerAt(UVertex v1) {
    return center().translate(v1);
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

  public UVertexList insert(int index,UVertex v1) {
    v.add(index,v1);
    return this;
  }

  public UVertexList add(UVertex vv[]) {
    for(UVertex vert:vv) if(vert!=null) add(vert);

    return this;
  }

  public UVertexList add(UVertex v1) {
    if(isEnabled(NODUPL) && v.contains(v1)) {
      log("Duplicate: "+v1+" "+ v.contains(v1));
      return this;
    }

    v.add(isEnabled(NOCOPY) ? v1 : v1.copy());      
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

  public UVertexList add(ArrayList<PVector> pv) {
    for(PVector vv:pv) add(vv.x,vv.y,vv.z);
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
    
    v1=isEnabled(NOCOPY) ? v1 : v1.copy();
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
    return translate(-v1.x,-v1.y,-v1.z);    
  }

  public UVertexList translate(UVertex v1) {    
    return translate(v1.x,v1.y,v1.z);    
  }

  public UVertexList translate(float mx,float my) {
    return translate(mx,my,0);
  }
  
  public UVertexList translate(float mx,float my,float mz) {
    bb=null;
    for(UVertex vv:v) vv.add(mx,my,mz);
    return this;    
  }

  
  public Iterator<UVertex> iterator() {
    // TODO Auto-generated method stub
    return v.iterator();
  }
  
  public UVertexList rotX(float deg) {
    bb=null;
    for(UVertex vv:v) vv.rotAxis(X, deg);
    return this;
  }

  public UVertexList rotY(float deg) {
    bb=null;
    for(UVertex vv:v) vv.rotAxis(Y, deg);
    return this;
  }

  public UVertexList rotZ(float deg) {
    bb=null;
    for(UVertex vv:v) vv.rotAxis(Z, deg);
    return this;
  }

  public UVertexList close() {
    return add(first());
  }

  
  public boolean contains(UVertex vv) {
    return v.contains(vv);
  }
  
  /**
   * Removes or "fixes" duplicate vertices in the list as identified by 
   * the <code>UVertex.equals()</code> method. <code>doDelete</code> controls how 
   * duplicates are handled: 
   * 
   * If <code>doDelete==true</code>, only the first instance is retained while additional 
   * duplicates are deleted from the list. This ensures that the list contains only unique
   * vertices, but may alter the list length and order. 
   * 
   * If <code>doDelete==false</code>, objects representing duplicate entries are replaced by 
   * an object reference to the first matching instance in the list. Vertex order and length of
   * list is preserved, but subsequent changes to any reference to  duplicate entry will 
   * affect all entries as well, as they now point to a single object. This is useful for
   * consistent STL export (where rounding errors can cause identical vertices to become non-identical).
   * 
   * @param doDelete Flag indicating whether duplicates should be deleted or replaced by a reference
   * to a single instance.
   * 
   * @return
   */
  public UVertexList removeDupl(boolean doDelete) {
    int id=0;
    
    while(id<v.size()) {
      int index=indexOf(v.get(id));
      if(index<id) {
        
        if(doDelete) { // REMOVE FROM LIST
          v.remove(id);
          id--;
        }
        else { // REPLACE WITH REFERENCE TO FIRST INSTANCE
          v.set(id, v.get(index));
        }
      }
      id++;
    }    
    
    return this;
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
    return "["+size()+TAB+str(v,TAB,null)+"]";
  }

  public String strWithID() {
    StringBuffer buf=strBufGet();
    buf.append(size()).append(TAB);
    for(UVertex vv:v) {
      if(vv!=null) buf.append(vv.ID).append('=').append(vv.str()).append(TAB);
      else buf.append("NULL").append(TAB);
    }
    
    if(size()>0) buf.deleteCharAt(buf.length()-1);
    
    return "["+strBufDispose(buf)+"]";
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
