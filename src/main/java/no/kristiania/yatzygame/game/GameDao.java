package no.kristiania.yatzygame.game;

import no.kristiania.yatzygame.AbstractDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameDao extends AbstractDao<Game> {
    public GameDao(DataSource dataSource) {
        super(dataSource);
    }

    public void insert(Game game) throws SQLException {
        long id = insert(game, "insert into game (player, category, dice, score, session) values(?,?,?,?,?)");
        game.setId(id);
    }

    public void update(Long id, String description) throws SQLException {
        update(description, id, "update game set description = ? where id = ?");

    }

    public void updatePlayerName(Long id, String playerName) throws SQLException {
        update(playerName, id, "update game set player = ? where id = ?");

    }

    public Game retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from game where id = ?");
    }

    public List<Game> listAll() throws SQLException {
        return listAll("select * from game");
    }

    @Override
    protected void mapToStatement(Game game, PreparedStatement statement) throws SQLException {
        statement.setString(1, game.getPlayerName());
        statement.setString(2, game.getCategory());
        statement.setString(3, game.getDiceSequence());
        statement.setString(4, game.getScore());
        statement.setString(5, game.getSession());

    }


    @Override
    protected Game mapFromResultSet(ResultSet resultSet) throws SQLException {
        Game game = new Game();
        game.setId(resultSet.getLong("id"));
        game.setPlayerName(resultSet.getString("player"));
        game.setCategory(resultSet.getString("category"));
        game.setDiceSequence(resultSet.getString("dice"));
        game.setScore(resultSet.getString("score"));
        game.setSession(resultSet.getString("session"));
        return game;
    }


    public Game retrieve(Long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapFromResultSet(resultSet);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public void delete(long id) throws SQLException {
        delete(id, "delete from game where id = ?");
    }
}
