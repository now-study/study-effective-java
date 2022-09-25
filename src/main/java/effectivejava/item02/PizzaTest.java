package main.java.effectivejava.item02;

public class PizzaTest {

	public static void main(String[] args) {

		System.out.println("# Pizza 클래스 테스트");

		NyPizza pizza = new NyPizza.Builder(NyPizza.Size.MEDIUM)
				.addTopping(Pizza.Topping.HAM)
				.addTopping(Pizza.Topping.ONION)
				.addTopping(Pizza.Topping.MUSHROOM)
				.build();
		System.out.println(pizza);

		Calzone calzone = new Calzone.Builder()
				.addTopping(Pizza.Topping.HAM)
				.addTopping(Pizza.Topping.PEPPER)
				.addTopping(Pizza.Topping.SAUSAGE)
				.sauceInside()
				.build();
		System.out.println(calzone);

	}

}
