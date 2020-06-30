package raytracer.geom;

import raytracer.core.Hit;
import raytracer.core.Obj;
import raytracer.core.def.LazyHitTest;
import raytracer.math.*;

public class Plane extends BBoxedPrimitive {

    private final Point m;
    private final Vec3 n;

    public Plane(Point a, Point b, Point c) {
        this.m = a;
        this.n = c.sub(a).cross(b.sub(a)).normalized();
    }

    public Plane(Vec3 n, Point supp) {
        this.m = supp;
        this.n = n.normalized();
    }

    @Override
    public Hit hitTest(Ray ray, Obj object, float tmin, float tmax) {
        return new LazyHitTest(object) {
            private Point point = null;
            private float r;

            @Override
            protected boolean calculateHit() {
                final Vec3 dir = ray.dir();

                if(Constants.isZero(dir.dot(n)))
                    return false;

                float d = m.dot(n),
                      l = (d - ray.base().dot(n))/dir.dot(n);

                r = l;

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
                return n;
            }

            @Override
            public Vec2 getUV() {
                return Util.computePlaneUV(n, m, getPoint());
            }
        };
    }

    @Override
    public int hashCode() {
        return m.hashCode() ^ n.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;

        Plane plane = (Plane) object;

        return m.equals(plane.m) && n.equals(plane.n);
    }
}
