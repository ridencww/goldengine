package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.parser.GOLDParser;

@ProcessRule(rule={
    "<ParamList> ::= Id , <ParamList>",
    "<ParamList> ::= Id",
    "<OptionalParamList> ::= <ParamList>",
    "<OptionalParamList> ::= "
})

/**
 * Rule handler for the function parameters rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Parameters extends Reduction {

    public static final String KEY_PARAMETERS = "[ParameterStack]";

    public Parameters(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();

        List<String> parameters = new ArrayList<String>();

        if (reduction.size() > 0) {
            Stack stack;

            Variable var = parser.getProgramVariable(KEY_PARAMETERS);
            if (var == null) {
                stack = new Stack();
                parser.setProgramVariable(KEY_PARAMETERS, new Variable(stack));
            } else {
                stack = (Stack)var.asObject();
            }

            stack.push(reduction.get(0).getData());

            // Copy the values of the current stack to the value object leaving
            // all the values on the stack
            int paramCount = stack.size();
            for (int i = paramCount - 1; i >= 0; i--) {
                parameters.add(stack.get(i).toString());
            }
        }

        setValue(new Variable(parameters));
    }

}
