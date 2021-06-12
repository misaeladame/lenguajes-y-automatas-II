/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                         LENGUAJES Y AUTOMATAS II           
 *: 
 *:        SEMESTRE: ______________            HORA: ______________ HRS
 *:                                   
 *:               
 *:    # Clase con la funcionalidad del Generador de COdigo Intermedio
 *                 
 *:                           
 *: Archivo       : GenCodigoInt.java
 *: Autor         : Fernando Gil  
 *: Fecha         : 03/SEP/2014
 *: Compilador    : Java JDK 7
 *: Descripción   :  
 *:                  
 *:           	     
 *: Ult.Modif.    :
 *:  Fecha      Modificó                     Modificacion
 *:=============================================================================
 *: 11/Jun/2021  18131209 Misael Adame       Se agregaron 20 acciones semanticas
 *:              18130588 Cristian Piña      para la generación del codigo
 *:              17130772 Sergio Chairez     intermedio
 *:-----------------------------------------------------------------------------
 */


package compilador;


public class GenCodigoInt {
    public static final int NIL = 0;
            
    private Compilador cmp;
    private int        p;
    private int        consecTemp;  // Consecutivo de variables temporales nuevos
    private int        consecEtiq;  // Consecutivo de etiquetas nuevas
    
    /*
    private boolean    analizarSemantica = false;
    private String     preAnalisis;
    */
    
    //--------------------------------------------------------------------------
    // Constructor de la clase, recibe la referencia de la clase principal del 
    // compilador.
    //
	public GenCodigoInt ( Compilador c ) {
            cmp = c;
    }
    // Fin del Constructor
    //--------------------------------------------------------------------------
	
    public void generar () {
        consecTemp = 1;
        consecEtiq = 0;
        programa();
    }

    //--------------------------------------------------------------------------
     
    private void emite ( String c3d ) {
        cmp.erroresListener.mostrarCodInt(c3d);
    }
    
    private String tempnuevo () {
        return "t" + consecTemp++;
    }
    
    //--------------------------------------------------------------------------
    
    private String etiqnueva () {
        return "etiq" + consecEtiq++;
    }
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Metodo que inicia la ejecucion del analisis sintactico predictivo.
    // analizarSemantica : true = realiza el analisis semantico a la par del sintactico
    //                     false= realiza solo el analisis sintactico sin comprobacion semantica
    
    /*
    public void analizar ( boolean analizarSemantica ) {
        this.analizarSemantica = analizarSemantica;
        preAnalisis = cmp.be.preAnalisis.complex;

        // * * *   INVOCAR AQUI EL PROCEDURE DEL SIMBOLO INICIAL   * * *7
        programa ();
    }
    */
    

    //--------------------------------------------------------------------------

    //************EMPAREJAR**************//
    private void emparejar ( String t ) {
	if (cmp.be.preAnalisis.complex.equals ( t ) )
		cmp.be.siguiente ();
	else
		errorEmparejar ( "Se esperaba " + t + " se encontró " +
                                 cmp.be.preAnalisis.lexema );
    }	
	
    
    //--------------------------------------------------------------------------
    // Metodo para devolver un error al emparejar
    //--------------------------------------------------------------------------
 
    private void errorEmparejar ( String _token ) {
        String msjError = "ERROR SINTACTICO: ";
              
        if ( _token.equals ( "id" ) )
            msjError += "Se esperaba un identificador" ;
        else if ( _token.equals ( "num" ) )
            msjError += "Se esperaba una constante entera" ;
        else if ( _token.equals ( "num.num" ) )
            msjError += "Se esperaba una constante real";
        else if ( _token.equals ( "literal" ) )
            msjError += "Se esperaba una literal";
        else if ( _token.equals ( "oparit" ) )
            msjError += "Se esperaba un Operador Aritmetico";
        else if ( _token.equals ( "oprel" ) )
            msjError += "Se esperaba un Operador Relacional";
        else 
            msjError += "Se esperaba " + _token;
                
        cmp.me.error ( Compilador.ERR_SINTACTICO, msjError );    
    } 

    // Fin de ErrorEmparejar
    //--------------------------------------------------------------------------
    // Metodo para mostrar un error sintactico

    private void error ( String _token ) {
        cmp.me.error ( cmp.ERR_SINTACTICO,
         "ERROR SINTACTICO: en la produccion del simbolo  " + _token );
    }
 
    // Fin de error
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // * * *   AQUI EMPIEZA  EL CODIGO DE LOS PROCEDURES    * * *
    
    // Autor: Cristian Gabriel Piña Rosales 18130588
    // PRIMEROS(programa) = {PRIMEROS(declaraciones), PRIMEROS(declaracion_subprogramas), 
    //                       PRIMEROS(proposiciones_optativas), end}
    
    private void programa () { 
        if ( cmp.be.preAnalisis.complex.equals ( "dim" ) || cmp.be.preAnalisis.complex.equals ( "function" ) ||
             cmp.be.preAnalisis.complex.equals ( "sub" ) || cmp.be.preAnalisis.complex.equals ( "id" )  || 
             cmp.be.preAnalisis.complex.equals ( "call" ) || cmp.be.preAnalisis.complex.equals ( "if" )  || 
             cmp.be.preAnalisis.complex.equals ( "do" ) || cmp.be.preAnalisis.complex.equals ( "end" ) ) {
            /* programa -> declaraciones
                           declaraciones_subprogramas
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
        if ( cmp.be.preAnalisis.complex.equals ( "dim" ) ) {
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
        if ( cmp.be.preAnalisis.complex.equals ( "id" ) ) {
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
        if ( cmp.be.preAnalisis.complex.equals ( "," ) ) {
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
        if ( cmp.be.preAnalisis.complex.equals ( "integer" ) ) {
            // tipo -> integer
            emparejar ( "integer" );
        } else if ( cmp.be.preAnalisis.complex.equals ( "single" ) ) {
            // tipo -> single
            emparejar ( "single" );
        } else if ( cmp.be.preAnalisis.complex.equals ( "string" ) ) {
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
        if ( cmp.be.preAnalisis.complex.equals ( "function" ) || cmp.be.preAnalisis.complex.equals ( "sub" ) ) {
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
        if ( cmp.be.preAnalisis.complex.equals ( "function" ) ) {
            //declaracion_subprograma -> declaracion_funcion
            declaracion_funcion ();
        } else if ( cmp.be.preAnalisis.complex.equals ( "sub" ) ) {
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
    // PRIMEROS(declaracion_funcion) = {function}
    
    private void declaracion_funcion () {
        if ( cmp.be.preAnalisis.complex.equals ( "function" ) ) {
            /* declaracion_funcion -> function id argumentos as tipo 
                                      proposiciones_optativas end function */
            emparejar ( "function" );
            emparejar ( "id" );
            argumentos ();
            emparejar ( "as" );
            tipo ();
            proposiciones_optativas ();
            emparejar ( "end" );
            emparejar ( "function" );
        } else {
            error ( "[declaracion_funcion] Se esperaba una funcion. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(declaracion_subrutina) = {sub}
    
    private void declaracion_subrutina () {
        if ( cmp.be.preAnalisis.complex.equals ( "sub" ) ) {
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
            error ( "[declaracion_subrutina] Se esperaba una subrutina valida. "
                   + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(argumentos) = {(, empty}
    
    private void argumentos () {
        if ( cmp.be.preAnalisis.complex.equals ( "(" ) ) {
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
        Atributos proposicion = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "id" ) || cmp.be.preAnalisis.complex.equals ( "call" ) ||
             cmp.be.preAnalisis.complex.equals ( "if" ) || cmp.be.preAnalisis.complex.equals ( "do" ))  {
            // proposiciones_optativas -> proposicion proposiciones_optativas
            proposicion ( proposicion );
            proposiciones_optativas ();
        } /* else {
            // proposiciones_optativias -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(proposicion) = {id, call, if, do}
    
    private void proposicion (Atributos proposicion) {
        Atributos condicion = new Atributos();
        Atributos expresion = new Atributos();
        Linea_BE id = new Linea_BE();
                
        if ( cmp.be.preAnalisis.complex.equals ( "id" ) ) {
            // proposicion -> id opasig expresion
            id = cmp.be.preAnalisis;
            emparejar ( "id" );
            emparejar ( "opasig" );
            expresion ( expresion );
            
            // Accion semantica 13
            p = cmp.ts.buscar ( id.lexema );
            if ( p != NIL ) {
                emite ( "[" + p + "]" + ":=" + expresion.Lugar );
            } else {
                cmp.me.error ( Compilador.ERR_CODINT, 
                               "[proposicion] No se encontro el lexema " +id.lexema +
                               " en la Tabla de Simbolos");
            }
            //Fin de accion semantica
            
        } else if ( cmp.be.preAnalisis.complex.equals( "call" ) ) {
            // proposicion -> call id proposicion'
            emparejar ( "call" );
            emparejar ( "id" );
            proposicion_prima ();
            
        } else if ( cmp.be.preAnalisis.complex.equals ( "if" ) ) {
            // proposicion -> if condicion then proposicicones_optativas else 
            //                proposiciones_optativas end if
            emparejar ( "if" );
            condicion ( condicion );
            
            //Accion semantica 14
            proposicion.cierta = etiqnueva();
            proposicion.falsa = etiqnueva();
            proposicion.siguiente = etiqnueva();
            emite ( "if " + condicion.cdc + condicion.op + condicion.cdc_ 
                    + " goto " + proposicion.cierta + " goto " 
                    + proposicion.falsa );
            emite ( proposicion.cierta + ":");
            //Fin de accion semantica
            
            emparejar ( "then" );
            proposiciones_optativas ();
            
            //Accion semantica 15
            emite ( "goto " + proposicion.siguiente);
            //Fin de accion semantica
            
            emparejar ( "else" );
            
            //Accion semantica 16
            emite(proposicion.falsa + ":");
            //Fin de accion semantica
            
            proposiciones_optativas ();
            
            //Accion semantica 17
            emite ( proposicion.siguiente + ":" );
            //Fin de accion semantica
            
            emparejar ( "end" );
            emparejar ( "if" );
            
            
        } else if ( cmp.be.preAnalisis.complex.equals ( "do" ) ) {
            // proposicion -> do while condicion proposiciones_optativas loop
            
            //Accion semantica 18
            proposicion.comienzo = etiqnueva();
            proposicion.cierta = etiqnueva();
            proposicion.falsa = etiqnueva();
            //fin de accion semantica
            
            emparejar ( "do" );
            emparejar ( "while" );
            condicion ( condicion );
            
            //Accion semantica 19
            emite ( proposicion.comienzo + ": if " + condicion.cdc 
                    + condicion.op + condicion.cdc_ + " goto " 
                    + proposicion.cierta 
                    + "\n goto " + proposicion.falsa);
            emite(proposicion.cierta + ":");
            //Fin de accion semantica
            
            proposiciones_optativas ();
            
            //Accion semantica 20
            emite ( " goto " + proposicion.comienzo );
            emite ( proposicion.falsa + ":" );
            //Fin de accion semantica
            
            emparejar ( "loop" );
        } else {
            error ( "[proposicion] Se esperaba una proposicion valida. "
                   + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Jose Misael Adame Sandoval 18131209
    // PRIMEROS(proposicion’) = {(, empty}
    
    private void proposicion_prima () {
        if ( cmp.be.preAnalisis.complex.equals ( "(" ) ) {
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
        Atributos expresion = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "id" )  || cmp.be.preAnalisis.complex.equals ( "num" ) || 
             cmp.be.preAnalisis.complex.equals ( "num.num" ) || cmp.be.preAnalisis.complex.equals ( "(" ) || 
             cmp.be.preAnalisis.complex.equals ( "literal" ) ) {
            // lista_expresiones -> expresion lista_expresiones' 
            expresion ( expresion );
            lista_expresiones_prima ();
        } /* else {
            // lista_expresiones -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(lista_expresiones') = {,, empty}
    
     private void lista_expresiones_prima () {
         Atributos expresion = new Atributos();
         
        if ( cmp.be.preAnalisis.complex.equals ( "," ) ) {
            /*lista_expresiones' -> , expresion lista_expresiones' */
            emparejar ( "," );
            expresion ( expresion );
            lista_expresiones_prima ();
        } /* else { 
            // lista_expresiones' -> empty
        } */
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(condicion) = {PRIMEROS(expresion)}
    
    private void condicion (Atributos condicion) {
        Atributos expresion = new Atributos();
        Linea_BE oprel = new Linea_BE();
        Atributos expresion1 = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "id" )  || cmp.be.preAnalisis.complex.equals ( "num" ) || 
             cmp.be.preAnalisis.complex.equals ( "num.num" ) || cmp.be.preAnalisis.complex.equals ( "(" ) || 
             cmp.be.preAnalisis.complex.equals ( "literal" ) ) {
            /* condicion -> expresion oprel expresion */
            expresion (expresion);
            oprel = cmp.be.preAnalisis;
            emparejar ( "oprel" );
            expresion (expresion1);
            
            //Accion semantica 12
            condicion.cdc = expresion.Lugar;
            condicion.op = oprel.lexema;
            condicion.cdc_ = expresion1.Lugar;
            //Fin de accion semantica
            
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
    
    private void expresion (Atributos expresion) {
        Atributos termino = new Atributos();
        Atributos expresion_prima = new Atributos();
        Linea_BE literal = new Linea_BE();
        
        if ( cmp.be.preAnalisis.complex.equals ( "id" ) || cmp.be.preAnalisis.complex.equals ( "num" ) ||
             cmp.be.preAnalisis.complex.equals ( "num.num" ) || cmp.be.preAnalisis.complex.equals ( "(" ) ) {
            /* expresion -> termino expresion' */
            termino ( termino );
            expresion_prima ( expresion_prima );
            
            // Acción semantica 5
            if ( ! ( expresion_prima.Lugar ).equals ("") ) {
                expresion.Lugar = tempnuevo();
                emite ( expresion.Lugar + ":=" + expresion_prima.op 
                        + termino.Lugar + expresion_prima.Lugar );
            } else {
                expresion.Lugar = termino.Lugar;
            }
            // Fin de accion semantica
        } else if ( cmp.be.preAnalisis.complex.equals ( "literal" ) ){
            // expresion -> literal
            literal = cmp.be.preAnalisis;
            emparejar ( "literal" );
            // Acción semantica 6
            expresion.Lugar = "[" + literal.entrada + "]";
            // Fin de accion semantica
        } else {
            error ( "[expresion] Se esperaba el nombre de un identificador, "
                    + "una constante numérica, o paréntesis o una literal. " 
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(expresion') = {opsuma, empty}
    
    private void expresion_prima (Atributos expresion_prima) {
        Atributos termino = new Atributos ();
        Atributos expresion_prima1 = new Atributos ();
        Linea_BE opsuma = new Linea_BE ();
        
        if ( cmp.be.preAnalisis.complex.equals ( "opsuma" ) ) {
            /* expresion' -> opsuma termino expresiom' */
            opsuma = cmp.be.preAnalisis;
            emparejar ( "opsuma" );
            termino (termino);
            expresion_prima(expresion_prima1);
            // Acción semantica 7
            if ( ! ( expresion_prima1.Lugar ).equals ("") ) {
                expresion_prima.Lugar = tempnuevo();
                expresion_prima.op = opsuma.lexema;
                emite ( expresion_prima.Lugar + ":=" + expresion_prima1.op 
                        + termino.Lugar + expresion_prima1.Lugar );
            } else {
                expresion_prima.Lugar = termino.Lugar;
                expresion_prima.op = opsuma.lexema;
            }
            // Fin de accion semantica
            
        } else {
            // Acción semantica 8
            expresion_prima.Lugar = "";
            // Fin de acción semantica
        }
        
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(termino) = {PRIMEROS(factor)}
    
    private void termino (Atributos termino) {
        Atributos factor = new Atributos();
        Atributos termino_prima = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "id" ) || cmp.be.preAnalisis.complex.equals ( "num" ) ||
             cmp.be.preAnalisis.complex.equals ( "num.num" ) || cmp.be.preAnalisis.complex.equals ( "(" )) {
            // termino -> factor termino'
            factor (factor);
            termino_prima (termino_prima);
            // Acción semantica 9
            if ( ! ( termino_prima.Lugar ).equals ("") ){
                termino.Lugar = tempnuevo();
                emite ( termino.Lugar + ":=" + termino_prima.op 
                        + factor.Lugar + termino_prima.Lugar );
            } else {
                termino.Lugar = factor.Lugar;
            }
            // Fin de accion semantica
        } else {
            error ( "[termino] Se esperaba un termino. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(termino') = {opmult, empty}
    
    private void termino_prima (Atributos termino_prima) {
        Atributos factor = new Atributos();
        Atributos termino_prima1 = new Atributos();
        Linea_BE opmult = new Linea_BE();
        
        if ( cmp.be.preAnalisis.complex.equals ( "opmult" ) ) {
            /* termino' -> opmult factor termino' */
            opmult = cmp.be.preAnalisis;
            emparejar ( "opmult" );
            factor ( factor );
            termino_prima ( termino_prima1 );
            
            // Accion semantica 10
            if( ! ( termino_prima1.Lugar ).equals ("") ){
                termino_prima.Lugar = tempnuevo();
                termino_prima.op = opmult.lexema;
                emite ( termino_prima.Lugar + ":=" + termino_prima1.op 
                        + factor.Lugar + termino_prima1.Lugar );
            } else {
                termino_prima.Lugar = factor.Lugar;
                termino_prima.op = opmult.lexema;
            }
            // Fin de accion semantica
        } else{
            // Accion semantica 11
            termino_prima.Lugar = "";
            // Fin de accion semantica
        }
        
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(factor) = {id, num, num.num, (}
    
    private void factor (Atributos factor) {
        Linea_BE id = new Linea_BE();
        Linea_BE num = new Linea_BE();
        Linea_BE numnum = new Linea_BE();
        Atributos expresion = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "id" ) ) {
            // factor -> id factor'
            id = cmp.be.preAnalisis;  // Salvamos los atributos de id
            emparejar ( "id" );
            factor_prima ();
            
            // Acción semantica 1
            p = cmp.ts.buscar ( id.lexema );
            if ( p != NIL ) {
                factor.Lugar = "[" + p + "]";
            } else {
                cmp.me.error ( Compilador.ERR_CODINT, 
                               "[programa] No se encontro el lexema " +id.lexema +
                               " en la Tabla de Simbolos");
            }
            // Fin de acción semantica
        }
         else if ( cmp.be.preAnalisis.complex.equals ( "num" ) ) {
            // factor -> num
            num = cmp.be.preAnalisis;
            emparejar ( "num" );
            
            // Acción semantica 2
            factor.Lugar = "[" + num.entrada + "]";
            // Fin de acción semantica
        }
        else if ( cmp.be.preAnalisis.complex.equals ( "num.num" ) ) {
            // factor -> num.num
            numnum = cmp.be.preAnalisis;
            emparejar ( "num.num" );
            
            // Acción semantica 3
            factor.Lugar = "[" + numnum.entrada + "]";
            // Fin de acción semantica
        }
        else if ( cmp.be.preAnalisis.complex.equals ( "(" ) ){
            // factor -> ( expresion )
            emparejar ( "(" );
            expresion ( expresion );
            emparejar ( ")" );
            
            // Acción semantica 4
            factor.Lugar = expresion.Lugar;
            // Fin de acción semantica
        } else {
            error ( "[factor] Se esperaba el nombre de un identificador, "
                    + "una constante numérica, o paréntesis. "
                    + "No. de Linea " + cmp.be.preAnalisis.numLinea);
        }
    }
    
    //--------------------------------------------------------------------------
    // Autor: Sergio Alejandro Chairez Garcia 17130772
    // PRIMEROS(factor') = {(, empty}
    
    private void factor_prima () {
        if ( cmp.be.preAnalisis.complex.equals ( "(" ) ) {
            /* factor' -> ( lista_expresiones ) */
            emparejar ( "(" );
            lista_expresiones ();
            emparejar ( ")" );
        } /* else {
            // factor' -> empty
        } */
    }
}

//------------------------------------------------------------------------------
//::
    

