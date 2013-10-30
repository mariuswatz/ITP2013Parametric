package itp;

import java.util.ArrayList;

import processing.core.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import processing.opengl.*;

public class PathTracing02 extends PApplet {
	UVertexList path,prof;
	UNav3D nav;
	ArrayList<UVertexList> sweep;

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
			prof.add(new UVertex(50,0,0).
					rotZ(map(i, 0,n, 0,TWO_PI)));
		}
		prof.close();
		
		path=new UVertexList();
		n=30;
		
		// create a smoothly "bending" path
		for(int i=0; i<n; i++) {
			float t=map(i, 0,n-1, 0,1);
			path.add(new UVertex(i*20,0,0));
			
			// "bend" the path by rotating incrementally
			path.last().rotZ(t*HALF_PI).rotY(t*PI/3);
			
//			if(i>20) path.last().rotY(radians(30));
		}
		
		// UHeading.sweep() produces an arraylist of profiles
		// oriented along the path
		sweep=UHeading.sweep(path, prof);

		n=sweep.size();
		int cnt=0;
		for(UVertexList l : sweep) {
			l.scaleInPlace(map(cnt++, 0,n-1, 1.5f, 0.25f));
		}
		
		// create a quadstripped mesh of the profiles 
		geo=new UGeo().quadstrip(sweep);
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

		stroke(255);
		noFill();
		path.draw();
		
		if(mousePressed) {
			UVertexList.draw(sweep);
		}
		else {
			fill(255);
			geo.draw();
		}
	}
	
	public static void main(String[] args) {
		PApplet.main("itp.PathTracing02");
	}

}
