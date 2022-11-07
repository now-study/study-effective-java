# 상속과 컴포지션
- 상속 - 하위 클래스가 상위 클래스의 특성을 재정의 한것 > (IS-A) 관계
- 컴포지션 - 기존 클래스가 새로운 클래스의 구성요소가 되는것 > (HAS-A) 관계

# 상속의 단점

1. 캡슐화를 위반한다.
2. 설계에 유연하지 못하다.
3. 구조가 복잡해질 수록 영향에 대한 예측이 힘들고 사용에 어려움이 생길 수 있음

# 오류의 가능성이 있는 상속 케이스

1. 내부구현 방식(자기사용)에 따라 오동작할 수 있음

    ```java
    package com.soojung.effectivejava.item18;
    
    import java.util.Collection;
    import java.util.HashSet;
    import java.util.List;
    
    public class InstrumentedHashSet<E> extends HashSet<E> {
    
        private int addCount = 0;
    
        public AbstractCollection() {
        }
    
        public InstrumentedHashSet(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }
    
        @Override
        public boolean add(E e) {
            addCount++;
            return super.add(e);
        }
    
        @Override
        public boolean addAll(Collection<? extends E> c) {
            addCount += c.size();
            return super.addAll(c);
        }
    
        public int getAddCount() {
            return addCount;
        }
    
        public static void main(String[] args) {
            InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
            s.addAll(List.of("냠", "뇸", "냠냠"));
    
            System.out.println(s.getAddCount());
        }
    }
    
    // 출력 : 6 - 3을 기대하지만 실제로는 6을 반환함 
    
    // AbstractCollection.java
    // addAll 동작 방식
    // 내부에서 add(InstrumentedHashSet에서 재정의)를 다시 호출하기 때문에 오류 발생
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }
    ```

    <aside>
    🚫 addAll을 재정의 하지 않으면 당장은 해결되지만 자신의 다른 부분을 사용하는 ‘자기사용(self-use)’ 여부는 해당 클래스의 내부 구현 방식에 해당하며 다음 릴리스에도 유효할지 알 수 없음

   →이런 가정에 기대면 결국 깨지기 쉬운 객체가 만들어짐

    </aside>

    <aside>
    🚫 다른식으로 재정의 : 원소 하나당 add 한번씩만 하도록 ..

   → 상위 클래스의 메서드 동작을 다시 구현하는 방식은 어렵고, 시간도 많이 들고, 오류를 내거나 성능을 떨어트릴 수도 있다. 또 private 필드인 경우에는 구현 자체가 불가능해진다.

    </aside>

2. 하위 클래스의 캡슐화가 깨져버리는 경우가 생길 수 있음
- 원소를 추가하는데 특정한 제약조건이 있는 컬렉션이 있을 때 상위 클래스에 새롭게 원소를 추가하는 메서드가 생긴다면?
    - 보안상의 구멍이 생길 수 있다.
    - `HashTable`과 `Vector`를 컬렉션에 추가하자, 심각한 보안 구멍이 생긴적이 있다.
- 상위 클래스에 추가된 메서드가 만일 하위 클래스에 추가한 메서드와 시그니처가 같고 반환 타입만 다르다면?
    - 이 경우엔 컴파일 에러가 먼저 뜰 것이다.
    - 반환 타입 마저 같다고 하면 상위 클래스의 새 메서드를 재정의한 꼴이 된다.

# 상속의 단점을 개선한 방법 : 컴포지션

위와같은 상속의 단점으로 기존의 클래스를 확장한는 것이 아니라,****

**새로운 클래스를 만들고 private 필드로 기존 클래스의 인스턴스를 참조**하게하는 컴포지션 방식을 사용한다.

이러한 방식을 **forwarding(전달)** 한다 라고 하며, 새로운 클래스는 기존의 클래스의 구현방식을 벗어나고, 기존 클래스에 새로운 메소드가 추가되더라도 전혀 영향을 받지 않는다.

HashSet 클래스는 기능을 규정하는 Set 인터페이스가 있으므로 CustomHashSet 클래스를 Forwarding 방식으로 구현할 수 있다.

```java
package com.soojung.effectivejava.item18;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InstrumentedSet<E> extends ForwardingSet<E> {
    private int addCount = 0;

    public InstrumentedSet(Set<E> s) {
        super(s);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }

    public static void main(String[] args) {
        InstrumentedSet<String> s = new InstrumentedSet(new HashSet());
        s.addAll(List.of("냠", "뇸", "냠냠"));

        System.out.println(s.getAddCount());
    }
}

package com.soojung.effectivejava.item18;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;

public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;

    public ForwardingSet(Set<E> s) {
        this.s = s;
    }

    @Override
    public Spliterator<E> spliterator() {
        return Set.super.spliterator();
    }

    @Override
    public int size() {
        return s.size();
    }

    @Override
    public boolean isEmpty() {
        return s.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return s.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return s.iterator();
    }

    @Override
    public Object[] toArray() {
        return s.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return s.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return s.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return s.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return s.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return s.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return s.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return s.removeAll(c);
    }

    @Override
    public void clear() {
        s.clear();
    }
}
```

Set 인터페이스를 구현하는 전달 클래스(Forwarding Class)를 상속하며 InstrumentedSet 클래스는 Set 인터페이스를 구현하며 Set객체를 인자로 받는 생성자를 가지고 있다.

→ InstrumentedSet 클래스는 어떤 Set 객체를 인자로 받아, 필요한 기능을 가지고있는 Set 객체로 변환시켜주는 역할을 한다. 이러한 클래스를 래퍼(Wrapper) 클래스라고 한다. 다른 Set 객체를 포장하고 있다라는 의미이다. 계속 기능을 덧씌운다는 뜻에서 데코레이터(decorator) 패턴이라고 한다.

> 컴포지션 + 전달 = 위임
>
- 엄밀히 따지면 래퍼 객체가 내부 객체에 자기 자신의 참조를 넘기는 경우만 위임에 해당

<aside>
🚫 **래퍼 클래스와 SELF 문제**

- 콜백 프레임워크에서는 자기 자신의 참조를 다른 객체에 넘겨서 다음 호출(콜백) 때 사용하도록 한다.
- 내부 객체는 자신을 감싸고 있는 래퍼의 존재를 모르니 대신 자신(this)의 참조를 넘기고, 콜백 때는 래퍼가 아닌 내부 객체를 호출하게 되는데, 이를 SELF 문제라고 한다.
</aside>

# **상속과 is-a**

- 상속은 반드시 하위 클래스가 상위 클래스의 '진짜' 하위 타입인 상황에서만 쓰여야 한다.
- 즉, 클래스 B가 클래스 A와 is-a 관계일 때만 클래스 A를 상속해야 한다.
- 만약 is-a 관계가 아니라면 A는 B의 필수 구성요소가 아니라 구현하는 방법 중 하나일 뿐이다.

# 상속을 사용할 때는 주의해야 한다.

- 컴포지션을 써야 할 상황에서 상속을 사용하는 건 내부 구현을 불필요하게 노출하는 것과 같다.
    - 클라이언트에서 노출된 내부에 직접 접근할 수 있게 된다.
    - 사용자를 혼란스럽게 한다.
    - 최악의 경우, 클라이언트에서 상위 클래스를 직접 수정하여 하위 클래스의 불변식을 해칠 수도 있다.
        - Properties - getProperty(key) : 키와 값으로 문자열만 허용 / get(key) : Object 허용
- 상속은 상위 클래스의 API를 '그 결함까지도' 승계하므로 주의해야 한다.

# **핵심 정리**

- 상속은 강력하지만 캡슐화를 해친다는 문제가 있다.
- 상속은 상위 클래스와 하위 클래스가 순수한 is-a 관계일 때만 써야 한다.
    - is-a 관계일 때도 안심할 수만은 없다. 하위 클래스의 패키지가 상위 클래스와 다르고, 상위 클래스가 확장을 고려해 설계되지 않았을 수도 있기 때문이다.
- 상속의 취약점을 피하려면 상속 대신 컴포지션과 전달을 사용한다. 특히 래퍼 클래스로 구현할 적당한 인터페이스가 있다면 더욱 그렇다. 래퍼 클래스는 하위 클래스보다 견고하고 강력하다.