package test.data_structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import model.data_structures.Queue;
import model.logic.Feature;
import model.data_structures.DataNode;

public class TestQueue {

	private Queue<Feature> queue;
	private DataNode<Feature> node;
	
	public void setUp1()
	{
		queue = new Queue<Feature>();
	}
	
	
	public void testsize()
	{
		setUp1();
		
		assertTrue("El tamaño debe ser mayor a cero", queue.size()>0 );
		assertEquals( "El tamaño debería ser 150", 150, queue.size() );
	}

	public void testenqueue()
	{
		setUp1();
		
		assertEquals( "No deben existir nodos", 0, queue.size() );
        try
        {
            queue.enqueue( node.getNodeInfo() );
            assertEquals( "Debería existir 1 nodo", 1, queue.size());
        }
        catch (Exception e)
        {
        	fail("Deberia existir al menos un nodo");
        }
	}

	public void testGetFront() 
	{
		setUp1();
		try
        {
            queue.getFront();
            fail( "No debería encontrar un nodo" );
        }
        catch( Exception e1 ){	
        }

        try
        {
            assertNull( "El anterior deberia ser null", node.getPrevious() );
        }
        catch( Exception e2 )
        {
            fail( "El anterior no es null" );
        }
	}

}
