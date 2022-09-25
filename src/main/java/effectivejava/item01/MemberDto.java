package main.java.effectivejava.item01;

import java.util.UUID;

public class MemberDto {

	private final UUID id;
	private final String username;
	private final String type;

	public MemberDto(UUID id, String username, String type) {
		this.id = id;
		this.username = username;
		this.type = type;
	}

	public static MemberDto from(Member member) {
		return new MemberDto(member.getId(), member.getUsername(), member.getType());
	}

	public UUID getId() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
	}

	public String getType() {
		return this.type;
	}

	public String toString() {
		return getClass().getName() + "(id=" + this.getId() + ", username=" + this.getUsername() + ", type=" + this.getType() + ")";
	}

}