/********************************NFALambda.java*****************************************/
/* Archivo que implementa un Automata Finito No Deterministico con transiciones lambda */
/*                                                                                     */
/*Cornejo, Politano, Raverta                                                           */
/***************************************************************************************/

package automata;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.Triple;

public class NFALambda extends FA {

    /*
     *  Construction
     */
    // Constructor
    public NFALambda(
            Set<State> states,
            Set<Character> alphabet,
            Set<Triple<State, Character, State>> transitions,
            State initial,
            Set<State> final_states)
            throws IllegalArgumentException
    {
        _states= states;
        _alphabet=alphabet;
        _alphabet.remove(new Character (FA.Lambda));
        _transitions=transitions;
        _initial=initial;
        _final_states= final_states;
        if (!rep_ok()){
            throw new  IllegalArgumentException();
        }
        System.out.println("Is a NFALambda");
    }

    /*
     *	State querying 
     */
    
    @Override
    public Set<State> delta(State from, Character c) {
        assert states().contains(from);
        //assert alphabet().contains(c); Porque el LAMBDA no esta en el alfabeto
        Iterator i=_transitions.iterator();
        Triple<State, Character, State> aux;
        Set<State> result=new HashSet();
        while (i.hasNext()){
            aux=(Triple<State, Character, State>) i.next();
            if (aux.first().equals(from) &&  (c.equals(aux.second())|| (Lambda.equals(aux.second())))) //PROBLEMA CON ; DE FROM
                {
                result.add(aux.third()) ;     
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
        return accepts2(_initial,string);
        }
    
    public boolean containsOneFinal(Set<State> set){
        for(State fin:_final_states){
            for(State state:set){
                if(fin.equals(state))
                    return true;
            }
        }
        return false;
    }
            
    
    public boolean accepts2 (State estado, String string){
        if (string.isEmpty()){      
            Set<State> finalVacio=this.clausuraLambdaState(estado);
            return containsOneFinal(finalVacio);
        }
        boolean res = false;
        for(Triple<State, Character, State> tran:_transitions){
            if (tran.first().equals(estado) &&  tran.second().equals(string.charAt(0))) //si puedo ir a otro estado por el caracter a actual, entones disminuyo la cadena
                res = res || accepts2(tran.third(),string.substring(1));
               if (tran.first().equals(estado) &&  tran.second().equals(Lambda)) //Clausura LAMBDA, si puedo ir por lambda, no disminuyo la cadena solo muevo el estado
                res = res || accepts2(tran.third(),string);
        }
        return res;    
            }
    
    /**
     * Converts the automaton to a DFA.
     *
     * @return DFA recognizing the same language.
     */
    public DFA toDFA() {
        assert rep_ok();
        // TODO
        return null;
    }

        public Set<State> clausuraLambdaState(State s){
        //metodo que dado estado da la clausura lambda de ese estado
            Set<State> estados=new HashSet();
            int oldLength=0;
            estados.add(s);
            while(oldLength!= estados.size()){
                oldLength= estados.size();
                for (State a: estados){							
                    for (Triple<State, Character, State> transiciones: _transitions){
		        if (transiciones.second()==FA.Lambda && a.equals(transiciones.first())){
                            estados.add(transiciones.third()); //estos son los alcanzables desde el primero
                        }
                    }
                }
	}
        return estados;
    }
    
    
    @Override
    public boolean rep_ok() {
        boolean statesOK=true;
        boolean transitionOK= true;
        boolean containsLambda=false;
        //Check that final states are included in 'states'.
        for(State s:_final_states){
            statesOK= _states.contains(s) && statesOK;
        }
        //Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
        for(Triple<State,Character,State> t:_transitions){
            transitionOK= _states.contains(t.first()) && _states.contains(t.third()) && (_alphabet.contains(t.second()) || t.second().equals(Lambda)) && transitionOK;
            if (t.second()==FA.Lambda)
                containsLambda=true;
        }
        return _states.contains(_initial) && transitionOK && statesOK && containsLambda;
    }
}

