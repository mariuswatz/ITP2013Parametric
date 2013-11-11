int source=-1;

public void switchSource() {
  if (bufL==null) {
    bufL=new float[bufSize];
    bufR=new float[bufSize];
  }


  source=(source+1)%2;

  // reset volMax
  volMax=0;

  // select correct sound input  
  if (source==0) { // LINE IN    
    // close AudioPlayer if it exists
    if (player!=null) {
      player.pause();
      player.close();
      player=null;
    }

    musicfile="LINE IN";
    if (lineIn==null) lineIn= minim.getLineIn(Minim.STEREO, bufSize);

    // enable line in monitoring so we can hear the input
    lineIn.enableMonitoring();
    in=(AudioSource)lineIn;
  }
  else { // SOUND FILE
    if (lineIn!=null) {
      lineIn.disableMonitoring();
    }

    musicfile=rndSoundFile();

    player = minim.loadFile(musicFolder+"/"+musicfile, bufSize);
    player.play();

    in=(AudioSource)player;
  }
}
