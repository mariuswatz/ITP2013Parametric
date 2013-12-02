package unlekker.snd;

import unlekker.mb2.util.UFile;
import unlekker.mb2.util.UMB;

import javax.sound.*; 
import javax.sound.sampled.*; 

import java.io.*; 
import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class USound extends UMB {
  float sampleRate;
  protected int channels,bytePerFrame;
  long frames;
  protected float dur;
  protected boolean isBigEndian,isStereo;
  
  public String filename,filenameShort;
  AudioInputStream audio=null;
  AudioFormat format;

  public int maxVal,minVal;
  
  public USound(String filename) {
    this.filename=filename;
    
    open();
  }
  
  public USound open() {
    try {
      File fileIn=new File(filename);
      if (!fileIn.exists() && papplet!=null) {
        filename=papplet.dataPath(filename);
        fileIn=new File(filename);
        filenameShort=UFile.noPath(filename);
      }
      
      log(fileIn.getAbsolutePath());
      audio=AudioSystem.getAudioInputStream(fileIn);
      getFormat();
      logDivider("\n"+str());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return this;
  }

  public AudioFormat getFormat() {
    format=audio.getFormat();
    sampleRate=format.getSampleRate();
    frames=audio.getFrameLength();
    
    isBigEndian=format.isBigEndian();
    channels=format.getChannels();
    isStereo=format.getChannels()>1;
    
    bytePerFrame=format.getFrameSize();
    
    dur=(float)frames/(float)sampleRate;
    return format;
  }

  public boolean isMono() {
    return channels<2;
  }

  public float duration() {
    return dur;
  }

  public int channels() {
    return channels;
  }

  public float sampleRate() {
    return sampleRate;
  }

  public void close() {
    try {
      if(audio!=null) audio.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  
  /////////////// FRAMES
  
  protected int bufSize=-1;
  byte[] bufRead;
  int[] bufReadInt;
  protected ArrayList<float[]> buf;
  private int bytesPerBuffer;
  private int bufFrame=0,bufFrameCount=-1;
  
  public USound bufferSize(int bufSize) {
    this.bufSize=bufSize;
    bytesPerBuffer=bufSize*bytePerFrame;
    logf("%d %d %d",bufSize,channels,bytePerFrame);
    
    buf=new ArrayList<float[]>();
    for(int i=0; i<channels; i++) buf.add(new float[bufSize]);
    
    bufFrameCount=(int)(frames/bufSize);
    
    return this;
  }

  public int getBufFrameCount() {
    return bufFrameCount;
  }

  public int getBufFrame() {
    return bufFrame;
  }

  public float getBufTime() {
    return map(bufFrame,0,bufFrameCount-1, 0,dur);
  }

  public boolean nextBuffer() {
    if(bufRead==null || bufRead.length!=(bytesPerBuffer)) {
      bufRead=new byte[bytesPerBuffer];
      bufReadInt=new int[bytesPerBuffer/2];
    }
    
    long nBytesRead=-1;
    
    try {
      nBytesRead=audio.read(bufRead, 0, bufRead.length);
    } 
    catch (IOException e) {
      e.printStackTrace();
    }
    
    if(bufFrame%50==0 || bufFrame==bufFrameCount-1) {
      log("bufFrame "+bufFrame+"/"+bufFrameCount+" | "+nBytesRead+" "+bufReadInt.length);
      logf("%d > %d",minVal,maxVal);
    }
    
    bufFrame++;


    if(nBytesRead!=bytesPerBuffer) return false;
    
    int cnt=0,byteCnt=0;
    
    byte b1,b2;
    for(int i=0; i<bufReadInt.length; i++) {
      b1=bufRead[byteCnt++];
      b2=bufRead[byteCnt++];
      bufReadInt[i]=(isBigEndian ?
          (b1<<8)|(b2&0xff) :
          (b2<<8)|(b1&0xff)            
          );          
      
      maxVal=max(bufReadInt[i],maxVal);
      minVal=min(bufReadInt[i],minVal);
    }
    
    int chID=0;
    cnt=0;
    
    for(int val : bufReadInt) {      
      buf.get(chID++)[cnt]=val;
      if(chID==channels) {
        chID=0;
        cnt++;
//        if(cnt%10==0) log("chID "+chID+" "+cnt+" | "+
//            channels+" "+(channels*cnt)+
//            " "+cnt+" "+bufRead.length);
      }      
      
    }
    
    
    return true;    
  }
  
  public float[] getBuffer(int id) {
    return buf.get(id);
  }

  public String str() {
    return String.format("%s\n%dhz, %.2f sec, %d ch, %d frames (%d bytes), %d bit",
        filenameShort,(int)sampleRate,dur,channels,frames,bytePerFrame,format.getSampleSizeInBits());
  }
  
  
}
