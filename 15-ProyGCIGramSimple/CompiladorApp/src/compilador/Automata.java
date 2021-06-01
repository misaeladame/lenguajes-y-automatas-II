/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Alex
 */
public class Automata {
    String _textoIma;
  int _edoAct;

  private char SigCar(int i) 
  {
    if (i == _textoIma.length())
      return '';
    else
      return _textoIma.charAt(i);
  }

  public Boolean Reconoce(String texto,Lexico oAnaLex,int noAuto) 
  {
     char c;
     _textoIma = texto;
     String lenguaje;
     switch (noAuto) 
     {
       //--------------  Automata  Delimita--------------
      case 0 : _edoAct = 0;
                    break;
       //--------------  Automata  Opmult--------------
      case 1 : _edoAct = 3;
                    break;
       //--------------  Automata  Opsuma--------------
      case 2 : _edoAct = 10;
                    break;
       //--------------  Automata  Identi--------------
      case 3 : _edoAct = 13;
                    break;
       //--------------  Automata  Literal--------------
      case 4 : _edoAct = 16;
                    break;
       //--------------  Automata  Signo--------------
      case 5 : _edoAct = 21;
                    break;
       //--------------  Automata  Opasigna--------------
      case 6 : _edoAct = 23;
                    break;
       //--------------  Automata  Reales1--------------
      case 7 : _edoAct = 26;
                    break;
       //--------------  Automata  Reales2--------------
      case 8 : _edoAct = 31;
                    break;
       //--------------  Automata  Reales3--------------
      case 9 : _edoAct = 35;
                    break;
       //--------------  Automata  Enteros--------------
      case 10 : _edoAct = 39;
                    break;
       //--------------  Automata  Oprel--------------
      case 11 : _edoAct = 42;
                    break;
       //--------------  Automata  Oprel2--------------
      case 12 : _edoAct = 46;
                    break;
       //--------------  Automata  CarEsp--------------
      case 13 : _edoAct = 48;
                    break;
       //--------------  Automata  Punto--------------
      case 14 : _edoAct = 51;
                    break;
     }
     while(oAnaLex.getI()<=_textoIma.length())
       switch (_edoAct) 
       {
       //--------------  Automata  Delimita--------------
      case 0 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=" \n\r\t").indexOf(c)>=0)  _edoAct=1;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 1 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=" \n\r\t").indexOf(c)>=0)  _edoAct=1;  else 
                    if ((lenguaje="!\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿\f").indexOf(c)>=0) _edoAct=2;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 2 : oAnaLex.setI(oAnaLex.getI()-1);
                    return true;
       //--------------  Automata  Opmult--------------
      case 3 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="*").indexOf(c)>=0)  _edoAct=4;  else 
                    if ((lenguaje="/").indexOf(c)>=0)  _edoAct=4;  else 
                    if ((lenguaje="d").indexOf(c)>=0)  _edoAct=5;  else 
                    if ((lenguaje="D").indexOf(c)>=0)  _edoAct=5;  else
                    if ((lenguaje="a").indexOf(c)>=0)  _edoAct=6;  else 
                    if ((lenguaje="A").indexOf(c)>=0)  _edoAct=6;  else
                    if ((lenguaje="m").indexOf(c)>=0)  _edoAct=7;  else 
                    if ((lenguaje="M").indexOf(c)>=0)  _edoAct=7;  else
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 4 : return true;
      case 5 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="i").indexOf(c)>=0)  _edoAct=8;  else 
                    if ((lenguaje="I").indexOf(c)>=0)  _edoAct=8;  else
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 6 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="n").indexOf(c)>=0)  _edoAct=9;  else
                    if ((lenguaje="N").indexOf(c)>=0)  _edoAct=9;  else
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 7 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="o").indexOf(c)>=0)  _edoAct=9;  else
                    if ((lenguaje="O").indexOf(c)>=0)  _edoAct=9;  else
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 8 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="v").indexOf(c)>=0)  _edoAct=4;  else 
                    if ((lenguaje="V").indexOf(c)>=0)  _edoAct=4;  else
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 9 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="d").indexOf(c)>=0)  _edoAct=4;  else
                    if ((lenguaje="D").indexOf(c)>=0)  _edoAct=4;  else
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
       //--------------  Automata  Opsuma--------------
      case 10 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="+").indexOf(c)>=0)  _edoAct=11;  else 
                    if ((lenguaje="o").indexOf(c)>=0)  _edoAct=12;  else
                    if ((lenguaje="O").indexOf(c)>=0)  _edoAct=12; else
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 11 : return true;
      case 12 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="r").indexOf(c)>=0)  _edoAct=11;  else 
                    if ((lenguaje="R").indexOf(c)>=0)  _edoAct=11;  else
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
       //--------------  Automata  Identi--------------
      case 13 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz").indexOf(c)>=0)  _edoAct=14;  else 
                    if ((lenguaje="_").indexOf(c)>=0)  _edoAct=14;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 14 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz").indexOf(c)>=0)  _edoAct=14;  else 
                    if ((lenguaje="_").indexOf(c)>=0)  _edoAct=14;  else 
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=14;  else 
                    if ((lenguaje=" !\"#$%&\'()*+,-./:;<=>?@[\\]^`{|}~€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿\n\t\r\f").indexOf(c)>=0) _edoAct=15;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 15 : oAnaLex.setI(oAnaLex.getI()-1);
                    return true;
       //--------------  Automata  Literal--------------
      case 16 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="\'").indexOf(c)>=0)  _edoAct=17;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 17 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz").indexOf(c)>=0)  _edoAct=18;  else 
                    if ((lenguaje="_").indexOf(c)>=0)  _edoAct=18;  else 
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=18;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 18 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="\'").indexOf(c)>=0)  _edoAct=19;  else 
                    if ((lenguaje="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz").indexOf(c)>=0)  _edoAct=18;  else 
                    if ((lenguaje="_").indexOf(c)>=0)  _edoAct=18;  else 
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=18;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 19 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=" !\"#$%&()*+,-./:;<=>?@[\\]^`{|}~€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿\n\t\r\f").indexOf(c)>=0) _edoAct=20;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 20 : oAnaLex.setI(oAnaLex.getI()-1);
                    return true;
       //--------------  Automata  Signo--------------
      case 21 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="-").indexOf(c)>=0)  _edoAct=22;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 22 : return true;
       //--------------  Automata  Opasigna--------------
      case 23 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=":").indexOf(c)>=0)  _edoAct=24;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 24 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="=").indexOf(c)>=0)  _edoAct=25;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 25 : return true;
       //--------------  Automata  Reales1--------------
      case 26 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=27;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 27 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=27;  else 
                    if ((lenguaje=".").indexOf(c)>=0)  _edoAct=28;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 28 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=29;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 29 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=29;  else 
                    if ((lenguaje=".").indexOf(c)>=0) _edoAct=30;  else 
                    if ((lenguaje=" !\"#$%&\'()*+,-/:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿\n\t\r\f").indexOf(c)>=0) _edoAct=30;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 30 : oAnaLex.setI(oAnaLex.getI()-1);
                    return true;
       //--------------  Automata  Reales2--------------
      case 31 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=".").indexOf(c)>=0)  _edoAct=32;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 32 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=33;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 33 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=".").indexOf(c)>=0) _edoAct=34;  else 
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=33;  else 
                    if ((lenguaje=" !\"#$%&\'()*+,-/:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿\n\t\r\f").indexOf(c)>=0) _edoAct=34;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 34 : oAnaLex.setI(oAnaLex.getI()-1);
                    return true;
       //--------------  Automata  Reales3--------------
      case 35 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=36;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 36 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=36;  else 
                    if ((lenguaje=".").indexOf(c)>=0)  _edoAct=37;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 37 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=".").indexOf(c)>=0) _edoAct=38;  else 
                    if ((lenguaje=" !\"#$%&\'()*+,-/:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿\n\t\r\f").indexOf(c)>=0) _edoAct=38;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 38 : oAnaLex.setI(oAnaLex.getI()-1);
                    return true;
       //--------------  Automata  Enteros--------------
      case 39 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=40;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 40 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="0123456789").indexOf(c)>=0)  _edoAct=40;  else 
                    if ((lenguaje=" !\"#$%&\'()*+,-./:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿\n\t\r\f").indexOf(c)>=0) _edoAct=41;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 41 : oAnaLex.setI(oAnaLex.getI()-1);
                    return true;
       //--------------  Automata  Oprel--------------
      case 42 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=">").indexOf(c)>=0)  _edoAct=43;  else 
                    if ((lenguaje="<").indexOf(c)>=0)  _edoAct=44;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 43 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="=").indexOf(c)>=0)  _edoAct=45;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 44 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=">").indexOf(c)>=0)  _edoAct=45;  else 
                    if ((lenguaje="=").indexOf(c)>=0)  _edoAct=45;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 45 : return true;
       //--------------  Automata  Oprel2--------------
      case 46 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="=").indexOf(c)>=0)  _edoAct=47;  else 
                    if ((lenguaje=">").indexOf(c)>=0)  _edoAct=47;  else 
                    if ((lenguaje="<").indexOf(c)>=0)  _edoAct=47;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 47 : return true;
       //--------------  Automata  CarEsp--------------
      case 48 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje="[").indexOf(c)>=0)  _edoAct=49;  else 
                    if ((lenguaje="]").indexOf(c)>=0)  _edoAct=49;  else 
                    if ((lenguaje="(").indexOf(c)>=0)  _edoAct=49;  else 
                    if ((lenguaje=")").indexOf(c)>=0)  _edoAct=49;  else 
                    if ((lenguaje=",").indexOf(c)>=0)  _edoAct=49;  else 
                    if ((lenguaje=":").indexOf(c)>=0)  _edoAct=49;  else 
                    if ((lenguaje=".").indexOf(c)>=0)  _edoAct=50;  else 
                    if ((lenguaje=";").indexOf(c)>=0)  _edoAct=49;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 49 : return true;
      case 50 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=".").indexOf(c)>=0)  _edoAct=49;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
       //--------------  Automata  Punto--------------
      case 51 : c=SigCar(oAnaLex.getI());
                    oAnaLex.setI(oAnaLex.getI()+1);
                    if ((lenguaje=".").indexOf(c)>=0)  _edoAct=52;  else 
                     { oAnaLex.setI(oAnaLex.getIniToken());
                          return false; }
                    break;
      case 52 : return true;
       }
     switch (_edoAct) 
     {       
            case 2 :      // Autómata  Delimita
            case 15 :      // Autómata  Identi
            case 20 :      // Autómata  Literal
            case 30 :      // Autómata  Reales1
            case 34 :      // Autómata  Reales2
            case 38 :      // Autómata  Reales3
            case 41 :      // Autómata  Enteros
                           oAnaLex.setI(oAnaLex.getI()-1);
                           return true;
     }
     return false;
  }

}
