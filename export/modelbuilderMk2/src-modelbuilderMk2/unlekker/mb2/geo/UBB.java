/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import unlekker.mb2.util.UMbMk2;

public class UBB extends UMbMk2 {
  public UVertex centroid,min,max,dim;
  private UVertexList vl;
  
  public UBB() {
    centroid=new UVertex();
    dim=new UVertex();
    
    max=new UVertex(
        Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
    min=new UVertex(
        Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
  }

  public UBB add(UVertex v[]) {
    if(v!=null && v.length<1) {
      for(UVertex vv:v) {
        add(vv);
        log(v.length+" "+vv.str());
      }
    }
    
    return this;    
  }

  public UBB add(UVertex v) {
    min.set(min(v.x,min.x),min(v.y,min.y),min(v.z,min.z));
    max.set(max(v.x,max.x),max(v.y,max.y),max(v.z,max.z));
    
    return this;
  }
  
  public float dimX() {return dim.x;}
  public float dimY() {return dim.y;}
  public float dimZ() {return dim.z;}

  public UBB calc() {
    dim.set(max).sub(min);
    centroid.set(max).add(min).mult(0.5f);
    log("calc\t"+centroid.str()+" "+str());
    
    return this;
  }

  public UBB draw() {
    if(!checkGraphicsSet()) return this;
    
    if(vl==null) vl=new UVertexList();
    if(vl.size()<1) {
      vl.add(min.x,min.y,min.z);
      vl.add(min.x,max.y,min.z);
      vl.add(min.x,max.y,max.z);
      vl.add(min.x,min.y,max.z);
      vl.add(min.x,min.y,min.z);
      
      vl.add(max.x,min.y,min.z);
      vl.add(max.x,max.y,min.z);
      vl.add(max.x,max.y,max.z);
      vl.add(max.x,min.y,max.z);
      vl.add(max.x,min.y,min.z);
      
      // centroid and "crosshairs"
      vl.add(centroid);
      vl.add(centroid.copy().add(-20,0,0));
      vl.add(centroid.copy().add(20,0,0));
      vl.add(centroid.copy().add(0,-20,0));
      vl.add(centroid.copy().add(0,20,0));
    }
    
    if(isGraphics3D) {
      g.beginShape(QUAD_STRIP);
      for(int i=0; i<5; i++) {
        pvertex(vl.get(i)).pvertex(vl.get(i+5));
      }
      g.endShape();
    }
    else {
      g.beginShape();
      pvertex(vl.get(0)).pvertex(vl.get(5));
      pvertex(vl.get(6)).pvertex(vl.get(1));
      pvertex(vl.get(0));
      g.endShape();
    }
    
    ppush().ptranslate(centroid);
    g.beginShape(LINES);
    pline(vl.get(11), vl.get(12));
    pline(vl.get(13), vl.get(14));
    g.endShape();

    ppop();
    
    return this;
  }
  
  public UBB clear() {
    centroid.set(0,0,0);
    max.set(
        Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
    min.set(
        Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
    dim.set(0,0,0);
    if(vl!=null) vl.clear();
    
    return this;
  }
  
  public String str() {
    StringBuffer buf=strBufGet();
    buf.append("[BB dim=").append(dim.str()).
      append(SPACE).append(min.str()).
      append(SPACE).append(max.str()).append(']');
    
    return strBufDispose(buf);
  }
}
