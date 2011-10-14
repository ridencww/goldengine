package com.creativewidgetworks.goldparser.simple2.rulehandlers;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.simple2.Simple2;

@ProcessRule(rule={
    "<Statement> ::= if <Expression> then <Statements> end",
    "<Statement> ::= if <Expression> then <Statements> else <Statements> end"
})

/**
 * Rule handler for the If-Then-Else rules.
 *
 * if <condition> then 
 *   <statement(s)>
 * end
 * 
 * if <condition> then
 *   <statement(s)>
 * else
 *   <statement(s)>
 * end
 *
 * if <condition> then
 *   <statements(s)>
 * else if <condition> then
 *   <statements(s)>
 * else if ...
 *   <statement(s)>
 * else
 *   <statement(s)>
 * end end end ..
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class IfThenElse extends Reduction {
    private Reduction conditional;
    private Reduction thenStatements;
    private Reduction elseStatements;

    public IfThenElse(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 5) {
                // if x then y end
                conditional    = reduction.get(1).asReduction();
                thenStatements = reduction.get(3).asReduction();                
            } else if (reduction.size() == 7) {
                // if x then y else z end
                conditional    = reduction.get(1).asReduction();
                thenStatements = reduction.get(3).asReduction(); 
                elseStatements = reduction.get(5).asReduction();
            } else {
                if (reduction.size() < 5) {
                    // if x then y form
                    parser.raiseParserException(Simple2.formatMessage("error.param_count", "5", String.valueOf(reduction.size())));
                }
                // if x then y else z end form
                parser.raiseParserException(Simple2.formatMessage("error.param_count", "7", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(Simple2.formatMessage("error.no_reduction"));
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
