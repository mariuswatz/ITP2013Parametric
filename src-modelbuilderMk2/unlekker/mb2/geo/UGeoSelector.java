/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Iterator;

import unlekker.mb2.util.UMB;

import java.util.*;

import processing.core.PImage;

/**
 * <p>Methods to select faces in UGeo, from single faces ({@link #add(UFace)}
 *  to single faces plus their adjacent faces ({@link #addConnected(UFace)}.
 *  Use ({@link #addConnected()} to expand the selection to include all 
 *  faces adjacent to currently selected faces.</p>  
 *      
 * To draw the selection use {@link #draw()}, it will draw selected faces
 * in red and unselected faces 
 * 
 * @author Marius Watz
 *
 */
public class UGeoSelector extends UMB implements Iterable<UFace> {
  public UGeo parent;
  public ArrayList<UFace> faces;
  public UEdgeList edges;
  
  public UGeoSelector(UGeo model) {
    parent=model;
    faces=new ArrayList<UFace>();
    edges=parent.getEdgeList();
  }

  public boolean contains(UFace f) {
    return !(faces.indexOf(f)<0);
  }

  public UGeoSelector clear() {
    faces.clear();
    edges=null;
    return this;
  }
  /**
   * Iterates through selection and adds all adjacent faces. 
   * @return
   */
  public UGeoSelector addConnected() {
    edges=parent.getEdgeList();
    ArrayList<UFace> fc=new ArrayList<UFace>();
    for(UFace ff:faces) fc.add(ff);
    
    for(UFace cf:fc) addConnected(cf);
    
    return this;
  }

  /**
   * Adds the adjacent faces of the provided {@link UFace}. If the
   * input face is not found in the selection it is added.
   * @param f
   * @return
   */
  public UGeoSelector addConnected(UFace f) {
    edges=parent.getEdgeList();
    add(f);
    UFace[] c=f.connected();
    for(UFace cf:c) add(cf);
    
    return this;
  }
  
  public UGeoSelector add(UFace f) {    
    if(f!=null && !contains(f)) faces.add(f);
    return this;
  }

  /**
   * Add all faces from a {@link UGeoGroup}.
   * @param group
   * @return
   */
  public UGeoSelector add(UGeoGroup group) {
    for(UFace f:group) add(f);
    return this;
  }
  
  public UGeoSelector remove(UFace ff) {
    if(contains(ff)) faces.remove(ff);
    return this;
  }

  public int size() {
    return faces.size();
  }
  
  public Iterator<UFace> iterator() {
    return faces.iterator();
  }

  /**
   * Get a random face from the parent UGeo.
   * @return
   */
  public UFace getRndF() {
    int n=parent.sizeF();
    return parent.getF(rndInt(n));
  }
  
  public ArrayList<UFace> getF() {
    return faces;
  }

  /**
   * Draws the selected faces in red, the remaining faces from the
   * parent UGeo are draw according to the current style. Calls 
   * {@link #checkSelection()}} before drawing to make sure the
   * selection is valid.
   * 
   * @return
   */
  public UGeoSelector draw() {
    checkSelection();
    
    ppush().pfill(color(255,0,0));
    for(UFace ff:faces) {
      ff.draw();
    }
    ppop();
    
    for(UFace ff:parent.getF()) {
      if(!contains(ff)) ff.draw();
    }
    
    return this;
  }

  /**
   * Checks validity of current selection. Any faces in the selection
   * that can't be found in the parent UGeo will be removed.
   * @return
   */
  public UGeoSelector checkSelection() {
    for(int i=0; i<size(); i++) {
      UFace fc=faces.get(i);
      if(!parent.contains(fc)) remove(fc);
    }
    return this;
  }
  
}
