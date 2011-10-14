package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import java.util.List;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Scope;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.simple3.Simple3;

@ProcessRule(rule="<Value> ::= Id ( <OptionalArgumentList> )")

/**
 * Rule handler for the call function rule.
 *
 * Calling function ID with no return value:  
 *   ID(<optional_args>)
 *
 * Calling function ID returning a value:
 *   result = ID(<optional_args>)
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class FunctionCall extends Reduction {
    private final GOLDParser theParser;

    private final String functionName;
    private final List<Reduction> arguments;

    public FunctionCall(GOLDParser parser) {
        theParser = parser;
        Reduction reduction = parser.getCurrentReduction();
        functionName = reduction.get(0).getData().toString();

        if (reduction.get(2).asReduction() instanceof Arguments) {
            arguments = (List<Reduction>)reduction.get(2).asReduction().getValue().asObject();
        } else {
            // This handles the single parameter case
            Arguments args = new Arguments(reduction.get(2).asReduction());
            arguments = (List<Reduction>)args.getValue().asObject();
        }
    }

    @Override
    public Variable getValue() {
        Scope newScope = new Scope(Function.FUNCTION_PREFIX + functionName, theParser.getCurrentScope());
        Scope oldScope = theParser.setCurrentScope(newScope);

        try {
            // Retrieve the function Reduction, set parameters, and execute the function
            Variable var = theParser.getProgramVariable(Function.FUNCTION_PREFIX + functionName);
            if (var != null) {
                Function fn = (Function)var.asObject();

                List<String> parameters = fn.getParameters();

                // Make sure we have the proper number of calling parameters
                if (parameters.size() != arguments.size()) {
                    throw new ParserException(Simple3.formatMessage("error.function_argument_count", 
                        String.valueOf(parameters.size()), String.valueOf(arguments.size())));
                }

                // Set the function parameters -- these will be stored under the function's
                // scope and will not be visible outside of the function. Any variables
                // in other scopes having the same name will not be visible to the function.
                for (int i = 0; i < parameters.size(); i++) {
                    theParser.setProgramVariable(parameters.get(i), arguments.get(i).getValue());
                }

                Reduction statements = fn.getStatements();
                if (statements != null) {
                    statements.execute();
                    var = statements.getValue();
                }

                // Simple3 doesn't allow a simple RETURN without a value hence this
                // path will never be executed.  The code remains to support other
                // syntaxes that allow an empty return statement.  These lines of
                // code will be flagged as untested by the coverage tool when the
                // Simple3 tests are run.
                if (var == null) {
                    var = new Variable("");
                }
            } else {
                throw new ParserException(Simple3.formatMessage("error.function_undefined", functionName));
            }

            return var;

        } finally {
            theParser.setCurrentScope(oldScope);
        }
    }

}
