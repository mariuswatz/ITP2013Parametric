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
import unlekker.mb2.geo.USubdivision;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestPrimitives extends UTest {
  UGeo geo;
  long last=0;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

//    geo=UGeo.box(100);
//    UMB.log(geo.bb().str());
//    geo.add(UGeo.box(200,10,50).translate(0,100,0));
    geo=UGeo.cyl(100, 200, 20);
    UMB.log(geo.bb().str());
    UMB.log(geo.strGroup());
    
//    USubdivision.subdivide(geo, UMB.SUBDIVMIDEDGES);
  }
  
  public void draw() {
    p.fill(255);
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    p.noFill();
    p.stroke(255);
    geo.drawNormals(100);

    UMB.pnoStroke().pfill(p.color(255,255,0));
//    p.noStroke();
    geo.draw();
    
//    if(p.keyPressed) {
//      if(p.millis()-last>1000) {
//        geo=USubdivision.subdivide(geo, UMB.SUBDIVMIDEDGES);
//        for(UFace f:geo.getF()) {
//          int col=p.lerpColor(0xFF330000, 0xFF00FFFF, p.random(1));
//          f.setColor(col);
//        }
//        last=p.millis();
//        geo.enable(geo.COLORFACE);
//      }
//    }

  }

  public void keyPressed(char key) {
    if(key=='r') geo.reverseNormals(); 
  }
  
}
