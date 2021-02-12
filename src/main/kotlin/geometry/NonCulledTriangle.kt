package geometry

import Hit
import Hittable
import Material
import Ray
import Vec3
import isNearZero
import lessThanZero
import reciprocal

class NonCulledTriangle(
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

        if (det.isNearZero) {
            return null
        }

        val inv_det = det.reciprocal

        val tvec = ray.origin - vert0
        val u = (tvec dot pvec) * inv_det
        if (u.lessThanZero || u > 1.0) {
            return null
        }

        val qvec = (tvec * edge1) * inv_det
        val v = (dir dot qvec) * inv_det
        if (v.lessThanZero || (u + v) > 1.0) {
            return null
        }

        val t = (edge2 dot qvec) * inv_det
        return if (t !in tMin..tMax) {
            null
        } else
            Hit(ray.at(t), t, ray, edge1 * edge2, material)
    }
}
