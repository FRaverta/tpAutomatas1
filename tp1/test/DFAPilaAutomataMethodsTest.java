
import automata.AP;
import automata.DFA;
import automata.DFAPila;
import automata.FA;
import java.io.FileWriter;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cesar
 */
public class DFAPilaAutomataMethodsTest {
public static String ruta ="";

@Test
	public void test1() throws Exception {
		DFAPila dfapila = (DFAPila) AP.parse_form_file("test/DFAPila1.dot");
                try{
                    FileWriter w= new FileWriter("/home/nando/Desktop/test.dot");
                    w.write(dfapila.to_dot());
                    w.close();
               }catch(Exception e){
                     assert(false);
               }
        
		assertTrue(dfapila.accepts("ab"));
                assertFalse(dfapila.accepts("abb"));
	}

	@Test
	public void test2() throws Exception {
		DFAPila dfapila = (DFAPila) AP.parse_form_file("test/DFAPila2.dot");
		assertTrue(dfapila.accepts("aabb"));
	}
    /*    
        @Test
        public void test3() throws Exception {
		DFAPila dfapila = (DFAPila) AP.parse_form_file("test/DFAPIla.dot");
		assertFalse(dfapila.accepts("aaabbba"));
	}
	
	@Test
	public void test4() throws Exception {
		DFAPila dfapila = (DFAPila) AP.parse_form_file("test/DFAPIla.dot");
		assertTrue(dfapila.accepts("aaaaabbb"));
	}
*/
}
