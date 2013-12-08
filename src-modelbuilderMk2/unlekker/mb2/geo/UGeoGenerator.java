package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.HashMap;

import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UTask;

public class UGeoGenerator extends UMB {
  static HashMap<String, UGeo> proto;
  
  static {
    proto=new HashMap<String, UGeo>();
  }
  
  public static UGeo sphere(float rad,int res) {
    String key="sph"+res;
    
    if(proto.containsKey(key)) {
      return proto.get(key);
    }
    
    UGeo geo=new UGeo();
    UVertex top,bottom;
    
    UVertexList l=UVertexList.arc(1, -HALF_PI, HALF_PI, res);
    top=l.first();
    bottom=l.last();
    
    l.remove(0).remove(l.size()-1);
    
    ArrayList<UVertexList> stack=new ArrayList<UVertexList>();
    for(int i=0; i<res; i++) {
      float t=map(i,0,res, 0,TWO_PI);
      stack.add(l.copy().rotY(-t));
    }
    stack.add(l.copy());
    
    geo.quadstrip(stack);
    geo.triangleFan(UVertexList.crossSection(0, stack),top);
    geo.triangleFan(UVertexList.crossSection(stack.get(0).size()-1, stack),bottom,true);
    
    proto.put(key, geo);
    return geo.copy().scale(rad);
  }
  
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

  public static UGeo meshBox(float w,float h,float d,int steps) {
    w*=0.5f;
    h*=0.5f;
    d*=0.5f;

    UGeo geo=new UGeo();
    UVertexList ll=UVertexList.line(100,steps);
    ArrayList<UVertexList> meshl=new ArrayList<UVertexList>();

    for(int i=0; i<steps; i++) {
      float t=map(i,0,steps-1,-1,1);
      meshl.add(ll.copy().translate(0,t*50,-50));
    }

    
    for(int i=1; i<steps; i++) {
      UVertexList vl=meshl.get(i).copy().rotX(HALF_PI);
      meshl.add(vl);
    }

    for(int i=1; i<steps; i++) {
      UVertexList vl=meshl.get(i).copy().rotX(PI);
      meshl.add(vl);
    }

    for(int i=1; i<steps; i++) {
      UVertexList vl=meshl.get(i).copy().rotX(HALF_PI*3);
      meshl.add(vl);
    }

    UVertexList.scale(meshl, w/50f,h/50f,d/50f);
    
    geo.quadstrip(meshl);
//    log(geo.bb().str());
    
    meshl.clear();
    ll=UVertexList.line(d*2,steps).rotY(HALF_PI);
    for(int i=0; i<steps; i++) {
      float t=map(i,0,steps-1,-1,1);
      meshl.add(ll.copy().translate(w,t*h,0));
    }
    geo.quadstrip(meshl);
    
    meshl.clear();
    ll.scale(-1,1,1);
    for(int i=0; i<steps; i++) {
      float t=map(i,0,steps-1,1,-1);
      meshl.add(ll.copy().translate(-w,t*h,0));
    }
    geo.quadstrip(meshl);
//    log(geo.bb().str());
    
    return geo;
  }

  public static UGeo extrude(UGeo geo,float offs,boolean makeSolid) {
    return extrude(geo,offs,makeSolid,true);
  }

  public static UGeo extrude(UGeo geo,float offs,boolean makeSolid,boolean addOriginal) {
    boolean force=false;
    geo.check();
    
    UTask task=new UTask("extrude - "+geo.str(),force);

    UGeo extr=geo.copy();
    int n=extr.sizeF()/10,cnt=0;
    n=max(3,extr.sizeF()/10);
    for(UFace tmp:extr.getF()) {
      tmp.reverse();
      if((cnt++)%n==0) 
        task.update(map(cnt,0,extr.sizeF()-1,0,25),"reverse",force);
    }
    
//    extr.vertexNormals();
//    task.update(40,"vertexNormals",force);

    cnt=0;
    n=max(3,extr.sizeV()/10);
    
    UVertex vn=new UVertex();
    for(UVertex tmp:extr.getV()) {
      vn.set(extr.getVNormal(cnt++)).mult(offs);
      tmp.add(vn);
      if(cnt%n==0) 
        task.update(map(cnt,0,extr.sizeV()-1,25,40),"offset vertex",force);
    }

    
    if(makeSolid) {
      task.update(40,"boundary - before | "+geo.str(),force);
      UEdgeList border=geo.getEdgeList();
      task.update(40,"boundary - edgelist",force);
      
      border=border.getBoundary();
      task.update(40,"boundary",force);
      
      int last=-1;
      
      UVertex v1=new UVertex(),v2=new UVertex(),
          v3=new UVertex(),v4=new UVertex();
      UVertex tmp=new UVertex();
      
 
      extr.beginShape(QUADS);
      
      cnt=0;
      n=max(3,border.size()/10);
      
      
      for(UEdge ed:border) {
        v1.set(ed.v[1]);
        v2.set(ed.v[0]);
        
//        if(v2.x>v1.x) {
//          UVertex tmpv=v1;
//          v1=v2;
//          v2=tmpv;
//        }
        
        int id1=geo.getVID(v1);
        int id2=geo.getVID(v2);
        
        if(id1>-1 && id2>-1) {
          tmp.set(geo.getVNormal(id2)).mult(-offs);
          v3.set(v2).add(tmp);

          tmp.set(geo.getVNormal(id1)).mult(-offs);
          v4.set(v1).add(tmp);

          extr.vertex(v1);
          extr.vertex(v2);
          extr.vertex(v3);
          extr.vertex(v4);          
        }
        else {
          logf("Extrude: No ID found for %s and %s",
              ed.v[0].str(),ed.v[1].str());
        }
        if((cnt++)%n==0) task.update(map(cnt,0,border.size()-1,50,90),"extruding edges.",force);

      }
      extr.endShape();
      
      if(addOriginal) {
        task.update(91,"Final: add to geo",force);
        extr.add(geo);
        task.update(100,"added to geo",force);        
      }
    }
    task.update(99,"RemoveDuplV",force);

    extr.removeDuplV();
    task.done();
    
    return extr;
    
  }

  public static UGeo box(float w,float h,float d) {
    return box(1).scale(w,h,d);
  } 
  
  public static UGeo box(float w) {
    UGeo geo=null;
    
    w*=0.5f;
    UVertexList vl,vl2;
    
    vl=UVertexList.circle(w, 4).rotZ(HALF_PI*0.5f).reverse();
//    vl=new UVertexList();
//    vl=new UVertexList().add(-w,-w).add(w,-w).
//        add(w,w).add(-w,w);
    vl2=vl.copy().translate(0,0,w);
    vl.translate(0,0,-w);
    
    log(vl.str());
    log(vl2.str());
    geo=new UGeo();
    geo.beginShape(QUAD_STRIP).
    vertex(vl.get(3)).vertex(vl.get(0)).
    vertex(vl.get(2)).vertex(vl.get(1)).endShape();
    geo.beginShape(QUAD_STRIP).
      vertex(vl2.get(2)).vertex(vl2.get(1)).
      vertex(vl2.get(3)).vertex(vl2.get(0)).endShape();
    geo.quadstrip(vl.close(),vl2.close());
  
    return geo;
//    return geo.groupJoinAll();
  }
  
  public static UGeo cyl(float w,float h,int steps) {
    w*=0.5f;
    h*=0.5f;
    
    UGeo geo=null;
    UVertexList vl,vl2;
//    vl=new UVertexList();
//    for(int i=0; i<steps; i++) {
//      float deg=map(i,0,steps,0,TWO_PI);
//      vl.add(new UVertex(w,0).rotY(deg));
//    }
//    
//    vl2=vl.copy().translate(0,h,0).close();
//    vl.translate(0,-h,0).close();
//    geo=new UGeo().quadstrip(vl,vl2).
//        triangleFan(vl,true)
//        .triangleFan(vl2);
    
    vl=UVertexList.circle(w, steps).rotX(-HALF_PI);
    vl2=vl.copy().translate(0,-h,0);
    vl.translate(0,h,0);
    
    
    geo=new UGeo().triangleFan(vl,true).triangleFan(vl2);
    geo.quadstrip(vl,vl2);

    return geo;//geo.groupJoinAll();
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
