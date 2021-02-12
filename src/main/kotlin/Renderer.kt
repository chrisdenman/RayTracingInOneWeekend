import Vec3.Companion.ZERO
import geometry.NonCulledTriangle
import geometry.Sphere
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import materials.Lambertian
import materials.Metal
import java.io.File

class Renderer(private val outputLocation: File) {

    private val world: World

    private val camera: Camera

    init {
        val lookAt = Point3(0, 0, 0)
        val lookFrom = Point3(0, 2, 2)
        val vup = Vec3(0, 1, 0)
        val focusDistance = 2.0
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
        private const val fieldOfViewDegrees: Double = 40.0
        private const val maxDepth = 50
        private const val samplesPerPixel = 200
        private const val imageWidth = 100
        private const val imageHeight = (imageWidth / aspectRatio).toInt()

        fun makeFinalScene(): World {
            val hittables = mutableListOf<Hittable>()
            val groundMaterial = Lambertian(Colour(0.5, 0.5, 0.5))
            hittables.add(Sphere(Point3(0, -1000, 0), 1000.0, groundMaterial))

            val axisColour = Colour(0.7, 0.9, 0.8)
            val axisNegColour = Colour(0.4, 0.2, 0.4)
            val axisMarkerMaterial = Metal(axisColour, 0.0)
            val axisMarkerNegMaterial = Metal(axisNegColour, 0.0)
            val axisMarkerRadius = 0.025
            val spacing = 0.2
            (-100..100).forEach { coordinate ->
                hittables.add(
                    Sphere(
                        Vec3(coordinate * spacing, 0.0, 0.0),
                        axisMarkerRadius,
                        if (coordinate < 0) axisMarkerNegMaterial else axisMarkerMaterial
                    )
                )

                hittables.add(
                    Sphere(
                        Vec3(0.0, coordinate.toDouble() * spacing, 0.0),
                        axisMarkerRadius,
                        if (coordinate < 0) axisMarkerNegMaterial else axisMarkerMaterial
                    )
                )

                hittables.add(
                    Sphere(
                        Vec3(0.0, 0.0, coordinate.toDouble() * spacing),
                        axisMarkerRadius,
                        if (coordinate < 0) axisMarkerNegMaterial else axisMarkerMaterial
                    )
                )
            }

//            val triangleMaterial = Metal(Colour(0.7, 0.9, 0.8), 0.0)
            val triangleMaterial = Lambertian(Colour(0.2, 0.9, 0.1))
            hittables.add(
                NonCulledTriangle(
                    Vec3(0.0, 0.0, 0.0),
                    Vec3(1.0, 0.0, -1.0),
                    Vec3(1.0, 1.0, -1.0),
                    triangleMaterial
                )
            )

            return World(hittables)
        }
    }
}

@ExperimentalCoroutinesApi
fun main() {
    Renderer(File("./results/test.ppm")).render()
}
