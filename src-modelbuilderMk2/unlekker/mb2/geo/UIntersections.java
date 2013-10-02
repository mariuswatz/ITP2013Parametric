package unlekker.mb2.geo;

import unlekker.mb2.util.UMB;

public class UIntersections extends UMB {

  public static UVertex intersectLinePlane(UVertex a,UVertex b,UFace plane) {
    UVertex D1,D2,v[],n,origin;
    
    v=plane.getV();
    n=plane.normal();
    
    D1=v[0].delta(v[1]);
    D2=v[0].delta(v[2]);
    n=D1.cross(D2).norm();

    origin=v[0];
    
    // distance plane -> a/b
    float dist=origin.mag();
    dist=0;
    float da = a.dot(n) - dist;
    float db = b.dot(n) - dist;

  //The distance is < zero if the point is on the backside of the plane. It's zero if the point is on the plane, and positive otherwise. If both points have a negative distance we can remove them. The line will be entirely on the backside of the plane. If both are positive we don't have to do anything (the line is completely visible). But if the signs are different we have to calculate the intersection point of the plane and the line:
    if(da>0 && db>0) return null;
    if(da<0 && db<0) return null;
    if(da==0 || db==0)  return null;
    
    UVertex i=new UVertex();
    float s = da/(da-db);   // intersection factor (between 0 and 1)
  
    i.set(b).sub(a).mult(s).add(a);
    i.set(a.x + s*(b.x-a.x),
        a.y + s*(b.y-a.y),
        a.z + s*(b.z-a.z));
    i.log("Intersection: "+ i.str()+" "+s);
    return i;
  
  }
  
  public static UVertex intersectLineY(UVertex a,UVertex b,float Y) {
    UVertex D1,D2,v[],n,origin;
    
    n=new UVertex(0,1,0);
    
//    D1=v[0].delta(v[1]);
//    D2=v[0].delta(v[2]);
//    n=D1.cross(D2).norm();

//    origin=v[0];
    
    // distance plane -> a/b
//    float dist=origin.mag();
    float dist=0;
    float da = a.dot(n) - dist;
    float db = b.dot(n) - dist;

  //The distance is < zero if the point is on the backside of the plane. It's zero if the point is on the plane, and positive otherwise. If both points have a negative distance we can remove them. The line will be entirely on the backside of the plane. If both are positive we don't have to do anything (the line is completely visible). But if the signs are different we have to calculate the intersection point of the plane and the line:
    if(da>0 && db>0) return null;
    if(da==0 || db==0)  return null;
    
    UVertex i=new UVertex();
    float s = da/(da-db);   // intersection factor (between 0 and 1)
  
    i.set(b).sub(a).mult(s).add(a);
//    i.set(a.x + s*(b.x-a.x),
//        a.y + s*(b.y-a.y),
//        a.z + s*(b.z-a.z));
    i.log(i.str()+" "+s);
    return i;
  
  }

}
