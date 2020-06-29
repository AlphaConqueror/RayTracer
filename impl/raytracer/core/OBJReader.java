package raytracer.core;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import raytracer.core.def.Accelerator;
import raytracer.core.def.StandardObj;
import raytracer.geom.GeomFactory;
import raytracer.geom.Primitive;
import raytracer.math.Point;
import raytracer.math.Vec3;

/**
 * Represents a model file reader for the OBJ format
 */
public class OBJReader {

	/**
	 * Reads an OBJ file and uses the given shader for all triangles. While
	 * loading the triangles they are inserted into the given acceleration
	 * structure accelerator.
	 *
	 * @param filename
	 *            The file to read the data from
	 * @param accelerator
	 *            The target acceleration structure
	 * @param shader
	 *            The shader which is used by all triangles
	 * @param scale
	 *            The scale factor which is responsible for scaling the model
	 * @param translate
	 *            A vector representing the translation coordinate with which
	 *            all coordinates have to be translated
	 * @throws IllegalArgumentException
	 *             If the filename is null or the empty string, the accelerator
	 *             is null, the shader is null, the translate vector is null,
	 *             the translate vector is not finite or scale does not
	 *             represent a legal (finite) floating point number
	 */
	public static void read(final String filename,
			final Accelerator accelerator, final Shader shader, final float scale,
			final Vec3 translate) throws FileNotFoundException {
		read(new BufferedInputStream(new FileInputStream(filename)), accelerator, shader, scale, translate);
	}


	/**
	 * Reads an OBJ file and uses the given shader for all triangles. While
	 * loading the triangles they are inserted into the given acceleration
	 * structure accelerator.
	 *
	 * @param in
	 *            The InputStream of the data to be read.
	 * @param accelerator
	 *            The target acceleration structure
	 * @param shader
	 *            The shader which is used by all triangles
	 * @param scale
	 *            The scale factor which is responsible for scaling the model
	 * @param translate
	 *            A vector representing the translation coordinate with which
	 *            all coordinates have to be translated
	 * @throws IllegalArgumentException
	 *             If the InputStream is null, the accelerator
	 *             is null, the shader is null, the translate vector is null,
	 *             the translate vector is not finite or scale does not
	 *             represent a legal (finite) floating point number
	 */
	public static void read(final InputStream in,
			final Accelerator accelerator, final Shader shader, final float scale,
			final Vec3 translate) {

		if(in == null)
			throw new IllegalArgumentException("Input stream is null.");
		if(accelerator == null)
			throw new IllegalArgumentException("Accelerator is null.");
		if(shader == null)
			throw new IllegalArgumentException("Shader is null.");
		if(translate == null)
			throw new IllegalArgumentException("Translate vector is null.");
		if(!translate.isFinite())
			throw new IllegalArgumentException("Translate vector is not finite.");
		if(!Float.isFinite(scale))
			throw new IllegalArgumentException("Scale is not finite.");
		if(Float.isNaN(scale))
			throw new IllegalArgumentException("Scale is NaN.");

		Scanner scanner = new Scanner(in);
		List<Point> points = new LinkedList<>();

		scanner.useLocale(Locale.ENGLISH);

		while(scanner.hasNext()) {
			String line = scanner.nextLine();

			if(line.length() < 7)
				continue;
			switch (line.charAt(0)) {
				case '#':
					continue;
				case 'v': {
					String[] split = line.substring(2).split(" ");

					if (split.length != 3)
						break;

					points.add(new Point(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2])).scale(scale).add(translate));
					break;
				}
				case 'f': {
					String[] split = line.substring(2).split(" ");

					if (split.length != 3)
						break;

					Primitive triangle = GeomFactory.createTriangle(points.get(Integer.parseInt(split[0]) - 1), points.get(Integer.parseInt(split[1]) - 1),
							points.get(Integer.parseInt(split[2]) - 1));

					accelerator.add(new StandardObj(triangle, shader));
					break;
				}
				default:
					break;
			}
		}

		scanner.close();
	}
}










