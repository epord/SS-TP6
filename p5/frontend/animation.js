
var file;
var time_checkpoint;
var frames;
var frames_state = [];
var canvas_size = 800;
var world_size = 22;
var walls = [];


function preload() {
    file = loadStrings('output.txt');
}

function setup() {
    frameRate(60);
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
    for (let i = 0; i < frame.length; i += 3) {
        particles.push({x: parseFloat(frame[i]),
            y: parseFloat(frame[i+1]),
            r: parseFloat(frame[i+2]),
        });
    }
    return particles;
}

var current_frame = 0;
var frame_skipping = 0; // amount of frames to skip
function draw() {
    background(24);

    drawParticles(current_frame);
    drawWalls();
    drawFrameRate();

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
    var c = color(255, 0, 0, 127);
    fill(c);
    ellipse(world2canvas(particle.x + 1), canvas_size-world2canvas(particle.y + 1), world2canvas(particle.r * 2));

    var c = color(255, 0, 0);
    fill(c);
    ellipse(world2canvas(particle.x + 1), canvas_size-world2canvas(particle.y + 1), world2canvas(0.2 * 2));
};

drawParticles = frame => {
    frames_state[frame].forEach(p => {
        drawParticle(p)
    })
};

drawFrameRate = () => {
    var fps = frameRate();
    fill(255);
    stroke(0);
    text("FPS: " + fps.toFixed(2), 10, height - 10);
}

function world2canvas(value) {
    return value * canvas_size / world_size;
}