package raytracer.geom;

import raytracer.math.Point;
import raytracer.math.Vec3;

import java.util.ArrayList;
import java.util.List;

public final class GeomFactory {

	//TODO: Edited
	private static final List<Primitive> objects = new ArrayList<>();

	private GeomFactory() {}

	/**
	 * Generates a plane from three given points. Each of the points is located
	 * on this plane. Therefore the points span the plane.
	 *
	 * The points must not lie on a straight line.
	 *
	 * @param a  First span point
	 * @param b  Second span point
	 * @param c  Third span point
	 * @return   The new plane
	 */
	public static Primitive createPlane(final Point a, final Point b, final Point c) {
		Plane plane = new Plane(a, b, c);

		objects.add(plane);

		return plane;
	}

	/**
	 * Generates a plane from a given point (that is located on the plane) and a
	 * normal vector of this plane.
	 *
	 * @param n     The normal vector defining this plane
	 * @param supp  An arbitrary point that lies on the plane
	 * @return      The new plane
	 */
	public static Primitive createPlane(final Vec3 n, final Point supp) {
		Plane plane = new Plane(n, supp);

		objects.add(plane);

		return plane;
	}

	/**
	 * Generates a sphere from given center point and radius.
	 *
	 * @param m  Center point of the sphere
	 * @param r  Radius
	 * @return   The new sphere
	 */
	public static Primitive createSphere(final Point m, final float r) {
		Sphere sphere = new Sphere(m, r);

		objects.add(sphere);

		return sphere;
	}

	/**
	 * Generates a triangle from given three points.
	 *
	 * @param a  First point
	 * @param b  Second point
	 * @param c  Third point
	 * @return   The new triangle
	 */
	public static Triangle createTriangle(final Point a, final Point b, final Point c) {
		Triangle triangle = new Triangle(a, b, c);

		objects.add(triangle);

		return triangle;
	}

	/**
	 * Gets a list of all created objects.
	 *
	 * @return A list of all created objects.
	 */
	public static List<Primitive> getObjects() {
		return objects;
	}
}
