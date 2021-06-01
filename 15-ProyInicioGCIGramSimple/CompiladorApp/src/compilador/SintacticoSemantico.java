/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                         LENGUAJES Y AUTOMATAS II           
 *: 
 *:                  SEMESTRE: ENE-JUN/2014     HORA: 10-11 HRS
 *:                                   
 *:               
 *:         Clase con la funcionalidad del Analizador Sintactico
 *                 
 *:                           
 *: Archivo       : SintacticoSemantico.java
 *: Autor         : Fernando Gil  ( Estructura general de la clase  )
 *:                 Grupo de Lenguajes y Automatas II ( Procedures  )
 *: Fecha         : 14/Febrero/2014
 *: Compilador    : Java JDK 7
 *: Descripción   : Esta clase implementa un parser descendente del tipo 
 *:                 Predictivo Recursivo. Se forma por un metodo por cada simbolo
 *:                 No-Terminal de la gramatica mas el metodo emparejar ().
 *:                 El analisis empieza invocando al metodo del simbolo inicial.
 *: Ult.Modif.    :
 *:  Fecha      Modificó            Modificacion
 *:=============================================================================
 *:                                 P -> V C
 *:                                 V -> id : T  V | empty
 *:                                 T -> caracter | entero | real 
 *:                                 C -> inicio S fin
 *:                                 S -> id := E 
 *:                                 E -> num  E' | num.num  E' |  id  E'
 *:                                 E'-> oparit E  |  empty 
 *:-----------------------------------------------------------------------------
 */


package compilador;

import javax.swing.JOptionPane;

public class SintacticoSemantico {
    

    private Compilador cmp;
    private boolean    analizarSemantica = false;
   
    //--------------------------------------------------------------------------
    // Constructor de la clase, recibe la referencia de la clase principal del 
    // compilador.
    //
	public SintacticoSemantico ( Compilador c ) {
        cmp = c;
    }
    // Fin del Constructor
	//--------------------------------------------------------------------------
	
    //--------------------------------------------------------------------------
    // Metodo que inicia la ejecucion del analisis sintactico predictivo.
    // analizarSemantica : true = realiza el analisis semantico a la par del sintactico
    //                     false= realiza solo el analisis sintactico sin comprobacion semantica
	//
    public void analizar ( boolean analizarSemantica ) {
        this.analizarSemantica = analizarSemantica;

        // ***   INVOCAR AQUI EL PROCEDURE DEL SIMBOLO INICIAL   * * * 
            P () ; 
    }
    // Fin de analizar
    //--------------------------------------------------------------------------

    //  * * * *   PEGAR AQUI LOS PROCEDURES    * * * * 
    private void P()
    {
        if ( cmp.be.preAnalisis.complex.equals ( "id"     ) ||
             cmp.be.preAnalisis.complex.equals ( "inicio" )     )
        {
            //P -> V C 
            V ( ) ;
            C ( ) ;

        }
        else {                    
            error("Error de P");
        }
    }
//-------------------------------------------------------------------
private void V( ) 
 {
        if ( cmp.be.preAnalisis.complex.equals ( "id" ) ) {
            //V -> id : T V {2}
            emparejar ( "id" ) ;
            emparejar ( ":" ) ;
            T ( ) ;
            V ( ) ;
        }
        else {
            //V -> EMPTY
        }
 }
//------------------------------------------------------------------------
private void T () {

        if ( cmp.be.preAnalisis.complex.equals ( "caracter" ) ){
         //T -> caracter
            emparejar ( "caracter" ) ;
	}
        else if (cmp.be.preAnalisis.complex.equals ( "entero" ) ){
            //T -> entero
            emparejar ( "entero" ) ;
	}
        else if ( cmp.be.preAnalisis.complex.equals ( "real" ) ){
            //T -> real
            emparejar( "real" );
	}
        else{
            error("Error de T");
         }
}  
private void C()
{
	if (cmp.be.preAnalisis.complex.equals ( "inicio" ) ) {
            //C -> inicio S fin
            emparejar ( "inicio" ) ;
            emparejar ( "fin" );
        }
        else{
            error("Error de C");
         }
}
//________________________________________________________________________
private void S()
{
	if (cmp.be.preAnalisis.complex.equals ( "id" ) ) {
                //S -> id := E
                emparejar ( "id" );
                emparejar ( ":=" );
                E();
            }
          else {
                //S -> EMPTY
            }
}
//------------------------------------------------------------------------
private void E()
{
	if( cmp.be.preAnalisis.complex.equals ( "num" ) ){
             //E -> num E'
             emparejar ( "num" ) ;
             Ep ();             
	}
         else if ( cmp.be.preAnalisis.complex.equals ( "num.num" ) ){
             //E -> num.num  E'
             emparejar ( "num.num" ) ;
             Ep ();             
	}
         else if ( cmp.be.preAnalisis.complex.equals ( "id" ) ){
             //E -> id E'
             emparejar ( "id" ) ;
             Ep ();
	}         
         else{
            error("Error de E");
         }
}

//------------------------------------------------------------------------

private void Ep () {
    if ( cmp.be.preAnalisis.equals ( "oparit" ) ) {
        // E' -> oparit E
        emparejar ( "oparit" );
        E ();
    } else {
        // E' -> empty
    }
}
     
//------------------------------------------------------------------------

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
	
    //--------------------------------------------------------------------------
    // Metodo para mostrar un error sintactico
 
    private void error ( String _token ) {
        cmp.me.error ( cmp.ERR_SINTACTICO,
         "ERROR SINTACTICO: en la produccion del simbolo  " + _token );
    }
 
    // Fin de error
    //--------------------------------------------------------------------------
}