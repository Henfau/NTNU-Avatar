package Entities;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FBO {

    public int ID;
    private int texture;
    private int t = 0;
    private Shader shader;


    public FBO(Shader shader,int WIDTH,int HEIGHT){
        this.ID = glGenFramebuffers();

        glBindFramebuffer(GL_FRAMEBUFFER,ID);

        this.texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D,texture);
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,WIDTH,HEIGHT,0,GL_RGB,GL_UNSIGNED_BYTE,NULL);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);

        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,texture,0);

        int rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER,rbo);
        glRenderbufferStorage(GL_RENDERBUFFER,GL_DEPTH24_STENCIL8,WIDTH,HEIGHT);
        glBindRenderbuffer(GL_RENDERBUFFER,0);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_STENCIL_ATTACHMENT,GL_RENDERBUFFER,rbo);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("Framebuffer not complete");


        glBindFramebuffer(GL_FRAMEBUFFER,0);

        this.shader = shader;
    }

    public void start(){
        glBindFramebuffer(GL_FRAMEBUFFER,ID);
        glClearColor(0f, 80/256f, 158/256f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glClear(GL_DEPTH_BUFFER_BIT);
    }
    public void renderTo(int target){
        glBindFramebuffer(GL_FRAMEBUFFER,target);
        //glClearColor(0.3f,0.5f,0.5f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        shader.use();
        shader.setFloat("offset", 1f/(500+t*t));
        glBindTexture(GL_TEXTURE_2D,texture);
        //glDrawArrays(GL_TRIANGLES,0,12);
        glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);
    }
    public void renderTo(FBO target){
        renderTo(target.ID);
    }
    public void setT(int t){
        this.t = t;
    }
}
