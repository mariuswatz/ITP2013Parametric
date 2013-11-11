import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

Minim minim;
AudioSource in;
AudioPlayer player;

public void setup() {
  size(1050, 400, OPENGL);

  minim=new Minim(this);

  initMusic();  
  musicfile=rndSoundFile();

  int bufSize=1024;
  player = minim.loadFile(musicFolder+"/"+musicfile, bufSize);
  player.play();

  in=(AudioSource)player;
}

public void draw() {
  background(0);

  processSound();

  drawDebug();
  drawWaveForm(height);
  
}

void drawDebug() {
  fill(200);
  textAlign(LEFT);
  text("Volume L: "+nf(volL*100, 1, 2), 10, 20);
  text("Volume R: "+nf(volR*100, 1, 2), 10, 32);
  text("Volume Max: "+nf(volMax*100, 1, 2), 10, 44);

  textAlign(RIGHT);
  text(musicfile, width-10, 20);
  text("Buffer size: "+bufSize, width-10, 32);
}
