/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import java.util.Set;
import java.util.Stack;
import utils.Quintupla;


/**
 * Implements a Automaton Deterministic with stack
 * @author cesar,nando,mariano
 */
public final class DFAPila extends AP {

    /*
    constructor
    * */
    private   Object _nroStates[] ;
    private Stack<Character> stack; //the stack of the automaton
    
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
        _stackAlphabet.add(Lambda); //the character lambda is used in the stack to know when do a pop
        _stackAlphabet.add(Comodin); //the mark of the stack
        _transitions=transitions;
        _stackInitial=stackInitial;
        _initial=initial;
        _final_states= final_states;
        _nroStates=  _states.toArray();
        _stack= new Stack<Character>();
        _stack.add(Comodin); //insert the mark in the stack
        if (!rep_ok()){
            throw new  IllegalArgumentException();
        }
        System.out.println("Is a DFA Pila");
    }
        
    
    @Override    
    public State delta(State from, Character c){
        assert states().contains(from);
        assert alphabet().contains(c);
        assert rep_ok();
        State result = null;
        Quintupla<State, Character,Character,String, State> avaiableT = null;
        //search in all the transitions if exist a valid transition
        for(Quintupla<State, Character,Character,String, State> aux : _transitions) {
            boolean condition = true;
            condition &= aux.first().equals(from);
            condition &= aux.second().equals(c);// || aux.second().equals(Lambda);
            condition &= aux.third().equals(_stack.peek());
                if(condition) {
                    avaiableT=aux;
                    break;
                }
        }
        if (avaiableT!=null){   //exist a valid transition
            if (!avaiableT.third().toString().equals(avaiableT.fourth())){ //the transition do a modification in the stack
                if (!(avaiableT.fourth().equals("_"))){  //the transition makes a push in the stack      
                    if (!_stack.peek().equals(Comodin)){ //the top of the stack is not "@" the mark of the stack so it is possible make a pop and then push all
                        _stack.pop();
                    }
                    for (int j = 0; j < avaiableT.fourth().length(); j++){ 
                        _stack.push(avaiableT.fourth().charAt(j)); //makes the push on the stack
                    }
                }else{ //the transition makes a pop in the stack
                    _stack.pop(); 
                    }
            }
            return avaiableT.fifth(); //return the states that goes the transition
        }else{
                return null; //if does not exist a valid transition, it is not possible go to a new state
             }
        }
        
    @Override
    public boolean accepts(String string) {
        assert string != null;
        assert verify_string(string);
        
        State actual = _initial;
        System.out.println(_initial.toString());
        int i = 0;
        boolean res = true;
        _stack= new Stack(); 
        _stack.add(Comodin); //reset the stack to the mark to know if string is valid for this automaton
        while (i < string.length() && res){
            actual =  delta(actual, string.charAt(i));
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
        //and all the transition have a valis Character of the Stack Alphabet
        for(Quintupla<State,Character,Character,String,State> t:_transitions){
            transitionOK= _states.contains(t.first()) && _states.contains(t.fifth()) && _alphabet.contains(t.second()) && transitionOK;
            for (int i = 0; i<t.fourth().length(); i++ ){
              transitionOK = transitionOK && ((_stackAlphabet.contains(t.fourth().charAt(i) )));
            }
            //Check that the transition relation is deterministic.
            for(Quintupla<State,Character,Character,String,State> l: _transitions){       
                //Is non deterministic if have more 1 transition from Qi to any node within the same label
                if ( t.first().equals(l.first()) && !t.fifth().equals(l.fifth())  && t.second()==l.second()){ 
                    nonDeterministic=true;
                }
            }
        }
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

    
