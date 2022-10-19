# Item07. 다 쓴 객체 참조를 해지해라  

```
public class Stack {

    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) throw new EmptyStackException();
        return elements[--size];
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

- 이 코드에서 메모리 누수가 발생할 가능성이 있음
→ 스택에서 꺼내진 객체들을 가비지 컬렉터가 회수하지 않기 때문
- GC 회수
    - ![image](https://user-images.githubusercontent.com/25292715/196627634-7e8406e7-d26c-45ff-a5a7-ab89d54f658a.png)  
      출처) https://madplay.github.io/post/java-garbage-collection-and-java-reference
    - 유효 scope 밖으로 넘어가게되면 (=Unreachable Object) 자동으로 GC의 대상이 됨.
    - 초록색으로 표시된 부분은 Reachable Object, 붉은색으로 표시된 부분은 Unreachable Object
    즉 붉은색으로 표시된 객체들이 GC의 대상이 됨
- pop 을 해서 스택의 크기를 줄이더라도, 여전히 배열에 값을 가지기 때문에 참조를 가져 GC에 의해 처리되지 못함. 이게 쌓이다보면 성능에 악영향
- 간단하게 다 쓴 참조를 null 처리 하면 해결됨
    - 그렇다고 모든 객체의 사용이 완료될 때마다 null 처리를 하는 것은 바람직하지 않음
  
  
### 메모리 누수를 주의해야 하는 경우

- 자기 메모리를 직접 관리하는 클래스
- 캐시
    - 객체 참조를 캐시에 넣고 잊게 되는 경우
    - 해결법
        - 캐시 외부에서 key를 참조하는 동안만 엔트라기 살아있는 캐시가 필요할 경우 WeakHashMap 사용
        - 시간이 지날수록 엔터리의 가치를 떨어트리는 방식. 이 때 쓰지 않는 엔트리의 청소 필요
            - ScheduledThreadPoolExecutor 같은 백그라운드 스레드 활용
            - 캐시에 새 엔트리를 추가할 때 부수 작업으로 수행 (LinkedHashMap의 removeEldestEntry)
- 리스너, 콜백
    - 콜백을 등록만 하고 명확히 해지하지 않는 경우
    - 콜백을 약한 참조로 저장하면 GC가  즉시 수거를 함 (ex. WeakHashMap 사용)
