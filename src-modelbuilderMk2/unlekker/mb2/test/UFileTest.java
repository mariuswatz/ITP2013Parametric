package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UBase;
import unlekker.mb2.util.UFile;

public class UFileTest extends PApplet {
  String path;
  
  public void setup() {
    size(600,600, OPENGL);

    path="C:\\Users\\marius\\Dropbox\\03 Code\\Novotel\\data";
    println(path);
    
//    UBasic.log(UBasic.str(UFile.list(path)));
    UBase.logDivider(path);
    UBase.log(UBase.str(UFile.list(path,"NovoShards")));
    UBase.logDivider(path);
    UBase.log(UBase.str(UFile.listByExtension(path, "gz")));
    
    UBase.logDivider(path);
    UBase.log(UFile.last(path, "NovoFinal04A-G","gz"));
    UBase.log(UFile.noExt(UFile.last(path, "NovoFinal04A-G","gz")));
    UBase.log(UFile.lastIndex(path, "NovoFinal04A-G"));
    UBase.log(UFile.nextFilename(path, "NovoFinal04A-G"));
  }

  public void draw() {
    background(0);

  }

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "unlekker.mb2.test.UGeoTest" });
  }

  
}
