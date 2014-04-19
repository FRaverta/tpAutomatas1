
import automata.FA;


public class Main {

    public static void main(String[] args) {
        System.out.println("Hello");
        try{
            FA a=FA.parse_form_file("/home/nando/Desktop/Automatas/TpAutomatas1/tp1/test/dfa1.dot");
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
