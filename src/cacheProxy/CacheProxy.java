package cacheProxy;

import implementation.LoaderImpl;
import implementation.ServiceImpl;
import interfaces.Loader;
import interfaces.Service;
import proxyHandlers.LoaderHandler;
import proxyHandlers.ServiceHandler;

import java.lang.reflect.Proxy;


public class CacheProxy {

    private String rootFolder;

    public CacheProxy() {
    }

    public CacheProxy(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public Service cache(ServiceImpl serviceImpl) {
        if (rootFolder != null)
            return (Service) Proxy.newProxyInstance(serviceImpl.getClass().getClassLoader(),
                    new Class[]{Service.class}, new ServiceHandler(serviceImpl, rootFolder));
        else return (Service) Proxy.newProxyInstance(serviceImpl.getClass().getClassLoader(),
                new Class[]{Service.class}, new ServiceHandler(serviceImpl));
    }

    public Loader cache(LoaderImpl loaderImpl) {
        if (rootFolder != null)
            return (Loader) Proxy.newProxyInstance(loaderImpl.getClass().getClassLoader(),
                    new Class[]{Loader.class}, new LoaderHandler(loaderImpl, rootFolder));
        else return (Loader) Proxy.newProxyInstance(loaderImpl.getClass().getClassLoader(),
                new Class[]{Loader.class}, new LoaderHandler(loaderImpl));
    }
}
