import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

Minim minim;
AudioPlayer player;

AudioInput lineIn;

AudioSource input;

boolean doNormalized;

void setup() {
  size(1050,600);
  
  minim=new Minim(this);
  
  initMusic(); // reads in the music directory

  switchSource();  
}

void draw() {
  background(0);
  
  fill(255);
  text(musicfile, 15,20);  
  
  processSound();
  drawWaveForm();
}

void keyPressed() {
  if(key==TAB) switchSource();
  if(key==' ') doNormalized=!doNormalized;
}

// close all the sound inputs and stop Minim
void stop() {
  if(lineIn!=null) lineIn.close();
  if(player!=null) player.close();
  minim.stop();
}






