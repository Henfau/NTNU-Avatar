#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D ourTexture;
uniform float opacity;

void main()
{
    float red = 0;
    float green = 0;
    float blue = 0;
    float alpha = 0;



    vec4 color = texture(ourTexture, TexCoord);

    float Alpha = min(opacity,color.a);

    FragColor = vec4(color.r,color.g,color.b,color.a);

}
