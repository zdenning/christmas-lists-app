package com.denning.demo.validator;

import org.springframework.jdbc.core.JdbcTemplate;
import com.denning.demo.dynamodb.DynamoDBClientV2;

public class Validator
{
	private JdbcTemplate jdbcTemplate;
	
//	@Autowired
	private DynamoDBClientV2 dynamoDBClientV2 = new DynamoDBClientV2();
	
	
	/**
	 * @param jdbcTemplate
	 */
	public Validator(final JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public boolean validateUser(final String username)
	{
		try 
		{
			dynamoDBClientV2.getUser(username);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
}
