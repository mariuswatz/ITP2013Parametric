
void savePDF() {
  PGraphicsPDF pdf=(PGraphicsPDF)createGraphics(width, height, PDF, "test.pdf");
  UMB.setGraphics(pdf);

  println("Save PDF - "+models.size());
  pdf.beginDraw();

  pdf.translate(width/2, height/2);
  pdf.noFill();
  pdf.stroke(0);
  UGeo.drawModels(models);

  pdf.endDraw();
  pdf.flush();
  pdf.dispose();

  println("Done - "+models.size());

  // set the PGraphics back to this sketch
  UMB.setGraphics(g);
}

