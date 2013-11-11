// UGLY HACK TO MAKE CONTROLP5 WORK WITH OPENGL

float damper=0.1, volume=1;
boolean doEnvelope=true, drawColor=true;
ControlP5 cp5;

void initGUI() {
    cp5 = new ControlP5(this);
  cp5.setAutoDraw(false );
    cp5.addSlider("damper").
      setValue(damper).
      setRange(0.001, 1).
      linebreak();

    cp5.addSlider("volume").
      setValue(volume).
      setRange(0, 20).
      linebreak();

    cp5.addToggle("drawColor", drawColor);
    cp5.addToggle("doEnvelope", doEnvelope);
}
