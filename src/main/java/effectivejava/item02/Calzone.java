package main.java.effectivejava.item02;

public class Calzone extends Pizza {

	private final boolean sauceInside;

	public static class Builder extends Pizza.Builder<Builder> {

		private boolean sauceInside = false;

		// 소스 추가 여부는 옵션이므로 빌더 메서드로 추가한다.
		public Builder sauceInside() {
			this.sauceInside = true;
			return this;
		}

		@Override
		public Calzone build() {
			return new Calzone(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
	}

	private Calzone(Builder builder) {
		super(builder);
		this.sauceInside = builder.sauceInside;
	}

	public String toString() {
		// 보통 게터를 이용하지만, 게터 생성을 하지 않았으므로 내부 변수를 그대로 사용한다.
		return getClass().getName() + "(toppings=" + this.toppings.toString() + ", sauceInside=" + this.sauceInside + ")";
	}

}
