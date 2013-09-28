/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import unlekker.mb2.util.UMbMk2;
import unlekker.mb2.util.UFile;


public class UGeoIO {
  private static ByteBuffer buf= null;
  private static FileOutputStream out;
  
  public static boolean writeSTL(String filename,UGeo model) {
    boolean res=false;
    int faceNum=model.sizeF();
    
    try {
      getSTLOut(filename, faceNum);
      writeFaces(model.getF());

      out.flush();
      out.close();
      UMbMk2.log("Closing '"+filename+"'. "+faceNum+" triangles written.\n");
    } catch (Exception e) {
      res=false;
      e.printStackTrace();
    }
    
    return res;
  }

  public static boolean writeSTL(String filename,ArrayList<UGeo> models) {
    boolean res=false;
    int faceNum=UGeo.sizeF(models);
    
    try {
      getSTLOut(filename, faceNum);
      for(UGeo theModel:models) writeFaces(theModel.getF());

      out.flush();
      out.close();
      UMbMk2.log("Closing '"+filename+"'. "+faceNum+" triangles written.\n");
    } catch (Exception e) {
      res=false;
      e.printStackTrace();
    }
    
    return res;
  }

  private static String getSTLOut(String filename,int faceNum) throws IOException {
    if(!filename.toLowerCase().endsWith("stl")) filename+=".stl";


    out=(FileOutputStream)UFile.getOutputStream(filename);
    writeSTLHeader(faceNum);
    UMbMk2.logDivider("Writing STL '"+filename+"' "+faceNum);
    
    return filename;
  }

  private static void writeFaces(ArrayList<UFace> ff) throws IOException {
    byte[] header=new byte[50];
    
    for(UFace f:ff) {
      UVertex v[]=f.getV();
      UVertex fn=f.normal();
      
      buf.rewind();
      buf.putFloat(fn.x);
      buf.putFloat(fn.y);
      buf.putFloat(fn.z);
      
      for(int j=0; j<3; j++) {
        buf.putFloat(v[j].x);
        buf.putFloat(v[j].y);
        buf.putFloat(v[j].z);
      }
      
      buf.rewind();
      buf.get(header);
      out.write(header);
    }
  }

  private static void writeSTLHeader(int faceNum) throws IOException {
    byte[] header;
    buf = ByteBuffer.allocate(200);
    
    header=new byte[80];
    buf.get(header,0,80);
    out.write(header);
    buf.rewind();

    buf.order(ByteOrder.LITTLE_ENDIAN);
    
    buf.putInt(faceNum);
    buf.rewind();
    buf.get(header,0,4);
    out.write(header,0,4);
    buf.rewind();
    buf.clear();
  }
}
