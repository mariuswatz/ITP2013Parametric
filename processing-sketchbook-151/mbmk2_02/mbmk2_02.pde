import unlekker.mb2.test.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

import processing.opengl.*;


  ArrayList<UVertexList> vvl;
  
  UVertexList vl,vl2;
  UGeo geo;
  
  public void setup() {
    size(600,600, OPENGL);
    
    vvl=new ArrayList<UVertexList>();
    
    UVertexList.setGraphics(g);
    vl=new UVertexList();
    vl.add(0,0,0);
    vl.add(100,0,0);
    vl.add(100,100,0);
    vl.add(0,100,0);
    vl.add(0,0,0);
    
    println(""+vl.centroid(true).str()+" "+vl.str());

    vl.translate(-50, -150, 0);
    for(int i=0; i<6; i++) {
      vl.rotX(radians(60)).translate(0, 0, 20);
      vvl.add(vl.copy());
      
      println(vl.str());
    }

    geo=new UGeo();
    geo.enable(geo.COLORFACE);
    geo.quadstrip(vvl);
    geo.triangleFan(vvl.get(0));
    geo.triangleFan(vvl.get(vvl.size()-1));
    
    geo.translate(200,0,0);
    geo.rotY(HALF_PI*0.5f);
    
    int n=geo.getFaces().size();
    
    int id=0;
    
    ArrayList<String> ss=new ArrayList<String>();
    
    for(UFace f:geo.getFaces()) {
      float v=map(id++,0,n-1,0,255);
      f.setColor(v*255,v*100,0);
      f.getVertices()[0].add(random(-15,15),0,0);
      ss.add(f.hex(f.col));
    }

    println(UGeo.str(ss));
    vl.log(""+vl.size()+" "+vl.allowCopy()+" "+vl.allowDupl());
    
    geo.enable(geo.COLORFACE);
    println(geo.optionStr());
  }

  public void draw() {
    background(0);
    
    translate(width/2,height/2);
    rotateY(radians(frameCount));
    stroke(255);
    noFill();
    vl.draw();
    
    lights();
    fill(255);
    geo.draw();
  }

