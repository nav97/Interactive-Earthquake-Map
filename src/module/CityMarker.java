package module;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PGraphics;


/** Implements a visual marker for cities on an earthquake map
 *
 * @author nav97
 *
 */
// TODO: Change SimplePointMarker to CommonMarker as the very first thing you do 
// in module 5 (i.e. CityMarker extends CommonMarker).  It will cause an error.
// That's what's expected.
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	public static int INFO_WIDTH = 200;
	public static int INFO_HEIGHT = 80;
	
	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}

	
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void drawMarker(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		if(this.isSelected()){

			String city = getCity();
			String country = getCountry();
			String population = getPopulation() + " million";

			float startX = x;
			float startY = y;

			if((x + INFO_WIDTH) > pg.width){
				startX = x - INFO_WIDTH;
			}

			//make box on top if necessary
			if((y + INFO_HEIGHT) > pg.height){
				startY = y - INFO_HEIGHT;
			}


			pg.fill(255);
			pg.rect(startX, startY, INFO_WIDTH, INFO_HEIGHT);

			pg.fill(0);
			pg.textSize(12);
			pg.text("City: " + getCity(), startX + 10, startY + 15);
			pg.text("Country: " + getCountry(), startX + 10, startY + 35);
			pg.text("Population: " + getPopulation() + " million", startX + 10, startY + 55);
		}

	}
	
	
	/* Local getters for some city properties.  
	 */
	public String getCity()
	{
		return getStringProperty("name");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}

	//in millions
	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
}
