// Initialize the Object Detector module
let objectDetector;

// Holds the image we want to run object detection on
let img;

function preload() {
    //objectDetector = ml5.objectDetector('cocossd');
    objectDetector = ml5.objectDetector('yolo');

    //img = loadImage('https://i.imgur.com/Mzh4cHR.jpg');
    img = loadImage('./car_1.jpg');
}

function setup() {
    createCanvas(700, 700);
    objectDetector.detect(img, gotResult);
    image(img, 0, 0);
}

function gotResult(error, results) {

    if (error) {
        console.error(error);
    } else {
        console.log(results);

        drawResults(results);

    }
}

function drawResults(results) {
    results.forEach((result) => {

        // Generates a random color for each object
        const r = Math.random()*256|0;
        const g = Math.random()*256|0;
        const b = Math.random()*256|0;

        // Draw the text
        stroke(0, 0, 0);
        strokeWeight(2);
        textSize(16);
        fill(r, g, b);
        text(`${result.label} (${result.confidence.toFixed(2)}%)`, result.x, result.y - 10);

        // Draw the rectangle stroke
        noFill();
        strokeWeight(3);
        stroke(r, g, b);
        rect(result.x, result.y, result.width, result.height);
    });
};