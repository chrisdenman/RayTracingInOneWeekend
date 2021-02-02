import Vec3.Companion.randomInUnitDisc
import java.lang.Math.toRadians
import kotlin.math.tan

class Camera(
    lookFrom: Point3,
    lookAt: Point3,
    vUp: Vec3,
    verticalFieldOfViewDegrees: Double,
    aspectRatio: Double,
    aperture: Double,
    focusDistance: Double
) {

    private val theta = toRadians(verticalFieldOfViewDegrees)
    private val h = tan(theta / 2)
    private val viewportHeight = 2.0 * h
    private val viewportWidth = aspectRatio * viewportHeight

    private val w = (lookFrom - lookAt).unit
    private val u = (vUp * w).unit
    private val v = w * u

    private val origin = lookFrom

    private val horizontal = focusDistance * viewportWidth * u
    private val vertical = focusDistance * viewportHeight * v
    private val lowerLeftCorner = origin - (horizontal / 2.0) - (vertical / 2.0) - (focusDistance * w)
    private val lensRadius = aperture / 2

    fun getRay(s: Double, t: Double): Ray {
        val rd = lensRadius * randomInUnitDisc
        val offset = u * rd.x + v * rd.y
        return Ray(
            origin + offset,
            lowerLeftCorner + (s * horizontal) + (t * vertical) - origin - offset
        )
    }
}
