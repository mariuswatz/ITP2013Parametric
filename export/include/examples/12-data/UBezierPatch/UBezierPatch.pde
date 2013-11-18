/*
 ModelbuilderMk2 - UTriangulate_01.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 Code for ITP: Parametric Design for Digital Fabrication, Fall 2013
 
 Demo of UBezierPatch. Sets up a 2D grid of control points
 as a ArrayList<UVertexList> and calculates the resulting
 patch surface.
 */

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

boolean doDrawModel=true;

ArrayList<UVertexList> vvl;
UGeo geo;
UNav3D nav;
ArrayList<UVertexList> stack;
UBezierPatch bez;


public void setup() {
  size(600, 600, OPENGL);

  // set PApplet reference
  UMB.setPApplet(this);

  // create navigation tool
  nav=new UNav3D();


  build();
}

public void draw() {
  background(0);
  drawCredit();
  String s=stack.size()+" x "+stack.get(0).size();
//  s+=" | mesh res="+bez.resu;
  text(s, 5, height-5);

  translate(width/2, height/2);
  lights();

  // execute navigation transforms
  nav.doTransforms();

  noFill();
  stroke(255, 0, 0);

  // draw control point stack as grid
  UMB.draw(stack, true);

  fill(255f, 100);
  stroke(100);
  ;
  if (mousePressed) {
    noFill();
    stroke(50);
  }

  geo.draw();
}
