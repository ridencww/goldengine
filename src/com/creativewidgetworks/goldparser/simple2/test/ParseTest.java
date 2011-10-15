package com.creativewidgetworks.goldparser.simple2.test;

import org.junit.Test;

import com.creativewidgetworks.goldparser.util.GOLDParserTestCase;

public class ParseTest extends GOLDParserTestCase {

    @Test
    public void testMultipleParses() throws Exception {
        // Verify that same parser instance can be used to parse multiple source streams
        GOLDParserForTesting parser = new GOLDParserForTesting();
        String[] actual = executeProgram(parser, "display \"Hello\"\r\n");
        validateLines(makeExpected("Hello"), actual);
        actual = executeProgram(parser, "display \"World\"\r\n");
        validateLines(makeExpected("World"), actual);
    }
    
}
