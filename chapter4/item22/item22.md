# Item22. 인터페이스 타입을 정의하는 용도로만 사용하라

---

## 인터페이스

- 자신을 구현한 클래스의 인스턴스를 참조할 수 있는 타입역할
- 클래스가 어떤 인터페이스를 구현한다는 것은 자신의 인스턴스로 무엇을 할 수 있는지 클라이언트에 얘기해주는 것

→ 인터페이스는 오직 이 용도로만 사용

## 지침 반례 상수 인터페이스

- 메서드 없이 상수를 뜻하는 static final 필드로 가득찬 인터페이스
- 상수들을 사용하려는 클래스에서는 정규화된 이름을 쓰는 것을 피하고자 그 인터페이스를 구현

### 상수 인터페이스 안티패턴

```java
public interface PhisicalConstants {
	// 아보가드로 수 (1/몰)
	static final double AVOGADROS_NUMBER = 6.022_140_857e23;

	// 볼츠만 상수 (J/K)
	static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;

	// 전자 질량 (kg)
	static final double ELECTRON_MASS = 9.109_383_56e-31;
}
```

상수 인터페이스 → 인터페이스를 잘못 사용한 예

1. 클래스 내부에서 사용하는 상수는 외부 인터페이스가 아니라 내부 구현에 해당.
   → 상수 인터페이스를 구현하는 것은 내부 구현을 클래스의 API로 노출하는 행위
2. 혼란 야기 → 클라이언트 코드가 내부 구현에 해당하는 이 상수들에게 종속
3. 상수들을 더는 쓰지 않게 되더라도 바이너리 호환성을 위해 여전히 상수 인터페이스를 구현하고 있어야함. 이때 final이 아닌 클래스가 상수 인터페이스를 구현한다면 모든 하위 클래스의 이름 공간이 그 인터페이스가 정의한 상수들로 오염되어 버린다.
4. java.io.ObjectStreamConstant 자바 플랫폼 라이브러리의 상수 인터페이스 예시
   1. [i](https://velog.io/@kasania/Java-Constant-interface)nterface [https://docs.oracle.com/javase/1.5.0/docs/api/java/io/ObjectStreamConstants.html](https://docs.oracle.com/javase/1.5.0/docs/api/java/io/ObjectStreamConstants.html)
   2. 구현
      1. [https://docs.oracle.com/javase/1.5.0/docs/api/java/io/ObjectInputStream.html](https://docs.oracle.com/javase/1.5.0/docs/api/java/io/ObjectInputStream.html)
      2. [https://docs.oracle.com/javase/1.5.0/docs/api/java/io/ObjectOutputStream.html](https://docs.oracle.com/javase/1.5.0/docs/api/java/io/ObjectOutputStream.html)

## 상수를 공개할 목적이라면

1. 특정 클래스나 인터페이스에 강하게 연관된 상수라면 그 클래스나 인터페이스 자체에 추가
   ex) 모든 숫자 기본타입의 박싱 클래스 Integer, Double에 선언된 MIN_VALUE, MAX_VALUE

   ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ca42628d-0c3c-44c8-8d07-6bb46ee6fa32/Untitled.png)

2. 열거 타입으로 나타내기 적합한 상수라면 열거 타입으로 만들어 공개하면 된다.
3. 인스턴스화 할 수 없는 유틸리티 클래스에 담아 공개

    ```java
    package effectivejava.item22.constantutilityclass;
    
    public class PhysicalConstants {
    	private PhysicalConstants() {} //인스턴스화 방지
    	
    	// 아보가드로 수 (1/몰)
    	public static final double AVOGADROS_NUMBER = 6.022_140_857e23;
    
    	// 볼츠만 상수 (J/K)
    	public static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;
    
    	// 전자 질량 (kg)
    	public static final double ELECTRON_MASS = 9.109_383_56e-31;
    }
    ```

### 정적 임포트

- 유틸리티 클래스에 정의된 상수를 클라이언트에서 사용하려면 클래스 이름까지 함께 명시해야한다.

    ```java
    public class Test {
    	double atom(double mols) {
    		return PhysicalConstants.AVOGADROS_NUMBER * mols;
    	}
    }
    ```

- 클래스의 상수를 빈번히 사용한다면 정적 임포트(static import)하여 클래스 이름 생략 가능

    ```java
    import static effectivejava.item22.constantutilityclass.PhysicalConstants.*;
    
    public class Test {
    	double atom(double mols) {
    		return AVOGADROS_NUMBER * mols;
    	}
    }
    ```

- 코드를 줄이기 위해 정적 임포트 사용하지만 혼란을 야기할 수 있다. 예를 들어 AVOGADROS_NUMBER라는 상수가 다른 Class에 또 있다면..?
- 호불호가 갈린다 → Coding Convention Check Point~

## 정리

인터페이스는 타입을 정의하는 용도로만 사용해야한다. 상수 공개용 수단으로 사용하지 말자.