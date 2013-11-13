/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Iterator;

import unlekker.mb2.util.UMB;

import java.util.*;

import processing.core.PImage;

public class UGeoGroup extends UMB implements Iterable<UFace> {
  public UGeo parent;
  public int type;
  private int atStart,atEnd;
  public ArrayList<UFace> faces;
  
  public UGeoGroup(UGeo model, int type) {
    parent=model;
    this.type=type;
    faces=new ArrayList<UFace>();
  }

  public UGeoGroup begin() {
    atStart=parent.sizeF();
    return this;
  }
  
  public UGeoGroup end() {
    atEnd=parent.sizeF();
    
    for(int i=atStart; i<atEnd; i++) {
      faces.add(parent.getF(i));
    }
    log(str());

    return this;    
  }
  
  public int size() {
    return faces.size();
  }
  
  public String typeName() {
    if(!groupTypeNames.containsKey(type)) return NULLSTR; 
    return groupTypeNames.get(type);
  }

  public Iterator<UFace> iterator() {
    return faces.iterator();
  }

  public UGeoGroup remove(UFace ff) {
    if(faces.indexOf(ff)>-1) faces.remove(ff);
    return this;    
  }

  public ArrayList<UFace> getF() {
    return faces;
  }
  
  public String str() {
    return strf("[%s n=%d]",
        typeName(),size()
        );
  }

  public UVertexList getV() {
    UVertexList vl=new UVertexList();
    vl.setOptions(NOCOPY|NODUPL);
    for(UFace ff:faces) {
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
  /*
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
   */
  
//  public ArrayList<String> strGroup() {
//    ArrayList<String> s=new ArrayList<String>();
//    for(int i=0; i<sizeGroup(); i++) {
//      s.add(strGroup(i));
//    }
//    return s;
//  }
//
//  public String strGroup(int id) {
//    int[] dat=faceGroups.get(id);
//    return strf("[%s n=%d|%d %d]",
//        groupTypeNames.get(dat[2]),dat[1]-dat[0]+1,dat[0],dat[1]
//        );
//  }

  
}
