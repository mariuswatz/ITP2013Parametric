import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

Minim minim;
AudioSource in;
AudioInput lineIn;
AudioPlayer player;

ArrayList<String> help;

ArrayList<Particle> particle;
float waveH=100;

public void setup() {
  size(800, 400, OPENGL);

  minim=new Minim(this);
  initMusic();
  switchSource();
  
  particle=new ArrayList<Particle>();
  for(int i=0; i<bufSize/2; i++) {
    particle.add(new Particle(i*2));
  }
  
  textFont(createFont("Arial",10,false));  
  help=new ArrayList<String>();
  help.add("TAB = Switch source");
  help.add("SPACE = Toggle normaliztion");
}

public void draw() {
  background(0);

  processSound();

  noStroke();
  fill(0,255,255);
  for(Particle pt:particle) pt.draw();
  
  drawDebug();
  drawWaveForm(waveH);
}

void keyPressed( ) {
  if(key==TAB) switchSource();
  else if(key==' ') doNormalize=!doNormalize;
}

void drawDebug() {
  fill(200);
  textAlign(LEFT);
  pushMatrix();
  translate(0,5);
  
  text("Volume L: "+nf(volL*100, 1, 2), 10, 10);
  text("Volume R: "+nf(volR*100, 1, 2), 10, 20);
  text("Volume Max: "+nf(volMax*100, 1, 2), 10, 30);

  textAlign(RIGHT);
  text(musicfile, width-10, 10);
  String normStr="Normalized: "+doNormalize+" | ";
  text(normStr+"Buffer size: "+bufSize, width-10, 20);
  
  float y=40;
  fill(0,255,255);
  for(String s:help) {
    text(s,width-10,y);
    y+=10;
  }
  
  popMatrix();
}
