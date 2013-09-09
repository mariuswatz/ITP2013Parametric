ModelbuilderMk2 - TODO
==================

+ UVertexList and UGeo: Support NODUPL/NOCOPY options
+ UVertexList: enable(NODUPL) should remove duplicate vertices
+ Implement bounding box UBB
+ Clarify methods related to UV for UVertexList and UVertex
+ UGeo: Add UGeo meshes to each other
+ UGeo: Remove faces
+ Check for duplicate faces
+ Color STL export
+ UGeoIO - add writeSTL for multiple UGeo instances
+ Port UNav3D
+ Splines (Bezier, Hermite etc)
+ UGeo: Add triangulation / POLYGON shape input
+ UGeo: Support TRIANGLE_STRIP
+ Improved logging
+ Color palette generation
+ Calculate per-vertex normals (by averaging face normals) for smooth mesh rendering
+ Shaders etc.
+ Interpolation / shaping methods

ModelbuilderMk2 Examples - TODO
==================

+ Basic UVertexList demo
+ Basic UGeo demo - build mesh from quadstrips
+ Using UV to color mesh per-face and per-vertex


Done
==================

Sept 7
+ STL export
+ File name tools
+ Incremental file naming


ModelbuilderMk2 - Wishful thinking list
==================

+ Clip UGeo / UFace to plane to cut models (clip to XZ plane would solve 90% of needs, clip/cut by arbitray plane would be nice but less significant)
+ Clip to plane with filling in of cut sections
+ CSG boolean operations
+ Sophisticated testing for duplicate faces (taking into account face normals)
+ Mesh smoothing
+ Sweep / loft functions
+ Quaternion rotations / frame transportation for outlining path with profile
+ Triangle strip unwrapping to 2D plane for laser cutting
+ Mesh to 2D triangles + connectors for laser cutting

