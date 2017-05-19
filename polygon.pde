class polygon {

	PVector[] points = new PVector[3];
	color c;

	polygon() {
		setRandom();
	}

	void setRandom() {
		for (int i=0; i<points.length; i++) {
			points[i] = new PVector( random(ww), random(hh) );
		}
		c = randomColor();
	}
	void alter() {
		for (int i=0; i<points.length; i++) {
			alterVector(points[i]);
		}
	}
	void alterColor() {
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

	polygon deepCopy() {
		polygon newPol = new polygon();

		for(int i=0; i<newPol.points.length; i++) {
			newPol.points[i] = points[i].copy();
		}
		newPol.c = c;

		return newPol;
	}

	void draw() {
		fill(c);
		beginShape();
		for (PVector v:points) { vertex(v.x, v.y); }
		endShape(CLOSE);
	}
}

