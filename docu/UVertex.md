###UVertex

The atomic unit of Modelbuilder geometry. It provides standard vector functionality, extended with data fields useful for mesh construction and rendering.

- float x,y,z - vector components
- int ID - unique auto-generated integer ID
- float U,V - UV coordinates for use with textures or to store parameter info
- int color - ARGB color for per-vertex shading

###UVertexList

Auto-growing list of UVertex data, stored internally as ArrayList<UVertexList> and easily copied and manipulated. 

**Data**

- ArrayList<UVertex> v; (actual list)
- int options; (bit flags incl. NOCOPY and NODUPL)
- UBB bb; (bounding box)

Modelbuilder is built around the idea of a simplified workflow for generating meshes. UVertex is the base unit, collected in UVertexList to
define edges or paths in space. These paths are as input to UGeo (as pairs or lists) to construct actual mesh geometry.

Mesh generation is done with methods familiar from Processing and OpenGL: 

- Quad strips (quads connecting two vertex lists) 
- Triangle fans (fan of triangles with a shared central vertex)

