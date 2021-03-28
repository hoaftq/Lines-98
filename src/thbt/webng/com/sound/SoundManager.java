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

	private SoundManager() {
	}

	public static void playMoveSound() {
		// Release old clip before playing new one
		closeIfOpen(moveClip);
		moveClip = soundManager.play(MOVE);
	}

	public static void playCantMoveSound() {
		// This method can be constantly called when user tries to move a ball to an
		// impossible square. If we constantly close and play the clip again there will
		// be a delay
		if (cantMoveClip != null && cantMoveClip.isRunning()) {
			return;
		}

		closeIfOpen(cantMoveClip);
		cantMoveClip = soundManager.play(CANTMOVE);
	}

	public static void playJumSound() {
		closeIfOpen(jumpClip);
		jumpClip = soundManager.play(JUMP);
	}

	public static void playDestroySound() {
		closeIfOpen(destroyClip);
		destroyClip = soundManager.play(DESTROY);
	}

	@Override
	protected void finalize() throws Throwable {
		closeIfOpen(moveClip);
		closeIfOpen(cantMoveClip);
		closeIfOpen(jumpClip);
		closeIfOpen(destroyClip);
	}

	private Clip play(String fileName) {
		Clip clip = null;

		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResource(SOUND_PATH + fileName));
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

	private static void closeIfOpen(Clip clip) {
		if (clip != null && clip.isOpen()) {
			clip.close();
		}
	}

	private final static String SOUND_PATH = "/thbt/webng/com/resources/";
	private static final String DESTROY = "DESTROY2.WAV";
	private static final String JUMP = "JUMP.WAV";
	private static final String CANTMOVE = "CANTMOVE.WAV";
	private static final String MOVE = "MOVE.WAV";

	private static Clip moveClip;
	private static Clip cantMoveClip;
	private static Clip jumpClip;
	private static Clip destroyClip;

	private static SoundManager soundManager = new SoundManager();
}