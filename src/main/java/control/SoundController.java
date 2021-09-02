package control;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundController {
    public static void playButtonSound() {
        AudioClip mediaPlayer = new AudioClip(SoundController.class.getResource("/sounds/button.wav").toExternalForm());
        mediaPlayer.play();
    }

    public static void playRefuseSound() {
        Media media = new Media(SoundController.class.getResource("/sounds/refuse.wav").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static void playRegisterSound() {
        AudioClip mediaPlayer = new AudioClip(SoundController.class.getResource("/sounds/register.wav").toExternalForm());
        mediaPlayer.play();
    }

    public static void playCoinSound() {
        AudioClip audioClip = new AudioClip(SoundController.class.getResource("/sounds/coin.wav").toExternalForm());
        audioClip.play();
    }

    public static void playStartGameSound() {
        AudioClip audioClip = new AudioClip(SoundController.class.getResource("/sounds/start.mp3").toExternalForm());
        audioClip.setCycleCount(AudioClip.INDEFINITE);
        audioClip.setVolume(0.3);
        audioClip.play();
    }

    public static void playBombSound() {
        AudioClip audioClip = new AudioClip(SoundController.class.getResource("/sounds/bomb.wav").toExternalForm());
        audioClip.setCycleCount(4);
        audioClip.play();
    }

    public static void playEatGhostSound() {
        AudioClip audioClip = new AudioClip(SoundController.class.getResource("/sounds/eatGhost.wav").toExternalForm());
        audioClip.setCycleCount(3);
        audioClip.play();
    }

    public static void playGameOver() {
        Media media = new Media(SoundController.class.getResource("/sounds/gameOver.wav").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static void playWinGameController() {
        Media media = new Media(SoundController.class.getResource("/sounds/gameWon.wav").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
}
