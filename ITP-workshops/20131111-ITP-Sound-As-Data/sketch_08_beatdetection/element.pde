
float bez[];

void initBezier() {
  bez=new float[8];
  for(int i=0; i<4; i++) {
    bez[i*2]=(float)i*0.33;
    bez[i*2+1]=random(-0.5,0.5);
  }  
}

void drawBezier(float x,float y, float sz) {
  pushMatrix();
  translate(x,y);
  scale(sz);
  strokeWeight(1f/sz);
  rotate(map(sz, 0,1, -radians(10),radians(10)));
  
  
  bezier(
    bez[0],bez[1],
    bez[2],bez[3],
    bez[4],bez[5],
    bez[6],bez[7]);
 
  popMatrix(); 
}
