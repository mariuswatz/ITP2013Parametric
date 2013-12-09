package unlekker.mb2.doc;


import java.lang.reflect.Method;
import java.util.*;

import processing.core.*;
import unlekker.mb2.util.*;
import unlekker.mb2.externals.*;
import unlekker.mb2.geo.*;

public class UMethod extends UDocUtil {
  String name,nameShort;
  String param;
  Method method;
  
  public UMethod(Method method) {
    this.method=method;
    String name=method.getName();
    nameShort=chop(name);
    
    String s=method.toGenericString();
    String tok[]=s.split(" ");
    int id=-1,cnt=0; 
    while(id<0) {
      if(tok[cnt].indexOf(name)>-1) {
        id=cnt;
      }
      cnt++;
    }
    
    s="";
    id--;
    tok[id]=chop(tok[id]);
//    tok[id+1]=chop(tok[id+1]);
    
    for(int j=id; j<tok.length; j++) s+=tok[j]+"\t";
    param=s;

    
    System.out.println((tok.length)+" "+(tok.length-id)+"\t" +s);
  }
  
}