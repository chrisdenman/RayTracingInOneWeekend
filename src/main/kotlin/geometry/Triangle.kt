package geometry

import Hit
import Hittable
import Material
import Ray
import Vec3
import isNearZero
import reciprocal
import times

class Triangle(
    private val vert0: Vec3,
    private val vert1: Vec3,
    private val vert2: Vec3,
    override val material: Material) : Hittable {

    override fun hit(ray: Ray, tMin: Double, tMax: Double): Hit? {
        val edge1 = vert1 - vert0
        val edge2 = vert2 - vert0
        val pvec = ray.direction * edge2
        val det = edge1 dot pvec
        if (det.isNearZero) {
            return null
        }

        val tvec = ray.origin - vert0
        var u = tvec dot pvec
        if (u < 0.0 || u > det) {
            return null
        }

        val qvec = tvec * edge1
        var v = ray.direction dot qvec
        if (v < 0.0 || (u + v) > det) {
            return null
        }

        var t = edge2 dot qvec
        val inv_det = det.reciprocal
        t *= inv_det
        if (t !in tMin..tMax) {
            return null
        }
        u *= inv_det
        v *= inv_det

        return Hit(ray.at(t), t, ray, edge1 * edge2)
    }
}
