package com.creativewidgetworks.goldparser.simple2.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.SystemConsole;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.simple2.Simple2;
import com.creativewidgetworks.goldparser.util.ConsoleDriver;

@ProcessRule(rule={
    "<Statement> ::= display <Expression>",
    "<Statement> ::= display <Expression> read Id"
})

/**
 * Rule handler for the display rules.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Display extends Reduction {
    private GOLDParser theParser;
    private Reduction dataToPrint;
    private String variableName;
    
    // These is made static and public so tests or applications can set the 
    // end of line sequence or reroute the console IO to another location.
    public static String EOLN = "\r\n";
    public static ConsoleDriver ioDriver = new SystemConsole();

    public Display(GOLDParser parser) {
        theParser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 2) {
                dataToPrint = reduction.get(1).asReduction();
                variableName = null;
            } else if (reduction.size() == 4) {
                dataToPrint = reduction.get(1).asReduction();
                variableName = reduction.get(3).asString();
            } else {
                if (reduction.size() < 3) {
                    // print x form
                    parser.raiseParserException(Simple2.formatMessage("error.param_count", "2", String.valueOf(reduction.size())));
                }
                // print x read y form
                parser.raiseParserException(Simple2.formatMessage("error.param_count", "4", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple2.formatMessage("error.no_reduction"));
        }        
    }

    @Override
    public void execute() throws ParserException {
        // Write the data to the console
        ioDriver.write(dataToPrint.getValue().asObject().toString());
        if (variableName != null) {
            // Read console input from the user (assuming that EOLN is not echoed to console)
            StringBuilder sb = new StringBuilder(ioDriver.read());
            theParser.setProgramVariable(variableName, new Variable(sb.toString()));
        }
        ioDriver.write(EOLN);
    }

}
