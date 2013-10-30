package itp;

import processing.core.PApplet;

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;

public class CirclePacking extends PApplet {

	// we'll store radius as the Z position
	UVertexList pt;
	
	public void setup() {
		size(600,600);
		UMB.setPApplet(this);
		
		pt=new UVertexList();		
	}
	
	public void draw() {
		background(0);
		
		place();
		
		for(UVertex vv:pt) {
			ellipse(vv.x,vv.y, vv.z,vv.z);
		}
	}
	
	public void place() {
		float rad=50;
		
		if(pt.size()<1) { // this is the first point
			pt.add(new UVertex(width/2,height/2,rad));
		}
		else {
			int tries=500;
			UVertex v=null;
			
			// as long as v is null and we haven't run out of tries
			while(v==null && tries>0) { 
				if(pt.size()<20) rad=random(80,120);
				else rad=random(5,20);
				v=new UVertex(random(width),random(height),rad);
				
				for(UVertex v2:pt) if(v!=null) {
					float d=dist(v.x,v.y, v2.x,v2.y);
					
					// check to see v2 is too close to v
					if(d<(v.z+v2.z)/2) {
						// it's too close
						v=null;
					}
				}
				
				tries--;
			}
			
			if(v!=null) { // success! add to list
				pt.add(v);
			}
		}
	}

	public void placeExponential() {
		float rad=50;
		
		if(pt.size()<1) { // this is the first point
			pt.add(new UVertex(width/2,height/2,rad));
		}
		else {
			int tries=500;
			UVertex v=null;
			
			// as long as v is null and we haven't run out of tries
			while(v==null && tries>0) { 
				if(pt.size()<20) rad=random(80,120);
				else rad=random(5,20);
				v=new UVertex(random(width),random(height),rad);
				
				for(UVertex v2:pt) if(v!=null) {
					float d=dist(v.x,v.y, v2.x,v2.y);
					
					// check to see v2 is too close to v
					if(d<(v.z+v2.z)/2) {
						// it's too close
						v=null;
					}
				}
				
				tries--;
			}
			
			if(v!=null) { // success! add to list
				pt.add(v);
			}
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] {"itp.CirclePacking"});

	}

}
