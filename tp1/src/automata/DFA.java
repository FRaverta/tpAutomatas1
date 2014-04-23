package automata;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
       
    private   Object _nroStates[] ; //array used to give a number to each state
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
        _nroStates=  _states.toArray();
        if (!rep_ok()){
            throw new  IllegalArgumentException();
        }
        System.out.println("Is a DFA");
    }
   
    /*
     *	State querying 
     */
    

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

  /*
     *  Automata methods
     */
    @Override
    public boolean accepts(String string) {
        assert rep_ok();
        assert string != null;
        assert verify_string(string);
        State actual = _initial;
        //int lenght = string.length(); 
	//int index = 0;
	/*while (lenght != 0){   
            Character caracterActual = string.charAt(index);        
            actual = delta(actual,caracterActual);
            lenght--; 
            index ++;     
            if (actual == null)
                return false;
        }*/
        for(Character c: string.toCharArray()){
            actual= delta(actual,c);
            if (actual==null){
                return false;
            }
        }
        return _final_states.contains(actual);       
    }

    /**
     * Converts the automaton to a NFA.
     *
     * @return NFA recognizingthe same language.
     */
    public NFA toNFA() {
        assert rep_ok();
        String name="ficticio";
        State ficticio = new State(name);
        _states.add(ficticio);
        Triple<State,Character,State> t = new Triple(_initial,_alphabet.iterator().next(),ficticio);
        _transitions.add(t);
        for (Character c:_alphabet){
            //Character c = this.alphabet().iterator().next();
            t = new Triple(ficticio, c, ficticio);
           _transitions.add(t);
        }
        NFA noDet = new NFA(_states,_alphabet,_transitions,_initial,_final_states);
        return noDet;
    }

    /**
     * Converts the automaton to a NFALambda.
     *
     * @return NFALambda recognizing the same language.
     */
    public NFALambda toNFALambda() {
        assert rep_ok();
        NFALambda noDetLambda = new NFALambda(_states,_alphabet,_transitions,_initial,_final_states);
        String name="ficticio";
        State ficticio = new State(name);
        noDetLambda._states.add(ficticio);
        Triple<State,Character,State> t = new Triple(noDetLambda._initial,FA.Lambda,ficticio);
        noDetLambda._transitions.add(t);
        for (Character c:_alphabet){
            t = new Triple(ficticio, c, ficticio);
            noDetLambda._transitions.add(t);
        }
        return noDetLambda;
    }

    /**
     * Checks the automaton for language emptiness.
     *
     * @returns True iff the automaton's language is empty.
     */
    public boolean is_empty() {
        assert rep_ok();
        int [][] matriz= this.clausuraTransitiva();
        String nameInicial = _initial.name();
        nameInicial = nameInicial.substring(1,nameInicial.length());
        Iterator i= _final_states.iterator();
        while (i.hasNext()){
            State fin= (State)i.next();
            String nameFinal= fin.name();
            nameFinal = nameFinal.substring(1,nameFinal.length());
            int numFin = Integer.parseInt(nameFinal);
            if (matriz[0][numFin]==0){ //If there is a path from the initial to some end
                    return true;
            }
        }
        return false;
    }

    //method that returns the index where is a given state
    private int findIndex(State S){
        for (int i=0; i< _nroStates.length; i++){
            if (_nroStates[i].equals(S)){
                return i;
            }
        }
        return -1;
    }
    
    //Method that builds a matrix corresponding to the relation.
    public int[][] matrizRelation (int size){
        int [][] matriz= new int[size][size];
        State a;
        for (State b:_states) {
            int numB=findIndex(b); 
               for(Character alpha:_alphabet){
                   System.out.println(numB);
                a=delta(b,alpha);
                    if (a!=null){
                        int numA=findIndex(a); 
                        System.out.print(numA);
                        matriz[numB][numA]=1;
                    } 
                }
        }
        return matriz;
  }

    //Method based in Wharsall that computes the transitive closure of a relation
        public int[][] clausuraTransitiva() {
        int[][] clausura = this.matrizRelation(_states.size());
        for (int m = 0; m < _states.size(); m++) {
            for (int i = 0; i < _states.size(); i++) {
                for (int j = 0; j < _states.size(); j++) {
                    if (clausura[i][j] == 1) {
                        for (int k = 0; k < _states.size(); k++) {
                            if (clausura[j][k] == 1) {
                                clausura[i][k] = 1;
                            }
                        }
                    }
                }
            }
        } 
        
        return clausura;
    }
    /**
     * Checks the automaton for language infinity.
     *
     * @returns True iff the automaton's language is finite.
     */
    public boolean is_finite() {
        assert rep_ok();
        int [][] relaciones = clausuraTransitiva();
        for(State fin:_final_states){
            int columnaFinal = findIndex(fin);  ;
            System.out.print(columnaFinal);
            for(int j=0; j<_states.size(); j++){    
                if((relaciones[j][columnaFinal]==1)){  //las transciones que lleguen a un final
                    int nroEstadoInicial= findIndex(_initial);  
                    if (relaciones[nroEstadoInicial][j]==1){ //transiciones que desde el inicial pueden llegar a aquellos que llegan al final
                        if(relaciones[j][j]==1){ //Si hay ciclo.
                            return false; 
                        }
                    }               
                }
            } 
        }
        return true;
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
                transitions.add(new Triple(initial,t.second(),t.third()));
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
                transitions.add(new Triple(initial,t.second(),t.third()));
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
