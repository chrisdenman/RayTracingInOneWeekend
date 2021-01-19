import java.io.Writer
import kotlin.math.sqrt

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

    fun magnitudeSquared(): Double = x * x + y * y + z * z

    fun magnitude(): Double = sqrt(magnitudeSquared())

    operator fun div(t: Double): Vec3 = this * (1.0/t)

    infix fun dot(v: Vec3) = Vec3(x * v.x, y * v.y, z * v.z)

    operator fun times(v: Vec3) = Vec3(
        y * v.z - z * v.y,
        z * v.x - x * v.z,
        x * v.y - y * v.x
    )

    fun unit(): Vec3 = this / magnitude()

    companion object {
        val ZERO = Vec3(0,0,0)
        val UNIT = Vec3(1,1,1)
    }
}

operator fun Double.times(v: Vec3) = v * this

typealias Point3 = Vec3
typealias Colour = Vec3

fun Writer.writeColour(pixelColour: Colour) {
    (pixelColour * 255.999).let {
        write("${it.x.toInt()} ${it.y.toInt()} ${it.z.toInt()}\n")
    }
}

fun rayColour(ray: Ray): Colour {
    val unitRay = ray.direction.unit()
    val t: Double = (unitRay.y + 1.0) * 0.5
    return Vec3.UNIT * (1.0 - t) + Colour(0.5, 0.7, 1.0) * t
}
