/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMbMk2;

public class UTestTriangulate extends UTest {
  UVertexList vl;
  UGeo mesh;
  
  public void init() {
    vl=new UVertexList();
    
    int n=100;
    float w=p.width/2;
    
    for(int i=0; i<n; i++) {
      float deg=(float)i*137.5f*DEG_TO_RAD;
      
      vl.add(new UVertex(p.random(0.1f,1)*w,0,0).rot(deg));      
      vl.last().z=vl.rndSigned(0.1f,0.15f)*w;
    }
    
    vl.rotY(HALF_PI);
    mesh=new UTriangulate(vl).mesh;
    if(main.nav==null) main.nav=new UNav3D();
    
  }
  
  public void draw() {
    
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();
    
    mesh.pnoFill().pstroke(0xffffffff);
    mesh.draw();
    
    mesh.pnoStroke().pfill(0x33ffff00);
    mesh.draw();
  }

  
}
