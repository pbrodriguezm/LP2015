package analizadorlexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parodriguez@locahost
 */
public class lexicoJava {

    /**
     * @param args the command line arguments
     */
    
    int charClass;
    char[] lexeme;
    char nextChar;
    int lexLen;
    int token;
    static int nextToken;
    static FileInputStream in_fp = null;
    private static final int EOF = -1;
    private static final int POINT = 8;
    private static final int LETTER=0;
    private static final int  DIGIT=1;
    private static final int ERROR=2;
    private static final int UNKNOWN=99;
    /* Token codes */
    private static final int FLOAT_LIT=9;
    private static final int INT_LIT=10;
    private static final int IDENT=11;
    private static final int ASSIGN_OP=20;
    private static final int ADD_OP=21;
    private static final int SUB_OP=22;
    private static final int MULT_OP=23;
    private static final int DIV_OP=24;
    private static final int  LEFT_PAREN=25;
    private static final int  RIGHT_PAREN=26;
    private static final int FLOAT=27;

    public lexicoJava() {
        this.lexeme = new char[100];
    }
    
    public int lookup(char ch){
        switch(ch){
            case '(':
            addChar();
            nextToken = LEFT_PAREN;
            break;
            case ')':
            addChar();
            nextToken = RIGHT_PAREN;
            break;
            case '+':
            addChar();
            nextToken = ADD_OP;
            break;
            case '-':
            addChar();
            nextToken = SUB_OP;
            break;
            case '*':
            addChar();
            nextToken = MULT_OP;
            break;
            case '/':
            addChar();
            nextToken = DIV_OP;
            break;
            default:
            addChar();
            nextToken = EOF;
            break;
        }
        return nextToken;
    }
    
    public void addChar() {
        if (lexLen <= 98) {
        lexeme[lexLen++] = nextChar;
        lexeme[lexLen] = 0;
        }
        else    
            System.out.print("Error - lexeme is too long \n");
    }
    
    public void getChar() throws IOException {
        if ((nextChar = (char) in_fp.read()) != EOF) {
            if (Character.isAlphabetic(nextChar))
            {
                charClass = LETTER;
                if(Character.isDigit(nextChar))
                    charClass=UNKNOWN;
            }
             else if (Character.isDigit(nextChar)||nextChar=='.')
             {
                 charClass = DIGIT;
                 if(nextChar=='.')
                    charClass=FLOAT;
                 else charClass=ERROR;
             }
             else charClass = UNKNOWN;
        }
        else
            charClass = EOF;
    }
    
    public void getNonBlank() throws IOException {
        while (Character.isWhitespace(nextChar))
        getChar();
    }
    
    public int lex() throws IOException {
        lexLen = 0;
        getNonBlank();
        switch (charClass) {
    /* Parse identifiers */
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = IDENT;
                break;
    /* Parse integer literals */
            case DIGIT:
                addChar();
                getChar();
                 while (charClass == DIGIT) {
                    addChar();
                    getChar();
                 }
                if(charClass!=FLOAT)
                {
                    nextToken=INT_LIT;
                    break;
                }
            case FLOAT:
                addChar();
                getChar();
                while(charClass==DIGIT)
                {
                    addChar();
                    getChar();
                }
                nextToken=FLOAT;
                break;
            case ERROR:
                addChar();
                getChar();
                while(charClass==DIGIT || charClass==LETTER)
                {
                    addChar();
                    getChar();
                }
                nextToken=ERROR;
                break;
    /* Parentheses and operators */
            case UNKNOWN:
                lookup(nextChar);
                getChar();
                break;
            case EOF:
                nextToken = EOF;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'F';
                lexeme[3] = 0;
                break;
    /* EOF */

        } /* End of switch */

        System.out.println("Next token is: "+nextToken+ " Next lexeme is: "+new String(lexeme));
        return nextToken;
    } 
    
    public static void main(String[] args) {
        // TODO code application logic here
        lexicoJava a= new lexicoJava();
        File file = new File("/home/parodriguez/tareas2015/LP/front.in");
        
        try {
            in_fp = new FileInputStream(file);
            a.getChar();
            do{
                a.lex();
            }while(nextToken!=EOF);
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
}
