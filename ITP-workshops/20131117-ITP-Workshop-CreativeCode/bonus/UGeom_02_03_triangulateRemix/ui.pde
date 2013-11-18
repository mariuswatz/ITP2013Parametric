ArrayList<String> help;
float helpW,helpH;

void drawCredit() {
  noLights();
  if (frameCount<5)   textFont(createFont("arial", 11, false));
  
  if (help==null) {
    help=new ArrayList<String>();
    help.add("SPACE = rebuild");
    help.add("TAB = drawing style");
    help.add("'D' = subdivide faces");
    help.add("'S' = save PNG");
    help.add("'C' = change color");
    help.add("'L' = random lights");
    
    for(String s:help) helpW=max(helpW,textWidth(s));
    helpH=12*help.size()+10;
    helpW+=10;
  }

  fill(255);
  noStroke();
  hint(DISABLE_DEPTH_TEST);
  
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);

  fill(100, 200);
  float y=height-helpH;
  rect(width-helpW,y-11, helpW,helpH);
  y+=5;
  
  fill(255);
  for (String str:help) {
    text(str, width-5, y);
    y=y+12;
  }

  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
  
    hint(ENABLE_DEPTH_TEST);

}
