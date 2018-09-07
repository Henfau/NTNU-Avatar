package Entities;

import Audio.AudioManager;
import Utils.Bezier;
import Utils.Maths;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferSubData;

public class BezierCurveEntity extends Entity {

    private int frequencyslot;
    private int steps;
    private AudioManager audioManager;

    private Vector3f p0 = new Vector3f(-1f,0f,0f);
    private Vector3f p1;
    private Vector3f p2;
    private Vector3f p3 = new Vector3f(1f,0f,0f);

    public Vector4f color;

    public BezierCurveEntity(Model model, AudioManager audioManager, int steps, int frequencyslot,Vector4f color) {
        super(model);
        this.frequencyslot = frequencyslot;
        this.steps = steps;
        this.audioManager = audioManager;
        this.color = color;
    }

    @Override
    public void render(){
        //steps

        float[] polygonVertices = Bezier.generateBezierPointArray(steps, p0, p1, p2, p3);

        model.getShader().setVector4f("Color", color);
        glBufferSubData(GL_ARRAY_BUFFER,0,polygonVertices);
        super.render();
    }

    @Override
    public float setAmp(float inputValue){
        float amp;
        if(inputValue<0){
            amp = -abs(super.setAmp(Maths.waveFunction(-inputValue)*5));

        }
        else {
            amp = super.setAmp(Maths.waveFunction((inputValue)) * 5);
        }
        //p0 = new Vector3f(-1f,amp,0f);
        p1 = new Vector3f(-0.5f,5*amp,0f);
        p2 = new Vector3f(0.5f,5*amp,0f);
        //p3 = new Vector3f(1f,amp,0f);
        return amp;
    }
    public float update(){
        double value = audioManager.getData()[frequencyslot];
        //setAmp((float) value);
        return max((float)value,500000);
    }
}
