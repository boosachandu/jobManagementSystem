/**
 * 
 */
package com.batch.items;

import org.springframework.batch.item.ItemProcessor;

import com.batch.model.pojo.Alerts;

/**
 * @author hjadhav1
 *
 */
public class CustomAlertProcessor implements ItemProcessor<Alerts,Alerts>{

	@Override
	public Alerts process(Alerts item) throws Exception {
		
		System.out.println("Inserting Alerts : " + item);
		item.setAlertAge(1);
		//add other processing requirements
		return item;
	}

	
}
