package materials

import Colour
import Hit
import Material
import Ray
import ScatterData
import Vec3.Companion.randomInUnitSphere
import Vec3.Companion.reflect
import times

class Metal(private val albedo: Colour, private val fuzz: Double) : Material {
    override fun scatter(ray: Ray, rec: Hit): ScatterData {
        val reflected = reflect(ray.direction.unit, rec.normal)
        val scatteredRay = Ray(rec.p, reflected + fuzz * randomInUnitSphere)
        val scattered = scatteredRay.direction dot rec.normal > 0
        return ScatterData(
            albedo,
            scatteredRay,
            scattered
        )
    }
}
