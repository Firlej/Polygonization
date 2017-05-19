import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class imageEvolving extends PApplet {

picture currentPic;
picture bestPic;

public void setup() {
	//size(720, 358); img = loadImage("images/mona.jpg");
	 img = loadImage("images/opera.png");

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

public void draw() {
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

public int calcAverageImageColor() {
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
	int c = color(r/total, g/total, b/total);
	return c;
}

public void mousePressed() {
	howmuch = 0;
}

public float calcFitness(picture pic) {
	
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
public void checkIfImproved() {
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

public void addPolygon(picture pic) {
	if (pic.polygons.size()>polygonsLimit) { howmuch=0; return; }
	if (random(1)>0.05f) { return; }
	pic.polygons.add(randomPolygon());
}

public void alterOnePolygon(picture pic) {
	int polIndex = floor(random(pic.polygons.size()));
	polygon p = pic.polygons.get(polIndex);	
	p.alter();
}
public void randomizePolygon(picture pic) {
	int polIndex = floor(random(pic.polygons.size()));

	polygon p = pic.polygons.get(polIndex);	
	p.setRandom();
}

public void alterVector(PVector v) {
	v.x += random(-10, 10);
	v.y += random(-10, 10);

	if (v.x>ww) { v.x=ww; }
	else if (v.x<0) { v.x=0; }
	if (v.y>hh) { v.y=hh; }
	else if (v.y<0) { v.y=0; }
}
public void alterColor(picture pic) {
	int polIndex = floor(random(pic.polygons.size()));
	polygon p = pic.polygons.get(polIndex);

	p.alterColor();
}
public void randomizeColor(picture pic) {
	int polIndex = floor(random(pic.polygons.size()));
	polygon p = pic.polygons.get(polIndex);

	p.c = randomColor();
}
class picture {

	ArrayList<polygon> polygons = new ArrayList<polygon>();

	float fitness=0;

	picture() {
		for (int i = 0; i<polygonsLimit/4; ++i) {
			polygons.add(new polygon());
		}
	}

	public picture deepCopy() {
		picture newPic = new picture();
		newPic.polygons = new ArrayList<polygon>();	

		for (polygon p : polygons) {
			newPic.polygons.add( p.deepCopy() );
		}

		return newPic;
	}

	public void draw() {
		fill(bgcolor);
		rect(0, 0, ww, hh);
		for (polygon pol:polygons) { pol.draw(); }
	}
}


public void drawBestPicture() {
	pushMatrix();
		translate(ww, 0);
		bestPic.draw();
	popMatrix();
}
class polygon {

	PVector[] points = new PVector[3];
	int c;

	polygon() {
		setRandom();
	}

	public void setRandom() {
		for (int i=0; i<points.length; i++) {
			points[i] = new PVector( random(ww), random(hh) );
		}
		c = randomColor();
	}
	public void alter() {
		for (int i=0; i<points.length; i++) {
			alterVector(points[i]);
		}
	}
	public void alterColor() {
		float r = red(c)+random(-5,5);
		float g = green(c)+random(-5,5);
		float b = blue(c)+random(-5,5);
		float a = alpha(c)+random(-5,5);
		if (r>255) { r=255; } else if (r<0) { r=0; }
		if (g>255) { g=255; } else if (g<0) { g=0; }
		if (b>255) { b=255; } else if (b<0) { b=0; }
		if (a>150) { a=150; } else if (a<50) { a=50; }
		c = color(r, g, b, a);
	}

	public polygon deepCopy() {
		polygon newPol = new polygon();

		for(int i=0; i<newPol.points.length; i++) {
			newPol.points[i] = points[i].copy();
		}
		newPol.c = c;

		return newPol;
	}

	public void draw() {
		fill(c);
		beginShape();
		for (PVector v:points) { vertex(v.x, v.y); }
		endShape(CLOSE);
	}
}

PImage img;

int ww, hh;

float bestFitness=0;
int maxFitness, maxGrayFitness;

int howmuch = 100;

//color bgcolor = randomColor();
int bgcolor = color(31);

int polygonsLimit = 500;

int genId = 0;
int lastGenId = genId;

public void setVariables() {
	ww=width/3; hh=height;

	maxFitness = ww*hh*3*256;
	maxGrayFitness = ww*hh*256;

	noStroke();
}

public polygon randomPolygon() {
	return new polygon();
}

public PVector randomVector() {
	return new PVector(random(ww), random(hh));
}

public int randomGray() {
	return color(floor(random(256)), floor(random(256)));
}
public int randomColor() {
	return color(floor(random(256)), floor(random(256)), floor(random(256)), floor(random(50,150)));
}

  public void settings() { 	size(600, 200); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "imageEvolving" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
