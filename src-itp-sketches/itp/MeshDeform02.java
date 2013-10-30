package itp;

import java.util.ArrayList;

import processing.core.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import processing.opengl.*;

public class MeshDeform02 extends PApplet {
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
		int n=36;
		for(int i=0; i<n; i++) {
			prof.add(new UVertex(50,0,0).
					rotY(map(i, 0,n-1, 0,TWO_PI)));
			
			// U indicates the rotational order
			prof.last().U=map(i,0,n-1, 0,1);
		}
		prof.close();
		
		n=30;
		sweep=new ArrayList<UVertexList>();
		
		for(int i=0; i<n; i++) {
			sweep.add(prof.copy().
					translate(0,map(i, 0,n-1, 0,400),0));
		}
				
		// create a quadstripped mesh of the profiles 
		geo=new UGeo().quadstrip(sweep);
		geo.center();
	}
	
	public void draw() {
		background(0);
		lights();
		translate(width/2,height/2);
		nav.doTransforms();
		
			fill(255);
			geo.draw();
	}
	
	public void keyPressed() {
		if(key==' ') {
			geo.bb(true);
			
			UVertex rv=new UVertex(
					random(geo.bb().min.x,geo.bb().max.x),
					random(geo.bb().min.y,geo.bb().max.y),
					random(geo.bb().min.z,geo.bb().max.z)
					);
			Deformer def=new Deformer(rv);
			def.deform(geo);
		}
	}
	
	
	class Deformer {
		UVertex loc;
		float rad,mult;
		
		Deformer(UVertex input) {
			loc=input;
			if(random(100)<80) rad=random(50,100);
			else rad=random(200,300);
			mult=random(0.4f,1);
		}
		
		void deform(UGeo model) {
			for(UVertex vv:model.getV()) {
				float d=loc.dist(vv);
				if(d<rad) {
					d=d/rad; // gives us a normalized value indicating distance
					float force=sq(1-d)*mult;
					
					// get the delta vector betw. loc and vv
					UVertex delta=vv.copy().sub(loc);
					
					// add the delta vector scaled by force
					vv.add(delta.mult(force));
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		PApplet.main("itp.MeshDeform02");
	}

	
	
	
	
}
