package main.java.effectivejava.item02;

public class CoffeeTest {

	public static void main(String[] args) {

		System.out.println("# 점층적 생성자 패턴");

		Coffee latte = new Coffee(1, 1, 0, 0, 0);
		Coffee americano = new Coffee(1, 0);
		// 설탕을 넣고 싶지만 설탕만 포함하는 생성자가 없으므로 기본 생성자를 이용해야 하며, 필요 없는 매개변수를 전달해야 한다.
		Coffee sweetAmericano = new Coffee(1, 0, 1, 0, 0);

		System.out.println(latte);
		System.out.println(americano);
		System.out.println(sweetAmericano);
		System.out.println();

		System.out.println("# Java-Beans 패턴");

		Coffee custom = new Coffee();
		// shot이 필수로 필요하지만 메소드 호출을 누락할 수 있다.
		custom.setShot(2);
		custom.setMilk(1);
		custom.setTopping(1);

		System.out.println(custom);
		System.out.println();

		System.out.println("# 빌더 패턴");

		// 필수값을 빌더 생성시에 강제하고 필요한 변수에 대해서만 손쉽게 추가가 가능하다.
		Coffee sweetSweetAmericano = new Coffee.Builder(2)
				.sugar(1)
				.syrup(1)
				.build();

		System.out.println(sweetSweetAmericano);
	}
}
