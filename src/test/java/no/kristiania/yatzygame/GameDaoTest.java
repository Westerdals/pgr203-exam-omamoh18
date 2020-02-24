package no.kristiania.yatzygame;

import no.kristiania.yatzygame.game.Game;
import no.kristiania.yatzygame.game.GameDao;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GameDaoTest {

    private GameDao gameDao;

    static JdbcDataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:yatzyGameTest;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();
        gameDao = new GameDao(dataSource);
    }


    @Test
    void shouldDeleteGame() throws SQLException {
        GameDao gameDao = new GameDao(GameDaoTest.createDataSource());
        Game game = new Game();

        game.setPlayerName("Moa");
        game.setCategory("TWO");
        game.setDiceSequence("1,2,5,4,2");
        game.setScore("4");

        assertThat(gameDao.listAll()).doesNotContain(game);

        gameDao.insert(game);

        assertThat(gameDao.listAll())
                .contains(game);

        gameDao.delete(game.getId());

        assertThat(gameDao.listAll())
                .doesNotContain(game);
    }

    @Test
    void shouldListInsertGame() throws SQLException {
        Game game = new Game();
        game.setPlayerName("Johannes");
        game.setCategory("ONES");
        game.setDiceSequence("1,2,1,2,1");
        game.setScore("3");

        gameDao.insert(game);

        assertThat(gameDao.listAll())
                .extracting(Game::getPlayerName)
                .contains(game.getPlayerName());

        assertThat(gameDao.listAll())
                .extracting(Game::getCategory)
                .contains(game.getCategory());

        assertThat(gameDao.listAll())
                .extracting(Game::getDiceSequence)
                .contains(game.getDiceSequence());

        assertThat(gameDao.listAll())
                .extracting(Game::getScore)
                .contains(game.getScore());

    }


    @Test
    public void shouldRetrieveAllGameField() throws SQLException {
        Game game = new Game();
        game.setPlayerName("Mr Konte");
        game.setCategory("SIXES");
        game.setDiceSequence("1,1,1,1,1");
        game.setScore("5");
        gameDao.insert(game);
        assertThat(gameDao.retrieve(game.getId()))
                .isEqualToComparingFieldByField(game);
    }

    @Test
    public void shouldBeEqual() throws SQLException {

        Game gameOne = new Game();
        Game gameTwo = new Game();

        gameOne.setPlayerName("Ole");
        gameOne.setCategory("FOURS");
        gameOne.setDiceSequence("1,2,3,4,4");
        gameOne.setScore("8");

        gameTwo.setPlayerName("Ole");
        gameTwo.setCategory("FOURS");
        gameTwo.setDiceSequence("1,2,3,4,4");
        gameTwo.setScore("8");

        gameDao.insert(gameOne);
        gameDao.insert(gameTwo);

        assertThat(gameOne).isEqualTo(gameTwo);
        assertThat(gameOne.hashCode()).isEqualTo(gameTwo.hashCode());
    }

    @Test
    public void testToString() throws SQLException {
        Game game = new Game();

        game.setId(1l);
        game.setDescription("test");
        game.setDate("");
        game.setPlayerName("Joker");
        game.setCategory("FIVES");
        game.setDiceSequence("2,2,2,2,2");
        game.setScore("10");
        gameDao.insert(game);
        assertThat(game.toString()).isEqualTo("{Game" + "id=" + game.getId() + ", description='" + game.getDescription() + '\'' + ", date=" + game.getDate() + ", score='" + game.getScore() + '\'' + ", playerName='" + game.getPlayerName() + '\'' + ", category='" + game.getCategory() + '\'' + ", diceSequence='" + game.getDiceSequence() + '\'' + '}');
    }

    @Test
    void shouldUpdatePlayer() throws SQLException {
        GameDao gameDao = new GameDao(GameDaoTest.createDataSource());
        Game game = new Game();

        game.setPlayerName("Omar");
        game.setCategory("TWOS");
        game.setDiceSequence("1,2,3,2,1");
        game.setScore("4");

        gameDao.insert(game);

        assertThat(gameDao.listAll())
                .extracting(Game::getPlayerName)
                .contains("Omar");

        gameDao.updatePlayerName(game.getId(), "Johannes");

        assertThat(gameDao.listAll())
                .extracting(Game::getPlayerName)
                .contains("Johannes");

    }

    @Test
    public void calculateScoreForPlayers() throws SQLException{
        Game game = new Game();
        List<Integer> lst = new ArrayList<>();

        lst.add(1);
        lst.add(2);
        lst.add(2);
        lst.add(4);
        lst.add(3);

        assertThat(game.calculateScore("ONES", lst)).isEqualTo(Integer.toString(1));
        assertThat(game.calculateScore("TWOS", lst)).isEqualTo(Integer.toString(4));
        assertThat(game.calculateScore("THREES", lst)).isEqualTo(Integer.toString(3));
        assertThat(game.calculateScore("FOURS", lst)).isEqualTo(Integer.toString(4));
        assertThat(game.calculateScore("FIVES", lst)).isEqualTo(Integer.toString(0));
        assertThat(game.calculateScore("SIXES", lst)).isEqualTo(Integer.toString(0));

    }
}
