package compilador;

public class Pila {
    private static final int MAX = 5000;
    int[] _elems;
    int _tope;

    public Pila()
    {
        _elems=new int[MAX];
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
    public void Push(int _entrada)
    {
        _elems[_tope++]=_entrada;
    }
    public int GetLenght()
    {
        return _tope;
    }
    public int Pop()
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
