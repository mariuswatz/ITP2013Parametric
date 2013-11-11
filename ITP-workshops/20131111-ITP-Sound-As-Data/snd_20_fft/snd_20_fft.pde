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

FFTBuffer fft;

public void setup() {
  size(800, 400, OPENGL);
  //    sketchPath="C:/Users/marius/Dropbox/40 Teaching/2013 ITP/ITP-Workshops/ITP-Workshop-Sound/sketchbook/snd_01_mp3";

  minim=new Minim(this);
  initMusic();
  switchSource();

  // create FFT buffer
  fft=new FFTBuffer(this, bufSize, in.sampleRate());
  
  // set up FFT to use logarithmic averages
  // param #1: minimum bandwidth for an octave 
  // param #2: bands per octave
  fft.initAverages(88, 22);


  textFont(createFont("Arial", 10, false));  
  help=new ArrayList<String>();
  help.add("TAB = Switch source");
  help.add("SPACE = Toggle normaliztion");
}

public void draw() {
  background(0);

  processSound();
  fft.update(in.left, 1);
  fft.draw(50, height-120);

  drawDebug();
  drawWaveForm(50, height-50);
}

public void keyPressed( ) {
  if (key==TAB) switchSource();
  else if (key==' ') doNormalize=!doNormalize;
}

public void drawDebug() {
  fill(200);

  textAlign(LEFT);
  text(musicfile, 10, 15);
  String normStr="Normalized: "+doNormalize+" | ";
  text(normStr+"Buffer size: "+bufSize, 10, 25);

  textAlign(RIGHT);
  float y=15;
  fill(0, 255, 255);
  for (String s:help) {
    text(s, width-10, y);
    y+=10;
  }
}
