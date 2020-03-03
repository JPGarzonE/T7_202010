package view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import model.logic.Feature;
import model.logic.Modelo;

public class View 
{
	    /**
	     * Metodo constructor
	     */
	    public View()
	    {
	    	
	    }
	    
		public void printMenu()
		{
			System.out.println("1. Consultar el primer comparendo de una localidad");
			System.out.println("2. Consultar el primer comparendo de una infracción");
			System.out.println("3. Buscar comparendos por fecha");
			System.out.println("4. Buscar comparendos por infracción");
			System.out.println("5. Comparar comparendos por dos fechas");
			System.out.println("6. Comparar comparendos por dos tipos de servicio");
			System.out.println("7. Buscar comparendos de una localidad entre dos fechas");
			System.out.println("8. Buscar los N codigos de infracción con más infracciones entre dos fechas");
			System.out.println("9. Histograma Localidades");
			System.out.println("10. Consultar comparendo");
			System.out.println("11. Exit");
			System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
		}

		public void printMessage(String mensaje) {

			System.out.println(mensaje);
		}
		
		public void printFeature(Feature feature){
			
			if(feature == null){
				System.out.println("No hay info de este comparendo o no existe.");
			}
			else{
				System.out.println("\nCOMPARENDO:");
				System.out.println("\n\tOBJECTID: " + feature.getObjectId());
				System.out.println("\n\tFECHA_HORA: " + feature.getDate());
				System.out.println("\n\tINFRACCION: " + feature.getInfraction());
				System.out.println("\n\tCLASE_VEHI: " + feature.getVehicleClass());
				System.out.println("\n\tTIPO_SERVI: " + feature.getServiceType());
				System.out.println("\n\tLOCALIDAD: " + feature.getLocality());
				System.out.println("\n");
			}
			
		}
		
		public void printGeneralFeaturesInfo(int featuresNumber, Feature featureBiggestId, double[] minmax ){
			
			System.out.println("\nDATOS GENERALES:");
			System.out.println("\n-----------------------------------");
			
			System.out.println("\nNUMERO TOTAL DE COMPARENDOS: " + featuresNumber);
			
			System.out.println("\n\nCOMPARENDO CON MAYOR ID:");
			printFeature(featureBiggestId);
			
			System.out.println("\nMINMAX DE LOS COMPARENDOS:\n");
			printMinimax(minmax);
			
		}
		
		public void printMinimax( double[] minmax ){
			
			int altoRect = 10;
			int anchoRect = 20;
			
			DecimalFormat format = new DecimalFormat("#.##");
			
			String maxlat = format.format(minmax[2]);
			String maxlong = format.format(minmax[3]);
			String minlat = format.format(minmax[0]);
			String minlong = format.format(minmax[1]);
			
			System.out.println("MaxLat (" + maxlat + ")");
			System.out.println("MaxLong (" + maxlong + ")");

			System.out.println("minLat (" + minlat + ")");
			System.out.println("minLong (" + minlong + ")\n");
			
			for(int i = 0; i < altoRect ; i++){
	            
				String linea = "";
				
				for(int j = 0; j < anchoRect ; j++){
					
					if( i == 0 && j == 0 )
						linea += (maxlat);
					else if( i == 0 && j == anchoRect - 1)
						linea += (maxlong);
					else if( i == altoRect - 1 && j == 0 )
						linea += (minlat);
					else if( i == altoRect - 1 && j == anchoRect - 1 )
						linea += (minlong);
					else if( i == 0 || i == altoRect - 1 || j == 0 || j == anchoRect - 1){
						if(  j == anchoRect - 1  ){
							linea += "        * ";
						}else{
							linea += "* ";
						}
					}else
						linea += "  ";
			    }
				
				System.out.println(linea);
			 }
			
			System.out.println("\n\n");
			
		}
		
		public void printFeatures( ArrayList<Feature> features ){
			for(int i = 0; i < features.size(); i++)
				printFeature(features.get(i));
		}
		
		public void printFeaturesComparatedByDate( String date1, String date2, Map<String, Integer> dates1, 
				Map<String, Integer> dates2){
			
			printMessage("Infracción	|	" + date1 + "	|	" + date2);
			
			dates1.forEach( (key, val) -> {
				boolean dates2ContainKey = dates2.containsKey(key);
				int dates2Num = dates2ContainKey ? dates2.get(key) : 0;
				printMessage(key + "		|		" + val + "	|	" + dates2Num);
				if( dates2ContainKey ) dates2.remove(key);
			});
			
			dates2.forEach( (key, val) -> {
				printMessage(key + "		|		" + 0 + "	|	" + val);
			});
		}
		
		public void printFeaturesComparatedByServiceType( String particularType, String publicType, Map<String, Integer> particularT, 
				Map<String, Integer> publicT){
			
			printMessage("Infracción	|	" + particularType + "	|	" + publicType);
			
			particularT.forEach( (key, val) -> {
				boolean publicTContainKey = publicT.containsKey(key);
				int publicTNum = publicTContainKey ? publicT.get(key) : 0;
				printMessage(key + "		|		" + val + "	|	" + publicTNum);
				if( publicTContainKey ) publicT.remove(key);
			});
			
			publicT.forEach( (key, val) -> {
				printMessage(key + "		|		" + 0 + "	|	" + val);
			});
		}
		
		
		public void printFeaturesBetweenTwoDates( String initialDate, String finalDate, 
				Map<String, Integer> features, String message ){
			
			printMessage(message + " del " + initialDate + " al " + finalDate);
			
			printMessage("Infracción	|	#Comparendos");
			
			features.forEach( (key, val) -> {
				printMessage(key + "		|	" + val);
			});
			
			printMessage("\n\n");
		}
		
		
		public void printHistogramL (String locality, Map<String, Integer>features)
		{
			printMessage ("Localidad		|			#Comparendos");
			
			features.forEach( (key, val) -> {
				printMessage(key + "			|			" + val);
			});
			
			printMessage("\n\n");
		}
		
		public void printHistogramLocality (Map<String, Integer>features)
		{
			features.forEach((key, val) -> {
				String resp = "";
				for (int i = 0; i<val ; i++)
				{
					resp += "*";
				}
				System.out.println(key+"	|	"+resp);
			});
		}
		
		private ArrayList<Integer> number (int n)
		{
			ArrayList<Integer> numeros = new ArrayList<Integer>();
			for (int i = 1; i<=n; i++)
			{
				numeros.add(n*i);
			}
			return numeros;
		}
}
