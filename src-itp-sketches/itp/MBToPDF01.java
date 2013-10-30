package itp;

import com.lowagie.text.pdf.PdfGraphics2D;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.pdf.PGraphicsPDF;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;

public class MBToPDF01 extends PApplet {
  UVertexList vl;

  UNav3D nav;

  public void setup() {
    size(600, 600, OPENGL);

    // set PApplet reference
    UMB.setPApplet(this);
    
    // sketchPath() defaults to the folder your sketch launches 
    // in, which means that in Eclipse it might be the wrong
    // path. set sketchPath manually to fix this
    sketchPath="C://Users//marius//Dropbox//40 Teaching//2013 ITP//ITP-Parametric - Resources//Code//ITP-Parametric-sketches//eclipse";
    

    // create navigation tool
    nav=new UNav3D();

    build();
    exportPDF();        
  }

  void build() {
    vl=new UVertexList();
    UVertex v=new UVertex(100, 0);

    int n=UBase.rndInt(10, 50);

    for (int i=0; i<n; i++) {
      vl.add(v.copy().rotZ(map(i, 0, n, 0, TWO_PI)).mult(vl.rnd(1, 2))
          .add(0, 0, random(-200, 200)));

      if (i>0) {
        // set Z of vertex to 80% of last Z value (if any)
        // and 20% of the new random value
        vl.get(i).z=(vl.get(i).z*0.3f+vl.get(i-1).z*0.7f);
      }
    }

    vl.close();

    // center the vertex list around origin
    vl.center();

    // scale the vertex list so that its width is 500
    vl.scale(300f/vl.bb().dimX());

    vl.log("Centroid: "+vl.centroid().str());
    vl.log("UBB: "+vl.bb().str());
    vl.log("Vertices: "+vl.bb.str());

  }
  
  
  // demonstrates how to export to PDF file
  // using PGraphicsPDF in combination with
  // UMB.setGraphics().
  void exportPDF() {
    // create a PGraphicsPDF instance that can be drawn to
    // directly, except the output goes to a specified file
    
    String filename=sketchPath("test.pdf");
    PGraphicsPDF pdf=
        (PGraphicsPDF)createGraphics(1000, 1000, PDF, filename);
    
    // PGraphics.beginDraw() must be called before drawing 
    pdf.beginDraw();

    UMB.setGraphics(pdf);
    pdf.translate(pdf.width/2,pdf.height/2);
    vl.draw();
    
    // finish drawing this "frame", flush and close the file 
    pdf.endDraw();
    pdf.flush();
    pdf.dispose();
    
    println("exportPDF done.");
  }


  public void draw() {
    background(0);
    drawCredit();

    translate(width/2, height/2);

    // execute navigation transforms
    nav.doTransforms();

    vl.draw();
  }

  public void keyPressed() {
    // build if any non-special key is pressed
    if (key!=CODED) build();
  }

  void drawCredit() {
    fill(255);
    textAlign(RIGHT);
    text(UBase.version(), width-5, 15);
    textAlign(LEFT);
    text(this.getClass().getSimpleName(), 5, 15);
  }


  static public void main(String args[]) {
    PApplet.main(new String[] {"itp.MBToPDF01"});
  }

}
