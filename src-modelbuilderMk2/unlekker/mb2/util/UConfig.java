package unlekker.mb2.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class UConfig extends Properties {
  public String path;

  public UConfig() {

  }

  public UConfig(String filename) {
    init(filename);
  }

  public void init(String filename) {
    FileInputStream in;

    try {
      File f=new File(filename);
      if(!f.exists()) {
        filename="conf/"+filename;
        f=new File(filename);
      }
      f=f.getCanonicalFile();
      path=f.getParent();
//      System.out.println("path "+path+" filename "+filename);
      in=new FileInputStream(f);
      load(in);
//      listProperties();
    } catch (Exception ex) {
      System.err.println("Error reading properties: "+filename);
    }
  }

  public UConfig put(String key,int val) {
    setProperty(key, ""+val);
    return this;
  }

  public UConfig put(String key,float val) {
    setProperty(key, ""+UMB.nf(val));
    return this;
  }

  public UConfig put(String key,boolean bool) {
    setProperty(key, ""+bool);
    return this;
  }

  public UConfig put(String key,String val) {
    setProperty(key, val);
    return this;
  }

  public String get(String id) {
    return getProperty(id);
  }

  public boolean getBool(String id) {
    return getBool(id,false);
  }

  public int getInt(String id) {
    return getInt(id,-1);
  }

  public float getFloat(String id) {
    return getFloat(id,-1);
  }

  public boolean getBool(String id, boolean defState) {
    String tmp=getProperty(id,""+defState).toLowerCase().trim();
    if(tmp.compareTo("true")==0) return true;
    return false;
  }

  public int getInt(String id, int defVal) {
    int tmp;
    try {
      tmp=Integer.parseInt(getProperty(id,""+defVal));
    } catch (NumberFormatException e) {
      System.out.println(id+": "+e.toString());
      tmp=defVal;
    }
    return tmp;
  }

  public float getFloat(String id, float defVal) {
    return Float.parseFloat(getProperty(id,""+defVal));
  }

  public void list() {
    for (java.util.Enumeration e=propertyNames(); e.hasMoreElements(); ) {
      String id=""+e.nextElement();
      System.out.println(id+" = "+getProperty(id));
    }
  }

}
