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
  UGeo geo,geo2;
  long last=0;
  
  UFace fn;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

//    geo=UGeo.box(100);
//    UMB.log(geo.bb().str());
    

    geo=new UGeo();
    geo.disable(NODUPL);
    geo.add(UGeo.box(100).translate(-200,0,0));
    UMB.log(geo.bb().str());
    logDivider();
    UMB.log(geo.strGroup());
    
    for(int i=0; i<geo.sizeF(); i++) {
      if(i%2==0) geo.getF(i).setColor(pcolor(255,0,0));
      else geo.getF(i).setColor(pcolor(255,255,255));
    }
    
    geo.add(UGeo.cyl(100, 200, 20).setColor(pcolor(0,255,255)));

//    geo.center();
//    geo.enable(geo.COLORFACE);


    if(rndBool()) 
      geo2=UGeo.box(100).translate(-200,0,0);

    else geo2=UGeo.cyl(100, 200, 20).setColor(pcolor(0,255,255));
    geo2.translate(0,450,0);

    for(UFace ff:geo.getF()) {
      ff.normal(true);
      
    }
//    USubdivision.subdivide(geo, UMB.SUBDIVMIDEDGES);
    
    fn=new UFace(null, 
        new UVertex(0,0),
        new UVertex(0,100),
        new UVertex(100,0));
    fn.normal();
        
    geo.writeSTL(p.sketchPath("Prim.stl"));
  }
  
  public void draw() {
    p.fill(255);
    p.lights();
    p.translate(p.width/2, p.height/2);
    main.nav.doTransforms();

    p.noFill();
    p.stroke(255,150,0);
    geo.drawNormals(40);
    geo2.drawNormals(40);

    p.stroke(100);
    p.fill(200);
//    p.noStroke();
    geo.draw();
    geo2.draw();
    
    fn.draw();
    fn.drawNormal(50);
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
