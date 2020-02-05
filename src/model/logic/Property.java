package model.logic;

public class Property {
	
	public int objectId;
	
	public String date;
	
	public String detectionMethod;
	
	public String vehicleClass;
	
	public String serviceType;
	
	public String infraction;
	
	public String reason;
	
	public String locality;
	
	public Property( int objectId, String date, String detectionMethod, String vehicleClass,
			String serviceType, String infraction, String reason, String locality ){
		
		this.objectId = objectId;
		this.date = date;
		this.detectionMethod = detectionMethod;
		this.vehicleClass = vehicleClass;
		this.serviceType = serviceType;
		this.infraction = infraction;
		this.reason = reason;
		this.locality = locality;
		
	}
}
