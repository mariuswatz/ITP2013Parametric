/*
 * modelbuilderMk2
 */
package processing.data;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import processing.core.PApplet;


/*
 * Marius Watz - July 2013
 * Simple hack to use processing.data.* from Processing 2.0.1
 * with legacy 1.5.1 code.
 * 
 * All code lifted from processing.core.PApplet, using 
 * the Processing 2.0.1 source on Github.
 */

public class ProcessingData {

  PApplet p;
  
  
  public ProcessingData(PApplet p) {
    this.p=p;
  }
  
//////////////////////////////////////////////////////////////

// DATA I/O


///**
//* @webref input:files
//* @brief Creates a new XML object
//* @param name the name to be given to the root element of the new XML object
//* @return an XML object, or null
//* @see XML
//* @see PApplet#loadXML(String)
//* @see PApplet#parseXML(String)
//* @see PApplet#saveXML(XML, String)
//*/
//public XML createXML(String name) {
//try {
//return new XML(name);
//} catch (Exception e) {
//e.printStackTrace();
//return null;
//}
//}


/**
* @webref input:files
* @param filename name of a file in the data folder or a URL.
* @see XML
* @see PApplet#parseXML(String)
* @see PApplet#saveXML(XML, String)
* @see PApplet#loadBytes(String)
* @see PApplet#loadStrings(String)
* @see PApplet#loadTable(String)
*/
public XML loadXML(String filename) {
return loadXML(filename, null);
}


// version that uses 'options' though there are currently no supported options
/**
* @nowebref
*/
public XML loadXML(String filename, String options) {
try {
return new XML(p.createReader(filename), options);
} catch (Exception e) {
e.printStackTrace();
return null;
}
}


/**
* @webref input:files
* @brief Converts String content to an XML object
* @param data the content to be parsed as XML
* @return an XML object, or null
* @see XML
* @see PApplet#loadXML(String)
* @see PApplet#saveXML(XML, String)
*/
public XML parseXML(String xmlString) {
return parseXML(xmlString, null);
}


public XML parseXML(String xmlString, String options) {
try {
return XML.parse(xmlString, options);
} catch (Exception e) {
e.printStackTrace();
return null;
}
}


/**
* @webref output:files
* @param xml the XML object to save to disk
* @param filename name of the file to write to
* @see XML
* @see PApplet#loadXML(String)
* @see PApplet#parseXML(String)
*/
public boolean saveXML(XML xml, String filename) {
return saveXML(xml, filename, null);
}


public boolean saveXML(XML xml, String filename, String options) {
return xml.save(p.saveFile(filename), options);
}


public JSONObject parseJSONObject(String input) {
return new JSONObject(new StringReader(input));
}

/**
* @webref input:files
* @param filename name of a file in the data folder or a URL
* @see JSONObject
* @see JSONArray
* @see PApplet#loadJSONArray(String)
* @see PApplet#saveJSONObject(JSONObject, String)
* @see PApplet#saveJSONArray(JSONArray, String)
*/
public JSONObject loadJSONObject(String filename) {
return new JSONObject(p.createReader(filename));
}

/**
* @webref output:files
* @see JSONObject
* @see JSONArray
* @see PApplet#loadJSONObject(String)
* @see PApplet#loadJSONArray(String)
* @see PApplet#saveJSONArray(JSONArray, String)
*/
public boolean saveJSONObject(JSONObject json, String filename) {
return saveJSONObject(json, filename, null);
}


public boolean saveJSONObject(JSONObject json, String filename, String options) {
return json.save(p.saveFile(filename), options);
}


public JSONArray parseJSONArray(String input) {
return new JSONArray(new StringReader(input));
}

/**
* @webref input:files
* @param filename name of a file in the data folder or a URL
* @see JSONObject
* @see JSONArray
* @see PApplet#loadJSONObject(String)
* @see PApplet#saveJSONObject(JSONObject, String)
* @see PApplet#saveJSONArray(JSONArray, String)
*/
public JSONArray loadJSONArray(String filename) {
return new JSONArray(p.createReader(filename));
}

/**
* @webref output:files
* @see JSONObject
* @see JSONArray
* @see PApplet#loadJSONObject(String)
* @see PApplet#loadJSONArray(String)
* @see PApplet#saveJSONObject(JSONObject, String)
*/
public boolean saveJSONArray(JSONArray json, String filename) {
return saveJSONArray(json, filename, null);
}


public boolean saveJSONArray(JSONArray json, String filename, String options) {
return json.save(p.saveFile(filename), options);
}



///**
//* @webref input:files
//* @see Table
//* @see PApplet#loadTable(String)
//* @see PApplet#saveTable(Table, String)
//*/
//public Table createTable() {
//return new Table();
//}


/**
* @webref input:files
* @param filename name of a file in the data folder or a URL.
* @see Table
* @see PApplet#saveTable(Table, String)
* @see PApplet#loadBytes(String)
* @see PApplet#loadStrings(String)
* @see PApplet#loadXML(String)
*/
public Table loadTable(String filename) {
return loadTable(filename, null);
}


/**
* @param options may contain "header", "tsv", "csv", or "bin" separated by commas
*/
public Table loadTable(String filename, String options) {
try {
//String ext = checkExtension(filename);
//if (ext != null) {
//if (ext.equals("csv") || ext.equals("tsv") || ext.equals("bin")) {
//if (options == null) {
//options = ext;
//} else {
//options = ext + "," + options;
//}
//}
//}
return new Table(p.createInput(filename),
Table.extensionOptions(true, filename, options));

} catch (IOException e) {
e.printStackTrace();
return null;
}
}


/**
* @webref input:files
* @param table the Table object to save to a file
* @param filename the filename to which the Table should be saved
* @see Table
* @see PApplet#loadTable(String)
*/
public boolean saveTable(Table table, String filename) {
return saveTable(table, filename, null);
}


/**
* @param options can be one of "tsv", "csv", "bin", or "html"
*/
public boolean saveTable(Table table, String filename, String options) {
//String ext = checkExtension(filename);
//if (ext != null) {
//if (ext.equals("csv") || ext.equals("tsv") || ext.equals("bin") || ext.equals("html")) {
//if (options == null) {
//options = ext;
//} else {
//options = ext + "," + options;
//}
//}
//}

try {
// Figure out location and make sure the target path exists
File outputFile = p.saveFile(filename);
// Open a stream and take care of .gz if necessary
return table.save(outputFile, options);

} catch (IOException e) {
e.printStackTrace();
return false;
}
}

}
