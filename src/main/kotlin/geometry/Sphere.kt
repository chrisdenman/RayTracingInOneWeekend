package geometry

import Hit
import Hittable
import Material
import Point3
import Ray
import kotlin.math.sqrt

class Sphere(
    private val center: Point3,
    private val radius: Double,
    val material: Material) : Hittable {

    override fun hit(ray: Ray, tMin: Double, tMax: Double): Hit? {
        val oc = ray.origin - center
        val a = ray.direction.magnitudeSquared
        val halfB = oc dot ray.direction
        val c = oc.magnitudeSquared - (radius * radius)
        val discriminant = (halfB * halfB) - (a * c)
        if (discriminant < 0) return null
        val sqrtd = sqrt(discriminant)
        val root = ((-halfB - sqrtd) / a).let {
            when {
                (it < tMin || tMax < it) -> {
                    ((-halfB + sqrtd) / a).let {
                        when {
                            (it < tMin || tMax < it) -> null
                            else -> it
                        }
                    }
                }
                else -> it
            }
        }

        return root?.let { it ->
            val p = ray.at(it)
            val outwardNormal = (p - center) / radius

            return Hit(p, it, ray, outwardNormal, material)
        }
    }
}
