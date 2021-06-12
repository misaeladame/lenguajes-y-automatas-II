/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                            LENGUAJES Y AUTOMATAS II           
 *: 
 *:                  SEMESTRE: ENE-JUN 2021    HORA: 18-19 HRS
 *:                                   
 *:               
 *:                             Clase con atributos
 *                 
 *:                           
 *: Archivo       : Atributos.java
 *: Autor         : 18131209  Misael Adame  
 *:                 18130588  Cristian Piña
 *:                 17130772  Sergio Chairez
 *: Fecha         : 18/MAY/2021
 *: Compilador    : Java JDK 7
 *: Descripción   : La clase atributo se usa para concentrar todos los atributos 
 *:                 a los que se hace referencia en las acciones semánticas, excepto 
 *:                 entrada, lexema, complex. No importando si los atributos son 
 *:                 heredados o sintetizados. En este caso usamos dos los cuales 
 *:                 son t de tipo y taux que es de tipo auxiliar.
 *: Ult.Modif.    :
 *:  Fecha      Modificó                     Modificacion
 *:=============================================================================
 *: 11/Jun/2021  18131209 Misael Adame       Se agregaron los atributos:
 *:              18130588 Cristian Piña      Lugar, op, comienzo, siguiente, 
 *:              17130772 Sergio Chairez     cierta, falsa, cdc, cdc_
 *:----------------------------------------------------------------------------
 */

package compilador;

public class Atributos {
    // Atributos para el Comprobador de tipos
    String t;
    String taux;
    
    // Atributos para el Codigo de tres direcciones
    String Lugar;
    String op;
    String comienzo;
    String siguiente;
    String cierta;
    String falsa;
    String cdc;
    String cdc_;
    
    public Atributos () {
        Lugar = "";
        op    = "";
        comienzo = "";
        siguiente = "";
        cierta = "";
        falsa = "";
        cdc = "";
        cdc_ = "";
    }
}
