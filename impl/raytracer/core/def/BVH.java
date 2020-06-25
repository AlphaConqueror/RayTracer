package raytracer.core.def;

import raytracer.core.Hit;
import raytracer.core.Obj;
import raytracer.geom.BBox;
import raytracer.math.Pair;
import raytracer.math.Point;
import raytracer.math.Ray;
import raytracer.math.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bounding volume hierarchy acceleration structure
 */
public class BVH extends BVHBase {
	private List<Obj> objects;
	private BBox bbox = null;

	public BVH() {
		objects = new ArrayList<>();
	}

	@Override
	public BBox bbox() {
		return bbox;
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
		Point max = null;

		for(Obj obj : objects) {
			Point bboxMax = obj.bbox().getMax();
			if (max == null)
				max = bboxMax;
			else
				max = max.max(bboxMax);
		}

		bbox =  BBox.create(calculateMinMax().a, max);
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
		float max = Math.max(size.x(), Math.max(size.y(), size.z()));

		return max == size.x() ? 0 : max == size.y() ? 1 : 2;
	}

	@Override
	public void distributeObjects(final BVHBase a, final BVHBase b, final int splitdim, final float splitpos) {
		for(Obj object : objects)
			if(object.bbox().getMin().get(splitdim) >= splitpos)
				a.add(object);
			else
				b.add(object);
	}

	@Override
	public Hit hit(final Ray ray, final Obj obj, final float tmin, final float tmax) {
		Hit hit = bbox.hit(ray, tmin, tmax);

		if(hit.hits()) {
			BBox a = BBox.EMPTY,
				 b = BBox.EMPTY;

			//distributeObjects(a, b, calculateSplitDimension());
		}

		return hit;
	}

	@Override
	public List<Obj> getObjects() {
		return objects;
	}
}
