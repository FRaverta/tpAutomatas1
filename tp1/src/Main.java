
import automata.FA;


public class Main {

    public static void main(String[] args) {
        System.out.println("Hello");
        try{
            FA a=FA.parse_form_file("/home/mariano/Escritorio/Automatas/tpAutomatas1/tp1/test/nfa2.dot");
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
