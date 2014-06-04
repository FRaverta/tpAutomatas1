
import automata.AP;
import automata.DFA;
import automata.DFAPila;
import automata.FA;
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
		DFAPila dfapila = (DFAPila) AP.parse_form_file("test/DFAPIla.dot");
		assertTrue(dfapila.accepts("abbb"));
	}

	@Test
	public void test2() throws Exception {
		DFAPila dfapila = (DFAPila) AP.parse_form_file("test/DFAPIla.dot");
		assertFalse(dfapila.accepts("abaa"));
	}
        
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

}
