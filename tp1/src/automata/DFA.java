package automata;

import java.util.HashSet;
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
        return _states;
    }

    @Override
    public Set<Character> alphabet() {
        return _alphabet;
    }

    @Override
    public State initial_state() {
        return _initial;
    }

    @Override
    public Set<State> final_states() {
        return _final_states;
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
        Iterator i;
        String aux;
        aux = "digraph{\n";
        aux = aux + "inic[shape=point];\n" + "inic->" + this._initial.name() + ";\n";
        i=this._transitions.iterator();
        while (i.hasNext()) {
           Triple triupla =(Triple) i.next();
           aux = aux + triupla.first().toString() + "->" + triupla.third().toString() + " [label=" + triupla.second().toString() + "];\n";
        }
        aux = aux+ "\n";
        i=this._final_states.iterator();
        while (i.hasNext()){
            State estado = (State) i.next();
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
        DFA complement = new DFA(_states,_alphabet,_transitions,_initial,new HashSet());
        Iterator i=_states.iterator();
        State aux;
        while(i.hasNext()){
            aux= (State) i.next();
            if (!_final_states.contains(aux)){
                complement._final_states.add(aux);
            }
        }
        return complement;
    }

    /**
     * Returns a new automaton which recognizes the kleene closure of language.
     *
     * @returns a new DFA accepting the language's complement.
     */
    public DFA star() {
        assert rep_ok();
            DFA kleene = this;
            kleene._final_states.add(_initial);
            while (_states.iterator().hasNext()) {
                State estado = _states.iterator().next();
                for (Character c : _alphabet) {
                    Triple<State,Character,State> t = new Triple (estado, c, _initial);
                    kleene._transitions.add(t);
                }
            }
        return kleene;
    }

    /**
     * Returns a new automaton which recognizes the union of both languages, the
     * one accepted by 'this' and the one represented by 'other'.
     *
     * @returns a new DFA accepting the union of both languages.
     */
    //this=t and other=0
    public DFA union(DFA other) {
        assert rep_ok();
        assert other.rep_ok();
        FA union;
        Set<State> states=new HashSet();
        Set<State> final_states= new HashSet();
        Set<Triple<State,Character,State>> transitions=new HashSet();
        Set<Character> alphabet= new HashSet();
        State initial= new State("Uq0");
        states.add(initial);
        states.addAll(this._states);
        if (this._final_states.contains(this._initial) ||other._final_states.contains(other._initial)){
            final_states.add(initial);
        }
        for(State s: other._states){
            s.rename("B"+s.name()); //remember that JAVA has a passage of parameters by value
            states.add(s);
        }
        final_states.addAll(this._final_states);
        for(State s: other._final_states){
            s.rename("B"+s.name()); //remember that JAVA has a passage of parameters by value
            final_states.add(s);
        }        
        for(Triple<State,Character,State> t: this._transitions){
            transitions.add(t);
            if (t.first().equals(this._initial)){
                transitions.add(new Triple(initial,t.second(),t.first()));
            } 
        }
        for(Triple<State,Character,State> t: other._transitions){
            if (!t.first().name().startsWith("B")){
                t.first().rename("B"+t.first().name());
            }
            if (!t.third().name().startsWith("B")){
                t.third().rename("B"+t.third().name());
            }
            transitions.add(t);
            if (t.first().equals(other._initial)){
                transitions.add(new Triple(initial,t.second(),t.first()));
            } 
        }
        
        alphabet.addAll(this._alphabet);
        alphabet.addAll(other._alphabet);
        try{
            union= new DFA(states,alphabet,transitions,initial,final_states);
        }catch(IllegalArgumentException e){
            union= new NFA(states,alphabet,transitions,initial,final_states);
            union= ((NFA) union).toDFA(); //OJO! PUEDE NO SER UN DFA DEBERIA VER 
        }
        
        return (DFA) union;                
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
        System.out.println("Contain initial: "+ _states.contains(_initial));
        System.out.println("Contain lamda: "+ containLambda);
        System.out.println("nonDeterministic: "+ nonDeterministic);
        System.out.println("transitionsOK"+ transitionOK);
        System.out.println("statesOK"+ statesOK);
        
        
        
        return _states.contains(_initial) && !containLambda && !nonDeterministic && transitionOK && statesOK;
    }
}
