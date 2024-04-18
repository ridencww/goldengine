package com.creativewidgetworks.goldparser.parser;

import java.lang.annotation.Annotation;

import junit.framework.TestCase;

import org.junit.Test;

import com.creativewidgetworks.goldparser.parser.ProcessRule;

public class ProcessRuleTest extends TestCase {

    @Test
    public void testProcessRuleAnnotation_defined_correctly() throws Exception {
        // Assert annotation is class based (TYPE) and available at runtime
        Annotation[] annotations = ProcessRule.class.getAnnotations();
        assertEquals("wrong number of annotations", 2, annotations.length);
        assertEquals("bad annotation definition", "@java.lang.annotation.Retention(RUNTIME)", annotations[0].toString());
        assertEquals("bad annotation definition", "@java.lang.annotation.Target({TYPE})", annotations[1].toString());
    }

}
