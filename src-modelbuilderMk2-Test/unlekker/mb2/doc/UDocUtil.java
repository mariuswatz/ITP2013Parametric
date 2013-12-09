package unlekker.mb2.doc;


import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import processing.core.*;
import unlekker.mb2.util.*;
import unlekker.mb2.externals.*;
import unlekker.mb2.geo.*;

public class UDocUtil extends UMB {
  static public String path,dataPath;
  static public UDocTemplate html;

  
  
  static public ArrayList<String> listPackage(String packageName) {
    ArrayList<String> str=new ArrayList<String>();
    
    List<Class<Object>> commands = new ArrayList<Class<Object>>();
    URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));

    UMB.log(root.toString());
    
    // Filter .class files.
    File[] files=null;
    try {
      files=new File(
          URLDecoder.decode(root.getFile(), "UTF-8")).listFiles(new FilenameFilter() {
          public boolean accept(File dir, String name) {
              return name.endsWith(".class");
          }
      });
    } catch (UnsupportedEncodingException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    // Find classes implementing UMB.
    
    for (File file : files) {
        String className = file.getName().replaceAll(".class", "");
        Class<?> cls= null;
        try {
          cls=Class.forName(packageName + "." + className);
          if(cls!=null) str.add(packageName+"."+className);
        } catch (ClassNotFoundException e) {
          // TODO Auto-generated catch block
          System.err.println(packageName + "." + className);
        }
        if (cls!=null && UMB.class.isAssignableFrom(cls)) {
            commands.add((Class<Object>) cls);
        }
    }

    return str;
  }
  
  static public String chop(String s) {
    int pos=s.lastIndexOf('.');
    if(pos>-1) s=s.substring(pos+1);
    
    return s;
  }

  static public ArrayList<UMethod> listMethods(String className) {
    ArrayList<UMethod> meth=new ArrayList<UMethod>();
    
    Method[] methods = null;
    try {
      Class cls=Class.forName(className);
      methods = cls.getMethods();
      for (int i = 0; i < methods.length; i++) {
        String s=methods[i].toGenericString();
        if(s.indexOf("native")<0) {
          meth.add(new UMethod(methods[i]));
        }
      }
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return meth;
  }
  
  static public String fMethod="<div class='method'><a href='%s'>%s</a> %s</div>";
  
  static public String strMethod(UMethod m) {
    String name=m.nameShort;
    
    return strf(fMethod,name,name,m.method.getReturnType().toString());
  }
  
  
  public void output() {
    
  }
  
}