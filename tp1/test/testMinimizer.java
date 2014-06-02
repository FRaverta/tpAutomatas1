/*
 * Class for test DFA/Mimimizer method
 */

/**
 *
 * @author nando
 */

import automata.DFA;
import automata.FA;
import static org.junit.Assert.*;
import org.junit.Test;
        
public class testMinimizer {

        @Test
	public void test1() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/dfa1.dot");
		DFA minimum= dfa.minimizer();
                assertTrue(minimum.accepts("abb"));
                assertTrue(minimum.accepts("abbbb"));
                assertTrue(minimum.accepts("abbbbbbbbbb"));
                assertFalse(minimum.accepts("a"));
                assertFalse(minimum.accepts("aba"));
                assertFalse(minimum.accepts("b"));
                assertFalse(minimum.accepts("bb"));
                assertFalse(minimum.accepts(""));
	}     
        
        @Test
	public void test2() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/dfa2.dot");
		DFA minimum= dfa.minimizer();
                assertTrue(minimum.accepts("bb"));
                assertTrue(minimum.accepts("bbbb"));
                assertTrue(minimum.accepts("bbbbbb"));
                assertTrue(minimum.accepts(""));
                assertFalse(minimum.accepts("b"));
                assertFalse(minimum.accepts("bbb"));
                assertFalse(minimum.accepts("bbbbb"));              
	}          

        @Test
	public void test3() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/dfa3.dot");
		DFA minimum= dfa.minimizer();
                assertFalse(minimum.accepts("aa"));
                assertFalse(minimum.accepts("aaaa"));
                assertFalse(minimum.accepts("aaaaaa"));
                assertFalse(minimum.accepts(""));
                assertTrue(minimum.accepts("a"));
                assertTrue(minimum.accepts("aaa"));
                assertTrue(minimum.accepts("aaaaa"));              
	}          
    
        
        @Test
	public void test4() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/dfa4.dot");
		DFA minimum= dfa.minimizer();
                assertTrue(minimum.accepts("automatas"));
                assertTrue(minimum.accepts("lenguajes"));
                assertTrue(minimum.accepts("y"));
                assertFalse(minimum.accepts("auto"));
                assertFalse(minimum.accepts("guajes"));
                assertFalse(minimum.accepts(""));
	}

        @Test
	public void test5() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/dfa5.dot");
		DFA minimum= dfa.minimizer();
                assertTrue(minimum.accepts("01"));
                assertTrue(minimum.accepts("1100101001"));
                assertTrue(minimum.accepts("00110"));
                assertFalse(minimum.accepts("01110"));
                assertFalse(minimum.accepts("1100"));
                assertFalse(minimum.accepts("000000"));
                assertFalse(minimum.accepts(""));
	}

        @Test
	public void testEquivalent() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/dfa5.dot");
                DFA dfaMinimum= (DFA) FA.parse_form_file("test/dfa5Minimum.dot");
                assertTrue(dfa.areEquivalent(dfaMinimum));                
        }
        
        
        @Test
	public void testEquivalent2() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("test/dfa5.dot");
                DFA dfaMinimum= (DFA) FA.parse_form_file("test/dfa1.dot");
                assertFalse(dfa.areEquivalent(dfaMinimum));                
        }

        
}
