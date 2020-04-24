package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import Exception.DataStructureException;
import model.data_structures.ILinearProbing;
import model.data_structures.IMaxPQ;
import model.data_structures.IQueue;
import model.data_structures.IRedBlackBST;
import model.data_structures.LinearProbingHash;
import model.data_structures.MaxPQ;
import model.data_structures.Queue;
import model.data_structures.RedBlackBST;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	
	private static final String MAXPQ = "MAXPQ";
	
	private static final String HASHMAP = "HASHMAP";
	
	private static final String REDBLACKTREE = "REDBLACKTREE";
	
	private static final String Req1A = "Req1A";
	
	private static final String Req2A = "Req2A";
	
	private static final String Req3A = "Req3A";
	
	private static final String Req1B = "Req1B";
	
	private static final String Req2B = "Req2B";
	
	private static final String Req3B = "Req3B";
	
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
		redBlacktree = new RedBlackBST<>(); 
		
		dataStructureInUse = MAXPQ;
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
	
	public Feature[] searchTopSeverityFeatures( int m ) throws DataStructureException{
		
		migrateData( Req1A );
		
		return priorityQueue.max( m );
		
	}
	
	public Feature[] searchFeaturesByMonthAndDay( String monthNumber, String weekDay ) throws DataStructureException{
		
		migrateData( Req2A );
		
		String compoundKey = monthNumber + "-" + weekDay;
		
		return hashMap.get(compoundKey);
		
	}
	
	public Iterator<Feature> searchFeaturesByDateAndLocality( String initDate, String endDate, String locality ) throws DataStructureException{
		
		migrateData(Req3A);
		
		IQueue<Feature> featureQueue = new Queue<>();
		Iterator<Feature> featureIterator = redBlacktree.valuesInRange(initDate, endDate);
		
		while( featureIterator.hasNext() ){
			Feature actFeature = featureIterator.next();
			
			if( actFeature.getLocality().equals(locality) )
				featureQueue.enqueue(actFeature);
		}
		
		featureIterator = null;

		return featureQueue.iterator();
	}
	
	public void migrateData( String requirement ) throws DataStructureException{
		
		switch( requirement ){
			case Req1A:
				switch( dataStructureInUse ){
					case MAXPQ:
						priorityQueue.changeComparator( new SevereComparator<>() );
						break;
					case HASHMAP:
						migrateDataFromHashToQueue(Req1A);
						break;
					case REDBLACKTREE:
						migrateDataFromTreeToQueue(Req1A);
						break;
				}
				break;
			case Req2A:
				switch( dataStructureInUse ){
					case MAXPQ:
						migrateDataFromQueueToHash(Req2A);
						break;
					case HASHMAP:
						migrateDataFromHashToHash(Req2A);
						break;
					case REDBLACKTREE:
						migrateDataFromTreeToHash(Req2A);
						break;
				}
				break;
			case Req3A:
				switch( dataStructureInUse ){
					case MAXPQ:
						migrateDataFromoHashToTree(Req3A);
						break;
					case HASHMAP:
						migrateDataFromoHashToTree(Req3A);
						break;
					case REDBLACKTREE:
						migrateDataFromTreeToTree(Req3A);
						break;
				}
				break;
			case Req1B:
				switch( dataStructureInUse ){
					case MAXPQ:
						priorityQueue.changeComparator( /*aquí va el otro comparador*/ );
						break;
					case HASHMAP:
						migrateDataFromHashToQueue(Req1B);
						break;
					case REDBLACKTREE:
						migrateDataFromTreeToQueue(Req1B);
						break;
				}
				break;
			case Req2B:
				switch( dataStructureInUse ){
					case MAXPQ:
						migrateDataFromQueueToHash(Req2B);
						break;
					case HASHMAP:
						migrateDataFromHashToHash(Req2B);
						break;
					case REDBLACKTREE:
						migrateDataFromTreeToHash(Req2B);
						break;
				}
				break;
			case Req3B:
				switch( dataStructureInUse ){
					case MAXPQ:
						migrateDataFromoHashToTree(Req3B);
						break;
					case HASHMAP:
						migrateDataFromoHashToTree(Req3B);
						break;
					case REDBLACKTREE:
						migrateDataFromTreeToTree(Req3B);
						break;
				}
				break;
		}
		
	}
	
	public void migrateDataFromQueueToHash( String requirement ) throws DataStructureException{
		if( !dataStructureInUse.equals(MAXPQ) )
			throw new DataStructureException("Can't migrate from Queue because is empty");
		
		while( !priorityQueue.isEmpty() ){
			Feature feature = priorityQueue.delMax();
			String compoundKey = "";
			
			if( requirement.equals( Req2A ) ){
				
				Calendar calendar = Calendar.getInstance();
				DateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					date = format.parse(feature.getDate());
					calendar.setTime(date);
					String weekDay = Integer.toString( calendar.get(calendar.DAY_OF_WEEK) );
					String month = Integer.toString( calendar.get(calendar.MONTH) );
					
					compoundKey = month + "-" + weekDay;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
			} else if( requirement.equals( Req2B ) ){
				compoundKey = feature.getDetectionMethod() + "-" + feature.getVehicleClass()
					+ "-" + feature.getServiceType() + "-" + feature.getLocality();
			}
			
			hashMap.put(compoundKey, feature);
		}
		
		dataStructureInUse = HASHMAP;
	}
	
	public void migrateDataFromQueueToTree( String requirement ) throws DataStructureException{
		if( !dataStructureInUse.equals(MAXPQ) )
			throw new DataStructureException("Can't migrate from Queue because is empty");
		
		while( !priorityQueue.isEmpty() ){
			Feature feature = priorityQueue.delMax();
			String key = "";
			
			if( requirement.equals( Req3A ) ){
				
				DateFormat parser  = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
				Date date;
				try {
					date = parser.parse(feature.getDate());
					DateFormat formater = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
					key = formater.format(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
			} else if( requirement.equals( Req3B ) ){
				key = Double.toString( feature.getLatitud() );
			}
			
			redBlacktree.put(key, feature);
		
		}
		
		dataStructureInUse = REDBLACKTREE;
	}
	
	public void migrateDataFromHashToQueue( String requirement ) throws DataStructureException{
		if( !dataStructureInUse.equals(HASHMAP) )
			throw new DataStructureException("Can't migrate from Hash because is empty");

		Comparator<Feature> comparator = requirement.equals( Req1A ) ? new SevereComparator<>() : null;
		
		priorityQueue.changeComparator(comparator);

		IQueue<Feature>[] vals = hashMap.getValues();
		
		for( int i = 0; i < vals.length; i++ ){
			IQueue<Feature> actValue = vals[i];
			while( !actValue.isEmpty() )
				priorityQueue.insert( actValue.dequeue() );
			
			vals[i] = null;
		}
		
		// resizing hash
		vals = (IQueue<Feature>[]) new Object[7];
		
		dataStructureInUse = MAXPQ;
	}
	
	public void migrateDataFromoHashToTree( String requirement ) throws DataStructureException{
		if( !dataStructureInUse.equals(HASHMAP) )
			throw new DataStructureException("Can't migrate from Hash because is empty");

		IQueue<Feature>[] vals = hashMap.getValues();
		
		for( int i = 0; i < vals.length; i++ ){
			
			IQueue<Feature> actValue = vals[i];
			
			while( !actValue.isEmpty()){
				Feature feature = actValue.dequeue();
				String key = "";
				
				if( requirement.equals( Req3A ) ){
					
					DateFormat parser  = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
					Date date;
					try {
						date = parser.parse(feature.getDate());
						DateFormat formater = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
						key = formater.format(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				
				} else if( requirement.equals( Req3B ) ){
					key = Double.toString( feature.getLatitud() );
				}
				
					
				redBlacktree.put(key, feature);
			}
				
			vals[i] = null;
		}
		
		// resizing hash
		vals = (IQueue<Feature>[]) new Object[7];
		
		dataStructureInUse = REDBLACKTREE;
	}
	
	public void migrateDataFromHashToHash( String requirement ) throws DataStructureException{
		if( !dataStructureInUse.equals(HASHMAP) )
			throw new DataStructureException("Can't migrate from Hash because is empty");
		
		IQueue<Feature>[] vals = hashMap.getValues();
		ILinearProbing<String, Feature> newHashMap = new LinearProbingHash<>( vals.length );
		
		for( int i = 0; i < vals.length; i++ ){
			
			IQueue<Feature> actValue = vals[i];
			
			while( !actValue.isEmpty()){
				Feature feature = actValue.dequeue();
				String compoundKey = "";
				
				if( requirement.equals( Req2A ) ){
					
					Calendar calendar = Calendar.getInstance();
					DateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					try {
						date = format.parse(feature.getDate());
						calendar.setTime(date);
						String weekDay = Integer.toString( calendar.get(calendar.DAY_OF_WEEK) );
						String month = Integer.toString( calendar.get(calendar.MONTH) );
						
						compoundKey = month + "-" + weekDay;
					} catch (ParseException e) {
						e.printStackTrace();
					}
				
				} else if( requirement.equals( Req2B ) ){
					compoundKey = feature.getDetectionMethod() + "-" + feature.getVehicleClass()
						+ "-" + feature.getServiceType() + "-" + feature.getLocality();
				}
				
					
				newHashMap.put(compoundKey, feature);
			}
				
			vals[i] = null;
		}
		
		// resizing hash
		vals = (IQueue<Feature>[]) new Object[7];
		
		// changing hash
		hashMap = newHashMap;
		
		dataStructureInUse = HASHMAP;
		
	}
	
	public void migrateDataFromTreeToTree( String requirement ) throws DataStructureException{
		if( !dataStructureInUse.equals(REDBLACKTREE) )
			throw new DataStructureException("Can't migrate from Tree because is empty");
		
		Iterator<Feature> treeIterator = redBlacktree.valuesInRange( redBlacktree.min(), redBlacktree.max() );

		redBlacktree.emptyTree();
		
		while( treeIterator.hasNext() ){
			Feature featureVal = treeIterator.next();
			
			String key = "";
			
			if( requirement.equals( Req3A ) ){
				
				DateFormat parser  = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
				Date date;
				try {
					date = parser.parse(featureVal.getDate());
					DateFormat formater = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
					key = formater.format(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
			} else if( requirement.equals( Req3B ) ){
				key = Double.toString( featureVal.getLatitud() );
			}
			
			redBlacktree.put(key, featureVal);
		}
		
		dataStructureInUse = REDBLACKTREE;
	}
	
	public void migrateDataFromTreeToQueue( String requirement ) throws DataStructureException{
		if( !dataStructureInUse.equals(REDBLACKTREE) )
			throw new DataStructureException("Can't migrate from Tree because is empty");
		
		Iterator<Feature> treeIterator = redBlacktree.valuesInRange( redBlacktree.min(), redBlacktree.max() );
		
		Comparator<Feature> comparator = requirement.equals( Req1A ) ? new SevereComparator<>() : null;
		priorityQueue.changeComparator(comparator);
		
		while( treeIterator.hasNext() ){
			Feature featureVal = treeIterator.next();
			priorityQueue.insert(featureVal);
		}
		
		redBlacktree.emptyTree();
		
		dataStructureInUse = MAXPQ;
	}
	
	public void migrateDataFromTreeToHash( String requirement ) throws DataStructureException{
		if( !dataStructureInUse.equals(REDBLACKTREE) )
			throw new DataStructureException("Can't migrate from Tree because is empty");
		
		Iterator<Feature> treeIterator = redBlacktree.valuesInRange( redBlacktree.min(), redBlacktree.max() );
		
		while( treeIterator.hasNext() ){
			Feature feature = treeIterator.next();	
			String compoundKey = "";
			
			if( requirement.equals( Req2A ) ){
				
				Calendar calendar = Calendar.getInstance();
				DateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					date = format.parse(feature.getDate());
					calendar.setTime(date);
					String weekDay = Integer.toString( calendar.get(calendar.DAY_OF_WEEK) );
					String month = Integer.toString( calendar.get(calendar.MONTH) );
					
					compoundKey = month + "-" + weekDay;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
			} else if( requirement.equals( Req2B ) ){
				compoundKey = feature.getDetectionMethod() + "-" + feature.getVehicleClass()
					+ "-" + feature.getServiceType() + "-" + feature.getLocality();
			}
			
			hashMap.put(compoundKey, feature);
		}
		
		redBlacktree.emptyTree();
		
		dataStructureInUse = HASHMAP;
	}
	
	public boolean loadDataList(String path){
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