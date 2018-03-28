package example.tb.com.module_compiler.two;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @auther tb
 * @time 2018/3/27 下午2:44
 * @desc 对应需要生成某个类的全部相关信息
 */
public class ProxyInfo2 {
    /**
     * 类
     */
    public TypeElement typeElement;
    /**
     * 类注解的值(布局id)
     */
    public int value;
    public String packageName;
    /**
     * key为id，也就是成员变量注解的值，value为对应的成员变量
     */
    public Map<Integer, VariableElement> mInjectElements = new HashMap<>();
    
    public Map<Integer, ExecutableElement> mInjectMethods = new HashMap<>();
    
    /**
     * 采用类名方式不能被混淆(否则编译阶段跟运行阶段，该字符串会不一样)，或者采用字符串方式
     */
    public static final String PROXY = "TA";
    public static final String ClassSuffix = "_" + PROXY;
    
    public String getProxyClassFullName() {
        return typeElement.getQualifiedName().toString() + ClassSuffix;
    }
    
    public String getClassName() {
        return typeElement.getSimpleName().toString() + ClassSuffix;
    }
    
    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("//自动生成的注解类，勿动!!!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import example.tb.com.module_api.*;\n");
        builder.append("import android.support.annotation.Keep;\n");
        builder.append('\n');
        
        builder.append("@Keep").append("\n");//禁止混淆，否则反射的时候找不到该类
        builder.append("public class ").append(getClassName());
        builder.append(" {\n");
        
        generateMethod(builder);
        
        builder.append('\n');
        
        builder.append("}\n");
        return builder.toString();
    }
    
    private void generateMethod(StringBuilder builder) {
        builder.append("public " + getClassName() + "(" + typeElement.getQualifiedName() + " host, android.view.View object) {\n");
        
        if (value > 0) {
            builder.append("host.setContentView(" + value + ");\n");
        }
        for (int id : mInjectElements.keySet()) {
            VariableElement variableElement = mInjectElements.get(id);
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();
            
            //这里object如果不为空，则可以传入view等对象
            builder.append("host." + name).append(" = ");
            builder.append("(" + type + ")object.findViewById(" + id + ");");
        }
        
        builder.append("/**hahaha"+mInjectMethods.keySet().size()+"*/");
        for (int id : mInjectMethods.keySet()) {
            builder.append("//wuwuwu");
            ExecutableElement executableElement = mInjectMethods.get(id);
            VariableElement variableElement = mInjectElements.get(id);
            String name = variableElement.getSimpleName().toString();
            builder.append("host." + name + ".setOnClickListener(new android.view.View.OnClickListener(){\n");
            builder.append("@Override\n");
            builder.append("public void onClick(android.view.View v) {\n");
            builder.append(executableElement.getSimpleName().toString() + "(android.view.View v)");
            builder.append("}\n");
            builder.append("});\n");
        }
        
        builder.append("  \n}\n");
    }
    
}
