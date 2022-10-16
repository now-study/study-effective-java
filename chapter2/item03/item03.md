# 아이템 3: private 생성자 또는 enum 타입을 사용해서 싱글톤으로 만들 것

오직 한 인스턴스만 만드는 클래스를 *싱글톤*이라 부른다.

싱글톤으로 만드는 두가지 방법이 있는데, 두 방법 모두 생성자를 private 으로 만들고 publis static 멤버를 사용해서 유일한 인스턴스를 제공한다.

## final 필드

첫 번째 방법은 final 필드로 제공한다.

```java
public class Elvis {

    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {
    }

}
```
- private 생성자는 public static final 필드인 Elvis.INSTANCE를 초기화할 때 딱 한번 호출
- 유일한 private 생성자. -> Elivis 클래스가 초기화 될 때 만들어진 인스턴스가 전체 시스템에서 하나뿐임을 보장
- 리플렉션 API인 AccessibleObject.setAccessible을 사용하면 private 생성자 호출 가능. 이를 방어하기 위해서 두번째 생성자 호출 시 예외 던짐
```java
private Elvis() {
  count++;
  if (count > 1) {
    throw new IllegalStateException("this object should be singleton");
  }
}
```

### 장점

이런 API 사용이 static 팩토리 메소드를 사용하는 방법에 비해 더 명확하고 더 간단하다.


## static 팩토리 메소드

```java
public class Elvis {

    private static final Elvis INSTANCE = new Elvis();

    private Elvis() {
    }

    public static Elvis getInstance() {
        return INSTANCE;
    }

}
```

### 장점

API를 변경하지 않고로 싱글톤으로 쓸지 안쓸지 변경할 수 있다. 처음엔 싱글톤으로 쓰다가 나중엔 쓰레드당 새 인스턴스를 만든다는 등 클라이언트 코드를 고치지 않고도 변경할 수 있다.
- API -> public static method를 의미
- 변경 예시
```java
    public static Elvis getInstance() {
        //return INSTANCE;
        return new Elvis();
    }

```
- 싱글톤으로 안쓴다고 변경을 하더라도 위의 예시와 같이 static factory 메서드의 코드만 변경하면 client code(main 등)은 변경하지 않아도 됨.

필요하다면 `Generic 싱글톤 팩토리`(아이템 30)를 만들 수도 있다.

static 팩토리 메소드를 `Supplier<Elvis>`에 대한 `메소드 레퍼런스`로 사용할 수도 있다. ex) Elvis::getInstance

> Supplier<Elvis> <br>
Java8부터 사용하기 시작한 인터페이스. T get(); T를 리턴하는 인터페이스.


## 직렬화 (Serialization)

위에서 살펴본 두 방법 모두, 직렬화에 사용한다면 `역직렬화 할 때 같은 타입의 인스턴스가 여러개` 생길 수 있다. 그 문제를 해결하려면 모든 인스턴스 필드에 `transient`를 추가 (직렬화 하지 않겠다는 뜻) 하고 `readResolve` 메소드를 다음과 같이 구현하면 된다.

```java
    private Object readResolve() {
        return INSTANCE;
    }

```
[issue.[item #03] 싱글턴 클래스의 역직렬화는 말로만 역직렬화인건가요?](https://github.com/now-study/study-effective-java/issues/16)

## Enum

직렬화/역직렬화 할 때 코딩으로 문제를 해결할 필요도 없고, 리플렉션으로 호출되는 문제도 고민할 필요없는 방법이 있다.
- Enum의 직렬화는 JVM이 보장한다.
- Enum의 경우 클래스의 생성자는 내부에서만 액세스가 가능

```java
public enum Elvis {
    INSTANCE;
}
```

코드는 좀 불편하게 느껴지지만 싱글톤을 구현하는 최선의 방법이다. 하지만 이 방법은 Enum 말고 다른 상위 클래스를 상속해야 한다면 사용할 수 없다. (하지만 인터페스는 구현할 수 있다.)

[issue.[item #03] 싱글턴의 목적으로 열거타입을 사용하는 이유](https://github.com/now-study/study-effective-java/issues/13)

참고 : https://github.com/keesun/study/blob/master/effective-java/item3.md