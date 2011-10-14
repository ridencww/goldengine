package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.simple3.Simple3;

@ProcessRule(rule="<Statement> ::= while <Expression> do <Statements> end")

/**
 * Rule handler for the while loop rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class WhileLoop extends Reduction {
    private Reduction conditional;
    private Reduction statements;

    public WhileLoop(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 5) {
                conditional = reduction.get(1).asReduction();
                statements  = reduction.get(3).asReduction();                
            } else {
                parser.raiseParserException(Simple3.formatMessage("error.param_count", "5", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple3.formatMessage("error.no_reduction"));
        }          
    }

    @Override
    public void execute() throws ParserException {
        conditional.execute();
        while (conditional.getValue().asBool()) {
            statements.execute();
            conditional.execute();
        }
    }

}
