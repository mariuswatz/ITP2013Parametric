/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestSelector extends UTest {
  PGraphics gg;
  UVertexList vl;
  UGeo geo;
  UGeoSelector sel;
  UEdgeList l;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    geo=UGeoGenerator.meshPlane(100, 100, 8);
    l=geo.getEdgeList();
    sel=new UGeoSelector(geo);
    
    sel.add(geo.getGroup(rndInt(geo.sizeGroup())));
    log(geo.strGroup());
  }

  public void draw() {
    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();
    
    sel.draw();
    
    String s= null;
    s=sel.size()+" ";
    p.popMatrix();
    
    p.text(s,5,24);

  }

  public void keyPressed(char key) {
    if(key==TAB) {
      UFace rf=sel.getRndF();
      sel.add(rf);
//      sel.addConnected(rf);
    }
    if(key=='a') sel.addConnected();
    if(key=='d') geo.remove(rndInt(geo.sizeF()));

//    else if(key==TAB) sel=(sel+1)%l.size();
  }
}
