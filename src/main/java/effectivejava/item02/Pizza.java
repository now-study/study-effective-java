package main.java.effectivejava.item02;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public abstract class Pizza {

	public enum Topping {
		HAM, MUSHROOM, ONION, PEPPER, SAUSAGE
	}

	final Set<Topping> toppings;

	abstract static class Builder<T extends Builder<T>> {

		// 비어있는 EnumSet을 생성한다.
		EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

		// 자식 클래스에서 모두 사용될 topping 추가를 위한 빌더 메서드를 추가한다.
		public T addTopping(Topping topping) {
			toppings.add(Objects.requireNonNull(topping));
			return self();
		}

		abstract Pizza build();

		// 부모 클래서에서 this를 자식 클래스에 제공할 수 없으므로 자식 클래스에서 this를 오버라이드한다.
		protected abstract T self();
	}

	Pizza(Builder<?> builder) {
		toppings = builder.toppings.clone();
	}

}
