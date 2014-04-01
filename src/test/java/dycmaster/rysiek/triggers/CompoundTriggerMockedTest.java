package dycmaster.rysiek.triggers;

import dycmaster.rysiek.BaseTestMockito;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriteria;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriterion;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;


public class CompoundTriggerMockedTest  extends BaseTestMockito{


	@Test
	public void testContainsSomeTimeAbsolutCriteria(){
		CompoundTriggerLogicalCriteria compoundTriggerLogicalCriteria = new CompoundTriggerLogicalCriteria();
		Date now = new Date();
		compoundTriggerLogicalCriteria.beforeTime(new Date(now.getTime()+3000));

		boolean actual = CompoundTrigger.containsSomeTimeAbsoluteCriteria(compoundTriggerLogicalCriteria);
		Assert.assertTrue(actual);
	}


	@Test
	public void testTimeAbsoluteCriteriaInTrigger_BEFORE_TIME() throws Exception{
		CompoundTrigger compoundTrigger = new CompoundTrigger("testCompTrigger");
		CompoundTriggerLogicalCriteria compoundTriggerLogicalCriteria = new CompoundTriggerLogicalCriteria();

		Date now = new Date();
		compoundTriggerLogicalCriteria.beforeTime(new Date(now.getTime()+3000));
		compoundTrigger.setCompoundTriggerLogicalCriteria(compoundTriggerLogicalCriteria);
		compoundTrigger.start();

		Boolean initState = compoundTrigger.getTriggerState();
		Thread.sleep(4000);
		Boolean finishState = compoundTrigger.getTriggerState();
		Assert.assertTrue(initState);
		Assert.assertFalse(finishState);
	}

	@Test
	public void testTimeAbsoluteCriteriaInTrigger_BEFORE_TIME_doesntGoOffTooEarly() throws Exception{
		CompoundTrigger compoundTrigger = new CompoundTrigger("testCompTrigger");
		CompoundTriggerLogicalCriteria compoundTriggerLogicalCriteria = new CompoundTriggerLogicalCriteria();

		Date now = new Date();
		compoundTriggerLogicalCriteria.beforeTime(new Date(now.getTime()+3000));
		compoundTrigger.setCompoundTriggerLogicalCriteria(compoundTriggerLogicalCriteria);
		compoundTrigger.start();

		Boolean initState = compoundTrigger.getTriggerState();
		Thread.sleep(2000);
		Boolean finishState = compoundTrigger.getTriggerState();
		Assert.assertTrue(initState);
		Assert.assertTrue(finishState);
	}

	@Test
	public void testTimeAbsoluteCriteriaInTrigger_AT_TIME_works5s() throws Exception{
		CompoundTrigger compoundTrigger = new CompoundTrigger("testCompTrigger");
		CompoundTriggerLogicalCriteria compoundTriggerLogicalCriteria = new CompoundTriggerLogicalCriteria();

		Date now = new Date();
		compoundTriggerLogicalCriteria.atTime(new Date(now.getTime() + 3000));
		compoundTrigger.setCompoundTriggerLogicalCriteria(compoundTriggerLogicalCriteria);
		compoundTrigger.start();

		Boolean initFalseState = compoundTrigger.getTriggerState();
		Thread.sleep(2000);
		Boolean stillFalseState  = compoundTrigger.getTriggerState();
		Thread.sleep(2000);
		Boolean onState = compoundTrigger.getTriggerState();
		Thread.sleep(5000);
		Boolean falseState = compoundTrigger.getTriggerState();
		
		Assert.assertFalse(initFalseState);
		Assert.assertFalse(stillFalseState);
		Assert.assertTrue(onState);
		Assert.assertFalse(falseState);

	}



	@Test
	public void testTimeAbsoluteCriteria() throws Exception{


		Map<String, Boolean> fakeTriggerStates = new HashMap<>();
		fakeTriggerStates.put("ONE", true);
		fakeTriggerStates.put("TWO", false);
		fakeTriggerStates.put("THREE", true);

		CompoundTrigger compoundTrigger = new CompoundTrigger("testCompTrigger");
		doReturn(fakeTriggerStates).when(compoundTrigger).getTriggerInput(any(TriggerValue.class));
		CompoundTriggerLogicalCriteria compoundTriggerLogicalCriteria = new CompoundTriggerLogicalCriteria();

		Date now = new Date();
		compoundTriggerLogicalCriteria.beforeTime(new Date(now.getTime()+3000));
		compoundTrigger.setCompoundTriggerLogicalCriteria(compoundTriggerLogicalCriteria);
		compoundTrigger.start();
		Thread.sleep(6000);


//		compoundTrigger.setCompoundTriggerParametersParser(new DefaultCompoundTriggerParametersParser());
//
//		compoundTrigger.compoundTriggerInputChanged(changeSource);
//		verify(compoundTrigger).setTriggerState(anyBoolean());
	}

	@Test
	public void testCompoundTriggerLogicalCriteria(){
		CompoundTriggerLogicalCriteria t1Criteria = new CompoundTriggerLogicalCriteria();
		t1Criteria.goesOn("t1").and(t1Criteria.afterTime(new Date())).and(t1Criteria.goesOnAtTime("t1",new Date()));

		Collection<CompoundTriggerLogicalCriterion> actual =  t1Criteria.getAllCriteria();
		Assert.assertEquals(actual.size(), 3);
	}


}
