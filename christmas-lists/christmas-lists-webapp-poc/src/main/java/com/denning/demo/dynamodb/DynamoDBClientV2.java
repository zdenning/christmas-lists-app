package com.denning.demo.dynamodb;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


import com.denning.demo.dynamodb.mapper.UserItem;
import com.denning.demo.dynamodb.transformer.DynamoDBTranformer;
import com.denning.demo.exception.ResponseDiagnostic;
import com.denning.demo.model.User;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import com.denning.demo.model.Gift;

@Component
public class DynamoDBClientV2
{
	/** The client */
	private DynamoDbClient client;
	
	/** The client */
	private DynamoDbEnhancedClient enhancedClient;
	
	/** The client */
	private static final String USERS_TABLE = "users";
	
	
	/**
	 * Constructor
	 */
	public DynamoDBClientV2()
	{
		client = DynamoDbClient.builder()
				.region(Region.US_EAST_1)
				.build();
		enhancedClient = DynamoDbEnhancedClient.builder()
				.dynamoDbClient(client)
				.build();
		
	}
	
	/**
	 * @return
	 * @throws InvocationTargetException 
	 */
	public ResponseDiagnostic addUser(final String username, final String displayName, final String password)
	{	
		final ResponseDiagnostic responseDiagnotic = new ResponseDiagnostic();
		
		try
		{
			getUser(username);
			responseDiagnotic.setStatus(HttpStatus.BAD_REQUEST);
			responseDiagnotic.setMessage("User already exists!");
		}
		catch (Exception e)
		{
			try
			{
				final DynamoDbTable<UserItem> usersTable =
					enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));
			
				UserItem userItem = new UserItem();
				userItem.setUsername(username);
				userItem.setName(displayName);
				userItem.setPassword(password);
				userItem.setItems(new ArrayList<>());
				
				usersTable.putItem(userItem);
				
				responseDiagnotic.setStatus(HttpStatus.OK);
			}
			catch (DynamoDbException dbe)
			{
				responseDiagnotic.setStatus(HttpStatus.BAD_REQUEST);
				responseDiagnotic.setMessage("Error inserting user");
				System.err.println(dbe.getMessage());
			}
		}

		return responseDiagnotic;
		
	}
	
	//TODO
	/**
	 * @param username
	 * @return
	 */
//	public Boolean deleteUser(final String username)
//	{
//		HashMap<String,AttributeValue> keyToGet = new HashMap<>();
//		UserItem
//		
//        keyToGet.put(username, AttributeValue.builder()
//                .s(keyVal)
//                .build());
//
//        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
//                .tableName(tableName)
//                .key(keyToGet)
//                .build();
//
//        try {
//            ddb.deleteItem(deleteReq);
//        } catch (DynamoDbException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//	}
	
	/**
	 * @param username
	 * @return
	 */
	public User getUser(final String username) throws IndexOutOfBoundsException
	{
		try
		{
            //Create a DynamoDbTable object
            DynamoDbTable<UserItem> table = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));

            //Create a KEY object
            Key key = Key.builder()
                    .partitionValue(username)
                    .build();

            // Get the item by using the key
            return DynamoDBTranformer.transformSingleUser(table.getItem(r -> r.key(key)));

        }
		catch (Exception e)
		{
            throw new IndexOutOfBoundsException("Could not get user");
        }
		
	}
	
	/**
	 * @return
	 * @deprecated should not be used as of implementation of friends feature
	 */
	public List<User> getAllUsers()
	{	

        final List<UserItem> itemList = new ArrayList<>();
		try
		{
            // Create a DynamoDbTable object
            DynamoDbTable<UserItem> usersTable = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));
            Iterator<UserItem> results = usersTable.scan().items().iterator();
            while (results.hasNext())
            {
            	itemList.add(results.next());
            }

        }
		catch (DynamoDbException e)
		{
            System.err.println(e.getMessage());
            return DynamoDBTranformer.transformUsers(itemList);
        }
		
		return DynamoDBTranformer.transformUsers(itemList);
	}
	
	/**
	 * @param username
	 * @return
	 */
	public List<User> getFriendsList(final String username)
	{

        final List<UserItem> friendsList = new ArrayList<>();
        final List<String> currentUserFriends = this.getUserItem(username).getFriends();
        try
		{
            // Create a DynamoDbTable object
            DynamoDbTable<UserItem> usersTable = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));
            Iterator<UserItem> results = usersTable.scan().items().iterator();
            while (results.hasNext())
            {
            	final UserItem nextUser = results.next();
            	if (currentUserFriends.contains(nextUser.getUsername()))
            	{
                	friendsList.add(nextUser);
            	}
            }

        }
		catch (DynamoDbException e)
		{
            System.err.println(e.getMessage());
            return DynamoDBTranformer.transformUsers(friendsList);
        }
        
        return DynamoDBTranformer.transformUsers(friendsList);
	}
	
	public void addFriend(final String username, final String friendUsername)
	{
		try
		{
            //Create a DynamoDbTable object
            DynamoDbTable<UserItem> table = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));

            //Create a KEY object
            final Key key = Key.builder()
                    .partitionValue(username)
                    .build();

            final UserItem userItem = table.getItem(r->r.key(key));
            userItem.getFriends().add(friendUsername);
            table.updateItem(userItem);

        }
		catch (DynamoDbException e)
		{
            throw new IndexOutOfBoundsException("Could not add friend");
        }
	}
	
	
	/**
	 * @param username
	 * @return
	 */
	public List<Gift> getUserList(final String username)
	{	
		DynamoDbTable<UserItem> mappedTable = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder()
                        .partitionValue(username)
                        .build());

        // Get items in the table and write out the ID value
        Iterator<UserItem> results = mappedTable.query(queryConditional).items().iterator();
        
        return results.next().getItems();
		
	}
	
	/**
	 * @param username
	 * @param gift
	 * @return
	 */
	public List<Gift> addGift(final String username, final Gift gift)
	{	
		try
		{
            //Create a DynamoDbTable object
            DynamoDbTable<UserItem> table = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));

            //Create a KEY object
            final Key key = Key.builder()
                    .partitionValue(username)
                    .build();

            final UserItem userItem = table.getItem(r->r.key(key));
            userItem.getItems().add(gift);
            table.updateItem(userItem);
            
            return getUserList(username);

        }
		catch (DynamoDbException e)
		{
            throw new IndexOutOfBoundsException("Could not add item");
        }
	}

	/**
	 * @param username
	 * @param gift
	 * @return
	 */
	public ResponseDiagnostic buyGift(final String username, final Gift gift)
	{
		final ResponseDiagnostic responseDiagnostic = new ResponseDiagnostic();
		try
		{
            //Create a DynamoDbTable object
            DynamoDbTable<UserItem> table = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));

            //Create a KEY object
            final Key key = Key.builder()
                    .partitionValue(username)
                    .build();

            final UserItem userItem = table.getItem(r->r.key(key));
            for (int i = 0; i < userItem.getItems().size(); i++)
            {
            	if (userItem.getItems().get(i).equals(gift))
            	{
            		userItem.getItems().get(i).setBought(true);
            		break;
            	}
            }
            table.updateItem(userItem);
            
            responseDiagnostic.setStatus(HttpStatus.OK);
            return responseDiagnostic;

        }
		catch (final Exception e)
		{
			responseDiagnostic.setStatus(HttpStatus.BAD_REQUEST);
			responseDiagnostic.setMessage("Could not buy item");
			return responseDiagnostic;
        }
		
	}
	
	/**
	 * @param username
	 * @param gift
	 * @return
	 */
	public ResponseDiagnostic updateGift(final String username, final Gift gift)
	{
		final ResponseDiagnostic responseDiagnostic = new ResponseDiagnostic();
		try
		{
            //Create a DynamoDbTable object
            DynamoDbTable<UserItem> table = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));

            //Create a KEY object
            final Key key = Key.builder()
                    .partitionValue(username)
                    .build();

            final UserItem userItem = table.getItem(r->r.key(key));
            userItem.getItems().set(userItem.getItems().indexOf(gift), gift);
            
            table.updateItem(userItem);
            
            responseDiagnostic.setStatus(HttpStatus.OK);
            return responseDiagnostic;

        }
		catch (final Exception e)
		{
			responseDiagnostic.setStatus(HttpStatus.BAD_REQUEST);
			responseDiagnostic.setMessage("Could not update item");
			
			return responseDiagnostic;
        }
		
	}
	
	/**
	 * @param username
	 * @param gift
	 * @return
	 */
	public ResponseDiagnostic deleteGift(final String username, final Gift gift)
	{
		final ResponseDiagnostic responseDiagnostic = new ResponseDiagnostic();
		try
		{
            //Create a DynamoDbTable object
            DynamoDbTable<UserItem> table = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));

            //Create a KEY object
            final Key key = Key.builder()
                    .partitionValue(username)
                    .build();

            final UserItem userItem = table.getItem(r->r.key(key));
            userItem.getItems().remove(userItem.getItems().indexOf(gift));
            
            table.updateItem(userItem);
            
            responseDiagnostic.setStatus(HttpStatus.OK);
            return responseDiagnostic;

        }
		catch (final Exception e)
		{
			responseDiagnostic.setStatus(HttpStatus.BAD_REQUEST);
			responseDiagnostic.setMessage("Could not delete item");
			return responseDiagnostic;
        }
		
	}
	
	/**
	 * @param username
	 * @return
	 */
	private UserItem getUserItem(final String username) throws IndexOutOfBoundsException
	{
		try
		{
            //Create a DynamoDbTable object
            DynamoDbTable<UserItem> table = enhancedClient.table(USERS_TABLE, TableSchema.fromBean(UserItem.class));

            //Create a KEY object
            Key key = Key.builder()
                    .partitionValue(username)
                    .build();

            // Get the item by using the key
            return table.getItem(r -> r.key(key));

        }
		catch (Exception e)
		{
            throw new IndexOutOfBoundsException("Could not get user");
        }
		
	}
}

