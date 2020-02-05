package controller;

import java.util.Scanner;

import model.logic.Feature;
import model.logic.Modelo;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;
	
	/* Instancia de la Vista*/
	private View view;
	
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
		String respuesta = "";

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){
				case 1:
					view.printMessage("--------- \nCargando datos de comparendos...");
				    modelo = new Modelo();
				    modelo.loadDataList("./data/comparendos_dei_2018_small.geojson");
				    Feature firstFeature = modelo.getFirstFeature();
				    Feature lastFeature = modelo.getLastFeature();
				    int featuresNumber = modelo.getFeaturesSize();
				    view.printGeneralFeaturesInfo(firstFeature, lastFeature, featuresNumber);
					break;

				case 2:
					view.printMessage("--------- \nNumero de ID: ");
					int dato = Integer.parseInt( lector.next() );
					Feature featureFounded = modelo.buscar(dato);
					view.printFeature( featureFounded );				
					break;
					
				case 3: 
					view.printMessage("--------- \n Hasta pronto !! \n---------"); 
					lector.close();
					fin = true;
					break;	

				default: 
					view.printMessage("--------- \n Opcion Invalida !! \n---------");
					break;
			}
		}
		
	}	
}
