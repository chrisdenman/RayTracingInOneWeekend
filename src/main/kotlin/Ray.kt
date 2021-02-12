data class Ray(val origin: Point3, val direction: Vec3) {

    fun at(t: Double): Point3 = origin + (direction * t)

    fun colour(world: World, depth: Int = 0): Vec3 =
        when {
            depth <= 0 -> Colour.ZERO
            else -> {
                world.hit(this, 0.001, Double.POSITIVE_INFINITY).let { worldHit ->
                    worldHit?.hit?.material?.scatter(this, worldHit.hit)?.let { scatterData ->
                        when (scatterData.isScattered) {
                            true -> scatterData.ray.colour(world, depth - 1).scale(scatterData.attenuation)
                            else -> Colour.ZERO
                        }
                    }
                        ?: (0.5 * (direction.unit.y + 1.0)).let { t ->
                            ((1.0 - t) * Colour.ONE) + (t * Colour(0.5, 0.7, 1.0))
                        }
                }
            }
        }
}
