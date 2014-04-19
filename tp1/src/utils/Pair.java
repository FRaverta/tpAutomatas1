/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author nando
 */
public class Pair<T> {
    private T _frst;
    private T _scond;
    
    public Pair(T x, T y){
        _frst=x;
        _scond=y;
    }
    
    public T getFrst(){
        return _frst;
    }
    
    
    public T getScond(){
        return _scond;
    }
    
    public String toString(){
        return "(" + _frst.toString() + "," + _scond.toString() +")";
    }
}
