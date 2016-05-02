package gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundEffect {

    private static ArrayList list;

    public static boolean isMute;
    public static SoundEffect winSound;
    public static SoundEffect loseSound;
    public static SoundEffect music;

    //audio of each object stored here
    private Clip sound;
    private String path;


    public SoundEffect(String path) {
        this(path, false);
    }

    public SoundEffect(String path, boolean isRepeating) {
        sound = getClip(path, isRepeating);
        this.path = path;
        registerSoundEffect(this);
    }

    private static Clip getClip(String path, boolean isRepeating) {
            Clip c = null;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    SoundEffect.class.getResourceAsStream(path));
            c = AudioSystem.getClip();
            c.open(ais);
            if (isRepeating) {
                c.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        catch (LineUnavailableException | IOException
                | UnsupportedAudioFileException ex)
        {
            Logger.getLogger(Hangman_GUI.class.getName()).log(Level.SEVERE,"", ex);
        }
        return c;
    }

    public void start() {
        if(!isMute){
        sound.stop();
        sound.setMicrosecondPosition(0);
        sound.start();
        }
    }

    public static void stop(SoundEffect effect) {
        if (list!=null) {
            if (list.contains(effect)) {
                effect.stop();
            }
        }
    }

    private void stop() {
        sound.stop();
    }

    public static void mute() {
        if (list!=null) {
            try {
                isMute = true;
                winSound.stop();
                loseSound.stop();
                music.stop();
            } catch (Exception ex) {System.out.println("I don't know why would this happen anyways.");}

        }
    }

    public static void unmute () {
        if (list!=null)
            try {
                isMute = false;
                music.start();
            } catch (Exception ex) {System.out.println("I don't know why would this happen anyways.");}
        }

    //must be called before using any other method
    public static void initialiseSoundList(){

        if (list == null) {
            list = new ArrayList();
            winSound = new SoundEffect("/music/applause.wav");
            loseSound = new SoundEffect("/music/scream_females.wav");
            music = new SoundEffect("/music/nyan_cat.wav", true);
        }
    }

    private static void registerSoundEffect(SoundEffect e){
        if (list!=null) {
            list.add(e);
        }
        else {
            initialiseSoundList();
            list.add(e);
        }

    }
}