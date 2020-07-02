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
	private final List<Obj> objects, nodes;
	private BBox bbox = BBox.EMPTY;

	public BVH() {
		objects = new ArrayList<>();
		nodes = new ArrayList<>();
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
		bbox = BBox.surround(bbox, prim.bbox());
	}

	/**
	 * Builds the actual bounding volume hierarchy
	 */
	@Override
	public void buildBVH() {
		if(objects.size() > BVH.THRESHOLD) {
			BVH a = new BVH(),
				b = new BVH();

			Pair<Point, Point> minMax = calculateMinMax();
			int dimension = calculateSplitDimension(minMax.b.sub(minMax.a));

			distributeObjects(a, b, dimension, minMax.a.add(minMax.b.sub(minMax.a).scale(0.5f)).get(dimension));

			if (a.getObjects().size() != objects.size() && b.getObjects().size() != objects.size()) {
				nodes.add(a);
				nodes.add(b);

				a.buildBVH();
				b.buildBVH();
			} else
				for(Obj object : objects)
					nodes.add(object);
		} else {
			for(Obj object : objects)
				nodes.add(object);
		}
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
		for(Obj object : objects) {
			if (object.bbox().getMin().get(splitdim) < splitpos)
				a.add(object);
			else
				b.add(object);
		}
	}

	@Override
	public Hit hit(final Ray ray, final Obj obj, final float tmin, final float tmax) {
		Hit hit = null;

		if(bbox.hit(ray, tmin, tmax).hits()) {
			for(Obj node : nodes) {
				Hit nodeHit = node.hit(ray, node, tmin, tmax);

				if(nodeHit.hits()) {
					if(hit == null || nodeHit.getParameter() < hit.getParameter())
						hit = nodeHit;
				}
			}
		}

		if(hit != null)
			return hit;

		return Hit.No.get();
	}

	@Override
	public List<Obj> getObjects() {
		return objects;
	}
}
