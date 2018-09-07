package Entities;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;


public class Shader {

    String vShader;
    String fShader;

    int vertexID,fragmentID,ID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16); //Assuming 4x4 matrices

    public Shader(String vName,String fName){
        this.vShader = getStringFromFile(vName);
        this.fShader = getStringFromFile(fName);

        this.vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID,vShader);
        glCompileShader(vertexID);
        if (glGetShaderi(vertexID, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new RuntimeException("Vertex shader compilation failed");
        }

        this.fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID,fShader);
        glCompileShader(fragmentID);

        if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new RuntimeException("Fragment shader compilation failed");
        }

        this.ID = glCreateProgram();
        glAttachShader(ID,vertexID);
        glAttachShader(ID,fragmentID);
        glLinkProgram(ID);
        //TODO: Handle linking errors

        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }
    public void use(){
        glUseProgram(ID);
    }

    public void setFloat(String name, float num){
        glUniform1f(glGetUniformLocation(ID,name),num);
    }
    public void setBool(String name, boolean val){
        if(val){
            glUniform1i(glGetUniformLocation(ID,name),1);
        }
        else{
            glUniform1i(glGetUniformLocation(ID,name),0);
        }

    }
    public int getUniformLocation(String uniformName){
        return glGetUniformLocation(ID,uniformName);
    }
    public void setVector(String name,Vector2f vector){
        glUniform2f(glGetUniformLocation(ID,name),vector.x,vector.y);
    }
    public void setVector4f(String name,Vector4f vector){glUniform4f(glGetUniformLocation(ID,name),vector.x,vector.y,vector.z,vector.w);}

    public void setMatrix(int location, Matrix4f matrix){
        matrix.get(matrixBuffer);
        glUniformMatrix4fv(location,false,matrixBuffer);
    }
    public void setInt(String name, int num){
        glUniform1i(glGetUniformLocation(ID,name),num);
    }

    private String getStringFromFile(String name){
        /*
        String shaderpath = new File("Entities.shaders/"+name).getAbsolutePath();
        */
        String shader = "";


        InputStream in = getClass().getResourceAsStream("shaders/" +name);
        //System.out.println(getClass().getResource("shaders/"+name));
        //System.out.println(shaderpath);
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            shader = "";
            String line;
            while ((line = r.readLine())!= null){
                shader+=line+'\n';
            }


        } catch (FileNotFoundException e) {
            System.out.println("A shader file was not found");
            e.printStackTrace();
        }
        catch(IOException e){
            System.out.println("An error occurred while reading a shader file");
            e.printStackTrace();
        }
        return shader;
    }

}
