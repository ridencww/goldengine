package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.simple3.Simple3;

@ProcessRule(rule="<Statement> ::= loop <Statements> until <Expression>")

/**
 * Rule handler for the loop until rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class LoopUntil extends Reduction {
    private Reduction conditional;
    private Reduction statements;

    public LoopUntil(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 4) {
                statements  = reduction.get(1).asReduction();
                conditional = reduction.get(3).asReduction();
            } else {
                parser.raiseParserException(Simple3.formatMessage("error.param_count", "4", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple3.formatMessage("error.no_reduction"));
        }        
    }

    @Override
    public void execute() throws ParserException {
        while (true) {
            statements.execute();
            conditional.execute();
            if (conditional.getValue().asBool()) {
                break;
            }
        }
    }

}
