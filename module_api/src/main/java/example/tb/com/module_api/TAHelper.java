package example.tb.com.module_api;

import android.app.Activity;
import android.widget.Toast;

import example.tb.com.module_compiler.one.ProxyInfo;

/**
 * @auther tb
 * @time 2018/3/27 下午2:51
 * @desc 实现帮助注入的类
 */
public class TAHelper {
    public static void inject(Activity o) {
        inject(o, o);
    }
    
    public static void inject(Activity host, Object root) {
        String classFullName = host.getClass().getName() + ProxyInfo.ClassSuffix;
        Toast.makeText(host.getApplicationContext(),classFullName,Toast.LENGTH_LONG).show();
        try {
            Class proxy = Class.forName(classFullName);
            TA injector = (TA) proxy.newInstance();
            injector.inject(host, root);
        } catch (Exception e) {
            Toast.makeText(host.getApplicationContext(),"error",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
