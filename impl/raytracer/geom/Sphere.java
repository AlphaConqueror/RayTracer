package raytracer.geom;

import raytracer.core.Hit;
import raytracer.core.Obj;
import raytracer.core.def.LazyHitTest;
import raytracer.math.*;

public class Sphere extends BBoxedPrimitive {

    private final Point m;
    private final float rad;

    public Sphere(Point m, float r) {
        super(BBox.create(m.add(new Vec3(r, r ,r)), m.sub(new Vec3(r, r ,r))));
        this.m = m;
        this.rad = r;
    }

    @Override
    public Hit hitTest(Ray ray, Obj object, float tmin, float tmax) {
        return new LazyHitTest(object) {
            private Point point;
            private float r;

            @Override
            protected boolean calculateHit() {
                Vec3 dir = ray.dir(),
                     mBase = ray.base().sub(m);

                float b = 2 * dir.dot(mBase);
                float c = mBase.dot(mBase) - (float) Math.pow(rad, 2);

                if(Math.pow(b, 2) - 4 * c < 0)
                    return false;

                r = (-1 * b - (float) Math.sqrt(Math.pow(b, 2) - 4 * c))/2;

                return r >= Constants.EPS;
            }

            @Override
            public float getParameter() {
                return r;
            }

            @Override
            public Point getPoint() {
                if(point == null)
                    point = ray.eval(r);

                return point;
            }

            @Override
            public Vec3 getNormal() {
                return getPoint().sub(m).normalized();
            }

            @Override
            public Vec2 getUV() {
                return Util.computeSphereUV(getPoint().sub(m));
            }
        };
    }

    @Override
    public int hashCode() {
        return m.hashCode() ^ (int) rad;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;

        Sphere sphere = (Sphere) object;

        return m.equals(sphere.m) && rad == sphere.rad;
    }
}
