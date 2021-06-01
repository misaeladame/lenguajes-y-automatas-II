/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                         LENGUAJES Y AUTOMATAS II           
 *: 
 *:                  SEMESTRE: AGO-DIC/2013     HORA: 12-13 HRS
 *:                                   
 *:               
 *:    # Clase con la funcionalidad del Generador de COdigo Intermedio
 *                 
 *:                           
 *: Archivo       : GenCodigoInt.java
 *: Autor         : Fernando Gil  
 *: Fecha         : 03/Octubre/2013
 *: Compilador    : Java JDK 7
 *: Descripción   :  
 *:                  
 *:           	     
 *: Ult.Modif.    :
 *:  Fecha      Modificó            Modificacion
 *:=============================================================================
 *:                                 P -> V C
 *:                                 V -> id : T  V | empty
 *:                                 T -> caracter | entero | real 
 *:                                 C -> inicio S fin
 *:                                 S -> id := E  S  |  empty
 *:                                 E -> num  E' | num.num  E' |  id  E'
 *:                                 E'-> oparit E  |  empty 
 *:-----------------------------------------------------------------------------
 */

package compilador;


public class GenCodigoInt {
 
    private Compilador cmp;

    
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
        P ();
    }    

    //--------------------------------------------------------------------------

    private void emite ( String c3d ) {
        cmp.erroresListener.mostrarCodInt ( c3d );
    }
    
    //--------------------------------------------------------------------------
    
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
            //V -> id : T V 
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
            S ();
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
                //S -> id := E  S
                emparejar ( "id" );
                emparejar ( ":=" );
                E ( );
                S();
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
