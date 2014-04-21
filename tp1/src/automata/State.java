package automata;

public class State {

    private String _name;

    public State(String name) {
        _name = name;
    }

    public String name() {
        return _name;
    }
   /* 
    public boolean equals(State obj){
        return _name.equals(obj._name);
    }*/
    
    public boolean equals(Object obj) {
        if (!(obj instanceof State))
            return false;	
	if (obj == this)
            return true;
	return this._name.equals(((State) obj)._name);
    }
    
    public String toString(){
        return _name;
    }
    
    public void rename(String Newname){
        _name= Newname;
    }

    
    // Optional - Use to get equality based in abtributes, 
    // instead than by reference (which is Java's default). 
    /*
     @Override
     public int hashCode() {
     final int prime = 31;
     int result = 1;
     result = prime * result + ((_name == null) ? 0 : _name.hashCode());
     return result;
     }
*/
    	public int hashCode(){
		return _name.hashCode();// return a hashCode each character- if not define we have problems with HashSet.contains
	}
  /*
     @Override
     public boolean equals(Object obj) {
     if (this == obj)
     return true;
     if (obj == null)
     return false;
     if (getClass() != obj.getClass())
     return false;
     State other = (State) obj;
     if (_name == null) {
     if (other._name != null)
     return false;
     } else if (!_name.equals(other._name))
     return false;
     return true;
     }
    */
}
