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
	
	static final String DATA_PATH = "./data/comparendos_dei_2018_small.geojson";
	
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

		loadData();
		
		Scanner lector = new Scanner(System.in);
		boolean fin = false;

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){
				case 1:
					view.printMessage("--------- \nFecha: ");
					String date = lector.next();
					view.printFeatures( modelo.searchFeaturesByDate(date) );
					break;
					
				case 2:
					view.printMessage("--------- \nPrimera Fecha: ");
					String date1 = lector.next();
					view.printMessage("--------- \nSegunda Fecha: ");
					String date2 = lector.next();
					view.printFeaturesComparatedByDate(
							date1,
							date2,
							modelo.searchFeaturesNumberByDate(date1),
							modelo.searchFeaturesNumberByDate(date2)
					);
					break;
					
				case 3:
					view.printMessage("--------- \nNumero de ID: ");
					int ID = Integer.parseInt( lector.next() );
					Feature featureFounded = modelo.searchFeature(ID);
					view.printFeature( featureFounded );				
					break;
					
				case 4: 
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
	
	private void loadData(){
		view.printMessage("--------- \nCargando datos de comparendos...");
	    modelo = new Modelo();
	    modelo.loadData(DATA_PATH);
	    int featuresNumber = modelo.getFeaturesSize();
	    Feature biggestIdFeature = modelo.getFeatureWithBiggestId();
	    double[] minmax = modelo.getMinmax();
	    view.printGeneralFeaturesInfo(featuresNumber, biggestIdFeature, minmax);
	}
	
}
