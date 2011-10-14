package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.simple3.Simple3;

@ProcessRule(rule="<Statement> ::= for ( <Statement> ; <Expression> ; <Statement> ) do <Statements> end")

/**
 * Rule handler for the for loop rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class ForLoop extends Reduction {
    private Reduction initStatement;
    private Reduction conditional;
    private Reduction postStatement;
    private Reduction statements;

    public ForLoop(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 11) {
                initStatement = reduction.get(2).asReduction();
                conditional   = reduction.get(4).asReduction();
                postStatement = reduction.get(6).asReduction();
                statements    = reduction.get(9).asReduction();
            } else {
                parser.raiseParserException(Simple3.formatMessage("error.param_count", "11", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple3.formatMessage("error.no_reduction"));
        }            
    }

    @Override
    public void execute() throws ParserException {
        initStatement.execute();
        conditional.execute();
        while (conditional.getValue().asBool()) {
            statements.execute();
            postStatement.execute();
            conditional.execute();
        }
    }

}
