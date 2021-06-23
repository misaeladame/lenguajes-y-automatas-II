/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                            LENGUAJES Y AUTOMATAS II           
 *: 
 *:                  SEMESTRE: ENE-JUN 2021    HORA: 18-19 HRS
 *:                                   
 *:               
 *:                      Clase de una lista de Cuadruplos
 *:           
 *:                           
 *: Archivo       : Cuadruplos.java
 *: Autor         : 18131209  Misael Adame  
 *:                 18130588  Cristian Piña
 *:                 17130772  Sergio Chairez
 *: Fecha         : 20/Jun/2021
 *: Compilador    : Java JDK 7
 *: Descripción   : Cuadruplos son una estructura de lista de objetos de tipo 
 *:                 Cuadruplo
 *: Ult.Modif.    :
 *:  Fecha      Modificó                     Modificacion
 *:=============================================================================
 *: 20/Jun/2021  18131209 Misael Adame       Se agregaron los atributos:
 *:              18130588 Cristian Piña      cuadruplos y compilador
 *:              17130772 Sergio Chairez     Se agregaron los métodos:
 *:                                          insertar ( Compilador c )
 *:                                          removerTodos ()
 *:                                          getCuadruplos ()
 *:----------------------------------------------------------------------------
 */

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
