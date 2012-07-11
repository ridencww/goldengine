package com.creativewidgetworks.goldparser.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.LRAction;
import com.creativewidgetworks.goldparser.engine.LRState;
import com.creativewidgetworks.goldparser.engine.LRStateList;
import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Position;
import com.creativewidgetworks.goldparser.engine.Production;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.engine.enums.SymbolType;

public class GOLDParserTest extends TestCase {

    /**
     * Overridden GOLDParser class that exposes protected methods so they can be
     * tested.
     */
    public class GOLDParserForTesting extends GOLDParser {

        public List<Token> tokensRead = new ArrayList<Token>();
        
        public GOLDParserForTesting() {
            super();
        }

        public GOLDParserForTesting(File cgtFile, String rulesPackage, boolean trimReductions) {
            super(cgtFile, rulesPackage, trimReductions);
        }

        @Override
        public Reduction createInstance() throws ParserException {
            return super.createInstance();
        }

        @Override
        public Reduction createInstance(String ruleName) throws ParserException {
            return super.createInstance(ruleName);
        }
        
        @Override
        protected Token nextToken() {
            Token token = super.nextToken();
            tokensRead.add(token);
            return token;
        }
        
        public List<Token> getTokensRead() {
            return tokensRead;
        }
        
        public LRStateList getStates() {
            return lrStates;
        }
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testConstructor_no_grammar_file() throws Exception {
        GOLDParser parser = new GOLDParser((File)null, "com.creativewidgetworks.goldparser.simple2", true);
        assertFalse(parser.isReady());
        assertEquals("wrong msg", "Grammar table cannot be loaded and initialized. A grammar file must be specified.", parser.getErrorMessage());
    
        parser = new GOLDParser((InputStream)null, "com.creativewidgetworks.goldparser.simple2", true);
        assertFalse(parser.isReady());
        assertEquals("wrong msg", "Grammar table cannot be loaded and initialized. InputStream null", parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testSetup_no_grammar_file() throws Exception {
        GOLDParser parser = new GOLDParser();
        try {
            parser.setup(null);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("wrong msg", "A grammar file must be specified.", e.getMessage());
        }
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testConstructor_missing_grammar_file() throws Exception {
        GOLDParser parser = new GOLDParser(new File("no_such_file"), "com.creativewidgetworks.goldparser.simple2", true);
        assertFalse(parser.isReady());
        String expected = "Grammar table cannot be loaded and initialized. ";
        assertTrue("wrong msg, expected \"" + expected + "\", got \"" + parser.getErrorMessage() + "\"", 
                parser.getErrorMessage().startsWith(expected));
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testSetup_missing_grammar_file() throws Exception {
        GOLDParser parser = new GOLDParser();
        try {
            parser.setup(new File("no_such_file"));
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // expected - not asserting message as message can vary by platform
        }
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testConstructor_no_rules() throws Exception {
        GOLDParser parser = new GOLDParser(new File("src/test/resources/parser/Simple2.cgt"), "bad.package", true);
        assertFalse("shouldn't be ready", parser.isReady());
        assertEquals("wrong msg", "Grammar table cannot be loaded and initialized. No rule handlers were found - base package: bad.package", parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testConstructor_success() throws Exception {
        GOLDParser parser = new GOLDParser(new File("src/test/resources/parser/Simple2.cgt"), "com.creativewidgetworks.goldparser.simple2", true);
        assertTrue("should be ready", parser.isReady());
        assertNotNull("expected scope", parser.getCurrentScope());
        assertEquals("wrong scope", "GLOBAL", parser.getCurrentScope().getName());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParse_no_grammar_file() throws Exception {
        GOLDParser parser = new GOLDParser();
        assertFalse(parser.parseSourceStatements(new StringReader("Display \"hello world\"")));
        assertEquals("wrong msg", "Grammar table not loaded.", parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParse_no_source_statements() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        assertFalse(parser.parseSourceStatements((String) null));
        assertEquals("wrong msg", "No source statements to parse.", parser.getErrorMessage());
        assertFalse(parser.parseSourceStatements(""));
        assertEquals("wrong msg", "No source statements to parse.", parser.getErrorMessage());
        assertFalse(parser.parseSourceStatements("\r\n  \t  "));
        assertEquals("wrong msg", "No source statements to parse.", parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParse_no_source_reader() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        assertFalse(parser.parseSourceStatements((Reader) null));
        assertEquals("wrong msg", "A source Reader must be supplied.", parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParse_success() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        assertTrue(parser.parseSourceStatements(new StringReader("Display \"hello world\"")));
        assertEquals("wrong msg", "", parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParse_with_rules_success() throws Exception {
        GOLDParser parser = new GOLDParser(new File("src/test/resources/parser/Simple2.cgt"), "com.creativewidgetworks.goldparser.simple2", true);
        assertTrue(parser.parseSourceStatements(new StringReader("Display \"hello world\"")));
        assertEquals("wrong msg", "", parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testClear() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.addErrorMessage("Syntax boo-boo");
        parser.setCurrentScope(new Scope("Widget"));
        parser.setProgramVariable("mode", new Variable("debug"));
        assertEquals("wrong msg", "Syntax boo-boo", parser.getErrorMessage());
        assertEquals("wrong number of scopes", 2, parser.getScopes().size());
        assertEquals("wrong number of variables", 0, parser.getScopes().get("GLOBAL").getVariables().size());
        assertEquals("wrong number of variables", 1, parser.getCurrentScope().getVariables().size());
        assertEquals("scope", "Widget", parser.getCurrentScope().getName());
        assertEquals("variable", "debug", parser.getProgramVariable("mode").asString());

        parser.clear();
        assertEquals("wrong msg", "", parser.getErrorMessage());
        assertEquals("wrong number of scopes", 1, parser.getScopes().size());
        assertNull("shouldn't exist", parser.getScopes().get("Widget"));
        assertEquals("scope", "GLOBAL", parser.getCurrentScope().getName());
        assertEquals("wrong number of variables", 0, parser.getScopes().get("GLOBAL").getVariables().size());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testClearProgramVariables() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setProgramVariable("mode1", new Variable("global"));
        parser.setCurrentScope(new Scope("Widget", parser.getCurrentScope()));
        parser.setProgramVariable("mode2", new Variable("widget"));
        assertEquals("wrong number of variables", 1, parser.getScopes().get("GLOBAL").getVariables().size());
        assertEquals("wrong number of variables", 1, parser.getScopes().get("Widget").getVariables().size());
        assertEquals("variable1", "global", parser.getProgramVariable("mode1").asString());
        assertEquals("variable2", "widget", parser.getProgramVariable("mode2").asString());

        parser.clearProgramVariables();
        assertEquals("wrong number of variables", 0, parser.getScopes().get("GLOBAL").getVariables().size());
        assertEquals("wrong number of variables", 0, parser.getScopes().get("Widget").getVariables().size());
        assertNull("variable1", parser.getProgramVariable("mode1"));
        assertNull("variable2", parser.getProgramVariable("mode2"));
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testClearProgramVariable() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setProgramVariable("mode1", new Variable("global"));
        parser.setCurrentScope(new Scope("Widget", parser.getCurrentScope()));
        parser.setProgramVariable("mode2", new Variable("widget"));
        assertEquals("wrong number of variables", 1, parser.getScopes().get("GLOBAL").getVariables().size());
        assertEquals("wrong number of variables", 1, parser.getScopes().get("Widget").getVariables().size());
        assertEquals("variable1", "global", parser.getProgramVariable("mode1").asString());
        assertEquals("variable2", "widget", parser.getProgramVariable("mode2").asString());

        parser.clearProgramVariable("mode1");
        assertEquals("wrong number of variables", 0, parser.getScopes().get("GLOBAL").getVariables().size());
        assertEquals("wrong number of variables", 1, parser.getScopes().get("Widget").getVariables().size());
        assertNull("variable1", parser.getProgramVariable("mode1"));
        assertNotNull("variable2", parser.getProgramVariable("mode2"));
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testClearProgramVariable_case_insensitive() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setIgnoreCaseOfVariables(true);
        parser.setProgramVariable("mode1", new Variable("global"));
        parser.setCurrentScope(new Scope("Widget", parser.getCurrentScope()));
        parser.setProgramVariable("mode2", new Variable("widget"));
        assertEquals("wrong number of variables", 1, parser.getScopes().get("GLOBAL").getVariables().size());
        assertEquals("wrong number of variables", 1, parser.getScopes().get("Widget").getVariables().size());
        assertEquals("variable1", "global", parser.getProgramVariable("mode1").asString());
        assertEquals("variable2", "widget", parser.getProgramVariable("mode2").asString());

        parser.clearProgramVariable("moDe1");
        assertEquals("wrong number of variables", 0, parser.getScopes().get("GLOBAL").getVariables().size());
        assertEquals("wrong number of variables", 1, parser.getScopes().get("Widget").getVariables().size());
        assertNull("variable1", parser.getProgramVariable("mode1"));
        assertNotNull("variable2", parser.getProgramVariable("mode2"));
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testIgnoreCaseOfVariables_case_sensitive() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setIgnoreCaseOfVariables(false);
        parser.setProgramVariable("mode1", new Variable("debug1"));
        parser.setCurrentScope(new Scope("Widget", parser.getCurrentScope()));
        parser.setProgramVariable("mode2", new Variable("debug2"));

        assertFalse(parser.isIgnoreCaseOfVariables());
        assertNotNull(parser.getProgramVariable("mode1"));
        assertNotNull(parser.getProgramVariable("mode2"));
        assertNull(parser.getProgramVariable("moDe1"));
        assertNull(parser.getProgramVariable("moDe2"));
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testIgnoreCaseOfVariables_case_insensitive() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setIgnoreCaseOfVariables(true);
        parser.setProgramVariable("mode1", new Variable("debug1"));
        parser.setCurrentScope(new Scope("Widget", parser.getCurrentScope()));
        parser.setProgramVariable("mode2", new Variable("debug2"));

        assertTrue(parser.isIgnoreCaseOfVariables());
        assertNotNull(parser.getProgramVariable("mode1"));
        assertNotNull(parser.getProgramVariable("mode2"));
        assertNotNull(parser.getProgramVariable("moDe1"));
        assertNotNull(parser.getProgramVariable("moDe2"));
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testSetProgramVariable_create_default_scope() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setCurrentScope(null);
        assertNull(parser.getCurrentScope());

        parser.setProgramVariable("mode", new Variable("debug"));
        assertNotNull(parser.getCurrentScope());
        assertEquals("wrong scope", "GLOBAL", parser.getCurrentScope().getName());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParse_syntax_error() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        assertFalse(parser.parseSourceStatements(new StringReader("Display hello world")));
        assertEquals("wrong msg", "Syntax error at line 1, column 15. " + "Read Id, expecting (EOF) - '&' ')' '*' '/' '+' '<' '<=' '<>' '==' '>' '>=' "
                + "assign display do else end if read then while", parser.getErrorMessage());

        assertFalse(parser.parseSourceStatements(new StringReader("")));
        assertEquals("wrong msg", "Syntax error at line 1, column 1. Read (EOF), expecting assign display if while", parser.getErrorMessage());

        assertFalse(parser.parseSourceStatements(new StringReader("\t  \t")));
        assertEquals("wrong msg", "Syntax error at line 1, column 5. Read (EOF), expecting assign display if while", parser.getErrorMessage());
        assertFalse(parser.parseSourceStatements(new StringReader("bad-keyword \"Hello\"")));
        assertEquals("wrong msg", "Syntax error at line 1, column 1. Read Id, expecting assign display if while", parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParse_lexical_error() throws Exception {
        GOLDParser parser = new GOLDParser(new File("src/test/resources/parser/Simple2.cgt"), "com.creativewidgetworks.goldparser.simple2", true);
        assertFalse(parser.parseSourceStatements(new StringReader("Assign i = 1..0")));
        assertEquals("wrong msg", "Lexical error at line 1, column 13. Read (Error)", parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testGetParseTree_parse_not_called() throws Exception {
        GOLDParser parser = new GOLDParser();
        parser.setGenerateTree(true);
        assertEquals("wrong tree", "Parse tree is not available. Did you set generateTree(true)?", parser.getParseTree());
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        assertEquals("wrong tree", "Parse tree is not available. Did you set generateTree(true)?", parser.getParseTree());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testGetParseTree_parse_called_no_trim() throws Exception {
        // Sample source to parse
        String source = "Assign a = 1 + 2 / (3 * 4)\r\n" + "Display \"Answer: \" + a ";

        // This should match the output of the VB reference implementation
        String expected = 
            "+-<Statements> ::= <Statement> <Statements>\r\n" + 
            "| +-<Statement> ::= assign Id '=' <Expression>\r\n" + 
            "| | +-Assign\r\n" + "| | +-a\r\n" + "| | +-=\r\n" + 
            "| | +-<Expression> ::= <Add Exp>\r\n" + 
            "| | | +-<Add Exp> ::= <Add Exp> '+' <Mult Exp>\r\n" + 
            "| | | | +-<Add Exp> ::= <Mult Exp>\r\n" + 
            "| | | | | +-<Mult Exp> ::= <Negate Exp>\r\n" + 
            "| | | | | | +-<Negate Exp> ::= <Value>\r\n" + 
            "| | | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | | +-1\r\n" + 
            "| | | | +-+\r\n" + 
            "| | | | +-<Mult Exp> ::= <Mult Exp> '/' <Negate Exp>\r\n" + 
            "| | | | | +-<Mult Exp> ::= <Negate Exp>\r\n" + 
            "| | | | | | +-<Negate Exp> ::= <Value>\r\n" + 
            "| | | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | | +-2\r\n" + "| | | | | +-/\r\n" + 
            "| | | | | +-<Negate Exp> ::= <Value>\r\n" + 
            "| | | | | | +-<Value> ::= '(' <Expression> ')'\r\n" + 
            "| | | | | | | +-(\r\n" + 
            "| | | | | | | +-<Expression> ::= <Add Exp>\r\n" + 
            "| | | | | | | | +-<Add Exp> ::= <Mult Exp>\r\n" + 
            "| | | | | | | | | +-<Mult Exp> ::= <Mult Exp> '*' <Negate Exp>\r\n" + 
            "| | | | | | | | | | +-<Mult Exp> ::= <Negate Exp>\r\n" + 
            "| | | | | | | | | | | +-<Negate Exp> ::= <Value>\r\n" + 
            "| | | | | | | | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | | | | | | | +-3\r\n" + 
            "| | | | | | | | | | +-*\r\n" + 
            "| | | | | | | | | | +-<Negate Exp> ::= <Value>\r\n" + 
            "| | | | | | | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | | | | | | +-4\r\n" + 
            "| | | | | | | +-)\r\n" + 
            "| +-<Statements> ::= <Statement>\r\n" + 
            "| | +-<Statement> ::= display <Expression>\r\n" + 
            "| | | +-Display\r\n" + 
            "| | | +-<Expression> ::= <Add Exp>\r\n" + 
            "| | | | +-<Add Exp> ::= <Add Exp> '+' <Mult Exp>\r\n" + 
            "| | | | | +-<Add Exp> ::= <Mult Exp>\r\n" + 
            "| | | | | | +-<Mult Exp> ::= <Negate Exp>\r\n" + 
            "| | | | | | | +-<Negate Exp> ::= <Value>\r\n" + 
            "| | | | | | | | +-<Value> ::= StringLiteral\r\n" + 
            "| | | | | | | | | +-\"Answer: \"\r\n" + 
            "| | | | | +-+\r\n" + 
            "| | | | | +-<Mult Exp> ::= <Negate Exp>\r\n" + 
            "| | | | | | +-<Negate Exp> ::= <Value>\r\n" + 
            "| | | | | | | +-<Value> ::= Id\r\n" + 
            "| | | | | | | | +-a\r\n";

        GOLDParser parser = new GOLDParser();
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        parser.setGenerateTree(true);
        parser.setTrimReductions(false);
        parser.parseSourceStatements(new StringReader(source));
        assertEquals("wrong tree", expected, parser.getParseTree());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testGetParseTree_parse_called_trim() throws Exception {
        // Sample source to parse
        String source = "Assign a = 1 + 2 / (3 * 4)\r\n" + "Display \"Answer: \" + a ";

        // This should match the output of the VB reference implementation
        String expected = 
            "+-<Statements> ::= <Statement> <Statements>\r\n" + 
            "| +-<Statement> ::= assign Id '=' <Expression>\r\n" + 
            "| | +-Assign\r\n" + 
            "| | +-a\r\n" + 
            "| | +-=\r\n" + 
            "| | +-<Add Exp> ::= <Add Exp> '+' <Mult Exp>\r\n" + 
            "| | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | +-1\r\n" + 
            "| | | +-+\r\n" + 
            "| | | +-<Mult Exp> ::= <Mult Exp> '/' <Negate Exp>\r\n" + 
            "| | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | +-2\r\n" + 
            "| | | | +-/\r\n" + 
            "| | | | +-<Value> ::= '(' <Expression> ')'\r\n" + 
            "| | | | | +-(\r\n" + 
            "| | | | | +-<Mult Exp> ::= <Mult Exp> '*' <Negate Exp>\r\n" + 
            "| | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | +-3\r\n" + 
            "| | | | | | +-*\r\n" + 
            "| | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | +-4\r\n" + 
            "| | | | | +-)\r\n" + 
            "| +-<Statement> ::= display <Expression>\r\n" + 
            "| | +-Display\r\n" + 
            "| | +-<Add Exp> ::= <Add Exp> '+' <Mult Exp>\r\n" + 
            "| | | +-<Value> ::= StringLiteral\r\n" + 
            "| | | | +-\"Answer: \"\r\n" + 
            "| | | +-+\r\n" + 
            "| | | +-<Value> ::= Id\r\n" + 
            "| | | | +-a\r\n";

        GOLDParser parser = new GOLDParser();
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        parser.setGenerateTree(true);
        parser.setTrimReductions(true);
        parser.parseSourceStatements(new StringReader(source));
        assertEquals("wrong tree", expected, parser.getParseTree());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testGetParseTree_parse_str_called_trim() throws Exception {
        // Sample source to parse
        String source = "Assign a = 1 + 2 / (3 * 4)\r\n" + "Display \"Answer: \" + a ";

        // This should match the output of the VB reference implementation
        String expected = 
            "+-<Statements> ::= <Statement> <Statements>\r\n" + 
            "| +-<Statement> ::= assign Id '=' <Expression>\r\n" + 
            "| | +-Assign\r\n" + 
            "| | +-a\r\n" + 
            "| | +-=\r\n" + 
            "| | +-<Add Exp> ::= <Add Exp> '+' <Mult Exp>\r\n" + 
            "| | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | +-1\r\n" + 
            "| | | +-+\r\n" + 
            "| | | +-<Mult Exp> ::= <Mult Exp> '/' <Negate Exp>\r\n" + 
            "| | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | +-2\r\n" + 
            "| | | | +-/\r\n" + 
            "| | | | +-<Value> ::= '(' <Expression> ')'\r\n" + 
            "| | | | | +-(\r\n" + 
            "| | | | | +-<Mult Exp> ::= <Mult Exp> '*' <Negate Exp>\r\n" + 
            "| | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | +-3\r\n" + 
            "| | | | | | +-*\r\n" + 
            "| | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | +-4\r\n" + 
            "| | | | | +-)\r\n" + 
            "| +-<Statement> ::= display <Expression>\r\n" + 
            "| | +-Display\r\n" + 
            "| | +-<Add Exp> ::= <Add Exp> '+' <Mult Exp>\r\n" + 
            "| | | +-<Value> ::= StringLiteral\r\n" + 
            "| | | | +-\"Answer: \"\r\n" + 
            "| | | +-+\r\n" + 
            "| | | +-<Value> ::= Id\r\n" + 
            "| | | | +-a\r\n";

        GOLDParser parser = new GOLDParser();
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        parser.setGenerateTree(true);
        parser.setTrimReductions(true);
        parser.parseSourceStatements(source);
        assertEquals("wrong tree", expected, parser.getParseTree());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testParse_with_group_error() throws Exception {
        // Sample source to parse
        String source = "/* unclosed comment\r\n";

        GOLDParser parser = new GOLDParser();
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        parser.setTrimReductions(true);
        
        assertFalse("should have parse error", parser.parseSourceStatements(source));
        assertEquals("wrong msg", "Runaway group (no closing group terminator found). Last position line 1, column 1.", 
            parser.getErrorMessage());
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testParse_with_internal_error() throws Exception {
        // Sample source to parse
        String source = "assign i = 1\r\n";
        
        GOLDParserForTesting parser = new GOLDParserForTesting();
        parser.setup(new File("src/test/resources/parser/Simple2.cgt"));
        parser.setTrimReductions(true);
        
        // artificially alter the LRStates so an internal error is generated
        LRStateList states = parser.getStates();
        for (int i = 0; i < states.size(); i++) {
            LRState state = states.get(i);
            for (int j = 0; j < state.size(); j++) {
                LRAction action = state.get(j);
                if (action.getType().name().equals("GOTO")) {
                    LRAction newAction = new LRAction(action.getSymbol(), action.getType(), 5);
                    state.set(j, newAction);
                }
            }
        }

        assertFalse("should have parse error", parser.parseSourceStatements(source));
        assertEquals("wrong msg", "Internal processing error. Last position line 2, column 1.", 
                parser.getErrorMessage());
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testValidateHandlerExists() throws Exception {
        GOLDParser parser = new GOLDParser(new File("src/test/resources/parser/Simple2.cgt"), "com.creativewidgetworks.goldparser.simple2", true);

        // Rule handler exists
        Symbol symbol = new Symbol("Value", SymbolType.NON_TERMINAL, 0);
        Production production = new Production(symbol, 0);
        production.getHandle().add(new Symbol("StringLiteral", SymbolType.CONTENT, 0));
        parser.validateHandlerExists(production);

        // Rule handler doesn't exist
        production = new Production(symbol, 0);
        production.getHandle().add(new Symbol("DateLiteral", SymbolType.CONTENT, 0));
        try {
            parser.validateHandlerExists(production);
            fail("expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "No rule handler for rule <Value> ::= DateLiteral", pe.getMessage());
        }
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testCreateInstance() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting(new File("src/test/resources/parser/Simple2.cgt"), "com.creativewidgetworks.goldparser.simple2", true);

        // Rule handler exists
        Reduction reduction = parser.createInstance("<Value> ::= StringLiteral");
        assertEquals("wrong handler", "StringLiteral", reduction.getClass().getSimpleName());

        // Rule handler doesn't exist
        try {
            reduction = parser.createInstance("<Value> ::= DateLiteral");
            fail("expected ParserException");
        } catch (ParserException pe) {
            assertEquals("wrong msg", "No rule handler for rule <Value> ::= DateLiteral", pe.getMessage());
        }
    }

    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testGenerateTree() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting(new File("src/test/resources/parser/Simple2.cgt"), "com.creativewidgetworks.goldparser.simple2", true);
        
        // Sample source to parse
        String source = "assign i = 1\r\n";
        
        parser.setGenerateTree(false);
        parser.setTrimReductions(false);
        parser.parseSourceStatements(source);
        assertEquals("wrong msg", "Parse tree is not available. Did you set generateTree(true)?", parser.getParseTree());
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testValidHandlersExist() throws Exception {
        GOLDParserForTesting parser = new GOLDParserForTesting(new File("src/test/resources/parser/Simple2.cgt"), "com.creativewidgetworks.goldparser.simple2", true);
        assertEquals("expected empty list", 0, parser.validateHandlersExist().size());
        
        // There will be missing rules between Simple2 and Simple3 grammars
        parser = new GOLDParserForTesting(new File("src/test/resources/parser/Simple2.cgt"), "com.creativewidgetworks.goldparser.simple3", true);
        
        String[] expected = {
            "No rule handler for rule <Statements> ::= <Statement> <Statements>",
            "No rule handler for rule <Statements> ::= <Statement>",
            "No rule handler for rule <Statement> ::= display <Expression>",
            "No rule handler for rule <Statement> ::= display <Expression> read Id",
            "No rule handler for rule <Statement> ::= assign Id '=' <Expression>"};
        List<String> errors = parser.validateHandlersExist();
        assertEquals("expected errors", expected.length, errors.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals("wrong message at index " + i, expected[i], errors.get(i));
        }
    }

    /*----------------------------------------------------------------------------*/
    
    private List<Token> filterByIndentTerminals(List<Token> tokens) {
        List<Token> list = new ArrayList<Token>();
        for (Token token : tokens) {
            if (token.getName().equals(GOLDParser.VT_INDENT_INCREASE) || token.getName().equals(GOLDParser.VT_INDENT_DECREASE)) {
                list.add(token);
            }    
        }
        return list;
    }
    
    private Token makeIndentToken(String name, int line, int column, Object indentLevels) {
        Position position = new Position(line, column);
        return new Token(new Symbol(name, SymbolType.CONTENT, 0), indentLevels, position);
    }
    
    @Test
    public void testIndentVirtualTerminals_position() throws Exception {
        String source = 
            "if (1==1):\r\n" + 
            "  print 'true - first line'\r\n" + 
            "  print 'true - second line'\r\n" + 
            "  if (2==2):\r\n" + 
            "    print 'true - level 2'\r\n" + 
            "    print 'true - level 2'\r\n" + 
            "  if (3==3):\r\n" + 
            "    print 'true - level 3'\r\n" +
            "  print 'about done'\r\n" +
            "print 'always should print'";
        
        GOLDParserForTesting parser = new GOLDParserForTesting();
        parser.setup(new File("src/test/resources/parser/simplepy.egt"));
        parser.setGenerateTree(true);
        parser.setTrimReductions(true);
        parser.parseSourceStatements(source);
        
        Token[] expected = { 
            //                                             parse indent  
            //                       name                  r  c  levels
            makeIndentToken(GOLDParser.VT_INDENT_INCREASE, 2, 3,  "3,1"),
            makeIndentToken(GOLDParser.VT_INDENT_INCREASE, 5, 5,  "5,3,1"),
            makeIndentToken(GOLDParser.VT_INDENT_DECREASE, 7, 3,  "3,1"),
            makeIndentToken(GOLDParser.VT_INDENT_INCREASE, 8, 5,  "5,3,1"),
            makeIndentToken(GOLDParser.VT_INDENT_DECREASE, 9, 3,  "3,1"),
            makeIndentToken(GOLDParser.VT_INDENT_DECREASE, 10,1,  "1"),
        };
        
        List<Token> tokens = filterByIndentTerminals(parser.getTokensRead());
        assertEquals("wrong number of tokens", expected.length, tokens.size());
        for (int i = 0; i < expected.length; i++) {
            String expectedData = expected[i].getData() == null ? null : expected[i].getData().toString();
            String actualData = tokens.get(i).getData() == null ? null : tokens.get(i).getData().toString();
            Position expectedPosition = expected[i].getPosition();
            Position actualPosition = tokens.get(i).getPosition();
            assertEquals("row " + i + ":type", expected[i].getName(), tokens.get(i).getName());
            assertEquals("row " + i + ":indentLevels", expectedData, actualData);
            assertEquals("row " + i + ":line", expectedPosition.getLine(), actualPosition.getLine());
            assertEquals("row " + i + ":column", expectedPosition.getColumn(), actualPosition.getColumn());
        }
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testIndentVirtualTerminals_tree() throws Exception {
        String source = 
            "if (1==1):\r\n" + 
            "  print 'true - first line'\r\n" + 
            "  print 'true - second line'\r\n" + 
            "  if (2==2):\r\n" + 
            "      print 'true - level 2'\r\n" + 
            "      print 'true - level 2'\r\n" + 
            "      if (3==3):\r\n" + 
            "        print 'true - level 3'\r\n" + 
            "print 'always should print'";
        
        String expected = 
            "+-<Statements> ::= <StatementOrBlock> <Statements>\r\n" + 
            "| +-<Statement> ::= if <Expression> ':' <StatementOrBlock>\r\n" + 
            "| | +-if\r\n" + 
            "| | +-<Value> ::= '(' <Expression> ')'\r\n" + 
            "| | | +-(\r\n" + 
            "| | | +-<Expression> ::= <Expression> '==' <Add Exp>\r\n" + 
            "| | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | +-1\r\n" + 
            "| | | | +-==\r\n" + 
            "| | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | +-1\r\n" + 
            "| | | +-)\r\n" + 
            "| | +-:\r\n" + 
            "| | +-<StatementOrBlock> ::= IndentIncrease <Statements> IndentDecrease\r\n" + 
            "| | | +-3,1\r\n" + 
            "| | | +-<Statements> ::= <StatementOrBlock> <Statements>\r\n" + 
            "| | | | +-<Statement> ::= print <Expression>\r\n" + 
            "| | | | | +-print\r\n" + 
            "| | | | | +-<Value> ::= StringLiteral\r\n" + 
            "| | | | | | +-'true - first line'\r\n" + 
            "| | | | +-<Statements> ::= <StatementOrBlock> <Statements>\r\n" + 
            "| | | | | +-<Statement> ::= print <Expression>\r\n" + 
            "| | | | | | +-print\r\n" + 
            "| | | | | | +-<Value> ::= StringLiteral\r\n" + 
            "| | | | | | | +-'true - second line'\r\n" + 
            "| | | | | +-<Statement> ::= if <Expression> ':' <StatementOrBlock>\r\n" + 
            "| | | | | | +-if\r\n" + 
            "| | | | | | +-<Value> ::= '(' <Expression> ')'\r\n" + 
            "| | | | | | | +-(\r\n" + 
            "| | | | | | | +-<Expression> ::= <Expression> '==' <Add Exp>\r\n" + 
            "| | | | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | | | +-2\r\n" + 
            "| | | | | | | | +-==\r\n" + 
            "| | | | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | | | +-2\r\n" + 
            "| | | | | | | +-)\r\n" + 
            "| | | | | | +-:\r\n" + 
            "| | | | | | +-<StatementOrBlock> ::= IndentIncrease <Statements> IndentDecrease\r\n" + 
            "| | | | | | | +-7,3,1\r\n" + 
            "| | | | | | | +-<Statements> ::= <StatementOrBlock> <Statements>\r\n" + 
            "| | | | | | | | +-<Statement> ::= print <Expression>\r\n" + 
            "| | | | | | | | | +-print\r\n" + 
            "| | | | | | | | | +-<Value> ::= StringLiteral\r\n" + 
            "| | | | | | | | | | +-'true - level 2'\r\n" + 
            "| | | | | | | | +-<Statements> ::= <StatementOrBlock> <Statements>\r\n" + 
            "| | | | | | | | | +-<Statement> ::= print <Expression>\r\n" + 
            "| | | | | | | | | | +-print\r\n" + 
            "| | | | | | | | | | +-<Value> ::= StringLiteral\r\n" + 
            "| | | | | | | | | | | +-'true - level 2'\r\n" + 
            "| | | | | | | | | +-<Statement> ::= if <Expression> ':' <StatementOrBlock>\r\n" + 
            "| | | | | | | | | | +-if\r\n" + 
            "| | | | | | | | | | +-<Value> ::= '(' <Expression> ')'\r\n" + 
            "| | | | | | | | | | | +-(\r\n" + 
            "| | | | | | | | | | | +-<Expression> ::= <Expression> '==' <Add Exp>\r\n" + 
            "| | | | | | | | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | | | | | | | +-3\r\n" + 
            "| | | | | | | | | | | | +-==\r\n" + 
            "| | | | | | | | | | | | +-<Value> ::= NumberLiteral\r\n" + 
            "| | | | | | | | | | | | | +-3\r\n" + 
            "| | | | | | | | | | | +-)\r\n" + 
            "| | | | | | | | | | +-:\r\n" + 
            "| | | | | | | | | | +-<StatementOrBlock> ::= IndentIncrease <Statements> IndentDecrease\r\n" + 
            "| | | | | | | | | | | +-9,7,3,1\r\n" + 
            "| | | | | | | | | | | +-<Statement> ::= print <Expression>\r\n" + 
            "| | | | | | | | | | | | +-print\r\n" + 
            "| | | | | | | | | | | | +-<Value> ::= StringLiteral\r\n" + 
            "| | | | | | | | | | | | | +-'true - level 3'\r\n" + 
            "| | | | | | | | | | | +-1\r\n" + 
            "| | | | | | | +-3,1\r\n" + 
            "| | | +-7,3,1\r\n" + 
            "| +-<Statement> ::= print <Expression>\r\n" + 
            "| | +-print\r\n" + 
            "| | +-<Value> ::= StringLiteral\r\n" + 
            "| | | +-'always should print'\r\n";
        
        GOLDParser parser = new GOLDParser();
        parser.setup(new File("src/test/resources/parser/simplepy.egt"));
        parser.setGenerateTree(true);
        parser.setTrimReductions(true);
        parser.parseSourceStatements(source);
        assertEquals("wrong tree", expected, parser.getParseTree());   
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test
    public void testPosition() throws Exception {
        String source = 
            "   if (1==1):\r\n" + 
            "     print 'true - first line'\r\n" + 
            "     print 'true - second line'\r\n" + 
            "print 'always should print'";
        
        GOLDParserForTesting parser = new GOLDParserForTesting();
        parser.setup(new File("src/test/resources/parser/simplepy.egt"));
        parser.setGenerateTree(true);
        parser.setTrimReductions(true);
        parser.parseSourceStatements(source);
    
        Position[] expected = new Position[] {
            new Position(1,1), new Position(1,4), new Position(1,6), new Position(1,7), new Position(1,8),
            new Position(1,9), new Position(1,11), new Position(1,12), new Position(1,13), new Position(1,14),
            new Position(2,6), new Position(2,6), new Position(2,11), new Position(2,12), new Position(2,31),
            new Position(3,6), new Position(3,11), new Position(3,12), new Position(3,32),
            new Position(4,1), new Position(4,1), new Position(4,6), new Position(4,7), new Position(4,28),
        };
    
        List<Token> tokens = parser.getTokensRead();
        
        assertEquals("wrong number of tokens", expected.length, tokens.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals("token " + i + ":line", expected[i].getLine(), tokens.get(i).getPosition().getLine());
            assertEquals("token " + i + ":column", expected[i].getColumn(), tokens.get(i).getPosition().getColumn());
        }
    }

}
