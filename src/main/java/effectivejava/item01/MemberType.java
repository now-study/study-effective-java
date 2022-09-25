package main.java.effectivejava.item01;

import java.util.HashMap;
import java.util.Map;

public enum MemberType {

	ONLINE("online"), OFFLINE("offline"), DEFAULT("default");

	private final String type;

	// 사전에 맵을 생성하여 enum을 string으로 매핑한다.
	private static final Map<String, MemberType> typeMap = new HashMap<>();

	static {
		for (MemberType e : values()) {
			typeMap.put(e.getType(), e);
		}
	}

	MemberType(String type) {
		this.type = type;
	}

	// 사전 생성된 인스턴스를 호출만 하므로 새로운 인스턴스를 만들지 않는다.
	public static MemberType getByString(String type) {
		return typeMap.getOrDefault(type, DEFAULT);
	}

	public String getType() {
		return this.type;
	}
}
