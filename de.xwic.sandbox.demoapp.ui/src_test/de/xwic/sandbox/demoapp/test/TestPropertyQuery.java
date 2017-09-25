/**
 * 
 */
package de.xwic.sandbox.demoapp.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;

/**
 * @author lippisch
 *
 */
public class TestPropertyQuery {

	@Test
	public void test() {

		List<String> allowedStates = new ArrayList<String>();
		allowedStates.add("Bla");
		allowedStates.add("Blup");
		
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("name", "123");
		pq.addEquals("customer.salesRep.name", "John");
		pq.addIn("state", allowedStates);
		pq.addLeftOuterJoinProperty("customer", "c");
		pq.addLeftOuterJoinProperty("c.salesRep", "sr");
		
		
		
		PropertyQuery pqSub = new PropertyQuery();
		pqSub.addEquals("x", "v");
		pqSub.addOrEmpty("x");
		
		pq.addQueryElement(new QueryElement(QueryElement.OR, pqSub));
		//pq.addSubQuery(pqSub);
		
		System.out.println(pq);
		
	}

}
