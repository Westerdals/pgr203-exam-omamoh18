package no.kristiania.yatzygame;

import no.kristiania.yatzygame.game.Game;
import no.kristiania.yatzygame.game.GameController;
import no.kristiania.yatzygame.game.GameDao;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTest {


    private GameDao dao;

    static JdbcDataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:GameDaoTest;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();
        dao = new GameDao(dataSource);
    }


    @Test
    void shouldReturnProjectFromDb() throws SQLException {
        Game gameOne = new Game();
        Game gameTwo = new Game();

        gameOne.setPlayerName("Jojo");
        gameTwo.setPlayerName("Bizarre");

        gameOne.setCategory("ONES");
        gameTwo.setCategory("ONES");

        gameOne.setDiceSequence("1,1,1,1,1");
        gameTwo.setDiceSequence("1,1,1,1,1");

        gameOne.setScore("5");
        gameTwo.setScore("5");

        gameOne.setSession("test");
        gameTwo.setSession("test");


        dao.insert(gameOne);
        dao.insert(gameTwo);

        GameController controller = new GameController(dao);
        assertThat(controller.getBody())
                .contains(String.format("<tr id=%s><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", gameOne.getId(), gameOne.getPlayerName(), gameOne.getDiceSequence(), gameOne.getCategory(), gameOne.getScore(), gameOne.getSession()))
                .contains(String.format("<tr id=%s><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", gameTwo.getId(), gameTwo.getPlayerName(), gameTwo.getDiceSequence(), gameTwo.getCategory(), gameTwo.getScore(), gameTwo.getSession()));

        assertThat(gameOne.getPlayerName()).isEqualTo("Jojo");
        assertThat(gameTwo.getPlayerName()).isEqualTo("Bizarre");
    }

}
