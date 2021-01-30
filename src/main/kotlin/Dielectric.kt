import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random.Default.nextDouble

class Dielectric(private val indexOfRefraction: Double) : Material {
    override fun scatter(ray: Ray, rec: Hit): ScatterData {
        val attenuation = Colour.ONE
        val refractionRatio =
            if (rec.frontFace) indexOfRefraction.reciprocal() else indexOfRefraction
        val unitRayDirection = ray.direction.unit

        val cosTheta = min(1.0, (-unitRayDirection) dot rec.normal)
        val sinTheta = sqrt(1 - (cosTheta * cosTheta))
        val cannotRefract = (refractionRatio * sinTheta) > 1.0

        val direction = if (cannotRefract || reflectance(cosTheta, refractionRatio) > nextDouble())
            Vec3.reflect(unitRayDirection, rec.normal) else
            Vec3.refract(unitRayDirection, rec.normal, refractionRatio)
        val scattered = Ray(rec.p, direction)
        return ScatterData(attenuation, scattered, true)
    }

    companion object {
        private fun reflectance(cosine: Double, refIndex: Double): Double =
            ((1 - refIndex) / (1 + refIndex)).let {
                it * it
            }.let {
                it + (1- it) * (1 - cosine).pow(5.0)
            }
    }
}
