package edu.cmu.sv.ws.ssnoc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;

@RunWith(HttpJUnitRunner.class)
public class MessagesServiceIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:4321/ssnoc/messages");

	@Context
	public Response response;

//	Exchange Information Use case Integration Testing
	/**
	 * Rest 5
	 */
	@HttpTest(method = Method.GET, path = "/wall")
	public void canGetWallMessages(){
		assertEquals(200, response.getStatus());
	}
	
	@HttpTest(method = Method.GET, path = "/wall/blah")
	public void noContentForWall(){
		assertEquals(204, response.getStatus());
	}
	
	/**
	 * Rest 6
	 */
	@HttpTest(method = Method.GET, path = "/gautam/chang")
	public void getPrivateMessagesBetweenUsers(){
		assertNotNull(response.getBody());
	}
	
	@HttpTest(method = Method.GET, path = "/gautam/blah")
	public void shouldNotGetChatsBetweenNonmembers(){
		assertEquals(204, response.getStatus());
	}	
}