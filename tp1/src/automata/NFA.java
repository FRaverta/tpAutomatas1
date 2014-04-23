package automata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.Triple;

public class NFA extends FA {

    /*
     *  Construction
     */
    // Constructor
    public NFA(
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
        System.out.println("Is a NFA");

    }

    /*
     *	State querying 
     */
    
    @Override
    public Set<State> delta(State from, Character c) {
        assert states().contains(from);
        assert alphabet().contains(c);
        Iterator i=_transitions.iterator();
        Triple<State, Character, State> aux;
        Set<State> result=new HashSet();
        while (i.hasNext()){
            aux=(Triple<State, Character, State>) i.next();
            if (c.equals(aux.second()) && aux.first().equals(from)) //PROBLEMA CON ; DE FROM
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
            
    public boolean accepts2 (State estado, String string){
        if (string.isEmpty())
            return _final_states.contains(estado);
        Set<State> siguientes = new HashSet();
        siguientes= delta(estado,string.charAt(0));
        boolean res = false;
        Iterator i= siguientes.iterator();
        State aux;
        while (i.hasNext()){
            aux= (State)i.next();
            res = res || accepts2(aux,string.substring(1));
        }
        return res;        
    }
    

    /**
     * Converts the automaton to a DFA.
     *
     * @return DFA recognizing the same language.
     */
    public DFA toDFA(){
        assert rep_ok();
        Set<Set<State>> states= new HashSet();//set of reachable "set states" 
        Set<Triple<Set<State>,Character,Set<State>>> transitions= new HashSet();//Set of transitions between set of states
        //State dInitial= new State("{"+_initial.name()+"}");
        State dInitial= new State(_initial.name()); //initial deterministic state
        Set<State> setInitial= new HashSet(); 
        setInitial.add(_initial); // a set with a unique member
        states.add(setInitial);//the "initial set of states" is reachable
        int lastSize= 0; //variable for count the set size before start a loop
        Set<State> newSet;
        for(Character c: this._alphabet){ //add all set of states reachable from initial node.
            newSet=this.delta(_initial, c);
            if (!newSet.isEmpty()){
                states.add(newSet);            
            }
        }
        //System.out.println("States "+states.toString());
        Set<Set<State>> auxSet= new HashSet(); //set auxiliar for don't modified the loop's variable states
        newSet= new HashSet();
        while(lastSize!=states.size()){ //If the loop don't add a new set of states so we have all reachable states
            lastSize=states.size(); //update the size before start a new loop
            auxSet=new HashSet(); //refresh auxSet
            for(Set<State> s: states){ //for each state
                 for(Character c: _alphabet){ //for each character
                    newSet=new HashSet(); 
                    for(State sub: s){//calculate the reachable state for each caracter                   
                        newSet.addAll(delta(sub,c));
                    }
                    if(!newSet.isEmpty()){
                        auxSet.add(newSet); //add the reachable state from s by char c.
                    }
                }
            }            
            states.addAll(auxSet);//add the new reachable states
            System.out.println("States "+states.toString());

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
            dt= new Triple(new State(getStateName(r.first())), r.second(),new State(getStateName(r.third())));
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
                aux= new State(getStateName(s));
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

//Method that take a Set<States> and return a string that contain all state's names concatenate     
//for example getStateName([q0,q1,q2]) return q0q1q2
    String getStateName(Set<State> set){
        //String name="{";
        String name="";
        for(State s:set){          
            /*if (name.length()>1){
                name=name+",";
            }  */  
            name=name+s.name();
        }
        //name=name+"}";                    
        return name;        
    }
    
    
    @Override
    public boolean rep_ok() {
        boolean containLambda= false;
        boolean statesOK=true;
        boolean transitionOK= true;
        //Check that the alphabet does not contains lambda.
        for(Character c: _alphabet){
            if (c== Lambda){
                containLambda=true;
            }                                      
        }
        //Check that final states are included in 'states'.
        for(State s:_final_states){
            statesOK= _states.contains(s) && statesOK;
        }
        //Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
        for(Triple<State,Character,State> t:_transitions){
            transitionOK= _states.contains(t.first()) && _states.contains(t.third()) && _alphabet.contains(t.second()) && transitionOK;
        }    
        return _states.contains(_initial) && !containLambda && transitionOK && statesOK;
    }
}
        
