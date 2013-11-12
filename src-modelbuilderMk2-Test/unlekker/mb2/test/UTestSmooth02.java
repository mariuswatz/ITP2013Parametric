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
  ArrayList<UVertexList> stack,stackNoSmooth,stackSmooth;
  UVertexList vl,vlNoSmooth,vlSmooth;
  UGeo geo,geoSmooth;
  int smoothLevel;
  
  UVertex a,b,ii;
  float R=400;
  
  int drawType=0;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();
    
    vl=new UVertexList();
    int n=rndInt(9,37)*2;
    float m=UMB.rnd(100,300);
    for(int i=0; i<n; i++) {
      m=m*0.2f+UMB.rnd(100,300)*0.8f;
      float t=-UMB.map(i, 0, n, 0, TWO_PI);
      vl.add(new UVertex(m,0).rotY(t));
    }
        
    PGraphics pdf=p.createGraphics(1000, 1000,p.PDF,this.getClass().getSimpleName()+".pdf");
    UMB.setGraphics(pdf);
    pdf.beginDraw();
    pdf.stroke(0);
    pdf.translate(vl.bb().dimX(), vl.bb().dimZ());
    vl.copy().rotX(HALF_PI).draw();
    
    vlNoSmooth=vl.copy().close();

    vl=UVertexList.smooth(vl.close(), 2);
    
    pdf.endDraw();
    pdf.flush();
    pdf.dispose();

    UMB.setGraphics(p);
    
    stack=new ArrayList<UVertexList>();
    stackNoSmooth=new ArrayList<UVertexList>();
    
    n=10;
    m=p.random(0.25f,0.5f);
    for(int i=0; i<n; i++) {
      m=m*0.2f+p.random(0.5f,2f)*0.8f;
      if(i==n-1) m=p.random(0.25f,0.5f);
      
      float y=UMB.map(i, 0, n-1, 0,1000);
      stack.add(vl.copy().scale(m).translate(0,y,0));
      stackNoSmooth.add(
          vlNoSmooth.copy().scale(m).translate(0,y,0));
    }

    UVertexList.center(stack);
    UVertexList.center(stackNoSmooth);
    
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
    
    
    int cnt=0;
    
    if(drawType==(cnt++)) {
      UMB.pstroke(colOutline).pnoFill();
      UVertexList.draw(stackNoSmooth);
    }
    else if(drawType==(cnt++)) {
      UMB.pstroke(colOutline).pnoFill();
      UVertexList.draw(stack);
    }
    else if(drawType==(cnt++)) {
      UMB.pstroke(colOutline).pnoFill();
      UVertexList.draw(stackSmooth);
    }
    else if(drawType==(cnt++)) {
      UMB.pfill(p.color(255)).pnoStroke();
      geo.draw();
      
//      geo.pstroke(0xffff0000);
//      geo.drawNormals(20);

      UMB.pstroke(colOutline).pnoFill();
      UMB.draw(stack);

    }
    else if(drawType==(cnt++)) {
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
    if(key==p.TAB) {
      if(main.keyEvent.isShiftDown()) 
        drawType=(drawType>0 ? drawType-1 : 4);
      else drawType=(drawType+1)%5;
    }
  }
  
}
