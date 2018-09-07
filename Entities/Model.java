package Entities;

import Entities.CustomTexture;
import Entities.Entity;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private Shader shader;
    private List<Entity> entities;
    private CustomTexture texture;
    public boolean isTextured = false;

    public Model(Shader shader, CustomTexture texture){
        this.shader = shader;
        this.entities = new ArrayList<>();
        if (texture != null){
            this.isTextured = true;
            this.texture = texture;
        }
    }
    public Model(Shader shader){
        this(shader,null);
    }

    public int getUniformLocation(String name){
        return shader.getUniformLocation(name);
    }

    public void removeEntity(Entity entity){

        if(!entities.contains(entity)){
            throw new IllegalArgumentException("Tried to remove an entity that does not exist in this model");
        }

        entities.remove(entity);
    }
    public void addEntity(Entity entity){
        if(entities.contains(entity)){
            throw new IllegalArgumentException("Tried to add an entity that already exists in this model");
        }
        entities.add(entity);
    }

    public void use(){
        shader.use();
    }
    public int getTextureID(){
        return texture.getID();
    }
    public void setMatrix(int transformationLocation, Matrix4f tranformation){
        shader.setMatrix(transformationLocation,tranformation);
    }
    public List<Entity> getEntities(){
        return entities;
    }
    public Shader getShader(){
        return shader;
    }
}
