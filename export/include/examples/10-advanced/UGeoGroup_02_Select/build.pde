
void build() {  
  geo=new UGeo();

  geo.add(
  UGeoGenerator.meshBox(200, 300, 200, 6).
    rotZ(radians(45)).rotX(radians(45))).center();

  sel=new UGeoGroup(geo);
}
