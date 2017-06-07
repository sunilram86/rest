package galvanize;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class Helpers {

    public static boolean classImplementsInterface(Class clazz, Class expectedInterface) {
        Type[] interfaces = clazz.getGenericInterfaces();
        for (Type actualInterface : interfaces) {
            if (actualInterface == expectedInterface) {
                return true;
            }
        }
        return false;
    }

    public static boolean classInjectsDependency(Class<?> clazz, Class<?> dependency) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            Class<?>[] parameterTypes  = constructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                if (parameterType.equals(dependency)) return true;
            }
        }
        return false;
    }

    public static Constructor getConstructorForInterface(Class<?> clazz, Class<?> dependency) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            Class<?>[] parameterTypes  = constructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                if (parameterType.equals(dependency)) return constructor;
            }
        }
        return null;
    }

}
