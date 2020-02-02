package proxyHandlers;

import annotations.Cache;
import implementation.ServiceImpl;
import interfaces.Service;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceHandler implements InvocationHandler {

    private Service original;
    private String rootFolder;

    public ServiceHandler(ServiceImpl original) {
        this.original = original;
    }

    public ServiceHandler(ServiceImpl original, String rootFolder) {
        this.original = original;
        this.rootFolder = rootFolder;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cache.class)) {
            Cache cache = method.getDeclaredAnnotation(Cache.class);
            if (method.getName().equals("run") || method.getName().equals("work"))
                if (cache.cacheType() == Cache.FILE) {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(cache.fileNamePrefix()));
                    Map<Object, List<String>> cachedData = new HashMap<>();
                    List<String> list = (List<String>) method.invoke(original, args);
                    cachedData.put(args, list);
                    objectOutputStream.writeObject(list);
                    return list;
                }
        }
        return method.invoke(original, args);
    }
}
