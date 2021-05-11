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
 *: 06/May/2021 Misael Adame        -Se implementa los esquemas de traducción 
 *:-----------------------------------------------------------------------------
 */
package compilador;

import javax.swing.JOptionPane;

public class SintacticoSemantico {
    
    public static final String VACIO      = "vacio";
    public static final String ERROR_TIPO = "error_tipo";
    public static final String NIL        = ""; 

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

    public void analizar(boolean analizarSemantica) {
        this.analizarSemantica = analizarSemantica;
        preAnalisis = cmp.be.preAnalisis.complex;

        // * * *   INVOCAR AQUI EL PROCEDURE DEL SIMBOLO INICIAL   * * *
        P ( new Atributos () );
    }

    //--------------------------------------------------------------------------

    private void emparejar(String t) {
        if (cmp.be.preAnalisis.complex.equals(t)) {
            cmp.be.siguiente();
            preAnalisis = cmp.be.preAnalisis.complex;            
        } else {
            errorEmparejar( t, cmp.be.preAnalisis.lexema, cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Metodo para devolver un error al emparejar
    //--------------------------------------------------------------------------
 
    private void errorEmparejar(String _token, String _lexema, int numLinea ) {
        String msjError = "";

        if (_token.equals("id")) {
            msjError += "Se esperaba un identificador";
        } else if (_token.equals("num")) {
            msjError += "Se esperaba una constante entera";
        } else if (_token.equals("num.num")) {
            msjError += "Se esperaba una constante real";
        } else if (_token.equals("literal")) {
            msjError += "Se esperaba una literal";
        } else if (_token.equals("oparit")) {
            msjError += "Se esperaba un operador aritmetico";
        } else if (_token.equals("oprel")) {
            msjError += "Se esperaba un operador relacional";
        } else if (_token.equals("opasig")) {
            msjError += "Se esperaba operador de asignacion";
        } else {
            msjError += "Se esperaba " + _token;
        }
        msjError += " se encontró " + ( _lexema.equals ( "$" )? "fin de archivo" : _lexema ) + 
                    ". Linea " + numLinea;        // FGil: Se agregó el numero de linea

        cmp.me.error(Compilador.ERR_SINTACTICO, msjError);
    }

    // Fin de ErrorEmparejar
    //--------------------------------------------------------------------------
    // Metodo para mostrar un error sintactico

    private void error(String _descripError) {
        cmp.me.error(cmp.ERR_SINTACTICO, _descripError);
    }

    // Fin de error
    //--------------------------------------------------------------------------
    //  *  *   *   *    PEGAR AQUI EL CODIGO DE LOS PROCEDURES  *  *  *  *
    //--------------------------------------------------------------------------
    // PRIMEROS ( P ) = { PRIMEROS ( V ), PRIMEROS ( C ) }
    // PRIMEROS ( P ) = { id, inicio }     
        
    private void P ( Atributos P ) {
        
        Atributos V = new Atributos ();
        Atributos C = new Atributos ();
        
        if ( preAnalisis.equals ( "id" ) || preAnalisis.equals ( "inicio" ) ) {
            // P -> V C
            V ( V );
            C ( C );
            // Accion semantica 1
            if ( analizarSemantica ) {
                P.tipo = ( V.tipo.equals ( VACIO ) && C.tipo.equals ( VACIO ) ) ? VACIO : ERROR_TIPO;
                if ( P.tipo.equals ( ERROR_TIPO ) ) {
                    cmp.me.error ( Compilador.ERR_SEMANTICO, 
                        "[P] Programa con errores de tipo en la declaracion de variables o en sentencias" );
                }
            }
            // Fin accion semantica 1
        } else {
            error ( "[P] Programa debe iniciar con una declaración de variable o "
                    + "con la palabra reservada inicio. "
                    + "No. de linea: " + cmp.be.preAnalisis.numLinea
                  );
        }
    }
    
    //------------------------------------------------------------------------------
    // PRIMEROS ( V ) = { id, empty }
    
    private void V ( Atributos V ) {
        
        Linea_BE id = new Linea_BE ();
        Atributos T = new Atributos ();
        Atributos V1 = new Atributos ();
        
        if ( preAnalisis.equals ( "id" ) ) {
            // V -> id : T V
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            emparejar ( ":" );
            T ( T );
            // Accion semantica 6 
            if ( analizarSemantica ) {
                if ( cmp.ts.buscaTipo ( id.entrada ).equals ( NIL ) ) {
                    cmp.ts.anadeTipo ( id.entrada, T.tipo );
                    V.tipoaux = VACIO;
                } else {
                    V.tipoaux = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                        "[V] Error identificador ya declarado : " + id.lexema );
                }
            }
            // Fin accion semantica 6
            V ( V1 );
            // Accion semantica 7
            if ( analizarSemantica ) {
                V.tipo = ( V.tipoaux.equals ( VACIO ) && V1.tipo.equals ( VACIO ) ) ? VACIO : ERROR_TIPO;
                if ( V.tipo.equals ( ERROR_TIPO ) ) {
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                            "[V] Error de tipos en la seccion de declaraciones de variables.");
                }
            }
            // Fin accion semantica 7
        } 
        else {
            // V -> empty
            // Accion semantica 2
            if ( analizarSemantica ) 
                V.tipo = VACIO;
            // Fin accion semantica 2
        }
    }
    
    //------------------------------------------------------------------------------
    // PRIMEROS ( T ) = { entero, real, caracter }
    
    private void T ( Atributos T ) {
        // No requiere variables locales
        
        if ( preAnalisis.equals ( "entero" ) ) {
            // T -> entero
            emparejar ( "entero" );
            // Accion semantica 3
            if ( analizarSemantica )
                T.tipo = "entero";
            // Fin Accion semantica 3
        } else if ( preAnalisis.equals ( "real" ) ) {
            // T -> real
            emparejar ( "real" );
            // Accion semantica 4
            if ( analizarSemantica )
                T.tipo = "real";
            // Fin Accion semantica 4
        } else if ( preAnalisis.equals ( "caracter" ) ) {
            // T -> caracter 
            emparejar ( "caracter" );
            // Accion semantica 5
            if ( analizarSemantica )
                T.tipo = "caracter";
            // Fin Accion semantica 5
        } else {
            error ("[T]: Se esperaba un tipo de dato: entero, real o caracter. No. de Línea " 
                    + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //------------------------------------------------------------------------------
    // PRIMEROS ( C ) = { inicio }
    
    private void C ( Atributos C ) {
        
        Atributos S = new Atributos ();
        
        if ( preAnalisis.equals ( "inicio" ) ) {
            // C -> inicio S end 
            emparejar ( "inicio" );
            S ( S );
            emparejar ( "fin" );
            // Accion semantica 8
            if ( analizarSemantica )
                C.tipo = S.tipo;
            // Fin Accion semantica 8
        } else {
            error ("[C]: El cuerpo del programa debe comenzar con la palabra inicio. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //------------------------------------------------------------------------------
    // PRIMEROS ( S ) = { id, empty }
    
    private void S ( Atributos S ) {
        
        Linea_BE id = new Linea_BE ();
        Atributos E = new Atributos ();
        Atributos S1 = new Atributos ();
        
        if ( preAnalisis.equals ( "id" ) ) {
            // S -> id opasig E S
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            emparejar ( "opasig" );
            E ( E );
            // Accion semantica 14
            if ( analizarSemantica ) {
                if ( cmp.ts.buscaTipo ( id.entrada ).equals ( E.tipo ) ||
                   ( cmp.ts.buscaTipo ( id.entrada ).equals ( "real" ) &&  
                      E.tipo.equals ( "entero" ) ) ) {
                    S.tipoaux = VACIO;
                } else {
                    S.tipoaux = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                        "[S] Error, la asignacion no es compatible al tipo de dato "
                               + "de la variable declarada" );
                }
            }
            // Fin Accion semantica 14
            S ( S1 ); 
            // Accion semantica 15
            if ( analizarSemantica ) {
                S.tipo = ( S.tipoaux.equals ( VACIO ) && S1.tipo.equals ( VACIO ) ) ? VACIO : ERROR_TIPO;
                if ( S.tipo.equals ( ERROR_TIPO ) ) {
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                            "[S] Error de tipos en la seccion de asignaciones de variables.");
                }
            }
            // Fin accion semantica 15
        } else {
            // S -> empty
            // Accion semantica 9
            if ( analizarSemantica )
                S.tipo = VACIO;
            // Fin accion semantica 9
        }
    }
    
    //------------------------------------------------------------------------------
    // PRIMEROS ( E ) = { id, num, num.num, literal }
    
    private void E ( Atributos E ) {
        
        Linea_BE id = new Linea_BE ();
        
        if ( preAnalisis.equals ( "id" ) ) {
            // E -> id
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            // Accion semantica 10
            if ( analizarSemantica )
                E.tipo = cmp.ts.buscaTipo ( id.entrada );
            // Fin accion semantica 10
        } else if ( preAnalisis.equals ( "num" ) ) {
            // E -> num
            emparejar ( "num" );
            // Accion semantica 11
            if ( analizarSemantica ) 
                E.tipo = "entero";
            // Fin accion semantica 11
        } else if ( preAnalisis.equals ( "num.num" ) ) {
            // E -> num.num
            emparejar ( "num.num" );
            // Accion semantica 12
            if ( analizarSemantica ) 
                E.tipo = "real";
            // Fin accion semantica 12
        } else if ( preAnalisis.equals ( "literal" ) ) {
            // E -> literal
            emparejar ( "literal" );
            // Accion semantica 13
            if ( analizarSemantica ) 
                E.tipo = "caracter";
            // Fin accion semantica 13
        } else {
            error ("[E]: Expresion invalida, debe iniciar con un identificador o " +
                   "con constante numerica. No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
}
//------------------------------------------------------------------------------
//::