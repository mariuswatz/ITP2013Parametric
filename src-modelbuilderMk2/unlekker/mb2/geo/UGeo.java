package unlekker.mb2.geo;

import java.util.ArrayList;

import unlekker.mb2.util.UBasic;

import java.util.*;

public class UGeo extends UBasic  {
  public UVertexList vl;
  
  private UVertexList vltmp;
  private int shapeType,shapeStart,shapeEnd;
  public ArrayList<UFace> faces;
  

  
  public UGeo() {
    vl=new UVertexList();
    faces=new ArrayList<UFace>();
  }

  public UGeo(UGeo v) {
    this();
    set(v);
  }

  public UGeo copy() {
    return new UGeo(this);
  }

  public UGeo set(UGeo v) {
   return this;
  }

  public ArrayList<UFace> getFaces() {
    return faces;
  }
  
  public void draw(UVertex v[]) {
    for(UVertex vv:v) g.vertex(vv.x,vv.y,vv.z);
  }
  
  public void draw() {
    g.beginShape(TRIANGLES);
    
    int opt=(enabled(COLORFACE) ? COLORFACE : 0);
    for(UFace f:faces) {
      if(opt==COLORFACE) g.fill(f.col);
      draw(f.getVertices());
    }
    g.endShape();
  }
  
  public UGeo translate(float mx,float my,float mz) {
    vl.translate(mx, my, mz);
    return this;
  }

  public UGeo rotX(float deg) {
    vl.rotX(deg);
    return this;
  }

  public UGeo rotY(float deg) {
    vl.rotY(deg);
    return this;
  }

  public UGeo rotZ(float deg) {
    vl.rotZ(deg);
    return this;
  }

  ///////////////////////////////////////////////////
  // BEGINSHAPE / ENDSHAPE METHODS
  
  
  /**
   * Starts building a new series of faces, using the same logic 
   * as <a href="http://processing.org/reference/beginShape_.html">PApplet.beginShape()</a>.
   * Currently supports the following types: TRIANGLE_FAN, TRIANGLE_STRIP, TRIANGLES, QUADS, QUAD_STRIP
   * 
   * While shape is being built vertices are stored in a temporary 
   * array, and only the ones that are used are copied to the vert vertexlist.
   * @param _type Shape type (TRIANGLE_FAN, TRIANGLE_STRIP, TRIANGLES, QUADS, QUAD_STRIP)
   */
  public UGeo beginShape(int _type) {
    if(vltmp==null) vltmp=new UVertexList();
    else vltmp.clear();
    
//    shapeRecord=new UStrip(_type);

    shapeType=_type;
    shapeStart=faces.size();
    
    return this;
  }

  public UGeo endShape() {
    switch (shapeType) {
      case TRIANGLE_FAN: {
        UVertex cp=vltmp.first();
        int n=(vltmp.size()-1)-1;
        int id=1;
        
        for(int i=0; i<n; i++) {
          addFace(cp,vltmp.get(id++),vltmp.get(id));
        }
      }
      break;

      case TRIANGLES: {
        int n=(vltmp.size())/3;
        int id=0;
        
        for(int i=0; i<n; i++) {
          addFace(vltmp.get(id++),vltmp.get(id++),vltmp.get(id++));
        }
      }
      break;

      case TRIANGLE_STRIP: {
        log("UGeo: TRIANGLE_STRIP currently unsupported.");

//        int stop = bvCnt - 2;
//        for (int i = 0; i < stop; i++) {
//          // HANDED-NESS ISSUE
////          if(i%2==1) addFace(bv[i], bv[i+2], bv[i+1]);
////          else addFace(bv[i], bv[i+1], bv[i+2]);
//          if(i%2==1) addFace(new UVertex[] {bv[i], bv[i+2], bv[i+1]});
//          else addFace(new UVertex[] {bv[i], bv[i+1], bv[i+2]});
//        }
      }
      break;

      // Processing order: bottom left,bottom right,top right,top left
//      addTriangle(i, i+1, i+2);
//      addTriangle(i, i+2, i+3);
      case QUADS: {
        int n=(vltmp.size())/4;
        int id=0;
        UVertex v0,v2;
        
        for(int i=0; i<n; i++) {
          v0=vltmp.get(id);
          v2=vltmp.get(id+2);          
          addFace(v0,vltmp.get(id+1),v2);
          addFace(v0,v2,vltmp.get(id+3));
          id+=4;
        }
      }
      break;

      case QUAD_STRIP: {
        int n=vltmp.size()/2;
        int id=0;
        UVertex v0=null,v1=null,v2=null,v3=null;
        
        for(int i=0; i<n; i++) {
          v2=vltmp.get(id);          
          v3=vltmp.get(id+1);         
          if(i>0) {
            addFace(v0,v2,v1);
            addFace(v1,v2,v3);
          }
          v0=v2;
          v1=v3;
          id+=2;
        }
      }
      break;

      case POLYGON:{
        log("UGeo: POLYGON currently unsupported.");
      }
      break;
    }
    
    vltmp.clear();
    
    shapeEnd=faces.size();

//    UUtil.log("Faces: "+faceNum);
    return this;

  }
  
  private void addFace(UVertex v1, UVertex v2, UVertex v3) {
    faces.add(new UFace(this, v1,v2,v3));    
  }

  /**
   * Add vertex to shape being built by <code>beginShape() / endShape()</code>
   * @param x
   * @param y
   * @param z
   * @return 
   */
  public UGeo vertex(float x,float y,float z) {
    vertex(new UVertex(x,y,z));
    return this;
  }

  /**
   * Add UVertex vertex to shape being built by <code>beginShape() / endShape()</code>
   * The vertex information is copied, leaving the original UVertex instance unchanged.
   * @param v
   * @return 
   */
  public UGeo vertex(UVertex v) {
    vltmp.add(v);
    return this;
  }

  /**
   * Add vertex list to shape being built by <code>beginShape() / endShape()</code>. 
   * All vertices are copied, leaving the original instances unchanged.
   * @param vl 
   * @param reverseOrder Add in reverse order?
   * @return 
   */
  public UGeo vertex(UVertexList vvl,boolean reverseOrder) {
    if(reverseOrder) {
      for(int i=vl.size()-1; i>-1; i--) vertex(vvl.get(i));
    }
    else {
      for(UVertex vv:vvl) vertex(vv);
    }
    
    return this;
  }

  /**
   * Adds vertex list to shape being built by <code>beginShape() / endShape()</code>. 
   * All vertices are copied, leaving the original instances unchanged.
   * @return 
   */
  public UGeo vertex(UVertexList vvl) {
    return vertex(vvl, false);
  }

  public UVertex getVertex(int vID) {    
    return vl.get(vID);
  }

  public int addVertex(UVertex v1) {    
    return vl.addID(v1);
  }

  public UGeo quadstrip(ArrayList<UVertexList> vl2) {
    UVertexList last=null;
    for(UVertexList vvl:vl2) {
      if(last!=null) quadstrip(last,vvl);
      last=vvl;
    }
    return this;
  }

  public UGeo triangleFan(UVertexList vl) {
    boolean omit=vl.last().equals(vl.first());
    log("omit "+omit);
    UVertex c=vl.centroid(omit);
    
    
    beginShape(TRIANGLE_FAN);
    vertex(c);
    vertex(vl);
    endShape();
    
    return this;
    
  }
  
  public UGeo quadstrip(UVertexList vl, UVertexList vl2) {
    beginShape(QUAD_STRIP);
    
    int id=0;
    for(UVertex vv:vl) {
      vertex(vv);
      vertex(vl2.get(id++));
    }
    endShape();
    
    return this;
  }
}
