package geometry

import Hit
import Hittable
import Material
import Ray
import Vec3
import materials.Lambertian
import java.lang.Double.max
import java.lang.Double.min

class Cube(
    x0: Double,
    x1: Double,
    y0: Double,
    y1: Double,
    z0: Double,
    z1: Double,
    ) : Hittable {

    private fun getMaterial()=
        Lambertian(Vec3.randomUnitComponents)

    private val xMin = min(x0, x1)
    private val xMax = max(x0, x1)

    private val yMin = min(y0, y1)
    private val yMax = max(y0, y1)

    private val zMin = min(z0, z1)
    private val zMax = max(z0, z1)

    private val v0 = Vec3(xMin, yMin, zMin)
    private val v2 = Vec3(xMax, yMax, zMin)
    private val v4 = Vec3(xMax, yMin, zMax)
    private val v6 = Vec3(xMin, yMax, zMax)

    private val f0: Square = Square(v0, v2, getMaterial())
    private val f1: Square = Square(v2, v4, getMaterial())
    private val f2: Square = Square(v4, v6, getMaterial())
    private val f3: Square = Square(v6, v0, getMaterial())

    private val f4: Square = Square(v2, v6, getMaterial())
    private val f5: Square = Square(v0, v4, getMaterial())

    override fun hit(ray: Ray, tMin: Double, tMax: Double): Hit? {
        return listOf(f0, f1, f2, f3, f4, f5)
            .map { it.hit(ray, tMin, tMax) }
            .filterNotNull()
            .sortedBy { it.t }
            .firstOrNull()
    }
}
