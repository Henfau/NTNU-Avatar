package Entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class CustomTexture {

    private int ID;

    public CustomTexture(String fileName){

        //Note: Currently, the file may have to be a png and MUST have RGBA formatting ( as opposed to just RGB )

        //File imageFile = new File("textures/"+fileName+".png");
        InputStream in = getClass().getResourceAsStream("textures/"+fileName+".png");
        //System.out.println(imageFile);


        try {
            BufferedImage image = ImageIO.read(in);
            flip(image);
            byte[] pixelBytes = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

            //The following converts an array of bytes with big endian ordering into an array of ints (i.e. 4 bytes to an int)
            //Praise be StackOverflow
            IntBuffer intBuf =
                    ByteBuffer.wrap(pixelBytes)
                            .order(ByteOrder.BIG_ENDIAN)
                            .asIntBuffer();
            int[] pixels = new int[intBuf.remaining()];
            intBuf.get(pixels);
            //--


            int ID = glGenTextures();
            this.ID = ID;
            glBindTexture(GL_TEXTURE_2D,ID);

            //This defines how the texture is wrapped. Unlikely to matter as of now.
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);	// set texture wrapping to GL_REPEAT (default wrapping method)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            //NB: See top of constructor
            glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,image.getWidth(),image.getHeight(),0,GL_RGBA,GL_UNSIGNED_BYTE,pixels);

            //Mipmaps are downsampled versions of textures, usually for improved performance. Unlikely to matter as of now.
            glGenerateMipmap(GL_TEXTURE_2D);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public int getID(){
        return ID;
    }

    public void flip(BufferedImage image)
    {
        for (int i=0;i<image.getWidth();i++)
            for (int j=0;j<image.getHeight()/2;j++)
            {
                int tmp = image.getRGB(i, j);
                image.setRGB(i, j, image.getRGB(i, image.getHeight()-j-1));
                image.setRGB(i, image.getHeight()-j-1, tmp);
            }
    }
    public BufferedImage horizontalBlur(BufferedImage original){
        BufferedImage newImage = new BufferedImage(original.getWidth(),original.getHeight(),original.getType());
        for(int i = 0;i<original.getWidth();i++){
            for(int j=0;j<original.getHeight();j++){

            }
        }

        return original;
    }

}
