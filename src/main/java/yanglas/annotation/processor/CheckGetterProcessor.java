package yanglas.annotation.processor;

import yanglas.annotation.CheckGetter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import java.util.Set;


//处理器要处理的annotation们
@SupportedAnnotationTypes("yanglas.annotation.CheckGetter")
//支持的jdk编译版本
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CheckGetterProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //第一个是所有的annotation类型集合
        //第二个是当前编译轮的语法树环境
        //遍历获取有这个注解的java结构元素类
        for(TypeElement annotationClass : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(CheckGetter.class))){
            //遍历获取这个元素类下的属性元素
            for(VariableElement field:ElementFilter.fieldsIn(annotationClass.getEnclosedElements())){
                //判断这个元素是否有get方法
                if (!containsGetter(annotationClass, field.getSimpleName().toString())) {
                    processingEnv.getMessager().printMessage(Kind.ERROR,
                            String.format("getter not found for '%s.%s'.", annotationClass.getSimpleName(), field.getSimpleName()));
                }
            }

        }
        return false;
    }
    private static boolean containsGetter(TypeElement typeElement, String name) {
        String getter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        for (ExecutableElement executableElement : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
            if (!executableElement.getModifiers().contains(Modifier.STATIC)
                    && executableElement.getSimpleName().toString().equals(getter)
                    && executableElement.getParameters().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
