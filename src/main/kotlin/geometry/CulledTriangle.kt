package geometry

import Hit
import Hittable
import Material
import Ray
import TOLERANCE
import Vec3
import isNearZero
import lessThanZero

class CulledTriangle(
    private val vert0: Vec3,
    private val vert1: Vec3,
    private val vert2: Vec3,
    val material: Material
) : Hittable {

    override fun hit(ray: Ray, tMin: Double, tMax: Double): Hit? {
        val edge1 = vert1 - vert0
        val edge2 = vert2 - vert0
        val dir = ray.direction.unit
        val pvec = dir * edge2
        val det = edge1 dot pvec

        if (det < TOLERANCE) {
            return null
        }

        val tvec = ray.origin - vert0
        val u = tvec dot pvec
        if (u.lessThanZero ||  u > det) {
            return null
        }

        val qvec = tvec * edge1
        val v = dir dot qvec
        if (v.lessThanZero || u + v > det) {
            return null
        }

        val t = (edge2 dot qvec) / det
        return if (t !in tMin..tMax) {
            null
        } else {
            Hit(ray.at(t), t, ray, -(edge1 * edge2), material)
        }
    }
}
