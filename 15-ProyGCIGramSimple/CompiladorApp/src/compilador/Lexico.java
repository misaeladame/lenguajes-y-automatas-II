/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Alex
 */
public class Lexico {
    final int TOKREC = 15;
   final int MAXTOKENS = 500;
   private String[] _lexemas;
   private String[] _tokens;
   private String _lexema;
   private int _noTokens;
   private int _i;
   private int _iniToken;
   private Automata oAFD;
   private Compilador cmp;

   public Lexico ( Compilador cmp )  // constructor por defecto
   {
     this.cmp = cmp;   
     _lexemas = new String[MAXTOKENS];
     _tokens = new String[MAXTOKENS];
     oAFD = new Automata();
     _i = 0;
     _iniToken = 0;
     _noTokens = 0;
   }

   public void Inicia() 
   {
     _i = 0;
     _iniToken = 0;
     _noTokens = 0;
     _lexemas = new String[MAXTOKENS];
     _tokens = new String[MAXTOKENS];
   }

   public void Analiza(String texto) 
   {
     Boolean recAuto;
     int noAuto;
     while (_i < texto.length()) 
     {
       recAuto=false;
       noAuto=0;
       for(;noAuto<TOKREC&&!recAuto;)
         if(oAFD.Reconoce(texto,this,noAuto))
           recAuto=true;
         else
           noAuto++;
       if (recAuto)
       {
         _lexema = texto.substring(_iniToken, _i);
         switch (noAuto)
         {     
           //--------------  Automata  Delimita--------------
          case 0 : //_tokens[_noTokens] = "Delimita";
                        break;
           //--------------  Automata  Opmult--------------
          case 1 : _tokens[_noTokens] = "oparit";
                        break;
           //--------------  Automata  Opsuma--------------
          case 2 : _tokens[_noTokens] = "oparit";
                        break;
           //--------------  Automata  Identi--------------
          case 3 : _tokens[_noTokens] = "id";
              break;
           //--------------  Automata  Literal--------------
          case 4 : _tokens[_noTokens] = "literal";
                        break;
           //--------------  Automata  Signo--------------
          case 5 : _tokens[_noTokens] = "oparit";
                        break;
           //--------------  Automata  Opasigna--------------
          case 6 : _tokens[_noTokens] = ":=";
                        break;
           //--------------  Automata  Reales1--------------
          case 7 : _tokens[_noTokens] = "num.num";//"Reales1";
                        break;
           //--------------  Automata  Reales2--------------
          case 8 : _tokens[_noTokens] = "num.num";//"Reales2";
                        break;
           //--------------  Automata  Reales3--------------
          case 9 : _tokens[_noTokens] = "num.num";//"Reales3";
                        break;
           //--------------  Automata  Enteros--------------
          case 10 : _tokens[_noTokens] = "num";//"Enteros";
                        break;
           //--------------  Automata  Oprel--------------
          case 11 : _tokens[_noTokens] = "oprel";
                        break;
           //--------------  Automata  Oprel2--------------
          case 12 : _tokens[_noTokens] = "oprel";
                        break;
           //--------------  Automata  CarEsp--------------
          case 13 : _tokens[_noTokens] = "caresp";
                        break;
           //--------------  Automata  Punto--------------
          case 14 : _tokens[_noTokens] = "punto";
                        break;
         }
         if(noAuto != 0)
             _lexemas[_noTokens++] = _lexema;
       }
       else
         _i++;
       _iniToken = _i;
     }
     //Activar metodo pasarBufferEntrada
     pasarBufferTabla ( );
     
   } // fin del método Analiza()

   public int getI()
   {
       return _i;
   }

   public void setI(int valor)
   {
       _i=valor;
   }

   public int getIniToken()
   {
       return _iniToken;
   }

   public String[] Tokens()
   {
       return _tokens;
   }

   public String[] Lexemas()
   {
       return _lexemas;
   }
   
   public int NOTOKENS()
   {
       return _noTokens;
   }
   
   private Boolean EsPalabraReservada(String lex)
   {      
       String palres[] = {"program","var","array","of","integer","real","function",
           "procedure","begin","end","if","then","else","while","do","not","caracter","entero",
       "real", "arreglo","inicio", "fin"};
       for (int i = 0; i < palres.length; i++) {
           if (lex.equals ( palres[i] ) ) {       
               return true; 
           }
       }
       return false;
   }    
   
   
//--------------------------------------------------------------------------
	// Toma los tokens y los pasa a la tabla de simbolos y buffer de entrada
	// Revision en 22/Nov/2012
    private void pasarBufferTabla ( )
    {
        // Comenzamos con establecer la entrada, la l?nea y una bandera para
        // palabras reservadas
        int entrada = 0;
        Linea_BE lineaBE = null;
        Linea_TS lineaTS = null;

        Boolean noPalres = true;

        //tabla de simbolos, linea reservada
        lineaTS = new Linea_TS ( "", "", "", "");
        entrada = cmp.ts.insertar ( lineaTS );
        lineaTS = null;

        //Vamos a comparar todos los tokens obtenidos e insertar en la tabla
        //de s?mbolos
        for ( int i = 0; i < _noTokens; i++ )
        {
            //Comparando el identificador que no sea palabra reservada
            if ( _tokens[ i ].compareTo ( "id" ) == 0 )
            {
                if(EsPalabraReservada(_lexemas[i])){
                    lineaBE = new Linea_BE (
               _lexemas [ i ], _lexemas [ i ], 0 );
                }
                else {
               lineaTS = new Linea_TS (
               _tokens [ i ], _lexemas [ i ], "", "" );
               entrada = cmp.ts.insertar ( lineaTS );
               lineaBE = new Linea_BE (
               _tokens [ i ], _lexemas [ i ], entrada );
                }
               
            }                 
            //Variables que deja pasar a tabla de simbolos
            else if ( _tokens [ i ].compareTo( "num" ) == 0 ||
                      _tokens [ i ].compareTo( "num.num" ) == 0 ||
                      _tokens [ i ].compareTo( "literal" ) == 0 )
            {
                lineaTS = new Linea_TS (
                           _tokens [ i ], _lexemas [ i ], "", "" );
                entrada = cmp.ts.insertar ( lineaTS );
                lineaBE = new Linea_BE (
                        _tokens [ i ], _lexemas [ i ], entrada );
            }
            //Los siguientes no se insertan en tabla simbolos
            else if ( _tokens [ i ].compareTo( "opmult" ) == 0 
                    || _tokens [ i ].compareTo( "opsuma" )== 0 
                    || _tokens [ i ].compareTo( "signo") == 0 
                    || _tokens [ i ].compareTo(":=") == 0 
                    || _tokens [ i ].compareTo("oprel") == 0
                    || _tokens [ i ].compareTo("oparit") == 0 )
                lineaBE = new Linea_BE (
                        _tokens [ i ], _lexemas [ i ], 0 );
            else if ( _tokens [ i ].compareTo( "caresp" ) == 0 ||
                      _tokens [ i ].compareTo( "punto" ) == 0 )
                lineaBE = new Linea_BE (
                        _lexemas [ i ], _lexemas [ i ], 0 );		

            //Verificar que la línea no está vacía para agregar a la tabla
            if ( lineaBE != null )
                cmp.be.insertar ( lineaBE );
            

            //Limpiar lineas
            lineaBE = null; lineaTS = null;
        }

    }//Fin del metodo para pasar al buffer entrada y tabla simbolos
	//--------------------------------------------------------------------------   
}
