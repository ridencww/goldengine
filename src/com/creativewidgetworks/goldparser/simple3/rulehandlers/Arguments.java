package com.creativewidgetworks.goldparser.simple3.rulehandlers;

import java.util.ArrayList;
import java.util.List;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.parser.GOLDParser;

@ProcessRule(rule={
    "<OptionalArgumentList> ::= <ArgumentList>",
    "<OptionalArgumentList> ::= ",
    "<ArgumentList> ::= <Expression> , <ArgumentList>",
    "<ArgumentList> ::= <Expression>"
})

/**
 * Rule handler for the pass arguments rule.
 *
 * @author Ralph Iden (http://www.creativewidgetworks.com)
 * @version 5.0 RC2 
 */
public class Arguments extends Reduction {

    public Arguments(Reduction reduction) {
        List<Reduction> arguments = new ArrayList<Reduction>();
        arguments.add(reduction);
        setValue(new Variable(arguments));
    }

    public Arguments(GOLDParser parser) throws ParserException {
        Reduction reduction = parser.getCurrentReduction();

        List<Reduction> arguments = new ArrayList<Reduction>();
        buildArgumentList(reduction, arguments);

        setValue(new Variable(arguments));
    }

    /**
     * Helper to recursively expand any Argument objects to retrieve a
     * list of Reductions
     * @param reduction the reduction currently being processed
     * @param args list of arguments
     */
    private void buildArgumentList(Reduction reduction, List<Reduction> args) throws ParserException {
        int tokenCount = reduction.size();
        if (tokenCount > 0) {
            for (int i = 0; i < tokenCount; i++) {
                Object obj = reduction.get(i).getData();
                if (obj instanceof Reduction) {
                    if (obj instanceof Arguments) {
                        buildArgumentList((Reduction)obj, args);
                    } else {
                        args.add((Reduction)obj);
                    }
                }
            }
        } else if (reduction.getValue() != null) {
            List<Reduction> moreArgs = (List<Reduction>) reduction.getValue().asObject();
            for (Reduction r : moreArgs) {
                args.add(r);
            }
        }
    }

}
