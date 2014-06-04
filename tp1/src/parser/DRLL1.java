/*
 *Implementacion de un parser descendente recursivo LL(1) de expresiones regulares
 * 
 */
package parser;

/**
 *
 * @author nando
 */
public class DRLL1{
    
    public static char[]  word;
    static int i;
    
    private static char marca= '$';
    
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
                                    i++;
                                    F1();
                               }else{
                                        throw new Exception("It isn't a language word");
                                     }                            
            }    
    }
            
    private static void L() throws Exception{
        if(word[i]=='('){
            i++;
            E();
            if(word[i]==')'){
                i++;
            }else{
                  throw new Exception("It isn't a language word");
            }
        }else{if(isLetter(word[i])){
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
    
    private static boolean isLetter(char a){
        if (((int) a)>=97 && ((int) a)<=122){
            return true;
        }else{
            return false;
        }
    }

 }
