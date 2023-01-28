// Based on https://www.shadertoy.com/view/lsBfRc#

#define PI 3.14159265359
#define TWO_PI 6.28318530718

#define colorRange 24.0

varying vec2 v_texCoords;

uniform vec2 u_resolution;

float getSquare(vec2 p, vec2 rp){
    p *= vec2(u_resolution.x, u_resolution.y);
    p /= max(u_resolution.x, u_resolution.y);

    p += rp;
    vec2 bl = step(abs(p * 2.0 - 1.0),vec2(0.2));
    float rt = bl.x * bl.y;

	return rt;
}

float getCircle(vec2 p, vec2 rp){
	p *= vec2(u_resolution.x, u_resolution.y);
    p /= max(u_resolution.x, u_resolution.y);

    return step(distance(p, rp), 0.1);
}

float getTriangle(vec2 p, vec2 rp){
    p *= vec2(u_resolution.x, u_resolution.y);
    p /= max(u_resolution.x, u_resolution.y);

    p -= rp;

    vec3 color = vec3(0.0);
    float d = 0.0;

    // Remap the space to -1. to 1.
    p = p *2.-1.;

    // Number of sides of your shape
    int N = 3;

    // Angle and radius from the current pixel
    float a = atan(p.x,p.y)+PI;
    float r = TWO_PI/float(N);

    // Shaping function that modulate the distance
    d = cos(floor(.5+a/r)*r-a)*length(p);

    return 1.0-step(.12,d);
}

float getRectangle(vec2 position, vec2 scale){
    scale = vec2(0.5) - scale * 0.5;

    vec2 shaper = vec2(step(scale.x, position.x), step(scale.y, position.y));
    shaper *= vec2(step(scale.x, 1.0 - position.x), step(scale.y, 1.0 - position.y));
    return shaper.x * shaper.y;
}

void main() {
    vec2 uv = v_texCoords;

    vec3 rectColor = vec3(2.0, 12.0, 30.0);
    float brightness = 1.1;

    vec3 color = getRectangle(uv, vec2(0.3, 0.3)) * rectColor * brightness;

    gl_FragColor = vec4(pow(color, vec3(1.0 / 2.2)) / colorRange,1.0);
}
