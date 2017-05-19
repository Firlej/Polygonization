PImage img;

int ww, hh;

float bestFitness=0;
int maxFitness, maxGrayFitness;

int howmuch = 100;

//color bgcolor = randomColor();
color bgcolor = color(31);

int polygonsLimit = 500;

int genId = 0;
int lastGenId = genId;

void setVariables() {
	ww=width/3; hh=height;

	maxFitness = ww*hh*3*256;
	maxGrayFitness = ww*hh*256;

	noStroke();
}

polygon randomPolygon() {
	return new polygon();
}

PVector randomVector() {
	return new PVector(random(ww), random(hh));
}

color randomGray() {
	return color(floor(random(256)), floor(random(256)));
}
color randomColor() {
	return color(floor(random(256)), floor(random(256)), floor(random(256)), floor(random(50,150)));
}

