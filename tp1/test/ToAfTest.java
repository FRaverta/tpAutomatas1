
import automata.DFA;
import automata.FA;
import automata.State;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
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
       
        
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void test1(){
        Set<State> states = new HashSet<State>();
        Set<Character> alpha = new HashSet<Character>();
        Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
        State initial;
        Set<State> finals = new HashSet<State>();
	
        State s0 = new State("s0");
	State s1 = new State("s1");
	
        states.add(s0);
        states.add(s1);
        
        alpha.add('a');
        
        transitions.add(new Triple(s0,'a',s1));
        transitions.add(new Triple(s1,'a',s1));
        
        finals.add(s1);
        finals.add(s0);
        
        initial=s0;
        DFA my_dfa = new DFA(states,alpha,transitions,initial,finals);
        assertTrue(my_dfa.rep_ok());
        
        String regularExp = "a*";
        
        
        
    }
    
    /*
    @Test
    public void test2(){
        Set<State> states = new HashSet<State>();
        Set<Character> alpha = new HashSet<Character>();
        Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
        State initial;
        Set<State> finals = new HashSet<State>(); 
        
        State s0 = new State("s0");
        State s1 = new State("s1");
        State s2 = new State("s2");
        
        alpha.add('a');
        alpha.add('b');
        
        transitions.add(new Triple(s0,'a',s1));
        transitions.add(new Triple(s0,'b',s2));
        transitions.add(new Triple(s1,'a',s1));
        transitions.add(new Triple(s2,'b',s2));
        
        finals.add(s2);
        finals.add(s1);
        finals.add(s0);
        
        initial = s0;
        
        DFA my_dfa = new DFA(states,alpha,transitions,initial,finals);
        assertTrue(my_dfa.rep_ok());
        
        String regularExp = "a*|b*";
    }
    
    @Test
    public void test3(){
        Set<State> states = new HashSet<State>();
        Set<Character> alpha = new HashSet<Character>();
        Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
        State initial;
        Set<State> finals = new HashSet<State>(); 
        
        State s0 = new State("s0");
        State s1 = new State("s1");
        State s2 = new State("s2");
        
        alpha.add('a');
        alpha.add('b');
        
        transitions.add(new Triple(s0,'a',s1));
        transitions.add(new Triple(s1,'a',s1));
        transitions.add(new Triple(s1,'b',s2));
        transitions.add(new Triple(s2,'b',s2));
        
        finals.add(s2);
        
        initial = s0;
        
        DFA my_dfa = new DFA(states,alpha,transitions,initial,finals);
        assertTrue(my_dfa.rep_ok());
        
        String regularExp = "a(a)*.b(b)*";
        
    }
    */
    }
