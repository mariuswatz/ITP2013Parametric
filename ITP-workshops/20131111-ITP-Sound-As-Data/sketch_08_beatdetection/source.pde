
int source=0;

void switchSource() {
  source=(source+1)%2;
  volMax=0;
  
  
  if(source==0) { // LINE IN
    if(player!=null) {
      player.pause();
      player.close();
      player=null;
    }
  
    if(lineIn==null) lineIn=minim.getLineIn(Minim.STEREO,bufSize);
    
    // enable monitoring
    lineIn.enableMonitoring();
    
    // assign input to lineIn by casting lineIn to AudioSource
    input=(AudioSource)lineIn;
    musicfile="LINE IN";
  }
  else { // LOAD SOUND FILE
    musicfile=rndSoundFile();
  
    if(lineIn!=null) lineIn.disableMonitoring();
    
    player=minim.loadFile(musicFolder+"/"+musicfile, bufSize);
    player.play();

    input=(AudioSource)player; 
  }
}
