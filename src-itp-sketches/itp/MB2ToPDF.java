package itp;

import processing.core.PApplet;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import processing.pdf.*;

public class MB2ToPDF extends PApplet {
	
	private UVertexList vl;

	public void setup() {
		size(600,600);
		
		build();
		export();
		UMB.setPApplet(this);
	}
	
	public void draw() {
		background(0);
		
		fill(255);
		stroke(255,0,0);
		translate(width/2,height/2);
		
		vl.draw();
	}
	
	public void build() {
		vl=new UVertexList();
		int n=60;
		for(int i=0; i<n; i++) {
			vl.add(new UVertex(random(100,150),0).
					rotZ(map(i, 0,n, 0,TWO_PI)));
		}
	}
	
	public void export() {
		String filename=sketchPath("test.pdf");
		
		// create PGraphicsPDF canvas
		PGraphicsPDF pdf=(PGraphicsPDF) createGraphics(500, 500, PDF, filename);
		
		// get pdf ready to draw
		pdf.beginDraw();
		pdf.translate(pdf.width/2,pdf.height/2);
		
		// tell Modelbuilder to draw to our PDF
		UMB.setGraphics(pdf);
		vl.draw();
		
		// end draw, close and flush the PDF file
		pdf.endDraw();
		pdf.flush();
		pdf.dispose();
	}
	
	public static void main(String[] args) {
		PApplet.main("itp.MB2ToPDF");

	}

}
