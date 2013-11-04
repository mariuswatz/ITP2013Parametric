/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Arrays;

import unlekker.mb2.util.UMB;


public class USubdivision extends UMB  {

  /**
   * Subdivides a single face according to the specified strategy. 
   * @param f
   * @param type
   * @return
   */
  
  public static ArrayList<UFace> subdivide(UFace f,int type) {
    ArrayList<UFace> ff=new ArrayList<UFace>();
    UGeo geo=f.parent;
    UVertex vv[]=f.getV();      
    
    if(type==SUBDIVCENTROID) {
      f.centroid=null;
      UVertex c=f.centroid();
      
      ff.add(new UFace(geo, vv[0], vv[1], c));
      ff.add(new UFace(geo, vv[1], vv[2], c));
      ff.add(new UFace(geo, vv[2], vv[0], c));
    }
    else if(type==SUBDIVMIDEDGES) {
      UVertex mid[]=f.getMidEdges();
      ff.add(new UFace(geo, vv[0], mid[0], mid[2]));
      ff.add(new UFace(geo, mid[0], vv[1], mid[1]));
      ff.add(new UFace(geo, mid[2], mid[0],mid[1]));
      ff.add(new UFace(geo, mid[1], vv[2],mid[2]));      
    }
    
    return ff;    
  }
  
  public static UGeo subdivide(UGeo geo,int type) {
    ArrayList<UFace> newf=new ArrayList<UFace>();
    
    for(UFace ff:geo.getF()) {
      ArrayList<UFace> l=subdivide(ff, type);
      for(UFace nf:l) {
        newf.add(nf);
//        log(str(nf.vID));
      }
    }
    
//    log(geo.sizeF()+" newf "+newf.size());
    geo.getF().clear();
    
    geo.addFace(newf);
//    log(geo.str());
    return geo;
  }
}
