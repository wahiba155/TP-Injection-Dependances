package ma.enset.ioc;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class MiniApplicationContext {

    private Map<Class<?>, Object> beans = new HashMap<>();

    public MiniApplicationContext() {
    }

    public void addBean(Class<?> clazz, Object obj) {
        beans.put(clazz, obj);
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beans.get(clazz));
    }

    // ===== Constructor Injection =====
    public <T> T createBean(Class<T> clazz) throws Exception {

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {

            if (constructor.isAnnotationPresent(Inject.class)) {

                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] dependencies = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    dependencies[i] = beans.get(paramTypes[i]);
                }

                return (T) constructor.newInstance(dependencies);
            }
        }

        return clazz.getDeclaredConstructor().newInstance();
    }

    // ===== Field + Setter Injection =====
    public void injectDependencies(Object obj) throws Exception {

        // Field Injection
        for (Field field : obj.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(Inject.class)) {

                Object dependency = beans.get(field.getType());

                if (dependency != null) {
                    field.setAccessible(true);
                    field.set(obj, dependency);
                }
            }
        }

        // Setter Injection
        for (Method method : obj.getClass().getDeclaredMethods()) {

            if (method.isAnnotationPresent(Inject.class)) {

                Class<?> paramType = method.getParameterTypes()[0];
                Object dependency = beans.get(paramType);

                if (dependency != null) {
                    method.invoke(obj, dependency);
                }
            }
        }
    }
}