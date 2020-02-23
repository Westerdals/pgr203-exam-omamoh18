import no.kristiania.http.HttpServer;
import no.kristiania.yatzygame.category.CategoryController;
import no.kristiania.yatzygame.category.CategoryDao;
import no.kristiania.yatzygame.player.PlayerController;
import no.kristiania.yatzygame.player.PlayerDao;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class YatzyServer {
    private HttpServer httpServer;
    public YatzyServer(int port) throws IOException{

        Properties properties = new Properties();
        try(FileReader fileReader = new FileReader("pgr203.properties")){
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        httpServer = new HttpServer(port);
        httpServer.setFileLocation("src/main/resources/yatzy-game");
        httpServer.addController("/api/players", new PlayerController(new PlayerDao(dataSource)));
        httpServer.addController("/api/category", new CategoryController(new CategoryDao(dataSource)));

    }

    public static void main(String[] args) throws IOException {
        new YatzyServer(8080).start();
    }

    private void start(){
        httpServer.start();
    }

}
