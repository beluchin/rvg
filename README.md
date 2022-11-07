# rvg
random value generator in Java

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

* fully supports generics
* can be configured 
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
                          // c: config - the same one used on the call to random
                          (tt, c) -> "hello world")
                          
                    .build();

SomeValue v = random(new TypeToken<SomeValue>() {});
assertThat(v.i).isNotNull();
assertThat(v.s).isEqualTo("hello world");
```
