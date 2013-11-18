void subdivide() {
    UGeoGroup gr=geo.getGroup(0);
//  for(UFace ff:geo.getFNoGroup()) gr.add(ff);
  gr.subdivide(UMB.SUBDIVMIDEDGES,true);

  for(UFace f : geo.getF()) f.setColor(randomColor());
  
  // assign colors to vertices
  for(UVertex v : geo.getV()) v.setColor(randomColor());
}
