////////////////////////////////////////////////
// Utility class to read AIFF file and perform
// FFT analysis for output.

class SoundData {
  public int bytesPerFrame, samplesPerFrame, readBufferSize, overlap=0;
  
  public boolean isMono=false;
  float left[][], right[][], spectrum[][];
  public int numFrames, spectrumLength;
  public int fps;

  public FFT fft;
  public FFTHelper ffthelper;
  public float damperval=0.1f;
  public char separator,COMMACHAR=',',TABCHAR='\t';

  // Constructor for SoundData
  // _spectrumLength is number of bands in FFT, must be a power of 2 (256,512,1024 etc...)
  // _damper is how much damping to apply to FFT. 0 = slow change, 1 = instant change
    
  public SoundData(int framesPerSecond,int _spectrumLength,float _damper) {
    fps=framesPerSecond;
    spectrumLength=_spectrumLength;
    damperval=_damper;
    separator=COMMACHAR;
    

    // Set up FFT for later
    
    fps=framesPerSecond;
    fft=new FFT(spectrumLength*2);
    fft.useEqualizer(true);
    fft.useEnvelope(true, 1f);

    // FFTHelper(int _n, int _nbands,boolean doAvg)
    ffthelper=new FFTHelper(spectrumLength,32,false);
    ffthelper.setMaxLimits(100,2000);
    ffthelper.setDamper(damperval);  
  }

  public void readSoundFile(String filename) {
    AudioInputStream audioIn=null;
    byte readBuffer[];
    int nBytesRead=0, frame;

    try {
      File fileIn=new File(filename);
      if(!fileIn.exists()) fileIn=new File(dataPath(filename));
      audioIn=AudioSystem.getAudioInputStream(fileIn);
      AudioFormat format=audioIn.getFormat();

      if(format.getChannels()==1) isMono=true;
      
      bytesPerFrame=format.getFrameSize();

      println("\nReading sound file: '"+filename+"'");
      println("Audio format: "+format.toString());
      println("BytesPerFrame "+bytesPerFrame+" FrameLength "
          +audioIn.getFrameLength());
    } catch (Exception e) {
      println("Exception: "+e);
    }

    samplesPerFrame=(44100/fps);
    readBufferSize=(44100/fps)*bytesPerFrame;
    readBuffer=new byte[readBufferSize];
    numFrames=(int)(((double)audioIn.getFrameLength()/44100.0)*fps);

    numFrames+=fps*2;

    println("spectrumLength "+spectrumLength+" samplesPerFrame "+
      samplesPerFrame+" numFrames "+numFrames);
    println("readBufferSize "+readBufferSize+" overlap "+overlap);

    spectrumLength=spectrumLength;
    left=new float[numFrames][samplesPerFrame];
    right=new float[numFrames][samplesPerFrame];

    // Read samples

    println("\nReading samples...");
    frame=0;
    while (nBytesRead!=-1&&frame<numFrames) {
      try {
        nBytesRead=audioIn.read(readBuffer, 0, readBufferSize);
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (nBytesRead==readBufferSize) {
        int index=0;
        if(isMono) { // MONO FORMAT
          for (int i=0; i<samplesPerFrame; i++) {
            left[frame][i]=((readBuffer[index++]<<8)|(readBuffer[index++]&0xFF));
            right[frame][i]=left[frame][i];
          }
        }
        else { // STEREO FORMAT          
          for (int i=0; i<samplesPerFrame; i++) {
            left[frame][i]=((readBuffer[index++]<<8)|(readBuffer[index++]&0xFF));
            right[frame][i]=((readBuffer[index++]<<8)|(readBuffer[index++]&0xFF));
          }
        }
      }

      frame++;
      if (frame%250==0||frame==numFrames-1)
        println("Frame "+nf(frame, 5)+"/"+nf(numFrames, 5));
    }
  }
  
  public void calcFFT() {
    float[] signal, fftResult=null;

    println("\nPerforming FFT..."+fft);
    // calculate FFT
    signal=new float[spectrumLength*2];
    spectrum=new float[numFrames][spectrumLength];

    int frame=0;
    for (int j=0; j<numFrames; j++) {
      for (int i=0; i<spectrumLength*2; i++) signal[i]=left[j][i];
      
      // compute FFT and apply damper and normalizer
      fftResult=fft.computeFFT(signal);
      if(fftResult==null) println("NULL");
      ffthelper.update(fftResult);
      
      // save data
      for (int i=0; i<spectrumLength; i++) {
        spectrum[frame][i]=ffthelper.spectrum[i];
      }
      frame++;
    }
  }

    

  public void readFFT(String filename) {
    String str[], tokens[];
    float[] data;
    
    try {
      
      StringBuffer contents = new StringBuffer();    
      BufferedReader input =  
        new BufferedReader(new FileReader(savePath(filename)));
      String line = null; //not declared within while loop
      String EOF=System.getProperty("line.separator");
      while (( line = input.readLine()) != null){
        contents.append(line);
        contents.append(EOF);
      }
      input.close();
        
        
      str=PApplet.split(contents.toString(), EOF);  
      numFrames=str.length;
      
      println("Reading FFT data from '"+shortFilename(filename));

      for (int i=0; i<numFrames; i++) if(str[i].length()>0) {
        data=parseLine(str[i]);
        
        if(data!=null) {
        if (i==0) {
          spectrumLength=data.length;
          spectrum=new float[numFrames][spectrumLength];
        }
        for (int j=0; j<spectrumLength; j++) spectrum[i][j]=data[j];
      if (i%250==0||i==numFrames-1)
        println("Frame "+nf(i, 5)+"/"+nf(numFrames, 5));
      }
        }
    } catch (Exception e) {
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
    
    } catch(Exception e) {
      println(data);
      e.printStackTrace();
      val=null;
    }
    
    return val;
  }

  String shortFilename(String name) {
    int pos=name.lastIndexOf(java.io.File.separatorChar);
    if(pos!=-1) return name.substring(pos+1);
    else return name;
  }

  public void writeFFT(String filename) {
    println("\nPreparing text data for output.");


    String str[]=new String[numFrames];
    StringBuffer buf=new StringBuffer(500);

    for (int i=0; i<numFrames; i++) {
      buf.setLength(0);
      for (int j=0; j<spectrumLength; j++) {
        buf.append(nf(spectrum[i][j], 0, 5).replace(',', '.'));
        if (j<511) buf.append(separator);
      }
      if (i%250==0||i==numFrames-1)
        println("Data frame "+nf(i, 5)+"/"+nf(numFrames, 5));
      str[i]=buf.toString();
    }

    try {
      filename=savePath(filename);
      if(!filename.endsWith(".csv")) filename+=".csv";
      PrintWriter writer=new PrintWriter(new FileOutputStream(filename));
      for (int i=0; i<str.length; i++) writer.println(str[i]);
      writer.flush();
      writer.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    println("\nSaved text data to '"+filename);
  }
}
