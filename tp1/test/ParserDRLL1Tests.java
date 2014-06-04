/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nando
 */
import static org.junit.Assert.*;
import org.junit.Test;
import parser.DRLL1;

public class ParserDRLL1Tests {
    
    @Test
    public void test1() throws Exception {
            assertTrue(DRLL1.parser("a"));
            assertTrue(DRLL1.parser("n"));
            assertTrue(DRLL1.parser("z"));
            assertFalse(DRLL1.parser("az"));
            assertFalse(DRLL1.parser("ba"));
            assertFalse(DRLL1.parser("1"));
    }
    
    @Test
    public void test2() throws Exception {
            assertTrue(DRLL1.parser("(a)"));
            assertTrue(DRLL1.parser("(n)"));
            assertTrue(DRLL1.parser("(z)"));
            assertFalse(DRLL1.parser("(a"));
            assertFalse(DRLL1.parser("((a)"));
            assertFalse(DRLL1.parser(")("));            
    }
    
    @Test
    public void test3() throws Exception {
            assertTrue(DRLL1.parser("a.b"));
            assertTrue(DRLL1.parser("a.n.c"));
            assertTrue(DRLL1.parser("a.z"));
            assertFalse(DRLL1.parser("."));
            assertFalse(DRLL1.parser("s."));
            assertFalse(DRLL1.parser("s.a."));   
    }

    @Test
    public void test4() throws Exception {
            assertTrue(DRLL1.parser("a|b"));
            assertTrue(DRLL1.parser("a|n|c"));
            assertTrue(DRLL1.parser("a|z"));
            assertFalse(DRLL1.parser("|"));
            assertFalse(DRLL1.parser("a|"));
            assertFalse(DRLL1.parser("a||b"));           
    }
    
    @Test
    public void test5() throws Exception {
            assertTrue(DRLL1.parser("a*"));
            assertTrue(DRLL1.parser("b*"));
            assertFalse(DRLL1.parser("*"));
    }

    @Test
    public void test6() throws Exception {
            assertTrue(DRLL1.parser("(a.b)*"));
            assertTrue(DRLL1.parser("(a|b)*"));
            assertTrue(DRLL1.parser("((a.b).c)*"));
            assertTrue(DRLL1.parser("((a.b|z)*|(d))"));
            assertFalse(DRLL1.parser(".*"));
            assertFalse(DRLL1.parser("a|bb"));
            assertFalse(DRLL1.parser("aa.d*"));

    }


    
}
