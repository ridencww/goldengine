package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import java.util.List;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;

@ProcessRule(rule="<Statement> ::= function Id ( <OptionalParamList> ) begin <Statements> end")

/**
 * Rule handler for the function declaration rule.
 *
 * function ID (<optional parameters) begin
 *   statements
 *   return/return statement
 * end
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Function extends Reduction {
    public static final String FUNCTION_PREFIX = "[fn]";

    private final String functionName;
    private final List<String> parameters;
    private final Reduction statements;

    public Function(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();

        functionName = reduction.get(1).getData().toString();
        parameters = (List<String>)reduction.get(3).asReduction().getValue().asObject();
        statements = reduction.get(6).asReduction();

        // Clean up the parameter call stack
        parser.clearProgramVariable(Parameters.KEY_PARAMETERS);

        // Make the function available to be called
        parser.setProgramVariable(FUNCTION_PREFIX + functionName, new Variable(this));
    }

    public List<String> getParameters() {
        return parameters;
    }

    public Reduction getStatements() {
        return statements;
    }

}
