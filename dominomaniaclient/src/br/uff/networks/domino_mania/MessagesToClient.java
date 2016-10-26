package br.uff.networks.domino_mania;

public class MessagesToClient {

    public static final String GAME_OVER_HEAD = "game over";
    public static final String YOUR_TURN = "you";
    public static final String INVALID_MOVEMENT = "invalid movement";
    public static final String VALID_MOVEMENT = "valid movement";
    public static final String ROUND_OVER = "round over";
    
    public static String turn(String name) {
        return name;
    }

    public static String gameOver(int winner) {
        return GAME_OVER_HEAD + "\n" + winner;
    }
}
