package unlekker.snd;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

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

public class FFTDataToCSV02 extends PApplet {

// FFTDataOutput.pde
// Marius Watz - http://workshop.evolutionzone.com
// 
// Performs analysis on a sound file and saves it to a
// comma-separated text file (CSV).





int sndFrame=0;
SoundData snd;
PFont fnt;

public void setup() {
  size(600,300);
  
  initColors();
  
  
  // number of fft bands, damping factor (0.1f == 10% of new value per frame)
  snd=new SoundData(256,0.1f);
  
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

public void draw() {
  background(0);
  
  int x=0;
  noStroke();
  for(int cc:colors) {
    fill(cc);
    rect(x++,0,1,20);
  }
  
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
//////////////////////////////////////////////
// FFT classes from Karsten Schmidt aka Toxi

public class FFT {
  // useful goodness

  static final float PI = (float) Math.PI;
  static final float HALF_PI    = PI / 2.0f;
  static final float THIRD_PI   = PI / 3.0f;
  static final float QUARTER_PI = PI / 4.0f;
  static final float TWO_PI     = PI * 2.0f;

  static final float DEG_TO_RAD = PI/180.0f;
  static final float RAD_TO_DEG = 180.0f/PI;

  int WINDOW_SIZE,WS2;
  int BIT_LEN;
  int[] _bitrevtable;
  float _normF;
  float[] _equalize;
  float[] _envelope;
  float[] _fft_result;
  float[][] _fftBuffer;
  float[] _cosLUT,_sinLUT;
  float[] _FIRCoeffs;
  boolean _isEqualized, _hasEnvelope;


  public FFT(int windowSize) {
    System.out.println("FFT windowSize "+windowSize);
    WINDOW_SIZE=WS2=windowSize;
    WS2>>=1;
    BIT_LEN = (int)(Math.log((double)WINDOW_SIZE)/0.693147180559945f+0.5f);
    _normF=2f/WINDOW_SIZE;
    _hasEnvelope=false;
    _isEqualized=false;
    initFFTtables();
  }


  public void initFFTtables() {
    _cosLUT=new float[BIT_LEN];
    _sinLUT=new float[BIT_LEN];
    _fftBuffer=new float[WINDOW_SIZE][2];
    _fft_result=new float[WS2];


    // only need to compute (float)Math.sin/Math.cos at BIT_LEN angles
    float phi=PI;
    for(int i=0; i<BIT_LEN; i++) {
      _cosLUT[i]=(float)Math.cos(phi);
      _sinLUT[i]=(float)Math.sin(phi);
      phi*=0.5f;
    }


    // precalc bit reversal lookup table ala nullsoft
    int i,j,bitm,temp;
    _bitrevtable = new int[WINDOW_SIZE];


    for (i=0; i<WINDOW_SIZE; i++) _bitrevtable[i] = i;
    for (i=0,j=0; i < WINDOW_SIZE; i++) {
      if (j > i) {
        temp = _bitrevtable[i];
        _bitrevtable[i] = _bitrevtable[j];
        _bitrevtable[j] = temp;
      }
      bitm = WS2;
      while (bitm >= 1 && j >= bitm) {
        j -= bitm;
        bitm >>= 1;
      }
      j += bitm;
    }
  }


  // taken from nullsoft VMS
  // reduces impact of bassy freqs and slightly amplifies top range
  public void useEqualizer(boolean on) {
    _isEqualized=on;
    if (on) {
      int i;
      float scaling = -0.02f;
      float inv_half_nfreq = 1.0f/WS2;
      _equalize = new float[WS2];
      for (i=0; i<WS2; i++) _equalize[i] = scaling * (float)Math.log( (double)(WS2-i)*inv_half_nfreq );
    }
  }


  // bell filter envelope to reduce artefacts caused by the edges of standard filter rect
  // 0.0 < power < 2.0
  public void useEnvelope(boolean on, float power) {
    _hasEnvelope=on;
    if (on) {
      int i;
      float mult = 1.0f/(float)WINDOW_SIZE * TWO_PI;
      _envelope = new float[WINDOW_SIZE];
      if (power==1.0f) {
        for (i=0; i<WINDOW_SIZE; i++) _envelope[i] = 0.5f + 0.5f*(float)Math.sin(i*mult - HALF_PI);
      } else {
        for (i=0; i<WINDOW_SIZE; i++) _envelope[i] = (float)Math.pow(0.5f + 0.5f*(float)Math.sin(i*mult - HALF_PI), power);
      }
    }
  }


  // compute actual FFT with current settings (eq/filter etc.)
  public float[] computeFFT(float[] waveInData) {
    float  u_r,u_i, w_r,w_i, t_r,t_i;
    int    l, le, le2, j, jj, ip, ip1, i, ii, phi;


    // ensure that we don't overwrite old results - Watz 21.03.05
    _fft_result=new float[WS2];

    // check if we need to apply window function or not
    if (_hasEnvelope) {
      for (i=0; i<WINDOW_SIZE; i++) {
        int idx = _bitrevtable[i];
        if (idx < WINDOW_SIZE) _fftBuffer[i][0] = waveInData[idx]*_envelope[idx];
        else  _fftBuffer[i][0] = 0;
        _fftBuffer[i][1] = 0;
      }
    } else {
      for (i=0; i<WINDOW_SIZE; i++) {
        int idx = _bitrevtable[i];
        if (idx < WINDOW_SIZE) _fftBuffer[i][0] = waveInData[idx];
        else  _fftBuffer[i][0] = 0;
        _fftBuffer[i][1] = 0;
      }
    }


    for (l = 1,le=2, phi=0; l <= BIT_LEN; l++) {
      le2 = le >> 1;
      w_r = _cosLUT[phi];
      w_i = _sinLUT[phi++];
      u_r = 1f;
      u_i = 0f;
      for (j = 1; j <= le2; j++) {
        for (i = j; i <= WINDOW_SIZE; i += le) {
          ip  = i + le2;
          ip1 = ip-1;
          ii  = i-1;
          t_r = _fftBuffer[ip1][0] * u_r - u_i * _fftBuffer[ip1][1];
          t_i = _fftBuffer[ip1][1] * u_r + u_i * _fftBuffer[ip1][0];
          _fftBuffer[ip1][0] = _fftBuffer[ii][0] - t_r;
          _fftBuffer[ip1][1] = _fftBuffer[ii][1] - t_i;
          _fftBuffer[ii][0] += t_r;
          _fftBuffer[ii][1] += t_i;
        }
        t_r = u_r * w_r - w_i * u_i;
        u_i = w_r * u_i + w_i * u_r;
        u_r = t_r;
      }
      le<<=1;
    }
    // normalize bands or apply EQ
    float[] currBin;
    if (_isEqualized) {
      for(i=0; i<WS2; i++) {
        currBin=_fftBuffer[i];
        _fft_result[i]=_equalize[i]*(float)Math.sqrt(currBin[0]*currBin[0]+currBin[1]*currBin[1]);
      }
    } else {
      for(i=0; i<WS2; i++) {
        currBin=_fftBuffer[i];
        _fft_result[i]=_normF*(float)Math.sqrt(currBin[0]*currBin[0]+currBin[1]*currBin[1]);
      }
    }
    return _fft_result;
  }
}
////////////////////////////////////////////////
// Utility class to normalize and dampen FFT
// values to a more useful form.

class FFTHelper {
  public int num,numbands;
  public float spectrum[],ttv[];
  public double v[];
  private double  tv[];
  double dampUp,dampDown,mult;
  boolean doDampen=false,doDampenAsymm=false,doAverage=false,isFirstTime=true,doNorm=false;

  // variables dealing with the calculation of maximum value
  public double maxinternal;
  public float max,maxMinimum,maxMaximum;
  double maxD;
  int maxCnt;

  double bandv[];
  public float band[],bandmax[];
  public int bandsteps;

  public FFTHelper(int _n, int _nbands,boolean doAvg) {
    num=_n;
    numbands=_nbands;
    spectrum=new float[num]; // Visible outside - normalized values
    v=new double[num]; // Visible outside -  - unmodified values
    tv=new double[num]; // Private - temp values
    ttv=new float[num]; // Private - temp values
    doAverage=doAvg;
    doNorm=true;
    mult=1;

    maxMinimum=100;
    maxMaximum=2000;
    maxinternal=maxMinimum;
    maxCnt=0;

    bandsteps=256/numbands;
    band=new float[numbands]; // Visible outside - normalized bands
    bandv=new double[numbands]; // Visible outside - unmodified bands
    bandmax=new float[numbands];
  }

  public void setMaxLimits(float _min,float _max) {
    maxMinimum=_min;
    maxMaximum=_max;
  }

  public void setDamper(double _dup,double _ddown) {
    dampUp=_dup;
    dampDown=_ddown;
    if(dampUp==-1) doDampen=false;
    else doDampen=true;
    doDampenAsymm=true;
  }

  public void setDamper(double _d) {
    setDamper(_d,_d);
    doDampenAsymm=false;
  }

  public float dampenVal(double oldval,double newval) {
    if(doDampenAsymm) {
      if (oldval>newval) oldval=newval*dampDown+oldval* (1-dampDown);
      else oldval=newval*dampUp+oldval* (1-dampUp);
    }
    else oldval=newval*dampUp+oldval* (1-dampUp);
    return (float)oldval;
  }

  public void update(float [] nv) {
    if(isFirstTime) isFirstTime=false;

    if(doAverage) {
      tv[0]=(nv[0]+nv[1])*0.5f;
      tv[num-1]=(nv[num-1]+nv[num-2])*0.5f;
      for(int i=1; i<num-1; i++) tv[i]=(nv[i-1]+nv[i]+nv[i+1])*0.333f;
    }
    else for(int i=0; i<num; i++) tv[i]=nv[i];

    // dampen values if requested
    if(doDampen && !isFirstTime) {
      if(doDampenAsymm)
        for (int i=0; i<num; i++) {
          if(v[i]>tv[i]) v[i]= tv[i]*dampDown+v[i]*(1-dampDown);
          else v[i]= tv[i]*dampUp+v[i]*(1-dampUp);
        }
      else {
        for (int i=0; i<num; i++) {
          v[i]=tv[i]*dampUp+v[i]* (1-dampUp);
          if(v[i]>maxMaximum) v[i]=maxMaximum;
        }
      }
    } else // just copy values
        System.arraycopy(tv,0, v,0, num);

    if(doNorm) {
      double currmax=0;
      double avg=0;
      if (maxCnt>0) {
        maxinternal+=maxD;
        maxCnt--;
      }
      for (int i=0; i<num; i++) {
        if (v[i]>maxinternal) {
          maxinternal=v[i];

          maxD= (v[i]-maxinternal)/4f;
          maxCnt=4;

          maxinternal+=maxD;
          maxCnt--;

        }
        avg+=v[i];
        if(v[i]>currmax) currmax=v[i];
      }
      if(currmax>maxMaximum) currmax=maxMaximum;
      if(maxinternal>maxMinimum && maxCnt==0) maxinternal=currmax*0.005f+maxinternal*(1-0.005f);
      else if(maxinternal>maxMaximum) maxinternal=maxMaximum;

      avg/=(double)num;
//      System.out.println("max "+max+" currmax "+currmax+" avg "+avg);
      for (int i=0; i<num; i++) {
        spectrum[i]=(float)(v[i]/maxinternal);
        if(spectrum[i]>1) spectrum[i]=1;
      }
      max=(float)maxinternal;


    }
    else for(int i=0; i<num; i++) spectrum[i]=(float)v[i];

    int fftindex=0;
    double oldval;
    for(int i=0; i<numbands; i++) {
      oldval=bandv[i];
      bandv[i]=0;
      for(int j=0; j<bandsteps; j++) bandv[i]+=spectrum[fftindex++];
      bandv[i]/=(float)bandsteps;
      bandv[i]=dampenVal(oldval,bandv[i]);
      if(bandmax[i]<bandv[i]) bandmax[i]=(float)bandv[i];
      else bandmax[i]*=0.995f;

      band[i]=(float)bandv[i];///(float)bandsteps;
    }
  }

  public void update(float [] nv,int start,int length) {
    System.arraycopy(nv,start, ttv,0, length);
    update(ttv);
  }

  public void updateBands(float [] nv,int div) {
    int n2=nv.length/div;
    float val=0;
    for(int i=0; i<n2; i++) {
      val=0;
      for(int j=0; j<n2; j++) val+=nv[i*n2+j];
      ttv[i]=val/(float)n2;
    }
    update(ttv);
  }

  public double checkBandMax(int valid) {
    return bandmax[valid/bandsteps];
  }

}
////////////////////////////////////////////////
// Utility class to read AIFF file and perform
// FFT analysis for output.

class SoundData {
  public int bytesPerFrame, samplesPerFrame, readBufferSize, overlap=0;

  public boolean isMono=false, bigEndian;
  float left[][], right[][], spectrum[][];
  public int numFrames, spectrumLength;
  public int fps;

  public FFT fft;
  public FFTHelper ffthelper;
  public float damperval=0.1f;
  public char separator, COMMACHAR=',', TABCHAR='\t';

  // Constructor for SoundData
  // _spectrumLength is number of bands in FFT, must be a power of 2 (256,512,1024 etc...)
  // _damper is how much damping to apply to FFT. 0 = slow change, 1 = instant change

  public SoundData(int _spectrumLength, float _damper) {
    spectrumLength=_spectrumLength;
    damperval=_damper;
    separator=COMMACHAR;

    // Set up FFT for later

    fft=new FFT(spectrumLength*2);
    //    fft.useEqualizer(true);
    //    fft.useEnvelope(true, 1.2f);

    // FFTHelper(int _n, int _nbands,boolean doAvg)
    ffthelper=new FFTHelper(spectrumLength, 32, false);
    ffthelper.setMaxLimits(200, 2000);
    ffthelper.setDamper(damperval);
  }

  // Set equalizer flag for FFT
  public void setFFTEqualizer(boolean flag) {
    fft.useEqualizer(flag);
    println("FFT equalizer: "+flag);
  }

  // Set envelope for FFT
  public void setFFTEnvelope(boolean flag, float val) {
    fft.useEnvelope(flag, val);
    println("FFT envelope: "+flag+", "+val);
  }

  // Read sound data from file
  public void readSoundFile(String filename) {
    AudioInputStream audioIn=null;
    byte readBuffer[];
    int nBytesRead=0, frame;

    try {
      File fileIn=new File(filename);
      if (!fileIn.exists()) fileIn=new File(dataPath(filename));
      audioIn=AudioSystem.getAudioInputStream(fileIn);
      AudioFormat format=audioIn.getFormat();

      if (format.getChannels()==1) isMono=true;
      bigEndian=format.isBigEndian();

      bytesPerFrame=format.getFrameSize();

      println("\nReading sound file: '"+filename+"'");
      println("Audio format: "+format.toString());
      println("BytesPerFrame "+bytesPerFrame+" Soundwave frames:  "
        +audioIn.getFrameLength());
      
      float time=audioIn.getFrameLength();
      time/=44100f;
      println("Sound duration: "+nf(time,1,2)+" seconds");
    } 
    catch (Exception e) {
      println("Exception: "+e);
    }

    fps=1; // NOT USED
    samplesPerFrame=(44100/fps);
    readBufferSize=(44100/fps)*bytesPerFrame;
    readBuffer=new byte[readBufferSize];
    numFrames=(int)(((double)audioIn.getFrameLength()/44100.0f)*fps);

    numFrames+=fps*2;
    
    readBufferSize=spectrumLength*2;
    int readBufferSizeReal=readBufferSize*bytesPerFrame;
    numFrames=(int)(audioIn.getFrameLength()/readBufferSize);
    readBuffer=new byte[readBufferSizeReal];
    println("spectrumLength "+spectrumLength);
    println("readBufferSize: "+readBufferSize+"  frames: "+numFrames);
    


    spectrumLength=spectrumLength;
    left=new float[numFrames][readBufferSize];
    right=new float[numFrames][readBufferSize];

    // Read samples

    println("\nReading samples... ");
    
    frame=0;
    while (nBytesRead!=-1&&frame<numFrames) {
      try {
        nBytesRead=audioIn.read(readBuffer, 0, readBufferSize);
      } 
      catch (IOException e) {
        e.printStackTrace();
      }
      if (nBytesRead==readBufferSize) {
        int index=0;
        if (bigEndian) {
          if (isMono) { // MONO FORMAT
            for (int i=0; i<readBufferSize; i++) {
              left[frame][i]=((readBuffer[index++]<<8)|(readBuffer[index++]&0xFF));
              right[frame][i]=left[frame][i];
            }
          }
          else { // STEREO FORMAT          
            for (int i=0; i<readBufferSize; i++) {
              left[frame][i]=((readBuffer[index++]<<8)|(readBuffer[index++]&0xFF));
              right[frame][i]=((readBuffer[index++]<<8)|(readBuffer[index++]&0xFF));
            }
          }
        }
        else {
          byte b1, b2;
          if (isMono) { // MONO FORMAT
            for (int i=0; i<readBufferSize; i++) {
              b1=readBuffer[index++];
              b2=readBuffer[index++];
              left[frame][i]=((b2<<8)|(b1&0xFF));
              right[frame][i]=left[frame][i];
            }
          }
          else { // STEREO FORMAT          
            for (int i=0; i<readBufferSize; i++) {
              b1=readBuffer[index++];
              b2=readBuffer[index++];
              left[frame][i]=((b2<<8)|(b1&0xFF));
              b1=readBuffer[index++];
              b2=readBuffer[index++];
              right[frame][i]=((b2<<8)|(b1&0xFF));
            }
          }
        }
      }

      frame++;
      String tmp=""+numFrames;
      int cnt=tmp.length();
      if (frame%250==0||frame==numFrames-1)
        println("Frame "+nf(frame, cnt)+"/"+nf(numFrames, cnt));
    }
    
    try {
      audioIn.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    audioIn=null;
  }

  // Calculates FFT from sound data
  public void calcFFT() {
    float[] signal, fftResult=null;

    println("\nPerforming FFT... "+numFrames);
    // calculate FFT
    signal=new float[spectrumLength*2];
    spectrum=new float[numFrames][spectrumLength];

    int frame=0;
    for (int j=0; j<numFrames; j++) {
      for (int i=0; i<spectrumLength*2; i++) signal[i]=left[j][i];

      // compute FFT and apply damper and normalizer
      fftResult=fft.computeFFT(signal);
      if (fftResult==null) println("NULL");
      ffthelper.update(fftResult);

      // save data
      for (int i=0; i<spectrumLength; i++) {
        spectrum[frame][i]=ffthelper.spectrum[i];
      }
      frame++;
    }
    println("Done.\n");
  }

  // Calculates FFT from sound data
  public void calcFFT(int buf[]) {
    float[] signal, fftResult=null;

    println("\nPerforming FFT... "+numFrames);
    // calculate FFT
    signal=new float[spectrumLength*2];
    spectrum=new float[numFrames][spectrumLength];

    int frame=0;
    for (int j=0; j<numFrames; j++) {
      for (int i=0; i<spectrumLength*2; i++) signal[i]=left[j][i];

      // compute FFT and apply damper and normalizer
      fftResult=fft.computeFFT(signal);
      if (fftResult==null) println("NULL");
      ffthelper.update(fftResult);

      // save data
      for (int i=0; i<spectrumLength; i++) {
        spectrum[frame][i]=ffthelper.spectrum[i];
      }
      frame++;
    }
    println("Done.\n");
  }


  // read FFT data from a file output by a previous analysis
  public void readFFT(String filename) {
    String str[], tokens[];
    float[] data;

    try {

      StringBuffer contents = new StringBuffer();    
      BufferedReader input =  
        new BufferedReader(new FileReader(savePath(filename)));
      String line = null; //not declared within while loop
      String EOF=System.getProperty("line.separator");
      while ( ( line = input.readLine ()) != null) {
        contents.append(line);
        contents.append(EOF);
      }
      input.close();


      str=PApplet.split(contents.toString(), EOF);  
      numFrames=str.length;

      println("Reading FFT data from '"+shortFilename(filename));

      for (int i=0; i<numFrames; i++) if (str[i].length()>0) {
        data=parseLine(str[i]);

        if (data!=null) {
          if (i==0) {
            spectrumLength=data.length;
            spectrum=new float[numFrames][spectrumLength];
          }
          for (int j=0; j<spectrumLength; j++) spectrum[i][j]=data[j];
          if (i%250==0||i==numFrames-1)
            println("Frame "+nf(i, 5)+"/"+nf(numFrames, 5));
        }
      }
    } 
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    println(numFrames+" frames read, "+spectrumLength+" FFT bands.");
  }

  public float[] parseLine(String data) {
    float val[]=null;

    try {

      String tokens[]=PApplet.split(data, separator);
      val=new float[tokens.length];

      for (int i=0; i<tokens.length; i++) {
        val[i]=Float.parseFloat(tokens[i]);
      }
    } 
    catch(Exception e) {
      println(data);
      e.printStackTrace();
      val=null;
    }

    return val;
  }

  public String shortFilename(String name) {
    int pos=name.lastIndexOf(java.io.File.separatorChar);
    if (pos!=-1) return name.substring(pos+1);
    else return name;
  }

  // write FFT spectrogram to image file.
  // w = width of spectrogram image. If w is greater than numFrames
  // then a width of numFrames will be used.  
  public void writeFFTSpectrogram(int w, String filename) {
    float step;
    int id;

    if (w>numFrames) w=numFrames;    
    PGraphics pg=createGraphics(w, spectrumLength);

    step=(float)numFrames/(float)w;
    pg.beginDraw();

    for (int i=0; i<w; i++) {
      id=(int)((float)i*step);
      id=i;
      for (int j=0; j<spectrumLength; j++) {
//        pg.set(i, j, color(sin(HALF_PI*spectrum[id][j])*255));
        float v=spectrum[id][j];
        pg.set(i, j, getColor(v));
      }
    }

    pg.fill(255);
    pg.textFont(createFont("Arial",10));
    pg.text(filename,5,pg.height-22);
    pg.text(filename,5,pg.height-22);
    
    pg.endDraw();
    println("Writing FFT spectrogram to '"+shortFilename(filename)+"'");
    pg.save(filename);
  }  

  // write FFT data to CSV file
  public void writeFFT(String filename) {
    println("\nPreparing FFT data for output.");


    String str[]=new String[numFrames];
    StringBuffer buf=new StringBuffer(500);

    String tmp=""+numFrames;
    int cnt=tmp.length();

    for (int i=0; i<numFrames; i++) {
      buf.setLength(0);
      for (int j=0; j<spectrumLength; j++) {
        buf.append(nf(spectrum[i][j], 0, 5).replace(',', '.'));
        if (j<spectrumLength-1) buf.append(separator);
      }
      if (i%250==0||i==numFrames-1)
        println("Data frame "+nf(i, cnt)+"/"+nf(numFrames, cnt));
      str[i]=buf.toString();
    }

    try {
      filename=savePath(filename);
      if (!filename.endsWith(".csv")) filename+=".csv";
      PrintWriter writer=new PrintWriter(new FileOutputStream(filename));
      for (int i=0; i<str.length; i++) writer.println(str[i]);
      writer.flush();
      writer.close();
    } 
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    println("\nSaved FFT data to '"+shortFilename(filename)+"'");
  }
}


ArrayList<Integer> colors;

public void initColors() {
  colorMode(HSB, 100);
  colors=new ArrayList<Integer>();
  
  int n=600;
  for(int i=0; i<n; i++) {
    float t=map(i, 0,n,0,25);
    int c=color(t,100,t*4);
    colors.add(c);
  }
  
  colorMode(RGB);
}
  
public int getColor(float t) {
  return color(t*255f,t*255f,0);
  
// int val=(int)map(t,0,1, 0,colors.size()-1); 
// return colors.get(val);
}

public void stop() {
  
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "unlekker.snd.FFTDataToCSV" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
