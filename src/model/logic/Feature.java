package model.logic;

import java.util.ArrayList;

public class Feature {

	private String type;
	
	private Property featureProperties;
	
	private Geometry featureGeometry;
	
	public Feature(String type,  int objectId, String date, String detectionMethod, String vehicleClass,
			String serviceType, String infraction, String reason, String locality, String geomType, ArrayList<Integer> coordinates){
	
		featureProperties = new Property(objectId, date, detectionMethod, vehicleClass, 
				serviceType, infraction, reason, locality);
		
		featureGeometry = new Geometry(geomType, coordinates);
		
	}

	public String getType() {
		return type;
	}
	

	public String getGeomType() {
		return featureGeometry.type;
	}

	public ArrayList<Integer> getCoordinates() {
		return featureGeometry.coordinates;
	}

	public int getObjectId() {
		return featureProperties.objectId;
	}

	public String getDate() {
		return featureProperties.date;
	}

	public String getDetectionMethod() {
		return featureProperties.detectionMethod;
	}

	public String getVehicleClass() {
		return featureProperties.vehicleClass;
	}

	public String getServiceType() {
		return featureProperties.serviceType;
	}

	public String getInfraction() {
		return featureProperties.infraction;
	}

	public String getReason() {
		return featureProperties.reason;
	}

	public String getLocality() {
		return featureProperties.locality;
	}
	
}
