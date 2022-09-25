package main.java.effectivejava.item01;

public class MemberTest {

	public static void main(String[] args) {

		System.out.println("# Generator Test");
		Member memberG1 = new OnlineMember("member_g1", "member_g1@opsnow.com");
		Member memberG2 = new OfflineMember("member_g2", "member_g2@opsnow.com");
		Member memberG3 = new Member("member_g3", "member_g3@opsnow.com", "default");

		System.out.println(memberG1);
		System.out.println(memberG2);
		System.out.println(memberG3);
		System.out.println();

		System.out.println("# Static Factory Method Test");
		// 하위 클래스를 반환할 수 있다.
		Member memberS1 = Member.newInstance("member_s1", "member_s1@opsnow.com", "online");
		Member memberS2 = Member.newInstance("member_s2", "member_s2@opsnow.com", "offline");
		Member memberS3 = Member.newInstance("member_s3", "member_s3@opsnow.com", "general");
		// 이름을 지정해서 사용할 수 있다.
		Member memberS4 = Member.newOnlineMember("member_s4", "member_s4@opsnow.com");
		Member memberS5 = Member.newOfflineMember("member_s5", "member_s5@opsnow.com");

		System.out.println(memberS1);
		System.out.println(memberS2);
		System.out.println(memberS3);
		System.out.println(memberS4);
		System.out.println(memberS5);
		System.out.println();

		System.out.println("# DTO encapsulation");
		MemberDto memberS1Dto = new MemberDto(memberS1.getId(), memberS1.getUsername(), memberS1.getType());
		// 데이터 접근에 대한 구현을 클래스 내부로 감출 수 있다.
		MemberDto memberS2Dto = MemberDto.from(memberS2);

		System.out.println(memberS1Dto);
		System.out.println(memberS2Dto);

	}

}
