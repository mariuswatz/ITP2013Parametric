package itp;

import processing.core.PApplet;
import unlekker.mb2.util.UMB;


/*
 * Dummy "main" class - simplifies launching different
 * PApplet sketches.
 */
public class ITPMain extends PApplet {


  static public void main(String args[]) {    
    String name[]=new String[] { 
        "itp.MBToPDF01", 
        "itp.MBTest",
        "itp.ITPTestApp" 
    };
    
    String theApp=name[0];
    
    // full name (including package) of your PApplet class goes here
    PApplet.main(new String[] {theApp});
  }

}
