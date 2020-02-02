import cacheProxy.CacheProxy;
import implementation.LoaderImpl;
import implementation.ServiceImpl;
import interfaces.Loader;
import interfaces.Service;

import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args){
        CacheProxy cacheProxy=new CacheProxy();
        Service service=cacheProxy.cache(new ServiceImpl());
        Loader loader=cacheProxy.cache(new LoaderImpl());

        List<String> list=service.run("work1",1.0,new Date());
        System.out.println(list);
    }
}
