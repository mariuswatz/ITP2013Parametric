ArrayList<PVector> p1,p2;

void setup() {
  size(600,600);

  p1=new ArrayList<PVector>();
  p2=new ArrayList<PVector>();
  
  int n=100;
  randomPositions(p1, n);
  randomPositions(p2, n);
  
}

void randomPositions(ArrayList<PVector> pt,int n) {
  // clear the arraylist
  pt.clear();
  
  for(int i=0; i<n; i++) {
    PVector v=new PVector(
      random(width),
      random(height),
      random(20,50));
      
    // add to the list
    pt.add(v);
  }
}

void draw() {
//  background(0);
  
  float perc=0.01;
  
  for(int i=0; i<p1.size(); i++) {
    PVector v=p1.get(i);
    v.x=dampen(v.x, p2.get(i).x, perc);
    v.y=dampen(v.y, p2.get(i).y, perc);
    v.z=dampen(v.z, p2.get(i).z, perc);
    
    ellipse(v.x,v.y, v.z,v.z);
  }
}

void mousePressed() {
  randomPositions(p2, p1.size());
}

// a=start, b=goal
float dampen(float a,float b,float perc) {
  return a*(1-perc)+b*perc;
}

  
  
  
  
  
  
  
  
  
