package model.logic;

import model.data_structures.ArregloDinamico;
import model.data_structures.IArregloDinamico;
import model.data_structures.IDataList;
import view.View;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	/**
	 * Atributos del modelo del mundo
	 */
	private IArregloDinamico datos;
		
	/**
	 * Lista doblemente encadenada
	 */
	private IDataList<Object> dataList;
	
	/**
	 * Constructor del modelo del mundo con capacidad predefinida
	 */
	public Modelo()
	{
		datos = new ArregloDinamico(7);
	}
	
	/**
	 * Constructor del modelo del mundo con capacidad dada
	 * @param tamano
	 */
	public Modelo(int capacidad)
	{
		datos = new ArregloDinamico(capacidad);
		loadGson("./data/comparendos_dei_2018_small.geojson");
	}
	
	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public int darTamano()
	{
		return datos.darTamano();
	}

	/**
	 * Requerimiento de agregar dato
	 * @param dato
	 */
	public void agregar(String dato)
	{	
		datos.agregar(dato);
	}
	
	/**
	 * Requerimiento buscar dato
	 * @param dato Dato a buscar
	 * @return dato encontrado
	 */
	public String buscar(String dato)
	{
		return datos.buscar(dato);
	}
	
	/**
	 * Requerimiento eliminar dato
	 * @param dato Dato a eliminar
	 * @return dato eliminado
	 */
	public String eliminar(String dato)
	{
		return datos.eliminar(dato);
	}
	
	public void loadGson(String path){
		
		try{
			JsonReader reader = new JsonReader( new FileReader(path) );
			JsonElement featuresElement = JsonParser.parseReader(reader).getAsJsonObject().get("features");
			JsonArray jsonFeaturesArray = featuresElement.getAsJsonArray();
			
			System.out.println("before json features array");
			
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
				
				System.out.println("\n { \n type: " + elemType);
				System.out.println("\n properties: { ");
				System.out.println("\n OBJECTID: " + elemId);
				System.out.println("\n FECHA_HORA: " + elemDate);
				System.out.println("\n MEDIO_DETE: " + elemDetectionMethod);
				System.out.println("\n CLASE_VEHI: " + elemVehicleClass);
				System.out.println("\n TIPO_SERVI: " + elemServiceType);
				System.out.println("\n INFRACCION: " + elemInfraction);
				System.out.println("\n DES_INFRAC: " + elemInfractionReason);
				System.out.println("\n LOCALIDAD: " + elemLocality);
				System.out.println("\n } \n }");
				
			}
			
		}
		catch(FileNotFoundException e){
			System.out.println("ERROR! File not found");
		}
		
		
	}


}
