package proxyHandlers;

import annotations.Cache;
import implementation.ServiceImpl;
import interfaces.Service;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceHandler implements InvocationHandler {

    private Service original;
    private String rootFolder;
    private Map<MethodArgs, List<String>> cachedResults = new HashMap<>();

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
            if (method.getName().equals("run") || method.getName().equals("work")) {
                if (cache.cacheType() == Cache.FILE) {
                    Map<MethodArgs, List<String>> cachedData = new HashMap<>();
                    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(cache.fileNamePrefix()))) {
                        cachedData = (Map<MethodArgs, List<String>>) objectInputStream.readObject();
                        for (Map.Entry<MethodArgs, List<String>> a : cachedData.entrySet())
                            cachedResults.put(a.getKey(), a.getValue());
                    } catch (FileNotFoundException e) {
                        System.out.println("Файл не найден, проводится обычный расчет");
                    } catch (EOFException e) {
                        System.out.println("В файле нет данных, проводится стандартный расчет");
                    }
                    if (cachedResults.containsKey(new MethodArgs(cache.identityBy(), args))) {
                        System.out.println("Взято из кеша");
                        return cachedResults.get(new MethodArgs(cache.identityBy(), args));
                    }
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(cache.fileNamePrefix()));

                    List<String> list = (List<String>) method.invoke(original, args);
                    cachedResults.put(new MethodArgs(cache.identityBy(), args), list);
                    objectOutputStream.writeObject(cachedResults);
                    objectOutputStream.close();
                    return list;
                }
                if (cache.cacheType() == Cache.IN_MEMORY) {
                    if (cachedResults.containsKey(new MethodArgs(cache.identityBy(), args))) {
                        System.out.println("Взято из кеша");
                        return cachedResults.get(new MethodArgs(cache.identityBy(), args));
                    }
                    List<String> list = (List<String>) method.invoke(original, args);
                    cachedResults.put(new MethodArgs(cache.identityBy(), args), list);
                }
            }
        }
        return method.invoke(original, args);
    }
}

class MethodArgs implements Serializable {
    private String item;
    private Double value;
    private Date date;

    public MethodArgs(Class[] identityClass, Object[] args) {
        for (Class a : identityClass) {
            if (a == String.class) item = (String) args[0];
            if (a == double.class) value = (double) args[1];
            if (a == Date.class) date = (Date) args[2];
        }
    }

    @Override
    public boolean equals(Object object) {
        MethodArgs other;
        if (object instanceof MethodArgs) {
            other = (MethodArgs) object;
            boolean[] b = new boolean[3];
            if ((this.item != null) && (other.item != null)) b[0] = this.item.equals(other.item);
            else if ((this.item == null) && (other.item == null)) b[0] = true;
            if ((this.value != null) && (other.value != null)) b[1] = this.value.equals(other.value);
            else if ((this.value == null) && (other.value == null)) b[1] = true;
            if ((this.date != null) && (other.date != null)) b[2] = this.date.equals(other.date);
            else if ((this.date == null) && (other.date == null)) b[2] = true;
            return b[0] && b[1] && b[2];
        }
        return false;
    }

    @Override
    public int hashCode() {
        int i = 0;
        if (item != null) i += item.hashCode();
        if (value != null) i += value.hashCode();
        if (date != null) i += date.hashCode();
        return i;
    }
}