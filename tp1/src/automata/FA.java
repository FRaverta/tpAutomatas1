package automata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import utils.Pair;
import utils.Triple;

public abstract class FA {

    public static final Character Lambda = '_';

//Automaton atributes    
    protected State _initial;
    protected Set<State> _states;
    protected Set<Character> _alphabet;
    protected Set<Triple<State,Character,State>> _transitions; //delta function
    protected Set<State> _final_states;
    
    /* Creation */
    /**
     * Parses and returns a finite automaton form the given file. The type of
     * the automaton returned is the appropriate one for the automaton
     * represented in the file (i.e. if the file contains the representation of
     * an automaton that is non-deterministic but has no lambda transitions,
     * then an instance of NFA must be returned).
     *
     * @param path Path to the file containing the specification of an FA.
     * @return An instance of DFA, NFA or NFALambda, corresponding to the
     * automaton represented in the file.
     * @throws Exception Throws an exception if there is an error during the
     * parsing process.
     */
    public static FA parse_form_file(String path) throws Exception {      
        FA automaton=null;
        String line;
        Scanner input = null;
        File f;
        Pair<String> p;
        State ini=null;
        State aux1,aux2;
        Triple<State,Character,State> transition;
        Set<State> Q= new HashSet();
        Set<Triple<State,Character,State>> delta= new HashSet();
        Set<Character> alphabet= new HashSet();
        Set<State> finalStates=new HashSet();
        try {
            f = new File(path);
            input=new Scanner(f);
             while (input.hasNextLine()) {
                 line = input.nextLine();
                 if (line.contains("->")){
                    p=getNodes(line);
                    //System.out.println(p.toString());
                    if (p.getFrst().equals("inic")){
                     ini= new State(p.getScond());
                     Q.add(ini);
                     //System.out.println("Initial State: " + ini.toString());
                    }else{
                            aux1=new State(p.getFrst());
                            aux2= new State(p.getScond());     
                            if (getElemFromSet(Q,aux1)==null){                       
                                Q.add(aux1);
                            }    
                            if (getElemFromSet(Q,aux2)==null){                       
                                Q.add(aux2);
                            }  
                            for (char letter:getLabel(line).toCharArray()){
                                if (letter!=','){
                                    transition= new Triple(getElemFromSet(Q,aux1),letter,getElemFromSet(Q,aux2));
                                    //System.out.println("Transition: "+ transition.toString());
                                    delta.add(transition); //Add transition to AF delta function
                                    alphabet.add(letter); //add letter to AF alphabet
                                }
                            }
                        }    
                 }else{if (line.contains("[shape=doublecircle]"))
                        {
                            line= line.trim();
                            aux1= new State(line.substring(0,line.indexOf("[shape=doublecircle]")));
                            if (getElemFromSet(Q,aux1)!=null){
                                finalStates.add((State)getElemFromSet(Q,aux1));
                                //System.out.println("Final State: "+ getElemFromSet(Q,aux1).toString() );
                            }else{
                                    Q.add(aux1);                                    
                                    finalStates.add(aux1);
                                    //System.out.println("--Final State: "+ getElemFromSet(Q,aux1).toString() );

                                 }    
                        }else{
                              //System.out.println(line); //erase 
                              }
                 }
             } 
        }catch(FileNotFoundException e){
             System.out.println(e.getMessage());
        }finally{
            if (input != null){
                input.close();
            }
        }
        System.out.println("States: "+ Q.toString());
        System.out.println("Delta: "+ delta.toString());
        System.out.println("InitialState: "+ ini.toString());
        System.out.println("Final Statates: "+ finalStates.toString()); 
        System.out.println("Alphabet: "+ alphabet.toString());
        automaton= builFA(Q,alphabet,delta,ini,finalStates) ;
        return automaton;
    }
    

//Method for String that contain the following structure: "_ A->B _" so return a pair with (A,B) 
// PostCondition: (A,B) if String has pattern: A->B or ("","") if it hasn't.    
    private static Pair<String> getNodes(String l){
        Pair<String> result= new Pair("","");
        if (l.contains("->")){
            l=l.trim();
            int arrow= l.indexOf("->");
            int clasp= l.indexOf(" ");
            if (clasp!=-1){
               result= new Pair(l.substring(0, arrow),l.substring(arrow+2,clasp));
            }else{
                    result= new Pair(l.substring(0, arrow),l.substring(arrow+2,l.length()));
                  }
        }
            return result;      
    }
 
 //Method that take a string that contain the following subString [label="A"] and return the string "A"     
 //Precondition= l has "[label="a"]" substring
    private static String getLabel(String l){
        int beginIndex=l.indexOf("[label=");
        int endIndex=l.lastIndexOf("]");
        return l.substring(beginIndex+8, endIndex-1); //Not take char ' " '
    }
    
 //Method that take a set and object and return a reference to object o from q if o in q or return null.   
    private static State getElemFromSet(Set<State> q,State o){
        //System.out.println("Set: "+ q.toString() + " State: "+ o.toString());
        Iterator i=q.iterator();
        State aux;
        State result=null;
        while (i.hasNext()){
            aux=(State) i.next();
            //System.out.println("Object in set: "+ aux.toString());
            if (aux.equals(o)){
                //System.out.println("-- "  +aux.toString() +" is equal to "+ o.toString());
                result=o;
               
            }
        }
        return result;
    }

//Method that build a concrete FA(DFA.NFA,NFALambda) depending if the parameters.      
 private static FA builFA(
        Set<State> states,
        Set<Character> alphabet,
        Set<Triple<State, Character, State>> transitions,
        State initial,
        Set<State> final_states
        )throws IllegalArgumentException    
        {
            boolean landa=false;
            boolean nonDeterministic= false;
            for(Triple<State,Character,State> t: transitions){
                if (t.second()=='_'){
                    landa=true;
                }
                for(Triple<State,Character,State> l: transitions){
                    //Is non deterministic if have more 1 transition from Qi to any node within the same label
                    if ( t.first().equals(l.first()) && !t.third().equals(l.third())  && t.second()==l.second()){ 
                        nonDeterministic=true;
                    }
                }    
            }
            if (!landa && !nonDeterministic){
                return new DFA(states,alphabet,transitions,initial,final_states);
            }else{
                  if(!landa){
                      return new NFA(states,alphabet,transitions,initial,final_states);
                  }else{
                            return new NFALambda(states,alphabet,transitions,initial,final_states);
                        }
            }
        }
         
         
    /*
     * 	State Querying
     */
    /**
     * @return the atomaton's set of states.
     */
    public abstract Set<State> states();

    /**
     * @return the atomaton's alphabet.
     */
    public abstract Set<Character> alphabet();

    /**
     * @return the atomaton's initial state.
     */
    public abstract State initial_state();

    /**
     * @return the atomaton's final states.
     */
    public abstract Set<State> final_states();

    /**
     * Query for the automaton's transition function.
     *
     * @return A state or a set of states (depending on whether the automaton is
     * a DFA, NFA or NFALambda) corresponding to the successors of the given
     * state via the given character according to the transition function.
     */
    public abstract Object delta(State from, Character c);

    /**
     * @return Returns the DOT code representing the automaton.
     */
    public abstract String to_dot();

    /*
     * 	Automata Methods 
     */
    /**
     * Tests whether a string belongs to the language of the current finite
     * automaton.
     *
     * @param string String to be tested for acceptance.
     * @return Returns true iff the current automaton accepts the given string.
     */
    public abstract boolean accepts(String string);

    /**
     * Verifies whether the string is composed of characters in the alphabet of
     * the automaton.
     *
     * @return True iff the string consists only of characters in the alphabet.
     */
    public boolean verify_string(String s){
        boolean ok=true;
        for(Character c: s.toCharArray()){
            if (!_alphabet.contains(c)){
                ok=false;
            }
        }
        return ok;
    }

    /**
     * @return True iff the automaton is in a consistent state.
     */
    public abstract boolean rep_ok();
}
