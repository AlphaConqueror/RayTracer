package raytracer.core.def;

import raytracer.core.Hit;
import raytracer.core.Obj;
import raytracer.geom.BBox;
import raytracer.geom.GeomFactory;
import raytracer.math.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bounding volume hierarchy acceleration structure
 */
public class BVH extends BVHBase {
	private final List<Obj> objects;
	private BBox bbox = null;
	private BVH a, b;
	private boolean builtChildren = false;

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
		buildBBox();
	}

	public void buildBBox() {
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

	/**
	 * Builds the actual bounding volume hierarchy
	 */
	@Override
	public void buildBVH() {
		if(objects.size() > 4) {
			a = new BVH();
			b = new BVH();

			Pair<Point, Point> minMax = calculateMinMax();
			int dimension = calculateSplitDimension(minMax.b.sub(minMax.a));

			distributeObjects(a, b, dimension, minMax.a.add(minMax.b.sub(minMax.a).scale(0.5f)).get(dimension));
			builtChildren = true;

			if (a.getObjects().size() == objects.size() || b.getObjects().size() == objects.size()) {
				a = null;
				b = null;
			} else {
				a.buildBVH();
				b.buildBVH();
			}
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
		Hit hit = bbox.hit(ray, tmin, tmax);

		if(!builtChildren) {
			System.out.println("building bvh");
			buildBVH();
		}

		if(hit.hits()) {
			if(objects.size() <= 4) {
				for(Obj object : objects) {
					Hit objectHit = object.hit(ray, obj, tmin, tmax);

					if(objectHit.hits())
						return objectHit;
				}
			} else {
				if (a != null && b != null) {
					Hit aHit = a.hit(ray, obj, tmin, tmax);

					if (aHit.hits())
						return aHit;

					return b.hit(ray, obj, tmin, tmax);
				} else if(a == null && b != null)
					return b.hit(ray, obj, tmin, tmax);
				else if(b == null && a != null)
					return a.hit(ray, obj, tmin, tmax);
			}
		}

		return new Hit() {
			@Override
			public boolean hits() {
				return false;
			}

			@Override
			public float getParameter() {
				return 0;
			}

			@Override
			public Point getPoint() {
				return null;
			}

			@Override
			public Vec3 getNormal() {
				return null;
			}

			@Override
			public Vec2 getUV() {
				return null;
			}

			@Override
			public Obj get() {
				return null;
			}
		};
	}

	@Override
	public List<Obj> getObjects() {
		return objects;
	}
}
