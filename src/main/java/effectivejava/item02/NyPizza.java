package main.java.effectivejava.item02;

import java.util.Objects;

public class NyPizza extends Pizza {

	public enum Size {
		SMALL, MEDIUM, LARGE
	}

	private final Size size;

	public static class Builder extends Pizza.Builder<Builder> {

		private final Size size;

		// size가 필수 값이므로 빌더 생성자에서 size를 받는다.
		public Builder(Size size) {
			// size 값이 NonNull임을 확인한다.
			this.size = Objects.requireNonNull(size);
		}

		@Override
		public NyPizza build() {
			return new NyPizza(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}

	private NyPizza(Builder builder) {
		super(builder);
		this.size = builder.size;
	}

	public String toString() {
		// 보통 게터를 이용하지만, 게터 생성을 하지 않았으므로 내부 변수를 그대로 사용한다.
		return getClass().getName() + "(toppings=" + this.toppings.toString() + ", size=" + this.size + ")";
	}

}
