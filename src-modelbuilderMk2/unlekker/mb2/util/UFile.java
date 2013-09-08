package unlekker.mb2.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

import processing.core.PApplet;

public class UFile implements UConst {
  protected static PApplet papplet;
  public static String currDir;
  
  /**
   * Finds the canonical path of the current directory.
   * @return Name of current directoy
   */
  public static String getCurrentDir() {
    try {
      currDir=new File (".").getCanonicalPath();
      currDir=currDir.replace('\\',DIRCHAR);
    }
    catch(Exception e) {
      e.printStackTrace();
      currDir=null;
    }
    return currDir;
  }

  
  
  ///////////////////////////////
  // PApplet methods
  
  /**
   * Set PApplet reference to get access to dataPath and sketchPath
   * @param papplet
   */
  public static void setPApplet(PApplet papplet) {
    UFile.papplet=papplet;
  }

  /**
   * Returns path prefixed with dataPath from PApplet reference if set,
   * if not returns path unmodified.  
   * @param path
   * @return
   */
  public static String dataPath(String path) {
    if(papplet!=null) return papplet.dataPath(path);
    return path;
  }

  /**
   * Returns path prefixed with sketchPath from PApplet reference if set,
   * if not returns path unmodified.
   * @param path
   * @return
   */
  public static String sketchPath(String path) {
    if(papplet!=null) return papplet.sketchPath(path);
    return path;
  }
  
  ///////////////////////////////
  // Directory tools
  
  
  public static ArrayList<String> list(String path) {
    return list(path,null,null);
  }

  public static ArrayList<String> list(String path,String pre) {
    return list(path,pre,null);
  }

  public static ArrayList<String> listByExtension(String path,String ext) {
    return list(path,null,ext);
  }

  public static ArrayList<String> list(String path,String pre,String ext) {
    ArrayList<String> l=null;
    String [] ls=null;
    
    try {
      File f=new File(path);
      l=new ArrayList<String>();

      if(f.exists() && f.isDirectory()) {
        path=fixPath(path);
        
        if(pre!=null || ext!=null) {
          FilenameFilterPreExt filter=new FilenameFilterPreExt(pre, ext);
          
          ls=f.list(filter);
        }
        else ls=f.list();
        
        if(l!=null && ls!=null) for(String fs:ls) l.add(fs);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      UBasic.log(e.getMessage());
      e.printStackTrace();
    }
    
    return l;
  }

  public static String nextFilename(String path,String pre) {
    return nextFilename(path, pre,null);
  }

  public static String nextFilename(String path,String pre,String ext) {
    int last=lastIndex(path, pre, ext)+1;
    
    String s=fixPath(path)+pre.trim()+" "+UBasic.nf(last,4);
    
    if(ext!=null) {
      if(!ext.startsWith(".")) ext="."+ext;
      s+=ext;
    }
    
    return s;
  }

  
  public static String last(String path,String pre,String ext) {
    String s=null;

    ArrayList<String> l=list(path,pre,ext);
    s=l.get(l.size()-1);
    return s;
  }

  public static int lastIndex(String path,String pre) {
    return lastIndex(path, pre, null);
  }

  public static int lastIndex(String path,String pre,String ext) {
    String s=null;
    int i=-1;
    
    try {
      ArrayList<String> l=list(path,pre,ext);
      if(l.size()<1) return -1;
        
      s=noExt(l.get(l.size()-1));
      s=s.substring(s.indexOf(' '));
      i=UBasic.parseInt(s);
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      i=-1;
    }
    
    return i;
  }
  
  
  
  ///////////////////////////////
  // File streams
  
  public static OutputStream getOutputStream(String filename) {
    return getOutputStream(filename,false);
  }

  public static OutputStream getOutputStream(String filename,boolean append) {
    OutputStream out=null;

    try {
      filename=getAbsolutePath(filename);   
      
      
      UBasic.logDivider(filename);
      new File(getPath(filename)).mkdirs();
      
      if(filename.endsWith("gz")) {
        out=new GZIPOutputStream(new FileOutputStream(filename));
      }
      else out=new FileOutputStream(filename,append);
    } catch (Exception e) {   
      e.printStackTrace();
      UBasic.logErr("getOutputStream - "+e.toString());
    }
    return out;
  }


  ///////////////////////////////
  // File name manipulation

  /**
   * Returns extension of file name
   * @param name
   * @return Extension
   */
  public static String getExt(String name) {
    String ext=null;
    
    int pos=name.lastIndexOf('.');
    if(pos>0) ext=name.substring(pos+1).toLowerCase();
    
    return ext;
  }

  /**
   * Checks if file has given extension ("jpg","png" etc.)
   * @param name
   * @return
   */
  public static boolean hasExt(String name,String ext) {
    String ext2=getExt(name);
    return ext.toLowerCase().equals(ext2);
  }

  /**
   * Fixes path String: DOS-style '\' becomes '/', also appends '/'
   * if string does not aready have one.
   * @param name
   * @return
   */
  public static String fixPath(String path) {
    if(path==null) return null;
        
    path=path.replace(DIRCHARDOS,DIRCHAR);
    if(!path.endsWith(DIRSTR)) path+=DIRSTR;
    
    return path;
  }
  
  public static String [] getPathElements(String filename) {
    String res[]=new String[3];
    res[0]=getPath(filename);
    
    filename=noPath(filename);
    res[1]=noExt(filename);
    res[2]=getExt(filename);

    return res;
  }

  public static String getPath(String path) {
    if(path==null) return null;

    if(path.indexOf(DIRCHAR)==-1 && path.indexOf(DIRCHARDOS)==-1) return ""; 

    path=path.replace(DIRCHARDOS, DIRCHAR);
    path=path.substring(0,path.indexOf(DIRCHAR)-1);
    
    return path;
  }
  
  
  /**
   * Returns the file name sans extension
   * @param name
   * @return File name sans extension
   */
  public static String noExt(String name) {
    int pos;
    
    pos=name.lastIndexOf(DIRCHAR);
    if(pos==-1) pos=name.lastIndexOf(DIRCHARDOS);

    if(pos!=-1) name=name.substring(pos+1);
    
    pos=name.indexOf(".");
    if(pos==-1) return name;
    return name.substring(0,pos);
  }

  /**
   * Returns filename sans path.
   * @param name
   * @return Filename sans  path
   */
  public static String noPath(String name) {
    int pos=name.lastIndexOf('\\');
    int pos2=name.lastIndexOf('/');
    if(pos<0 || (pos2>-1 && pos2<pos)) pos=pos2;

    return name.substring(pos+1);
  }
  
  /**
   * Finds the canonical path of the current directory.
   * @return Name of current directoy
   */
  public static String getAbsolutePath(String name) {
    File f=new File(name);    
    if(f.isAbsolute()) return name;

    if(currDir==null) currDir=getCurrentDir();
    name=currDir+DIRCHAR+name;

//    if(debugLevel>2) Util.log("IO.getAbsolutePath "+s);
    return name;
  }


  ///////////////////////////////
  // File name filter
  
  static class FilenameFilterPreExt implements FilenameFilter {
    String pre,ext;
    
    public FilenameFilterPreExt(String pre,String ext) {
      this.pre=pre;
      this.ext=(ext==null ? null : ext.toLowerCase());
    }
        
    
    public boolean accept(File dir, String name) {
      boolean ok=true;
//      UBasic.log(">> "+dir.toString()+"|"+name);
      
      if(pre!=null) {
        if(!name.startsWith(pre)) ok=false;
      }
      
      if(ok && ext!=null) {
         if(!name.toLowerCase().endsWith(ext)) ok=false;
      }
      
      return ok;
    }
    
  }

}
