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
    public State _ini;
    
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
        State ini,aux1,aux2;
        Triple<State,State,String> transition;
        Set<State> Q= new HashSet();
        Set<Triple> delta= new HashSet();
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
                    }else{
                        aux1=new State(p.getFrst());
                        aux2= new State(p.getScond());
                        Q.add(aux1);
                        Q.add(aux2);
                        transition= new Triple(getElemFromSet(Q,aux1), getElemFromSet(Q,aux2),getLabel(line));
                        System.out.println("Transition: "+ transition.toString());
                        delta.add(transition);
                    }
                 }else{   
                        System.out.println(line); //erase 
                      }   
             } 
        }catch(FileNotFoundException e){
             System.out.println(e.getMessage());
        }finally{
            if (input != null){
                input.close();
            }
        }
        return automaton;
    }
    
    private static void workWithLine(String line){
        /*if (line.equals("digraph {") || line.equals("digraph{")){
            System.out.println("IS EQUAL");
        }*/
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
        return l.substring(beginIndex+7, endIndex);
    }
    
    private static Object getElemFromSet(Set q, Object o){
        Iterator i=q.iterator();
        Object aux;
        while (i.hasNext()){
            aux= i.next();
            if (aux.equals(o)){
                return aux;
            }
        }
        return null;
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
    public boolean verify_string(String s) {
        // TODO
        return false;
    }

    /**
     * @return True iff the automaton is in a consistent state.
     */
    public abstract boolean rep_ok();
}
