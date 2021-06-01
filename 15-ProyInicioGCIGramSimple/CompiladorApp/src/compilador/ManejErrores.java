/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                         LENGUAJES Y AUTOMATAS II           
 *: 
 *:                  SEMESTRE: AGO-DIC/2013     HORA: 12-13 HRS
 *:                                   
 *:               
 *:    # Clase que implementa la funcionalidad del Manejador de Errores del Compilador.
 *                 
 *:                           
 *: Archivo       : ManejErrores.java
 *: Autor         : Fernando Gil  
 *: Fecha         : 03/Octubre/2013
 *: Compilador    : Java JDK 7
 *: Descripción   : Esta clase recibe la invocacion de alguno de sus metodos 
 *:                 error ()  y transfiere la llamada a la Interfaz de Usuario.
 *:                  
 *:           	     
 *: Ult.Modif.    :
 *:  Fecha      Modificó            Modificacion
 *:=============================================================================
 *:-----------------------------------------------------------------------------
 */



package compilador;

public class ManejErrores {

    private Compilador compilador;
    private int        totErrLexico      = 0;
    private int        totErrSintacticos = 0;
    private int        totErrSemanticos  = 0;
    private int        totErrCodInt      = 0;
    private int        totWarningsSem    = 0;
    
    //--------------------------------------------------------------------------
    
    public ManejErrores ( Compilador c ) {
        compilador = c;
    }

    //--------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------
    public void inicializar () {
        totErrLexico = totErrSintacticos = totErrSemanticos = totErrCodInt = 0;
        totWarningsSem = 0;
    }
    //--------------------------------------------------------------------------
    public int getTotErrLexico      () { return totErrLexico;      }
    //--------------------------------------------------------------------------
    public int getTotErrSintacticos () { return totErrSintacticos; }
    //--------------------------------------------------------------------------
    public int getTotErrSemanticos  () { return totErrSemanticos;  }
    //--------------------------------------------------------------------------
    public int getTotErrCodInt      () { return totErrCodInt;      }
    //--------------------------------------------------------------------------
    public int getTotWarningsSem    () { return totWarningsSem;    }
    //--------------------------------------------------------------------------
    
    public void error ( int tipoError, String errorMensaje ) {
      // Contabilizar el error
        switch  ( tipoError ) {
           case Compilador.ERR_LEXICO      : totErrLexico++; 
                                             break;
           case Compilador.ERR_SINTACTICO  : totErrSintacticos++;
                                             break;
           case Compilador.ERR_SEMANTICO   : totErrSemanticos++;
                                             break;
           case Compilador.ERR_CODINT      : totErrCodInt++;
                                             break;
           case Compilador.WARNING_SEMANT  : totWarningsSem++;
                                             break;
		}

        if ( tipoError == Compilador.WARNING_SEMANT )
            // Manejo de Warnings
            compilador.erroresListener.mostrarWarning ( errorMensaje );
        else
            // Invocar el despliegue del error a la GUI
            compilador.erroresListener.mostrarErrores ( errorMensaje );
    }
    
    //--------------------------------------------------------------------------
}
