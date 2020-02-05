package model.logic;

import model.data_structures.DataList;
import model.data_structures.DataNode;
import model.data_structures.IDataList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

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
	
	private void loadGson(String path){
		
		try{
			JsonReader reader = new JsonReader( new FileReader(path) );
			JsonElement featuresElement = JsonParser.parseReader(reader).getAsJsonObject().get("features");
			JsonArray jsonFeaturesArray = featuresElement.getAsJsonArray();
			
			
			for( JsonElement element : jsonFeaturesArray ){
				
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
				ArrayList<Integer> elemCoordinates = new ArrayList<Integer>();
				
				for( JsonElement elemCoord : elemGeomCoordinates ){
					int actualCoord = elemCoord.getAsInt();
					elemCoordinates.add( actualCoord );
				}
				
				Feature feature = new Feature(elemType, elemId, elemDate, elemDetectionMethod, elemVehicleClass, 
						elemServiceType, elemInfraction, elemInfractionReason, elemLocality, elemGeomType, elemCoordinates);
				
				loadDataNode(feature);
				
			}
			
		}
		catch(FileNotFoundException e){
			System.out.println("ERROR! File not found");
		}
		
		
	}
	
	private void loadDataNode( Feature nodeInfo ){
		
		dataList.addNode(nodeInfo);
		
	}


}
