package model.logic;

import java.util.ArrayList;

public class Geometry {

	public String type;
	
	public ArrayList<Double> coordinates;
	
	public Geometry(String type, ArrayList<Double> coordinates){
		
		this.type = type;
		this.coordinates = coordinates;
		
	}

}
