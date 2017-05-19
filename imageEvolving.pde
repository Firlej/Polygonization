picture currentPic;
picture bestPic;

void setup() {
	//size(720, 358); img = loadImage("images/mona.jpg");
	size(600, 200); img = loadImage("images/opera.png");

	setVariables();

	currentPic = new picture();
	bestPic = currentPic.deepCopy();

	background(31);
	
	image(img, ww*2, 0, ww, hh);

	bgcolor = color(31);
	bgcolor = calcAverageImageColor();

	currentPic.draw();
	drawBestPicture();
}

void draw() {
	for (int i = 0; i < howmuch; ++i) {
		addPolygon(currentPic); checkIfImproved();
	}
	alterColor(currentPic); checkIfImproved();
	randomizeColor(currentPic); checkIfImproved();
	alterOnePolygon(currentPic); checkIfImproved();
	randomizePolygon(currentPic); checkIfImproved();
	
	/*if (genId != lastGenId) {
		saveFrame("output/f_####.png");
		lastGenId = genId;
	}*/

	fill(0);
	text(floor(frameRate), 10, 10);
}

color calcAverageImageColor() {
	int r=0, g=0, b=0;
	loadPixels();
	for(int x=2*ww; x<width; x++) {
		for (int y=0; y<height; y++) {
			int i = x+y*width;

			r+=red(pixels[i]);
			g+=green(pixels[i]);
			b+=blue(pixels[i]);
		}
	}
	int total = ww*hh;
	color c = color(r/total, g/total, b/total);
	return c;
}

void mousePressed() {
	howmuch = 0;
}

float calcFitness(picture pic) {
	
	pic.draw();

	float fitness = 0;
	loadPixels();
	for(int x=0; x<(width/3); x++) {
		for (int y=0; y<height; y++) {
			int i1 = x+y*width;
			int i2 = i1+ww+ww;

			//fitness+= abs(brightness(pixels[i1])-brightness(pixels[i2]));

			fitness += abs(red(pixels[i1])-red(pixels[i2]));
			fitness += abs(green(pixels[i1])-green(pixels[i2]));
			fitness += abs(blue(pixels[i1])-blue(pixels[i2]));

		}
	}
	return (1-fitness/maxFitness);
}
void checkIfImproved() {
	float currentFitness = calcFitness(currentPic);

	if (currentFitness>bestFitness) {
		bestFitness = currentFitness;
		println("bestFitness: "+bestFitness);
		bestPic = currentPic.deepCopy();
		drawBestPicture();
		genId++;
	} else {
		currentPic = bestPic.deepCopy();
	}
}