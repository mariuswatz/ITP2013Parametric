package unlekker.mb2.util;


/**
 * Constants used by the ModelbuilderMk2 library.
 * 
 * @author marius
 *
 */
public interface UConst extends processing.core.PConstants {
  public static final int NOCOPY=1,NODUPL=2;
  
  /**
   * Version identifier for this release of the Modelbuilder library
   */
  public static final String CREDIT="Marius Watz - http://workshop.evolutionzone.com/";
  public static final String TIMESTR="%2d:%2d:%2d";
  public final static String VERSION = "ModelbuilderMk2-0002c";
  public final static String FSTRXYZ="<%s,%s,%s>";
  public final static String FSTRXY="<%s,%s>";
  
  public static final float EPSILON=1.1920928955078125E-7f;

  public static final int KB=1024,MB=KB*KB;
  public static final int XY=0,XZ=1,YZ=2;
  public static final int SECONDMSEC=1000;
  public static final int MINUTEMSEC=60*SECONDMSEC;
  public static final int HOURMSEC=60*MINUTEMSEC;
  public static final int DAYMSEC=24*HOURMSEC;
  
  public static final int XYZ=0,XZY=1;
  public static final int YZX=2, YXZ=3,ZXY=5, ZYX=6;

  public static final char TAB='\t',COMMA=',',SPACE=' ',ZERO='0';
  public static final char DIRCHAR='/',DIRCHARDOS='\\',NEWLN='\n';      
  public static final String DIRSTR="/",DIRSTRDOS="\\",NULLSTR="null";      
  public static final String ENCLSQ="[]",ENCLTAG="<>";
  public static final String LOGDIVIDER="------";
  public static final String LOGDIVIDERNEWNL="------";
  
  public static final String UGEO="UGEO",UVERTEXLIST="UVL",UVERTEX="UV",UFACE="UF";
  public static final int SMOOTHSTEP=0;
  
  public static final float PTCM=72f/2.54f,PTMM=72f/25.4f,PTINCH=72f;
  
  public static final int VID4KW=3840,VID4H=2160;
  public static final int VID1080PW=1920,VID1080PH=1080;
  public static final int VID720PW=1280,VID720PH=720;
  public static final int VIDWQXGAW=2560,VIDWQXGAH=1600;
  
  public static final int COLORVERTEX=8,COLORFACE=4;

  public static final int SUBDIVCENTROID=0,SUBDIVMIDEDGES=1;
  
}
