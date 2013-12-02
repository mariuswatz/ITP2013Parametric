package unlekker.snd;

import processing.core.PApplet;
import unlekker.mb2.data.UPeakFollow;
import unlekker.mb2.util.UFile;
import unlekker.mb2.util.UMB;

public class SoundDataTest extends PApplet {
  USound snd;
  int bufSize=1024;
  UPeakFollow peak;
  UFFT fft;
  
  public void setup() {
    size(bufSize,600);
    UMB.setPApplet(this);
    peak=new UPeakFollow().setDamping(0.1f);
    println(peak.str());
    fft=new UFFT(bufSize/2);
    fft.useEnvelope(true, 1.2f);
    fft.useEqualizer(true);
    
//    peak.set(0.02f,100, 50000);
    
    sketchPath=UFile.getCurrentDir();
    String name="Ryoji Ikeda 03 Headphonics 1_0.wav";
//    name="1471_nicStage_churchBellTapeFails.aif";
    snd=new USound(name);
    snd.bufferSize(bufSize);
    frameRate(10);
  }
  
  float[] bufF;
  
  public void draw() {
    boolean res=snd.nextBuffer();
    if(res==false) {
      snd.close();
      exit();
    }
    
    background(0);
    noFill();
    stroke(255,255,0);
    
    float[] buf=fft.computeFFT(snd.getBuffer(0));
    int y=120+200;
    line(0,y-60,width,y-60);
    float mx=0,avg=0;
    
    for(int j=0; j<buf.length; j++) {
      float val;
//      val=(float)buf[j]/22050f;
      val=buf[j]*50;
      mx=max(mx,abs(val));
      avg+=abs(val);
//      mx=max(abs(peak.max()),val);
      line(j,y,j,y+val);
    }
    
    avg/=(float)buf.length;
    fill(255);
    text(buf.length+" mx "+nf(mx,1,2)+" "+nf(avg,1,2),
        20,15);


//    drawOld();
  }

  private void drawOld() {
    float mx=0;
    
    for(int i=0; i<snd.channels(); i++) {
      float[] buf=snd.getBuffer(i);
      bufF=buf;
//      bufF=UMB.toFloat(buf,bufF);
      if(frameCount%20==0) {
        println(frameCount+" "+min(bufF)+" "+max(bufF)+" | "+
            min(buf)+" "+max(buf)
            );
      }
      
      peak.update(bufF);
//      println(min(bufF)+" "+max(bufF)+" | "+peak.str());
      bufF=peak.norm(bufF);
//      println(min(bufF)+" "+max(bufF)+" | "+peak.str());
      
      int y=i*120+200;
      line(0,y-60,width,y-60);
      
      for(int j=0; j<bufF.length; j++) {
        float val;
//        val=(float)buf[j]/22050f;
        val=bufF[j]*50;
        mx=max(abs(peak.max()),val);
        line(j,y,j,y+val);
      }
    }

    fill(255);
    text((snd.getBufFrame()+1)+" / "+snd.getBufFrameCount()+" "+
        nf(snd.getBufTime(),0,2)+"s / "+nf(snd.duration(),0,2)+
        "    "+nf(frameRate,0,1)+
        "   max "+nf(mx,1,2)+
        "\n    "+peak.str()+
        "\n"+
        snd.str(),20,15);
    
    if(frameCount%20==0) {
      println(frameCount+" "+min(bufF)+" "+max(bufF));
    }
  }
  
  static public void main(String arg[]) {
    
    PApplet.main("unlekker.snd.FFTDataToCSV");
//    PApplet.main("unlekker.snd.SoundDataTest");
  }
  
}
