import Vec3.Companion.ZERO
import geometry.Sphere
import geometry.Triangle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import materials.Lambertian
import materials.Metal
import java.io.File
import kotlin.random.Random.Default.nextDouble

class Renderer(private val outputLocation: File) {

    private val world: World

    private val camera: Camera

    init {
        val lookAt = Point3(0, 1, 0)
        val lookFrom = Point3(-2, 5, 14)
        val vup = Vec3(0, 1, 0)
        val focusDistance = 10.0
        val aperture = 0.1
        camera = Camera(
            lookFrom,
            lookAt,
            vup,
            fieldOfViewDegrees,
            aspectRatio,
            aperture,
            focusDistance
        )

        world = makeFinalScene()
    }

    @ExperimentalCoroutinesApi
    fun render() {
        runBlocking {
            outputLocation.bufferedWriter().use {
                it.run {
                    write("P3\n")
                    write("$imageWidth\n")
                    write("$imageHeight\n")
                    write("255\n")
                    (imageHeight - 1).downTo(0).fold(Unit) { _, y ->
                        println("${y + 1}/$imageHeight scan lines remaining.")
                        val start = System.currentTimeMillis()
                        (0 until imageWidth).fold(Unit) { _, x ->
                            (0 until samplesPerPixel).map {
                                async(context = Dispatchers.Default) {
                                    val u = (x + Math.random()) / (imageWidth - 1)
                                    val v = (y + Math.random()) / (imageHeight - 1)
                                    val r = camera.getRay(u, v)
                                    r.colour(world, maxDepth)
                                }
                            }.let { jobs ->
                                jobs.joinAll()
                                writeColour(
                                    jobs.fold(ZERO) { acc, curr ->
                                        acc + curr.getCompleted()
                                    },
                                    samplesPerPixel
                                )
                            }
                        }
                        val end = System.currentTimeMillis()
                        println("Elapsed time ${(end - start)} mS")
                    }
                }
            }
        }
    }

    companion object {
        private const val aspectRatio: Double = 3.0 / 2.0
        private const val fieldOfViewDegrees: Double = 20.0
        private const val maxDepth = 50
        private const val samplesPerPixel = 100
        private const val imageWidth = 1000
        private const val imageHeight = (imageWidth / aspectRatio).toInt()

        fun makeFinalScene(): World {
            val hittables = mutableListOf<Hittable>()
            val groundMaterial = Lambertian(Colour(0.5, 0.5, 0.5))
            hittables.add(Sphere(Point3(0, -1000, 0), 1000.0, groundMaterial))

            for (a in -11 until 11) {
                val chooseMat = nextDouble()
                val center = Point3(a + 0.9, 0.2, 0 + 0.9)
                hittables.add(
                    Sphere(
                        center,
                        0.2,
                        Lambertian(Vec3.randomUnitComponents * Vec3.randomUnitComponents)
                    )
                )
                hittables.add(
                    Sphere(
                        center + Vec3(0.7, 0.0, -4.0),
                        0.2,
                        Lambertian(Vec3.randomUnitComponents * Vec3.randomUnitComponents)
                    )
                )
            }

            hittables.add(
                Triangle(
                    Point3(1, 0, 0),
                    Point3(4, 0, 0),
                    Point3(1, 3, 0),
                    Lambertian(Colour(0.9, 0.2, 0.1))
                )
            )

            hittables.add(
                Triangle(
                    Point3(-3, 0, -2),
                    Point3(3, 0, -2),
                    Point3(-3, 4, -2),
                    Metal(Colour(0.7, 0.9, 0.8), 0.2)
                )
            )

            hittables.add(Sphere(Point3(0, 1, 0), 1.0, Metal(Colour(0.7, 0.6, 0.5), 0.0)))

            return World(hittables)
        }
    }
}

@ExperimentalCoroutinesApi
fun main() {
    Renderer(File("./results/test.ppm")).render()
}
