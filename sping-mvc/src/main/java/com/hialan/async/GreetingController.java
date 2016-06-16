package com.hialan.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * User: Alan
 * Email: houlong.yang@shuyun.com
 * Date: 10:28 6/16/16
 */
@RestController
public class GreetingController {
	private static Map<String, Future<User>> user = new ConcurrentHashMap<>();

	@RequestMapping(method= RequestMethod.GET )
	public Callable<Person> getPerson() {

		return () -> {
			Thread.sleep(10000);
			Person p = new Person();
			p.setAge(22);
			p.setFullName("Alan Yang");
			p.setName("alan");
			return p;
		};
	}

	@Autowired
    private GitHubLookupService gitHubLookupService;
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public User getGitHubUser(@RequestParam(name = "username") String username) {
		try {
			if (user.get("xyalan") == null) {
				Future<User> fu = gitHubLookupService.findUser(username);
				user.put("xyalan", fu);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return Optional.ofNullable(user.get("xyalan")).map(s -> {
			try {
				if (s.isDone()) {
					User r = s.get();
					user.clear();
					return r;
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return new User();
		}).orElse(new User());
	}
}
