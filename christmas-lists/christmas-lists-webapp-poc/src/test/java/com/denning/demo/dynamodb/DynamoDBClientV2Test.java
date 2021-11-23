package com.denning.demo.dynamodb;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.denning.demo.exception.ResponseDiagnostic;
import com.denning.demo.model.Gift;
import com.denning.demo.model.User;


public class DynamoDBClientV2Test
{
	private DynamoDBClientV2 client;

	@Before
	public void init()
	{
		client = new DynamoDBClientV2();
	}
	
	@Test
	public void testGetUser()
	{
		User result = null;
		try
		{
			result = client.getUser("emdenning");
			assertEquals("emdenning", result.getUsername());
			assertEquals("ilovedogs", result.getPassword());
			assertEquals("Erin", result.getFirstName());
			System.out.println(" ******************** \n" + result);
		}
		catch (Exception e)
		{
			Assert.fail();
		}
		
	}

	@Test
	public void testGetAllUsers()
	{
		final List<User> result = client.getAllUsers();
		
		assertEquals(4, result.size());
		System.out.println(" ******************** \n" + result);
	}

	@Test
	public void testAddUserDuplicate()
	{
		final ResponseDiagnostic responseDiagnostic = client.addUser("testUsername","testName", "testPassword");
		assertEquals(HttpStatus.BAD_REQUEST, responseDiagnostic.getStatus());
		System.out.println(" ******************** \n" + responseDiagnostic);
	}
	
	@Test
	public void testGetUserList()
	{
		final List<Gift> giftList = client.getUserList("emdenning");
		assertTrue(!giftList.isEmpty());
		System.out.println(" ******************** \n" + giftList);
	}
	
	@Test
	public void testGetUserEmptyList()
	{
		final List<Gift> giftList = client.getUserList("testUsername");
		assertNull(giftList);
	}
	
	@Test
	public void testBuyItem()
	{
		final Gift gift = new Gift();
		gift.setName("Piano");
		final ResponseDiagnostic response = client.buyGift("bigZac", gift);
		assertEquals(Integer.valueOf(200), response.getHttpCode());
	}
}
