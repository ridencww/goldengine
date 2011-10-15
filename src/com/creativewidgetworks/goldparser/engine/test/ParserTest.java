package com.creativewidgetworks.goldparser.engine.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.engine.CharacterSetList;
import com.creativewidgetworks.goldparser.engine.FAStateList;
import com.creativewidgetworks.goldparser.engine.GroupList;
import com.creativewidgetworks.goldparser.engine.LRStateList;
import com.creativewidgetworks.goldparser.engine.Parser;
import com.creativewidgetworks.goldparser.engine.Production;
import com.creativewidgetworks.goldparser.engine.ProductionList;
import com.creativewidgetworks.goldparser.engine.Symbol;
import com.creativewidgetworks.goldparser.engine.SymbolList;
import com.creativewidgetworks.goldparser.engine.Token;
import com.creativewidgetworks.goldparser.engine.enums.ParseMessage;

public class ParserTest extends TestCase {

    public static final String ABOUT_TEXT = "GOLD Parser Engine - Version 5.0";
    public static final String GENERATED_BY = "GOLD Parser Builder 5.0 RC";
    public static final String GENERATED_ON = "2011-05-29 04:00";
    
    public static final int START_SYMBOL = 39;
    
    /**
     * Test class that provides access to the protected members without getters and setters. These
     * fields are meant not to be shared, but must be exposed for testing.  In the future, 
     * reflection can be used to access these fields from tests and then they can be marked
     * private once again.
     */
    public class TestParser extends Parser {
        public SymbolList getSymbols() { return symbolTable; }
        public ProductionList getProductions() { return productionTable; }
        public FAStateList getFAStates() { return dfa; }
        public LRStateList getLRStates() { return lrStates; }
        public CharacterSetList getCharacterSetList() { return characterSetTable; }
        public GroupList getGroups() { return groupTable; }
        
        @Override
        public Token getCurrentToken() {
            return super.getCurrentToken();
        }

        @Override
        public Reader getSource() {
            return super.getSource();
        }
        
        @Override
        public boolean isVersion1Format() {
            return super.isVersion1Format();
        }
        
        @Override
        protected boolean loadTables(File file) throws IOException {
            return super.loadTables(file);
        }

        @Override
        protected boolean loadTables(InputStream input) throws IOException {
            return super.loadTables(input);
        }

        @Override
        protected boolean open(File sourceFile) throws IOException {
            return super.open(sourceFile);
        }

        @Override
        protected boolean open(String sourceStatements) {
            return super.open(sourceStatements);
        }

        @Override
        protected boolean open(Reader reader) {
            return super.open(reader);
        }
        
        @Override
        public ParseMessage parse() {
            return super.parse();
        }
    }
    
    /*----------------------------------------------------------------------------*/

    /**
     * Helper function to build a string that can be used for comparison to expected values.
     * @param List<Symbol>
     * @return String
     */
    private String handleVals(List<Symbol> list) {
        StringBuilder sb = new StringBuilder();
        for (Symbol symbol : list) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(symbol.getName());
        }
        return sb.toString();
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testAbout() throws Exception {
        Parser parser = new Parser();
        assertEquals("wrong msg", ABOUT_TEXT, parser.about());
    }
    
    /*----------------------------------------------------------------------------*/
    /* VERSION 1.0 CGT TESTS                                                       */
    /*----------------------------------------------------------------------------*/

    @Test
    public void testLoadTables_File_CGT() throws Exception {
        TestParser parser = new TestParser();
        assertTrue("should have loaded tables", parser.loadTables(new File("grammars/Simple2.cgt")));
        validateLoadTable(parser);
    }

    /*----------------------------------------------------------------------------*/

    @Test
    public void testLoadTables_File_EGT() throws Exception {
        TestParser parser = new TestParser();
        assertTrue("should have loaded tables", parser.loadTables(new File("grammars/Simple2.egt")));
        validateLoadTable(parser);
    }
    
    /*----------------------------------------------------------------------------*/

    @Test
    public void testLoadTables_InputStream() throws Exception {
        TestParser parser = new TestParser();
        InputStream is = new FileInputStream("grammars/Simple2.cgt");
        assertTrue("should have loaded tables", parser.loadTables(is));
        validateLoadTable(parser);
    }    
    
    /*----------------------------------------------------------------------------*/

    /**
     * Common validation for Load Tables tests
     */
    private void validateLoadTable(TestParser parser) {
        // Grammar attributes
        // - present in 1.0 and 5.0 formats
        assertEquals("about", "This is a very simple grammar designed for use in examples", parser.getAttribute(Parser.ABOUT));
        assertEquals("author", "Devin Cook", parser.getAttribute(Parser.AUTHOR));
        assertEquals("name", "Simple", parser.getAttribute(Parser.NAME));
        assertEquals("version", "2.0", parser.getAttribute(Parser.VERSION));
        if (parser.isVersion1Format()) {
            // - present in 1.0 format
            assertEquals("case-sensitive", "false", parser.getAttribute(Parser.CASE_SENSITIVE));
            assertEquals("start_symbol", "39", parser.getAttribute(Parser.START_SYMBOL));
        } else {
            // - present in 5.0+ formats
            assertEquals("character-mapping", "Windows-1252", parser.getAttribute(Parser.CHARACTER_MAPPING));
            assertEquals("character-set", "Unicode", parser.getAttribute(Parser.CHARACTER_SET));
            assertEquals("generated_by", "GOLD Parser Builder 5.0 RC 2", parser.getAttribute(Parser.GENERATED_BY));
            assertEquals("generated_on", "2011-10-11 22:37", parser.getAttribute(Parser.GENERATED_DATE));
        }
        
        // Symbol table
        SymbolList symbols = parser.getSymbols();
        assertNotNull("expected symbols");
        String[][] expected = {
            {"0", "EOF", "END"},
            {"1", "Error", "ERROR"},
            {"2", "Comment", "NOISE"},
            {"3", "NewLine", "NOISE"},
            {"4", "Whitespace", "NOISE"},
            {"5", "*/", "GROUP_END"},
            {"6", "/*", "GROUP_START"},
            {"7", "//", parser.isVersion1Format() ? "COMMENT_LINE" : "GROUP_START"},
            {"8", "-", "CONTENT"},
            {"9", "&", "CONTENT"},
            {"10", "(", "CONTENT"},
            {"11", ")", "CONTENT"},
            {"12", "*", "CONTENT"},
            {"13", "/", "CONTENT"},
            {"14", "+", "CONTENT"},
            {"15", "<", "CONTENT"},
            {"16", "<=", "CONTENT"},
            {"17", "<>", "CONTENT"},
            {"18", "=", "CONTENT"},
            {"19", "==", "CONTENT"},
            {"20", ">", "CONTENT"},
            {"21", ">=", "CONTENT"},
            {"22", "assign", "CONTENT"},
            {"23", "display", "CONTENT"},
            {"24", "do", "CONTENT"},
            {"25", "else", "CONTENT"},
            {"26", "end", "CONTENT"},
            {"27", "Id", "CONTENT"},
            {"28", "if", "CONTENT"},
            {"29", "NumberLiteral", "CONTENT"},
            {"30", "read", "CONTENT"},
            {"31", "StringLiteral", "CONTENT"},
            {"32", "then", "CONTENT"},
            {"33", "while", "CONTENT"},
            {"34", "Add Exp", "NON_TERMINAL"},
            {"35", "Expression", "NON_TERMINAL"},
            {"36", "Mult Exp", "NON_TERMINAL"},
            {"37", "Negate Exp", "NON_TERMINAL"},
            {"38", "Statement", "NON_TERMINAL"},
            {"39", "Statements", "NON_TERMINAL"},
            {"40", "Value", "NON_TERMINAL"},
        };
        assertEquals("wrong symbol count", expected.length, symbols.size());
        for (int i = 0; i < expected.length; i++) {
            Symbol symbol = symbols.get(i);
            assertEquals("row " + i + ": index", Integer.parseInt(expected[i][0]), symbol.getTableIndex());
            assertEquals("row " + i + ": name", expected[i][1], symbol.getName());
            assertEquals("row " + i + ": type", expected[i][2], symbol.getType().name());
        }
        
        // Productions
        ProductionList productions = parser.getProductions();
        assertNotNull("expected productions");
        expected = new String[][] {
            {"0", "Statements", "2", "Statement Statements"},
            {"1", "Statements", "1", "Statement"},
            {"2", "Statement", "2", "display Expression"},
            {"3", "Statement", "4", "display Expression read Id"},
            {"4", "Statement", "4", "assign Id = Expression"},
            {"5", "Statement", "5", "while Expression do Statements end"},
            {"6", "Statement", "5", "if Expression then Statements end"},
            {"7", "Statement", "7", "if Expression then Statements else Statements end"},
            {"8", "Expression", "3", "Expression > Add Exp"},
            {"9", "Expression", "3", "Expression < Add Exp"},
            {"10", "Expression", "3", "Expression <= Add Exp"},
            {"11", "Expression", "3", "Expression >= Add Exp"},
            {"12", "Expression", "3", "Expression == Add Exp"},
            {"13", "Expression", "3", "Expression <> Add Exp"},
            {"14", "Expression", "1", "Add Exp"},
            {"15", "Add Exp", "3", "Add Exp + Mult Exp"},
            {"16", "Add Exp", "3", "Add Exp - Mult Exp"},
            {"17", "Add Exp", "3", "Add Exp & Mult Exp"},
            {"18", "Add Exp", "1", "Mult Exp"},
            {"19", "Mult Exp", "3", "Mult Exp * Negate Exp"},
            {"20", "Mult Exp", "3", "Mult Exp / Negate Exp"},
            {"21", "Mult Exp", "1", "Negate Exp"},
            {"22", "Negate Exp", "2", "- Value"},
            {"23", "Negate Exp", "1", "Value"},
            {"24", "Value", "1", "Id"},
            {"25", "Value", "1", "StringLiteral"},
            {"26", "Value", "1", "NumberLiteral"},
            {"27", "Value", "3", "( Expression )"},
        };        
        for (int i = 0; i < expected.length; i++) {
            Production production = productions.get(i);
            assertEquals("row " + i + ": index", Integer.parseInt(expected[i][0]), production.getTableIndex());
            assertEquals("row " + i + ": head", expected[i][1], production.getHead().getName());
            assertEquals("row " + i + ": handle size", Integer.parseInt(expected[i][2]), production.getHandle().size());
            assertEquals("row " + i + ": handle vals", expected[i][3], handleVals(production.getHandle()));
        }
        
        // CharacterSet
        CharacterSetList sets = parser.getCharacterSetList();
        assertNotNull("expected character sets");
        assertEquals("wrong set size", 53, sets.size());
        // TODO - assert rows           
        
        // DFA
        FAStateList faStates = parser.getFAStates();
        assertNotNull("expected FA states");
        assertEquals("wrong FA states size", 70, faStates.size());
        // TODO - assert rows        
        
        // LR states
        LRStateList lrStates = parser.getLRStates();
        assertNotNull("expected LR states");
        assertEquals("wrong LR states size", 59, lrStates.size());
        // TODO - assert rows

        // Groups
        GroupList groups = parser.getGroups();
        assertNotNull("expected groups", groups);
        
        // TODO
        // will be null for 1.0 and 0 for 5.0
        //assertEquals("wrong groups size", 0, groups.size());
    }

    /*----------------------------------------------------------------------------*/

    @Test 
    public void testParse_simple() throws Exception {
        String src = "display 1 + (5 * 3)";
        
        TestParser parser = new TestParser();
        parser.setTrimReductions(true);
        assertTrue("should have loaded tables", parser.loadTables(new File("grammars/Simple2.cgt")));
        assertTrue("should have opened source to parse", parser.open(src));

        Object[][] expected = {
            {ParseMessage.TOKEN_READ, "display"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "1"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "+"},
            {ParseMessage.REDUCTION, "+"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "("},
            {ParseMessage.TOKEN_READ, "5"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "*"},
            {ParseMessage.REDUCTION, "*"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "3"},
            {ParseMessage.TOKEN_READ, ")"},
            {ParseMessage.REDUCTION, ")"},
            {ParseMessage.REDUCTION, ")"},
            {ParseMessage.TOKEN_READ, ""},
            {ParseMessage.REDUCTION, ""},
            {ParseMessage.REDUCTION, ""},
            {ParseMessage.REDUCTION, ""},
            {ParseMessage.ACCEPT, ""},      
        };

        for (int i = 0; i < expected.length; i++) {
            assertEquals("row " + i + " token type", expected[i][0], parser.parse());
            assertEquals("row " + i + " token", expected[i][1], parser.getCurrentToken().asString());
        }
    }

    /*----------------------------------------------------------------------------*/

    @Test 
    public void testParse_simple_with_comments() throws Exception {
        String src = 
            "/* \r\n" +
            " * Comment\r\n" +
            "*/\r\n" +
            "\r\n" +
            "// Another comment\r\n" +
            "\r\n" +
            "display 1 + (5 * 3)";
        
        TestParser parser = new TestParser();
        parser.setTrimReductions(true);
        assertTrue("should have loaded tables", parser.loadTables(new File("grammars/Simple2.cgt")));
        assertTrue("should have opened source to parse", parser.open(src));

        Object[][] expected = {
            {ParseMessage.TOKEN_READ, "/* \r\n * Comment\r\n*/"},    
            {ParseMessage.TOKEN_READ, "\r\n"},  // CRLF after the comment
            {ParseMessage.TOKEN_READ, "\r\n"},
            {ParseMessage.TOKEN_READ, "// Another comment"},
            {ParseMessage.TOKEN_READ, "\r\n"}, // CRLF after line comment
            {ParseMessage.TOKEN_READ, "\r\n"},
            {ParseMessage.TOKEN_READ, "display"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "1"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "+"},
            {ParseMessage.REDUCTION, "+"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "("},
            {ParseMessage.TOKEN_READ, "5"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "*"},
            {ParseMessage.REDUCTION, "*"},
            {ParseMessage.TOKEN_READ, " "},
            {ParseMessage.TOKEN_READ, "3"},
            {ParseMessage.TOKEN_READ, ")"},
            {ParseMessage.REDUCTION, ")"},
            {ParseMessage.REDUCTION, ")"},
            {ParseMessage.TOKEN_READ, ""},
            {ParseMessage.REDUCTION, ""},
            {ParseMessage.REDUCTION, ""},
            {ParseMessage.REDUCTION, ""},
            {ParseMessage.ACCEPT, ""},      
        };

        for (int i = 0; i < expected.length; i++) {
            assertEquals("row " + i + " token type", expected[i][0], parser.parse());
            assertEquals("row " + i + " token", expected[i][1], parser.getCurrentToken().asString());
        }
    }

    /*----------------------------------------------------------------------------*/
    
    @Test 
    public void testParse_simple_with_nested_comments() throws Exception {
        String src = 
            "/* \r\n" +
            " * // Test\r\n" +
            " * /* Hi */ Comment\r\n" +
            "*/\r\n" +
            "\r\n" +
            "// Another comment\r\n" +
            "\r\n" +
            "display 1 + (5 * 3)";
        
        TestParser parser = new TestParser();
        parser.setTrimReductions(true);
        assertTrue("should have loaded tables", parser.loadTables(new File("grammars/Simple2a.egt")));
        assertTrue("should have opened source to parse", parser.open(src));
        
        Object[][] expected = {
                {ParseMessage.TOKEN_READ, "/* \r\n * // Test\r\n * /* Hi */ Comment\r\n*/"},    
                {ParseMessage.TOKEN_READ, "\r\n"},  // CRLF after the comment
                {ParseMessage.TOKEN_READ, "\r\n"},
                {ParseMessage.TOKEN_READ, "// Another comment"},
                {ParseMessage.TOKEN_READ, "\r\n"}, // CRLF after line comment
                {ParseMessage.TOKEN_READ, "\r\n"},
                {ParseMessage.TOKEN_READ, "display"},
                {ParseMessage.TOKEN_READ, " "},
                {ParseMessage.TOKEN_READ, "1"},
                {ParseMessage.TOKEN_READ, " "},
                {ParseMessage.TOKEN_READ, "+"},
                {ParseMessage.REDUCTION, "+"},
                {ParseMessage.TOKEN_READ, " "},
                {ParseMessage.TOKEN_READ, "("},
                {ParseMessage.TOKEN_READ, "5"},
                {ParseMessage.TOKEN_READ, " "},
                {ParseMessage.TOKEN_READ, "*"},
                {ParseMessage.REDUCTION, "*"},
                {ParseMessage.TOKEN_READ, " "},
                {ParseMessage.TOKEN_READ, "3"},
                {ParseMessage.TOKEN_READ, ")"},
                {ParseMessage.REDUCTION, ")"},
                {ParseMessage.REDUCTION, ")"},
                {ParseMessage.TOKEN_READ, ""},
                {ParseMessage.REDUCTION, ""},
                {ParseMessage.REDUCTION, ""},
                {ParseMessage.REDUCTION, ""},
                {ParseMessage.ACCEPT, ""},      
        };
        
        for (int i = 0; i < expected.length; i++) {
            assertEquals("row " + i + " token type", expected[i][0], parser.parse());
            assertEquals("row " + i + " token", expected[i][1], parser.getCurrentToken().asString());
        }
    }

    /*----------------------------------------------------------------------------*/

    @Test 
    public void testParse_simple_with_runaway_comment() throws Exception {
        String src = 
            "display 1\r\n" +
            "/* \r\n" +
            " * Comment\r\n";
        
        TestParser parser = new TestParser();
        parser.setTrimReductions(true);
        assertTrue("should have loaded tables", parser.loadTables(new File("grammars/Simple2.cgt")));
        assertTrue("should have opened source to parse", parser.open(src));

        Object[][] expected = {
            {ParseMessage.TOKEN_READ, "display"},    
            {ParseMessage.TOKEN_READ, " "},    
            {ParseMessage.TOKEN_READ, "1"},    
            {ParseMessage.TOKEN_READ, "\r\n"},    
            {ParseMessage.GROUP_ERROR, ""},    
        };

        for (int i = 0; i < expected.length; i++) {
            assertEquals("row " + i + " token type", expected[i][0], parser.parse());
            assertEquals("row " + i + " token", expected[i][1], parser.getCurrentToken().asString());
        }
    }
    
    /*----------------------------------------------------------------------------*/
    
    @Test 
    public void testParse_simple_with_runaway_comment_consumes_entire_program() throws Exception {
        String src = 
            "/* \r\n" +
            " * Comment\r\n" +
            "display 1 + (5 * 3)";
        
        TestParser parser = new TestParser();
        parser.setTrimReductions(true);
        assertTrue("should have loaded tables", parser.loadTables(new File("grammars/Simple2.cgt")));
        assertTrue("should have opened source to parse", parser.open(src));
        
        Object[][] expected = {
                {ParseMessage.GROUP_ERROR, ""},    
        };
        
        for (int i = 0; i < expected.length; i++) {
            assertEquals("row " + i + " token type", expected[i][0], parser.parse());
            assertEquals("row " + i + " token", expected[i][1], parser.getCurrentToken().asString());
        }
    }

    /*----------------------------------------------------------------------------*/

    @Test 
    public void testParse_simple_with_no_starting_comment() throws Exception {
        String src = 
            "display 1\r\n" +
            "*/ \r\n" +
            " * Comment\r\n";
        
        TestParser parser = new TestParser();
        parser.setTrimReductions(true);
        assertTrue("should have loaded tables", parser.loadTables(new File("grammars/Simple2.cgt")));
        assertTrue("should have opened source to parse", parser.open(src));

        Object[][] expected = {
            {ParseMessage.TOKEN_READ, "display"},    
            {ParseMessage.TOKEN_READ, " "},    
            {ParseMessage.TOKEN_READ, "1"},    
            {ParseMessage.TOKEN_READ, "\r\n"},    
            {ParseMessage.TOKEN_READ, "*/"},    
            {ParseMessage.SYNTAX_ERROR, "*/"},    
        };

        for (int i = 0; i < expected.length; i++) {
            assertEquals("row " + i + " token type", expected[i][0], parser.parse());
            assertEquals("row " + i + " token", expected[i][1], parser.getCurrentToken().asString());
        }
    }
    
}
