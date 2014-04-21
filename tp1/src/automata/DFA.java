package automata;

import java.util.Iterator;
import java.util.Set;

import utils.Triple;

/* Implements a DFA (Deterministic Finite Atomaton).
 */
public class DFA extends FA {

    /*	
     * 	Construction
     */
    // Constructor
    public DFA(
            Set<State> states,
            Set<Character> alphabet,
            Set<Triple<State, Character, State>> transitions,
            State initial,
            Set<State> final_states)
            throws IllegalArgumentException 
    {
        _states= states;
        _alphabet=alphabet;
        _transitions=transitions;
        _initial=initial;
        _final_states= final_states;
        if (!rep_ok()){
            throw new  IllegalArgumentException();
        }
        System.out.println("Is a DFA");
    }

    /*
     *	State querying 
     */
    @Override
    public Set<State> states() {
        // TODO
        return null;
    }

    @Override
    public Set<Character> alphabet() {
        // TODO
        return null;
    }

    @Override
    public State initial_state() {
        // TODO
        return null;
    }

    @Override
    public Set<State> final_states() {
        // TODO
        return null;
    }

    @Override
    public State delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        Iterator i=_transitions.iterator();
        Triple<State, Character, State> aux;
        State result=null;
        while (i.hasNext()){
            aux=(Triple<State, Character, State>) i.next();
       
            if (c.equals(aux.second()) && aux.first().equals(from)) //PROBLEMA CON ; DE FROM
                {
                result=aux.third();      
            }
        }
        return result;
    }   

    @Override
    public String to_dot() {
        assert rep_ok();
        String aux;
        aux = "digraph{\n";
        aux = aux + "inic[shape=ponit];\n" + "inic->" + this._initial.name() + ";\n";
        while (this._transitions.iterator().hasNext()) {
           Triple triupla = this._transitions.iterator().next();
           aux = aux + triupla.first().toString() + "->" + triupla.third().toString() + " [label=" + triupla.second().toString() + "];\n";
        }
        aux = "\n";
        while (this._final_states.iterator().hasNext()){
            State estado = this._final_states.iterator().next();
            aux = aux + estado.name() + "[shape=doublecircle];\n";
        }
        aux = aux + "}";
        return aux;
    }

  /*
     *  Automata methods
     */
    @Override
    public boolean accepts(String string) {
        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        State actual = _initial;
        int lenght = string.length(); 
	int index = 0;
	while (lenght != 0){
            
            Character caracterActual = string.charAt(index);
            
            actual = delta(actual,caracterActual);
            lenght--; 
            index ++;
           
            
            if (actual == null)
                return false;
        }
        
	return _final_states.contains(actual);
        
    }


    /**
     * Converts the automaton to a NFA.
     *
     * @return NFA recognizing the same language.
     */
    public NFA toNFA() {
        assert rep_ok();
        // TODO
        return null;
    }

    /**
     * Converts the automaton to a NFALambda.
     *
     * @return NFALambda recognizing the same language.
     */
    public NFALambda toNFALambda() {
        assert rep_ok();
        // TODO
        return null;
    }

    /**
     * Checks the automaton for language emptiness.
     *
     * @returns True iff the automaton's language is empty.
     */
    public boolean is_empty() {
        assert rep_ok();
        // TODO
        return false;
    }

    /**
     * Checks the automaton for language infinity.
     *
     * @returns True iff the automaton's language is finite.
     */
    public boolean is_finite() {
        assert rep_ok();
        // TODO
        return false;
    }

    /**
     * Returns a new automaton which recognizes the complementary language.
     *
     * @returns a new DFA accepting the language's complement.
     */
    public DFA complement() {
        assert rep_ok();
        // TODO
        return null;
    }

    /**
     * Returns a new automaton which recognizes the kleene closure of language.
     *
     * @returns a new DFA accepting the language's complement.
     */
    public DFA star() {
        assert rep_ok();
        // TODO
        return null;
    }

    /**
     * Returns a new automaton which recognizes the union of both languages, the
     * one accepted by 'this' and the one represented by 'other'.
     *
     * @returns a new DFA accepting the union of both languages.
     */
    public DFA union(DFA other) {
        assert rep_ok();
        assert other.rep_ok();
        // TODO
        return null;
    }

    /**
     * Returns a new automaton which recognizes the intersection of both
     * languages, the one accepted by 'this' and the one represented by 'other'.
     *
     * @returns a new DFA accepting the intersection of both languages.
     */
    public DFA intersection(DFA other) {
        assert rep_ok();
        assert other.rep_ok();
        // TODO
        return null;
    }

    @Override
    public boolean rep_ok() {
        boolean containLambda= false;
        boolean statesOK=true;
        boolean transitionOK= true;
        boolean nonDeterministic= false;
        //Check that the alphabet does not contains lambda.
        for(Character c: _alphabet){
            if (c== Lambda){
                containLambda=true;
                System.out.println("ContainLambda");
            }                                      
        }
        //Check that final states are included in 'states'.
        for(State s:_final_states){
            statesOK= _states.contains(s) && statesOK;
        }
        //Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
        for(Triple<State,Character,State> t:_transitions){
            transitionOK= _states.contains(t.first()) && _states.contains(t.third()) && _alphabet.contains(t.second()) && transitionOK;
            //Check that the transition relation is deterministic.
            for(Triple<State,Character,State> l: _transitions){       
                //Is non deterministic if have more 1 transition from Qi to any node within the same label
                if ( t.first().equals(l.first()) && !t.third().equals(l.third())  && t.second()==l.second()){ 
                    nonDeterministic=true;
                }
            }
        }
        //Check that the transition relation is deterministic.
        return _states.contains(_initial) && !containLambda && !nonDeterministic && transitionOK && statesOK;
    }
}
