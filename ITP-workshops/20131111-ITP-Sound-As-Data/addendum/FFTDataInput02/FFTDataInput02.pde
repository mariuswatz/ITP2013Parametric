// FFTDataOutput.pde
// Marius Watz - http://workshop.evolutionzone.com
// 
// Performs analysis on a sound file and saves it to a
// comma-separated text file (CSV).

import javax.sound.*;
import javax.sound.sampled.*;
import java.io.*;

int sndFrame=0;
SoundData snd;
PFont fnt;

void setup() {
  size(512,300);
  
  snd=new SoundData(25,256,0.1f);
  
  String sndfile="1 Atomic - Pain Body.wav";
  sndfile="1471_nicStage_churchBellTapeFails.aif";

  snd.readSoundFile(sndfile);

  sndfile=sndfile.substring(0,sndfile.length()-4);
  
  snd.setFFTEqualizer(true);
  snd.setFFTEnvelope(true, 1.2f);
  snd.calcFFT();
  snd.writeFFTSpectrogram(snd.numFrames,sndfile+" Spectrogram.png");
  snd.writeFFT(sndfile+".csv");
  
/*

  snd.readFFT(sndfile+".csv");    
*/
  fnt=createFont("Courier",18,false);  
  frameRate(25);
}

void draw() {
  background(0);
  
  if(sndFrame>-1) {
    textFont(fnt,18);
    fill(255);
    noStroke();
  
    noFill();
    stroke(255,0,0);
    float max=0;
    for(int i=0; i<snd.spectrumLength; i++) {
      if(max<snd.spectrum[sndFrame][i]) max=snd.spectrum[sndFrame][i];
      line(i,height,i,height-height*snd.spectrum[sndFrame][i]);
    }

    text("Frame "+sndFrame+" Max: "+nf(max,0,3),20,20);
  }
  
  sndFrame=(sndFrame+1);
  if(sndFrame==snd.numFrames) sndFrame=-25;
}
