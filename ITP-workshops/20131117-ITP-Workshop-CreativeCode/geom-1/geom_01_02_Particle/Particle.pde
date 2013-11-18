
class Particle {
  UVertex v, vD;
  float aD, aDGoal;
  float r, rGoal, speedGoal;

  Particle() {
    v=new UVertex(width/2, height/2);
    vD=new UVertex(random(1, 3), 0);
    vD.rot(random(TWO_PI));

    newRotation();
  }

  void newRotation() {
    aDGoal=radians(random(0.5, 1.5));
    if (random(100)<50) aDGoal=-aDGoal;

    rGoal=random(10, 80);
    speedGoal=random(1, 5);

    // change speed by changing the magnitude of vD
    vD.norm(random(1, 3));
  }


  void draw() {
    // dampen aD, r and magnitude of vD
    r=dampen(r, rGoal, 0.05);
    aD=dampen(aD, aDGoal, 0.05);
    vD.norm(dampen(vD.mag(), speedGoal, 0.05));

    // add vD to v
    v.add(vD);
    vD.rot(aD);

    // wrap around
    //  if(v.x>width+r/2) v.x=-r/2;
    //  if(v.x<-r/2) v.x=width+r/2;
    //  if(v.y>height+r/2) v.y=-r/2;
    //  if(v.y<-r/2) v.y=height+r/2;

    // bounce
    if (v.x<0 || v.x>width || v.y<0 || v.y>height) {
      vD.rot(HALF_PI*0.5);
    }


    stroke(0);
    ellipse(v.x, v.y, r, r);

    // draw the vD direction vector
    stroke(255, 0, 0);
    line(v.x, v.y, v.x+vD.x*20, v.y+vD.y*20);

    if (frameCount%100==0) newRotation();
  }
}

