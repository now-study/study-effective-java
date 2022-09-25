package main.java.effectivejava.item02;

public class Coffee {

	// 필수 값이라고 하자.
	private int shot;

	private int milk;
	private int sugar;
	private int syrup;
	private int topping;

	public Coffee() {}

	public Coffee(int shot, int milk, int sugar, int syrup, int topping) {
		this.shot = shot;
		this.milk = milk;
		this.sugar = sugar;
		this.syrup = syrup;
		this.topping = topping;
	}

	// 점층적 생성자 패턴
	public Coffee(int shot, int milk) {
		this(shot, milk, 0, 0, 0);
	}

	public Coffee(int shot, int milk, int topping) {
		this(shot, milk, 0, 0, topping);
	}

	/* 다음과 같은 생성자는 이미 위에서 같은 시그니처의 생성자가 존재하므로 생성할 수 없다.
	public Coffee(int shot, int sugar) {
		this.shot = shot;
		this.sugar = sugar;
	}
	 */

	// Java-Beans 패턴
	public void setShot(int shot) {
		this.shot = shot;
	}

	public void setMilk(int milk) {
		this.milk = milk;
	}

	public void setSugar(int sugar) {
		this.sugar = sugar;
	}

	public void setSyrup(int syrup) {
		this.syrup = syrup;
	}

	public void setTopping(int topping) {
		this.topping = topping;
	}

	// 빌더 패턴
	private Coffee(Builder builder) {
		this.shot = builder.shot;
		this.milk = builder.milk;
		this.sugar = builder.sugar;
		this.syrup = builder.syrup;
		this.topping = builder.topping;
	}

	// 빌더 클래스 생성이 필요하다.
	public static class Builder {

		private final int shot;

		private int milk;
		private int sugar;
		private int syrup;
		private int topping;

		public Builder(int shot) {
			this.shot = shot;
		}

		public Builder milk(int milk) {
			this.milk = milk;
			return this;
		}

		public Builder sugar(int sugar) {
			this.sugar = sugar;
			return this;
		}

		public Builder syrup(int syrup) {
			this.syrup = syrup;
			return this;
		}

		public Builder topping(int topping) {
			this.topping = topping;
			return this;
		}

		public Coffee build() {
			return new Coffee(this);
		}
	}

	public String toString() {
		return getClass().getName() + "(shot=" + this.shot + ", milk=" + this.milk + ", sugar=" + this.sugar + ", syrup=" + this.syrup + ", topping=" + this.topping + ")";
	}

}
