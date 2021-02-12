import java.io.Writer
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random.Default.nextDouble

const val TOLERANCE = 1E-8

@Suppress("unused")
data class Vec3(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0) {

    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun unaryMinus(): Vec3 = Vec3(-x, -y, -z)

    operator fun plus(other: Vec3): Vec3 = Vec3(
        x + other.x,
        y + other.y,
        z + other.z
    )

    operator fun minus(other: Vec3): Vec3 = this + -other

    operator fun times(t: Double): Vec3 = Vec3(x * t, y * t, z * t)

    val magnitudeSquared: Double = (x * x) + (y * y) + (z * z)

    val magnitude: Double = sqrt(magnitudeSquared)

    val isNearZero: Boolean = x.isNearZero && y.isNearZero && z.isNearZero

    fun scale(scalar: Vec3) = Vec3(
        scalar.x * x,
        scalar.y * y,
        scalar.z * z
    )

    operator fun div(t: Double): Vec3 = this * t.reciprocal

    infix fun dot(v: Vec3) = (x * v.x) + (y * v.y) + (z * v.z)

    operator fun times(v: Vec3) = Vec3(
        y * v.z - z * v.y,
        z * v.x - x * v.z,
        x * v.y - y * v.x
    )

    val unit: Vec3
        get() = this / magnitude

    companion object {
        val ONE = Vec3(1, 1, 1)
        val ZERO = Vec3(0, 0, 0)

        fun boundedRandomComponents(minInclusive: Double, maxExclusive: Double): Vec3 =
            Vec3(
                nextDouble(minInclusive, maxExclusive),
                nextDouble(minInclusive, maxExclusive),
                nextDouble(minInclusive, maxExclusive)
            )

        val randomInUnitDisc: Vec3
            get() {
                while (true) {
                    Vec3(
                        nextDouble(-1.0, 1.0),
                        nextDouble(-1.0, 1.0),
                        0.0
                    ).let { candidate ->
                        if (candidate.magnitudeSquared < 1) return candidate
                    }
                }
            }

        fun randomInHemisphere(normal: Vec3): Vec3 =
            randomInUnitSphere.let {
                when {
                    it dot normal > 0.0 -> it
                    else -> -it
                }
            }

        val randomInUnitSphere: Vec3
            get() = boundedRandomComponents(-1.0, 1.0).let {
                when {
                    it.magnitudeSquared >= 1 -> randomInUnitSphere
                    else -> it
                }
            }

        val randomUnit: Vec3
            get() = randomInUnitSphere.unit

        val randomUnitComponents: Vec3
            get() = Vec3(nextDouble(), nextDouble(), nextDouble())

        fun reflect(v: Vec3, normal: Vec3): Vec3 = v - 2 * (v dot normal) * normal

        fun refract(i: Vec3, n: Vec3, etai_over_etat: Double): Vec3 {
            val cosThetaI = min(-i dot n, 1.0)
            val sinSquaredThetaT = etai_over_etat * etai_over_etat * (1 - (cosThetaI * cosThetaI))
            return (etai_over_etat * i) + (etai_over_etat * cosThetaI - sqrt(1 - sinSquaredThetaT)) * n
        }
    }
}

typealias Point3 = Vec3
typealias Colour = Vec3

operator fun Double.times(v: Vec3) = v * this

val Double.reciprocal get() = 1.0 / this
val Double.cosOrSin get() = sqrt(1 - this * this)
val Double.isNearZero get() = abs(this) < TOLERANCE
val Double.lessThanZero get() = this < 0.0
val Double.greaterThanZero get() = this > 0.0

fun Double.clamp(minInclusive: Double, maxInclusive: Double) = when {
    this < minInclusive -> minInclusive
    this > maxInclusive -> maxInclusive
    else -> this
}

fun Writer.writeColour(pixelColour: Colour, samplesPerPixel: Int) {
    val scale = samplesPerPixel.toDouble().reciprocal
    val scaled = scale * pixelColour
    val gammaCorrected = Vec3(sqrt(scaled.x), sqrt(scaled.y), sqrt(scaled.z))
    gammaCorrected.let {
        write(
            "${(256 * it.x.clamp(0.0, 0.999)).toInt()} " +
                "${(256 * it.y.clamp(0.0, 0.999)).toInt()} " +
                "${(256 * it.z.clamp(0.0, 0.999)).toInt()}\n"
        )
    }
}
