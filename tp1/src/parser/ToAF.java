/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import automata.DFA;
import automata.FA;
import automata.NFA;
import automata.NFALambda;
import automata.State;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import utils.Triple;


public class ToAF {
    
    public static char[]  word;
    static int i;    
    private static char marca= '$';
    private static FA automata;
    private static List<Object> formula;
    
    //Method that return a finite authomaton for an expresion regular
    public static DFA ExpToAF(String exp){           
            try{
                exp= exp+marca;
                word=exp.toCharArray();
                formula= new LinkedList();
                i=0;
                S();
                FA a= ((NFALambda)createAutomaton());
                File test= new File("test.dot");
                FileWriter w= new FileWriter("test.dot");
                if (a instanceof DFA){
                    w.write(a.to_dot());
                    w.close();
                    return (DFA) a;
                }else{
                    a= ((NFALambda)a).toDFA();
                    w.write(a.to_dot());
                    w.close();                    
                    return ((DFA) a);
                }
            }catch(Exception e){
                System.out.println("NO ES UNA EXP REGULAR "+ e.toString());
                return null;
            }

   }
    
    
    
    public static FA createAutomaton(){
        while (formula.size()>1){
            deleteParentesis();
            clausura();
            deleteParentesis();
            punto();
            deleteParentesis();
            barra();
        }
        return (FA) formula.get(0);
    }

    
    private static void S() throws Exception{
        if(word[i]=='(' || isLetter(word[i])){ //poner igual a una letra cualquiera
            E();
            if (word[i]==marca){
            }else{
                throw new Exception("It isn't a language word");
            }
        }else{
              throw new Exception("It isn't a language word");
        }
    }
    
    private static void E() throws Exception{
        if(word[i]=='(' || isLetter(word[i])){
            T();
            E1();    
        }else{
           throw new Exception("It isn't a language word");
        }
    }
    
    private static void E1() throws Exception{
        if(word[i]==')' || word[i]==marca){
            //nothing
        }else{ if (word[i]=='|'){
                                    formula.add(word[i]);
                                    i++;
                                    T();
                                    E1();
                                }else{
                                        throw new Exception("It isn't a language word");
                                      }
              } 
    }
    
    private static void F() throws Exception{
        if(word[i]=='(' || isLetter(word[i])){
            L();
            F1();
        }else{
              throw new Exception("It isn't a language word");
        }
    }

    private static void F1() throws Exception{
        if(word[i]==')' || word[i]=='.' || word[i]=='|' || word[i]==marca){
            //nada is a epsiolon
        }else{ if(word[i]=='*'){
                                    formula.add(word[i]);
                                    i++;
                                    F1();
                               }else{
                                        throw new Exception("It isn't a language word");
                                     }                            
            }    
    }
            
    private static void L() throws Exception{
        if(word[i]=='('){
            formula.add(word[i]);
            i++;
            E();
            if(word[i]==')'){
                formula.add(word[i]);
                i++;
            }else{
                  throw new Exception("It isn't a language word");
            }
        }else{if(isLetter(word[i])){
                formula.add(createAFLetter(word[i]));
                i++;
            }else{
                    throw new Exception("It isn't a language word");
                  }           
         }
    }
    
    private static void T() throws Exception{
        if (word[i]=='(' || isLetter(word[i])){
            F();
            T1();
        }else{
              throw new Exception("It isn't a language word");
        }
    }
    
    private static void T1() throws Exception{
        if(word[i]=='.'){
            formula.add(word[i]);
            i++;
            F();
            T1();
        }else{ if (word[i]==')' || word[i]=='|' || word[i]==marca){
                //nothing is epsiolon
            }else{
              throw new Exception("It isn't a language word");
            }
        }
    }

//Metodo para crear una formula para transformar una expresion regular w en un automata finito    
    public static boolean parser(String w){
        w=w + marca;
        word= w.toCharArray();       
        i=0;
        try{
            S();
            return true;            
        }catch(Exception e){
            return false;
        }
    }

//Method that return true if a in {a,b,...,y,z} else return false  
    private static boolean isLetter(char a){
        if (((int) a)>=97 && ((int) a)<=122){
            return true;
        }else{
            return false;
        }
    }

//Method for create an automaton A: L(A)="c"    
    private static FA createAFLetter(char c){
        State _initial= new State("q0");
        State _final= new State("q1");
        Set<Character> _alphabet= new HashSet();
        _alphabet.add(c);
        Set<State> _states= new HashSet();
        _states.add(_final);
        _states.add(_initial);
        Triple<State,Character,State> t= new Triple(_initial,c,_final);
        Set<Triple<State,Character,State>> _delta= new HashSet();
        _delta.add(t);
        Set<State> _finalStates=new HashSet();
        _finalStates.add(_final);
        try{
            return new DFA(_states,_alphabet,_delta,_initial,_finalStates);
        }catch(Exception e){
            return null;
        }
    }

//Method that make a kleen clausure for a authomaton a    
       private static FA createClausura(FA a){
        State _initial= new State("I" + a.initial_state().name());
        State _final= new State("F" + a.final_states().iterator().next().name());        
        Set<State> _states= new HashSet();
        _states.add(_final);
        _states.add(_initial);
        _states.addAll(a.states());
        Set<Triple<State,Character,State>> _delta= new HashSet();
        Triple<State,Character,State> t= new Triple(_initial,NFA.Lambda,_final);
        _delta.add(t);
        t= new Triple(_initial,NFA.Lambda,a.initial_state());
        _delta.add(t);
        t= new Triple(a.final_states().iterator().next(),NFA.Lambda,_final);
        _delta.add(t);
        t= new Triple(a.final_states().iterator().next(),NFA.Lambda,a.initial_state());
        _delta.add(t);
        _delta.addAll(a.transitions());
        Set<State> _finalStates=new HashSet();
        _finalStates.add(_final);
        try{
            return new NFALambda(_states,a.alphabet(),_delta,_initial,_finalStates);
        }catch(Exception e){
            return null;
        }
    }
    
    
   
    //Metodo que elimina parentesis inecesario de nuestra formula magica
    private static void deleteParentesis(){
        int i=0;
        while (i<formula.size()-2){            
            if (formula.get(i).equals('(') &&formula.get(i+2).equals(')') ){
                if (formula.get(i+1) instanceof FA){
                    formula.remove(i);//remove '('
                    formula.remove(i+1); //remove ')'
                }
            }
            i++;                 
        }
    }


//Metod that applay a clausura in a formula
    private static void clausura() {
        int i=1;
        while(i< formula.size()){
            if(formula.get(i).equals('*') && formula.get(i-1) instanceof FA){ //si tengo un automata a la izquierda de punto lo clausuro
                FA a= (FA)formula.get(i-1);
                formula.remove(i-1);
                formula.add(i-1,createClausura(a));
                formula.remove(i); //remove *
            }else{
                i++;
            }            
        }
    }
//Methods that apply an concatenation in a formula    
      private static void punto() {
        int i=1;
        while(i< formula.size()-1){
            if(formula.get(i).equals('.') && formula.get(i-1) instanceof FA && formula.get(i+1) instanceof FA){ //si tengo un automata a la izquierda de punto lo clausuro
                FA a= (FA) union((FA)formula.get(i-1),(FA)formula.get(i+1));
                formula.remove(i+1); //remove second automata
                formula.remove(i); //remove .
                formula.remove(i-1); // remove firsr automata
                formula.add(i-1,a);
            }else{
                i++;
            }    
    }
}

    
//Methods that apply an union in a formula    
    private static FA union(FA a, FA b) {
        State _initial= a.initial_state();
        State _final= b.final_states().iterator().next();
        Set<State> _states= new HashSet();
        for(State s: b.states()){
            s.rename("B"+s.name());
        }
        _states.addAll(a.states());
        _states.addAll(b.states());
        Set<Triple<State,Character,State>> _delta= new HashSet();
        Triple<State,Character,State> t= new Triple(a.final_states().iterator().next(),NFA.Lambda,b.initial_state());
        _delta.add(t);       
        _delta.addAll(a.transitions());
        _delta.addAll(b.transitions());
        Set<State> _finalStates=new HashSet();
        _finalStates.add(_final);
        Set<Character> _alphabet= new HashSet();
        _alphabet.addAll(a.alphabet());
        _alphabet.addAll(b.alphabet());
        try{
            return new NFALambda(_states,_alphabet,_delta,_initial,_finalStates);
        }catch(Exception e){
            return null;
        }        
    }

//Method that make a union between two Authomaton    
    private static FA applyBarra(FA a, FA b){
        State _initial= new State("I" + a.initial_state().name() + b.initial_state().name());
        State _final= new State("F" + a.final_states().iterator().next().name() + b.final_states().iterator().next().name());        
        for(State s: b.states()){
            s.rename("B"+s.name());
        }
        Set<State> _states= new HashSet();
        _states.add(_final);
        _states.add(_initial);
        _states.addAll(a.states());
        _states.addAll(b.states());        
        Set<Triple<State,Character,State>> _delta= new HashSet();
        Triple<State,Character,State> t= new Triple(_initial,NFA.Lambda,a.initial_state());
        _delta.add(t);
        t= new Triple(_initial,NFA.Lambda,b.initial_state());
        _delta.add(t);
        t= new Triple(a.final_states().iterator().next(),NFA.Lambda,_final);
        _delta.add(t);
        t= new Triple(b.final_states().iterator().next(),NFA.Lambda,_final);
        _delta.add(t);
        _delta.addAll(a.transitions());
        _delta.addAll(b.transitions());
        Set<Character> _alphabet= new HashSet();
        _alphabet.addAll(a.alphabet());
        _alphabet.addAll(b.alphabet());
        Set<State> _finalStates=new HashSet();
        _finalStates.add(_final);
        try{
            return new NFALambda(_states,_alphabet,_delta,_initial,_finalStates);
        }catch(Exception e){
            return null;
        }        
    }
    
    private static void barra(){
        int i=1;
        while(i< formula.size()-1){
            if(formula.get(i).equals('|') && formula.get(i-1) instanceof FA && formula.get(i+1) instanceof FA){ //si tengo un automata a la izquierda de punto lo clausuro
                FA a= (FA) applyBarra((FA)formula.get(i-1),(FA)formula.get(i+1));
                formula.remove(i+1); //remove second automata
                formula.remove(i); //remove .
                formula.remove(i-1); // remove firsr automata
                formula.add(i-1,a);
            }else{
                i++;
            }    
        }
    }
    
  
    public static void main(String[] args) {
        FA a= ExpToAF("a*|b*");        
      //  System.out.println(formula.toString());
    }    
}
