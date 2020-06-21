package raytracer.geom;

import raytracer.core.Hit;
import raytracer.core.Obj;
import raytracer.core.def.LazyHitTest;
import raytracer.math.*;

public class Sphere extends BBoxedPrimitive {

    private final Point m;
    private final float rad;

    public Sphere(Point m, float r) {
        this.m = m;
        this.rad = r;
    }

    @Override
    public Hit hitTest(Ray ray, Obj object, float tmin, float tmax) {
        return new LazyHitTest(object) {
            private Point point;
            private float r = 0;

            @Override
            protected boolean calculateHit() {
                Vec3 dir = ray.dir();

                float b = 2 * dir.dot(ray.base().sub(m));
                float c = (float) (ray.base().sub(m).dot(ray.base().sub(m)) - Math.pow(rad, 2));

                if(Math.pow(b, 2) - 4 * c < 0)
                    return false;

                float sqrt = (float) Math.sqrt(Math.pow(b, 2) - 4 * c);
                float lPos = (-1 * b + sqrt)/2,
                      lNeg = (-1 * b - sqrt)/2;

                if(Math.max(lPos, lNeg) < -1 * Constants.EPS)
                    return true;

                if(tmin <= lPos && lPos <= tmax)
                    r = lPos;
                if(lNeg < lPos && tmin <= lPos && lPos <= tmax)
                    r = lNeg;

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
                return ray.base().sub(m).normalized();
            }

            @Override
            public Vec2 getUV() {
                return Util.computeSphereUV(getPoint().sub(m));
            }
        };
    }

    @Override
    public int hashCode() {
        //TODO: Check
        return m.hashCode();
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
