package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UGeoIO;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UFile;

public class UVLTest extends PApplet {
  ArrayList<UVertexList> vvl;
  
  UVertexList vl,vl2;
  UGeo geo;
  
  public void setup() {
    size(600,600, OPENGL);
    
    UVertexList.setGraphics(g);
    vl=new UVertexList();
//    vl.enable(vl.NOCOPY).enable(vl.NODUPL);
    vl.log(vl.optionStr());
    
    UVertex v=new UVertex();
    vl.add(v.copy());
    vl.add(v.add(50,100).copy());
    vl.add(v.add(50,-50).copy());
    vl.add(v.add(50,-50).copy());

    vl.add(new UVertex(50,100));
    
    vl.removeDupl(false);
    
    vl.log(vl.str());
    vl.log(vl.strWithID());
    vl.log("NODUPL "+vl.isEnabled(vl.NODUPL));
    vl.log(vl.get(1).equals(vl.last())+" "+ vl.get(1).distSimple(vl.last()));
    
  }

  public void draw() {
    background(0);
    
    translate(width/2,height/2);
    lights();
  }

  static public void main(String args[]) {
    String sk[]=new String[] {
        "UVLTest",
        "UFileTest"
    };
    
    PApplet.main(new String[] {"unlekker.mb2.test."+sk[0]});
  }

  
}
