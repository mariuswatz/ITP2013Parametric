
class Particle {
  float x,y,rad;
  UGeo cyl;
  int col;
  int bufpos;
  
  Particle(int pos) {
    bufpos=pos;
    
//    x=random(width);
//    y=random(height);
    x=0;
    y=map(bufpos, 0,fftbuf.fftsize-1, -fftbuf.fftsize/2,fftbuf.fftsize/2);
    
    // create cylinder with width=1, height=1, 18 points around circle
//    cyl=UPrimitive.cylinder(1,0.5, 4,true);
    cyl=new UGeo();
    cyl.beginShape(QUAD_STRIP);
    UVertex pt=new UVertex(1,0,0);
    for(float i=0; i<37; i++) {
      cyl.vertex(pt.x,0.5,pt.z);
      cyl.vertex(pt.x,-0.5,pt.z);
      pt.rotY(radians(10));
    }
    cyl.endShape();
    
    col=randomColor();
  }
  
  void draw() {
 //   fill(map(bufpos, 0,fftbuf.fftsize-1, 100,255));
    rad=fftbuf.spectrum[bufpos]*10;
    
    pushMatrix();
    
    translate(x,y);
    if(drawColor) {
      fill(col);
      scale(rad,rad/10,rad);
      cyl.draw();
    }
    else {
      noFill();
      stroke(255);
      scale(rad,0.01,rad);
      cyl.draw();
    }
    
    popMatrix();
  }
}
