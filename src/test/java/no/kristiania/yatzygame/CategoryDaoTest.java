package no.kristiania.yatzygame;

import no.kristiania.yatzygame.category.Category;
import no.kristiania.yatzygame.category.CategoryDao;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryDaoTest {

    private static Random random = new Random();
    private CategoryDao dao;

    @BeforeEach
    void setUp(){
        JdbcDataSource dataSource = createDataSource();
        dao = new CategoryDao(dataSource);
    }

    static JdbcDataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:yatzyGameTest;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    @Test
    void shouldListInsertedCategory() throws SQLException {
        Category category = sampleCategory();
        dao.insert(category);
        assertThat(dao.listAll())
                .extracting(Category::getName)
                .contains(category.getName());
    }

    @Test
    public void shouldRetrieveAllCategory() throws SQLException {
        Category category = sampleCategory();
        dao.insert(category);
        assertThat(category).hasNoNullFieldsOrProperties();
        assertThat(dao.retrieve(category.getId()))
                .isEqualToComparingFieldByField(category);
    }

    static Category sampleCategory(){
        Category category = new Category();
        category.setName(pickOne(new String[] {"get categories", "ONES", "TWOS", "THREES","FOURS","FIVES","SIXES"}));
        return category;
    }

    private static String pickOne(String[] alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }

    @Test
    public void testToString() throws SQLException {
        Category category = new Category();

        category.setName("get categories");
        dao.insert(category);
        assertThat(category.toString()).isEqualTo("Category{" +
                "id=" + category.getId() +
                ", name='" + category.getName() + '\'' +
                '}');
    }
}
