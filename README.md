# [Ray Tracing in One Weekend](https://raytracing.github.io/books/RayTracingInOneWeekend.html)

A Kotlin implementation of Peter Shirley's 'Ray Tracing in One Weekend' book.


## Chapter Links

1.  Overview
1.  [Output An Image](https://raytracing.github.io/books/RayTracingInOneWeekend.html#outputanimage)
1.  [The vec3 class](https://raytracing.github.io/books/RayTracingInOneWeekend.html#thevec3class)
1. [Rays, a Simple Camera, and Background](https://raytracing.github.io/books/RayTracingInOneWeekend.html#rays,asimplecamera,andbackground)
1. [Adding a Sphere](https://raytracing.github.io/books/RayTracingInOneWeekend.html#addingasphere)
1. [Surface Normals and Multiple Objects](https://raytracing.github.io/books/RayTracingInOneWeekend.html#surfacenormalsandmultipleobjects)
1. [Antialiasing](https://raytracing.github.io/books/RayTracingInOneWeekend.html#antialiasing)
1. [Diffuse Materials](https://raytracing.github.io/books/RayTracingInOneWeekend.html#diffusematerials)
1. [Metal](https://raytracing.github.io/books/RayTracingInOneWeekend.html#metal)
1. [Dielectrics](https://raytracing.github.io/books/RayTracingInOneWeekend.html#dielectrics)
1. [Positionable Camera](https://raytracing.github.io/books/RayTracingInOneWeekend.html#positionablecamera)
1.  [Defocus Blur](https://raytracing.github.io/books/RayTracingInOneWeekend.html#defocusblur)
1.  [Where Next?](https://raytracing.github.io/books/RayTracingInOneWeekend.html#wherenext?)


## Notes

-   Not optimised for efficiency.
-   I use a slightly different method for refraction calculation (see first reference).


## Final Scene

![An image of the final rendered scene](generated/finalScene.png "Final Rendered Scene")
Rendered at: 2000px * 1333px, 500 samples/px, maximum depth of 50.


## Future Work

-   Rectangular parallelepipeds.    
-   Triangle support.
-   Abstracting the description of scenes (maybe a DSL and custom file-format).


## References

- [Ray Tracing in One Weekend — The Book Series](https://raytracing.github.io)
- [Reflections and Refractions in Ray Tracing - Bram de Greve (2006)](https://graphics.stanford.edu/courses/cs148-10-summer/docs/2006--degreve--reflection_refraction.pdf)
- [Fast Minimum Storage Ray Triangle Intersection - Tomas Moller, Ben Trumb ore](https://cadxfem.org/inf/Fast%20MinimumStorage%20RayTriangle%20Intersection.pdf)
