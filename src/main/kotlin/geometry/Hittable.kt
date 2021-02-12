interface Hittable {
    fun hit(ray: Ray, tMin: Double, tMax: Double): Hit?
}

data class Hit(
    val p: Point3,
    val t: Double,
    val ray: Ray,
    val outwardNormal: Vec3,
    val material: Material
) {
    val frontFace: Boolean = ray.direction dot outwardNormal < 0
    val normal: Vec3 = if (frontFace) outwardNormal else -outwardNormal
}

data class WorldHit(val hittable: Hittable, val hit: Hit)
