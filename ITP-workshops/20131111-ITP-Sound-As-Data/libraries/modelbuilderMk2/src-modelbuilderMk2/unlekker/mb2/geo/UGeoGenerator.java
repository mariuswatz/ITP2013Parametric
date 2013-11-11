package unlekker.mb2.geo;

import java.util.ArrayList;

import unlekker.mb2.util.UMB;

public class UGeoGenerator extends UMB {

  
  public static UGeo meshPlane(float w,float h,int steps) {
    UGeo geo=new UGeo();
    UVertexList ll=UVertexList.line(w,steps);
    ArrayList<UVertexList> meshl=new ArrayList<UVertexList>();
    
    for(int i=0; i<steps; i++) {
      float t=map(i,0,steps-1,-0.5f,0.5f);
      meshl.add(ll.copy().translate(0,t*h));
    }
    return geo.quadstrip(meshl);
  }

  public static UGeo box(float w,float h,float d) {
    return box(1).scale(w,h,d);
  } 
  
  public static UGeo box(float w) {
    UGeo geo=null;
    
    w*=0.5f;
    UVertexList vl,vl2;
    vl=new UVertexList().add(-w,-w).add(w,-w).
        add(w,w).add(-w,w);
    vl2=vl.copy().translate(0,0,w);
    vl.translate(0,0,-w);
    
    geo=new UGeo().quadstrip(vl.close(),vl2.close());
    geo.beginShape(QUAD_STRIP).
    vertex(vl.get(3)).vertex(vl.get(0)).
    vertex(vl.get(2)).vertex(vl.get(1)).endShape();
    geo.beginShape(QUAD_STRIP).
      vertex(vl2.get(2)).vertex(vl2.get(1)).
      vertex(vl2.get(3)).vertex(vl2.get(0)).endShape();
    
    return geo.groupJoinAll();
  }
  
  public static UGeo cyl(float w,float h,int steps) {
    w*=0.5f;
    h*=0.5f;
    
    UGeo geo=null;
    UVertexList vl,vl2;
    vl=new UVertexList();
    for(int i=0; i<steps; i++) {
      float deg=-map(i,0,steps,0,TWO_PI);
      vl.add(new UVertex(w,0).rotY(deg));
    }
    
    vl2=vl.copy().translate(0,h,0).close();
    vl.translate(0,-h,0).close();
    geo=new UGeo().quadstrip(vl,vl2).triangleFan(vl,true).triangleFan(vl2);
    return geo.groupJoinAll();
  }

  public static UGeo roundedBox(float w,float h,float d,float r,int steps) {
    return null;
  }
  
  public static UVertexList roundedProfile(float h,float r,int steps) {
    UVertexList vl=new UVertexList();
    UVertexList arc=new UVertexList();
    for(int i=0; i<steps; i++) {
      arc.add(new UVertex(0,r,0).
          rotZ(map(i,0,steps-1,0,HALF_PI)));
    }
    arc.translate(0,h-r,0);
    
    for(int i=0; i<steps; i++) {
      int id=(steps-1)-i;
      arc.add(arc.get(id).copy().mult(1,-1,1));
    }
    
    
    
    
    return arc;
  }
}
