
import automata.AP;
import automata.DFAPila;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;



/**
 *
 * @author cesar
 */
public class DFAPilaAutomataMethodsTest {


    @Test
    public void test1() throws Exception {
	DFAPila dfapila = (DFAPila) AP.parse_form_file("test/DFAPIla.dot");
	
        assertTrue(dfapila.accepts("aaaaaaaaaaaaaaaaaaaaab"));
                
        assertFalse(dfapila.accepts("bbbb"));
                
        assertTrue(dfapila.accepts("ab"));
                
        assertFalse(dfapila.accepts("aaabbba"));
                
        assertFalse(dfapila.accepts("abaa"));
                
        assertFalse(dfapila.accepts("aaaaabbbbbbbbb"));
                
    }   

    @Test	
    public void test2() throws Exception {
        DFAPila dfapila = (DFAPila) AP.parse_form_file("test/dfapila2.dot");
		
        assertTrue(dfapila.accepts("aaaaaaaaaaaaaaaaaaaaab"));
                
        assertTrue(dfapila.accepts("aaabb"));
                
        assertFalse(dfapila.accepts("aaabbba"));
                
        assertFalse(dfapila.accepts("abaa"));
                
        assertFalse(dfapila.accepts("bbbb"));
              
        assertFalse(dfapila.accepts("aaaaabbbbbbbbbbbbbbbbbbb"));
                
        assertTrue(dfapila.accepts("aaaabbb"));
                
        assertTrue(dfapila.accepts("aaaaabbbbb"));
                
    } 


}
