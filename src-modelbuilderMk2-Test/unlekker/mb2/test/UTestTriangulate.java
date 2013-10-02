/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestTriangulate extends UTest {
  UVertexList vl;
  UGeo mesh,cyl;
  
  public void init() {
    vl=new UVertexList();
    
    int n=100;
    float w=p.width/2;
    
    for(int i=0; i<n; i++) {
      float deg=(float)i*137.5f*DEG_TO_RAD;
      
      vl.add(new UVertex(p.random(0.1f,1)*w,0,0).rotY(deg));      
      vl.last().z=vl.rndSigned(0.1f,0.15f)*w;
    }
    
    vl.rotY(HALF_PI);
    mesh=new UTriangulate(vl).mesh;
    
    UVertexList circ,circ2;
    circ=new UVertexList();
    for(float t=0; t<1; t+=0.05f) 
      circ.add(new UVertex(p.random(100,200),0,0).rotY(t*TWO_PI));
    
    circ2=circ.copy().translate(0,100,0);
    circ.translate(0,-100,0);
    cyl=new UGeo();
    cyl.triangulation(circ);
    cyl.quadstrip(circ.close(),circ2.close());
    
//    circ.add(circ.copy().scale(0.5f,1,0.5f));
//    cyl.triangulation(circ2);
    
    for(UFace f:cyl.getF()) {
      f.setColor(p.color(p.random(255)));
      f.log(
          f.centroid().str()+" "+
              f.normal().str()+" "+
      UVertex.str(f.getV()));
    }
    cyl.enable(cyl.COLORFACE);
    
    if(main.nav==null) main.nav=new UNav3D();
    
  }
  
  public void draw() {
    
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    int facesToDraw=(p.frameCount/50)%cyl.sizeF()+1;
    for(int i=0; i<facesToDraw; i++) {
      UFace f=cyl.getF().get(i);
      
      UMB.pnoFill().pstroke(0xffffffff);
      f.draw();
      f.drawNormal(100,false);
    }
//    mesh.pnoFill().pstroke(0xffffffff);
//    mesh.draw();
//    
//    mesh.pnoStroke().pfill(0x33ffff00);
//    mesh.draw();
// 
    
//   cyl.draw(); 
//   
//   mesh.pnoFill().pstroke(0xffffffff);
//   for(UFace ff:mesh.getF()) {
//     ff.drawNormal(50);
//     ff.ppush().ptranslate(ff.centroid());
//     p.box(5);
//     ff.ppop();
//     
//   }
  }

  
}
