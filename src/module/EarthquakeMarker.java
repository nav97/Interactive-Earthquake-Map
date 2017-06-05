package module;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;


/** Implements a visual marker for earthquakes on an earthquake map
 *
 * @author nav97
 *
 */
public abstract class EarthquakeMarker extends CommonMarker
{
	
	// Did the earthquake occur on land?  This will be set by the subclasses.
	protected boolean isOnLand;

	// The radius of the Earthquake marker
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude. 
	protected float radius;

	//size of info box
	public static int INFO_HEIGHT = 80;

	// constants for distance and radius multiplier
	protected static final float kmPerMile = 1.6f;
    protected static final float radiusMultip = 1.75f;
	
	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	
	// abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	
	// constructor
	public EarthquakeMarker (PointFeature feature) {
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude );
		setProperties(properties);
		this.radius = 1.75f*getMagnitude();
	}
	

	// calls abstract method drawEarthquake and then checks age and draws X if needed
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
			
		// determine colour of marker from depth
		colourDetermine(pg);
		
		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);
		
		// add X over marker if within past day
		String age = getStringProperty("age");
		if ("Past Hour".equals(age) || "Past Day".equals(age)) {
			
			pg.strokeWeight(2);
			int buffer = 2;
			pg.line(x-(radius+buffer), 
					y-(radius+buffer), 
					x+radius+buffer, 
					y+radius+buffer);
			pg.line(x-(radius+buffer), 
					y+(radius+buffer), 
					x+radius+buffer, 
					y-(radius+buffer));
			
		}
		
		// reset to previous styling
		pg.popStyle();
		
	}

	/** Show the title of the earthquake if this marker is selected */
	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		if(this.isSelected()){

			String tempTitle = getTitle(); //Strip magnitude
			String name = "Title: " + tempTitle.substring(tempTitle.indexOf('-') + 2);
			float startX = x;
			float startY = y;
			float INFO_WIDTH = pg.textWidth(name) + 15;

			//make box on left side
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
			pg.text(name, startX + 10, startY + 15);
			pg.text("Magnitude: " + getMagnitude(), startX + 10, startY + 35);
			pg.text("Depth: " + getDepth() + " km", startX + 10, startY + 55);
		}
	}

	
	/**
	 * Return the "threat circle" radius, or distance up to 
	 * which this earthquake can affect things, for this earthquake.   
	 * DISCLAIMER: this formula is for illustration purposes
	 *  only and is not intended to be used for safety-critical 
	 *  or predictive applications.
	 */
	public double threatCircle() {	
		double miles = 20.0f * Math.pow(1.8, 2*getMagnitude()-5);
		double km = (miles * kmPerMile);
		return km;
	}
	
	// determine colour of marker from depth
	// We use: Deep = red, intermediate = blue, shallow = yellow
	private void colourDetermine(PGraphics pg) {
		float depth = getDepth();
		
		if (depth < THRESHOLD_INTERMEDIATE) {
			pg.fill(255, 255, 0);
		}
		else if (depth < THRESHOLD_DEEP) {
			pg.fill(0, 0, 255);
		}
		else {
			pg.fill(255, 0, 0);
		}
	}
	
	
	/*
	 * getters for earthquake properties
	 */
	
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());	
	}
	
	public String getTitle() {return (String) getProperty("title");}

	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public boolean isOnLand()
	{
		return isOnLand;
	}
	

	
	
}
