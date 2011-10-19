package com.creativewidgetworks.goldparser.simple3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.util.FormatHelper;

/**
 * An interpreter for the Simple3 demonstration language that is
 * an extension of the Simple2 language that comes with the GOLD 
 * Parser. Source files can be executed and parse trees can be
 * generated from the input stream.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Simple3 {
    
    private static final String RESOURCE_FILE = "com/creativewidgetworks/goldparser/simple3/simple3";
    
    /**
     * Formats rule handler messages using the simple3.properties resource file
     * @param message key (e.g., error.syntax)
     * @param parameters that will be inserted into any message placeholder entries (0}...
     * @return formated string.
     */
    public static String formatMessage(String message, Object... parameters) {
        return FormatHelper.formatMessage(RESOURCE_FILE, message, parameters);
    }

    /*----------------------------------------------------------------------------*/

    /**
     * Executes a Simple3 program
     * @param sourceCode to interpret
     * @param wantTree true if the parse tree should be generated
     * @return parse tree if generate parse tree open is set
     */
    public String executeProgram(String sourceCode, boolean wantTree) {
        // Use the compiled grammar file inside the jar
        GOLDParser parser = new GOLDParser(
            getClass().getResourceAsStream("Simple3.cgt"), // compiled grammar table
            "com.creativewidgetworks.goldparser.simple3",  // rule handler package
            true);                                         // trim reductions
        
        // Controls whether or not a parse tree is returned or the program executed.
        parser.setGenerateTree(wantTree);
        
        String tree = null;
        try {
            // Parse the source statements to see if it is syntactically correct
            boolean parsedWithoutError = parser.parseSourceStatements(sourceCode);

            // Holds the parse tree if setGenerateTree(true) was called
            tree = parser.getParseTree();
            
            // Either execute the code or print any error message
            if (parsedWithoutError) {
                parser.getCurrentReduction().execute();
            } else {
                System.out.println(parser.getErrorMessage());
            }
        } catch (ParserException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return tree;
    }

    /*----------------------------------------------------------------------------*/
    
    /**
     * Load a source file to be interpreted by Simple3.  
     * @param filename of Simple3 source file
     * @return source code to be interpreted
     * @throws IOException 
     */
    public String loadSourceFile(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[(int)file.length()];
        fis.read(buf);
        fis.close();
        return new String(buf);
    }
   
    /*----------------------------------------------------------------------------*/
    
    public static void main(String[] args) throws Exception {
        
//        args = new String[1];
//        args[0] = "/projects/goldengine/dist/examples/Simple3/milesandkm.txt";
        
        if (args.length > 0) {
            Simple3 parser = new Simple3();
            String source = parser.loadSourceFile(args[0]);
            boolean wantTree = args.length > 1 && args[1].equalsIgnoreCase("-tree");
            String tree = parser.executeProgram(source, wantTree);
            if (wantTree) {
                System.out.println(tree);
            }
        } else {
            System.out.println("Usage: java -jar simple3.jar <sourcefile> [-tree]");
        }
    }

}
