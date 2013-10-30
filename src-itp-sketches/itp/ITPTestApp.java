package itp;

import processing.core.PApplet;
import unlekker.mb2.util.UMB;


public class ITPTestApp extends PApplet {

  public void setup() {
    size(600,600, OPENGL);   
    UMB.setPApplet(this);

  }

  public void draw() {
    background(0);
    fill(255);
    
    textAlign(CENTER);
    text(this.getClass().getSimpleName(),
      width/2,height/2);
  }
  
  public void keyPressed() {
  }

  static public void main(String args[]) {
    
    // full name (including package) of your PApplet class goes here
    PApplet.main(new String[] { 
        "itp.ITPTestApp" 
    });
  }

}
