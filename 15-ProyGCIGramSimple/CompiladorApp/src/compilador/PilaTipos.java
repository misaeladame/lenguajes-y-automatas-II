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
public class PilaTipos {
    private static final int MAX = 5000;
    String[] _elems;
    int _tope;

    public PilaTipos()
    {
        _elems=new String[MAX];
        /*for (int i = 0; i < _elems.length; i++)
            _elems[i] = new Atributos();*/
        _tope = 0;
    }
    public Boolean Empty()
    {
        return _tope==0? true : false;
    }
    public Boolean Full()
    {
        return _tope==_elems.length? true : false;
    }
    public void Push(String oElem)
    {
        _elems[_tope++]=oElem;
    }
    public int GetLenght()
    {
        return _tope;
    }
    public String Pop()
    {
        return _elems[--_tope];
    }

    public void Inicia() 
    {
        _tope = 0;
    }

    /*public SimbGram Tope 
    {
        get { return _elems[_tope - 1];}
    } */
}
