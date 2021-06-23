/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                            LENGUAJES Y AUTOMATAS II           
 *: 
 *:                  SEMESTRE: ENE-JUN 2021    HORA: 18-19 HRS
 *:                                   
 *:               
 *:                      Clase con atributos de un Cuadruplo
 *:           
 *:                           
 *: Archivo       : Cuadruplo.java
 *: Autor         : 18131209  Misael Adame  
 *:                 18130588  Cristian Piña
 *:                 17130772  Sergio Chairez
 *: Fecha         : 20/Jun/2021
 *: Compilador    : Java JDK 7
 *: Descripción   : Un cuádruplo es una estructura tipo registro con cuatro 
 *:                 campos, que se llamarán op, arg1, arg2 y resultado.
 *: Ult.Modif.    :
 *:  Fecha      Modificó                     Modificacion
 *:=============================================================================
 *: 21/Jun/2021  18131209 Misael Adame       Se agregaron los atributos:
 *:              18130588 Cristian Piña      op, arg1, arg2, resultado 
 *:              17130772 Sergio Chairez     
 *:----------------------------------------------------------------------------
 */

package compilador;

public class Cuadruplo {
    
    public String op;
    public String arg1;
    public String arg2;
    public String resultado;

    public Cuadruplo ( String op, String arg1, String arg2, String resultado ) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.resultado = resultado;
    }

}
