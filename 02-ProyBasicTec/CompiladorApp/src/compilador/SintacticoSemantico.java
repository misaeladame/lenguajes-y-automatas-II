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
    // PRIMEROS(programa) = {PRIMEROS(declaraciones)}
    
    private void programa () {
        if ( preAnalisis.equals ( "dim" ) ) {
            /* programa -> declaraciones
                           declataciones_subprogramas
                           proposiciones_optativas
                           end
            */
            declaraciones ();
            declaraciones_subprogramas ();
            declaraciones_optativas ();
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
        } /*else {
            // declaraciones -> empty
        }*/
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
        } /*else {
            // listas_declaraciones' -> empty
        }*/
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
    // PRIMEROS(declaraciones_subprogramas) = {PRIMEROS(declaraciones_subprograma), 
    //                                         empty}
    
    private void declaraciones_subprogramas () {
        if ( preAnalisis.equals ( "function" ) || preAnalisis.equals ( "sub" ) ) {
            // declaraciones_subprogramas -> declaracion_subprograma 
            //                               declaraciones_subprogramas
            declaracion_subprograma ();
            declaraciones_subprogramas ();
        }
        /*else{
            // declaraciones_subprogramas -> empty
        }*/
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(declaraciones_subprograma) = {PRIMEROS(declaraciones_funcion), 
    //                                        PRIMEROS(declaraciones_subrutina)}
    
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
    
    private void declaracion_funcion(){
        if ( preAnalisis.equals ( "function") ) {
            /*declaracion_funcion -> function id ( argumentos ) as
                as tipo proposiciones_optativas end function*/
            emparejar ( "function" );
            emparejar ( "id" );
            emparejar ( "(" );
            argumentos ();
            emparejar ( ")" );
            emparejar ( "as" );
            tipo ();
            proposiciones_optativas ();
            emparejar ( "end" );
            emparejar ( "function" );
        } else{
            error ( "[declaracion_funcion] Se esperaba una funcion "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------

    
}

//------------------------------------------------------------------------------
//::