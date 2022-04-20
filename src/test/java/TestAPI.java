import api.API;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.sparkjava.test.SparkServer;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spark.servlet.SparkApplication;

public class TestAPI {

    public static class TestAPISparkApplication implements SparkApplication {
        @Override
        public void init() {
            new API();
        }
    }

    @ClassRule
    public static SparkServer<TestAPISparkApplication> testServer = new SparkServer<>(TestAPISparkApplication.class, 4567);

    @Test
    public void test() throws Exception {
        GetMethod get = testServer.get("/", false);
        HttpResponse httpResponse = testServer.execute(get);
        Assertions.assertEquals(200, httpResponse.code());
    }


}
