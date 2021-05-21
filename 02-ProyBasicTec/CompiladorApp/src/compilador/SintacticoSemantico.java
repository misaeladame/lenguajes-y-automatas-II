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
 *: 22/Mar/2021 Cristian Piña       Se agregaron los Procedures de:
 *:                                 programa ()
 *:                                 declaraciones ()
 *:                                 lista_declaraciones ()
 *:                                 lista_declaraciones_prima ()
 *:                                 tipo ()
 *:                                 declaraciones_subprogramas ()
 *:                                 declaracion_funcion ()
 *: 23/Mar/2021 Misael Adame        Se agregaron los Procedures de:
 *:                                 declaracion_subrutina ()
 *:                                 argumentos ()
 *:                                 proposiciones_optativas ()
 *:                                 proposicion ()
 *:                                 proposicion_prima ()
 *:                                 lista_expresiones ()
 *: 23/Mar/2021 Sergio Chairez      Se agregaron los Procedures de:
 *:                                 lista_expresiones_prima ()
 *:                                 condicion ()
 *:                                 expresion ()
 *:                                 expresion_prima ()
 *:                                 termino ()
 *:                                 termino_prima ()
 *:                                 factor ()
 *:                                 factor_prima ()
 *:----------------------------------------------------------------------------
 */
package compilador;

import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class SintacticoSemantico {
    
    public static final String VACIO         = "vacio";
    public static final String ERROR_TIPO    = "error_tipo";
    public static final String NIL           = "";

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
        programa ( new Atributos () );
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
    // PRIMEROS(programa) = {PRIMEROS(declaraciones), PRIMEROS(declaracion_subprogramas), 
    //                       PRIMEROS(proposiciones_optativas), end}
    
    private void programa ( Atributos programa ) { 
        
        Atributos declaraciones = new Atributos ();
        Atributos declaraciones_subprogramas = new Atributos ();
        Atributos proposiciones_optativas = new Atributos ();
        
        if ( preAnalisis.equals ( "dim" ) || preAnalisis.equals ( "function" ) ||
             preAnalisis.equals ( "sub" ) || preAnalisis.equals ( "id" )  || 
             preAnalisis.equals ( "call" ) || preAnalisis.equals ( "if" )  || 
             preAnalisis.equals ( "do" ) || preAnalisis.equals ( "end" ) ) {
            
            /* programa -> declaraciones
                           declaraciones_subprogramas
                           proposiciones_optativas
                           end {1}
            */
            declaraciones ( declaraciones );
            declaraciones_subprogramas ( declaraciones_subprogramas );
            proposiciones_optativas ( proposiciones_optativas );
            emparejar ( "end" );
            
            // Accion semantica 1
            if ( analizarSemantica ) {
               if ( declaraciones.t.equals ( VACIO ) && 
                    declaraciones_subprogramas.t.equals ( VACIO )  && 
                    proposiciones_optativas.t.equals ( VACIO ) 
                       ) 
                   programa.t = VACIO;
                else {
                   programa.t = ERROR_TIPO;
                   cmp.me.error ( Compilador.ERR_SEMANTICO, 
                        "[programa] Programa con errores de tipo en la declaracion de variables o en sentencias" );
                }  
            }
            // Fin accion semantica 1
            
        } else {
            error ( "[programa] Programa debe de iniciar con una declaracion "
                    + "de variable. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(declaraciones) = {dim, empty}
    
    private void declaraciones ( Atributos declaraciones ) {
        
        Atributos lista_declaraciones = new Atributos ();
        Atributos declaraciones1 = new Atributos ();
        
        if ( preAnalisis.equals ( "dim" ) ) {
            // declaraciones -> dim {2} lista_declaraciones declaraciones1 {4}
            emparejar ( "dim" );
            
            // Accion semantica 2
            if ( analizarSemantica ) 
                lista_declaraciones.taux = "declaraciones";
            // Fin accion semantica 2
            
            lista_declaraciones ( lista_declaraciones );  
            declaraciones ( declaraciones1 );
            
            // Accion semantica 4
            if ( analizarSemantica ) {
                if ( lista_declaraciones.t.equals ( VACIO ) && 
                     declaraciones1.t.equals ( VACIO ) )
                    declaraciones.t = VACIO;
                else {
                    declaraciones.t = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                            "[declaraciones] Error de tipos en la seccion de declaracion de variables.");
                }
            }
            // Fin Accion semantica 4
            
        } else {
            // declaraciones -> empty {3}
            
            // Accion semantica 3
            if ( analizarSemantica ) 
                declaraciones.t = VACIO;
            // Fin accion semantica 3
        } 
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(lista_declaraciones) = {id}
    
    private void lista_declaraciones ( Atributos lista_declaraciones ) {
        
        Linea_BE id = new Linea_BE ();
        Atributos tipo = new Atributos ();
        Atributos lista_declaraciones_prima = new Atributos ();
        
        if ( preAnalisis.equals ( "id" ) ) {
            // lista_declaraciones -> id as tipo {9} lista_declaraciones' {10}
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            emparejar ( "as" );
            tipo ( tipo );
            
            // Accion semantica 9
            if ( analizarSemantica )
                lista_declaraciones_prima.taux = lista_declaraciones.taux;
            // Fin accion semantica 9
            
            lista_declaraciones_prima ( lista_declaraciones_prima );
        
            // Accion semantica 10
            if ( analizarSemantica ) {
                if ( cmp.ts.buscaTipo ( id.entrada ).equals ( NIL ) ) {
                    cmp.ts.anadeTipo ( id.entrada, tipo.t );
                    if ( lista_declaraciones.taux.equals ( "declaraciones" ) ) {
                        if ( lista_declaraciones_prima.t.equals ( VACIO ) ) 
                            lista_declaraciones.t = VACIO;
                        else {
                            lista_declaraciones.t = ERROR_TIPO;
                            cmp.me.error ( Compilador.ERR_SEMANTICO, "[lista_declaraciones] Error identificador ya declarado " +id.lexema);
                        }
                    } else if ( ! lista_declaraciones_prima.t.equals ( ERROR_TIPO ) ) {
                        if ( ! lista_declaraciones_prima.t.equals ( VACIO ) )
                            lista_declaraciones.t = tipo.t + " X " + lista_declaraciones_prima.t;
                        else
                            lista_declaraciones.t = tipo.t;
                    } else {
                        lista_declaraciones.t = ERROR_TIPO;
                        cmp.me.error ( Compilador.ERR_SEMANTICO, "[lista_declaraciones] Error identificador ya declarado " +id.lexema);
                    }   
                } else {
                    lista_declaraciones.t = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO, "[lista_declaraciones] Error identificador ya declarado " +id.lexema);
                }
            }
            // Fin accion semantica 10
            
        } else {
            error ( "[lista_declaraciones] Se esperaba una declaracion "
                    + "de variable en la lista. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(lista_declaraciones’) = {,, empty}
    
    private void lista_declaraciones_prima ( Atributos lista_declaraciones_prima ) {
        
        Atributos lista_declaraciones = new Atributos ();
        
        if ( preAnalisis.equals ( "," ) ) {
            // lista_declaraciones' -> , {16} lista_declaraciones {11}
            emparejar ( "," );
            
            // Accion seamntica 16
            if ( analizarSemantica )
                lista_declaraciones.taux = lista_declaraciones_prima.taux;
            // Fin accion semantica 16
            
            lista_declaraciones ( lista_declaraciones );
            
            // Accion semantica 11
            if ( analizarSemantica )
                lista_declaraciones_prima.t = lista_declaraciones.t;
            // Fin accion semantica 11
            
        } else {
            // listas_declaraciones' -> empty {8}
            
            // Accion semantica 8
            if ( analizarSemantica ) 
                lista_declaraciones_prima.t = VACIO;
            
            // Fin accion semantica 8
        } 
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(tipo) = {integer, single, string}
    
    private void tipo ( Atributos tipo ) {
        
        // No se requieren variables locales
        
        if ( preAnalisis.equals ( "integer" ) ) {
            // tipo -> integer {5}
            emparejar ( "integer" );
            
            // Accion semantica 5
            if ( analizarSemantica ) 
                tipo.t = "integer";
            // Fin accion semantica 5
            
        } else if ( preAnalisis.equals ( "single" ) ) {
            // tipo -> single {6}
            emparejar ( "single" );
            
            // Accion semantica 6
            if ( analizarSemantica ) 
                tipo.t = "single";
            // Fin accion semantica 6
            
        } else if ( preAnalisis.equals ( "string" ) ) {
            // tipo -> string {7}
            emparejar ( "string" );
            
            // Accion semantica 7
            if ( analizarSemantica ) 
                tipo.t = "string";
            // Fin accion semantica 7
            
        } else {
            error ( "[tipo] Se esperaba una constante integer, "
                    + "single o string. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(declaraciones_subprogramas) = {PRIMEROS(declaracion_subprograma), 
    //                                         empty}
    
    private void declaraciones_subprogramas ( Atributos declaraciones_subprogramas ) {
        
        Atributos declaracion_subprograma = new Atributos ();
        Atributos declaraciones_subprogramas1 = new Atributos ();
        
        if ( preAnalisis.equals ( "function" ) || preAnalisis.equals ( "sub" ) ) {
            // declaraciones_subprogramas -> declaracion_subprograma
            //                               declaraciones_subprogramas {12}
            declaracion_subprograma ( declaracion_subprograma );
            declaraciones_subprogramas ( declaraciones_subprogramas1 );
            
            // Accion semantica 12
            if ( analizarSemantica ) {
                if ( declaracion_subprograma.t.equals ( VACIO ) && 
                     declaraciones_subprogramas1.t.equals ( VACIO ) )
                    declaraciones_subprogramas.t = VACIO;
                else {
                    declaraciones_subprogramas.t = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                            "[declaraciones_subprogramas] Error en la declaracion de una función y/o subrutina.");
                }
            }
            // Fin accion semantica 12
            
        } else {
            // declaraciones_subprogramas -> empty {13}
            
            // Accion semantica 13
            if ( analizarSemantica ) 
                declaraciones_subprogramas.t = VACIO;
            // Fin accion semantica 13
            
        } 
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(declaracion_subprograma) = {PRIMEROS(declaracion_funcion), 
    //                                      PRIMEROS(declaracion_subrutina)}
    
    private void declaracion_subprograma ( Atributos declaracion_subprograma ) {
        
        Atributos declaracion_funcion = new Atributos ();
        Atributos declaracion_subrutina = new Atributos ();
    
        if ( preAnalisis.equals ( "function" ) ) {
            // declaracion_subprograma -> declaracion_funcion {14}
            declaracion_funcion ( declaracion_funcion );
            
            // Accion semantica 14
            if ( analizarSemantica )
                declaracion_subprograma.t = declaracion_funcion.t;
            // Fin accion semantica 14
            
            
        } else if ( preAnalisis.equals ( "sub" ) ) {
            //declaracion_subprograma -> declaracion_subrutina {15}
            declaracion_subrutina ( declaracion_subrutina );
            
            // Accion semantica 15
            if ( analizarSemantica )
                declaracion_subprograma.t = declaracion_subrutina.t;
            // Fin accion semantica 15
            
        } else {
          error ( "[declaracion_subprograma] Se esperaba una funcion o "
                    + "una subrutina. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(declaracion_funcion) = {function}
    
    private void declaracion_funcion ( Atributos declaracion_funcion ) {
        
        Linea_BE id = new Linea_BE ();
        Atributos tipo = new Atributos ();
        Atributos argumentos = new Atributos ();
        Atributos proposiciones_optativas = new Atributos ();
        String expresion_tipo = new String ();
        
        // Atributos proposiciones_optativas = new Atributos ();
        
        if ( preAnalisis.equals ( "function" ) ) {
            // declaracion_funcion -> function id argumentos as tipo {17}
            //                        proposiciones_optativas end function {18} 
            emparejar ( "function" );
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            argumentos ( argumentos );
            emparejar ( "as" );
            tipo ( tipo );
            
            // Accion semantica 17
            if ( analizarSemantica ) {
                if ( cmp.ts.buscaTipo ( id.entrada ).equals ( NIL ) &&
                     ! argumentos.t.equals ( ERROR_TIPO ) ) {
                    expresion_tipo = argumentos.t + " -> " + tipo.t;
                    cmp.ts.anadeTipo ( id.entrada, expresion_tipo );
                    declaracion_funcion.taux = VACIO;
                } else {
                    declaracion_funcion.taux = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                        "[declaracion_funcion] Error funcion ya declarada : " + id.lexema );
                }
            }
            // Fin accion semantica 17
            
            proposiciones_optativas ( proposiciones_optativas );
            emparejar ( "end" );
            emparejar ( "function" );
            
            // Accion semantica 18
            if ( analizarSemantica ) {
                if ( declaracion_funcion.taux.equals ( VACIO ) && 
                     proposiciones_optativas.t.equals ( VACIO ) )
                    declaracion_funcion.t = VACIO;
                else {
                    declaracion_funcion.t = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                        "[declaracion_funcion] Error en la declaracion de la funcion");
                }
            }
            // Fin accion semantica 18
            
        } else {
            error ( "[declaracion_funcion] Se esperaba una funcion. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(declaracion_subrutina) = {sub}
    
    private void declaracion_subrutina ( Atributos declaracion_subrutina ) {
        
        Linea_BE id = new Linea_BE ();
        Atributos argumentos = new Atributos ();
        Atributos proposiciones_optativas = new Atributos ();
        String expresion_tipo = new String ();
        
        if ( preAnalisis.equals ( "sub" ) ) {
            // declaracion_subrutina -> sub id argumentos {19} proposiciones_optativas
            //                          end sub {20}
            
            emparejar ( "sub" );
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            argumentos ( argumentos );
            
            // Accion semantica 19
            if ( analizarSemantica ) {
                if ( cmp.ts.buscaTipo ( id.entrada ).equals ( NIL ) &&
                     ! argumentos.t.equals ( ERROR_TIPO ) ) {
                    expresion_tipo = argumentos.t + " -> void";
                    cmp.ts.anadeTipo ( id.entrada, expresion_tipo );
                    declaracion_subrutina.taux = VACIO;
                } else {
                    declaracion_subrutina.taux = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                        "[declaracion_subrutina] Error subrutina ya declarada : " + id.lexema );
                }
            }
            // Fin accion semantica 19
            
            proposiciones_optativas ( proposiciones_optativas );
            emparejar ( "end" );
            emparejar ( "sub" );
            
            // Accion semantica 20
            if ( analizarSemantica ) {
                if ( declaracion_subrutina.taux.equals ( VACIO ) && 
                     proposiciones_optativas.t.equals ( VACIO ) )
                    declaracion_subrutina.t = VACIO;
                else {
                    declaracion_subrutina.t = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO,
                        "[declaracion_subrutina] Error en la declaracion de la subrutina");
                }
            }
            // Fin accion semantica 20
            
            
        } else {
            error ( "[declaracion_subrutina] Se esperaba una subrutina valida. "
                   + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    

    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(argumentos) = {(, empty}
    
    private void argumentos ( Atributos argumentos ) {
        
        Atributos lista_declaraciones = new Atributos ();
    
        if ( preAnalisis.equals ( "(" ) ) {
            // argumentos -> ( {21} lista_declaraciones ) {23} 
            emparejar ( "(" );
            
            // Accion semantica 21
            if ( analizarSemantica ) 
                lista_declaraciones.taux = "argumentos";
            // Fin accion semantica 21
            
            lista_declaraciones ( lista_declaraciones );   
            emparejar ( ")" );
            
            // Accion semantica 23
            if ( analizarSemantica ) 
                argumentos.t = lista_declaraciones.t;
            // Fin accion semantica 23
            
        } else {
            // argumentos -> empty {22}
            
            // Accion semantica 22
            if ( analizarSemantica ) 
                argumentos.t = "void";
            // Fin accion semantica 22
        } 
    }
    
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(proposiciones_optativas) = {PRIMEROS(proposicion), empty}
    
    private void proposiciones_optativas ( Atributos proposiciones_optativas ) {
        
        Atributos proposicion = new Atributos ();
        Atributos proposiciones_optativas1 = new Atributos ();
    
        if ( preAnalisis.equals ( "id" ) || preAnalisis.equals ( "call" ) ||
             preAnalisis.equals ( "if" ) || preAnalisis.equals ( "do" ) )  {
            // proposiciones_optativas -> proposicion proposiciones_optativas1 {25}
            proposicion ( proposicion );
            proposiciones_optativas ( proposiciones_optativas1 );
            
            // Accion semantica 25
            if ( analizarSemantica ) {
                if ( proposicion.t.equals ( VACIO ) && 
                     proposiciones_optativas1.t.equals ( VACIO ) ) 
                    proposiciones_optativas.t = VACIO;
                else {
                    proposiciones_optativas.t = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO, 
                            "[proposiciones_optativas] Error en el analisis de las expresiones");
                }
            }
            // Fin accion semantica 25
            
        } else {
            // proposiciones_optativias -> empty {24}
            
            // Accion semantica 24
            if ( analizarSemantica ) 
                proposiciones_optativas.t = VACIO;
            // Fin accion semantica 24
            
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(proposicion) = {id, call, if, do}
    
    private void proposicion ( Atributos proposicion ) {
        
        Linea_BE id = new Linea_BE ();
        Atributos expresion = new Atributos ();
        Atributos proposicion_prima = new Atributos ();
        Atributos condicion = new Atributos (); 
        Atributos condicion1 = new Atributos ();
        Atributos proposiciones_optativas = new Atributos ();
        Atributos proposiciones_optativas1 = new Atributos ();
        Atributos proposiciones_optativas2 = new Atributos ();
        String expresion_tipo = new String ();
        
    
        if ( preAnalisis.equals ( "id" ) ) {
            // proposicion -> id opasig expresion {26}
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            emparejar ( "opasig" );
            expresion ( expresion );
            
            // Accion semantica 26
            if ( analizarSemantica ) {
                if ( ! expresion.t.equals ( ERROR_TIPO ) ) {
                    if ( cmp.ts.buscaTipo ( id.entrada ).equals ( expresion.t ) )
                        proposicion.t = VACIO;
                    else if ( cmp.ts.buscaTipo ( id.entrada ).equals ( "single" ) &&
                              ! expresion.t.equals ( "string" ) )
                        proposicion.t = VACIO;
                    else if ( cmp.ts.buscaTipo( id.entrada ).equals ( NIL ) ) {
                        proposicion.t = ERROR_TIPO;
                        cmp.me.error( Compilador.ERR_SEMANTICO, 
                                "[proposicion] Error hay un identificador sin ningún tipo de dato : " + id.lexema );
                    } else {
                        proposicion.t = ERROR_TIPO;
                        cmp.me.error ( Compilador.ERR_SEMANTICO, 
                                "[proposicion] Error los tipos de dato no son compatibles en alguna proposicion.");
                    }
                } else {
                    proposicion.t = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO, 
                            "[proposicion] Error la asignacion de variables no es valida");
                }
            }
            // Fin accion semantica 26
            
        } else if ( preAnalisis.equals( "call" ) ) {
            // proposicion -> call id proposicion' {27}
            emparejar ( "call" );
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            proposicion_prima ( proposicion_prima );
            
            // Accion semantica 27
            if ( analizarSemantica ) {
                if ( ! proposicion_prima.t.equals ( ERROR_TIPO ) ) {
                    expresion_tipo = proposicion_prima.t + "void";
                    if ( Pattern.matches ( expresion_tipo, cmp.ts.buscaTipo( id.entrada ) ) )
                        proposicion.t = VACIO;
                    else {
                        proposicion.t = ERROR_TIPO;
                        cmp.me.error ( Compilador.ERR_SEMANTICO, 
                                "[proposicion] Error la subrutina a la que trata de llamar no existe." );
                    }
                } else {
                    proposicion.t = ERROR_TIPO;
                    cmp.me.error ( Compilador.ERR_SEMANTICO, "[proposicion] Error. "
                    + "Argumentos no validos.");
                }
            }
            // Fin accion semantica 27
            
        } else if ( preAnalisis.equals ( "if" ) ) {
            // proposicion -> if condicion then proposicicones_optativas else 
            //                proposiciones_optativas end if
            emparejar ( "if" );
            condicion ( condicion );
            emparejar ( "then" );
            proposiciones_optativas ( proposiciones_optativas );
            emparejar ( "else" );
            proposiciones_optativas ( proposiciones_optativas1 );
            emparejar ( "end" );
            emparejar ( "if" );
        } else if ( preAnalisis.equals ( "do" ) ) {
            // proposicion -> do while condicion proposiciones_optativas loop
            emparejar ( "do" );
            emparejar ( "while" );
            condicion ( condicion1 );
            proposiciones_optativas ( proposiciones_optativas2 );
            emparejar ( "loop" );
        } else {
            error ( "[proposicion] Se esperaba una proposicion valida. "
                   + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(proposicion’) = {(, empty}
    
    private void proposicion_prima ( Atributos proposicion_prima ) {
        if ( preAnalisis.equals ( "(" ) ) {
            // proposicion' -> ( lista_expresiones ) 
            emparejar ( "(" );
            lista_expresiones ( lista_expresiones );
            emparejar ( ")" );
        } else {
            // proposicion' -> empty
        } 
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(lista_expresiones) = {PRIMEROS(expresion), empty}
    
    private void lista_expresiones ( Atributos lista_expresiones ) {
        if ( preAnalisis.equals ( "id" )  || preAnalisis.equals ( "num" ) || 
             preAnalisis.equals ( "num.num" ) || preAnalisis.equals ( "(" ) || 
             preAnalisis.equals ( "literal" ) ) {
            // lista_expresiones -> expresion lista_expresiones' 
            expresion ( expresion );
            lista_expresiones_prima ( lista_expresiones_prima );
        } else {
            // lista_expresiones -> empty
        } 
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(lista_expresiones') = {,, empty}
    
     private void lista_expresiones_prima ( Atributos lista_expresiones_prima ) {
        if ( preAnalisis.equals ( "," ) ) {
            // lista_expresiones' -> , expresion lista_expresiones' 
            emparejar ( "," );
            expresion ( expresionn );
            lista_expresiones_prima ();
        } else { 
            // lista_expresiones' -> empty
        } 
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(condicion) = {PRIMEROS(expresion)}
    
    private void condicion ( Atributos condicion ) {
        if ( preAnalisis.equals ( "id" )  || preAnalisis.equals ( "num" ) || 
             preAnalisis.equals ( "num.num" ) || preAnalisis.equals ( "(" ) || 
             preAnalisis.equals ( "literal" ) ) {
            // condicion -> expresion oprel expresion 
            expresion ();
            emparejar ( "oprel" );
            expresion ();
        } else {
            error ( "[condicion] Se esperaba el nombre de un identificador, "
                    + "una constante numérica, o paréntesis o una literal "
                    + "seguido de un operador relacional. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(expresion) = {PRIMEROS(termino), literal}
    
    private void expresion ( Atributos expresion ) {
        
        Linea_BE literal = new Linea_BE ();
        Atributos termino = new Atributos ();
        Atributos expresion_prima = new Atributos ();
        
        if ( preAnalisis.equals ( "id" ) || preAnalisis.equals ( "num" ) ||
             preAnalisis.equals ( "num.num" ) || preAnalisis.equals ( "(" ) ) {
            // expresion -> termino expresion' {45}
            termino ( termino );
            expresion_prima ( expresion_prima );
            
            // Accion semantica 45
            
            // Fin accion semantica 45
            
        } else if ( preAnalisis.equals ( "literal" ) ){
            // expresion -> literal {38}
            literal = cmp.be.preAnalisis;  // Salvamos los atributos de literal
            emparejar ( "literal" );
            
            // Accion semantica 38
            if ( analizarSemantica ) 
                expresion.t = cmp.ts.buscaTipo( literal.entrada );;
            // Fin accion semantica 38
            
        } else {
            error ( "[expresion] Se esperaba el nombre de un identificador, "
                    + "una constante numérica, o paréntesis o una literal. " 
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(expresion') = {opsuma, empty}
    
    private void expresion_prima ( Atributos expresion_prima ) {
        
        Atributos termino = new Atributos ();
        Atributos expresion_prima1 = new Atributos ();
        
        if ( preAnalisis.equals ( "opsuma" ) ) {
            // expresion' -> opsuma termino expresion' {39} 
            emparejar ( "opsuma" );
            termino ( termino );
            expresion_prima ( expresion_prima1 );
            
            // Accion semantica 39
            if ( analizarSemantica ) {
                if ( ! termino.t.equals ( ERROR_TIPO ) && 
                     ! expresion_prima1.t.equals ( ERROR_TIPO ) ) {
                    if ( expresion_prima1.t.equals ( VACIO ) ) 
                        expresion_prima.t = termino.t;
                    else if ( termino.t.equals ( expresion_prima1.t ) ) 
                        expresion_prima.t = termino.t;
                    else 
                        expresion_prima.t = "single";
                } else {
                    expresion_prima.t = ERROR_TIPO;
                    cmp.me.error(Compilador.ERR_SEMANTICO, 
                            "[expresion'] Error en el analisis de la expresion.");
                }
            }
            // Fin accion semantica 39
            
        } else { 
            // expresion' -> empty {43}
            
            // Accion semantica 43
            if ( analizarSemantica ) 
                expresion_prima.t = VACIO;
            // Fin accion semantica 43 
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(termino) = {PRIMEROS(factor)}
    
    private void termino ( Atributos termino ) {
        
        Atributos factor = new Atributos ();
        Atributos termino_prima = new Atributos ();
        
        if ( preAnalisis.equals ( "id" ) || preAnalisis.equals ( "num" ) ||
             preAnalisis.equals ( "num.num" ) || preAnalisis.equals ( "(" )) {
            // termino -> factor termino' {41}
            factor ( factor );
            termino_prima ( termino_prima );
            
            // Accion semantica 41
            if ( analizarSemantica ) {
                if ( ! factor.t.equals ( ERROR_TIPO ) && 
                     ! termino_prima.t.equals ( ERROR_TIPO ) ) {
                    if (termino_prima.t.equals ( VACIO ) ) {
                        termino.t = factor.t;
                    } else if ( factor.t.equals ( termino_prima.t ) ) {
                        termino.t = factor.t;
                    } else if ( ! factor.t.equals ( "string" ) ) {
                        termino.t = "single";
                    } else {
                        termino.t = ERROR_TIPO;
                        cmp.me.error(Compilador.ERR_SEMANTICO, 
                                "[termino] Error, los tipos de dato no son compatibles.");
                    }
                }
            } else {
                termino.t = ERROR_TIPO;
                cmp.me.error(Compilador.ERR_SEMANTICO, 
                        "[termino] Error, Error, los tipos de dato no son compatibles.");
            }
            // Fin accion semantica 41
            
        } else {
            error ( "[termino] Se esperaba un termino. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(termino') = {opmult, empty}
    
    private void termino_prima ( Atributos termino_prima ) {
        
        Atributos factor = new Atributos ();
        Atributos termino_prima1 = new Atributos ();
        
        if ( preAnalisis.equals ( "opmult" ) ) {
            // termino' -> opmult factor termino' {42}
            emparejar ( "opmult" );
            factor ( factor );
            termino_prima ( termino_prima1 );
            
            // Accion semantica 42
            if ( analizarSemantica ) {
                if ( ! factor.t.equals ( ERROR_TIPO ) && ! termino_prima1.t.equals ( ERROR_TIPO ) ) {
                    if (termino_prima1.t.equals ( VACIO ) ) {
                        termino_prima.t = factor.t;
                    } else if ( factor.t.equals ( termino_prima1.t ) ) 
                        termino_prima.t = factor.t;
                    else if ( ! factor.t.equals ( "string" ) ) {
                        termino_prima.t = "single";
                    } else {
                        termino_prima.t = ERROR_TIPO;
                        cmp.me.error(Compilador.ERR_SEMANTICO, 
                                "[termino'] Error, los tipos de dato no son compatibles.");
                    }
                } else {
                    termino_prima.t = ERROR_TIPO;
                    cmp.me.error(Compilador.ERR_SEMANTICO, 
                            "[termino'] Error, los tipos de dato no son compatibles.");
                }
            }
            // Fin accion semantica 42
            
        } else {
            // termino' -> empty {43}
            
            // Accion semantica 43
            if ( analizarSemantica ) 
                termino_prima.t = VACIO;
            // Fin accion semantica 43 
            
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(factor) = {id, num, num.num, (}
    
    private void factor ( Atributos factor ) {
        
        Linea_BE id = new Linea_BE ();
        Linea_BE num = new Linea_BE ();
        Linea_BE num_num = new Linea_BE ();
        Atributos factor_prima = new Atributos ();
        Atributos expresion = new Atributos ();
        
        if ( preAnalisis.equals ( "id" ) ) {
            // factor -> id factor' {44}
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            factor_prima ( factor_prima );
            
            // Accion semantica 44
            if ( analizarSemantica ) {
                if ( ! factor_prima.t.equals ( ERROR_TIPO ) ) {
                    if ( factor_prima.t.equals ( VACIO ) )
                        if ( !cmp.ts.buscaTipo ( id.entrada ).equals ( NIL ) )
                            factor.t = cmp.ts.buscaTipo ( id.entrada );
                        else {
                            factor.t = ERROR_TIPO;
                            cmp.me.error( Compilador.ERR_SEMANTICO, 
                                    "[factor] Error, identificador sin ningun tipo de dato : " + id.lexema );
                        } 
                    else {
                        String expresion_tipo = factor_prima.t + "integer";
                        if ( Pattern.matches ( expresion_tipo, cmp.ts.buscaTipo ( id.entrada ) ) )
                            factor.t = "integer";
                        else {
                            expresion_tipo = factor_prima.t + "single";
                            if ( Pattern.matches ( expresion_tipo, cmp.ts.buscaTipo ( id.entrada ) ) )
                                factor.t = "single";
                            else {
                                expresion_tipo = factor_prima.t + "string";
                                if ( Pattern.matches ( expresion_tipo, cmp.ts.buscaTipo ( id.entrada ) ) )
                                    factor.t = "string";
                                else {
                                    factor.t = ERROR_TIPO;
                                    cmp.me.error( Compilador.ERR_SEMANTICO, 
                                            "[factor] Error, la funcion no existe y además no se reconoce el tipo de dato");
                                }
                            }
                        }
                    }  
                } else {
                    factor.t = ERROR_TIPO;
                    cmp.me.error( Compilador.ERR_SEMANTICO, "[factor] Error, el tipo de dato no se reconoce, se aceptan "
                            + "solamente de tipos integer, single y string." );
                }
            }
            // Fin accion semantica 44
            
        } else if ( preAnalisis.equals ( "num" ) ) {
            // factor -> num {37}
            num = cmp.be.preAnalisis;
            emparejar ( "num" );
            
            // Accion semantica 37
            if ( analizarSemantica )
                factor.t = cmp.ts.buscaTipo( num.entrada );
            // Fin accion semantica 37
            
        } else if ( preAnalisis.equals ( "num.num" ) ) {
            // factor -> num.num {36}
            num_num = cmp.be.preAnalisis;
            emparejar ( "num.num" );
            
            // Accion semantica 36
            if ( analizarSemantica )
                factor.t = cmp.ts.buscaTipo( num_num.entrada );
            // Fin accion semantica 36
            
        } else if ( preAnalisis.equals ( "(" ) ){
            // factor -> ( expresion ) {47}
            emparejar ( "(" );
            expresion ( expresion );
            emparejar ( ")" );
            
            // Accion semantica 47
            if ( analizarSemantica ){
                if ( ! expresion.t.equals( ERROR_TIPO ) && 
                     ! expresion.t.equals( "string" ) )
                    factor.t = expresion.t;
                else {
                    factor.t = ERROR_TIPO;
                    cmp.me.error(Compilador.ERR_SEMANTICO, 
                            "[factor] Error el tipo de dato no es compatible");
                }
            }
            // Fin accion semantica 47
            
        } else {
            error ( "[factor] Se esperaba el nombre de un identificador, "
                    + "una constante numérica, o paréntesis. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea);
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(factor') = {(, empty}
    
    private void factor_prima ( Atributos factor_prima ) {
        
        Atributos lista_expresiones = new Atributos ();
        
        if ( preAnalisis.equals ( "(" ) ) {
            // factor' -> ( lista_expresiones ) {49}
            emparejar ( "(" );
            lista_expresiones ( lista_expresiones );
            emparejar ( ")" );
            
            // Accion semantica 49
            if ( analizarSemantica )
                factor_prima.t = lista_expresiones.t;
            // Fin accion semantica 49
            
        } else {
            // factor' -> empty {48}
            
            // Accion semantica 48
            if ( analizarSemantica ) 
                factor_prima.t = VACIO;
            // Fin accion semantica 48     
        }
    }
}

//------------------------------------------------------------------------------
//::