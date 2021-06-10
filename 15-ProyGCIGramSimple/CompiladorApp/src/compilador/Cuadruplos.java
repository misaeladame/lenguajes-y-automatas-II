package compilador;

import java.util.ArrayList;


public class Cuadruplos {
    
    private ArrayList<Cuadruplo> cuadruplos;
    private Compilador compilador;
    
    public Cuadruplos ( Compilador c ) {
        compilador = c;
        cuadruplos = new ArrayList<> ();
    }
    
    public void insertar ( Cuadruplo c ) {
        cuadruplos.add ( c );
    }
}
