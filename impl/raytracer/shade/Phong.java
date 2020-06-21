package raytracer.shade;

import raytracer.core.Hit;
import raytracer.core.LightSource;
import raytracer.core.Shader;
import raytracer.core.Trace;
import raytracer.core.def.StandardObj;
import raytracer.geom.GeomFactory;
import raytracer.geom.Primitive;
import raytracer.math.Color;
import raytracer.math.Ray;

public class Phong implements Shader {

    private final Shader inner;
    private final Color ambient;
    private final float diffuse, specular, shininess;

    public Phong(Shader inner, Color ambient, float diffuse, float specular, float shininess) {
        if(inner == null)
            throw new IllegalArgumentException("Inner shader is null.");
        if(ambient == null)
            throw new IllegalArgumentException("Ambient color is null.");

        if(diffuse < 0)
            throw new IllegalArgumentException("Diffuse is negative.");
        if(specular < 0)
            throw new IllegalArgumentException("Specular is negative.");
        if(shininess < 0)
            throw new IllegalArgumentException("shininess is negative.");

        if(Float.isInfinite(diffuse))
            throw new IllegalArgumentException("Diffuse is infinite.");
        if(Float.isInfinite(specular))
            throw new IllegalArgumentException("Specular is infinite.");
        if(Float.isInfinite(shininess))
            throw new IllegalArgumentException("Shininess is infinite.");

        if(Float.isNaN(diffuse))
            throw new IllegalArgumentException("Diffuse is not a number.");
        if(Float.isNaN(specular))
            throw new IllegalArgumentException("Specular is not a number.");
        if(Float.isNaN(shininess))
            throw new IllegalArgumentException("Shininess is not a number.");

        this.inner = inner;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    @Override
    public Color shade(Hit hit, Trace trace) {
        Color color = Color.BLACK;
        Color dColor = Color.BLACK,
              sColor = Color.BLACK;

        System.out.println("Light sources size = " + trace.getScene().getLightSources().size());

        for(LightSource lightSource : trace.getScene().getLightSources()) {
            boolean isHit = false;

            //Tests if another object has been hit by the light ray.
            for(Primitive obj : GeomFactory.getObjects()) {
                if(obj.hit(new Ray(trace.getRay().base(), trace.getRay().invDir()), new StandardObj(obj, new SingleColor(Color.BLACK)), 0, hit.getParameter()).hits()) {
                    isHit = true;
                    break;
                }
            }

            System.out.println("Is hit? " + isHit);

            if(!isHit)
                dColor.add(lightSource.getColor().mul(inner.shade(hit, trace)));

            sColor.add(lightSource.getColor());
        }

        System.out.println("Trace ray dir --> " + trace.getRay().dir().toString());

        System.out.println("max 0 nv --> " + Math.max(0, hit.getNormal().dot(trace.getRay().base().sub(trace.getRay().dir()))));
        System.out.println("max 0 rv --> " + Math.pow(Math.max(0, trace.getRay().reflect(hit.getPoint(), hit.getNormal()).dir().dot(trace.getRay().base().sub(trace.getRay().dir()))), shininess));

        dColor.scale(diffuse * Math.max(0, hit.getNormal().dot(trace.getRay().base().sub(trace.getRay().dir()))));
        sColor.scale(specular * (float) Math.pow(Math.max(0, trace.getRay().reflect(hit.getPoint(), hit.getNormal()).dir().dot(trace.getRay().base().sub(trace.getRay().dir()))), shininess));

        color.add(ambient).add(dColor).add(sColor);

        return new Color(color.x() <= 1 ? color.x() : 1, color.y() <= 1 ? color.y() : 1, color.z() <= 1 ? color.z() : 1);
    }
}
