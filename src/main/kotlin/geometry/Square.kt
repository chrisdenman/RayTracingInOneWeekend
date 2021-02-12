package geometry

import Hit
import Hittable
import Material
import Point3
import Ray
import Vec3

class Square(
    v0: Vec3,
    v1: Vec3,
    val material: Material
) : Hittable {

    private val t0 = CulledTriangle(
        v0,
        Point3(v1.x, v0.y, v1.z),
        Point3(v0.x, v1.y, v0.z),
        material
    )

    private val t1 = CulledTriangle(
        v1,
        Point3(v0.x, v1.y, v0.z),
        Point3(v1.x, v0.y, v1.z),
        material
    )

    override fun hit(ray: Ray, tMin: Double, tMax: Double): Hit? =
        t0.hit(ray, tMin, tMax) ?: t1.hit(ray, tMin, tMax)
}
