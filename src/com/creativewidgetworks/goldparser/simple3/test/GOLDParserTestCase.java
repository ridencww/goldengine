package com.creativewidgetworks.goldparser.simple3.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import junit.framework.TestCase;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.Id;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.NumberLiteral;
import com.creativewidgetworks.goldparser.simple3.rulehandlers.StringLiteral;
import com.creativewidgetworks.goldparser.util.ConsoleDriverForTests;

public abstract class GOLDParserTestCase extends TestCase {

    public static final String GRAMMAR  = "grammars/Simple3.cgt";
    public static final String HANDLERS = "com.creativewidgetworks.goldparser.simple3.rulehandlers";
    
    private boolean allowParseExceptions;

    /**
     * Extended GOLDParser class that exposes protected methods and provides
     * functionality to push and pop reductions on the stack.
     */
    public class GOLDParserForTesting extends GOLDParser {
        public GOLDParserForTesting() {
            this(new File(GRAMMAR), HANDLERS, true);
        }

        public GOLDParserForTesting(File cgtFile, String rulesPackage, boolean trimReductions) {
            super(cgtFile, rulesPackage, trimReductions);
        }
        
        public void close() {
            if (source != null) {
                try {
                    source.close();
                } catch (Exception e) {
                    // okay to ignore
                }
            }
        }

        @Override
        public Reduction createInstance() throws ParserException {
            return super.createInstance();
        }

        @Override
        public Reduction createInstance(String ruleName) throws ParserException {
            return super.createInstance(ruleName);
        }
        
        public Reduction makeReductionAndPush(Token... tokens) {
            Reduction reduction = makeReduction(tokens);
            haveReduction = true;
            stack.push(new Token(new Symbol(), reduction));
            return reduction;
        }
        
        public void pushReduction(Reduction reduction) {
            haveReduction = true;
            stack.push(new Token(new Symbol(), reduction));
        }
    }

    /*----------------------------------------------------------------------------*/

    public boolean isAllowParseExceptions() {
        return allowParseExceptions;
    }

    public void setAllowParseExceptions(boolean exceptionsAllowed) {
        allowParseExceptions = exceptionsAllowed;
    }

    public String[] executeProgram(String src) throws ParserException {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        return executeProgram(parser, src, null);
    }

    public String[] executeProgram(File src) throws ParserException {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        return executeProgram(parser, src, null);
    }

    public String[] executeProgram(URL src) throws ParserException {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        return executeProgram(parser, src, null);
    }

    public String[] executeProgram(Reader src) throws ParserException {
        GOLDParserForTesting parser = new GOLDParserForTesting();
        return executeProgram(parser, src, null);
    }

    /**
     * Test helper that will execute a program using various source object types
     * @param parser a GOLDParser engine instance
     * @param src String, File, URL, or Reader that can return the source code to execute
     * @return An array of Strings representing the program output
     * @throws ParserException if something goes wrong
     */    
    public String[] executeProgram(GOLDParserForTesting parser, Object src) throws ParserException {
        return executeProgram(parser, src, null);
    }
    
    /**
     * Test helper that will execute a program using various source object types
     * @param parser a GOLDParser engine instance
     * @param src String, File, URL, or Reader that can return the source code to execute
     * @param consoleDriver used to capture and provide data to anything that uses StdIn/StdOut (e.g., Display)
     * @return An array of Strings representing the program output
     * @throws ParserException if something goes wrong
     */
    public String[] executeProgram(GOLDParserForTesting parser, Object src, ConsoleDriverForTests consoleDriver) 
        throws ParserException {
        
        String[] lines = null;

        if (!parser.isReady()) {
            throw new ParserException("Parser has not been properly initialized: " + parser.getErrorMessage());
        }

        // Create a new console driver if one wasn't passed in.
        if (consoleDriver == null) {
            consoleDriver = new ConsoleDriverForTests();
        }
        
        // Save current System input and output streams
        InputStream saveInp = System.in;
        PrintStream saveErr = System.err;
        PrintStream saveOut = System.out;
        
        InputStream is = null;
        PrintStream ps = null;
        try {
            is = consoleDriver.getInputStream();
            ps = consoleDriver.getPrintStream();
        } catch (Exception e) {
            throw new ParserException(e.getMessage());
        }
            
        try {
            // Hook into StdIn and StdOut/StdErr
            System.setIn(is);
            System.setErr(ps);
            System.setOut(ps);

            Reader reader = null;
            try {
                if (src instanceof String) {
                    reader = new StringReader((String)src);
                } else if (src instanceof File) {
                    reader = new FileReader((File)src);
                } else if (src instanceof URL) {
                    reader = new FileReader(((URL)src).getFile().replace("%20", " "));
                } else if (src instanceof Reader) {
                    reader = (Reader)src;
                } else {
                    throw new ParserException("Invalid source object type: " + src.getClass().getSimpleName());
                }
            } catch (FileNotFoundException nfe) {
                throw new ParserException("Invalid source file: " + src.toString());
            }

            try {
                if (parser.parseSourceStatements(reader)) {
                    parser.getCurrentReduction().execute();
                }
            } catch (ParserException e) {
                if (isAllowParseExceptions()) {
                    setAllowParseExceptions(false);
                    throw e;
                }
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            
            parser.close();

            System.setIn(saveInp);
            System.setOut(saveErr);
            System.setOut(saveOut);

            // Get output and append any error message
            StringBuilder sb = new StringBuilder(consoleDriver.getDataWritten());
            if (parser.getErrorMessage() != null && parser.getErrorMessage().trim().length() > 0) {
                 sb.append(parser.getErrorMessage()).append("\r\n");
            }

            // Return the program output as a series of lines
            lines = sb.toString().split("\r\n");
        }

        return lines;
    }

    /*----------------------------------------------------------------------------*/
    /* Helper methods to produce tokens and reductions
    /*----------------------------------------------------------------------------*/

    public Id makeIdForTest(String name) {
        GOLDParserForTesting tempParser = new GOLDParserForTesting();
        tempParser.pushReduction(makeReduction(new Token(new Symbol(), name)));
        return new Id(tempParser);        
    }
    
    public NumberLiteral makeNumberLiteralForTest(String value) throws ParserException {
        GOLDParserForTesting tempParser = new GOLDParserForTesting();
        tempParser.pushReduction(makeReduction(new Token(new Symbol(), value)));
        return new NumberLiteral(tempParser);
    }
    
    public StringLiteral makeStringLiteralForTest(String value) throws ParserException {
        GOLDParserForTesting tempParser = new GOLDParserForTesting();
        tempParser.pushReduction(makeReduction(new Token(new Symbol(), "\"" + value + "\"")));
        return new StringLiteral(tempParser);
    }
    
    public Reduction makeReduction(Token... tokens) {
        Reduction reduction = new Reduction();
        for (Token token : tokens) {
            reduction.add(reduction.size(), token);
        }
        return reduction;
    }
  
    public Token makeToken(String name, SymbolType type, Object value) {
        return new Token(new Symbol(name, type, 0), value);
    }
    
    /*----------------------------------------------------------------------------*/
    
    public String[] makeExpected(String... expected) {
        return expected;
    }

    public void validateLines(String[] expected, String[] actual) throws Exception {
        if (expected.length != actual.length) {
            if (actual.length == 1) {
                fail(actual[0]);
            } else {
                StringBuilder sb = new StringBuilder("Wrong number of lines, expected ");
                sb.append(expected.length);
                sb.append(" but got ");
                sb.append(actual.length);
                sb.append("\r\n");
                for (int i = 0; i < actual.length; i++) {
                    sb.append("    ");
                    sb.append(actual[i]);
                    sb.append("\r\n");
                }
                fail(sb.toString());
            }
        }

        for (int i = 0; i < expected.length; i++) {
            assertEquals("wrong value at offset " + i, expected[i], actual[i]);
        }
    }

}