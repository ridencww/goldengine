package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.simple3.Simple3;

@ProcessRule(rule="<Statement> ::= return <Expression>")

/**
 * Rule handler for the return from subroutine rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Return extends Reduction {

    private Reduction statements;

    public Return(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 2) {
                statements = parser.getCurrentReduction().get(1).asReduction();
            } else if (reduction.size() != 1) {
                parser.raiseParserException(Simple3.formatMessage("error.param_count", "2", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple3.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        if (statements != null) {
            statements.execute();
            setValue(statements.getValue());
        }
    }

}
