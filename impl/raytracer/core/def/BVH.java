package raytracer.core.def;

import java.util.ArrayList;
import java.util.List;

import raytracer.core.Hit;
import raytracer.core.Obj;
import raytracer.geom.BBox;
import raytracer.geom.Primitive;
import raytracer.math.Pair;
import raytracer.math.Point;
import raytracer.math.Ray;
import raytracer.math.Vec3;

/**
 * Represents a bounding volume hierarchy acceleration structure
 */
public class BVH extends BVHBase {
	private List<Obj> objects;

	public BVH() {
		objects = new ArrayList<>();
	}

	@Override
	public BBox bbox() {
		Point max = null;

		for(Obj obj : objects) {
			Point bboxMax = obj.bbox().getMax();
			if (max == null)
				max = bboxMax;
			else
				max = max.max(bboxMax);
		}

		return BBox.create(calculateMinMax().a, max);
	}

	/**
	 * Adds an object to the acceleration structure
	 *
	 * @param prim
	 *            The object to add
	 */
	@Override
	public void add(final Obj prim) {
		objects.add(prim);
	}

	/**
	 * Builds the actual bounding volume hierarchy
	 */
	@Override
	public void buildBVH() {
		//TODO: Implement
	}

	@Override
	public Pair<Point, Point> calculateMinMax() {
		Point min = null, max = null;

		for(Obj obj : objects) {
			Point bboxMin = obj.bbox().getMin();

			if(min == null)
				min = bboxMin;
			else
				min = min.min(bboxMin);

			if(max == null)
				max = bboxMin;
			else
				max = max.max(bboxMin);
		}

		return new Pair<>(min, max);
	}

	@Override
	public int calculateSplitDimension(final Vec3 size) {
		// TODO Implement this method
		throw new UnsupportedOperationException("This method has not yet been implemented.");
	}

	@Override
	public void distributeObjects(final BVHBase a, final BVHBase b, final int splitdim, final float splitpos) {
		// TODO Implement this method
		throw new UnsupportedOperationException("This method has not yet been implemented.");
	}

	@Override
	public Hit hit(final Ray ray, final Obj obj, final float tmin, final float tmax) {
		// TODO Implement this method
		throw new UnsupportedOperationException("This method has not yet been implemented.");
	}

	@Override
	public List<Obj> getObjects() {
		return objects;
	}
}
