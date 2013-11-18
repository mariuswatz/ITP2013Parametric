import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

ArrayList<Particle> pt;


void setup() {
  size(600,600);
  
  pt=new ArrayList<Particle>();
  int n=30;
  
  // add 'n' number of Particles to 'pt'
  while(pt.size()<n) pt.add(new Particle());
  
}

void draw() {
//  background(0);
  
  // oldskool
  //for(int i=0; i<pt.size(); i++) pt.get(i).draw();
  
  // newskool
  for(Particle p : pt) p.draw();
}

float dampen(float a,float b,float perc) {
  return a*(1-perc)+b*perc;
}

