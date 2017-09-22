package me.qinyu;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class StringMapTestHelper<V> {

    private Map<String, V> map;

    private StringMapTestHelper(Map<String, V> map) {
        this.map = map;
    }

    public <I> I assertThat(Class<I> builderClass) {
        return (I) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{builderClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Assertions.assertThat(map).containsEntry(method.getName(), (V) args[0]);
                return proxy;
            }
        });
    }


    public <I> I assertThatSoftly(Class<I> builderClass, final SoftAssertions soft) {
        return (I) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{builderClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                soft.assertThat(map).containsEntry(method.getName(), (V) args[0]);
                return proxy;
            }
        });
    }


    public <I> I buildFor(Class<I> assertClass) {
        return (I) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{assertClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                map.put(method.getName(), (V) args[0]);
                return proxy;
            }
        });
    }

    public static <V, I> StringMapTestHelper<V> helper(Map<String, V> map) {
        return new StringMapTestHelper<V>(map);
    }

    public static <V, I> StringMapTestHelper<V> helper() {
        return new StringMapTestHelper<V>(new HashMap<String, V>());
    }

}
