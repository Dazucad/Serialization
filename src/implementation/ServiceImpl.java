package implementation;

import interfaces.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceImpl implements Service {

    @Override
    public List<String> run(String item, double value, Date date) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) list.add(item + " " + value + " " + date);
        return list;
    }

    @Override
    public List<String> work(String item) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 101_000; i++) list.add(item);
        return list;
    }

    @Override
    public double doHardWork(String s, int i) {
        return (double) i * i / 2;
    }
}
