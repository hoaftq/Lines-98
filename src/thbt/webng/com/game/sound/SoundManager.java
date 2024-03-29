package thbt.webng.com.game.sound;

import javax.sound.sampled.*;
import java.io.IOException;

public final class SoundManager {

    private static final String SOUND_PATH = "/thbt/webng/com/resources/";
    private static final String DESTROY = "DESTROY2.WAV";
    private static final String JUMP = "JUMP.WAV";
    private static final String CANTMOVE = "CANTMOVE.WAV";
    private static final String MOVE = "MOVE.WAV";

    private static final SoundManager soundManager = new SoundManager();

    private static Clip moveClip;
    private static Clip cantMoveClip;
    private static Clip jumpClip;
    private static Clip destroyClip;

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

    private static void closeIfOpen(Clip clip) {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        closeIfOpen(moveClip);
        closeIfOpen(cantMoveClip);
        closeIfOpen(jumpClip);
        closeIfOpen(destroyClip);
    }

    private Clip play(String fileName) {
        try (var stream = AudioSystem.getAudioInputStream(getClass().getResource(SOUND_PATH + fileName))) {
            var format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
            return clip;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}