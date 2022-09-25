package main.java.effectivejava.item01;

import java.util.UUID;

public class Member {

	protected static final String OFFLINE_TYPE = "offline";
	protected static final String ONLINE_TYPE = "online";

	private final UUID id;
	private final String username;
	private final String email;
	private final MemberType type;

	{
		this.id = UUID.randomUUID();
	}

	// 생성자는 하나의 시그니처에 대응한다.
	public Member(String username, String email, String type) {
		this.username = username;
		this.email = email;
		this.type = MemberType.getByString(type);
	}

	// 정적 팩토리 메소드는 이름을 가지고 각각의 시그니처에 대응할 수 있다.
	public static Member newOnlineMember(String username, String email) {
		return new OnlineMember(username, email);
	}

	public static Member newOnlineByUsername(String username) {
		return new OnlineMember(username, "");
	}

	public static Member newOfflineMember(String username, String email) {
		return new OfflineMember(username, email);
	}

	// 매개 변수에 따라서 다른 클래스 객체를 반환할 수 있다.
	// 정적 메서드를 public으로 노출하므로 생성자를 private으로 감출 수 있다.
	public static Member newInstance(String username, String email, String type) {
		if (type.equals(ONLINE_TYPE)) {
			return new OnlineMember(username, email);
		} else if (type.equals(OFFLINE_TYPE)) {
			return new OfflineMember(username, email);
		} else {
			return new Member(username, email, type);
		}
	}

	public UUID getId() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
	}

	public String getEmail() {
		return this.email;
	}

	public String getType() {
		return this.type.getType();
	}

	public String toString() {
		return getClass().getName() + "(id=" + this.getId() + ", username=" + this.getUsername() + ", email=" + this.getEmail() + ", type=" + this.getType() + ")";
	}

}