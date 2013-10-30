package itp;

import java.util.ArrayList;

import processing.core.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import processing.opengl.*;

public class MeshDeform01 extends PApplet {
	UVertexList path,prof;
	UNav3D nav;
	ArrayList<UVertexList> sweep;

	UGeo geo;
	
	public void setup() {
		size(600,600,OPENGL);
		
		UMB.setPApplet(this);
		nav=new UNav3D();
		
		build();
	}
	
	public void build() {
		prof=new UVertexList();
		int n=7;
		for(int i=0; i<n; i++) {
			prof.add(new UVertex(50,0,0).
					rotY(map(i, 0,n-1, 0,TWO_PI)));
			
			// U indicates the rotational order
			prof.last().U=map(i,0,n-1, 0,1);
		}
		prof.close();
		
		n=10;
		sweep=new ArrayList<UVertexList>();
		
		for(int i=0; i<n; i++) {
			sweep.add(prof.copy().
					translate(0,map(i, 0,n-1, 0,400),0));
			
//			for(UVertex vv:sweep.get(sweep.size()-1)) {
//				// V indicates the vertical order
//				vv.V=map(i,0,n-1, 0,1); 
//			}
		}
				
		// create a quadstripped mesh of the profiles 
		geo=new UGeo().quadstrip(sweep);
		
		for(UVertex vv:geo.getV()) {
			vv.y+=UMB.rndSigned(5, 10);
			
			// calculate V as a function the bounds in the Y axis
			vv.V=map(vv.y, geo.bb().min.y,geo.bb().max.y, 0,1);
			vv.mult(0.5f+0.5f*vv.V,1,0.5f+0.5f*vv.V);
			vv.rotX(vv.V*radians(30));
		}
	}
	
	public void draw() {
		background(0);
		lights();
		translate(width/2,height/2);
		nav.doTransforms();
		
		if(mousePressed) {
			UVertexList.draw(sweep);
		}
		else {
			fill(255);
			geo.draw();
		}
	}
	
	public static void main(String[] args) {
		PApplet.main("itp.MeshDeform01");
	}

}
