//USimpleGUI gui;

float damper=0.1,volume=1;
boolean doEnvelope=true,drawColor=true;

void initGUI() {
    cf = addControlFrame("extra", 200,200);

 
}
ControlFrame cf;

ControlFrame addControlFrame(String theName, int theWidth, int theHeight) {
  Frame f = new Frame(theName);
  ControlFrame p = new ControlFrame(this, theWidth, theHeight);
  f.add(p);
  p.init();
  f.setTitle(theName);
  f.setSize(p.w, p.h);
  f.setLocation(100, 100);
  f.setResizable(false);
  f.setVisible(true);
  return p;
}

// the ControlFrame class extends PApplet, so we 
// are creating a new processing applet inside a
// new frame with a controlP5 object loaded
public class ControlFrame extends PApplet {

  int w, h;

  int abc = 100;
  ControlP5 cp5;

  public void setup() {
    size(w, h);
    frameRate(25);
    cp5 = new ControlP5(this);

  cp5.addSlider("damper").
    setRange(0.001,1).plugTo(parent,"damper");
    
  cp5.addSlider("volume").
    setRange(0,20).plugTo(parent,"volume");

  cp5.addToggle("drawColor",drawColor).
  plugTo(parent,"drawColor");
  cp5.addToggle("doEnvelope",doEnvelope).
  plugTo(parent,"doEnvelope");
  
  }

  public void draw() {
      background(abc);
  }
  
  private ControlFrame() {
  }

  public ControlFrame(Object theParent, int theWidth, int theHeight) {
    parent = theParent;
    w = theWidth;
    h = theHeight;
  }


  public ControlP5 control() {
    return cp5;
  }
  
  
  ControlP5 cp5;

  Object parent;

  
}
//void keyPressed() {
//  if(key=='s') saveFrame(
//    UIO.getIncrementalFilename(
//      this.getClass().getSimpleName()+" ###.png", 
//      sketchPath));
//}
