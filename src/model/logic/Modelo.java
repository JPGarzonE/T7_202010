package model.logic;

import model.data_structures.DataList;
import model.data_structures.DataNode;
import model.data_structures.IDataList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
		
	/**
	 * Lista doblemente encadenada
	 */
	private IDataList<Feature> dataList;
	
	/**
	 * Comparendo con el mayor id registrado
	 */
	private Feature featureWithBiggestId;
	
	/**
	 * Min Latitud registrada en los comparendos
	 */
	private Double minLatitud;
	
	/**
	 * Max Latitud registrada en los comparendos
	 */
	private Double maxLatitud;
	
	/**
	 * Min Longitud registrada en los comparendos
	 */
	private Double minLongitud;
	
	/**
	 * Max Longitud registrada en los comparendos
	 */
	private Double maxLongitud;
	
	/**
	 * Constructor del modelo del mundo con capacidad dada
	 * @param tamano
	 */
	public Modelo()
	{
		dataList = new DataList<Feature>();
	}
	
	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public int getFeaturesSize()
	{
		return dataList.getSize();
	}
	
	public Feature getFirstFeature(){
		return dataList.getFirstNode().getNodeInfo();
	}
	
	public Feature getLastFeature(){
		return dataList.getLastNode() == null ? null : dataList.getLastNode().getNodeInfo();
	}
	
	public double[] getMinmax(){
		
		double[] minmax = { minLatitud, minLongitud, maxLatitud, maxLongitud };
		
		return minmax;
	}
	
	public void loadDataList(String path){
		loadGson(path);
	}

	/**
	 * Requerimiento de agregar dato
	 * @param dato
	 */
	public void agregar(Feature dato)
	{	
		dataList.addNode(dato);
	}
	
	/**
	 * Busca un comparendo dado su object id
	 * @param objectId Id del comparendo a buscar
	 * @return comparendo encontrado. null si no lo encontro
	 */
	public Feature buscar(int objectId)
	{
		
		DataNode<Feature> actualNode = dataList.getFirstNode();
		boolean found = false;
		
		while( actualNode != null ){
			
			if( objectId == actualNode.getNodeInfo().getObjectId() ){
				found = true;
				break;
			}
			
			actualNode = actualNode.getNext();
			
		}
		
		return found ? actualNode.getNodeInfo() : null;
	}
	
	public Feature getFeatureWithBiggestId(){
		return featureWithBiggestId;
	}
	
	private void loadGson(String path){
		
		try{
			JsonReader reader = new JsonReader( new FileReader(path) );
			JsonElement featuresElement = JsonParser.parseReader(reader).getAsJsonObject().get("features");
			JsonArray jsonFeaturesArray = featuresElement.getAsJsonArray();
			
			double featureLatitud = 0;
			double featureLongitud = 0;
			
			for( JsonElement element : jsonFeaturesArray ){
				
				Feature feature = loadFeature(element);
				
				loadDataNode(feature);
				
				if( this.featureWithBiggestId == null )
					this.featureWithBiggestId = feature;
				else if( this.featureWithBiggestId.getObjectId() < feature.getObjectId() )
					this.featureWithBiggestId = feature;
				
				featureLatitud = feature.getCoordinates().get(0);
				featureLongitud = feature.getCoordinates().get(1);

				if( this.minLatitud == null ){
					this.minLatitud = featureLatitud;
				}

				if( this.maxLatitud == null ){
					this.maxLatitud = featureLatitud;
				}
				
				if( this.minLongitud == null ){
					this.minLongitud = featureLongitud;
				}
				
				if( this.maxLongitud == null ){
					this.maxLongitud = featureLongitud;
				}
				
				if( this.minLatitud > featureLatitud ){
					this.minLatitud = featureLatitud;
				}else if( this.maxLatitud < featureLatitud ){
					this.maxLatitud = featureLatitud;
				}
				
				if( this.minLongitud > featureLongitud ){
					this.minLongitud = featureLongitud;
				}else if( this.maxLongitud < featureLongitud ){
					this.maxLongitud = featureLongitud;
				}
				
			}			
		}
		catch(FileNotFoundException e){
			System.out.println("ERROR! File not found");
		}
	}
	
	
	private Feature loadFeature( JsonElement element ){
		
		String elemType = element.getAsJsonObject().get("type").getAsString();
		
		JsonElement elemProperties = element.getAsJsonObject().get("properties");
		
		int elemId = elemProperties.getAsJsonObject().get("OBJECTID").getAsInt();
		String elemDate = elemProperties.getAsJsonObject().get("FECHA_HORA").getAsString();
		String elemDetectionMethod = elemProperties.getAsJsonObject().get("MEDIO_DETE").getAsString();
		String elemVehicleClass = elemProperties.getAsJsonObject().get("CLASE_VEHI").getAsString();
		String elemServiceType = elemProperties.getAsJsonObject().get("TIPO_SERVI").getAsString();
		String elemInfraction = elemProperties.getAsJsonObject().get("INFRACCION").getAsString();
		String elemInfractionReason = elemProperties.getAsJsonObject().get("DES_INFRAC").getAsString();
		String elemLocality = elemProperties.getAsJsonObject().get("LOCALIDAD").getAsString();
		
		JsonElement elemGeometry = element.getAsJsonObject().get("geometry");
		
		String elemGeomType = elemGeometry.getAsJsonObject().get("type").getAsString();
		JsonArray elemGeomCoordinates = elemGeometry.getAsJsonObject().get("coordinates").getAsJsonArray();
		ArrayList<Double> elemCoordinates = new ArrayList<Double>();
		
		for( JsonElement elemCoord : elemGeomCoordinates ){
			Double actualCoord = elemCoord.getAsDouble();
			elemCoordinates.add( actualCoord );
		}
		
		Feature feature = new Feature(elemType, elemId, elemDate, elemDetectionMethod, elemVehicleClass, 
				elemServiceType, elemInfraction, elemInfractionReason, elemLocality, elemGeomType, elemCoordinates);
		
		return feature;
	}
	
	
	private void loadDataNode( Feature nodeInfo ){
		
		dataList.addNode(nodeInfo);
		
	}


}
