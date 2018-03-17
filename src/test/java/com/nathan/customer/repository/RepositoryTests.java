package com.nathan.customer.repository;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathan.customer.entity.Customer;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTests.class);

	@Autowired
	private CustomerRepository repository;

	@Test
	public void testSaveAndUpdate() {
		
		Customer entity = new Customer();
		entity.setFirstName("SASA");
		entity.setLastName("SAAI");
		repository.save(entity);
		assertThat(repository.findAll(), hasSize(1));
		
		entity.setLastName("TEST_LAST");
		entity.setFirstName("TEST_FIRST");
		
		repository.save(entity);
		
		Customer updateObject = repository.findByLastName("TEST_LAST");
		assertThat(updateObject.getFirstName(), equalTo("TEST_FIRST"));
		assertThat(updateObject.getLastName(), equalTo("TEST_LAST"));
		
	}

	@Test
	public void testFindAndDelete() {
		
		Customer entity = new Customer();
		entity.setFirstName("SASA");
		entity.setLastName("SAAI");
		repository.save(entity);
		
		assertThat(repository.findAll(), hasSize(1));

		LOGGER.info(">>>> id {} ", entity.getId());
		
		Customer retriveSameObject = repository.findById(entity.getId());
		assertThat(retriveSameObject, equalTo(entity));
		
		repository.delete(retriveSameObject);
		assertThat(repository.findAll(), hasSize(0));
		
	}
}
