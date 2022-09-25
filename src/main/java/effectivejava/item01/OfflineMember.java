package main.java.effectivejava.item01;

public class OfflineMember extends Member {

	public OfflineMember(String username, String email) {
		super(username, email, OFFLINE_TYPE);
	}

}
