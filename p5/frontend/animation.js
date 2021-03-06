
var file;
var time_checkpoint;
var frames;
var frames_state = [];
var canvas_size = 800;
var world_size = 22;
var walls = [];
var fps = 60;


function preload() {
    file = loadStrings('output.txt');
}

function setup() {
    frameRate(fps);
    frames = parseInt(file.length-1);
    createCanvas(canvas_size, canvas_size);

    readWalls(file[0].trim());
    readFrames(frames);

    time_checkpoint = millis();
}

readWalls = serialized => {
    serialized = serialized.split(" ");
    for (let i = 0; i < serialized.length; i += 4) {
        walls.push({startX: parseFloat(serialized[i]), startY: parseFloat(serialized[i+1]),
            endX: parseFloat(serialized[i+2]), endY: parseFloat(serialized[i+3])});
    }
}

readFrames = () => {
    for (let i = 1; i <= frames; i++) {
        frames_state.push(getParticlesFromFrame(file[i].trim()))
    }
}

getParticlesFromFrame = frame => {
    frame = frame.split(" ");
    particles = [];
    for (let i = 0; i < frame.length; i += 4) {
        particles.push({
            id: parseInt(frame[i]),
            x: parseFloat(frame[i+1]),
            y: parseFloat(frame[i+2]),
            r: parseFloat(frame[i+3]),
        });
    }
    return particles;
}

var current_frame = 0;
var frame_skipping = 1; // amount of frames to skip
function draw() {
    background(24);

    drawParticles(current_frame);
    drawWalls();
    drawFrameRate();
    // drawTime();

    current_frame = (current_frame + (1 + frame_skipping)) % frames;
}

drawWalls = () => {
    for (let i = 0; i < walls.length; i++) {
        var c = color(255, 255, 255);
        stroke(c);
        line(world2canvas(walls[i].startX + 1), canvas_size-world2canvas(walls[i].startY + 1),
            world2canvas(walls[i].endX + 1), canvas_size-world2canvas(walls[i].endY + 1));
    }
};

drawParticle = particle => {
    var c = color(255, 0, 0, 65);
    fill(c);
    noStroke();
    ellipse(world2canvas(particle.x + 1), canvas_size-world2canvas(particle.y + 1), world2canvas(particle.r * 2));

    var c = color(255, 0, 0);
    fill(c);
    stroke(0)
    strokeWeight(1);
    if (particle.id >= 0) {
        ellipse(world2canvas(particle.x + 1), canvas_size-world2canvas(particle.y + 1), world2canvas(0.15 * 2));
    } else {
        ellipse(world2canvas(particle.x + 1), canvas_size-world2canvas(particle.y + 1), world2canvas(0.05 * 2));
    }
}

drawParticles = frame => {
    frames_state[frame].forEach(p => {
        drawParticle(p);
    })
}

drawFrameRate = () => {
    var fps = frameRate();
    fill(255);
    stroke(0);
    text("FPS: " + fps.toFixed(2), 10, height - 10);
}

drawTime = () => {
    fill(255);
    stroke(0);
    text((current_frame/fps).toFixed(2) + " s", width - 100, height - 10);
}

function world2canvas(value) {
    return value * canvas_size / world_size;
}