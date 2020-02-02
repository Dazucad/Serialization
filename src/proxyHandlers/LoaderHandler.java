package proxyHandlers;

import implementation.LoaderImpl;
import interfaces.Loader;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoaderHandler implements InvocationHandler {
    private Loader original;
    private String rootFolder;

    public LoaderHandler(LoaderImpl original) {
        this.original = original;
    }

    public LoaderHandler(LoaderImpl original, String rootFolder) {
        this.original = original;
        this.rootFolder = rootFolder;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if (rootFolder != null) args[0] = rootFolder + "\\" + args[0];
        return method.invoke(original, args);
    }
}
