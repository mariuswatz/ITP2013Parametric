# ITP, NYU Fall 2013: Parametric Design for Digital Fabrication

Class for NYU ITP, Fall 2013. Instructor is [Marius Watz](http://mariuswatz.com).
See the Code & Form blog for a detailed description](http://workshop.evolutionzone.com/itp-2013-parametric-design-for-digital-fabrication/)
Contact e-mail: marius at mariuswatz dot com
Class hours: Mondays 6:00pm – 8:55pm

## October 30

Added source folder "src-itp-sketches". It contains miscellaneous code demos and ModelbuilderMk2 tutorials demonstrated in the Parametric Design class at ITP.


## October 20

Plenty of incremental updates and fixes, notably the new UHeading class which supports aligning geometry and vertex lists to heading vectors given by two vertices. That piece of code is based on the [Apache Commons Mathematics Library](http://commons.apache.org/proper/commons-math),
specifically the org.apache.commons.math.geometry package. 

STL import has been added, see UGeoIO.readSTL().


## Sept 28

ModelbuilderMk2: The core functionality of the library is now in place, offering a full replacement for the old Modelbuilder's geometry workflow. The new library design offers many subtle improvements along with some totally new tools. 

Some elements from the old code base (for instance USimpleGUI) are still missing. Given that many functions were added ad hoc and likely only used by myself, I will only re-implement  missing classes that seem important to common uses for the library.

News:

- unlekker.mb2.geo.UNav3D has been re-implemented. If UNav3D is instantiated after UMB.setPApplet() has been called it will automatically register with that PApplet instance to receive events.
- unlekker.mb2.util.UBase (the base class that is extended by most of the core classes) has been renamed to unlekker.mb2.util.UMB for brevity and clarity. (For a second it was called UMbMk2, but that proved too hard to pronounce in class....)
- unlekker.mb2.geo.UTriangulate now offers triangulation of vertex sets that represent 2.5D topologies. It is based on Florian Jenett's code from [http://wiki.processing.org/w/Triangulation](http://wiki.processing.org/w/Triangulation), but it hasn't been extensively tested and appears to have some peculiarities.
- Added UGeo.triangulation(), adds faces produced by triangulation of a UVertexList. As with UTriangulate, this code has not been extensively tested.
- Added UVertexList.point(t), returns an interpolated vertex at position "t" along the vertex list. Ideally this should be done by accounting for actual length of the list, currently the calculation is done using the number of vertices so that t=0.5 of a list with 33 vertices will give an interpolation t=0.5 between the vertices at positions 16 and 17 (t*0.33= position 16 with 0.5 remainder)
- Added UVertexList.resample(n), creates a resampled version of a UVertexList with "n" number of vertices. For now this function uses UVertexList.point(), which is not an ideal solution since it is based only on vertex indices rather than the actual vector length of the list.
- Added UVertexList.copyNoDupl(), returns a copy of the given list with duplicate vertices removed.

## Sept 7

ModelbuilderMk2: Added STL export and file utilities (UFile). The current code works with 2.0 and 1.5.1, but I'm not doing a packaged Processing-ready release just yet. If you're working in Eclipse or similar IDE you can download the code and plug it in.

## Sept 6

Added first version of ModelbuilderMk2, which is a complete rewrite of Modelbuilder. Currently implemented (but incomplete): UVertex, UVertexLists, UGeo - basic features for mesh creation. STL output is not included yet.

## Sept 5

Added ProcessingData library, which is a hack to make the Processing 2.0 Data API available for use in Processing 1.5.1.

## Aug 30 

Posted code for Modelbuilder-0020, which is compatible with Processing 1.5.1. We will eventually migrate to Processing 2.0, but for now 1.5.1 is a more stable platform and many of the Modelbuilder examples have not been updated to be compatible. This build uses ControlP5 0.5.4 by Andreas Schlegel, which can be [downloaded from his repository](https://code.google.com/p/controlp5/downloads/detail?name=controlP5_0.5.4.zip&can=2&q=).

Modelbuilder is in bad need of restructuring. It currently suffers from an abundanc of ad hoc hackery and some poor design decisions, hardly surprising given that is my first serious attempt at writing a comprehensive geometry library. I plan to rewrite it from scratch as part of teaching this class.


## Code resources + tutorials

+ [Nature of Code](http://natureofcode.com/) Daniel Shiffman (esp. [Chapter 1. Vectors](http://natureofcode.com/book/chapter-1-vectors/))
+ [Modelbuilder](https://github.com/mariuswatz/modelbuilder), Marius Watz (Processing library)
+ [Toxiclibs.org,](http://toxiclibs.org/) Karsten Schmidt (Processing library)
+ [Hemesh,](http://hemesh.wblut.com/) Frederik Vanhoutte (Processing library)
+ [OpenProcessing.org,](http://www.openprocessing.org/) online community for the posting of Processing sketches (useful search terms: ["geometry"](http://www.openprocessing.org/search/?q=geometry), ["mesh"](http://www.openprocessing.org/search/?q=geometry), ["architecture"](http://www.openprocessing.org/search/?q=architecture))

Remember: Learning by example is excellent, but always give credit (w/ linkbacks) where due. Appropriation of styles or creative content without modification or prior permission is laziness and will inevitably be discovered.

## Inspiration

+ [Flickr: Digital fabrication](http://www.flickr.com/groups/digitalfabrication/)
+ [pinterest/watz: Digital fabrication](http://pinterest.com/watzmarius/digital-fabrication/)
+ [pinterest/Andrew Kudless](http://pinterest.com/matsys/computational-design/)
+ [pinterest/Trang Nguyen](http://pinterest.com/trangng/texture-materiality/)
+ [Parametric World Tumblr](http://parametricworld.tumblr.com/)

As it turns out, Pinterest and Tumblr are excellent sources for inspiration and research, at least in the sense of providing an endless stream of images. Unfortunately, the widespread lack of proper accreditation can make it difficult to trace the origin of works and learn more about the context of their creation.
