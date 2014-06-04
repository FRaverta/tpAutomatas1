/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import static automata.FA.getElemFromSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;
import java.util.Stack;
import utils.Pair;
import utils.Quintupla;

/**
 *
 * @author cesar
 */
public abstract class AP {

    public static final Character Lambda = '_';
    public static final Character Comodin = '@';
    public static final Character Initial = 'Z';
    
    private static Pair<String> getNodes(String line) {
        Pair<String> result= new Pair("","");
        if (line.contains("->")){
            line=line.trim();
            int arrow= line.indexOf("->");
            int clasp= line.indexOf(" ");
            if (clasp!=-1){
               result= new Pair(line.substring(0, arrow),line.substring(arrow+2,clasp));
            }else{
                    result= new Pair(line.substring(0, arrow),line.substring(arrow+2,line.length()));
                  }
        }
            return result;  
    }

    private static String getLabel(String l){
        int beginIndex=l.indexOf("[label=");
        int endIndex=l.lastIndexOf("]");
        return l.substring(beginIndex+8, endIndex-1); //Not take char ' " '
    }


    //Automaton atributes    
    protected State _initial;
    protected Character _stackInitial;
    protected Stack<Character> _stack;
    protected Set<State> _states;
    protected Set<Character> _alphabet;
    protected Set<Character> _stackAlphabet;
    protected Set<Quintupla<State,Character,Character,String,State>> _transitions; //delta function
    protected Set<State> _final_states;
    
    
    public static AP parse_form_file(String path) throws Exception {      
        AP automaton=null;
        String line;
        Scanner input = null;
        File f;
        Pair<String> p;
        State ini=null;
        State aux1,aux2;
        Set<Character> alphabetStack = null;
        Quintupla<State,Character,Character,String,State> transition;
        Set<State> Q= new HashSet();
        Set<Quintupla<State,Character,Character,String,State>> delta= new HashSet();
        Set<Character> alphabet= new HashSet();
        Set<State> finalStates=new HashSet();
        try {
            f = new File(path);
            input=new Scanner(f);
            while (input.hasNextLine()) {
                System.out.println("encontre una linea"); 
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
                            char [] cadena = getLabel(line).toCharArray();
                            char letter = cadena[0];
                            char letterStack = cadena[2];
                            String string = new String();
                            System.out.println(cadena[4]);
                            for (int j=3; j<cadena.length; j++){
                                System.out.println("dentro de label");
                                //System.out.println(cadena[j]);
                                if (cadena[j]!='/'){
                                    string = string + cadena[j];
                                    alphabetStack.add(new Character(cadena[j]));
                                    System.out.println(cadena[j]);
                                }
                            }
                            transition= new Quintupla(getElemFromSet(Q,aux1),letter,letterStack,string,getElemFromSet(Q,aux2));
                                    //System.out.println("Transition: "+ transition.toString());
                            delta.add(transition); //Add transition to AF delta function
                            alphabet.add(letter); //add letter to AF alphabet
                             }
                        }else{
                        if (line.contains("[shape=doublecircle]"))
                        {
                            line= line.trim();
                            aux1= new State(line.substring(0,line.indexOf("[shape=doublecircle]")));
                            aux2=getElemFromSet(Q,aux1);
                            if (aux2 != null){
                                finalStates.add(aux2);
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
        automaton= builAP(Q,alphabet,alphabetStack,Initial,delta,ini,finalStates) ;
        return automaton;
    }
    
     public static State getElemFromSet(Set<State> q,State o){
        //System.out.println("Set: "+ q.toString() + " State: "+ o.toString());
       for(State s: q){
           if (s.name().equals(o.name())){
               return s;
           }
       }
       return null;
     }
     
     
     private static AP builAP(
        Set<State> states,
        Set<Character> alphabet,
        Set<Character> stackAlphabet,
        Character stackInitial,
        Set<Quintupla<State, Character,Character,String, State>> transitions,
        State initial,
        Set<State> final_states)
        throws IllegalArgumentException    
        {
            boolean landa=false;
            boolean nonDeterministic= false;
            for(Quintupla<State,Character,Character,String,State> t: transitions){
                if (t.second()=='_'){
                    landa=true;
                }
                for(Quintupla<State,Character,Character,String,State> l: transitions){       
                //Is non deterministic if have more 1 transition from Qi to any node within the same label
                if ( t.first().equals(l.first()) && !t.fifth().equals(l.fifth())  && t.second()==l.second()){ 
                    nonDeterministic=true;
                }
                
                }
            }    
            return new DFAPila(states,alphabet,stackAlphabet,transitions,Initial,initial,final_states);
            /*if (!landa && !nonDeterministic){
                return new DFA(states,alphabet,transitions,initial,final_states);
            }else{
                  if(!landa){
                      return new NFA(states,alphabet,transitions,initial,final_states);
                  }else{
                            return new NFALambda(states,alphabet,transitions,initial,final_states);
                        }
            }*/
        }
     
     public Set<State> final_states(){
        return _final_states;
    }
     
    public State initial_state(){
        return _initial;
    }
    
    public Set<Character> alphabet(){
        return _alphabet;
    }
    
    public Set<State> states(){
        return _states;
    }
     
     public abstract boolean accepts(String string);
     
     public abstract Object delta(State from, Character c);


     //falta modificar
     public final String to_dot(){
        //assert rep_ok();
        char comilla= '"';
        Iterator i;
        String aux;
        aux = "digraph{\n";
        aux = aux + "inic[shape=point];\n" + "inic->" + this._initial.name() + ";\n";
        i=this._transitions.iterator();
        while (i.hasNext()) {
           Quintupla quintupla =(Quintupla) i.next();
           aux = aux + quintupla.first().toString() + "->" + quintupla.fifth().toString() + " [label=" +comilla+ quintupla.second().toString() +"/"+ quintupla.third()+"/"+quintupla.fourth()+ "];\n";
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


}
