package jfugue;

import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;

public class Jfugue {

    public static void main(String[] args) throws InterruptedException {
        Player player = new Player();
        Rhythm rhythm = new Rhythm()
        .addLayer("O..oO...O..oOOooo..")
        .addLayer("..S...S...S...S.")
        .addLayer("````````````````")
        .addLayer("...............+");

        player.play(rhythm.getPattern().repeat(2));
    }

}
