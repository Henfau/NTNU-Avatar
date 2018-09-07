#version 330 core
out vec4 FragColor;

in vec2 TexCoords;

uniform sampler2D screenTexture;

uniform float offset;

void main()
{
    vec2 offsets[9] = vec2[](
        vec2(-4*offset,  0.0f), // top-left
        vec2(-3*offset,    0.0f), // top-center
        vec2(-2*offset,  0.0f), // top-right
        vec2(-offset,  0.0f),   // center-left
        vec2( 0.0f,    0.0f),   // center-center
        vec2( offset,  0.0f),   // center-right
        vec2( 2*offset, 0.0f), // bottom-left
        vec2( 3*offset,   0.0f), // bottom-center
        vec2( 4*offset, 0.0f)  // bottom-right
    );

float kernel[9] = float[](
    0.081812	,0.101701	,0.118804	,0.130417	,0.134535	,0.130417	,0.118804	,0.101701,	0.081812
);

    vec3 sampleTex[9];
    vec3 colSample[9];
    for(int i = 0; i < 9; i++)
    {
        sampleTex[i] = vec3(texture(screenTexture, TexCoords.st + offsets[i]));
        colSample[i] = vec3(texture(screenTexture, TexCoords.st + vec2(offsets[i].y,offsets[i].x)));
    }
    vec3 col = vec3(0.0);
    for(int i = 0; i < 9; i++){
        col += sampleTex[i] * kernel[i];
    }

    FragColor = vec4(col, 1.0);
}