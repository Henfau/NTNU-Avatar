
import Audio.AudioManager;
import Entities.*;
import Utils.Bezier;
import Utils.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Vector;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ModernIntro {

    private boolean fullscreen;
    private int HEIGHT = 1080;
    private int WIDTH = 1920;
    private long window;
    private float scale = 1.0f;

    private boolean SPACEBAR_PRESSED = false;
    private boolean started = false;
    private boolean forward = true;

    private void run() {
        init();
        loop();
    }

    private void loop() {
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));


        float[] squareVertices = {
                //positions            //texture
                1f, 1f, 0.0f, 1.0f, 1.0f,
                1f, -1f, 0.0f, 1.0f, 0.0f,
                -1f, -1f, 0.0f, 0.0f, 0.0f,
                -1f, 1f, 0.0f, 0.0f, 1.0f,
        };
        int[] squareIndices = {
                0, 1, 3,
                1, 2, 3
        };

        Vector3f p0 = new Vector3f(-1f, 0f, 0f);
        Vector3f p1 = new Vector3f(-0.5f, 1f, 0f);
        Vector3f p2 = new Vector3f(0.5f, 1f, 0f);
        Vector3f p3 = new Vector3f(1f, 0f, 0f);
        int steps = 20;
        float[] polygonVertices = Bezier.generateBezierPointArray(steps, p0, p1, p2, p3);
        int[] polygonIndices = new int[steps * 6];

        for(int i = 0;i<steps*3;i+=3){
            polygonIndices[i] = i/3;
            polygonIndices[i+1] = i/3+1;
            polygonIndices[i+2] = steps+i/3+1;
        }

        for(int i = steps*3;i<steps*6;i+=3){
            polygonIndices[i] = i/3+1;
            polygonIndices[i+1]=i/3+2;
            polygonIndices[i+2]=i/3-steps+1;
        }

        int EBO = glGenBuffers();
        int VBO = glGenBuffers();
        int VAO = glGenVertexArrays();

        int VAO2 = glGenVertexArrays();
        int VBO2 = glGenBuffers();
        int EBO2 = glGenBuffers();


        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, polygonVertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, polygonIndices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 3, 0);
        glEnableVertexAttribArray(0);


        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        Shader bezierShader = new Shader("bezier.vert", "basicColor.frag");
        Shader textureShader = new Shader("shader.vert", "shader.frag");
        CustomTexture logoTexture = new CustomTexture("logo2fin");

        Model bezier = new Model(bezierShader, null);
        Model logoTextureModel = new Model(textureShader, logoTexture);

        Vector4f red = new Vector4f(1.0f, 0.0f, 0.0f, 0.5f);
        Vector4f blue = new Vector4f(0.0f, (float)80/256, (float)158/256, 0.5f);
        Vector4f green = new Vector4f(0.0f,1.0f,0.0f,0.5f);
        Vector4f white = new Vector4f(1.0f,1f,1f,0.5f);


        AudioManager aud = new AudioManager();
        int k = 150;
        for(int i = 0;i<k;i++){


            BezierCurveEntity e = new BezierCurveEntity(bezier,aud,20,i+3,white);

            e.transformation.scale(0.15f,0.15f,1f);
            e.transformation.scale(scale,scale,1f);
            e.transformation.scale((float)HEIGHT/WIDTH,1f,1f);
            e.transformation.rotateZ((float)Math.PI*2*(float)i/k);
            e.transformation.translate(0f,-2.5f,0f);

            BezierCurveEntity f = new BezierCurveEntity(bezier,aud,20,i+3,white);
            f.transformation.scale(0.15f,0.15f,1f);
            f.transformation.scale(scale,scale,1f);
            f.transformation.scale((float)HEIGHT/WIDTH,1f,1f);
            f.transformation.rotateZ((float)-Math.PI*2*(float)i/k);
            f.transformation.translate(0f,-2.5f,0f);
        }

        for(int i = 0;i<k;i++){


        }


        //Logo states
        Matrix4f start = new Matrix4f();
        start.scale(9f/16,1f,1f);
        start.translate(-1.44f,0.2f,0f);
        start.scale(0.52f,0.52f,1f);


        Matrix4f end = new Matrix4f();

        end.scale(0.25f,0.25f,1f);
        Entity z = new Entity(logoTextureModel);
        z.transformation = end;
        z.transformation.scale(scale,scale,1f);
        z.transformation.scale((float)HEIGHT/WIDTH,1f,1f);


        Shader horizontalBlurShader = new Shader("fbo.vert","horizontalBlur.frag");
        FBO blur = new FBO(horizontalBlurShader,WIDTH,HEIGHT);
        FBO blur2 = new FBO(horizontalBlurShader,WIDTH,HEIGHT);
        Shader verticalBlurShader = new Shader("fbo.vert","verticalBlur.frag");
        FBO blur3 = new FBO(verticalBlurShader,WIDTH,HEIGHT);
        FBO blur4 = new FBO(verticalBlurShader,WIDTH,HEIGHT);

        logoTextureModel.getShader().setFloat("opacity",1f);
        boolean wasPressed;
        int i = 0;
        int t = 0;
        while (!glfwWindowShouldClose(window)) {
            i++;
            //input
            wasPressed = SPACEBAR_PRESSED;
            processInput();
            //update phase
            aud.update();

            //render phase


            blur.setT(t);
            blur.start();

            glBindVertexArray(VAO);

            glBindBuffer(GL_ARRAY_BUFFER,VBO);
            glBufferData(GL_ARRAY_BUFFER,polygonVertices,GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,EBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER,polygonIndices,GL_STATIC_DRAW);

            glVertexAttribPointer(0,3,GL_FLOAT,false,Float.BYTES*3,0);
            glEnableVertexAttribArray(0);



            bezier.use();

            for(int x = 0;x<k;x++){
                BezierCurveEntity e = (BezierCurveEntity) bezier.getEntities().get(x);
                float funca = (float)(Math.sin((double)(i+x*5)/70));
                float funcb = (float)(Math.sin((double)(-i+x*3)/120));
                float func = funca+funcb;
                float circleopacity = (float)(t+20-2*sqrt(t))/100/4;
                e.color.w=circleopacity;
                //e.setAmp(func*80000000);
                e.transformation.translate(0f,(float)(100-t)*2/100,0f);
                float value = ((float)(100-t)/100)*func*15000000+((float)(t)/100*e.update());



                e.setAmp(value);
                e.render();
            }


            glBindVertexArray(VAO2);

            glBindBuffer(GL_ARRAY_BUFFER, VBO2);
            glBufferData(GL_ARRAY_BUFFER, squareVertices, GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO2);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, squareIndices, GL_STATIC_DRAW);

            glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 5, 0);
            glEnableVertexAttribArray(0);

            glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, Float.BYTES * 3);
            glEnableVertexAttribArray(1);

            logoTextureModel.use();

            z.render();


            blur2.start();
            blur.renderTo(blur2);
            blur2.setT(t);
            blur2.renderTo(blur3);
            blur3.setT(t);
            blur3.renderTo(blur4);


            blur4.setT(t);
            blur4.renderTo(0);

            for(Entity e: bezier.getEntities()){
                BezierCurveEntity b = (BezierCurveEntity) e;
                b.transformation.translate(0f,(float)-(100-t)*2/100,0f);
            }


            if(SPACEBAR_PRESSED&&!started){
                started = true;

            }

            if(started){
                if(forward)
                    t++;
                else
                    t--;
                if(t==0){
                    started = false;
                    forward = true;
                }
                else if(t==100){
                    started = false;
                    forward = false;
                }
            }




            //swap buffers
            glfwSwapBuffers(window);
            glClear(GL_COLOR_BUFFER_BIT);
            glfwPollEvents();
        }
        glDeleteBuffers(VAO);
        glDeleteBuffers(VAO2);
        glDeleteBuffers(VBO);
        glDeleteBuffers(VBO2);
        glDeleteTextures(logoTexture.getID());
        glfwTerminate();

        aud.close();
        System.gc();
    }



    private void processInput() {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(window, true);
        if (glfwGetKey(window,GLFW_KEY_SPACE) == GLFW_PRESS){
            SPACEBAR_PRESSED = true;
        }
        else
            SPACEBAR_PRESSED = false;

    }

    private void init() {

        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new RuntimeException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);



        if (fullscreen) {
            window = glfwCreateWindow(WIDTH, HEIGHT, "A_window", glfwGetPrimaryMonitor(), NULL);
        }
        else {
            window = glfwCreateWindow(WIDTH, HEIGHT, "A_window", NULL, NULL);
        }

        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create GLFW Window");
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*


            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );



        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        GL.createCapabilities();
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
        glViewport(0,0,WIDTH,HEIGHT);
        //glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        PointerBuffer b = glfwGetMonitors();


        //glfwSetWindowMonitor(window,glfwGetPrimaryMonitor(),0,0,1920,1080,60);
    }
    public static void reload(Entity e,String texturename){
        Model model = e.model;
        Shader shader = model.getShader();
        Matrix4f m = e.transformation;
        CustomTexture texture = new CustomTexture(texturename);

        Model newModel = new Model(shader,texture);
        Entity newEntity = new Entity(newModel);
        newEntity.transformation = m;
        e = newEntity;

    }

    public static void main(String args[]) {

        ModernIntro intro = new ModernIntro();
        intro.fullscreen = args[0].equals("true");

        intro.WIDTH = Integer.parseInt(args[1]);
        intro.HEIGHT = Integer.parseInt(args[2]);
        intro.scale = Float.parseFloat(args[3]);


        intro.run();
    }
}
