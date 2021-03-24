/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                         LENGUAJES Y AUTOMATAS II           
 *: 
 *:                  SEMESTRE: ___________    HORA: ___________ HRS
 *:                                   
 *:               
 *:         Clase con la funcionalidad del Analizador Sintactico
 *                 
 *:                           
 *: Archivo       : SintacticoSemantico.java
 *: Autor         : Fernando Gil  ( Estructura general de la clase  )
 *:                 Grupo de Lenguajes y Automatas II ( Procedures  )
 *: Fecha         : 03/SEP/2014
 *: Compilador    : Java JDK 7
 *: Descripción   : Esta clase implementa un parser descendente del tipo 
 *:                 Predictivo Recursivo. Se forma por un metodo por cada simbolo
 *:                 No-Terminal de la gramatica mas el metodo emparejar ().
 *:                 El analisis empieza invocando al metodo del simbolo inicial.
 *: Ult.Modif.    :
 *:  Fecha      Modificó            Modificacion
 *:=============================================================================
 *: 22/Feb/2015 FGil                -Se mejoro errorEmparejar () para mostrar el
 *:                                 numero de linea en el codigo fuente donde 
 *:                                 ocurrio el error.
 *: 08/Sep/2015 FGil                -Se dejo lista para iniciar un nuevo analizador
 *:                                 sintactico.
 *:----------------------------------------------------------------------------
 */
package compilador;

import javax.swing.JOptionPane;

public class SintacticoSemantico {

    private Compilador cmp;
    private boolean    analizarSemantica = false;
    private String     preAnalisis;
    
    //--------------------------------------------------------------------------
    // Constructor de la clase, recibe la referencia de la clase principal del 
    // compilador.
    //

    public SintacticoSemantico(Compilador c) {
        cmp = c;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Metodo que inicia la ejecucion del analisis sintactico predictivo.
    // analizarSemantica : true = realiza el analisis semantico a la par del sintactico
    //                     false= realiza solo el analisis sintactico sin comprobacion semantica

    public void analizar ( boolean analizarSemantica ) {
        this.analizarSemantica = analizarSemantica;
        preAnalisis = cmp.be.preAnalisis.complex;

        // * * *   INVOCAR AQUI EL PROCEDURE DEL SIMBOLO INICIAL   * * *7
        programa ();
    }

    //--------------------------------------------------------------------------

    private void emparejar ( String t ) {
        if ( cmp.be.preAnalisis.complex.equals ( t ) ) {
             cmp.be.siguiente ();
            preAnalisis = cmp.be.preAnalisis.complex;            
        } else {
            errorEmparejar ( t, cmp.be.preAnalisis.lexema, cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Metodo para devolver un error al emparejar
    //--------------------------------------------------------------------------
 
    private void errorEmparejar ( String _token, String _lexema, int numLinea ) {
        String msjError = "";

        if ( _token.equals ( "id" ) ) {
            msjError += "Se esperaba un identificador";
        } else if ( _token.equals ( "num" ) ) {
            msjError += "Se esperaba una constante entera";
        } else if ( _token.equals ( "num.num") ) {
            msjError += "Se esperaba una constante real";
        } else if ( _token.equals ( "literal") ) {
            msjError += "Se esperaba una literal";
        } else if ( _token.equals ( "oparit") ) {
            msjError += "Se esperaba un operador aritmetico";
        } else if ( _token.equals ( "oprel") ) {
            msjError += "Se esperaba un operador relacional";
        } else if ( _token.equals ( "opasigna") ) {
            msjError += "Se esperaba operador de asignacion";
        } else {
            msjError += "Se esperaba " + _token;
        }
        msjError += " se encontró " + ( _lexema.equals ( "$" )? "fin de archivo" : _lexema ) + 
                    ". Linea " + numLinea;        // FGil: Se agregó el numero de linea

        cmp.me.error ( Compilador.ERR_SINTACTICO, msjError );
    }

    // Fin de ErrorEmparejar
    //--------------------------------------------------------------------------
    // Metodo para mostrar un error sintactico

    private void error ( String _descripError ) {
        cmp.me.error ( cmp.ERR_SINTACTICO, 
                       _descripError + 
                       "Linea: " + cmp.be.preAnalisis.numLinea );
    }

    // Fin de error
    //--------------------------------------------------------------------------
    // * * *   AQUI EMPIEZA  EL CODIGO DE LOS PROCEDURES    * * *
    
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(programa) = {PRIMEROS(declaraciones), PRIMEROS(declaracion_subprograma), 
    //                       PRIMEROS(proposiciones_optativas), end}
    
    private void programa () { 
        if ( preAnalisis.equals ( "dim" ) || preAnalisis.equals ( "function" ) ||
             preAnalisis.equals ( "id" ) || preAnalisis.equals ( "end" ) ||
             preAnalisis.equals ( "call" ) || preAnalisis.equals ( "if" )  ||
             preAnalisis.equals ( "do" ) ) {
            /* programa -> declaraciones
                           declataciones_subprogramas
                           proposiciones_optativas
                           end
            */
            declaraciones ();
            declaraciones_subprogramas ();
            proposiciones_optativas ();
            emparejar ( "end" );
        } else {
            error ( "[programa] Programa debe de iniciar con una declaracion "
                    + "de variable. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(declaraciones) = {dim, empty}
    
    private void declaraciones () {
        if ( preAnalisis.equals ( "dim" ) ) {
            // declaraciones -> dim lista_declaraciones declaraciones
            emparejar ( "dim" );
            lista_declaraciones ();
            declaraciones ();
        } /* else {
            // declaraciones -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(lista_declaraciones) = {id}
    
    private void lista_declaraciones () {
        if ( preAnalisis.equals ( "id" ) ) {
            // lista_declaraciones -> id as tipo lista_declaraciones'
            emparejar ( "id" );
            emparejar ( "as" );
            tipo ();
            lista_declaraciones_prima ();
        } else {
            error ( "[lista_declaraciones] Se esperaba una declaracion "
                    + "de variable en la lista. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(lista_declaraciones’) = {,, empty}
    
    private void lista_declaraciones_prima () {
        if ( preAnalisis.equals ( "," ) ) {
            // lista_declaraciones' -> , lista_declaraciones
            emparejar ( "," );
            lista_declaraciones ();
        } /* else {
            // listas_declaraciones' -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(tipo) = {integer, single, string}
    
    private void tipo () {
        if ( preAnalisis.equals ( "integer" ) ) {
            // tipo -> integer
            emparejar ( "integer" );
        } else if ( preAnalisis.equals ( "single" ) ) {
            // tipo -> single
            emparejar ( "single" );
        } else if ( preAnalisis.equals ( "string" ) ) {
            // tipo -> string
            emparejar ( "string" );
        } else {
            error ( "[tipo] Se esperaba una constante integer, "
                    + "single y string. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(declaraciones_subprogramas) = {PRIMEROS(declaracion_subprograma), 
    //                                         empty}
    
    private void declaraciones_subprogramas () {
        if ( preAnalisis.equals ( "function" ) || preAnalisis.equals ( "sub" ) ) {
            // declaraciones_subprogramas -> declaracion_subprograma 
            //                               declaraciones_subprogramas
            declaracion_subprograma ();
            declaraciones_subprogramas ();
        } /* else {
            // declaraciones_subprogramas -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(declaracion_subprograma) = {PRIMEROS(declaracion_funcion), 
    //                                      PRIMEROS(declaracion_subrutina)}
    
    private void declaracion_subprograma () {
        if ( preAnalisis.equals ( "function" ) ) {
            //declaracion_subprograma -> declaracion_funcion
            declaracion_funcion ();
        } else if ( preAnalisis.equals ( "sub" ) ) {
            //declaracion_subprograma -> declaracion_subrutina
            declaracion_subrutina ();
        } else {
          error ( "[declaracion_subprograma] Se esperaba una funcion o "
                    + "una subrutina. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(declaraciones_funcion) = {function}
    
    private void declaracion_funcion () {
        if ( preAnalisis.equals ( "function" ) ) {
            /* declaracion_funcion -> function id ( argumentos ) as
                                      tipo proposiciones_optativas end function */
            emparejar ( "function" );
            emparejar ( "id" );
           // emparejar ( "(" );
            argumentos ();
           // emparejar ( ")" );
            emparejar ( "as" );
            tipo ();
            proposiciones_optativas ();
            emparejar ( "end" );
            emparejar ( "function" );
        } else {
            error ( "[declaracion_funcion] Se esperaba una funcion "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(declaracion_subrutina) = {sub}
    
    private void declaracion_subrutina () {
        if ( preAnalisis.equals ( "sub" ) ) {
            /* declaracion_subrutina -> sub id argumentos proposiciones_optativas
                                        end sub
            */
            emparejar ( "sub" );
            emparejar ( "id" );
            argumentos ();
            proposiciones_optativas ();
            emparejar ( "end" );
            emparejar ( "sub" );
        } else {
            error ( "[declaracion_subrutina] Se esperaba una subrutina valida "
                   + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(argumentos) = {(, empty}
    
    private void argumentos () {
        if ( preAnalisis.equals ( "(" ) ) {
            // argumentos -> ( lista_declaraciones ) 
            emparejar ( "(" );
            lista_declaraciones ();
            emparejar ( ")" );
        } /* else {
            // argumentos -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(proposiciones_optativas) = {PRIMEROS(proposicion), empty}
    
    private void proposiciones_optativas () {
        if ( preAnalisis.equals ( "id" ) || preAnalisis.equals ( "call" ) ||
             preAnalisis.equals ( "if" ) || preAnalisis.equals ( "do" ))  {
            // proposiciones_optativas -> proposicion proposiciones_optativas
            proposicion ();
            proposiciones_optativas ();
        } /* else {
            // proposiciones_optativias -> empry
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(proposicion) = {id, call, if, do}
    
    private void proposicion () {
        if ( preAnalisis.equals ( "id" ) ) {
            // proposicion -> id opasig expresion
            emparejar ( "id" );
            emparejar ( "opasig" );
            expresion ();
        } else if ( preAnalisis.equals( "call" ) ) {
            // proposicion -> call id proposicion'
            emparejar ( "call" );
            emparejar ( "id" );
            proposicion_prima ();
        } else if ( preAnalisis.equals ( "if" ) ) {
            // proposicion -> if condicion then proposicicones_optativas else 
            //                proposiciones_optativas end if
            emparejar ( "if" );
            condicion ();
            emparejar ( "then" );
            proposiciones_optativas ();
            emparejar ( "else" );
            proposiciones_optativas ();
            emparejar ( "end" );
            emparejar ( "if" );
        } else if ( preAnalisis.equals ( "do" ) ) {
            // proposiccion -> do while condicion proposiciones_optativas loop
            emparejar ( "do" );
            emparejar ( "while" );
            condicion ();
            proposiciones_optativas ();
            emparejar ( "loop" );
        } else {
            error ( "[proposicion] Se esperaba una proposicion valida "
                   + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(proposicion’) = {(, empty}
    
    private void proposicion_prima () {
        if ( preAnalisis.equals ( "(" ) ) {
            // proposicion' -> ( lista_expresiones ) 
            emparejar ( "(" );
            lista_expresiones ();
            emparejar ( ")" );
        } /* else {
            // proposicion' -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(lista_expresiones) = {PRIMEROS(expresion), empty}
    
    private void lista_expresiones () {
        if ( preAnalisis.equals ( "id" ) || preAnalisis.equals ( "literal" ) ||
             preAnalisis.equals ( "num" ) || preAnalisis.equals ( "num.num" ) ||
             preAnalisis.equals ( "(" ) ) {
            // lista_expresiones -> expresion lista_expresiones' 
            expresion ();
            lista_expresiones_prima ();
         
        } /* else {
            // lista_expresiones -> empty
        } */
    }
    
    
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(lista_expresiones') = {,, empty}
    
     private void lista_expresiones_prima () {
        if ( preAnalisis.equals ( "," ) ) {
            /*lista_expresiones' -> , expresion lista_expresiones' */
            emparejar ( "," );
            expresion ();
            lista_expresiones_prima ();
        } /* else { 
            // lista_expresiones' -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(condicion) = {PRIMEROS(expresion)}
    
    private void condicion () {
        if ( preAnalisis.equals ( "id" )  || preAnalisis.equals ( "num" ) || 
             preAnalisis.equals ( "num.num" ) || preAnalisis.equals ( "(" ) || 
             preAnalisis.equals ( "literal" ) ) {
            /* condicion -> expresion oprel expresion */
            expresion ();
            emparejar ( "oprel" );
            expresion ();
        } else {
            error("[condicion] Se esperaba el nombre de un identificador, "
                    + "una constante numérica, o paréntesis o una literal "
                    + "seguido de un operador relacional "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(expresion) = {PRIMEROS(termino), literal}
    
    private void expresion () {
        if ( preAnalisis.equals ( "id" ) || preAnalisis.equals ( "num" ) ||
             preAnalisis.equals ( "num.num" ) || preAnalisis.equals ( "(" ) ) {
            /* expresion -> termino expresion' */
            termino ();
            expresion_prima ();
        } else if ( preAnalisis.equals ( "literal" ) ){
            // expresion -> literal
            emparejar ( "literal" );
        } else {
            error ( "{expresion} Se esperaba el nombre de un identificador, "
                    + "una constante numérica, o paréntesis o una literal" 
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(expresion') = {opsuma, empty}
    
    private void expresion_prima () {
        if ( preAnalisis.equals ( "opsuma" ) ) {
            /* expresion' -> opsuma termino expresiom' */
            emparejar ( "opsuma" );
            termino ();
            expresion_prima ();
        } /* else { 
            // expresion' -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(termino) = {PRIMEROS(factor)}
    
    private void termino () {
        if ( preAnalisis.equals ( "id" ) || preAnalisis.equals ( "num" ) ||
             preAnalisis.equals ( "num.num" ) || preAnalisis.equals ( "(" )) {
            // termino -> factor termino'
            factor ();
            termino_prima ();
        } else {
            error ( "[termino] Se esperaba un termino "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(termino') = {opmult}
    
    private void termino_prima () {
        if ( preAnalisis.equals ( "opmult" ) ) {
            /* termino' -> opmult factor termino' */
            emparejar ( "opmult" );
            factor ();
            termino_prima ();
        } /* else {
            // termino' -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(factor) = {id, num, num.num, (}
    
    private void factor () {
        if ( preAnalisis.equals ( "id" ) ) {
            // factor -> id factor'
            emparejar ( "id" );
            factor_prima ();
        }
         else if ( preAnalisis.equals ( "num" ) ) {
            // factor -> num
            emparejar ( "num" );
        }
        else if ( preAnalisis.equals ( "num.num" ) ) {
            // factor -> num.num
            emparejar ( "num.num" );
        }
        else if ( preAnalisis.equals ( "(" ) ){
            // factor -> ( expresion )
            emparejar ( "(" );
            expresion ();
            emparejar ( ")" );
        } else {
            error ( "{factor} Se esperaba el nombre de un identificador, "
                    + "una constante numérica, o paréntesis"
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea);
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(factor') = {(, empty}
    
    private void factor_prima () {
        if ( preAnalisis.equals ( "(" ) ) {
            /* factor' -> ( lista_expresiones ) */
            emparejar ( "(" );
            lista_expresiones ();
            emparejar ( ")" );
        } /* else {
            // factor -> empty
        } */
    }
}

//------------------------------------------------------------------------------
//::