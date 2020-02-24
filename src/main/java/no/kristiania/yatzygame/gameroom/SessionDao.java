package no.kristiania.yatzygame.gameroom;

import no.kristiania.yatzygame.AbstractDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SessionDao extends AbstractDao<Session> {
    public SessionDao(DataSource dataSource) {
        super(dataSource);
    }

    public void insert(Session session) throws SQLException {
        long id = insert(session, "insert into gamesession (name,description) values(?,?)");
        session.setId(id);
    }

    public void update(Long id, String name) throws SQLException {
        update(name, id, "update gamesession set name = ? where id = ?");

    }

    public Session retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from gamesession where id = ?");
    }

    public List<Session> listAll() throws SQLException {
        return listAll("select * from gamesession");
    }

    @Override
    protected void mapToStatement(Session session, PreparedStatement statement) throws SQLException {
        statement.setString(1, session.getName());
        statement.setString(2, session.getDescription());
    }


    @Override
    protected Session mapFromResultSet(ResultSet resultSet) throws SQLException {
        Session session = new Session();
        session.setId(resultSet.getLong("id"));
        session.setName(resultSet.getString("name"));
        session.setDescription(resultSet.getString("description"));
        return session;
    }



    public Session retrieve(Long id, String sql) throws SQLException {
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
        delete(id, "delete from gamesession where id = ?");
    }
}
