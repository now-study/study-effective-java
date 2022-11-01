# 아이템 11 : equals를 재정의하려거든 hashcode도 재정의하라

### Object 명세에서의 HashCode 규약

- equals() 비교에 사용되는 필드가 변하지 않았다면, hashcode() 메서드는 몇 번을 호출하든 항상 같은 값을 반환해야한다. (애플리케이션 재시작한 경우에는 달라질 수 있음)
- equals(Object)가 두 객체 같다고 판단 → 두 객체의 hashCode는 똑같은 값을 반환해야 한다.
- equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode()가 다른 값을 반환할 필요는 없다. (단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아짐)


논리적으로 같은 객체는 같은 해시코드를 반환해야한다.

반대 예제

```java
Map<PhoneNumber, String> m = new HashMap<>();
m.put(new PhoneNumber(010,1111,1111), "제니");
m.get(new PhoneNumber(010,1111,1111)) // 제니가 나와야할 것 같지만 실상은 null
```

생성자에 같은 값을 주어 생성한 PhoneNumber이 서로 다르다고 판단.

Object의 Default hashcode 메서드는 이 둘이 전혀 다르다고 판단하여 서로 다른 값을 반환한다.

PhoneNumber 클래스는 해시코드를 재정의 하지 않았기 때문에 논리적으로 같지만 서로 다른 해시코드 반환 (2번째 규약 지키지 못함)

**HashMap은 해시코드가 다른 엔트리끼리는 아예 동치성 비교를 시도조차 하지 않도록 최적화** 되어 있다.

해결법.

PhoneNumber에 적절한 hashCode 메서드만 작성해주면 해결

```java
//최악의 hashcode 구현 - 사용 금지
@Override
public int hashCode() { return 42; }
```

위 코드는 동치인 모든 객체에서 똑같은 해시코드를 반환하니 원하는대로 동작은 한다.

하지만, 모든 객체가 해시테이블의 버킷 하나에 담겨 객체가 많아질수록 해시 테이블의 성능이 매우 떨어진다. (O(1)의 시간 복잡도가 점차 링크드 리스트처럼 O(n)으로 ..)

좋은 해시 함수란, 서로 다른 인스턴스에 다른 해시코드를 반환한다. 서로 다른 인스턴스들을 32비트 정수 범위에 균일하게 분배해야한다.

### 좋은 HashCode를 작성하는 법?

1. int 변수 result를 선언한 후 값c로 초기화한다.
    - 이때 c는 해당 객체의 첫번째 핵심 필드를 계산한 것 (핵심필드 : equals 비교에 사용되는 필드)
2. 해당 필드의 해시코드 c를 계산
    1. 기본 타입 필드,
       Type.hashCode(f)를 수행. (Type은 해당 기본 타입의 박싱 클래스)
    2. 참조 타입,
       참조 타입의 hashCode 호출. 필드의 값이 null이면 0 사용
       필드면서 이 클래스의 equals 메서드가 이 필드의 equals를 재귀적으로 호출해 비교한다면, 이 필드의 hashCode를 재귀적으로 호출한다. 계산이 복잡해질 것 같으면 이 필드의 표준형을 만들어 그 표준형의 hashCode를 호출한다. 필드의 값이 null이면 0을 사용한다.
    3. 필드가 배열이라면,
       핵심 원소 각각을 별도 필드처럼 다룬다. 배열에 핵심 원소가 하나도 없다면 단순히 상수 0을 사용한다. 모든 원소가 핵심 원소라면 Arrays.hashCode를 사용한다.
    4. 위에서 계산한 해시코드 c로 result를 갱신한다.
       result = 31 * result + C;
3. result를 반환한다.

   위와 같이 구현 → Person class를 hashCode 재구현

    ```java
    public class HashCodeOverridePersonExample {
        public static void main(String[] args) {
            Person p1 = new Person("123456-1234567", "coco" , 28);
            Person p2 = new Person("123456-1234567", "coco" , 28);
    
            HashMap hashMap = new HashMap();
            hashMap.put(p1, "cocoVal");
    
            String o = (String)hashMap.get(p1);
            System.out.println("p1을 통해 get = " + o + "hash = " + hashMap.get(p1).hashCode()); //cocoVal

            String o2 = (String)hashMap.get(p2);
            System.out.println("p2를 통해 get = " + o2 + "hash = " + hashMap.get(p2).hashCode()); //cocoVal
        }
    }
    
    class Person {
        private String idNum;
        private String name;
        private int age;
    
        public Person(String idNum, String name, int age) {
            this.idNum = idNum;
            this.name = name;
            this.age = age;
        }
    
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Person)) {
                return false;
            }
    
            Person person = (Person) o;
    
            return this.idNum.equals(person.idNum) && this.name.equals(person.name) && this.age == person.age;
        }
    
        @Override
        public int hashCode() {
    
            int defaultBit = 31;
            int result = this.idNum.hashCode();
    
            result = defaultBit * this.name.hashCode() + result;
            result = defaultBit * Integer.hashCode(this.age) + result;
    
            return result;
        }
    }
    ```

   p1, p2가 cocoVal로 같은 값 잘 return.
   eqauls에 사용한 핵심 필드만 사용하여 hashCode 구현!


Object 클래스는 임의의 개수만큼 객체를 받아 해시코드를 계산해주는 `정적 메서드 hash`를 제공

```java
@Oberride public int hashCode() {
	return Objects.hash(lineNum, prefix, areaCode);
}
```

한 줄로 작성할 수 있지만, 속도가 더 느림.

입력 인수를 담기위한 배열이 만들어지고, 기본 타입이 입력중 있다면 박싱과 언방싱도 거침.

그렇기 때문에 성능에 민감하지 않은 상황에서 사용 고려..

### 해시코드를 캐싱하는 방식

클래스가 불변이고 해시코드를 계산하는 비용이 크다면, 매번 새로 계산하기보다는 캐싱하는 방식을 고려. (필드에 값을 저장해 놓고 재활용)

LazyLoad 방식. Thread Safe 고려 필요

```java
private int hashCode; //0으로 초기화
@Override
public int hashCode() {
  int result = hashCode;

  if(result == 0) {
    result = Short.hashCode(areaCode);
    result = 31 * result + Short.hashCode(prefix);
    result = 31 * result + Short.hashCode(lineNum);
    hashCode = result;
  }

  return result;
}
```

### 정리

equals()를 재정의할 때는 반드시 hashcode()도 재정의하자

hashcode()를 정의할 때는 반드시 Object의 API 문서에 기술된 일반 규약을 따라라

서로 다른 인스턴스라면 되도록 해시코드도 서로 다르게 구현해라
