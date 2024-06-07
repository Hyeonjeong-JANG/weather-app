package shop.mtcoding.weather;

import com.google.gson.Gson;
import shop.mtcoding.weather._core.data.WeatherDTO;
import shop.mtcoding.weather._core.data.WeatherVO;
import shop.mtcoding.weather._core.db.DBConnection;
import shop.mtcoding.weather._core.util.MyHttp;
import shop.mtcoding.weather.dao.WeatherDAO;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. init 객체 초기화
        Scanner sc = new Scanner(System.in);
        WeatherDAO dao = new WeatherDAO(DBConnection.getInstance());

        // 2. 프로세스 시작
        System.out.println("1. 구를 입력하세요");
        System.out.println("[종로구, 수영구, 부산진구]");

        String gu = sc.nextLine();
        List<String> dongs = dao.동찾기(gu);

        System.out.println("2. 동을 입력하세요");
        dongs.forEach(s -> System.out.print(s+" "));
        System.out.println();

        String dong = sc.nextLine();
        Map<String, String> los = dao.위경도찾기(dong, gu);

        WeatherVO vo = new WeatherVO("20240607", "1600", los.get("nx"), los.get("ny"));
        try {
            String responseBody = MyHttp.get(
                    vo.uri,
                    vo.serviceKey,
                    vo.baseDate,
                    vo.baseTime,
                    vo.nx,
                    vo.ny
            );

            Gson gson = new Gson();
            WeatherDTO dto = gson.fromJson(responseBody, WeatherDTO.class);

            System.out.println("현재 온도 : "+dto.response.body.items.item.get(3).obsrValue);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
