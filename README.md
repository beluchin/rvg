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
SomeValue v = aq.rvg.Operational.random(new TypeToken<SomeValue>() {});
assertThat(v.i).isNotNull();
assertThat(v.s).isNotNull();
```

* fully supports generics
* can be configured ... 
