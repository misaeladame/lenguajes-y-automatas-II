package compilador;

import java.util.ArrayList;

public class Cuadruplos {
    
    private ArrayList<Cuadruplo> cuadruplos;
    private Compilador           compilador;

    public Cuadruplos( Compilador c ) {
        cuadruplos = new ArrayList<> ();
        compilador = c;
    }
    
    public void insertar ( Cuadruplo c ) {
        cuadruplos.add ( c );
    }
    
    public void removerTodos ( ) {
        cuadruplos.clear ();
    }

    public ArrayList<Cuadruplo> getCuadruplos() {
        return cuadruplos;
    }
    
}
