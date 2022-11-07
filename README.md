# rvg
random value generator in Java

### TL;DR:
```java
class SomeValue {
  public final Integer i;
  public final String s;
  
  public SomeValue(int i, String s) {...}
  
  ...
}

...
import com.google.common.reflect.TypeToken;
import static aq.rvg.Operational.random;

SomeValue v = random(new TypeToken<SomeValue>() {});
assertThat(v.i).isNotNull();
assertThat(v.s).isNotNull();
```

##### fully supports generics
```java
class SomeValue {
  public final Integer i;
  public final String s;
  
  public SomeValue(int i, String s) {...}
  
  ...
}
class WithTypeArgs<T> {
  public final T t;
  
  public WithTypeArgs(T t) { ... }
  ...
}

...
import com.google.common.reflect.TypeToken;
import static aq.rvg.Operational.random;

WithTypeArgs<SomeValue> v = random(new TypeToken<WithTypeArgs<SomeValue>>() {});
assertThat(v.t.i).isNotNull();
assertThat(v.t.s).isNotNull();
```

##### can be configured 
```java
class SomeValue {
  public final Integer i;
  public final String s;
  
  public SomeValue(int i, String s) {...}
  
  ...
}

...
import com.google.common.reflect.TypeToken;
import static aq.rvg.Operational.random;
import static aq.rvg.Config.builder;

Config config = Config.builder()
                    .for_(String.class,
                    
                          // tt: new TypeToken<String>() {}
                          // c: config - the same one used on the top-level call to random
                          //
                          // All strings generated using config will be "hello world"
                          (tt, c) -> "hello world")
                          
                    .build();

SomeValue v = random(new TypeToken<SomeValue>() {}, config);
assertThat(v.i).isNotNull();
assertThat(v.s).isEqualTo("hello world");
```

### More details:
##### size of collections is configurable
```java
Config c = Config.builder()
               .collectionSize(5)
               ...
               .build();
```
##### leverage TypeToken and generics
```java
final class Either<Left, Right> {

    // all constructors are private. The only way to instantiante Either is with ...
    static <L, R> Either<L, R> left(L left) { ... }
    static <L, R> Either<L, R> right(R right) { ... }
    
    boolean isLeft() { ... }
    Left getLeft() { ... }
    
    boolean isRight() { ... }
    Right getRight() { ... }
    
    ...
}

class WithTypeArgs<T> {
  public final T t;
  
  public WithTypeArgs(T t) { ... }
  ...
}

...
import static aq.rvg.Operational.randomBoolean;
Config config = Config.builder()
                    .for_(Either.class,
                          (tt, c) -> {
                              val typeArg0 = tt.resolveType(Either.class.getTypeParameters()[0]);
                              val typeArg1 = tt.resolveType(Either.class.getTypeParameters()[1]);
                              return randomBoolean()
                                      ? Either.left(random(typeArg0, c))
                                      : Either.right(random(typeArg1, c));
                          })
                    .build();
val v = random(new TypeToken<WithTypeArgs<Either<Integer, String>>() {});
if (v.isLeft()) assertThat(v.getLeft()).isNotNull();
if (v.isRight()) assertThat(v.getRight()).isNotNull();
```
