package com.creativewidgetworks.goldparser.simplepy.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.simplepy.Py;

@ProcessRule(rule = {
    "<Statement> ::= if <Expression> : <StatementOrBlock>",
    "<Statement> ::= else : <StatementOrBlock>"
})

public class IfThenElse extends Reduction {
    private Reduction conditional;
    private Reduction thenStatements;
    private Reduction elseStatements;

    public IfThenElse(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 4) {
                // if x then y 
                conditional = reduction.get(1).asReduction();
                thenStatements = reduction.get(3).asReduction();
            } else if (reduction.size() == 3) {
                // else x 
                conditional = reduction.get(0).asReduction();
                thenStatements = reduction.get(2).asReduction();
                elseStatements = reduction.get(2).asReduction();
            } else {
                if (reduction.size() < 5) {
                    // if x then y form
                    parser.raiseParserException(Py.formatMessage("error.param_count", "5", String.valueOf(reduction.size())));
                }
                // if x then y else z end form
                parser.raiseParserException(Py.formatMessage("error.param_count", "7", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Py.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        if (conditional.getValue().asBool()) {
            thenStatements.execute();
        } else if (elseStatements != null) {
            elseStatements.execute();
        }
    }

}
