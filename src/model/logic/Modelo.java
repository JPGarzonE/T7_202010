package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import model.data_structures.ILinearProbing;
import model.data_structures.IMaxPQ;
import model.data_structures.IRedBlackBST;
import model.data_structures.LinearProbingHash;
import model.data_structures.MaxPQ;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	
	/**
	 * MaxPQ for requirements 1A y 1B
	 */
	private IMaxPQ<Feature> priorityQueue;
	
	/**
	 * LinearProbingHash for requirements 2A y 2B
	 */
	private ILinearProbing<String, Feature> hashMap;
	
	/**
	 * RedBlackBinarySearchTree for requirements 3A y 3B
	 */
	private IRedBlackBST<String, Feature> redBlacktree;
	
	/**
	 * First feature
	 */
	private Feature firstFeature;
	
	/**
	 * Last feature
	 */
	private Feature lastFeature;
	
	/**
	 * Feature with biggest id
	 */
	private Feature featureWithBiggestId;
	
	/**
	 * Size
	 */
	private int size;
	
	/**
	 * Estructura de datos que se esta usando actualmente
	 */
	private String dataStructureInUse;
	
	/**
	 * Constructor del modelo del mundo con capacidad predefinida
	 */
	public Modelo()
	{
		this(20);
	}
	
	/**
	 * Constructor del modelo del mundo con capacidad dada
	 * @param tamano
	 */
	public Modelo(int capacity )
	{
		priorityQueue = new MaxPQ<>(capacity, new SevereComparator<>());
		hashMap = new LinearProbingHash<>(capacity);
//		redBlacktree = 
	}
	
	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public int size(){
		return size;
	}
	
	public Feature getFirstFeature(){
		return firstFeature;
	}
	
	public Feature getLastFeature(){
		return lastFeature;
	}
	
	public Feature getFeatureWithBiggestId(){
		return featureWithBiggestId;
	}
	
	public String getDataStructureInUse(){
		return dataStructureInUse;
	}
	
	public void migrateDataFromQueueToHash(){
		
	}
	
	public void migrateDataFromQueueToTree(){
		
	}
	
	public void migrateDataFromHashToQueue(){
		
	}
	
	public void migrateDataFromoHashToTree(){
		
	}
	
	public void migrateDataFromTreeToQueue(){
		
	}
	
	public void migrateDataFromTreeToHash(){
		
	}
	
	public boolean loadDataList(String path) {
		if( loadGson(path) )
			return true;
		else	
			return false;
	}

	private boolean loadGson(String path) {

		try {
			System.out.println(path);
			JsonReader reader = new JsonReader(new FileReader(path));
			JsonElement featuresElement = JsonParser.parseReader(reader).getAsJsonObject().get("features");
			JsonArray jsonFeaturesArray = featuresElement.getAsJsonArray();

			for (JsonElement element : jsonFeaturesArray) {

				String elemType = element.getAsJsonObject().get("type").getAsString();

				JsonElement elemProperties = element.getAsJsonObject().get("properties");

				int elemId = elemProperties.getAsJsonObject().get("OBJECTID").getAsInt();
				String elemDate = elemProperties.getAsJsonObject().get("FECHA_HORA").getAsString();
				String elemDetectionMethod = elemProperties.getAsJsonObject().get("MEDIO_DETECCION").getAsString();
				String elemVehicleClass = elemProperties.getAsJsonObject().get("CLASE_VEHICULO").getAsString();
				String elemServiceType = elemProperties.getAsJsonObject().get("TIPO_SERVICIO").getAsString();
				String elemInfraction = elemProperties.getAsJsonObject().get("INFRACCION").getAsString();
				String elemInfractionReason = elemProperties.getAsJsonObject().get("DES_INFRACCION").getAsString();
				String elemLocality = elemProperties.getAsJsonObject().get("LOCALIDAD").getAsString();
				String elemTown = elemProperties.getAsJsonObject().get("MUNICIPIO").getAsString();
				
				JsonElement elemGeometry = element.getAsJsonObject().get("geometry");

				String elemGeomType = elemGeometry.getAsJsonObject().get("type").getAsString();
				JsonArray elemGeomCoordinates = elemGeometry.getAsJsonObject().get("coordinates").getAsJsonArray();
				ArrayList<Double> elemCoordinates = new ArrayList<Double>();

				for (JsonElement elemCoord : elemGeomCoordinates) {
					Double actualCoord = elemCoord.getAsDouble();
					elemCoordinates.add(actualCoord);
				}

				elemVehicleClass = elemVehicleClass.startsWith("AUTOM") ? "AUTOMOVIL" : elemVehicleClass;
				
				Feature feature = new Feature(elemType, elemId, elemDate, elemDetectionMethod, elemVehicleClass,
						elemServiceType, elemInfraction, elemInfractionReason, elemLocality, elemTown, elemGeomType,
						elemCoordinates);

				loadMapElement(feature);

				if( featureWithBiggestId == null )
					featureWithBiggestId = feature;
				else if( featureWithBiggestId.getObjectId() < feature.getObjectId() )
					featureWithBiggestId = feature;
				
				if( firstFeature == null )
					firstFeature = feature;
				
				lastFeature = feature;
			}

			size = priorityQueue.size();

		} catch (FileNotFoundException e) {
			System.out.println("ERROR! File not found\n\n");
			return false;
		}
		
		return true;

	}
	
	
	private void loadMapElement(Feature feature){
		
		priorityQueue.insert(feature);
		
	}


}