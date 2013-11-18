
void build() {  

    int n=(int)random(5,10);
    stack=UVertexList.grid2D(n,n, 400,400);
    println("stack "+stack.get(0).size()+" "+stack.size());
    
    n=(int)random(5,(n-2)*(n-2));
    for(int i=0; i<n; i++) {
      int x=(int)random(1,stack.get(0).size()-1);
      int y=(int)random(1,stack.size()-1);
      stack.get(y).get(x).z=random(-50,300);
    }
    
    ArrayList<UVertexList> s=new ArrayList<UVertexList>();
    bez=new UBezierPatch(s);
    geo=bez.eval(20, 20);
    
 }
