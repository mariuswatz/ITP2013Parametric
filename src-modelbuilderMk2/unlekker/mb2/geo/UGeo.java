/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;

import unlekker.mb2.util.UMB;

import java.util.*;

public class UGeo extends UMB  {
  /**
   * Master vertex list, contains all vertices for the face geometry
   * contained in the UGeo instance. Use <code>enable(NODUPL)</code>
   * to ensure no duplicate vertices exist. 
   */
  public UVertexList vl;
  
  /**
   * ArrayList of triangle faces. 
   */
  public ArrayList<UFace> faces;

  private UVertexList vltmp;
  private int shapeType;
  public ArrayList<int[]> faceGroups;

  
  public UGeo() {
    vl=new UVertexList();
    setOptions(NODUPL);
    faces=new ArrayList<UFace>();
    faceGroups=new ArrayList<int[]>();
  }

  public UGeo(UGeo v) {
    this();
    set(v);
  }

  /** Returns a copy of this UGeo instance.
   *  
   * @return
   */
  public UGeo copy() {
    return new UGeo(this);
  }

  public UGeo clear() {
    vl.clear();
    faces.clear();
    faceGroups.clear();
    return this;
  }

  public UGeo removeDupl() {
    for(UFace ff:faces) ff.getV();
    getV().removeDupl(true);
    for(UFace ff:faces) {
      ff.getVID();
      ff.getV(true);
    }
    
    return this;
  }

  
  public UGeo setOptions(int opt) {
    super.setOptions(opt);
    if(vl!=null) vl.setOptions(opt);
    return this;
  }

  public UGeo enable(int opt) {
    super.setOptions(opt);
    vl.setOptions(opt);
    return this;
  }

  public UGeo disable(int opt) {
    super.disable(opt);
    return this;
  }

  /**
   * Copies the mesh data contained <code>model</code> to this UGeo instance,
   * replacing any existing data. The <code>model.vl</code> vertex list
   * is copied using {@see UVertexList#copy()}, then face data is copied
   * by creating new UFace instances using the {@see UFace#vID} vertex indices. 
   *  
   * @param model
   * @return
   */
  public UGeo set(UGeo model) {
   vl=model.getV().copy();
   faces=new ArrayList<UFace>();
   for(UFace ff:model.getF()) {
     UFace newFace=new UFace(this,ff.vID);
     newFace.setColor(ff.col);
     addFace(newFace);
   }
   return this;
  }
  
  public UGeo setColor(int col) {
    for(UFace ff:getF()) ff.setColor(col);
    return this;
  }


  private UGeo groupBegin() {
    return groupBegin(-1);
  }

  public UGeo groupBegin(int type) {
    faceGroups.add(new int[] {sizeF(),-1,type});
    return this;
  }

  public UGeo groupJoinAll() {
    return groupJoin(0, sizeGroup()-1);
  }

  public UGeo groupJoin(int id1,int id2) {
    int id[]=new int[] {getGroupID(id1)[0],getGroupID(id2)[1],getGroupID(id1)[2]};
    
    int cnt=(id2-id1)+1;
    while((cnt--)>0) faceGroups.remove(id1);
    faceGroups.add(id);
    return this;
  }

  
  public UGeo groupEnd() {
    int[] id=faceGroups.get(sizeGroup()-1);
    id[1]=sizeF()-1;
    if(id[1]-id[0]<1) {
      log("UGeo: zero cnt group "+str(id));
    }
    
    return this;
  }

  
  public int[] getGroupID(int id) {
    return faceGroups.get(id);
  }

  /**
   * Returns a list of UFace instances belonging to a given 
   * group of faces. 
   * 
   * When groups of triangles are added to UGeo by methods like
   * triangleFan(), quadstrip() or add(UGeo), the start and end IDs
   * for the faces produced by that operation is stored internally so
   * that the group can later be retrieved.
   * 
   * @param id
   * @return
   */
  
  public ArrayList<UFace> getGroup(int id) {
    int[] fid=faceGroups.get(id);
    ArrayList<UFace> ff=new ArrayList<UFace>();
    for(int i=fid[0]; i<=fid[1]; i++) ff.add(getF(i));
    return ff;
  }

  public UVertexList getGroupV(int id) {
    int[] fid=faceGroups.get(id);
    UVertexList vl=new UVertexList();
    vl.setOptions(NOCOPY);
    for(int i=fid[0]; i<=fid[1]; i++) {
      int cnt=i-fid[0];
      UFace ff=getF(i);
      vl.add(ff.getV());
    }
    return vl;
  }

  /**
   * If the specified group is of type QUAD_STRIP, this method will return an ArrayList of UVertex[].
   * Each array contains the four vertices of a single quad. Vertices are ordered in clockwise order, according to the logic of 
   * beginShape(QUADS).
   * @param id
   * @return
   */
  public ArrayList<UVertex []> getGroupQuadV(int id) {
    ArrayList<UVertex []> res=null;
    
    int[] fid=faceGroups.get(id);
    if(fid[2]!=QUAD_STRIP) return null;
    
    
    UVertexList vl=new UVertexList();
    vl.setOptions(NOCOPY);

    int n=(fid[1]-fid[0]+1)/2;
//    log("getGroupQuadV "+n+" "+str(fid));
    res=new ArrayList<UVertex[]>();
    
    int cnt=fid[0];
    // vertex order for the faces = [0,2,1] [3,1,2] 
    for(int i=0; i<n; i++) {
      UVertex v1[]=getF(cnt++).getV();
      UVertex v2[]=getF(cnt++).getV();
      res.add(new UVertex[]{v1[0],v1[2],v2[0],v2[2]});
    }
    
    return res;
  }

  public int sizeGroup() {
    return faceGroups.size();
  }



  public UGeo add(UGeo model) {
    log(model.str());
    
    groupBegin(TRIANGLES);
    for(UFace ff:model.getF()) {
      UVertex vv[]=ff.getV();
      addFace(vv);
    }    
    groupEnd();
    
/*    if(model.sizeGroup()>0) {
      int gn=model.sizeGroup();
      for(int i=0; i<gn; i++) {
        int id[]=model.getGroupID(i);
        groupBegin(id[2]);
        ArrayList<UFace> gr=model.getGroup(i);
        for(UFace ff:gr) {
          UVertex vv[]=ff.getV();
          addFace(vv);
        }    
        groupEnd();
      }
    }
    
    else {
      for(UFace ff:model.getF()) {
        UVertex vv[]=ff.getV();
        addFace(vv);
      }    
    }
*/    
    return this;
    
  }
  
  //////////////////////////////////////////
  // BOUNDING BOX, DIMENSIONS 


  public UBB bb() {
    return bb(false);
  }

  public UBB bb(boolean force) {
    return vl.bb(force);
  }

  /** 
   * @return The centroid of this mesh, found by calling
   * <code>vl.bb().centroid()</code>.
   */
  public UVertex centroid() {
    return bb().centroid;
  }

  /** 
   * Translates mesh so that its centroid lies at
   * the origin.
   */
  public UGeo center() {
    return translateNeg(bb().centroid);
  }

  public static void center(ArrayList<UGeo> models) {
    UBB btmp=new UBB();
    for(UGeo geo:models) btmp.add(geo.bb());
    btmp.calc();
    UVertex c=btmp.centroid;
    for(UGeo geo:models) geo.translateNeg(c);
    
  }

  /**
   * Translates mesh so that its centroid lies at
   * at the provided point in space.
   * @param v1 Point where mesh will be centered
   * @return
   */
  public UGeo centerAt(UVertex v1) {
    return center().translate(v1);
  }

  /**
   * Translate mesh by the negative of the given vertex,
   * equivalent to <code>translate(-v1.x,-v1.y,-v.z)</code>.
   * @param v1
   * @return
   */
  public UGeo translateNeg(UVertex v1) {
    return translate(-v1.x,-v1.y,-v1.z);    
  }

  public UGeo translate(UVertex v1) {    
    return translate(v1.x,v1.y,v1.z);    
  }

  public UGeo translate(float mx,float my) {
    return translate(mx,my,0);
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

  public UGeo scaleToDim(float max) {
    float m=max/bb().dimMax();
    return scale(m,m,m);
  }

  public UGeo scale(float m) {return scale(m,m,m);}

  public UGeo scale(float mx,float my,float mz) {
    vl.scale(mx,my,mz);
    return this;
  }

  /**
   * Returns UVertex instance where x,y,z represent the
   * size of this mesh in the X,Y,Z dimensions.  
   * @return
   */
  public UVertex dim() {
    return bb().dim;
  }


  public float dimX() {return bb().dimX();}
  public float dimY() {return bb().dimY();}
  public float dimZ() {return bb().dimZ();}
  public float dimMax() {return bb().dimMax();}


  //////////////////////////////////////////
  // LIST TOOLS


  /**
   * @param models List of UGeo instances.
   * @return The total number of faces contained in all
   * meshes in the list. 
   */
  public static int sizeF(ArrayList<UGeo> models) {
    int n=0;
    for(UGeo gg:models) if(gg!=null) n+=gg.sizeF();
    return n;
  }

  /**
   * @return Number of triangle faces contained in this mesh.
   */
  public int sizeF() {
    return faces.size();
  }

  /**
   * @return Number of vertices contained in this mesh.
   */
  public int sizeV() {
    return vl.size();
  }

  public ArrayList<UFace> getF() {
    return faces;
  }

  public UFace getF(int id) {
    return faces.get(id);
  }

  /**
   * @return A direct reference to the {@link UVertexList} that
   * is the master vertex list for this mesh.
   */
  public UVertexList getV() {
    return vl;
  }
  
  public UVertex getV(int id) {
    return vl.get(id);
  }

  public static UMB drawModels(ArrayList<UGeo> models) {
    for(UGeo geo:models) geo.draw();
    return UMB.UMB;

  }

  public UGeo draw() {
    return draw(options);
  }

  public UGeo drawNormals(float w) {
    for(UFace ff:faces) ff.drawNormal(w);
    return this;
  }

  public UGeo draw(int theOptions) {
    if(checkGraphicsSet()) {
      g.beginShape(TRIANGLES);
      
      
      int opt=(isEnabled(theOptions,COLORFACE) ? COLORFACE : 0);
      for(UFace f:faces) {
        if(opt==COLORFACE) g.fill(f.col);
        draw(f.getV());
      }
      g.endShape();
    }
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
    vl.bb=null;
    
//    shapeRecord=new UStrip(_type);

    shapeType=_type;
    
    return this;
  }

  public UGeo endShape() {
    groupBegin(shapeType);

    
    int[] vID=vl.addID(vltmp);
    
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

        for(int i=1; i<n; i++) {
          addFace(new int[] {vID[id],vID[id+2],vID[id+1]});
          addFace(new int[] {vID[id+3],vID[id+1],vID[id+2]});
          id+=2;
        }

//        for(int i=0; i<n; i++) {
//          v2=vltmp.get(id);          
//          v3=vltmp.get(id+1);         
//          if(i>0) {
//            addFace(v0,v2,v1);
//            addFace(v3,v1,v2);
//          }
//          v0=v2;
//          v1=v3;
//          id+=2;
//        }
      }
      break;

      case POLYGON:{
        log("UGeo: POLYGON currently unsupported.");
      }
      break;
    }
    
    vltmp.clear();
    
    groupEnd();
    
//    UUtil.log("Faces: "+faceNum);
    return this;

  }

  public UGeo add(UVertexList v1) {
    vl.add(v1);    
    return this;
  }

  public UGeo add(UVertex v1) {
    vl.add(v1);    
    return this;
  }
  
  public UGeo addFace(UVertex vv[]) {
    return addFace(vv[0], vv[1], vv[2]);
  }

  public UGeo addFace(ArrayList<UFace> f) {
    for(UFace ff:f) addFace(ff);
    return this;
  }

  public UGeo addFace(UFace f) {
    faces.add(f);
    return this;
  }

  public UGeo addFace(int vID[]) {
    faces.add(new UFace(this,vID));
    return this;
  }
  
  public UGeo addFace(UVertex v1, UVertex v2, UVertex v3) {
    if(!UFace.check(v1,v2,v3)) {
      log("Invalid face");
      return this;
    }
    if(duplicateF(v1,v2,v3)) {
      log("Duplicate face");
      return this;
    }
    
    faces.add(new UFace(this, v1,v2,v3));    
    vl.bb=null;
    return this;
  }

  public boolean duplicateF(UVertex v1, UVertex v2, UVertex v3) {
    int cnt=0;
    
    UFace ff=new UFace(v1,v2,v3);
    for(UFace theFace:faces) if(theFace.equals(ff)) return true;
    
    // TODO Auto-generated method stub
    return false;
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
   * @param vvl Vertex list
   * @param reverseOrder Add in reverse order?
   * @return UGeo
   */
  public UGeo vertex(UVertexList vvl,boolean reverseOrder) {
    if(reverseOrder) {
      for(int i=vvl.size()-1; i>-1; i--) vertex(vvl.get(i));
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

  public int[] getVID(UVertex vv[]) {    
    return getVID(vv,null);
  }

  public int[] getVID(UVertex vv[],int vid[]) {    
    return vl.getVID(vv,vid);
  }

  /**
   * Get the list index of the provided vertex using 
   * {@link UVertexList.getVID()}. Returns -1 if vertex is not
   * found in list.
   * @param vv
   * @return
   */
  public int getVID(UVertex vv) {    
    return vl.getVID(vv);
  }

  /**
   * Get the list indices of an array of provided vertices using 
   * {@link UVertexList.getVID()}. Index values may include -1 for
   *  vertices that are not found.
   * @param vv
   * @return
   */
  public UVertex[] getVByID(int vID[]) {
    return vl.get(vID);
  }
  
  public UVertex[] getVByID(int vID[],UVertex tmp[]) {
    return vl.get(vID,tmp);
  }

  public UVertex getVertex(int vID) {    
    return vl.get(vID);
  }

  public int addVertex(UVertex v1) {    
    int id=vl.indexOf(v1);
    if(id<0) id=vl.addID(v1);
    return id;
  }

  public UGeo quadstrip(ArrayList<UVertexList> vl2) {
    UVertexList last=null;  
    for(UVertexList vvl:vl2) {
      if(last!=null) quadstrip(last,vvl);
      last=vvl;
    }
    return this;
  }

  /**
   * Add faces representing a triangulation of the provided
   * vertex list. Primarily useful for meshing irregular
   * polygons and point sets representing 2.5D topologies,
   * as well as filling holes in meshes (for instance "capping" 
   * cylindrical structures.) 
   * 
   * The triangulation logic acts in a 2D plane, hence point clouds 
   * representing true 3D volumes will give poor results, requiring
   * convex hull or other re-meshing strategies.
   *  
   *  See {@link UTriangulate} for details. 
   * @param vl
   * @return
   */
  public UGeo triangulation(UVertexList vl) {
    return triangulation(vl,false);
  }

  public UGeo triangulation(UVertexList vl,boolean reverse) {
    int oldSize=sizeF();
    new UTriangulate(this, vl);

    if(reverse) {
      // reverse all new faces
      for(int i=oldSize; i<sizeF(); i++) faces.get(i).reverse();      
    }
    
    return this;
  }

  public UGeo triangleFan(UVertex c,UVertexList vl) {
    return triangleFan(c, vl,false);
  }

  public UGeo triangleFan(UVertex c,UVertexList vl,boolean reverse) {
    beginShape(TRIANGLE_FAN);
    vertex(c);
    vertex(vl,reverse);
    endShape();
    
    return this;
  }

  /**
   * Add triangle fan using the vertices in <code>vl</code>, with the centroid of
   * <code>vl</code> as the central vertex.
   * @param vl
   * @return
   */
  public UGeo triangleFan(UVertexList vl) {
    return triangleFan(vl,false);
  }

  /**
   * Add triangle fan using the vertices in <code>vl</code>, with the centroid of
   * <code>vl</code> as the central vertex. 
   * @param vl
   * @param reverse Flag to indicate whether vertices should be added in reverse order
   * @return
   */
  public UGeo triangleFan(UVertexList vl,boolean reverse) {
    UVertex c=vl.centroid();
    return triangleFan(vl,c,reverse);
  }

  /**
   * Add triangle fan using the vertices in <code>vl</code>, using <code>c</code> 
   * as the central vertex.
   * @param vl
   * @param c Central vertex of the fan.
   * @return
   */
  public UGeo triangleFan(UVertexList vl,UVertex c) {
    return triangleFan(vl,c,false);
  }

  /**
   * Add triangle fan using the vertices in <code>vl</code>, using <code>c</code> 
   * as the central vertex.
   * @param vl
   * @param c Central vertex of the fan.
   * @param reverse Flag to indicate whether vertices should be added in reverse order
   * @return
   */
  public UGeo triangleFan(UVertexList vl,UVertex c,boolean reverse) {
    
    beginShape(TRIANGLE_FAN);
    vertex(c);
    vertex(vl,reverse);
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
  
  
  public boolean writeSTL(String filename) {
    return UGeoIO.writeSTL(filename, this);
  }
  
  public String str() {return str(false);}
  
  public String str(boolean complete) {
    StringBuffer buf=strBufGet();
    
    buf.append(UGEO).append(TAB).append("f="+sizeF());
    buf.append(TAB).append("v="+sizeV());      

    if(complete) {
      buf.append(NEWLN).append(vl.str());
      
      buf.append(NEWLN).append("Face ID\t");
      int cnt=0;
      for(UFace ff:faces) {
        if(cnt++>0) buf.append(TAB);
        buf.append(ff.vID[0]).append(TAB);
        buf.append(ff.vID[1]).append(TAB);
        buf.append(ff.vID[2]);
      }
    }
      
    
    return "["+strBufDispose(buf)+"]";
  }

  public static UGeo box(float w,float h,float d) {
    return UGeoGenerator.box(w,h,d);
  } 
  
  public static UGeo box(float w) {
    return UGeoGenerator.box(w);
  }    
  
  public static UGeo cyl(float w,float h,int steps) {
    return UGeoGenerator.cyl(w, h, steps);
  }
}
