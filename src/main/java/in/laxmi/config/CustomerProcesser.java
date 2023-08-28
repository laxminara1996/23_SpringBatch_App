package in.laxmi.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import in.laxmi.entity.Customer;
@Component
public class CustomerProcesser implements ItemProcessor<Customer, Customer> {

	public Customer process(Customer item) throws Exception {
		/*if(item.getCounty().equals("India")) {
			return item;
		}*/
		return item;
	}

}
