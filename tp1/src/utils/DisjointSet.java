package utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DisjointSet <Type extends Object> implements java.lang.Iterable{

  private Type[] reference;  
  private int[] padre;
  /**
   *  Construct a disjoint sets object.
   *
   *  @param numElements the initial number of elements--also the initial
   *  number of disjoint sets, since every element is initially in its own set.
   **/
  public DisjointSet(Set<Type> set) {
    padre = new int[set.size()];
    
    for (int i = 0; i < padre.length; i++) {
      padre[i] = i; //ojoooDFA a
    }
    reference= (Type[]) set.toArray(); 
  }
  
    public DisjointSet(int[] parents, Type[] obj) {
    padre =parents;
    reference= obj;
    
  }
  
  public void union(Type x, Type y){
      unionInt(search(x), search(y));
  }
  
  private int search(Type x){
      for (int i=0; i<reference.length; i++){
          if (reference[i].equals(x)){
              return i;
          }
      }
      return -1;
  }
  
  public void disUnite(Type x){
      
      int agent=search(x);
      if (padre[agent]!= agent){
         int agentFather= padre[agent];
         for(int i=0; i<padre.length;i++){
             if (padre[i]==agent){
                 padre[i]=agentFather;
             }
         }
         padre[agent]= agent;      

      }else{
         int newFather=-1;
         for(int i=0; i<padre.length;i++){
             if (padre[i]==agent && i!=agent){
                newFather=padre[i];
                 break;
             }
         }
         for(int i=0; i<padre.length;i++){
             if (padre[i]==agent && i!=agent){
                 padre[i]=newFather;
             }
         }             
                  
      }
  }
  
  public Set<Set<Type>> toSet(){
      Set<Set<Type>> disjointSet= new HashSet();
      for(int i=0; i<padre.length; i++){ //update array padre with class's father
          padre[i]= find(i);
      }
      HashSet<Type> fathers= new HashSet(); // Set with all father class
      for(int i=0; i<padre.length; i++){
          fathers.add(reference[padre[i]]);
      }
      for(Type s: fathers){
        HashSet<Type> clase= new HashSet();
        for(int i=0; i<padre.length; i++){
          if (reference[padre[i]].equals(s)){
              clase.add(reference[i]);
          }
        }
        disjointSet.add(clase);        
      }
      return disjointSet;
  }
  /**
   *  union() unites two disjoint sets into a single set.  A union-by-rank
   *  heuristic is used to choose the new root.  This method will corrupt
   *  the data structure if root1 and root2 are not roots of their respective
   *  sets, or if they're identical.
   *
   **/
  private void unionInt(int x, int y) {
    int xRoot=find (x);
    int yRoot=find (y);
    padre[xRoot]=yRoot;
  }

  /**
   *  find() finds the (int) name of the set containing a given element.
   *  Performs path compression along the way.
   *
   *  @param x the element sought.
   *  @return the set containing x.
   **/
  public int find(int x) {
    if (padre[x] == x) 
      return x;                         // x is the root of the tree; return it
    else 
      // Find out who the root is; compress path by making the root x's parent.
      return find(padre[x]);                                      // Return the root
  }



  /**
   *  main() is test code.  All the find()s on the same output line should be
   *  identical.
   **/

    public Iterator iterator() {
        return toSet().iterator();        
    }
    
    public int size(){
      for(int i=0; i<padre.length; i++){ //update array padre with class's father
          padre[i]= find(i);
      }
      HashSet<Type> fathers= new HashSet(); // Set with all father class
      for(int i=0; i<padre.length; i++){
          fathers.add(reference[padre[i]]);
      }
      return fathers.size();
    }
    
    public Type father(Type o){
        int parent= padre[search(o)]; //search position of father for o 
        return reference[parent]; // return o's father
    }

/*  
public static void main(String[] args){
    DFA a=null;
    DisjointSet<State> d=null;
    try{
        a= (DFA) FA.parse_form_file("/home/nando/Desktop/ProyectoAutomatas/tpAutomatas1/tp1/test/dfa4.dot");
        d= new DisjointSet(a.states());

    }catch(Exception e){
        System.out.println("ERRORrrrrrrr");
    }
       State finalState=a.final_states().iterator().next();
        State notFinal=a.initial_state();
       for(State s: a.states()){
           if (!a.final_states().contains(s)){
                     notFinal=s;

           }
       }
       for (State s: a.states()){
            if (a.final_states().contains(s)){
                d.union(finalState,s);
            }else{
                d.union(notFinal,s);
            }
        } 
        System.out.println(d.toSet().toString());
}*/

public DisjointSet copy(){
    int[] father= this.padre.clone();
    Type[] refer= this.reference;
    return new DisjointSet(father,refer);        
 }
}