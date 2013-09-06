package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PGraphics3D;
import unlekker.mb2.util.UBasic;

/**
 * TODO
 * - No dupl / no copy
 * - addID
 * 
 * @author marius
 *
 */
public class UVertexList extends UBasic implements Iterable<UVertex> {
  public ArrayList<UVertex> v;
  public int options;
  
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
    g.beginShape();
    for(UVertex vv:v) g.vertex(vv.x,vv.y,vv.z);
    g.endShape();
  }

  public UVertex centroid(boolean omitLast) {
    UVertex c=new UVertex();
    
    if(omitLast) {
      int id=0;
      int nn=size()-1;
      for(UVertex vv:v) if((id++)!=nn) c.add(vv);
      c.div(nn);
    }
    else {
      for(UVertex vv:v) c.add(vv);
      c.div(size());
    }
    
    return c;
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
    return v.indexOf(v2);
  }

  public UVertexList extract(int n1,int n2) {
    UVertexList l=new UVertexList();
    for(int i=n1; i<n2; i++) l.add(v.get(i));
    return l;
  }

  public UVertexList add(UVertex v1) {
    v.add(allowCopy() ? v1.copy() : v1);
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

  public int addID(float x,float y,float z) {
    return addID(new UVertex(x,y,z));
  }

  public int addID(UVertex v1) {
//    add(v);    
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

  public UVertexList translate(float mx,float my,float mz) {
    for(UVertex vv:v) vv.add(mx,my,mz);
    return this;    
  }

  
  @Override
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

  public String str() {
    return "["+size()+TAB+str(v)+"]";
  }

}
