import automata.DFA;
import automata.FA;
import automata.State;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Test;
import parser.ToAF;

import utils.Triple;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cesar
 */

    public class ToAfTest {
    
    @Test
    public void test1(){    
        String regularExp = "a|b";
        assertTrue(ToAF.ExpToAF(regularExp).accepts("a"));
        assertTrue(ToAF.ExpToAF(regularExp).accepts("b"));
       // assertTrue(ToAF.ExpToAF(regularExp).accepts("aaabba"));
        //assertTrue(ToAF.ExpToAF(regularExp).accepts("aaabb"));
       // assertTrue(ToAF.ExpToAF(regularExp).accepts("bbbb"));
        assertFalse(ToAF.ExpToAF(regularExp).accepts("ab"));

        
    }
    
    @Test
    public void test2(){
                String regularExp = "a*";
        assertTrue(ToAF.ExpToAF(regularExp).accepts("aaaa"));
        assertTrue(ToAF.ExpToAF(regularExp).accepts("a"));
        
    }
	@Test
        public void test3(){
            String regularExp= "(a|b)*";
            FA a= ToAF.ExpToAF(regularExp);
            assertTrue(a.accepts("aaaa"));
            assertTrue(a.accepts("bbbbb"));
            assertTrue(a.accepts(""));
            assertTrue(a.accepts("abababababab"));
            
        }
        
        	@Test
        public void test4(){
            String regularExp= "(a.b)";
            FA a= ToAF.ExpToAF(regularExp);
            assertTrue(a.accepts("ab"));
            assertFalse(a.accepts("a"));
            assertFalse(a.accepts("b"));
            
        }
        
                	@Test
        public void test5(){
            String regularExp= "(a.b*)";
            FA a= ToAF.ExpToAF(regularExp);
            assertTrue(a.accepts("abbbbbbbb"));
            assertFalse(a.accepts("abbbbba"));
            assertTrue(a.accepts("a"));
            
        }
        
                	@Test
        public void test6(){
            String regularExp= "(a.b)*";
            FA a= ToAF.ExpToAF(regularExp);
            assertTrue(a.accepts("abababab"));
            assertTrue(a.accepts("ab"));
            assertTrue(a.accepts(""));
            assertFalse(a.accepts("aaaabbbb"));
            
        }
                   	@Test
        public void test7(){
            String regularExp= "((a|b).c)";
            FA a= ToAF.ExpToAF(regularExp);
            assertTrue(a.accepts("ac"));
            assertTrue(a.accepts("bc"));
            assertFalse(a.accepts(""));
        
    }
    }