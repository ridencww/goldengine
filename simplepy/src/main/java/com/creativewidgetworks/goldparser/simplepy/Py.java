package com.creativewidgetworks.goldparser.simplepy;

import java.io.*;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.util.FormatHelper;

/**
 * An interpreter for the mythical Py language. Source files can be executed and parse
 * trees can be generated from the input stream.
 */
public class Py {

    private static final String RESOURCE_FILE = "/simplepy";

    /**
     * Formats rule handler messages using the simplepy.properties resource file
     * @param message key (e.g., error.syntax)
     * @param parameters that will be inserted into any message placeholder entries (0}...
     * @return formated string.
     */
    public static String formatMessage(String message, Object... parameters) {
        return FormatHelper.formatMessage(RESOURCE_FILE, message, parameters);
    }


    /**
     * Executes a program written in Py.
     * @param sourceCode to interpret
     * @param wantTree true if the parse tree should be generated
     * @return parse tree if generate parse tree open is set
     */
    public String executeProgram(String sourceCode, boolean wantTree) throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        // Use the compiled grammar file inside the jar
        GOLDParser parser = new GOLDParser(
            new FileInputStream("./src/test/resources/py.egt"), // compiled grammar table
            "com.creativewidgetworks.goldparser.simplepy.rulehandlers",  // rule handler package (fully qualified package)
            true);                                                       // trim reductions

        
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
     * Load a source file to be interpreted by the engine.  
     * @param filename of a source file
     * @return source code to be interpreted
     * @throws IOException 
     */
    public String loadSourceFile(String filename) throws IOException {
        InputStream is;
        try {
            is = new FileInputStream(filename);
        } catch (FileNotFoundException ex) {
            if (!filename.startsWith("/")) {
                filename = "/" + filename;
            }
            is = getClass().getResourceAsStream(filename);
            if (is == null) {
                throw ex;
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
        } finally {
            is.close();
        }

        return baos.toString("UTF-8");
    }
   
    /*----------------------------------------------------------------------------*/
    
    private static String source = 
        "if (1==1):\r\n" + 
    	"  print 'true - first line'\r\n" + 
    	"  print 'true - second line'\r\n" + 
    	"  if (2==2):\r\n" + 
    	"      print 'true - level 2'\r\n" + 
    	"      print 'true - level 2'\r\n" + 
    	"      if (3==3):\r\n" + 
    	"        print 'true - level 3'\r\n" + 
    	" print 'always should print'";
    
    
    public static void main(String[] args) throws Exception {
        Py parser = new Py();
        
        boolean wantTree = false;
        String tree = parser.executeProgram(source, wantTree);
        if (wantTree) {
            System.out.println(tree);
        }
    }
}
