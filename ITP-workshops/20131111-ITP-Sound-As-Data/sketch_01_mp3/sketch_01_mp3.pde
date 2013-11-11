import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

Minim minim;
AudioPlayer player;

void setup() {
  size(1050,600);
  
  minim=new Minim(this);
  
  initMusic(); // reads in the music directory
  musicfile=rndSoundFile();
  
  player=minim.loadFile(musicFolder+"/"+musicfile);
  player.play();
  
}

void draw() {
  background(0);
  
  fill(255);
  text(musicfile, 15,20);  
}






