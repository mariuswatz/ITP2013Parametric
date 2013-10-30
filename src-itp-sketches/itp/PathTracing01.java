package itp;

import processing.core.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import processing.opengl.*;

public class PathTracing01 extends PApplet {
	UVertexList path,prof;
	UNav3D nav;
	
	UHeading head;
	UGeo headbox;
	
	UGeo geo;
	
	public void setup() {
		size(600,600,OPENGL);
		
		UMB.setPApplet(this);
		nav=new UNav3D();
		
		build();
	}
	
	public void build() {
		prof=new UVertexList();
		int n=6;
		for(int i=0; i<n; i++) {
			prof.add(new UVertex(150,0,0).
					rotZ(map(i, 0,n, 0,TWO_PI)));
		}
		
		// make a triangle fan mesh of the profile
		geo=new UGeo().triangleFan(prof.close());
		
		headbox=UGeo.box(100, 100, 5);
		head=new UHeading(new UVertex(0,0,1).
				rotY(random(PI)).rotX(random(PI)));
		
	}
	
	public void draw() {
		background(0);
		lights();
		translate(width/2,height/2);
		nav.doTransforms();
		
		// draw the X/Y/Z axis
		stroke(255,0,0);
		line(0,0,0, 400,0,0);
		stroke(0,255,0);
		line(0,0,0, 0,400,0);
		stroke(255,255,0);
		line(0,0,0, 0,0,400);
		
		fill(255);
		geo.draw();
		stroke(0,255,255);
		// draw the normals for the mesh faces
		geo.drawNormals(100);
		
		line(0,0,0, head.dir.x*100,head.dir.y*100,head.dir.z*100);
		noStroke();
		headbox.draw();
	}
	
	public void keyPressed() {
		head.align(headbox);
	}
	
	public static void main(String[] args) {
		PApplet.main("itp.PathTracing01");
	}

}
