package com.creativewidgetworks.goldparser.simplepy.rulehandlers;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;

@ProcessRule(rule = "<Value> ::= StringLiteral")

/**
 * Rule handler for the string literal rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0.0
 */
public class StringLiteral extends Reduction {

    public StringLiteral(GOLDParser parser) {
        // The null test is being performed because this class is used in a test where the parser
        // is not completely initialized.  In production, the current reduction will always be set.
        String str = parser.getCurrentReduction() == null ? "\"\"" : parser.getCurrentReduction().get(0).asString();

        StringBuilder sb = new StringBuilder(str);
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(0);
        setValue(new Variable(sb.toString()));
    }

}
