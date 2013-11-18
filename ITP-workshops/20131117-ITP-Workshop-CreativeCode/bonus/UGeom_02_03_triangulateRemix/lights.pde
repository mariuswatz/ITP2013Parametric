UVertex ldir[];
int lcol[];
float lMult;

void buildLights() {
  int n=(int)random(2,5);
  ldir=new UVertex[n];
  lcol=new int[n];
  lMult=random(0.4,0.8);
  lMult=0.3f+0.7f/(float)(n-1);
  
  for(int i=0; i<n; i++) {
    ldir[i]=new UVertex(0,0,-1).
      rotX(random(-1,1)*radians(45)).
      rotY(random(-1,1)*radians(45)).
      rotZ(random(TWO_PI));
    lcol[i]=randomColor();    
  }
  
  lcol[0]=color(255);
}

void customLights() {
  for(int i=0; i<ldir.length; i++) {
    directionalLight(
      red(lcol[i])*lMult,
      blue(lcol[i])*lMult,
      green(lcol[i])*lMult,
      ldir[i].x,ldir[i].y,ldir[i].z);
  }
}
