import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import automata.DFA;
import automata.FA;
import automata.NFA;
import automata.NFALambda;


public class IntegrationTests {
	
	static DFA my_dfa;
	
	static NFA my_nfa;
	
	static NFALambda my_nfalambda;
        
        static NFALambda my_nfalambda2;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
                my_dfa = (DFA) FA.parse_form_file("test/dfa1.dot");
                my_nfa = (NFA) FA.parse_form_file("test/nfa1.dot");
                my_nfalambda = (NFALambda) FA.parse_form_file("test/nfalambda1.dot");
                my_nfalambda2 = (NFALambda) FA.parse_form_file("test/nfalambda2.dot");               
        }

	@Test
	public void test1() {
		assertTrue(my_dfa.toNFA().accepts("ab"));
		assertTrue(my_dfa.toNFA().accepts("abbbbb"));
		assertFalse(my_dfa.toNFA().accepts("bbbbb"));
		assertFalse(my_dfa.toNFA().accepts("a"));
	}
	
	@Test
	public void test2() {  
                    assertTrue(my_nfa.toDFA().accepts("ab"));
                    assertTrue(my_nfa.toDFA().accepts("abaaaaa"));
                    assertFalse(my_nfa.toDFA().accepts("abbbb"));
                    assertFalse(my_nfa.toDFA().accepts("a"));
	}
	
	@Test
	public void test3() {
		assertTrue(my_nfalambda.toDFA().accepts("casa"));
		assertTrue(my_nfalambda.toDFA().accepts("asa"));
		assertFalse(my_nfalambda.toDFA().accepts("cas"));
		assertFalse(my_nfalambda.toDFA().accepts("asac"));                
                
                assertFalse(my_nfalambda.toDFA().accepts("c"));
                assertFalse(my_nfalambda.toDFA().accepts("s"));
                
	}
        
        @Test
        public void nfaLambdaToDfa() {        
            assertTrue(my_nfalambda2.toDFA().accepts("casa"));
            assertTrue(my_nfalambda2.toDFA().accepts("casacasa"));
            assertFalse(my_nfalambda2.toDFA().accepts("cas"));
            assertFalse(my_nfalambda2.toDFA().accepts(""));
        }
	
}
