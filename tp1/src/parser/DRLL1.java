/*
 * 
 * 
 */
package parser;

import java.util.Stack;

/**
 *
 * @author nando
 */
public class DRLL1{
    
    public static Stack<String> word;
    
    public String[] terminals={"|","*","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","(",")"};
    public String[] nonterminals={"E","F","T","L","E1","F1","T1"};
    private static boolean E(){
        //search table
        if (true/* xi isn't a terminal */){
            //call search table    
        }else{if(true){//if xi es igual al simbolo de entrada
            //avanzar la entrada hasta el siguiente simbolo
        
            }else{
                  return false;
                 }
        }
        return true;
    }
    
    public static boolean parser(String w){
        word= new Stack<String>();
        return E();
    }
    
}
