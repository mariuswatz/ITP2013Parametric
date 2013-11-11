ArrayList<Integer> colors,colgrad;

void initColors() {
  colors=new ArrayList<Integer>(); 
  
  colgrad=new ArrayList<Integer>(); 
  colgrad.add(0x00e8e6);
  colgrad.add(0x0b508b);
  colgrad.add(0xffff00);
  colgrad.add(0xff6600);
  colgrad.add(color(0xffffff));
  colgrad.add(color(0xeafffe));

  int last=-1,cnt=0;
  for(int c:colgrad) {
    if(cnt%2==1) {
      int n=(int)random(4,11);
      for(int i=0; i<n; i++) {
        float t=map(i,0,n-1,0,1);
        int col=lerpColor(last,c,t);
        colors.add(0xff000000|col);
      }
      
          println(UMB.hex(last)+" "+UMB.hex(c ));

    }
    last=c;
    cnt++;
  }

    for(int cc:colors) if(cc!=0) {
    UMB.log(UMB.hex(cc)+" "+alpha(cc));
  }  
//
//  for(int cc:colors) {
//    UMB.log(UMB.hex(cc));
//  }  
}

int randomColor() {
  return colors.get((int)random(colors.size()));
}
