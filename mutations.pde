
void addPolygon(picture pic) {
	if (pic.polygons.size()>polygonsLimit) { howmuch=0; return; }
	if (random(1)>0.05) { return; }
	pic.polygons.add(randomPolygon());
}

void alterOnePolygon(picture pic) {
	int polIndex = floor(random(pic.polygons.size()));
	polygon p = pic.polygons.get(polIndex);	
	p.alter();
}
void randomizePolygon(picture pic) {
	int polIndex = floor(random(pic.polygons.size()));

	polygon p = pic.polygons.get(polIndex);	
	p.setRandom();
}

void alterVector(PVector v) {
	v.x += random(-10, 10);
	v.y += random(-10, 10);

	if (v.x>ww) { v.x=ww; }
	else if (v.x<0) { v.x=0; }
	if (v.y>hh) { v.y=hh; }
	else if (v.y<0) { v.y=0; }
}
void alterColor(picture pic) {
	int polIndex = floor(random(pic.polygons.size()));
	polygon p = pic.polygons.get(polIndex);

	p.alterColor();
}
void randomizeColor(picture pic) {
	int polIndex = floor(random(pic.polygons.size()));
	polygon p = pic.polygons.get(polIndex);

	p.c = randomColor();
}