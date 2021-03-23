  dim  a as single,  b as single
  dim  c as integer
  dim  s as string, z as integer
  
  function calcular   as string
    i = 0
	do while i < 10
	  if i == 1 then
	     i = i + 5
	  else
	     i = i + 1
	  end if
	loop
	i = 10
  end function
  
  function ecuacion ( c0 as single ) as single
  end function
  
  function leyendaRespuesta (  c1 as integer, c2 as single ) as string
  end function

  sub retardo
    i = 0
	do while i < 10
	  if i == 1 then
	     i = i + 5
	  else
	     i = i + 1
	  end if
	loop
	i = 10  
  end sub
  
  sub imprimir (  x as integer, y as single, z as string ) 
  end sub

  a  = 882
  b  = 3.12123
  c  = 0
  d  = ( 2 * c + d ) * a + 1
  s  = "Hola mis inges"  
  
  if  a <= 10   then
     if a > 0   then
        a = 1 
	    b = 1.2
	 else
     end if	 
  else 
     a = 1   
     do while b <= a
        a = 1 
	    b = 1.2
     loop
     b = 1.2	 
  end if  
  
  do while  a <>  10 
        a2 =  promedio ( a, b, 3 )
        do while  a + 1 * y  == 2 * ( b + 3 + 1 )  
           a  =  1 + promedio ( a, b, 3 * ( a + b ) ) * x
        loop
        b  =  b * suma ()  		
  loop  

end	   