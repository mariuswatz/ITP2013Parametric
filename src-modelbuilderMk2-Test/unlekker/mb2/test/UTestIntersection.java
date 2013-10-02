/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestIntersection extends UTest {
  UVertexList vl;
  UGeo mesh,cyl;
  
  UVertexList lines,inter;
  
  UFace f;
  
  UVertex a,b,ii;
  float R=400;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();
    UVertex v=new UVertex(1000,0,0);
    f=new UFace().set(
        v.copy(),
        v.copy().rotY(120*DEG_TO_RAD),
        v.copy().rotY(240*DEG_TO_RAD));
    f.reverse();
    
    UVertex.rotX(f.getV(), PI/6);

    lines=new UVertexList();
    inter=new UVertexList();
    
    for(int i=0; i<10; i++) {
      a=new UVertex(p.random(0.5f,1)*R, 0).
          rotY(p.random(TWO_PI)).add(0, 200);
      b=new UVertex(20,0).
          rotY(p.random(TWO_PI)).
          add(a).add(0, p.random(-400, -150));

      ii=UIntersections.intersectLinePlane(a, b, f);

//      ii=UIntersections.intersectLineY(a, b, 0);
      lines.add(a).add(b);
      inter.add(ii);
    }
  }
  
  public void draw() {
    
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    
    p.stroke(255);
    UMB.ppush().protX(HALF_PI).prect(1000,1000).ppop();
    p.noFill();
    
    if(f!=null) {
      UMB.ppush();      
      f.draw().drawNormal(100);
      f.pline(f.getV()[0], f.centroid());
      f.pline(f.getV()[1], f.centroid());
      f.pline(f.getV()[2], f.centroid());
      UMB.ppop();
    }
    else {      
      UMB.ppush().
      protX(HALF_PI).pellipse(0,0,R,R).ppop();
    }

    
    for(int i=0; i<inter.size(); i++) {
      ii=inter.get(i);
      a=lines.get(i*2);
      b=lines.get(i*2+1);
      
      if (ii!=null) {        
        UMB.pnoFill().pstroke(p.color(255, 255, 0)).pline(a,ii);
        UMB.pstroke(p.color(255, 0, 128)).pline(b, ii);
        p.text(ii.str(), ii.x, 20);
        UMB.ppush().ptranslate(ii);
        p.box(10);
        p.box(1);
        UMB.ppop();
        
        UMB.ppush().ptranslate(b);
        p.box(5);
        UMB.ppop();
        
      }
      else 
        UMB.pstroke(0xffff0000).pline(a, b);
      
    }

 
  }

  
}
