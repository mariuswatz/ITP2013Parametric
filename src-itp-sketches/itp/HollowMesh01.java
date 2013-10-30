package itp;

import java.util.ArrayList;

import processing.core.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import processing.opengl.*;

public class HollowMesh01 extends PApplet {
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
		prof.add(10,0,0);
		prof.add(100,0,0);
		prof.add(100,200,0);
		prof.add(80,200,0);
		prof.add(80,20,0);
		prof.add(10,20,0);
		
		
		int n=30;
		sweep=new ArrayList<UVertexList>();
		
		for(int i=0; i<n; i++) {
			sweep.add(prof.copy().
					rotY(map(i,0,n, 0,TWO_PI)));
		}
		
		UVertexList vlFirst=new UVertexList();
		for(UVertexList l:sweep) vlFirst.add(l.first());
		
		UVertexList vlLast=new UVertexList();
		for(UVertexList l:sweep) vlLast.add(l.last());
				
		// create a quadstripped mesh of the profiles 
		geo=new UGeo().quadstrip(sweep);
		geo.quadstrip(sweep.get(sweep.size()-1),sweep.get(0));
		
		geo.triangleFan(vlFirst.close());
		geo.triangleFan(vlLast.close().reverse());
		
		
		geo.center();
		geo.writeSTL(sketchPath("Hollow.stl"));
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
		PApplet.main("itp.HollowMesh01");
	}

	
	
	
	
}
