package no.kristiania.yatzygame.player;

import no.kristiania.yatzygame.AbstractDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PlayerDao extends AbstractDao<Player> {
    public PlayerDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void mapToStatement(Player o, PreparedStatement statement) throws SQLException {
       statement.setString(1, o.getName());
       statement.setInt(2, o.getAge());
    }

    public void insert(Player player) throws SQLException {
        long id = insert(player, "insert into players (name, age) values (?,?)");
        player.setId(id);
    }


    public List<Player> listAll() throws SQLException {
        return listAll("select * from players");
    }

    public Player retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from players where id = ?");
    }

    @Override
    protected Player mapFromResultSet(ResultSet resultSet) throws SQLException {
        Player player = new Player();
        player.setId(resultSet.getLong("id"));
        player.setName(resultSet.getString("name"));
        player.setAge(resultSet.getInt("age"));
        return player;
    }

    public Player retrieve(Long id, String sql) throws SQLException {
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
}
