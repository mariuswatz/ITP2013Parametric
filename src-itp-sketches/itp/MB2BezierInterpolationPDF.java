package itp;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import processing.pdf.*;

/*
 * Slightly more advanced version of MB2ToPDFSlices, adding
 * incremental file export, a scaling factor shaping the geometry,
 * mesh geometry preview and rebuilding on key press.
 */
public class MB2BezierInterpolationPDF extends PApplet {
	
	private UVertexList vl,vl2;
	ArrayList<UVertexList> slices;
	UNav3D nav;
	UGeo geo;
	
	// define max RADIUS (in millimeters)
	float maxRad=50f;
	
  // assumed height of our material
  float materialH=3;
	
	public void setup() {
		size(600,600,OPENGL);
		
		build();
    UMB.setPApplet(this);
		
		// create UNav3D after setPApplet
		nav=new UNav3D();
	}
	
	public void draw() {
		background(0);
		drawCredit();
		
		translate(width/2,height/2);
		lights();
		
		nav.doTransforms();
    // center the geometry
//    translate(0,0,-geo.centroid().z);
    
		
		// draw the mesh
    UMB.pnoStroke().pfill(color(255,255,0));
//		geo.draw();
		
		
    // draw the slices
		UMB.pstroke(color(255,0,0)).pnoFill();
//    vl.draw();
    vl2.draw();
		for(int i=0; i<vl.size(); i+=3) {
      if(i>0) vl.pline(vl.get(i), vl.get(i-1));
      if(i<vl.size()-1) vl.pline(vl.get(i), vl.get(i+1));
		}
		
//		for(UVertexList l:slices) {
//			l.draw();
//		}		
	}
	
	public void build() {
	  vl=buildBezier();
	  vl2=calcBezier(vl, 100).scale(2);
    slices=new ArrayList<UVertexList>();
		
		/*
		
		int n=60;
		for(int i=0; i<n; i++) {
			vl.add(new UVertex(random(0.5f,1)*maxRad,0).
					rotZ(map(i, 0,n, 0,TWO_PI)));
			vl2.add(new UVertex(random(0.5f,1)*maxRad*0.66f,0).
					rotZ(map(i, 0,n, 0,TWO_PI)));
		}
		
		n=50;
		
		float rot= 0;
		
		for(int i=0; i<n; i++) {
			float t=map(i, 0,n-1, 0,1);

			// apply a shaping function to t
			t=bezierPoint(0,0.5f,0.5f,1,t);
			
      // use a shaping function to calculate a scaling factor
			float sc=bezierPoint(0.4f,1,0.8f,0.1f,t);
			
			// create temp vertex list using VertexList.lerp()
			UVertexList tmp=UVertexList.lerp(t, vl, vl2).scale(sc).close();

			// rotate this slice by a small random factor, interpolated
			// with 80% of the previous value of rot
			rot=rot*0.8f+0.2f*random(-0.75f,0.75f)*(TWO_PI/(float)n);
			tmp.rotZ(rot);
			
			// add the interpolated slice, stacking along Z to simulate
			// the 3D stack.
			slices.add(tmp.translate(0,0,(float)i*materialH));
		}
		
		// build a mesh preview of the form
		geo=new UGeo().quadstrip(slices);*/
		
		// do exporting
//		export();
	}
	
	public UVertexList calcBezier(UVertexList input,int n) {
	  UVertexList res=new UVertexList();
	  
	  for(int i=0; i<n; i++) {
	    float t=map(i,0,n-1,0,1);
	    res.add(evalPoint(input, t));
	  }
	  
	  return res;
	}

	public UVertex evalPoint(UVertexList input,float _t) {
    float ct,D,ctsq,tsq,t,t0,t1,t2,t3;
    UVertex v1,v2,v3,v4,vv=new UVertex();
    
    int numSeg=(input.size()-1)/3;
    
    t=_t*(float)numSeg;
    int seg=(int)t;
    if(seg==numSeg) seg--;
    t-=seg;

    
    ct=1f-t;
    ctsq=ct*ct;
    tsq=t*t;

    t0=ctsq*ct;
    t1=3*ctsq*t;
    t2=3*ct*tsq;
    t3=t*tsq;

    int id=seg*3;
    v1=input.get(id++);
    v2=input.get(id++);
    v3=input.get(id++);
    v4=input.get(id++);

//    UUtil.log(UUtil.nf(_t)+" "+seg+" "+UUtil.nf(t)+" "+id);

    vv.set(
         v1.x*t0 + v2.x*t1 + v3.x*t2 + v4.x*t3,
         v1.y*t0 + v2.y*t1 + v3.y*t2 + v4.y*t3,
         v1.z*t0 + v2.z*t1 + v3.z*t2 + v4.z*t3);
    
    return vv;
  }
	private UVertexList buildBezier() {
    UVertex pt,cp0,cp1;
	  pt=new UVertex(100,0);
    cp0=new UVertex(0,-25f);
    cp1=new UVertex(0,25f);
	  
    UVertexList tmp=new UVertexList();
    
    int n=9;
    for(int i=0; i<n; i++) {
      float deg=map(i,0,n-1,0,TWO_PI);
      UVertex thePt=pt.copy().rot(deg).mult(random(1,1.5f));
      if(i>0) tmp.add(cp0.copy().rot(deg).add(thePt));
      tmp.add(thePt);
      if(i<n-1) tmp.add(cp1.copy().rot(deg).add(thePt));
    }
    println(tmp.str());
    return tmp;
  }
	
	public void export() {
	  // export our slices using PGraphicsPDF, using UMB.setGraphics()
	  // to tell ModelbuilderMk2 to draw to the PDF directly

	  // get incremental filename using the name of this class as a prefix
		String filename=
		    UMB.nextFilename(sketchPath, this.getClass().getSimpleName(), "pdf");
		
		println("Exporting to "+filename);
		
		// we plan to arrange our slices in a XY grid, so we need to find
		// a grid size that will suffice. the square root of the number of 
		// slices, rounded upwards, should work.
		int sliceRowN=(int) ceil(sqrt(slices.size()));
		
		// Units in a PDF are given as typographic points (pt), for explanation see:
		// http://en.wikipedia.org/wiki/Point_(typography)
		// We want our Processing units to equal millimeters, so we need to scale
		// accordingly. 72 pts == 1 inch, so 1 mm == 72/25.4.
		// For convenience, UMB provides a UMB.PTMM constant (72/25.4)
		
		// the actual page size (in typographic points)
		int pageSize=(int) ((float)sliceRowN*maxRad*UMB.PTMM)*2;
		
		// create a PGraphicsPDF canvas to draw to
		PGraphicsPDF pdf=(PGraphicsPDF)
				createGraphics(pageSize, pageSize, PDF, filename);

    // tell ModelbuilderMk2 to draw to our PDF. since PGraphicsPDF is
	  // a 2D renderer, the Z-values of vertices are simply ignored 
    UMB.setGraphics(pdf);

		// get pdf ready to draw
		pdf.beginDraw();		
		
		// scale the canvas by UMB.PTMM so that one unit ==  1 mm. this allows 
		// us to use our measurements as-is.
		pdf.scale(UMB.PTMM);
		pdf.noFill();
		
		// iterate through the slice array list, calculating x and y
		// positions using an index counter along with sliceRowN
		int index=0;
		float x,y;
		for(UVertexList l:slices) {
			x=(index%sliceRowN);
			y=(index/sliceRowN);
			
			// scale by the max diameter, offset by maxRad to account for the
			// initial edge positions
      x=x*maxRad*2+maxRad;
      y=y*maxRad*2+maxRad;
			
			pdf.pushMatrix();
			pdf.translate(x*maxRad*2+maxRad,y*maxRad*2+maxRad);
			
			// draw this slice to pdf
			l.draw();
			

			// draw a circular hole that can be used for assembly 
			pdf.ellipse(0,0, 2,2);
			pdf.popMatrix();
			
			index++;
		}
		
		
		// end draw, close and flush the PDF file
		pdf.endDraw();
		pdf.flush();
		pdf.dispose();
		
    println("Done exporting.");

    // since we called UMB.setGraphics() to draw to PDF, UMB.setPApplet()
    // will tell it to revert to using the graphics engine of this sketch 
    UMB.setPApplet(this);
	}
	
	
	public void keyPressed() {
	  // build if any non-special key is pressed
	  if(key!=CODED) build();
	}

	void drawCredit() {
	  if(frameCount<2)   textFont(createFont("courier", 11, false));

	  fill(255);
	  textAlign(RIGHT);
	  text(UBase.version(), width-5, 15);
	  textAlign(LEFT);
    text(this.getClass().getSimpleName(), 5, 15);
    text("Slices: "+slices.size()+
        " Diam: "+nf(maxRad*2,1,2)+
        " H="+nf(materialH*slices.size(),1,2), 5, height-5);
	}
	
	public static void main(String[] args) {
		PApplet.main("itp.MB2BezierInterpolationPDF");

	}

}
