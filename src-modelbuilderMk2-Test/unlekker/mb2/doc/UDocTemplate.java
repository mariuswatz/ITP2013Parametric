package unlekker.mb2.doc;


import java.lang.reflect.Method;
import java.util.*;

import processing.core.*;
import unlekker.mb2.util.*;
import unlekker.mb2.externals.*;
import unlekker.mb2.geo.*;

public class UDocTemplate extends UDocUtil {
  ArrayList<String> pre,post,body;
  
  public UDocTemplate(String filename) {
    pre=new ArrayList<String>();
    post=new ArrayList<String>();
    body=new ArrayList<String>();
    
    filename=path+"/templ/"+filename;
    
    String str[]=papplet.loadStrings(filename);
    int breakCnt=0;
    
    for(String s:str) {
      if(s.startsWith("--")) breakCnt++;
      else {
        if(breakCnt==0) pre.add(s);
        if(breakCnt==1) body.add(s);
        if(breakCnt==2) post.add(s);
      }
    }
    
//    logDivider("pre "+str(pre));
//    logDivider("body "+str(body));
//    logDivider("post "+str(post));
  }

  public void add(ArrayList<String> str,ArrayList<String> out) {
    out.addAll(str);
  }
  
  public void output(UMethod m,ArrayList<String> out) {
    add(pre,out);
    add(body,out);
    add(post,out);
  }
  
}