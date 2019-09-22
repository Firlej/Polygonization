class picture {

	ArrayList<polygon> polygons = new ArrayList<polygon>();

	float fitness=0;

	picture() {
		for (int i = 0; i<polygonsLimit; ++i) {
			polygons.add(new polygon());
		}
	}

	picture deepCopy() {
		picture newPic = new picture();
		newPic.polygons = new ArrayList<polygon>();	

		for (polygon p : polygons) {
			newPic.polygons.add( p.deepCopy() );
		}

		return newPic;
	}

	void draw() {
		fill(bgcolor);
		rect(0, 0, ww, hh);
		for (polygon pol:polygons) { pol.draw(); }
	}
}


void drawBestPicture() {
	pushMatrix();
		translate(ww, 0);
		bestPic.draw();
	popMatrix();
}