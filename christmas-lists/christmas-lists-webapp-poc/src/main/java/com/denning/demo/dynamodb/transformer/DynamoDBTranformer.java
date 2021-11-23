/**
 * 
 */
package com.denning.demo.dynamodb.transformer;

import java.util.ArrayList;
import java.util.List;

import com.denning.demo.dynamodb.mapper.UserItem;
import com.denning.demo.model.User;

/**
 * @author zacdenning
 *
 */
public class DynamoDBTranformer
{
	/**
	 * @param items
	 * @return
	 */
	public static List<User> transformUsers(final List<UserItem> items)
	{
		final List<User> userList = new ArrayList<>();
		items.stream().forEach(item -> {
			final User user = new User();
			user.setFirstName(item.getName());
			user.setUsername(item.getUsername());
			user.setPassword(item.getPassword());
			userList.add(user);
		});
		return userList;
	}
	
	/**
	 * @param item
	 * @return
	 */
	public static User transformSingleUser(final UserItem item)
	{
		final User user = new User();
		user.setFirstName(item.getName());
		user.setUsername(item.getUsername());
		user.setPassword(item.getPassword());
		return user;
	}
}
