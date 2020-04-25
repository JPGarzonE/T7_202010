package controller;

import java.util.Scanner;

import Exception.DataStructureException;
import model.logic.Feature;
import model.logic.Modelo;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;
	
	/* Instancia de la Vista*/
	private View view;
	
	static final String DATA_PATH = "./data/comparendos_dei_2018_Bogotá_D.C_small.geojson";
	
	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new View();
		modelo = new Modelo();
	}
		
	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		
		view.printMessage("\n La aplicación va a cargar los datos inmediatamente de esta ruta: " + DATA_PATH);
		view.printMessage("\n ¿Esta de acuerdo? (Y/n)");
		String answer = lector.next();
		System.out.println("x");
		if( answer.equalsIgnoreCase("Y") ){
			loadData();
		}else{
			view.printMessage("\n Cambie el archivo y vuelva a entrar. Gracias!");
			fin = true;
		}

		while( !fin ){
			view.printMenu();

			try{
				
				int option = lector.nextInt();
				switch(option){
					case 1:
						view.printMessage("\n ¿Cuántos comparendos deseas mirar?"); 
						int m = lector.nextInt();
						view.printMessage("\n Buscando...");
						view.printFeatures( modelo.searchTopSeverityFeatures(m) );
						
						break;
					case 2:
						view.printMessage("\n Mes (Numero del 1-12):");
						String month = lector.next();
						view.printMessage("\n Día (Letra L,M,I,J,V,S,D):");
						String weekDay = lector.next();
						
						view.printMessage("\n Buscando...");
						view.printFeatures( modelo.searchFeaturesByMonthAndDay(month, weekDay) );
						
						break;
					case 3:
						view.printMessage("\n Fecha inicial (formato: YYYY/MM/DD-HH:MM:ss):");
						view.printMessage("Ej: 2018/09/22-13:45:34");
						String initDate = lector.next();
						view.printMessage("\n Fecha final (formato: YYYY/MM/DD-HH:MM:ss):");
						view.printMessage("Ej: 2018/09/22-13:45:34");
						String endDate = lector.next();
						view.printMessage("\n Localidad:");
						String locality = lector.next();
						
						view.printMessage("\n Buscando...");
						view.printFeatures( modelo.searchFeaturesByDateAndLocality(initDate, endDate, locality) );
						
						break;
					case 4:
						view.printMessage("\n ¿Cuántos comparendos deseas mirar cercanos a la estacion de policía del Campín?"); 
						int n = lector.nextInt();
						view.printMessage("\n Buscando...");
						view.printFeatures( modelo.searchNearestFeatures(n));
						
						break;
					case 5:
						view.printMessage("\n Medio de Deteccion:");
						String detection = lector.next();
						view.printMessage("\n Clase de vehiculo:");
						String vehicleClass = lector.next();
						view.printMessage("\n Tipo de servicio:");
						String serviceType = lector.next();
						view.printMessage("\n Localidad:");
						String locality1 = lector.next();
						
						view.printMessage("\n Buscando...");
						view.printFeatures( modelo.searchFeaturesByParameters(detection, vehicleClass, serviceType, locality1));
						
						
						break;
					case 6:
						view.printMessage("\n Latitud baja:");
						String lowLatitude = lector.next();
						view.printMessage("\n Latitud alta:");
						String highLatitude = lector.next();
						view.printMessage("\n Clase de vehiculo:");
						String vehicleClass1 = lector.next();
						
						view.printMessage("\n Buscando...");
						view.printFeatures( modelo.searchFeaturesByLatitudeAndVehicleType(lowLatitude, highLatitude, vehicleClass1));
						
						
						break;
					case 7:
						view.printMessage("--------- \n Hasta pronto !! \n---------"); 
						lector.close();
						fin = true;
						break;
					case 8:
						view.printMessage("--------- \n Hasta pronto !! \n---------"); 
						lector.close();
						fin = true;
						break;
					case 9:
						view.printMessage("--------- \n Hasta pronto !! \n---------"); 
						lector.close();
						fin = true;
						break;
					case 10:
						view.printMessage("--------- \n Hasta pronto !! \n---------"); 
						lector.close();
						fin = true;
						break;
	
					default: 
						view.printMessage("--------- \n Opcion Invalida !! \n---------");
						break;
				}
			}
			catch( DataStructureException e ){
				view.printMessage( e.getMessage() );
			}
		}
		
	}	
	
	private void loadData(){
		view.printMessage("--------- \nCargando datos de comparendos...");
	    modelo = new Modelo();
	    modelo.loadDataList(DATA_PATH);
	    int featuresNumber = modelo.size();
	    Feature biggestIdFeature = modelo.getFeatureWithBiggestId();
	    view.printGeneralFeaturesInfo(featuresNumber, biggestIdFeature);
	}
	
}
