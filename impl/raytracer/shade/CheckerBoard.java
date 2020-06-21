package raytracer.shade;

import raytracer.core.Hit;
import raytracer.core.Shader;
import raytracer.core.Trace;
import raytracer.math.Color;
import raytracer.math.Constants;
import raytracer.math.Vec2;

public class CheckerBoard implements Shader {

    private final Shader a, b;
    private final float scale;

    public CheckerBoard(Shader a, Shader b, float scale) {
        if(a == null)
            throw new IllegalArgumentException("Shader a is null");
        if(b == null)
            throw new IllegalArgumentException("Shader b is null");
        if(Constants.isZero(scale))
            throw new UnsupportedOperationException("The scale is equal to zero.");
        else if(scale < 0)
            throw new IllegalArgumentException("The scale is negative.");
        else if(Float.isInfinite(scale))
            throw new IllegalArgumentException("The scale is not finite.");
        else if(Float.isNaN(scale))
            throw new IllegalArgumentException("The scale is not a number.");

        this.a = a;
        this.b = b;
        this.scale = scale;
    }

    @Override
    public Color shade(Hit hit, Trace trace) {
        Vec2 uv = hit.getUV();
        float x = (uv.x() + uv.y())/scale;

        return x % 2 == 0 ? a.shade(hit, trace) : b.shade(hit, trace);
    }
}
