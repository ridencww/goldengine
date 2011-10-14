package com.creativewidgetworks.goldparser.simple2.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.simple2.Simple2;

@ProcessRule(rule={
    "<Statements> ::= <Statement> <Statements>",    
    "<Statements> ::= <Statement>"
})

/**
 * Rule handler for the statement(s) rules.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Statements extends Reduction {
    private Reduction statement1;
    private Reduction statement2;

    public Statements(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() > 0 && reduction.size() < 3) {
                statement1 = reduction.get(0).asReduction();
                statement2 = (reduction.size() > 1) ? reduction.get(1).asReduction() : null;
            } else {
                parser.raiseParserException(Simple2.formatMessage("error.param_count_range", "1", "2", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple2.formatMessage("error.no_reduction"));
        }         
    }

    @Override
    public void execute() throws ParserException {
        statement1.execute();
        setValue(statement1.getValue());
        if (statement2 != null) {
            statement2.execute();
            setValue(statement2.getValue());
        }
    }

}
