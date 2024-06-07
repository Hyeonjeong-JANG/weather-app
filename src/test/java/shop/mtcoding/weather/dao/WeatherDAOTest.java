package shop.mtcoding.weather.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import shop.mtcoding.weather._core.db.DBConnection;

import java.util.List;
import java.util.Map;

public class WeatherDAOTest {

    @Test
    public void 동찾기_test(){
        // given
        String gu = "종로구";

        // when
        WeatherDAO dao = new WeatherDAO(DBConnection.getInstance());
        List<String> dongs = dao.동찾기(gu);

        dongs.forEach(System.out::println);
    }

    @Test
    public void 위경도찾기_test(){
        // given
        String gu = "종로구";
        String dong = "사직동";

        // when
        WeatherDAO dao = new WeatherDAO(DBConnection.getInstance());
        Map<String, String> los = dao.위경도찾기(dong, gu);

        System.out.println(los.get("nx")); // 60
        System.out.println(los.get("ny")); // 127

        Assertions.assertEquals("60", los.get("nx"));
        Assertions.assertEquals("127", los.get("ny"));
    }
}
