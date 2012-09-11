package com.creativewidgetworks.goldparser.simplepy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.parser.GOLDParser;

/**
 * An interpreter for the mythical py language. Source files can be executed and parse
 * trees can be generated from the input stream.
 */
public class py {
    
    /**
     * Executes a program written in py.
     * @param sourceCode to interpret
     * @param wantTree true if the parse tree should be generated
     * @return parse tree if generate parse tree open is set
     */
    public String executeProgram(String sourceCode, boolean wantTree) throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        // Use the compiled grammar file inside the jar
        GOLDParser parser = new GOLDParser(
            new FileInputStream("./src/test/resources/simplypy/py.egt"), // compiled grammar table
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
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[(int)file.length()];
        fis.read(buf);
        fis.close();
        return new String(buf);
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
        py parser = new py();
        
        boolean wantTree = false;
        String tree = parser.executeProgram(source, wantTree);
        if (wantTree) {
            System.out.println(tree);
        }
    }
    
    
    
}
