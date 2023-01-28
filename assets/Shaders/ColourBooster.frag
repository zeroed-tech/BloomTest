#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture0;

#define colorRange 24.0
void main() {
    vec3 color = texture(u_texture0, v_texCoords).rgb * vec3(2.0, 12.0, 30.0) * 2.0;
    gl_FragColor = vec4(pow(color, vec3(1.0 / 2.2)) / colorRange,1.0);
}
