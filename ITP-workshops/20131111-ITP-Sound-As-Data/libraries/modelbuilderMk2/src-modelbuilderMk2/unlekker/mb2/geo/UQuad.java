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
public class UQuad extends UFace  {
  
  public UQuad() {
    ID=globalID++;
    vID=new int[] {-1,-1,-1,-1};
    col=Integer.MAX_VALUE;
  }

  public UQuad(UQuad v) {
    this();
    set(v);
  }

  public UQuad draw() {
    if(checkGraphicsSet()) {
      if(col!=Integer.MAX_VALUE) g.fill(col);
      if(v==null) getV();
      g.beginShape(QUADS);
      pvertex(v);
      g.endShape();
    }
    
    return this;
  }
  
  public UQuad copy() {
    return new UQuad(this);
  }
}
