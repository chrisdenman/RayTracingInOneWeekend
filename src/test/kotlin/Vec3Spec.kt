import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD
import kotlin.math.abs

@TestInstance(PER_METHOD)
internal class Vec3Spec {

    private infix fun Vec3.assertEquals(other: Vec3) = assertEquals(this, other)

    private infix fun Vec3.tolerablyEquals(other: Vec3) = assertTrue((this - other).isNearZero)

    @Test fun `Constructing with Double values`() {
        val x = 3.0
        val y = 5.0
        val z = 7.0
        Vec3(x, y, z).let {
            assertEquals(x, it.x)
            assertEquals(y, it.y)
            assertEquals(z, it.z)
        }
    }

    @Test fun `Constructing with Integer values`() {
        val x = 3
        val y = -5
        val z = 13
        Vec3(x, y, z).let {
            assertEquals(x, it.x.toInt())
            assertEquals(y, it.y.toInt())
            assertEquals(z, it.z.toInt())
        }
    }

    @Test fun `equality testing`() {
        assertEquals(V0, Vec3(X0, Y0, Z0))
    }

    @Test fun `componentN accessors`() {
        V0.run {
            assertEquals(X0, component1())
            assertEquals(Y0, component2())
            assertEquals(Z0, component3())
        }
    }

    @Test fun `That isNearZero is true if each component is less than the allowable tolerance`() {
        assertTrue(Vec3(TOLERABLE, TOLERABLE, TOLERABLE).isNearZero)
    }

    @Test fun `That isNearZero is false if each the x component not within the allowable tolerance`() {
        assertFalse(Vec3(INTOLERABLE, TOLERABLE, TOLERABLE).isNearZero)
    }

    @Test fun `That isNearZero is false if each the y component not within the allowable tolerance`() {
        assertFalse(Vec3(TOLERABLE, INTOLERABLE, TOLERABLE).isNearZero)
    }

    @Test fun `That isNearZero is false if each the z component not within the allowable tolerance`() {
        assertFalse(Vec3(TOLERABLE, TOLERABLE, INTOLERABLE).isNearZero)
    }

    @Test fun `Unary minus operator`() {
        -V0 assertEquals Vec3(-X0, -Y0, -Z0)
    }

    @Test fun `The summing of two vectors`() {
        (V0 + V1) assertEquals Vec3(X0 + X1, Y0 + Y1, Z0 + Z1)
    }

    @Test fun `The difference of two vectors`() {
        (V0 - V1) assertEquals Vec3(X0 - X1, Y0 - Y1, Z0 - Z1)
    }

    @Test fun `Scaling a vector by a scalar`() {
        (V0 * T) assertEquals Vec3(V0.x * T, V0.y * T, V0.z * T)
    }

    @Test fun `Scaling a scalar by a vector`() {
        (T * V0) assertEquals Vec3(V0.x * T, V0.y * T, V0.z * T)
    }

    @Test fun `Reciprocating a Double`() {
        (-23.11).run {
            assertEquals(1.0 / this, reciprocal)
        }
    }

    @Test fun `Obtaining the magnitude squared`() {
        tolerable(103881.3544 - Vec3(-11.1, 321.12, -25.3).magnitudeSquared)
    }

    @Test fun `Obtaining the magnitude`() {
        tolerable(322.306305243940271 - Vec3(-11.1, 321.12, -25.3).magnitude)
    }

    @Test fun `Component-wise scaling by another vector`() {
        V0.scale(V1) assertEquals Vec3(V0.x * V1.x, V0.y * V1.y, V0.z * V1.z)
    }

    @Test fun `Dividing a vector by a scalar`() {
        (V0 / T) tolerablyEquals Vec3(V0.x / T, V0.y / T, V0.z / T)
    }

    @Test fun `Dot product`() {
        assertEquals(V0 dot V1, (V0.x * V1.x) + (V0.y * V1.y) + (V0.z * V1.z))
    }

    @Test fun `Cross product`() {
        (V0 * V1) assertEquals Vec3(
            Y0 * Z1 - Z0 * Y1,
            Z0 * X1 - X0 * Z1,
            X0 * Y1 - Y0 * X1
        )
    }

    @Test fun `Obtaining a unit vector`() {
        V0.unit.let {
            it tolerablyEquals Vec3(-0.22770953509689237, 0.46257565558254427, -0.8568384506360207)
            tolerable(it.magnitude - 1.0)
        }
    }

    companion object {
        const val X0 = -3.5
        const val Y0 = 7.11
        const val Z0 = -13.17

        const val X1 = -17.3
        const val Y1 = 23.5
        const val Z1 = 27.5

        const val T = 23.11

        val V0 = Vec3(X0, Y0, Z0)
        val V1 = Vec3(X1, Y1, Z1)

        const val TOLERABLE = 1E-9
        const val INTOLERABLE = 1E-8

        fun tolerable(e: Double) = assertTrue(abs(e) < TOLERABLE)
    }
}
