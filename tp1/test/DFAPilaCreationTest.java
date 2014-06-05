
import automata.DFA;
import automata.DFAPila;
import automata.State;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import utils.Quintupla;
import utils.Triple;



/**
 *
 * @author cesar
 */
public class DFAPilaCreationTest {
    
//    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void creation_test1() {
		Set<State> states = new HashSet<State>();
		Set<Character> alpha = new HashSet<Character>();
		Set<Quintupla<State,Character,Character,String,State>> transitions = new HashSet<Quintupla<State,Character,Character,String,State>>();
		State initial;
		Set<State> finals = new HashSet<State>();
		Set<Character> stackAlphabet = new HashSet();
		State s0 = new State("s0");
		State s1 = new State("s1");
		State s2 = new State("s2");
		states.add(s0);
		states.add(s1);
		states.add(s2);
		alpha.add('a');
		alpha.add('b');
                stackAlphabet.add('a');
                stackAlphabet.add('b');
                stackAlphabet.add('@');
     
		transitions.add(new Quintupla(s0, 'a','@',"a", s1));
		transitions.add(new Quintupla(s1, 'a','a',"aa", s1));
		transitions.add(new Quintupla(s1, 'b','a',"_", s2));
		transitions.add(new Quintupla(s2, 'b','b',"_", s2));
		initial = s0;
		finals.add(s1);
		
		DFAPila my_dfapila = new DFAPila(states, alpha,stackAlphabet, transitions,'Z', initial, finals);
		assertTrue(my_dfapila.rep_ok());
			
	}

}
