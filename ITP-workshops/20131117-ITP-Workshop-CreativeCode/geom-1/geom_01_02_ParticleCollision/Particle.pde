
class Particle {
  UVertex v, vD;
  float aD, aDGoal;
  float r, rGoal, speedGoal;

  Particle() {
    v=new UVertex(random(width), random(height));
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

  // check to see if this particle collides with
  // any other particle. if it does, return a
  // reference to the other particle. 
  // if it doesn't, then return null.
  Particle collision() {
    Particle tmp=null;
    
    for(Particle p : pt) if(p!=this) {
      float d=dist(v.x,v.y, p.v.x,p.v.y);
      float rLimit=(r/2)+p.r/2;
      if(d<rLimit) return p;
    }
    
    return tmp;
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

    // check for collisions
    Particle coll=collision();
    if(coll!=null) {
      float collA=radians(15);
      if(aD<0) collA=-collA;
      
      vD.rot(collA);
      
      // calculate the delta vector between
      // v and coll.v
      UVertex delta=v.delta(coll.v);
      noFill();
      
      UVertex bang=v.lerp(0.5,coll.v);
      bang=v;
      fill(255,100,0);
      ellipse(bang.x,bang.y, 100,100);
      
      delta.mult(-1).norm(2);
      v.add(delta);
      
      
    }

    fill(255);
    stroke(0);
    ellipse(v.x, v.y, r, r);

    // draw the vD direction vector
    stroke(255, 0, 0);
    line(v.x, v.y, v.x+vD.x*20, v.y+vD.y*20);

    if (frameCount%100==0) newRotation();
  }
}

