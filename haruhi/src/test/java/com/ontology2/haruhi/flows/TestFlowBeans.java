package com.ontology2.haruhi.flows;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ontology2.haruhi.JobApp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"../shell/applicationContext.xml","../shell/testDefaults.xml"})

public class TestFlowBeans {
    private static Log logger = LogFactory.getLog(TestFlowBeans.class);
    @Autowired SpringFlow basekbNowFlow;
    
    @Test public void basekbNowFlowConfiguresStepsCorrectly() {
        List<String> flowArgs=Lists.newArrayList("s3n://freebase-dumps/","1942-12-07-00-00","s3n://basekb-now/");
        List<FlowStep> steps=basekbNowFlow.generateSteps(flowArgs);
        assertNotNull(steps);
        assertEquals(4,steps.size());
        
        {
            assertTrue(steps.get(0) instanceof SpringStep);
            SpringStep step0=(SpringStep) steps.get(0);
            List<String> args=step0.getStepArgs(emptyMap(),flowArgs);
            
            assertEquals(4,args.size());
            
            int i=0;
            assertEquals("run",args.get(i++));
            assertEquals("freebaseRDFPrefilter",args.get(i++));
            assertEquals("s3n://freebase-dumps/freebase-rdf-1942-12-07-00-00/",args.get(i++));
            assertEquals("/preprocessed/1942-12-07-00-00/",args.get(i++));
        }
        
        {
            assertTrue(steps.get(1) instanceof SpringStep);
            SpringStep step1=(SpringStep) steps.get(1);
            List<String> args=step1.getStepArgs(emptyMap(),flowArgs);
            
            assertEquals(4,args.size());
            
            int i=0;
            assertEquals("run",args.get(i++));
            assertEquals("pse3",args.get(i++));
            assertEquals("/preprocessed/1942-12-07-00-00/",args.get(i++));
            assertEquals("s3n://basekb-now/1942-12-07-00-00/",args.get(i++));
        }
        
        {
            assertTrue(steps.get(2) instanceof SpringStep);
            SpringStep step2=(SpringStep) steps.get(2);
            List<String> args=step2.getStepArgs(emptyMap(),flowArgs);
            
            assertEquals(4,args.size());
            
            int i=0;
            assertEquals("run",args.get(i++));
            assertEquals("sieve3",args.get(i++));
            assertEquals("s3n://basekb-now/1942-12-07-00-00/accepted/",args.get(i++));
            assertEquals("s3n://basekb-now/1942-12-07-00-00/sieved/",args.get(i++));
        }
        
        {
            assertTrue(steps.get(3) instanceof SpringStep);
            SpringStep step2=(SpringStep) steps.get(3);
            List<String> args=step2.getStepArgs(emptyMap(),flowArgs);
            
            assertEquals(5,args.size());
            
            int i=0;
            assertEquals("run",args.get(i++));
            assertEquals("fs",args.get(i++));
            assertEquals("-rmr",args.get(i++));
            assertEquals("s3n://basekb-now/1942-12-07-00-00/accepted/",args.get(i++));
            assertEquals("/preprocessed/1942-12-07-00-00/",args.get(i++));
        }
    }

    private Map<String,Object> emptyMap() {
        return Maps.newHashMap();
    }
}