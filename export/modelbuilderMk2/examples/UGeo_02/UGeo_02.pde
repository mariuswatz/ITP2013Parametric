import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UGeo geo;

public void setup() {
  size(600, 600, OPENGL);

  // pass this PApplet instance to UBase for easy drawing
  UBase.setGraphics(this);

  build();
}

public void draw() {
  background(0);

  translate(width/2, height/2);
  lights();

  float ry=map(width/2-mouseX, -width/2, width/2, PI, -PI);
  float rx=map(height/2-mouseY, -height/2, height/2, PI, -PI);

  rotateY(ry+radians(frameCount));
  rotateX(rx);


  stroke(255);
  fill(255);

  geo.draw();
}

