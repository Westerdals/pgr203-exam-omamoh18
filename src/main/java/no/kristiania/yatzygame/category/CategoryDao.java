package no.kristiania.yatzygame.category;

import no.kristiania.yatzygame.AbstractDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class CategoryDao extends AbstractDao<Category> {


    public CategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void mapToStatement(Category o, PreparedStatement statement) throws SQLException {
        statement.setString(1, o.getName());
    }

    public void insert(Category category) throws SQLException {
        long id = insert(category, "insert into categories (name) values (?)");
        category.setId(id);
    }


    public List<Category> listAll() throws SQLException {
        return listAll("select * from categories");
    }

    public Category retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from categories where id = ?");
    }

    @Override
    protected Category mapFromResultSet(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getLong("id"));
        category.setName(resultSet.getString("name"));
        return category;
    }

    public Category retrieve(Long id, String sql) throws SQLException {
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
