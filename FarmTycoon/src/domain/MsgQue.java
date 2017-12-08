package domain;

import java.util.Iterator;

import api.Message;

public class MsgQue implements Iterator<Message> {
	private static class Msg implements Message {
		public Msg next;
		String msg;
		long time;

		Msg(String msg, long time) {
			this.msg = msg;
			this.time = time;
		}

		@Override
		public long getTime() {
			return time;
		}

		@Override
		public String getMessage() {
			return msg;
		}
	}

	private static MsgQue que;

	Msg head;
	Msg tail;
	
	public static MsgQue get(){
		if(que==null)
			que=new MsgQue();
		return que;
	}

	public boolean hasNext() {
		return head != null;
	}

	public Message next() {
		Msg tmp = head;
		head = tmp.next;
		tmp.next = null;
		return tmp;
	}

	public void put(String msg, long timestamp) {
		if (head == null) {
			tail = head = new Msg(msg, timestamp);
		} else {
			tail.next = new Msg(msg, timestamp);
			tail = tail.next;
		}
		tail.next = null;
	}

	@Override
	public void remove() {}
}