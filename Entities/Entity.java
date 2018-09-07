package Entities;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Entity {

    private static Matrix4f defaultMatrix = new Matrix4f();
    private static int SMOOTHING_FACTOR = 5;
    private List<Float> inputValues = new ArrayList<>();
    private float amp;
    public char state = 'a';

    public Model model;
    public Matrix4f transformation;
    private int transformationLocation;

    float x,y,z;
    float xScale,yScale,zScale;


    public Entity(Model model){
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;

        this.xScale = 1.0f;
        this.yScale = 1.0f;
        this.zScale = 1.0f;

        this.model = model;
        this.transformationLocation = model.getUniformLocation("transform");
        this.transformation = new Matrix4f();
        model.addEntity(this);
    }

    public void render(){
        if(model.isTextured) {
            glBindTexture(GL_TEXTURE_2D, model.getTextureID());
        }
        model.setMatrix(transformationLocation,transformation);

        if (this instanceof BezierCurveEntity) {
            glDrawElements(GL_TRIANGLES, 120, GL_UNSIGNED_INT, 0);
        }
        else
            glDrawElements(GL_TRIANGLES,12,GL_UNSIGNED_INT,0);
    }

    public void setYScale(float newYScale){
        this.transformation.scale(1.0f,newYScale/yScale,1.0f);
        yScale = newYScale;
    }

    public void setY(){
        float sum = 0;
        for(float n: inputValues){
            sum+=n;
        }
        setY(sum/SMOOTHING_FACTOR);
    }

    public void setY(float newY){
        this.transformation.translate(0f,newY-y,0f);
        y= newY;
    }
    public float setAmp(float inputvalue){
        inputValues.add(inputvalue);
        if(inputValues.size()>=SMOOTHING_FACTOR){
            inputValues.remove(0);
        }
        float sum = 0;
        for(float n: inputValues){
            sum+=n;
        }

        return sum/SMOOTHING_FACTOR;
    }
    public float getY(){
        return y;
    }

}
