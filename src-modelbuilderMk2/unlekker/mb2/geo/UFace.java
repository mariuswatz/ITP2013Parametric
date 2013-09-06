package unlekker.mb2.geo;

import unlekker.mb2.util.UBasic;

public class UFace extends UBasic  {
  public static int globalID;

  public UGeo parent;
  public int ID;  
  public int vID[];
  public UVertex v[];
  
  public int col;
  public UVertex normal;
  
  public UFace() {
    ID=globalID++;
    vID=new int[] {-1,-1,-1};
    col=Integer.MAX_VALUE;
  }

  public UFace(UFace v) {
    this();
    set(v);
  }

  public UFace(UGeo model, UVertex v1, UVertex v2, UVertex v3) {
    this();
    if(model!=null) parent=model;
    
    set(v1,v2,v3);
    
    log("F "+ID+" "+vID[0]+" "+vID[1]+" "+vID[2]);
  }

  public UFace set(UVertex v1, UVertex v2, UVertex v3) {
    if(parent!=null) {
      vID[0]=parent.addVertex(v1);
      vID[1]=parent.addVertex(v2);
      vID[2]=parent.addVertex(v3);
    }
    else {
      if(v==null) v=new UVertex[3];
      v[0]=v1;
      v[1]=v2;
      v[2]=v3;
    }
    
   
    return this;

  }

  public UVertex[] getVertices() {
    if(parent==null) return v;
    
    if(v==null) v=new UVertex[3];
 
    int id=0;    
    for(int vid:vID) v[id++]=parent.getVertex(vid);
    
    return v;
  }
  
  public UFace copy() {
    return new UFace(this);
  }
    
  public UFace setParent(UGeo geo) {
    parent=geo;
    return this;
  }

  public UFace set(UFace v) {
    vID=new int[] {v.vID[0],v.vID[1],v.vID[2]};
    col=v.col;
    parent=v.parent;
    
    if(v.normal!=null) {
      if(normal!=null) normal.set(v.normal.x,v.normal.y,v.normal.x);
    }
    
   return this;
  }

  public UFace setColor(int a) {
    col=a;
    return this;
  }

  public UFace setColor(float a,float b,float c) {
    return setColor(color(a,b,c));
  }
}
