/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package soundManagement;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author F776
 */
public class SoundPlayer {

    public static void playSound(String filePath, float volume, boolean loop) {
        try (InputStream soundStream = SoundPlayer.class.getClassLoader().getResourceAsStream(filePath)) {
            if (soundStream != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(soundStream));
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            } else {
                System.err.println("Sound file not found: " + filePath);
            }
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
        }
    }

    public static void playBackgroundTheme() {
        playSound("soundManagement/sounds/pokemonCenter.wav", -10f, true);
    }

    public static void playSave() {
        playSound("soundManagement/sounds/GUIsavegame.wav", -5f, false);
    }

    public static void wrongBuzzer() {
        playSound("soundManagement/sounds/GUIselbuzzer.wav", -5f, false);
    }

    public static void menuOpened() {
        playSound("soundManagement/sounds/PCaccess.wav", -5f, false);
    }

    public static void menuClosed() {
        playSound("soundManagement/sounds/PCclose.wav", -5f, false);
    }

    public static void optionSelected() {
        playSound("soundManagement/sounds/GUIselcancel.wav", -5f, false);
    }

    public static void programOpened() {
        playSound("soundManagement/sounds/PCopen.wav", -5f, false);
    }
}
