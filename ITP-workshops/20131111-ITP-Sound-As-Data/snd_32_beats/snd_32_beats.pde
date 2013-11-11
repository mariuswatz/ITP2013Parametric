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
BeatDetect beat;

float eRadius;

public void setup() {
  size(800, 400, OPENGL);

  minim=new Minim(this);
  initMusic();
  switchSource();

  textFont(createFont("Arial",10,false));  
  help=new ArrayList<String>();
  help.add("TAB = Switch source");
  help.add("SPACE = Toggle normaliztion");
  
      beat = new BeatDetect();
  beat.setSensitivity(10);  // milliseconds  
//  beat.detectMode(BeatDetect.SOUND_ENERGY);

}

public void draw() {
  background(0);

  processSound();

  drawDebug();
  drawWaveForm(height-100);
  
      beat.detect(bufL);
  float a = map(eRadius, 20, 80, 60, 255);
  fill(0,255,255, a);
  if ( beat.isOnset() ) eRadius = 200;
  ellipse(width-150, (height-100)/2+100, eRadius, eRadius);
  eRadius *= 0.9;
  if ( eRadius < 2 ) eRadius = 2;

}

void keyPressed( ) {
  if (key==TAB) switchSource();
  else if (key==' ') doNormalize=!doNormalize;
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
