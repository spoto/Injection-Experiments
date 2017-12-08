package iste.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Ließt/Speichert Objekte aus/in Dateien.
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class ISTEStream {
	/**
	 * Speichert Objekte in einer Datei.
	 * @param	objectList	Liste von beliebigen Objekten
	 * @param	savePath	Pfad, unter der die Datei gespeichert werden soll
	 * @throws	IOException
	 */
	public static void saveObjects(List<Object> objectList, String savePath) throws IOException {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(savePath))) {
			for (Object object: objectList) {
				outputStream.writeObject(object);
			}
		}
	}
	
	/**
	 * Liest Objekte aus einer Datei
	 * @param	filePath	Datei, die die Objekte enthält
	 * @return				Liste, die die Objekte enthält
	 * @throws	IOException
	 */
	public static ArrayList<Object> readObjects(String filePath) throws IOException {
		ArrayList<Object> objectList = new ArrayList<Object>();
		
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
			while (true) {
				objectList.add(inputStream.readObject());
			}
		}
		catch (EOFException e) {
			return objectList;
		}
		catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/**
	 * Speichert ein einzelnes Objekt.
	 * @param	object		Beliebiges Objekt
	 * @param	savePath	Pfad, unter der die Datei gespeichert werden soll
	 * @throws	IOException
	 */
	public static void saveObject(Object object, String savePath) throws IOException {
		ArrayList<Object> tmpList = new ArrayList<>(1);
		
		tmpList.add(object);
		saveObjects(tmpList, savePath);
	}
	
	/**
	 * Ließt ein Objekt aus einer Datei
	 * @param	filePath	Datei, die das Objekte enthält
	 * @return				Das eingelesene Objekt
	 * @throws	IOException
	 */
	public static Object readObject(String filePath) throws IOException {
		ArrayList<Object> tmpList = readObjects(filePath);
		
		return tmpList.get(0);
	}
}
