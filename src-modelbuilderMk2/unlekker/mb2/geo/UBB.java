package unlekker.mb2.geo;

import unlekker.mb2.util.UBase;

public class UBB extends UBase {
  public UVertex c,mn,mx,sz;
  
  public UBB() {
    c=new UVertex();
    sz=new UVertex();
    
    mx=new UVertex(
        Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
    mn=new UVertex(
        Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
  }
  
  public UBB add(UVertex v) {
    mn.set(min(v.x,mn.x),min(v.y,mn.y),min(v.z,mn.z));
    mx.set(max(v.x,mx.x),max(v.y,mx.y),max(v.z,mx.z));
    
    calc();
    
    return this;
  }
  
  public float w() {return sz.x;}
  public float h() {return sz.y;}
  public float d() {return sz.z;}

  private void calc() {
    sz.set(mx).sub(mn);
    c.set(mx).add(mn).mult(0.5f);
  }
  
  
  public UBB clear() {
    c.set(0,0,0);
    mn.set(0,0,0);
    mx.set(0,0,0);
    sz.set(0,0,0);
    return this;
  }
}
