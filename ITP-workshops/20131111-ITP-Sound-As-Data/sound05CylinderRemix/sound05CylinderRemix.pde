import java.awt.Frame;
import controlP5.*;

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

import processing.opengl.*;

import controlP5.*;

import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

Minim minim;
AudioInput in;
AudioPlayer player;
String musicfile;


Particle part[];
FFTBuffer fftbuf;
UNav3D nav;
PApplet papplet;


void setup() {
  size(800,600, P3D);
  UMB.setPApplet(this);
  
  nav=new UNav3D();
  initGUI();
  
  minim=new Minim(this);
  papplet=this;
  
    findMusic();
  
  // loadFile will look in all the same places as loadImage does.
  // this means you can find files that are in the data folder and the 
  // sketch folder. you can also pass an absolute path, or a URL.

  musicfile=musicList.get((int)random(musicList.size()));
  player = minim.loadFile(musicFolder+"/"+musicfile);
  player.play(); // play the file
  initColors();

  fftbuf=new FFTBuffer(player.bufferSize(),player.sampleRate());
  

  part=new Particle[fftbuf.fftsize];
  for(int i=0; i<fftbuf.fftsize; i++) {
    part[i]=new Particle(i);
  }
}

void draw() {
  hint(ENABLE_DEPTH_TEST);
  background(0);
  
  pushMatrix();
  lights();
  translate(width/2,height/2,0);

  nav.doTransforms();
  
  noStroke();
  
  fftbuf.env.enabled=doEnvelope;
  fftbuf.update(player.mix);
  
  
  for(int i=0; i<fftbuf.fftsize; i++) {
    part[i].draw();
  }
  
  popMatrix();

  noLights();
  hint(DISABLE_DEPTH_TEST);
  fftbuf.draw();
  cp5.draw();
}
