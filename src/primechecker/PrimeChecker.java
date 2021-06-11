/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primechecker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author pedja
 */
public class PrimeChecker {
    

    
    static class Poly {

	BigInteger [] monos;
	int degree;
	
	/**
	 * Set all coefficients and degree to zero
	 * 
	 * @param p
	 */
	static void zero(Poly p)
	{
		for( int i = 0; i < p.monos.length; i++ )
			p.monos[i] = BigInteger.ZERO;
		
		p.degree = 0;
	}
	
	/**
	 * Debugging method
	 */
	void printMonos()
	{
		for( int i = 0; i <= this.degree; i++ )
			System.out.println(this.monos[i]);
	}
	
	/**
	 * Create the zero polynomial
	 */
	public Poly()
	{
		this.monos = new BigInteger[10];
		zero(this);
	}

	/**
	 * Create a zero polynomial with 
	 * a coefficient array of a given size
	 * 
	 * @param size
	 */
	public Poly(int size)
	{
		this.monos = new BigInteger[size+1];
		zero(this);
	}
	
	/**
	 * Clone a polynomial
	 * 
	 * @param p
	 */
	public Poly(Poly p)
	{
		this.degree = p.degree;
		this.monos = Arrays.copyOf(p.monos, p.monos.length);
	}
	
	/**
	 * Create a one-term polynomial
	 * @param coeff coefficient
	 * @param exp exponent
	 */
	public Poly( BigInteger coeff, int exp )
	{
		this(exp);
		if (coeff.compareTo(BigInteger.ZERO) != 0)
		{
			monos[exp] = coeff;
			degree = exp;
		}
	}
	
	/**
	 * Degree of the polynomial
	 * @return degree of the polynomial
	 */
	public int degree()
	{
		return this.degree;
	}

	/**
	 * Obtain coefficient for a given term of
	 * the polynomial
	 * 
	 * @param d
	 * @return coefficient for the term of given degree
	 */
	public BigInteger coefficient(int d)
	{
		return this.monos[d];
	}
	
	/**
	 * String representation of polynomial
	 */
	public String toString()
	{
		String s = "";
		for( int i = this.degree; i >= 1; i-- )
			if( monos[i].compareTo(BigInteger.ZERO) != 0 ) 
				s += (monos[i].compareTo(BigInteger.ONE) == 0 ? "" : monos[i])
						+ "x^" + i + " + ";
		
		if ( monos[0].compareTo(BigInteger.ZERO) !=0 )
			s += monos[0] + " + ";

		return s == "" ? "0" : s.substring(0,s.length()-3);
	}

	@Override
	/***
	 * Implement an equals method
	 * 
	 * @returns true if the two objects are the same
	 */
	public boolean equals(Object p)
	{
		if (p == null) return false;
	    if (p == this) return true;
	    if (this.getClass() != p.getClass()) return false;
	    
	    if( this.degree != ((Poly) p).degree )
			return false;
		
		for( int i = 0; i <= degree; i++ )
			if( this.monos[i].compareTo(((Poly) p).monos[i]) != 0 )
				return false;
		
		return true;
	}

	/**
	 * Add two polynomials together
	 * 
	 * @param p
	 * @return the sum of this and p
	 */
	public Poly plus(Poly p)
	{
		int maxDegree = Math.max(this.degree, p.degree);
		
		Poly sum = new Poly(maxDegree);
		sum.degree = maxDegree;
		
		for( int i = 0; i <= maxDegree; i++ )
			sum.monos[i] = (i <= this.degree && i <= p.degree) ? p.monos[i].add(this.monos[i]) :
				(i > this.degree) ? p.monos[i] : this.monos[i];
		
		return sum;
	}
	
	/***
	 * Subtract two polynomials
	 * 
	 * @param p
	 * @return this minus p
	 */
	public Poly minus(Poly p)
	{
		int maxDegree = Math.max(this.degree, p.degree);
		
		Poly difference = new Poly(maxDegree);
		difference.degree = maxDegree;
		
		for( int i = 0; i <= maxDegree; i++ )
			difference.monos[i] = (i <= this.degree && i <= p.degree) ? this.monos[i].subtract(p.monos[i]) :
				(i > this.degree) ? p.monos[i].negate() : this.monos[i];

		updateDegree(difference);
		return difference;
	}
	
	/**
	 * Synchronize the degree of the polynomial
	 * with the coefficient array length and coefficient
	 * values
	 * 
	 * @param p
	 */
	static void updateDegree(Poly p)
	{
		p.degree = p.monos.length - 1;
		
		while( p.monos[p.degree].compareTo(BigInteger.ZERO) == 0 && p.degree > 0 )
			p.degree--;
	}
	
	/**
	 * Multiply all coefficients by b
	 * 
	 * @param b
	 * @return b*this
	 */
	public Poly times(BigInteger b)
	{
		Poly product = new Poly(this);

		for( int i = 0; i <= product.degree; i++ )
			product.monos[i] = product.monos[i].multiply(b);
		
		return product;
	}
	
	/**
	 * Multiply this with another polynomial
	 * 
	 * @param p
	 * @return this*p
	 */
	public Poly times(Poly p)
	{
        Poly product = new Poly(this.degree+p.degree);
        product.degree = this.degree + p.degree;
        
        for( int i = 0; i <= this.degree; i++ )
        {
        	for( int j = 0; j <= p.degree; j++ )
                product.monos[i+j] = product.monos[i+j].add(this.monos[i].multiply(p.monos[j]));
        }

        return product;
	}
	
	/**
	 * Mod all of the coefficients by m
	 * 
	 * @param m
	 * @return this mod m
	 */
	public Poly mod(BigInteger m)
	{
		Poly remainder = new Poly(this);
		
		for( int i = 0; i <= remainder.degree; i++ )
			remainder.monos[i] = monos[i].mod(m);

		updateDegree(remainder);
		
		return remainder;
	}

	/**
	 * Mod this by a polynomial of the form x^e + a
	 * 
	 * @param m
	 * @return
	 */
	public Poly mod(Poly m)
	{
		Poly remainder = new Poly(this);

		while( remainder.degree >= m.degree )
		{
			int diff = remainder.degree - m.degree;
			Poly sub = new Poly(m).times(new Poly(remainder.coefficient(remainder.degree), diff));
			// System.out.println("sub = " + sub);
			remainder = remainder.minus(sub);
		}

		return remainder;
	}
	
	/***
	 * Exponentiate a polynomial with the coefficients mod a constant
	 * and the polynomial mod another polynomial
	 * 
	 * @param exponent
	 * @param mPoly
	 * @param mBigInteger
	 * @return this^exponent (mod mBigInteger, mPoly)
	 */
	public Poly modPow(BigInteger exponent, Poly mPoly, BigInteger mBigInteger)
	{
		
		int maxBits = exponent.bitLength();

		Poly answer = new Poly(BigInteger.ONE,0);
		for( int bit = 0; bit < maxBits; bit++ )
		{
			// explicitly break apart the multiplication and modulus
			answer = answer.times(answer);
			answer = answer.mod(mPoly);
			answer = answer.mod(mBigInteger);
			
			// Consider bits from left to right
			// if the bit is set multiply by this
			if( exponent.testBit(maxBits - bit - 1) )
			{
				// explicitly break apart the multiplication and modulus
				answer = answer.times(this);
				answer = answer.mod(mPoly);
				answer = answer.mod(mBigInteger);
			}
				
		}
			
		return answer;
	}
	

    }
    
    static boolean isprime(BigInteger n,BigInteger m,int c) 
    {
    Poly p0 = new Poly(BigInteger.ONE,1);
    Poly p1 = new Poly (BigInteger.ONE,0);
    Poly p2 = new Poly (n.subtract(BigInteger.ONE),1);
    Poly p3 = new Poly (BigInteger.ONE,2);
    Poly p4 = new Poly (BigInteger.valueOf(c),0);
    
    Poly p5= p0.plus(p1);
    Poly p6= p2.plus(p1);
    Poly p7= p3.minus(p4);
    
    // (1+X)^n <> 1-X (mod X^2 - c,n)   
        return(p6.equals(p5.modPow(n,p7,n)));
      
        
    
    
    } 
    
   
   public static int computeJacobiSymbol(BigInteger initial_a, BigInteger n) {
        // Step 1: a = a mod n
        BigInteger a = initial_a.mod(n);
        // Step 2: if a = 1 or n = 1 return 1
        if (a.equals(BigInteger.ONE) || n.equals(BigInteger.ONE)) {
            return 1;
        }
        // Step 3: if a = 0 return 0
        if (a.equals(BigInteger.ZERO)) {
            return 0;
        }
        // Step 4: define e and a_1 such that a = 2^e * a_1 where a_1 is odd
        int e = 0;
        BigInteger a_1 = a;
        while (a_1.remainder(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            e++;
            a_1 = a_1.divide(BigInteger.valueOf(2));
        }
        // Step 5: if e is even, then s = 1;
        //          else if n mod 8 = 1 or n mod 8 = 7, then s = 1
        //          else if n mod 8 = 3 or n mod 8 = 5, then s = -1
        int s;
        if (e % 2 == 0) {
            s = 1;
        } else {
            BigInteger n_mod_eight = n.mod(BigInteger.valueOf(8));
            if (n_mod_eight.equals(BigInteger.ONE) || n_mod_eight.equals(BigInteger.valueOf(7))) {
                s = 1;
            } else { // n_mod_eight.equals(THREE) || n_mod_eight.equals(FIVE)
                s = -1;
            }
        }
        // Step 6: if n mod 4 = 3 and a_1 mod 4 = 3, then s = -s
        if (n.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) && a_1.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            s = -s;
        }
        // Step 7: n_1 = n mod a_1
        BigInteger n_1 = n.mod(a_1);
        // Step 8: return s * JacobiSymbol(n_1, a_1)
        return s * computeJacobiSymbol(n_1, a_1);
		
    }
    
      static int nextPrime(int input){
  int counter;
  input++;
  while(true){
    int l = (int) Math.sqrt(input);
    counter = 0;
    for(int i = 2; i <= l; i ++){
      if(input % i == 0)  counter++;
    }
    if(counter == 0)
      return input;
    else{
      input++;
      continue;
    }
  }
}
           private boolean isPerfectSquare(BigInteger num) {
         BigInteger left = BigInteger.ONE;
        BigInteger right = num;
        
        while (left.compareTo(right)==0 || left.compareTo(right)==-1 ){
            BigInteger mid = (left.add(right)).divide(BigInteger.valueOf(2));
            // Check if mid is perfect square
            if (mid.multiply(mid).equals(num))
                {
                return true;
            }
            // Mid is small -> go right to increase mid
            if (mid.multiply(mid).compareTo(num)==-1)
            {
                left = mid.add(BigInteger.ONE);
            }
            // Mid is large -> to left to decrease mid
            else
            {
                right = mid.subtract(BigInteger.ONE);
            }
        }
        return false;
    }
      
      public static BigDecimal eval(final String str) {
    return new Object() {
        int pos = -1, ch;

        void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        BigDecimal parse() {
            nextChar();
            BigDecimal x = parseExpression();
            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
            return x;
        }

        // Grammar:
        // expression = term | expression `+` term | expression `-` term
        // term = factor | term `*` factor | term `/` factor
        // factor = `+` factor | `-` factor | `(` expression `)`
        //        | number | functionName factor | factor `^` factor

        BigDecimal parseExpression() {
            BigDecimal x = parseTerm();
            for (;;) {
                if      (eat('+')) x=x.add(parseTerm()); // addition
                else if (eat('-')) x=x.subtract(parseTerm()); // subtraction
                else return x;
            }
           }

        BigDecimal parseTerm() {
            BigDecimal x = parseFactor();
            for (;;) {
                if      (eat('*')) x=x.multiply(parseFactor()); // multiplication
                else if (eat('/')) x=x.divide(parseFactor()); // division
                else return x;
            }
        }

        BigDecimal parseFactor() {
            if (eat('+')) return parseFactor(); // unary plus
            if (eat('-')) return parseFactor().negate(); // unary minus

            BigDecimal x;
            int startPos = this.pos;
            if (eat('(')) { // parentheses
                x = parseExpression();
               if (!eat(')'))
               {
                throw new RuntimeException("Unexpected: " + (char)ch);   
               }
            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = new BigDecimal(str.substring(startPos, this.pos));
            }  else {
                throw new RuntimeException("Unexpected: " + (char)ch);
            }

            if (eat('^')) x =x.pow(parseFactor().intValue()); // exponentiation

            return x;
        }
    }.parse();
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
           try{
        BigInteger n,m;
        BigDecimal bd;
        int r;
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a number: ");
        String expression = input.nextLine();
    	
        PrimeChecker primechecker = new PrimeChecker();
        bd = primechecker.eval(expression);
        if (bd.remainder( BigDecimal.ONE ).equals(BigDecimal.ZERO))
        {
            n=bd.toBigInteger();
            m=n;
       
        if(m.compareTo(BigInteger.valueOf(2))==-1)
        {
        System.out.println("Number must be greater than one!");    
        }
        else if(m.equals(BigInteger.valueOf(2))) {
           System.out.println("Prime!");
        }
        else if (m.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)){
           System.out.println("Composite!"); 
        }
        else if(primechecker.isPerfectSquare(m)){
           
            System.out.println("Composite!");
        }
        else {
            
        int     c=3;
        while( primechecker.computeJacobiSymbol(BigInteger.valueOf(c),m)!=-1) {
          c=primechecker.nextPrime(c);
          
        }
        
        if(primechecker.isprime(n,m,c))
        {
        System.out.println("Prime!");
       
        }
        else {
          System.out.println("Composite!");
          
        }
        }
        }
        else {
         System.out.println("Invalid entry!");    
        }
        }
        catch(Exception e) {
         System.out.println("Invalid entry!");   
        }
    }
    }
    

