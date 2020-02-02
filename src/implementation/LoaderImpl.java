package implementation;

import interfaces.Loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class LoaderImpl implements Loader {

    @Override
    public Object load(String fileName) {
        List objects = new ArrayList();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            while (objectInputStream.available() != 0) objects.add(objectInputStream.readObject());
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не известен");
        } catch (IOException e) {
            System.out.println("Непредвиденная ошибка чтения файла:\n" + e);
        }
        return objects.size() == 0 ? null : objects;
    }
}
