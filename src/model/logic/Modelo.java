package model.logic;

import model.data_structures.Queue;
import model.data_structures.DataNode;
import model.data_structures.IQueue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
	private IQueue<Feature> queue;
	
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
	 * Mapa que contiene los primeros comparendos del 
	 * archivo por cada localidad presente en el mismo
	 */
	private Map<String, Feature> firstFeaturesLocalities;
	
	/**
	 * 
	 */
	public Feature[] featuresArray;
	
	/**
	 * Constructor del modelo del mundo con capacidad dada
	 * @param tamano
	 */
	public Modelo()
	{
		queue = new Queue<Feature>();
		firstFeaturesLocalities = new HashMap<String, Feature>();
	}
	
	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public int getFeaturesSize()
	{
		return featuresArray.length;
	}
	
	public Feature getFirstFeature(){
		return featuresArray[0];
	}
	
	public double[] getMinmax(){
		
		double[] minmax = { minLatitud, minLongitud, maxLatitud, maxLongitud };
		
		return minmax;
	}
	
	public void loadData(String path){
		loadGson(path);
		loadFeaturesArray();
	}

	/**
	 * Requerimiento de agregar dato
	 * @param dato
	 */
	public void agregar(Feature dato)
	{	
		queue.enqueue(dato);
	}
	
	/**
	 * Busca un comparendo dado su object id
	 * @param objectId Id del comparendo a buscar
	 * @return comparendo encontrado. null si no lo encontro
	 */
	public Feature searchFeature(int objectId)
	{
		for( int i = 0; i < featuresArray.length; i++ )
			if( featuresArray[i].getObjectId() == objectId )
				return featuresArray[i];
		
		return null;
	}
	
	/**
	 * Busca el primer comparendo dada una localidad por parametro
	 * @param locality. Localidad de donde es el comparendo que se va a buscar.
	 * @return el comparendo si existe, si no null.
	 */
	public Feature searchFirstFeatureByLocality( String locality ){
		
		return firstFeaturesLocalities.get(locality);
	}
	
	public ArrayList<Feature> searchFeaturesByDate( String date ){
		return binarySearchByFeaturesDate( featuresArray, date, 0, featuresArray.length - 1 );
	}
	
	public Map<String, Integer> searchFeaturesNumberByDate( String date ){
		ArrayList<Feature> featuresFounded = binarySearchByFeaturesDate(
				featuresArray,
				date, 
				0, 
				featuresArray.length-1
		);
		
		Map<String, Integer> featuresNumber = new HashMap<>();
		String actualInfraction = "";
		
		for( int i = 0; i < featuresFounded.size(); i++ ){
			actualInfraction = featuresFounded.get(i).getInfraction();
			if( featuresNumber.containsKey(actualInfraction) ){
				featuresNumber.put(
						actualInfraction,
						featuresNumber.get(actualInfraction)+1
				);
			}
			else{
				featuresNumber.put( actualInfraction, 1 );
			}
		}
		
		return featuresNumber;
		
	}
	
	private ArrayList<Feature> binarySearchByFeaturesDate( Feature[] data, String searchingDate, int idxBottom, int idxTop ){
		
		ArrayList<Feature> featuresMatched = new ArrayList<Feature>();
		
		if( idxBottom > idxTop )
			return featuresMatched;
		
		int idxMiddle = (idxTop + idxBottom) / 2;
		
		String middleDate = data[idxMiddle].getDate();
		
		if( middleDate.equals( searchingDate ) ){
			featuresMatched.add( data[idxMiddle] );
			featuresMatched.addAll( 
					binarySearchByFeaturesDate(data, searchingDate, idxBottom, idxMiddle-1)
			);
			featuresMatched.addAll(
					binarySearchByFeaturesDate(data, searchingDate, idxMiddle+1, idxTop)
			);
		}
		else if( middleDate.compareTo(searchingDate) < 0 ){
			featuresMatched.addAll(
					binarySearchByFeaturesDate(data, searchingDate, idxMiddle+1, idxTop)
			);
		}
		else{
			featuresMatched.addAll(
					binarySearchByFeaturesDate(data, searchingDate, idxBottom, idxMiddle-1)
			);
		}
		
		return featuresMatched;
	}
	
	private void quickSort( Feature[] data, int lowIdx, int highIdx ){
		
		if( highIdx <= lowIdx ) return;
		
		int partition = partition( data, lowIdx, highIdx );
		
		quickSort( data, lowIdx, partition-1 );
		quickSort( data, partition+1, highIdx );
	}
	
	private int partition( Feature[] data, int lowIdx, int highIdx ){
		
		int i = lowIdx;
        int j = highIdx + 1;
        Feature v = data[lowIdx];
        while (true) { 

            // find item on lo to swap
            while (data[++i].compareTo(v) < 0) {
                if (i == highIdx) break;
            }

            // find item on hi to swap
            while (v.compareTo(data[--j]) < 0 ) {
                if (j == lowIdx) break;      // redundant since a[lo] acts as sentinel
            }

            // check if pointers cross
            if (i >= j) break;

            swap(data, i, j);
        }

        // put partitioning item v at a[j]
        swap(data, lowIdx, j);

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
	}
	
	private void swap( Feature[] data, int i, int j ){
		Feature temporary = data[i];
		data[i] = data[j];
		data[j] = temporary;
	}
	
	/**
	 * Retorna el comparendo con mayor ID que se encontró en la carga del archivo
	 * @return featureWithBiggestId
	 */
	public Feature getFeatureWithBiggestId(){
		return featureWithBiggestId;
	}
	
	private void loadFeaturesArray(){
		
		featuresArray = new Feature[ queue.size() ];

		for( int i = 0; i < featuresArray.length; i++  )
			featuresArray[i] = queue.dequeue();
		
		quickSort(featuresArray, 0, featuresArray.length - 1);
	}
	
	private void loadGson(String path){
		
		try{
			JsonReader reader = new JsonReader( new FileReader(path) );
			JsonElement featuresElement = JsonParser.parseReader(reader).getAsJsonObject().get("features");
			JsonArray jsonFeaturesArray = featuresElement.getAsJsonArray();
			
			for( JsonElement element : jsonFeaturesArray ){
				
				Feature feature = loadFeature(element);
				
				loadDataNode(feature);
				
				setFeatureBiggestId(feature);
				
				setMinMax(feature);
				
				loadFirstFeaturesByLocality(feature, feature.getLocality());
			}			
		}
		catch(FileNotFoundException e){
			System.out.println("ERROR! File not found");
		}
	}
	
	private void setFeatureBiggestId( Feature feature ){
		if( this.featureWithBiggestId == null )
			this.featureWithBiggestId = feature;
		else if( this.featureWithBiggestId.getObjectId() < feature.getObjectId() )
			this.featureWithBiggestId = feature;
	}
	
	private void setMinMax( Feature feature ){
		double featureLatitud = feature.getCoordinates().get(0);
		double featureLongitud = feature.getCoordinates().get(1);

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
	
	private void loadFirstFeaturesByLocality(Feature feature, String locality){
		if( !firstFeaturesLocalities.containsKey(locality) )
			firstFeaturesLocalities.put(locality, feature);
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
		
		queue.enqueue(nodeInfo);
		
	}


}
