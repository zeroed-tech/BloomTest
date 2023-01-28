#ifdef GL_ES
precision highp float;
#endif

#define colorRange 24.0

// Tex0 is the original image
uniform sampler2D u_texture0;
// Tex1 is the bloom texture
uniform sampler2D u_texture1;
uniform vec2 u_resolution;

// In variables from vertex shader
varying vec4 v_color;
varying vec2 v_texCoords;

vec3 jodieReinhardTonemap(vec3 c){
    float l = dot(c, vec3(0.2126, 0.7152, 0.0722));
    vec3 tc = c / (c + 1.0);

    return mix(c / (l + 1.0), tc, tc);
}

vec3 bloomTile(float lod, vec2 offset, vec2 uv){
    return texture(u_texture1, uv * exp2(-lod) + offset).rgb;
}

vec3 getBloom(vec2 uv){

    vec3 blur = vec3(0.0);

    blur = pow(bloomTile(2., vec2(0.0,0.0), uv),vec3(2.2))        + blur;
    blur = pow(bloomTile(3., vec2(0.3,0.0), uv),vec3(2.2)) * 1.3  + blur;
    blur = pow(bloomTile(4., vec2(0.0,0.3), uv),vec3(2.2)) * 1.6  + blur;
    blur = pow(bloomTile(5., vec2(0.1,0.3), uv),vec3(2.2)) * 1.9  + blur;
    blur = pow(bloomTile(6., vec2(0.2,0.3), uv),vec3(2.2)) * 2.2  + blur;

    return blur * colorRange;
}

void main() {
    vec2 uv = v_texCoords;

    vec3 color = pow(texture(u_texture0, uv).rgb * colorRange, vec3(2.2));
    color = pow(color, vec3(2.2));

    color += pow(getBloom(uv), vec3(2.2));
    color = pow(color, vec3(1.0 / 2.2));

    color = jodieReinhardTonemap(color);

    gl_FragColor = vec4(color,1.0);
}
