package no.kristiania.yatzygame.game;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Game {

    private Long id;
    private String description;
    private String date;
    private String playerName;
    private String category;
    private String score;

    public String getDiceSequence() {
        return diceSequence;
    }

    public void setDiceSequence(String diceSequence) {
        this.diceSequence = diceSequence;
    }

    private String diceSequence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return
                Objects.equals(description, game.description) &&
                Objects.equals(date, game.date) &&
                Objects.equals(score, game.score) &&
                Objects.equals(playerName, game.playerName) &&
                Objects.equals(category, game.category) &&
                Objects.equals(diceSequence, game.diceSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash( description, date, score, playerName, category, diceSequence);
    }

    @Override
    public String toString() {
        return "{Game" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", score='" + score + '\'' +
                ", playerName='" + playerName + '\'' +
                ", category='" + category + '\'' +
                ", diceSequence='" + diceSequence + '\'' +
                '}';
    }

    //Logic for calculating score
    public String calculateScore(String fetchCategory, List<Integer> lst) {

        int categoryStringToInt = 0;
        switch (fetchCategory){
           case "ONES":
               categoryStringToInt = 1;
               break;
            case "TWOS":
                categoryStringToInt = 2;
                break;
            case "THREES":
                categoryStringToInt = 3;
                break;
            case "FOURS":
                categoryStringToInt = 4;
                break;
            case "FIVES":
                categoryStringToInt = 5;
                break;
            case "SIXES":
                categoryStringToInt = 6;
                break;
            default: throw new IllegalArgumentException("Category type not found");
       }

        int score = 0;
        score = Collections.frequency(lst, categoryStringToInt) * categoryStringToInt;
        return Integer.toString(score);
    }
}

