package com.denning.demo.dynamodb.mapper;

import java.util.List;

import com.denning.demo.model.Gift;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class UserItem
{
	private String username;
	private String password;
	private String name;
	private List<Gift> items;
	private List<String> friends;

	private static final DynamoDbEnhancedClient DDB_ENHANCED_CLIENT = 
	        DynamoDbEnhancedClient.create();
	private static final DynamoDbTable<UserItem> USERS_TABLE =
	        DDB_ENHANCED_CLIENT.table("users", 
	                                  TableSchema.fromBean(UserItem.class));
	@DynamoDbPartitionKey
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public List<Gift> getItems() { return items; }
	public void setItems(List<Gift> items) { this.items = items; }

	public List<String> getFriends() { return friends; }
	public void setFriends(List<String> friends) { this.friends = friends; }
	
	
	public static UserItem load(UserItem userItem)
	{
        return USERS_TABLE.getItem(userItem);
    }
	
	public void save()
	{
		USERS_TABLE.putItem(this);
    }
	
}
