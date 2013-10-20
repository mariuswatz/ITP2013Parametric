/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestHeading03 extends UTest {
  UGeo geo,geoColl;
  UVertex v1,v2;
  ArrayList<UVertexList> rows;
  
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    
    geo=new UGeo();
    buildRows(UMB.rndInt(6,10),20);//UMB.rndInt(5,12)*2);
    
    UVertexList prof=UVertexList.circle(12, 12);
//    prof=UVertexList.rect(10, 10);
    
    UVertexList last=null;
    for(UVertexList row:rows) {
      ArrayList<UVertexList> profiles=UHeading.sweep(row.close(), prof,0.08f);
//      profiles.add(profiles.get(0));
      geo.add(new UGeo().quadstrip(profiles));
      
      if(last!=null) {
        for(int i=0; i<row.size(); i++) {
          geo.add(UHeading.boxVector(row.get(i), last.get(i), 8));
        }
      }
      
      last=row;
    }
    
    
        
//        
//    
//    for(int i=0; i<path.size(); i++) {
//      UVertex v1=path.get(i);
//      UVertex v2=path2.get(i);
//      
//      v1.add(0,UMB.rndSigned(5, 30),0);
//      v2.add(0,UMB.rndSigned(5, 30),0);
//      geo.add(UHeading.boxVector(v1, v2, 5));
//      if(i>0) {
//        geo.add(UHeading.boxVector(v1, path.get(i-1), 5));
//        geo.add(UHeading.boxVector(v2, path2.get(i-1), 5));
//
//      }
//    }

    
    geo.center();
  }
  
  private void buildRows(int rowN,int steps) {
    float degMod=0.25f*(TWO_PI/(float)steps);
    float h=UMB.rnd(200,500);
    float hMod=0.25f*(h/(float)rowN);
    
    UVertexList path=UVertexList.circle(100, steps).rotX(HALF_PI);
    path.remove(path.size()-1);
    rows=new ArrayList<UVertexList>();
    
    for(int i=0; i<rowN; i++) {
      UVertexList thePath=path.copy().scale(UMB.rnd(1,1.5f));
      for(UVertex vv:thePath) {
        vv.mult(UMB.rnd(0.8f,1.2f));
//        vv.rotY(UMB.rndSigned(0.1f,1)*degMod);
        if(i>0) vv.y+=UMB.rndSigned(0.2f,1)*hMod;
      }
      
      thePath.translate(0,-UMB.map(i,0,rowN-1,0,h),0);
      rows.add(thePath);
    }
  }

  public void draw() {
    p.fill(255);
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    p.fill(255);
    geo.draw();
    
  }

  
}
