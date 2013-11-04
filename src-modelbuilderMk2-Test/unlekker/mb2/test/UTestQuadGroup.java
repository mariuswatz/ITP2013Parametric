/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestQuadGroup extends UTest {
  UVertexList vl;
  UGeo geo;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    vl=UVertexList.line(500, 11);
    geo=new UGeo().quadstrip(vl,vl.copy().translate(0,100,0));
    geo.center();
    
    UMB.log("groups "+geo.sizeGroup()+" f="+geo.sizeF()+
        " v="+geo.sizeV()+
        " | "+UMB.str(geo.getGroupID(0)));
    for(int i=0; i<geo.sizeF(); i++) {
      UMB.log(UMB.str(geo.getF(i).vID));
    }
    
  }

  public void draw() {
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();
    
    p.stroke(100);
    p.strokeWeight(1);
    p.fill(255);
    geo.draw();
    
    ArrayList<UVertex[]> res=geo.getGroupQuadV(0);
    
    int id=(p.frameCount/50)%(res.size());
    int vid1[]=geo.getF(id*2).vID;
    int vid2[]=geo.getF(id*2+1).vID;
    if(p.frameCount%100==0)
      UMB.log(id+" q="+res.size()+" group="+
          UMB.str(geo.getGroupID(0))+
        " vid="+UMB.str(vid1)+" "+UMB.str(vid2));
    p.fill(255,0,0);
    UMB.pquad(res.get(id));
    
  }

  
}
