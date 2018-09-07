package Utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Maths {

    public static float waveFunction(double input) {
        double k = input / 100000000;
        return (float) (1 - Math.exp(-k));
    }

    public static void main(String[] args){

        Vector4f vec = new Vector4f(1.0f,1.0f,1.0f,1.0f);
        Matrix4f trans = new Matrix4f();
        trans.translate(2.0f,0.0f,0.0f)
                .scale(0.5f)
                .rotateY((float) Math.toRadians(90.0f));

        vec = trans.transform(vec);


        System.out.println(vec);
    }
}
