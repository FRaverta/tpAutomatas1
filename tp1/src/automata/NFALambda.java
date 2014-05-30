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
        
        Set<State> ini= clausuraLambdaState(this._initial);  //Dfa's initial state is clausura lambda de this.q0
        Set<Set<State>> states= new HashSet(); //states for contain the DFA's states
        states.add(ini); //add initial state to dfa's states
        State dInitial= new State(getStateName(ini));  //create a correct representation for DFA
        int lastSize=0; 
        Set<Set<State>> auxSet;
        Set<State> newSet;
        Set<Triple<Set<State>,Character,Set<State>>> transitions= new HashSet();//Set of transitions between set of states        
        
        while(lastSize!=states.size()){ //If the loop don't add a new set of states so we have all reachable states
            lastSize=states.size(); //update the size before start a new loop
            auxSet=new HashSet(); //refresh auxSet
            for(Set<State> s: states){ //for each state
                 for(Character c: _alphabet){ //for each character
                    newSet=new HashSet(); 
                    for(State sub: s){//calculate the reachable state for each caracter                   
                        newSet.addAll(delta(sub,c)); 
                        for (State q: newSet){ //calculate the landa clausure for reachable state
                            newSet.addAll(clausuraLambdaState(q));//add landa clausure for set
                        }
                    }
                    if(!newSet.isEmpty()){
                        auxSet.add(newSet); //add the reachable state from s by char c.    
                    }
                }
            }            
            states.addAll(auxSet);//add the new reachable states
        }
        State aux;
        String name;
        Set<State> dStates=new HashSet();
        for(Set<State> set:states){//loop for generate a correct representation for states
            name= getStateName(set); //make a compound name
            aux= new State(name);//make a state with a compound name
            dStates.add(aux); //add state to the deterministic states set
        }
        
        Triple<Set<State>,Character,Set<State>> t;
        Set<State> goTo;
        for(Character c: _alphabet){ //loop for generate a transition between states
            for(Set<State> set: states){
                goTo= new HashSet();
                for(State s: set){
                    goTo.addAll(delta(s,c)); //for each char, for each state I search the reachable states
                }
                if (!goTo.isEmpty()){
                    t= new Triple(set,c,goTo); //make a transitions representations
                    transitions.add(t);
                }    
            }
        }
                HashSet dTransitions= new HashSet();
        Triple<State,Character,State> dt;
        //loop for generate a correct representation for deteriministic delta fuction
        for (Triple<Set<State>,Character,Set<State>>r: transitions){
            dt= new Triple(FA.getElemFromSet(dStates,new State(getStateName(r.first()))), r.second(), FA.getElemFromSet(dStates,new State(getStateName(r.third()))));
            dTransitions.add(dt);
        }
        HashSet<State> dFinalStates= new HashSet();
        boolean isFinal;
        for(Set<State> s: states){
            isFinal=false;
            //loop for detected final states.
            for(State q: s){
                isFinal=isFinal || _final_states.contains(q); //if a set  contain any final states so it is final
            }    
            if (isFinal){ //if isFinal, add to final states set
                aux= FA.getElemFromSet(dStates,new State(getStateName(s)));
                dFinalStates.add(aux);
            }
        }
        
        /*System.out.println("States: "+ dStates.toString());
        System.out.println("Delta: "+ dTransitions.toString());
        System.out.println("InitialState: "+ dInitial.toString());
        System.out.println("Final Statates: "+ dFinalStates.toString());*/ 
        DFA res= new DFA(dStates,_alphabet,dTransitions,dInitial,dFinalStates);
        return res; //return a deterministic FA
                
    }

        String getStateName(Set<State> set){
        //String name="{";
        String name="";
        for(State s:set){          
            name=name+s.name();
        }
        return name;        
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

