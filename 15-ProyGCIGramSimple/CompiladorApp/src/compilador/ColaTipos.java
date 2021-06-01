/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compilador;

/**
 *
 * @author REBECA
 */
public class ColaTipos {
    //Atributos
        private int primero;
        private int ultimo;
        private String[] _elems;
        private static final int MAX = 5000;

        //Constructor
        public ColaTipos( )
        {
            primero = ultimo = -1;
            _elems= new String[ MAX ];            
        }

        //Métodos de Cola
        public Boolean Empty()
        {
            if (primero == -1 && ultimo == -1)
                return true;
            else
                return false;
        }

        public Boolean Full()
        {
            if ((primero == 0 && ultimo == _elems.length - 1) || (primero == ultimo + 1))
                return true;
            else
                return false;
        }

        public void Input(String _tipoI)
        {
            if (!this.Full())
            {
                if (primero == -1 && ultimo == -1)
                {
                    primero = ultimo = 0;
                    _elems[primero] = _tipoI;
                }
                else
                    if (ultimo == _elems.length - 1)
                    {
                        ultimo = 0;
                        _elems[ultimo] = _tipoI;
                    }
                    else
                        _elems[++ultimo] = _tipoI;
            }                
        }

        public String Output()
        {
            String tipoF = new String();
            if (!this.Empty())
            {
                if (primero == ultimo)
                {
                    tipoF = _elems[primero];
                    primero = ultimo = -1;
                }

                else if (primero == _elems.length - 1)
                {
                    tipoF = _elems[primero];
                    primero = 0;
                }
                else
                    tipoF = _elems[primero++];
            }            
            return tipoF; 
        }

        public int GetLength()
        {
            if (ultimo == primero)
                return 1;
            else if (ultimo >= primero)
                return ultimo - primero + 1;
            else
                return _elems.length - primero + ultimo + 1;
        }
    
}
