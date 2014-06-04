/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import static automata.FA.Lambda;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import utils.Quintupla;
import utils.Triple;

/**
 *
 * @author cesar
 */
public final class DFAPila extends AP {

    private   Object _nroStates[] ;
    private Stack<Character> stack;
    
    public DFAPila(
            Set<State> states,
            Set<Character> alphabet,
            Set<Character> stackAlphabet,
            Set<Quintupla<State, Character,Character,String, State>> transitions,
            Character stackInitial,
            State initial,
            Set<State> final_states)
            throws IllegalArgumentException 
    {
        _states= states;
        _alphabet=alphabet;
        _stackAlphabet= stackAlphabet;
        _transitions=transitions;
        _stackInitial=stackInitial;
        _initial=initial;
        _final_states= final_states;
        _nroStates=  _states.toArray();
        _stack= new Stack<Character>();
        if (rep_ok()){
            throw new  IllegalArgumentException();
        }
        System.out.println("Is a DFA Pila");
    }
        
        
    public State delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        Iterator i=_transitions.iterator();
        Quintupla<State, Character,Character,String, State> aux;
        State result=null;
        while (i.hasNext()){
            aux=(Quintupla<State, Character,Character,String, State>) i.next();
            if (c.equals(aux.second()) && aux.first().equals(from)) {
                if (!aux.third().equals(Comodin)){
                    Character pop = _stack.pop();
                    if (aux.third().equals(pop)) {
                        for (int j = 0; j < aux.fourth().length(); j++){
                            if (!c.equals('-')) _stack.push(aux.fourth().charAt(j));
                        }
                    result=aux.fifth();      
                    }else{
                    if(!c.equals('_'))
                        _stack.push(pop);
                    }
                }else{
                    for (int j = 0; j < aux.fourth().length(); j++){
                        _stack.push(aux.fourth().charAt(j));
                    }
                    result = aux.fifth();
                }
            }
        }
       return result;  
    }
    
    public boolean accepts(String string) {
        assert string != null;
        assert verify_string(string);
        
        State actual = _initial;
        int i = 0;
        boolean res = true;
        
        while (i < string.length() && res){
            actual = (State) delta(actual, string.charAt(i));
            if (actual != null){
                i++;
            }else{
                res= false;
            }
        }
        res = res && _final_states.contains(actual);
        return res;
    }

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
        for(Quintupla<State,Character,Character,String,State> t:_transitions){
            transitionOK= _states.contains(t.first()) && _states.contains(t.fifth()) && _alphabet.contains(t.second()) && _stackAlphabet.contains(t.third()) && transitionOK;
            for (int i = 0; i<t.fourth().length(); i++ ){
                transitionOK = transitionOK && ((_stackAlphabet.contains(t.fourth().charAt(i)) || (_stackAlphabet.contains(Lambda))));
            }
            //Check that the transition relation is deterministic.
            for(Quintupla<State,Character,Character,String,State> l: _transitions){       
                //Is non deterministic if have more 1 transition from Qi to any node within the same label
                if ( t.first().equals(l.first()) && !t.fifth().equals(l.fifth())  && t.second()==l.second()){ 
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
    
   private boolean verify_string(String string) {
        boolean ok=true;
        for(Character c: string.toCharArray()){
            if (!_alphabet.contains(c)){
                ok=false;
            }
        }
        return ok;    
   }
    
}

    
