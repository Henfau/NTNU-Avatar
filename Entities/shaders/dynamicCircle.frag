#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D ourTexture;
uniform vec2 angles;
uniform vec2 amps;

void main()
{
    float x = gl_FragCoord.x-400;
    float y = gl_FragCoord.y-400;

    float FragAngle = (atan(y,x)+3.14);
    if(FragAngle < angles.x || FragAngle > angles.y)
        discard;

    float dist = angles.y-angles.x;

    float IF = (FragAngle-angles.x)/dist;

    float radius = IF*amps.x+(1-IF)*amps.y;

    float fragRadiusSquared = x*x+y*y;

    float fragRadius = sqrt(x*x+y*y);

    if(fragRadius>radius)
        discard;





    FragColor = vec4(1,0,0,1);
}
