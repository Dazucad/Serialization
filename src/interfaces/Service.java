package interfaces;

import annotations.Cache;

import java.util.Date;
import java.util.List;

import static annotations.Cache.*;

public interface Service {
    @Cache(cacheType = FILE, fileNamePrefix = "serialized", zip = true, identityBy = {String.class, double.class})
    List<String> run(String item, double value, Date date);

    @Cache(cacheType = IN_MEMORY, listList = 100_000)
    List<String> work(String item);

    @Cache(cacheType = FILE, fileNamePrefix = "serialized", zip = true, identityBy = {String.class, int.class})
    double doHardWork(String s, int i);
}
