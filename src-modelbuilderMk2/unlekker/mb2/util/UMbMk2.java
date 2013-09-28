package unlekker.mb2.util;

import java.lang.Character.Subset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PGraphics3D;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;

/**
 * 
 * UMbMk2 is the base class for most of the classes in ModelbuilderMk2, meaning that
 * they all extend this class and thus inherit the capabilities it provides. This
 * includes convenient tools for common computational tasks (math, string formatting etc), 
 * as well as a mechanism to set and share a PApplet instance provided by the user.
 * 
 * Call {@see UMbMk2#setPApplet(PApplet)} or {@see UMbMk2#setGraphics(PGraphics)} to provide a PApplet 
 * or PGraphics instance,which can then be used by geometry classes like {@see UGeo} and {@see UVertexList}
 * for rendering etc.
 * 
 * 
 * 
 * @author <a href="https://github.com/mariuswatz">Marius Watz</a>
 *
 */
public class UMbMk2 implements UConst {

  /**
   * Options like <code>NODUPL</code>, <code>NOCOPY</code> etc. encoded as bit flags stored as an int. Options can be
   * toggled and checked with <code>enable()</code> and <code>isEnabled()</code>
   */
  public int options;
  
  private static String[] optionNames;
  protected static UMbMk2 UMbMk2;
  protected static PApplet papplet=null;
  protected static PGraphics g;
  protected static PGraphics g3d;
  protected static boolean isGraphics3D;
  
  protected static int gErrorCnt=0;

  protected static boolean libraryPrinted=false;
  static {
    if(!libraryPrinted) {
      UMbMk2.logDivider(VERSION);
      UMbMk2.log(CREDIT);
      UMbMk2.logDivider();
      
      libraryPrinted=true;
    }
  }
  

  public static String version() {
    return VERSION;
  }


  ///////////////////////////////////////////////
  // PGRAPHICS CONVENIENCE METHODS
  
  /**
   * Static method to call PGraphics.translate() with a UVertex instance as
   * input.
   * 
   * @param v
   * @return
   */
  public static UMbMk2 ptranslate(UVertex v) {
    return ptranslate(v.x,v.y,v.z);
  }

  /**
   * Static method to call PGraphics.translate() with a UVertex instance as
   * input.
   * 
   * @param v
   * @return
   */
  public static UMbMk2 ptranslate(float x,float y,float z) {
    if (checkGraphicsSet()) g.translate(x, y, z);
    else g.translate(x, y);
    return UMbMk2.UMbMk2;
  }

  /**
   * Static method to call PGraphics.translate() with a UVertex instance as
   * input.
   * 
   * @param v
   * @return
   */
  public static UMbMk2 ptranslate(float x,float y) {
    if (checkGraphicsSet()) g.translate(x, y);
    return UMbMk2.UMbMk2;
  }

  /**
   * Static conveniece method to call PGraphics.line() with two UVertex
   * instances as input.
   */
  public static UMbMk2 pline(UVertex v, UVertex v2) {
    if (checkGraphicsSet()) {
      if(isGraphics3D)
        g.line(v.x, v.y, v.z, v2.x, v2.y, v2.z);
      else g.line(v.x, v.y, v2.x, v2.y);
    }
    return UMbMk2.UMbMk2;
  }

  /**
   * Static conveniece method to call both <code>PGraphics.pushMatrix()</code>
   * <code>PGraphics.pushStyle()</code>
   */
  public static UMbMk2 ppush() {
    if (checkGraphicsSet()) {
      g.pushMatrix();
      g.pushStyle();
    }
    return UMbMk2.UMbMk2;
  }

  /**
   * Static conveniece method to call both <code>PGraphics.popMatrix()</code>
   * <code>PGraphics.popStyle()</code>
   */
  public static UMbMk2 ppop() {
    if (checkGraphicsSet()) {
      g.popStyle();
      g.popMatrix();
    }
    return UMbMk2.UMbMk2;
  }

  /**
   * Static conveniece method to call <code>PGraphics.vertex()</code> with a
   * UVertex instance as input
   */
  public static UMbMk2 pvertex(UVertex v) {
    if (checkGraphicsSet()) {
      if (isGraphics3D) g3d.vertex(v.x, v.y, v.z);
      else g.vertex(v.x, v.y);
    }
    return UMbMk2.UMbMk2;
  }

  /**
   * Static conveniece method to iterate through an array of
   * <code>UVertex</code> and call <code>PGraphics.vertex()</code> for each
   * instance.
   */
  public static UMbMk2 pvertex(UVertex v[]) {
    if (checkGraphicsSet()&&v!=null) {
      for (UVertex vv : v)
        if (vv!=null) {
          pvertex(vv);
        }
    }
    return UMbMk2.UMbMk2;
  }

  /**
   * Static conveniece method to call <code>PGraphics.fill()</code>
   */
  public static UMbMk2 pfill(int col) {
    if (checkGraphicsSet()) g.fill(col);
    return UMbMk2.UMbMk2;
  }

  /**
   * Static conveniece method to call <code>PGraphics.stroke()</code>
   */
  public static UMbMk2 pstroke(int col) {
    if (checkGraphicsSet()) g.stroke(col);
    return UMbMk2.UMbMk2;
  }

  /**
   * Static conveniece method to call <code>PGraphics.noFill()</code>
   */
  public static UMbMk2 pnoFill() {
    if (checkGraphicsSet()) g.noFill();
    return UMbMk2.UMbMk2;
  }

  /**
   * Static conveniece method to call <code>PGraphics.noStroke()</code>
   */
  public static UMbMk2 pnoStroke() {
    if (checkGraphicsSet()) g.noStroke();
    return UMbMk2.UMbMk2;
  }

  
  
  ///////////////////////////////////////////////
  // GEOMETRY OPTIONS 

  public UMbMk2 setOptions(int opt) {
    options=opt;
//    log(optionStr());
    return this;
  }

  public UMbMk2 enable(int opt) {
    options=options|opt;
//    log(optionStr());
    return this;
  }

  public boolean isEnabled(int opt) {
    return (options & opt)==opt;
  }

  public UMbMk2 disable(int opt) {
    options=options  & (~opt);
//    log(optionStr());
    return this;
  }
  
  public String optionStr() {
    if(optionNames==null) {
      optionNames=new String[1000];
      optionNames[COLORVERTEX]="COLORVERTEX";
      optionNames[COLORFACE]="COLORFACE";
      optionNames[NOCOPY]="NOCOPY";
      optionNames[NODUPL]="NODUPL";
    }
    
    StringBuffer buf=new StringBuffer();
    if(isEnabled(NODUPL)) buf.append(optionNames[NODUPL]).append(TAB);
    if(isEnabled(NOCOPY)) buf.append(optionNames[NOCOPY]).append(TAB);
    if(isEnabled(COLORFACE)) buf.append(optionNames[COLORFACE]).append(TAB);
    if(isEnabled(COLORVERTEX)) buf.append(optionNames[COLORVERTEX]).append(TAB);

    if(buf.length()>0) {
      buf.deleteCharAt(buf.length()-1);
      return "Options: "+buf.toString();
    }
    
    return "Options: None";
  }
  
  ///////////////////////////////////////////////
  // COLOR 
  
  public static final int color(int c,float a) {
    return ((int)a<< 24) & c;
  }

  public static final int color(float r, float g, float b) {
    int rr=(int)r,gg=(int)g,bb=(int)b;
    return (0xFF000000)|
        ((rr&0xff)<<16)|((gg&0xff)<<8)|(bb&0xff);
  }

  public static final int color(int r, int g, int b, int a) {
    return (0xFF00000)|((r&0xff)<<16)|((g&0xff)<<8)|(b&0xff);
  }

  public static String hex(int col) {
    String s="",tmp;
    
    int a=(col >> 24) & 0xff;
    if(a<255) s+=strPad(Integer.toHexString(a),2,ZERO);
    
    s+=strPad(Integer.toHexString((col>>16)&0xff),2,ZERO);
    s+=strPad(Integer.toHexString((col>>8)&0xff),2,ZERO);
    s+=strPad(Integer.toHexString((col)&0xff),2,ZERO);
//    s+=(tmp.length()<2 ? "0"+tmp : tmp);
//    tmp=Integer.toHexString((col>>8)&0xff);
//    s+=(tmp.length()<2 ? "0"+tmp : tmp);
//    tmp=Integer.toHexString((col)&0xff);
//    s+=(tmp.length()<2 ? "0"+tmp : tmp);
    
    s=s.toUpperCase();
    return s;
  }

  public static final int color(String hex) {
    int c=0xFFFF0000,alpha=255;
    
    boolean ok=true;
    
    if(hex==null) ok=false; 
    else for(int i=0; ok && i<hex.length(); i++) {
      char ch=hex.charAt(i);
      if(!(
          Character.isLetter(ch) ||
              Character.isDigit(ch)
              )) ok=false;
    }
    if(!ok) {
      log("toColor('"+hex+"') failed.");
      return c;
    }
    
    try {
      if(hex.length()==8) {
        alpha=Integer.parseInt(hex.substring(0,2),16);
//      UUtil.log("hex: "+hex+" alpha: "+alpha);
        hex=hex.substring(2);
      }
      c=(alpha<<24) | Integer.parseInt(hex, 16);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      c=color(255,0,0);
      e.printStackTrace();
    }
    
    return c;
  }

  //////////////////////////////////////////
  // MATH
  // map,lerp,max,constrain code taken from processing.core.PApplet
  
  
  static public final float abs(float n) {
    return (n < 0) ? -n : n;
  }

  static public final int abs(int n) {
    return (n < 0) ? -n : n;
  }

  static public final float sq(float a) {
    return a*a;
  }

  static public final float sqrt(float a) {
    return (float)Math.sqrt(a);
  }

  static public final int max(int a, int b) {
    return (a > b) ? a : b;
  }

  static public final float max(float a, float b) {
    return (a > b) ? a : b;
  }

  static public final int min(int a, int b) {
    return (a < b) ? a : b;
  }

  static public final float min(float a, float b) {
    return (a < b) ? a : b;
  }

  
  static public final float map(float value,
      float ostart, float ostop) {
        return ostart + (ostop - ostart) * (value);
  }
  
  static public final float map(float value,
      float istart, float istop,
      float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }

  static public final int constrain(int amt, int low, int high) {
    return (amt < low) ? low : ((amt > high) ? high : amt);
  }

  static public final float constrain(float amt, float low, float high) {
    return (amt < low) ? low : ((amt > high) ? high : amt);
  }

  static public final float lerp(float start, float stop, float amt) {
    return start + (stop-start) * amt;
  }

  
  // extended versions
  
  static public final float mod(float a, float b) { // code from David Bollinger
    return (a%b+b)%b; 
  }

  static public final float max(ArrayList<Float> val) {
    float theMax=Float.MIN_VALUE;
    
    for(float v:val) theMax=(v>theMax ? v : theMax);
    return theMax;
  }

  static public final float min(ArrayList<Float> val) {
    float theMin=Float.MAX_VALUE;
    
    for(float v:val) theMin=(v>theMin ? v : theMin);
    return theMin;
  }

  static public final float max(float val[]) {
    float theMax=val[0];
    for(float v:val) theMax=(v>theMax ? v : theMax);
    return theMax;
  }

  static public final int max(int val[]) {
    int theMax=val[0];
    for(int v:val) theMax=(v>theMax ? v : theMax);
    return theMax;
  }


  static public final double mapDbl(double value,
      double istart, double istop,
      double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }


  //////////////////////////////////////////
  // PARSING VALUES

  public static int parseInt(String s) {
    if(s==null) return Integer.MIN_VALUE;
    return Integer.parseInt(s.trim());
  }

  public static float parseFloat(String s) {
    if(s==null) return Float.NaN;
    return Float.parseFloat(s.trim());
  }

  public static float[] parseFloat(String s[]) {
    if(s==null) return null;
    
    float f[]=new float[s.length];
    int id=0;
    for(String ss:s) f[id++]=parseFloat(ss);
    
    return f;
  }

  
  //////////////////////////////////////////
  // RANDOM NUMBERS

  /**
   * Static copy of unlekker.util.Rnd for easy random number generation.
   */
  public static URnd rnd=new URnd(System.currentTimeMillis());
  
  public static void setRnd(URnd rnd) {
    UMbMk2.rnd=rnd;
    UMbMk2.UMbMk2=new UMbMk2();
  }
  
  /**
   * Returns <code>true</code> if <code>rnd(100) > prob</code>.
   * @param prob
   * @return
   */
  public boolean rndProb(float prob) {
    return rnd.prob(prob>100 ? 100 : prob);
  }

  public static boolean rndBool() {
    return rnd.bool();
  }

  public static float rnd() {
    return rnd.random(1);
  }

  public static float rnd(float max) {
    return rnd.random(max);
  }

  public static float rndSign() {
    return (rndBool() ? -1 : 1);
  }

  public static float rnd(float min, float max) {
    return rnd.random(min,max);
  }

  public static float rndSigned(float v) {
    return rnd(v)*rndSign();
  }
  
  /**
   * Generates randomly signed integer numbers in the ranges [min..max] and
   * [-max..-min], with equal chances of getting a negative or
   * positve outcome. Avoids the problem
   * of a call like <code>random(-1,1)</code> generating 
   * values close to zero.  
   * 
   * @param min Minimum absolute value
   * @param max Maximum absolute value
   * @return
   */
  public static float rndSigned(float min, float max) {
     float val=rnd.random(min,max);
      return rndBool() ? val : -val;
    }

  public static int rndInt(int max) {
    return rnd.integer(max);
  }

  public static int rndInt(int min, int max) {
    return rnd.integer(min,max);
  }

  /**
   * Generates randomly signed integer numbers in the ranges [min..max] and
   * [-max..-min], with equal chances of getting a negative or
   * positve outcome. Avoids the problem
   * of a call like <code>random(-1,1)</code> generating 
   * values close to zero.  
   * 
   * @param min Minimum absolute value
   * @param max Maximum absolute value
   * @return
   */
  public static int rndIntSigned(float min, float max) {
    int val=rnd.integer(min,max);
     return rndBool() ? val : -val;
   }
  
  
  //////////////////////////////////////////
  // SET + GET PAPPLET AND PGRAPHICS 

  public static void setPApplet(PApplet papplet) {
    setPApplet(papplet,true);
  }

  public static void setPApplet(PApplet papplet,boolean useGraphics) {
    UMbMk2.papplet=papplet;    
    if(useGraphics) setGraphics(papplet);
  }

  public static PApplet  getPApplet() {
    return UMbMk2.papplet;    
  }

  public static boolean checkGraphicsSet() {
    if(g==null) {
      if(gErrorCnt%100==0) logErr("ModelbuilderMk2: No PGraphics set. Use UGeo.setGraphics(PApplet).");
      gErrorCnt++;
      return false;
    }
    return true;
  }
  
  public static PGraphics getGraphics() {
    return g;
  }

  public static void setGraphics(PApplet papplet) {
    setGraphics(papplet.g);
  }

  public static void setGraphics(PGraphics gg) {
    UMbMk2.g=gg;
    if(gg.is3D()) {
      UMbMk2.g3d=(PGraphics3D)gg;
      isGraphics3D=true;
    }
    else isGraphics3D=false;
    
    log("UMbMk2.setGraphics: "+
        g.getClass().getSimpleName()+
        " (is3D="+isGraphics3D+")");
  }

  //////////////////////////////////////////
  // LOGGING
  
  public static void log(String s) {
    System.out.println(s);
  }

  public static void log(int i) {
    System.out.println(i);
  }

  public static void log(float f) {
    System.out.println(f);
  }

  public static void logErr(String s) {
    System.err.println(s);
  }

  public static void logDivider() {
    System.out.println(LOGDIVIDER);
  }

  public static void logDivider(String s) {
    System.out.println(LOGDIVIDER+' '+s);
  }

  
  //////////////////////////////////////////
  // FILE TOOLS
  
  public static String nextFile(String path,String pre) {
    return nextFilename(path, pre,null);
  }

  public static String nextFilename(String path,String pre,String ext) {
    return UFile.nextFile(path, pre, ext);
  }
  
  //////////////////////////////////////////
  // NUMBER FORMATTING
  
  private static NumberFormat formatFloat, formatInt;
  private static char numberChar[]=new char[] {'0', '1', '2', '3', '4', '5',
      '6', '7', '8', '9', '-', '.'};

  static public void nfInitFormats() {
    formatFloat=NumberFormat.getInstance();
    formatFloat.setGroupingUsed(false);

    formatInt=NumberFormat.getInstance();
    formatInt.setGroupingUsed(false);
  }

  /**
   * Format floating point number for printing
   * 
   * @param num
   *          Number to format
   * @param lead
   *          Minimum number of leading digits
   * @param decimal
   *          Number of decimal digits to show
   * @return Formatted number string
   */
  static public String nf(float num, int lead, int decimal) {
    if (formatFloat==null) nfInitFormats();
    formatFloat.setMinimumIntegerDigits(lead);
    formatFloat.setMaximumFractionDigits(decimal);
    formatFloat.setMinimumFractionDigits(decimal);

    return formatFloat.format(num).replace(",", ".");
  }

  static public String nf(double num, int lead, int decimal) {
    return nf((float)num,lead,decimal);
  }

  /**
   * Format floating point number for printing with maximum 3 decimal points.
   * 
   * @param num
   *          Number to format
   * @return Formatted number string
   */
  static public String nf(float num) {
    return nf(num,0,3);
  }

  static public String nf(float num,int prec) {
    return nf(num,1,prec);
  }

  static public String nf(double num) {
    return nf((float)num);
  }

  /**
   * Format integer number for printing, padding with zeros if number has fewer
   * digits than desired.
   * 
   * @param num
   *          Number to format
   * @param digits
   *          Minimum number of digits to show
   * @return Formatted number string
   */
  static public String nf(int num, int digits) {
    if (formatInt==null) nfInitFormats();
    formatInt.setMinimumIntegerDigits(digits);
    return formatInt.format(num);
  }

  public static String strPad(String s,int len,char c) {
    len-=s.length();
    while(len>0) {
      s+=c;
      len--;
    }
    
    return s;
  }
  

  public static <T> String str(ArrayList<T> o) {
    return str(o,NEWLN,null);
  }

  public static <T> String str(ArrayList<T> o, char delim,String enclosure) {
    StringBuffer buf=strBufGet();
    if(o==null) buf.append("null");
    else {
      int id=0;
      for(T oo:o) {
        if(buf.length()>0) buf.append(delim); 
//        buf.append(id++).append(' ');
        buf.append(oo.toString());
      }
    }
    
    if(enclosure!=null) {
      buf.insert(0, enclosure.charAt(0));
      buf.append(enclosure.charAt(1));
    }
    
    return strBufDispose(buf);
  }
  
  
  //////////////////////////////////////////
  // STRING BUFFER POOL
  
  protected static ArrayList<StringBuffer> strBufFree,strBufBusy;

  protected static String strBufDispose(StringBuffer buf) {
    if(strBufBusy!=null) {
      strBufBusy.remove(buf);
      strBufFree.add(buf);
    }
      
    return buf.toString();
  }

  
  protected static StringBuffer strBufGet() {
    try {
      StringBuffer buf;
      
      if(strBufBusy==null) {
        strBufBusy=new ArrayList<StringBuffer>();
        strBufFree=new ArrayList<StringBuffer>();
      }
      
      if(strBufFree.size()>1) {
        buf=strBufFree.remove(0);
        buf.setLength(0);
      }
      else buf=new StringBuffer();
      
      strBufBusy.add(buf);
      
      return buf;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return new StringBuffer();
  }
  
  //////////////////////////////////////////
  // STRING MANIPULATION
  
  public String strTrim(String s,int newlen) {
    return s.substring(0,newlen);
  }

  public String strStripContainer(String str) {
    String s=str.trim();
    int len=s.length();
    char ch1=s.charAt(0);
    char ch2=s.charAt(len-1);
    
    if((ch1=='[' && ch2==']') ||
        (ch1=='<' && ch2=='>')) {
      s=s.substring(1,len-1);
      return s;
    }
    
    return str;
  }

}

