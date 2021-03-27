package thbt.webng.com.game.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class StorageUtil {

	public static <T extends Object> boolean save(T o, String fileName) {
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileName))) {
			stream.writeObject(o);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> Optional<T> load(String fileName) {
		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(fileName))) {
			return Optional.of((T) stream.readObject());
		} catch (IOException | ClassNotFoundException e) {
			return Optional.empty();
		}
	}
}
