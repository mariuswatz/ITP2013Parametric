package unlekker.mb2.data;

import unlekker.mb2.util.UMB;

public class UPeakFollow extends UMB {
  float max,min,maxAbs,maxMinD;
  
  float maxLimit=Float.NaN,minLimit=Float.NaN;
  float damper=-1,damperNeg;
  
  boolean useMaxLimit=false,useMinLimit=false;
  
  public UPeakFollow() {
    max=Float.MIN_VALUE;
    min=Float.MAX_VALUE;
    maxAbs=0;
  }


  public float min() {
    return min;
  }

  public float max() {
    return max;
  }


  public UPeakFollow set(float dampen,float min,float max) {
    setDamping(dampen);
    return setLimits(min, max);
  }

  public UPeakFollow setDamping(float dampen) {
    damper=constrain(dampen,0,1f);
    damperNeg=1f-damper;
    
    logf("damper %f %f",damper,damperNeg);
    return this; 
  }

  public UPeakFollow setLimits(float minL,float maxL) {
    minLimit=minL;
    maxLimit=maxL;
    

    useMaxLimit=true;
    useMinLimit=true;
    
    return this;
  }

  public UPeakFollow setMax(float max) {
    maxLimit=max;
    useMaxLimit=true;
    
    return this;
  }

  public UPeakFollow update(int val[]) {
    return update(toFloat(val,bufFloat));
  }

  public UPeakFollow update(float val[]) {
    float maxNew=val[0],minNew=val[0];
    float maxAbsNew=0;
    
    for(float v:val) {
      float valAbs=(v<0 ? -v : v);
      maxAbsNew=max(valAbs,maxAbsNew);

      maxNew=(v>maxNew ? v : maxNew);
      minNew=(v<minNew ? v : minNew);
    }

    
    maxNew=useMaxLimit && maxNew>maxLimit ? maxLimit : maxNew;
    maxAbsNew=useMaxLimit && maxAbsNew>maxLimit ? maxLimit : maxAbsNew;
    minNew=useMinLimit && minNew<minLimit ? minLimit : minNew;
    
    if(damper>0) {
      max=damper*maxNew+max*damperNeg;
      maxAbs=damper*maxAbsNew+maxAbs*damperNeg;
      min=damper*minNew+min*damperNeg;
    }
    else {
      max=maxNew;
      min=minNew;
      maxAbs=maxAbsNew;
    }
    
    maxMinD=max-min;
    
    return this;
  }

  public UPeakFollow update(float val) {
    float valAbs=(val<0 ? -val : val);
    valAbs=useMaxLimit ? min(maxLimit,valAbs) : valAbs;
    
    float maxNew=UMB.max(useMaxLimit ? min(val,maxLimit) : val, max);
    float minNew=UMB.min(useMinLimit ? max(val,minLimit) : val, min);
    
    if(damper>0) {
      max=damper*maxNew+max*damperNeg;
      min=damper*minNew+min*damperNeg;
    }
    else {
      max=maxNew;
      min=minNew;
    }
    
    maxMinD=max-min;
    
    return this;
  }

  public float normMinMax(float val) {
    return (val-min)/maxMinD;
  }

  public float norm(float val) {
    return val/max;
  }
  
  public float[] norm(float val[]) {
    for(int i=0; i<val.length; i++) {
      val[i]=val[i]/max;
      val[i]=(val[i]>0 ?
          val[i]>1 ? 1 : val[i] :
            val[i]<-1 ? -1 : val[i]
          );
//      val[i]=val[i]<0 ? (val[i]<-max ? -1f : -val[i]/max) :
//          (val[i]>max ? 1f : val[i]/max);
    }
    return val;
  }

  float bufFloat[];
  
  public float[] norm(int val[]) {
    return norm(toFloat(val, bufFloat));
  }

  public String str() {
    if(!(useMaxLimit || useMinLimit)) {
      return strf("%.1f | %.1f | %.1f" ,
          min,max,maxAbs);
    }
    return strf("%.1f %.1f | %.1f %.1f | %.1f" ,min,minLimit,max,maxLimit,maxAbs);
  }
}
