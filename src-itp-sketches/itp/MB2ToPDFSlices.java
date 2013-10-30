package itp;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import processing.pdf.*;

public class MB2ToPDFSlices extends PApplet {
	
	private UVertexList vl,vl2;
	ArrayList<UVertexList> slices;
	UNav3D nav;
	
	// define max RADIUS as a value in millimeters
	float maxRad=50f*UMB.PTMM;
	

	
	public void setup() {
		size(600,600,OPENGL);
		
		build();
		export();
		UMB.setPApplet(this);
		
		// create UNav3D after setPApplet
		nav=new UNav3D();
	}
	
	public void draw() {
		background(0);
		
		noFill();
		stroke(255,0,0);
		translate(width/2,height/2);
		
		nav.doTransforms();
		
		for(UVertexList l:slices) {
			l.draw();
		}
		
	}
	
	public void build() {
		vl=new UVertexList();
		vl2=new UVertexList();
		
		int n=60;
		for(int i=0; i<n; i++) {
			vl.add(new UVertex(random(0.5f,1)*maxRad,0).
					rotZ(map(i, 0,n, 0,TWO_PI)));
			vl2.add(new UVertex(random(0.5f,1)*maxRad*0.33f,0).
					rotZ(map(i, 0,n, 0,TWO_PI)));
		}
		
		slices=new ArrayList<UVertexList>();
		n=100;
		
		// assumed height of our material
		float materialH=3*UMB.PTMM;
		
		for(int i=0; i<n; i++) {
			float t=map(i, 0,n-1, 0,1);
			slices.add(UVertexList.lerp(t, vl, vl2).
					close().
					translate(0,0,(float)i*materialH));
		}
	}
	
	public void export() {
		String filename=sketchPath("test.pdf");
		
		// create PGraphicsPDF canvas
		int sliceRowN=(int) ceil(sqrt(slices.size()));
		int pageSize=(int) ((float)sliceRowN*maxRad)*2;
		PGraphicsPDF pdf=(PGraphicsPDF)
				createGraphics(pageSize, pageSize, PDF, filename);
		
		// get pdf ready to draw
		pdf.beginDraw();
		pdf.noFill();
		
		// tell Modelbuilder to draw to our PDF
		UMB.setGraphics(pdf);

		int index=0;
		float x,y;
		for(UVertexList l:slices) {
			x=(index%sliceRowN);
			y=(index/sliceRowN);
			
			pdf.pushMatrix();
			pdf.translate(x*maxRad*2+maxRad,y*maxRad*2+maxRad);
			l.draw();
			pdf.ellipse(0,0, 2*UMB.PTMM,2*UMB.PTMM);
			pdf.popMatrix();
			
			index++;
		}
		
		
		// end draw, close and flush the PDF file
		pdf.endDraw();
		pdf.flush();
		pdf.dispose();
	}
	
	public static void main(String[] args) {
		PApplet.main("itp.MB2ToPDFSlices");

	}

}
