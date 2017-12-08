package edu.cmu.sv.ws.ssnoc.test;

import static com.eclipsesource.restfuse.Assert.assertBadRequest;
import static com.eclipsesource.restfuse.Assert.assertOk;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.MediaType;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;

@RunWith(HttpJUnitRunner.class)
public class MessageServiceIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:4321/ssnoc/message");

	@Context
	public Response response;

//	Exchange Information Use case Integration Testing
	/**
	 * Rest 1
	 */
	@HttpTest(method = Method.POST, path = "/gautam", type = MediaType.APPLICATION_JSON,
			content = "{\"from_userName\":\"gautam\",\"to_userName\":\" \",\"message\":\"Hey! what up\",\"message_timestamp\":\" \",\"location\":\" \"}")
	public void canPostMessageOnWall(){
		assertOk(response);
	}
	
	@HttpTest(method = Method.POST, path = "/blah", type = MediaType.APPLICATION_JSON, 
			content = "{\"from_userName\":\"blah\",\"to_userName\":\" \",\"message\":\"Hey! what up\",\"message_timestamp\":\" \",\"location\":\" \"}")
	public void anyUserCannotPostMessageOnWall(){
		assertBadRequest(response);
	}
	
	/**
	 * Rest 2
	 */
	@HttpTest(method = Method.GET, path = "/2")
	public void canGetMessageForID(){
		assertEquals(200, response.getStatus());
	}
	
	@HttpTest(method = Method.GET, path = "/-3")
	public void cannotGetMessageForID(){
		assertEquals(204, response.getStatus());
	}
	
	/**
	 * Rest 3
	 */
	@HttpTest(method = Method.POST, path = "/gautam/chang", type = MediaType.APPLICATION_JSON,
			content = "{\"from_userName\":\"gautam\",\"to_userName\":\"chang\",\"message\":\"Hey! this is a private chat\",\"message_timestamp\":\" \",\"location\":\" \"}")
	public void twoMembersCanPrivatelyChat(){
//		Created
		assertEquals(201, response.getStatus());
	}
	
	@HttpTest(method = Method.POST, path = "/gautam/blah", type = MediaType.APPLICATION_JSON,
			content = "{\"from_userName\":\"gautam\",\"to_userName\":\"blah\",\"message\":\"Hey! this is a private chat\",\"message_timestamp\":\" \",\"location\":\" \"}")
	public void nonMemberCannotPrivatelyChat(){
		assertBadRequest(response);
	}	
}