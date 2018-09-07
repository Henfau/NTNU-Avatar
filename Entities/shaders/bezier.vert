#version 330 core
layout (location = 0) in vec3 aPos;

out vec2 TexCoord;

uniform mat4 transform;

void main()
{
    float y = sqrt(24-aPos.x*aPos.x)+aPos.y;
    //float y = aPos.y;
    gl_Position = transform * vec4(aPos.x,y,aPos.z, 1.0f);
}

vec2 b3_mix( in vec2 p0, in vec2 p1,
         in vec2 p2, in vec2 p3,
 in float t )
{
   vec2 q0 = mix(p0, p1, t);
   vec2 q1 = mix(p1, p2, t);
   vec2 q2 = mix(p2, p3, t);

   vec2 r0 = mix(q0, q1, t);
   vec2 r1 = mix(q1, q2, t);

   return mix(r0, r1, t);
}