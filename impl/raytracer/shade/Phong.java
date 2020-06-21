package raytracer.shade;

import raytracer.core.Hit;
import raytracer.core.Shader;
import raytracer.core.Trace;
import raytracer.math.Color;

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
        return null;
    }
}
