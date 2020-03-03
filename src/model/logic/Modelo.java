package model.logic;

import model.data_structures.Queue;
import model.data_structures.DataNode;
import model.data_structures.IQueue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	 * Mapa que contiene los primeros comparendos del 
	 * archivo por cada infraccion presente en el mismo
	 */
	private Map<String, Feature> firstFeaturesInfractions;
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
		firstFeaturesInfractions = new HashMap<String, Feature>();
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
	//1A
	public Feature searchFirstFeatureByLocality( String locality ){
		
		return firstFeaturesLocalities.get(locality);
	}
	
	/**
	 * Busca el primer comparendo dada una infraccion por parametro
	 * @param infraction. Infraccion del comparendo que se va a buscar.
	 * @return el comparendo si existe, si no null.
	 */
	//1B
	public Feature searchFirstFeatureByInfraction (String infraction)
	{
		return firstFeaturesInfractions.get(infraction);
	}
	
	//2A
	public ArrayList<Feature> searchFeaturesByDate( String date ){
		return binarySearchByFeaturesDate( featuresArray, date, 0, featuresArray.length - 1 );
	}
	
	//2B
	public ArrayList<Feature> searchFeaturesByInfraction (String infraction)
	{
		return binarySearchByFeaturesInfraction(featuresArray, infraction, 0, featuresArray.length - 1);
	}
	
	
	//3A
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
	
	
	//3B
	public Map<String, Integer> searchFeaturesNumberByServiceType( String serviceType ){
		ArrayList<Feature> featuresFounded = binarySearchByFeaturesServiceType(
				featuresArray,
				serviceType, 
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
	
	public Map<String, Integer> searchFeaturesNumberByLocality(){
		
		Map<String, Integer> featuresNumber = new HashMap<>();
		String actualLocality = "";
		
		for( int i = 0; i<featuresArray.length;i++ )
		{
			actualLocality = featuresArray[i].getLocality();
			if( featuresNumber.containsKey(actualLocality) )
			{
					featuresNumber.put(actualLocality,featuresNumber.get(actualLocality)+1);
			}
			else{
				featuresNumber.put( actualLocality, 1 );
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
	
	
	
	private ArrayList<Feature> binarySearchByFeaturesInfraction( Feature[] data, String searchingInfraction, int idxBottom, int idxTop )
	{
		
		ArrayList<Feature> featuresMatched = new ArrayList<Feature>();
		
		if( idxBottom > idxTop )
			return featuresMatched;
		
		int idxMiddle = (idxTop + idxBottom) / 2;
		
		String middleInfraction = data[idxMiddle].getInfraction();
		
		if( middleInfraction.equals( searchingInfraction ) ){
			featuresMatched.add( data[idxMiddle] );
			featuresMatched.addAll( 
					binarySearchByFeaturesInfraction(data, searchingInfraction, idxBottom, idxMiddle-1)
			);
			featuresMatched.addAll(
					binarySearchByFeaturesInfraction(data, searchingInfraction, idxMiddle+1, idxTop)
			);
		}
		else if( middleInfraction.compareTo(searchingInfraction) < 0 ){
			featuresMatched.addAll(
					binarySearchByFeaturesInfraction(data, searchingInfraction, idxMiddle+1, idxTop)
			);
		}
		else{
			featuresMatched.addAll(
					binarySearchByFeaturesInfraction(data, searchingInfraction, idxBottom, idxMiddle-1)
			);
		}
		
		return featuresMatched;
	}
	
	
	
	private ArrayList<Feature> binarySearchByFeaturesServiceType( Feature[] data, String searchingType, int idxBottom, int idxTop )
	{
		
		ArrayList<Feature> featuresMatched = new ArrayList<Feature>();
		
		if( idxBottom > idxTop )
			return featuresMatched;
		
		int idxMiddle = (idxTop + idxBottom) / 2;
		
		String middle = data[idxMiddle].getServiceType();
		
		if( middle.equals( searchingType ) ){
			featuresMatched.add( data[idxMiddle] );
			featuresMatched.addAll( 
					binarySearchByFeaturesServiceType(data, searchingType, idxBottom, idxMiddle-1)
			);
			featuresMatched.addAll(
					binarySearchByFeaturesServiceType(data, searchingType, idxMiddle+1, idxTop)
			);
		}
		else if( middle.compareTo(searchingType) < 0 ){
			featuresMatched.addAll(
					binarySearchByFeaturesServiceType(data, searchingType, idxMiddle+1, idxTop)
			);
		}
		else{
			featuresMatched.addAll(
					binarySearchByFeaturesServiceType(data, searchingType, idxBottom, idxMiddle-1)
			);
		}
		
		return featuresMatched;
	}
	
	
	private ArrayList<Feature> binarySearchByFeaturesLocality( Feature[] data, String searchingType, int idxBottom, int idxTop )
	{
		
		ArrayList<Feature> featuresMatched = new ArrayList<Feature>();
		
		if( idxBottom > idxTop )
			return featuresMatched;
		
		int idxMiddle = (idxTop + idxBottom) / 2;
		
		String middle = data[idxMiddle].getLocality();
		
		if( middle.equals( searchingType ) ){
			featuresMatched.add( data[idxMiddle] );
			featuresMatched.addAll( 
					binarySearchByFeaturesLocality(data, searchingType, idxBottom, idxMiddle-1)
			);
			featuresMatched.addAll(
					binarySearchByFeaturesLocality(data, searchingType, idxMiddle+1, idxTop)
			);
		}
		else if( middle.compareTo(searchingType) < 0 ){
			featuresMatched.addAll(
					binarySearchByFeaturesLocality(data, searchingType, idxMiddle+1, idxTop)
			);
		}
		else{
			featuresMatched.addAll(
					binarySearchByFeaturesLocality(data, searchingType, idxBottom, idxMiddle-1)
			);
		}
		
		return featuresMatched;
	}
	
	
	/**
	 * Busca los N comparendos con m�s infracciones que estan entre dos fechas por parametro en una
	 * localidad espec�fica, si la localidad es null: se buscar�n todas las localidades
	 * @param dateBottom fecha inicial desde la que se van a buscar los comparendos
	 * @param dateTop fecha final hasta donde se van a buscar los comparendos
	 * @param locality localidad en la que se quiere buscar. null para buscar en todas las localidades
	 * @param n numero de comparendos con m�s infracciones. si es null, se buscar�n todos los comparendos
	 * @return
	 */
	//1C
	public Map<String, Integer> searchNfeaturesNumbersBetweenDatesInALocality(String dateBottom, String dateTop,
			String locality, Integer n){
		
		ArrayList<Feature> featuresBetweenDates = binarySearchBetweenFeaturesDates(
				featuresArray, dateBottom, dateTop, 0, featuresArray.length - 1);

		Map<String, Integer> featuresBetweenDatesMap = new HashMap<>();
		
		Iterator<Feature> iterator = featuresBetweenDates.iterator();
		
		while( iterator.hasNext() ){
			Feature featureIter = iterator.next();
			String featureInfraction = featureIter.getInfraction();
			
			if( locality != null &&  featureIter.getLocality().equals(locality) ){
				if( featuresBetweenDatesMap.containsKey(featureInfraction) )
					featuresBetweenDatesMap.put(
							featureInfraction, featuresBetweenDatesMap.get(featureInfraction)+1);
				else
					featuresBetweenDatesMap.put(
							featureInfraction, 1);
			}
			else if( locality == null ){
				if( featuresBetweenDatesMap.containsKey(featureInfraction) )
					featuresBetweenDatesMap.put(
							featureInfraction, featuresBetweenDatesMap.get(featureInfraction)+1);
				else
					featuresBetweenDatesMap.put(
							featureInfraction, 1);
			}

		}
		
		if( n != null ){
			Map<String, Integer> sortedFeaturesMap = sortByValue(featuresBetweenDatesMap);
			featuresBetweenDatesMap.clear();
			
			int i = 1;
			
			for( Map.Entry<String, Integer> entry : sortedFeaturesMap.entrySet() ){
				featuresBetweenDatesMap.put( entry.getKey(), entry.getValue() );
				
				if( i++ == n )
					break;
				
			}
			
		}
		
		return featuresBetweenDatesMap;
		
	}
	
	
	
	
	
	
	private HashMap<String, Integer> sortByValue(Map<String, Integer> unSortedMap ){
		HashMap<String, Integer> reverseSortedMap = new HashMap<>();
		unSortedMap.entrySet()
	    .stream()
	    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
	    .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
		
		return reverseSortedMap;
	}
	
//	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
//        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
//        list.sort(Entry.comparingByValue(), Collections.reverseOrder());
//
//        Map<K, V> result = new LinkedHashMap<>();
//        for (Entry<K, V> entry : list) {
//            result.put(entry.getKey(), entry.getValue());
//        }
//
//        return result;
//    }
	
	//2C
	private ArrayList<Feature> binarySearchBetweenFeaturesDates( Feature[] data, String dateBottom, String dateTop,
			int idxBottom, int idxTop){
		
		ArrayList<Feature> featuresMatched = new ArrayList<Feature>();
		
		if( idxBottom > idxTop )
			return featuresMatched;
		
		int idxMiddle = (idxTop + idxBottom) / 2;
		
		String middleDate = data[idxMiddle].getDate();

		if( isInRange(middleDate, dateBottom, dateTop) ){
			featuresMatched.add( data[idxMiddle] );
			featuresMatched.addAll( 
					binarySearchBetweenFeaturesDates(data, dateBottom, dateTop, idxBottom, idxMiddle-1)
			);
			featuresMatched.addAll(
					binarySearchBetweenFeaturesDates(data, dateBottom, dateTop, idxMiddle+1, idxTop)
			);
		}
		else if( middleDate.compareTo(dateTop) > 0 ){
			featuresMatched.addAll(
					binarySearchBetweenFeaturesDates(data, dateBottom, dateTop, idxBottom, idxMiddle-1 )
			);
		}
		else if( middleDate.compareTo(dateBottom) < 0 ){
			featuresMatched.addAll(
					binarySearchBetweenFeaturesDates(data, dateBottom, dateTop, idxMiddle+1, idxTop)
			);
		}
		
		return featuresMatched;
	}
	
	
	
	
	private <T extends Comparable<T>> boolean isInRange( T middleData, T bottomData, T topData ){
		return (middleData.compareTo(bottomData) >= 0) && (middleData.compareTo(topData) <= 0);
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
	 * Retorna el comparendo con mayor ID que se encontr� en la carga del archivo
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
				
				loadFirstFeaturesByInfraction(feature, feature.getInfraction());
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
	
	private void loadFirstFeaturesByInfraction(Feature feature, String infraction)
	{
		if( !firstFeaturesInfractions.containsKey(infraction) )
			firstFeaturesInfractions.put(infraction, feature);
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
