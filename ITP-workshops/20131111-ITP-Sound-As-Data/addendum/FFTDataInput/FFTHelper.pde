////////////////////////////////////////////////
// Utility class to normalize and dampen FFT
// values to a more useful form.

class FFTHelper {
  public int num,numbands;
  public float spectrum[],ttv[];
  public double v[];
  private double  tv[];
  double dampUp,dampDown,mult;
  boolean doDampen=false,doDampenAsymm=false,doAverage=false,isFirstTime=true,doNorm=false;

  // variables dealing with the calculation of maximum value
  public double maxinternal;
  public float max,maxMinimum,maxMaximum;
  double maxD;
  int maxCnt;

  double bandv[];
  public float band[],bandmax[];
  public int bandsteps;

  public FFTHelper(int _n, int _nbands,boolean doAvg) {
    num=_n;
    numbands=_nbands;
    spectrum=new float[num]; // Visible outside - normalized values
    v=new double[num]; // Visible outside -  - unmodified values
    tv=new double[num]; // Private - temp values
    ttv=new float[num]; // Private - temp values
    doAverage=doAvg;
    doNorm=true;
    mult=1;

    maxMinimum=200;
    maxMaximum=2000;
    maxinternal=maxMinimum;
    maxCnt=0;

    bandsteps=256/numbands;
    band=new float[numbands]; // Visible outside - normalized bands
    bandv=new double[numbands]; // Visible outside - unmodified bands
    bandmax=new float[numbands];
  }

  public void setMaxLimits(float _min,float _max) {
    maxMinimum=_min;
    maxMaximum=_max;
  }

  public void setDamper(double _dup,double _ddown) {
    dampUp=_dup;
    dampDown=_ddown;
    if(dampUp==-1) doDampen=false;
    else doDampen=true;
    doDampenAsymm=true;
  }

  public void setDamper(double _d) {
    setDamper(_d,_d);
    doDampenAsymm=false;
  }

  public float dampenVal(double oldval,double newval) {
    if(doDampenAsymm) {
      if (oldval>newval) oldval=newval*dampDown+oldval* (1-dampDown);
      else oldval=newval*dampUp+oldval* (1-dampUp);
    }
    else oldval=newval*dampUp+oldval* (1-dampUp);
    return (float)oldval;
  }

  public void update(float [] nv) {
    if(isFirstTime) isFirstTime=false;

    if(doAverage) {
      tv[0]=(nv[0]+nv[1])*0.5;
      tv[num-1]=(nv[num-1]+nv[num-2])*0.5;
      for(int i=1; i<num-1; i++) tv[i]=(nv[i-1]+nv[i]+nv[i+1])*0.333;
    }
    else for(int i=0; i<num; i++) tv[i]=nv[i];

    // dampen values if requested
    if(doDampen && !isFirstTime) {
      if(doDampenAsymm)
        for (int i=0; i<num; i++) {
          if(v[i]>tv[i]) v[i]= tv[i]*dampDown+v[i]*(1-dampDown);
          else v[i]= tv[i]*dampUp+v[i]*(1-dampUp);
        }
      else {
        for (int i=0; i<num; i++) {
          v[i]=tv[i]*dampUp+v[i]* (1-dampUp);
          if(v[i]>maxMaximum) v[i]=maxMaximum;
        }
      }
    } else // just copy values
        System.arraycopy(tv,0, v,0, num);

    if(doNorm) {
      double currmax=0;
      double avg=0;
      if (maxCnt>0) {
        maxinternal+=maxD;
        maxCnt--;
      }
      for (int i=0; i<num; i++) {
        if (v[i]>maxinternal) {
          maxinternal=v[i];

          maxD= (v[i]-maxinternal)/4f;
          maxCnt=4;

          maxinternal+=maxD;
          maxCnt--;

        }
        avg+=v[i];
        if(v[i]>currmax) currmax=v[i];
      }
      if(currmax>maxMaximum) currmax=maxMaximum;
      if(maxinternal>maxMinimum && maxCnt==0) maxinternal=currmax*0.005+maxinternal*(1-0.005);
      else if(maxinternal>maxMaximum) maxinternal=maxMaximum;

      avg/=(double)num;
//      System.out.println("max "+max+" currmax "+currmax+" avg "+avg);
      for (int i=0; i<num; i++) {
        spectrum[i]=(float)(v[i]/maxinternal);
        if(spectrum[i]>1) spectrum[i]=1;
      }
      max=(float)maxinternal;


    }
    else for(int i=0; i<num; i++) spectrum[i]=(float)v[i];

    int fftindex=0;
    double oldval;
    for(int i=0; i<numbands; i++) {
      oldval=bandv[i];
      bandv[i]=0;
      for(int j=0; j<bandsteps; j++) bandv[i]+=spectrum[fftindex++];
      bandv[i]/=(float)bandsteps;
      bandv[i]=dampenVal(oldval,bandv[i]);
      if(bandmax[i]<bandv[i]) bandmax[i]=(float)bandv[i];
      else bandmax[i]*=0.995;

      band[i]=(float)bandv[i];///(float)bandsteps;
    }
  }

  public void update(float [] nv,int start,int length) {
    System.arraycopy(nv,start, ttv,0, length);
    update(ttv);
  }

  public void updateBands(float [] nv,int div) {
    int n2=nv.length/div;
    float val=0;
    for(int i=0; i<n2; i++) {
      val=0;
      for(int j=0; j<n2; j++) val+=nv[i*n2+j];
      ttv[i]=val/(float)n2;
    }
    update(ttv);
  }

  public double checkBandMax(int valid) {
    return bandmax[valid/bandsteps];
  }

}
