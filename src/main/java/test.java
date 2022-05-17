
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alumno6k
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream("mapa.dat");
        ObjectInputStream ois = new ObjectInputStream(fi);
//        
//        
//        Map mapaLeido = (TreeMap) ois.readObject();
//        System.out.println("mapa " + mapa);

        Map<Integer, String> mapa = new TreeMap();
        mapa.put(202112211, "cosa1");
        mapa.put(202112212, "cosa2");
        mapa.put(202112222, "cosa3");
        mapa.put(2022111, "cosa4");

        FileOutputStream fo = new FileOutputStream("mapa.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fo);

        oos.writeObject(mapa);

        Map mapaLeido = (TreeMap) ois.readObject();
        System.out.println("mapa " + mapa);
    }

}
