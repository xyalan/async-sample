package com.hialan.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * User: Alan
 * Email: houlong.yang@shuyun.com
 * Date: 11:29 6/16/16
 */
@Service
public class GitHubLookupService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private RestTemplate restTemplate = new RestTemplate();

    @Async
    public Future<User> findUser(String user) throws InterruptedException {
        System.out.println("Looking up " + user);
        User results = restTemplate.getForObject("https://api.github.com/users/" + user, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
		this.query();
        return new AsyncResult<User>(results);
    }

	 @Autowired
    private JdbcTemplate jdbcTemplate;
	public void query() {
		jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");
		// Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
		/*List<String> s = jdbcTemplate.queryForList("select * from b", String.class);
		return Joiner.on(',').skipNulls().appendTo(new StringBuilder("("), s).append(")")
				.toString();*/
	}
}
