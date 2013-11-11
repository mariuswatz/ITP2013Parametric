ArrayList<String> musicList;
String musicFolder;
String musicExt[]=new String[]{"mp3","aif","wav"};

void findMusic() {
  String dir=sketchPath;
  dir=dir.replace('\\','/');
//  println(dir);
  musicFolder=dir.substring(0,dir.lastIndexOf('/')+1)+"Music";
  
  File ff=new File(musicFolder);
  String l[]=ff.list();
  
  musicList=new ArrayList<String>();
  for(String name:l) {
    String ext=name.substring(name.indexOf('.')+1).toLowerCase();
    
    boolean ok=false;    
    for(String checkExt:musicExt) {
      if(ext.indexOf(checkExt)!=-1) ok=true;
    }
    
    println(ext+" ok? "+ok);
    if(ok) musicList.add(name);
  }
  
  println("------- Music folder: ");
  println(musicFolder);
  println(musicList);
}
