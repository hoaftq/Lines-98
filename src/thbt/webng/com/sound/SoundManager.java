package thbt.webng.com.sound;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
	private Clip play(String fileName) {
		Clip clip = null;

		try {
			AudioInputStream stream = AudioSystem
					.getAudioInputStream(getClass().getResource(
							"/thbt/webng/com/resources/" + fileName));
			AudioFormat format = stream.getFormat();

			DataLine.Info info = new DataLine.Info(Clip.class, format);
			try {
				clip = (Clip) AudioSystem.getLine(info);
				clip.open(stream);
				clip.start();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return clip;
	}

	public static void playMoveSound() {
		if (moveClip != null) {
			moveClip.close();
		}

		moveClip = soundManager.play("MOVE.WAV");
	}

	public static void playCantMoveSound() {
		if (cantMoveClip != null) {
			if (cantMoveClip.isRunning()) {
				return;
			}
			cantMoveClip.close();
		}
		cantMoveClip = soundManager.play("CANTMOVE.WAV");
	}

	public static void playJumSound() {
		if (jumpClip != null) {
			jumpClip.close();
		}
		jumpClip = soundManager.play("JUMP.WAV");
	}

	public static void playDestroySound() {
		if (clipDestroy != null) {
			clipDestroy.close();
		}
		clipDestroy = soundManager.play("DESTROY2.WAV");
	}

	private static Clip moveClip;
	private static Clip cantMoveClip;
	private static Clip jumpClip;
	private static Clip clipDestroy;

	private static SoundManager soundManager = new SoundManager();
}
