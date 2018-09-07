package Utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Bezier {


    public static float[] generateBezierPointArray(int steps,Vector3f p0, Vector3f p1,Vector3f p2,Vector3f p3){

        float[] vertexArray = new float[6*(steps+1)];

        for(int i = 0;i<steps+1;i++){
            double t = (double)i/steps;
            Vector3f point = calculateBezierPoint(t,p0,p1,p2,p3);
            vertexArray[3*i] = point.x;
            vertexArray[3*i+1]=point.y;
            vertexArray[3*i+2]=point.z;

            vertexArray[3*(i+steps+1)] = (float)(2*t-1);
            vertexArray[3*(i+steps+1)+1] = 0f;
            vertexArray[3*(i+steps+1)+2] = 0f;
        }
        int L = vertexArray.length;

        vertexArray[L-3] = p3.x;
        vertexArray[L-2] = p3.y;
        vertexArray[L-1] = p3.z;

        return vertexArray;
    }

    public static Vector3f calculateBezierPoint(double t, Vector3f px0,Vector3f px1,Vector3f px2, Vector3f px3){

        // B(t) = (1-t)3P0 + 3(1-t)2tP1 + 3(1-t)t2P2 + t3P3 , 0 < t < 1

        Vector3f p0 = cloneVector(px0);
        Vector3f p1 = cloneVector(px1);
        Vector3f p2 = cloneVector(px2);
        Vector3f p3 = cloneVector(px3);
        Vector3f term1 = p0.mul((float)Math.pow(1-t,3));
        Vector3f term2 = p1.mul((float)((1-t)*(1-t)*t));
        Vector3f term3 = p2.mul((float)((1-t)*t*t));
        Vector3f term4 = p3.mul((float)(t*t*t));

        term1.add(term2);
        term1.add(term3);

        term1.add(term4);



        return term1;
    }
    public static Vector3f cloneVector(Vector3f original){
        Vector3f v = new Vector3f(original.x,original.y,original.z);

        return v;
    }

    public static void main(String[] args){
        Vector3f p0 = new Vector3f(0f,0f,0f);
        Vector3f p1 = new Vector3f(0f,1f,0f);
        Vector3f p2 = new Vector3f(1f,1f,0f);
        Vector3f p3 = new Vector3f(1f,0f,0f);

        float[] arr = generateBezierPointArray(10,p0,p1,p2,p3);
        for(int i = 0;i<arr.length;i++){
            System.out.println(arr[i]);
        }
    }

}
