package test.logic;

import static org.junit.Assert.*;

import model.logic.Feature;
import model.logic.Modelo;

import org.junit.Before;
import org.junit.Test;

public class TestModelo {
	
	private Modelo modelo;
	private Feature dato;
	
	@Before
	public void setUp1() {
		modelo= new Modelo();
	}


	@Test
	public void testModelo() {
		assertTrue(modelo!=null);
		assertEquals(0, modelo.getFeaturesSize());  // Modelo con 0 elementos presentes.
	}

	@Test
	public void testDarTamano() {
		setUp1();
		
		assertTrue("El precio debe ser mayor a cero", modelo.getFeaturesSize()>0 );
		assertEquals( "El tamaño debería ser 150", 150, modelo.getFeaturesSize() );
	}

	@Test
	public void testAgregar() {
		setUp1();
		
		assertEquals( "No deben existir datos", 0, modelo.getFeaturesSize());

        try
        {
            modelo.agregar(dato);
            assertEquals( "Debería existir 1 dato", 1, modelo.getFeaturesSize());
        }
        catch (Exception e)
        {
        	fail("Deberia existir al menos un dato");
        }
	}

	@Test
	public void testBuscar() {
		setUp1();
		// TODO Completar la prueba
		try {
			modelo.buscar(dato.getObjectId());
			
		}
		catch (Exception e1)
		{
			fail ("No encontro nada");
		}
		try
		{
			assertTrue("El id debe ser mayor o igual cero", dato.getObjectId()>=0);
		}
		catch (Exception e2)
		{
			fail ("El id es menor a cero");
		}
	}
	
	public void getFirstFeature(){
		setUp1();
		try
        {
            modelo.getFirstFeature();
            fail( "No debería encontrar un dato" );
        }
        catch( Exception e1 ){	
        }

        try
        {
            assertNull( "No deberia haber nada antes", modelo.getFirstFeature() );
        }
        catch( Exception e2 )
        {
            fail( "Hay un dato antes" );
        }
	}
	
	public void getLastFeature(){
		setUp1();
		try
        {
            modelo.getLastFeature();
            fail( "No debería un dato" );
        }
        catch( Exception e1 ){
        }

        try
        {
            assertNull( "No deberia haber ningun dato despues",modelo.getLastFeature());
        }
        catch( Exception e2 )
        {
            fail( "Hay un dato despues" );
        }
	}
}
