package yanglas.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface CheckGetter {
    //target 指定哪些元素可以被该注解注解
    //retention 生命周期（源代码，编译文件，运行时）

    //这是自定义的注解，也就是需要自定义处理器 Processor
}
