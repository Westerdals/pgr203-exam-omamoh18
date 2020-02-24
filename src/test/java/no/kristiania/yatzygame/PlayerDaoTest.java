package no.kristiania.yatzygame;


import no.kristiania.yatzygame.player.Player;
import no.kristiania.yatzygame.player.PlayerDao;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerDaoTest {

    private static Random random = new Random();
    private PlayerDao dao;

    @BeforeEach
    void setUp(){
        JdbcDataSource dataSource = createDataSource();
        dao = new PlayerDao(dataSource);
    }

    static JdbcDataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:yatzyGameTest;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    @Test
    void shouldListInsertedPlayers() throws SQLException {
        Player player = new Player();
        player.setName("Barack Obama");
        player.setAge(22);
        dao.insert(player);
        assertThat(dao.listAll())
                .extracting(Player::getName)
                .contains(player.getName());

        assertThat(dao.listAll())
                .extracting(Player::getAge)
                .contains(player.getAge());

    }

    @Test
    public void shouldRetrieveAllPlayerFields() throws SQLException {
        Player player = new Player();
        player.setName("Bowser");
        player.setAge(25);
        dao.insert(player);
        assertThat(player).hasNoNullFieldsOrProperties();
        assertThat(dao.retrieve(player.getId()))
                .isEqualToComparingFieldByField(player);
    }

    @Test
    public void shouldNotBeEqual() throws SQLException {
        Player playerOne = new Player();
        Player playerTwo = new Player();

        playerOne.setAge(20);
        playerTwo.setAge(20);
        playerOne.setName("test");
        playerTwo.setName("test");
        dao.insert(playerOne);
        dao.insert(playerTwo);
        assertThat(playerOne).isNotEqualTo(playerTwo);
        assertThat(playerOne.getId()).isNotEqualTo(playerTwo.getId());
    }

    @Test
    public void testToString() throws SQLException {
        Player player = new Player();

        player.setName("test");
        player.setAge(22);
        dao.insert(player);
        assertThat(player.toString()).isEqualTo("Player{" +
                "name='" + player.getName() + '\'' +
                ", age=" + player.getAge() +
                ", id=" + player.getId() +
                '}');
    }

}
