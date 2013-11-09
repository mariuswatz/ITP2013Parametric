/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UFile;
import unlekker.mb2.util.UMB;

public class UTestSmooth02 extends UTest {
  ArrayList<UVertexList> stack,stackSmooth;
  UVertexList vl,vlSmooth;
  UGeo geo,geoSmooth;
  int smoothLevel;
  
  UVertex a,b,ii;
  float R=400;
  
  int drawType=0;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();
    
    vl=new UVertexList();
    int n=rndInt(6,16)*2;
    for(int i=0; i<n; i++) {
      float t=UMB.map(i, 0, n, 0, TWO_PI);
      vl.add(new UVertex(UMB.rnd(50,300),0).rotY(t));
    }
    
    
    PGraphics pdf=p.createGraphics(1000, 1000,p.PDF,this.getClass().getSimpleName()+".pdf");
    UMB.setGraphics(pdf);
    pdf.beginDraw();
    pdf.stroke(0);
    pdf.translate(vl.bb().dimX(), vl.bb().dimZ());
    vl.copy().rotX(HALF_PI).draw();
    

    vl=UVertexList.smooth(vl.close(), 1);
//  pdf.translate(vl.bb().dimX(), 0);
  vl.copy().rotX(HALF_PI).draw();
  vl=UVertexList.smooth(vl.close(), 4);
//pdf.translate(vl.bb().dimX(), 0);
vl.copy().rotX(HALF_PI).draw();
    
    pdf.endDraw();
    pdf.flush();
    pdf.dispose();

    UMB.setGraphics(p);
    
    stack=new ArrayList<UVertexList>();
    n=10;
    float m=p.random(0.5f,2f);
    for(int i=0; i<n; i++) {
      m=m*0.2f+p.random(0.5f,2f)*0.8f;
      float y=UMB.map(i, 0, n-1, 0,1000);
      stack.add(vl.copy().scale(m).translate(0,y,0));
    }
    UVertexList.center(stack);
    
    stackSmooth=UVertexList.smooth(stack,2);
    geo=new UGeo().quadstrip(stack);    
    geoSmooth=new UGeo().quadstrip(stackSmooth);
  }
  
  public void draw() {
    main.stroke(255,200,0);
//    main.drawGrid();
    p.stroke(0);
    p.noStroke();

    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    int colOutline=p.color(255,100,0);
    
    if(drawType==0) {
      UMB.pstroke(colOutline).pnoFill();
      UVertexList.draw(stack);
    }
    else if(drawType==1) {
      UMB.pstroke(colOutline).pnoFill();
      UVertexList.draw(stackSmooth);
    }
    else if(drawType==2) {
      UMB.pfill(p.color(255)).pnoStroke();
      geo.draw();

      UMB.pstroke(colOutline).pnoFill();
      UMB.draw(stack);

    }
    else if(drawType==3) {
      UMB.pfill(p.color(255)).pnoStroke();
      geoSmooth.draw();
      
      UMB.pstroke(colOutline).pnoFill();
      UMB.draw(stackSmooth);
    }

    p.popMatrix();
    
    String str="Closed? "+(vl.isClosed()+
        " smoothLevel"+smoothLevel);
    p.text(str, 10, p.height-5);
    
    if(main.saveFilename!=null) {
      log("main.saveFilename "+main.saveFilename);
      
      String fname=UFile.noExt(main.saveFilename)+".stl";
      log("fname "+fname);

      
      ArrayList<UGeo> l=new ArrayList<UGeo>();
      l.add(geo);
      l.add(geoSmooth);
      
      UGeoIO.writeSTL(fname, l);
      UGeoIO.writeSTL(fname+"-1.stl", geo);
      main.saveFilename=null;
    }
    
    
  }

  public void keyPressed(char key) {
    if(key==p.TAB) drawType=(drawType+1)%4;
  }
  
}
