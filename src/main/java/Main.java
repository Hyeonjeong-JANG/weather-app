import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysql.cj.util.DnsSrv;
import shop.mtcoding.weather.WeatherResponseDTO;
import shop.mtcoding.weather._core.util.ApiExplorer;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 목표 : 자바를 이용해서 (변수)의 현재 날씨를 확인하세요.
 * 준비 : 위경도 엑셀데이터를 MySQL에 구축하기
 * 1. 구를 입력하세요.
 * [예, 종로구, 수영구]
 * 2. 동을 입력하세요
 * [예, 구에 대한 동]
 * => 부전동
 * process : 부전동에 대한 위경도를 DB 조회하기
 * 3. URL 요청 (위경도 받기)
 * => url 요청 [서비스키, 시간, 위경도]
 * 4. 파싱
 * => 파싱 (t1h) - Class DTO 만들고, Gson으로 파싱하기
 * 콘솔 : 21.5 출력
 */
public class Main {
    public static void main(String[] args) {
        // 0. 준비 : 위경도 엑셀데이터를 MySQL에 구축하기 -> 워크벤치로 함

        // process : 부전동에 대한 위경도를 DB 조회하기
        // MySQL 연결 정보를 설정해야 한다.
        String jdbcUrl = "jdbc:mysql://localhost:3306/weather_db";
        String username = "root";
        String password = "1234";
        String nx = null;
        String ny = null;
        Scanner sc = new Scanner(System.in);
        String level3 = null;
        // 내가 뭘 하고 싶냐면 결과 내에서 찾고 다시 결과 내에서 찾는 것을 하고 싶거든?
        // MySQL에 연결해야 한다. try-resources로 하면 close하지 않아도 되어서 편함 -> 그런데 이렇게 하면 또 한 번 하고 다 닫아버려서 하나의 try-resources문에 과정을 다 넣어야 함.
        Set<String> hashLevel1 = new HashSet<>();
        Set<String> hashLevel2 = new HashSet<>();
        Set<String> hashLevel3 = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // 1. 시 리스트 보여주고 시 입력받기
            String queryLevel1 = "SELECT level1 FROM weather";

            try (PreparedStatement preparedStatement = connection.prepareStatement(queryLevel1)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        hashLevel1.add(resultSet.getString("level1"));
                    }
                    System.out.println("다음 예시를 보고 날씨를 확인하고 싶은 시를 입력하세요");
                    System.out.println(hashLevel1);
                    System.out.print("입력: ");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // 시를 입력하면 여기에 담김
            String level1 = sc.nextLine();

            // 2. 군/구 리스트 보여주고 군/구 입력받기
            String queryLevel2 = "SELECT level2 FROM weather where level1 = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(queryLevel2)) {
                preparedStatement.setString(1, level1);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        hashLevel2.add(resultSet.getString("level2"));
                    }
                    System.out.println("다음 예시를 보고 날씨를 확인하고 싶은 구/군을 입력하세요");
                    System.out.println(hashLevel2);
                    System.out.print("입력: ");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // 군/구를 입력하면 여기에 담김
            String level2 = sc.nextLine();

            // 3. 동 리스트 보여주고 동 입력받기
            String queryLevel3 = "SELECT level3 FROM weather where level2 = ?";


            try (PreparedStatement preparedStatement = connection.prepareStatement(queryLevel3)) {
                preparedStatement.setString(1, level2);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        hashLevel3.add(resultSet.getString("level3"));
                    }
                    System.out.println("다음 예시를 보고 날씨를 확인하고 싶은 동을 입력하세요");
                    System.out.println(hashLevel3);
                    System.out.print("입력: ");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // 동을 입력하면 여기에 담김
            level3 = sc.nextLine();

            // 4. URL 요청 (동 이름을 토대로 위경도 받기)
            // 4-1. 동 이름으로 위 경도 받기
            String queryNxNy = "SELECT nx, ny FROM weather where level3 = ?";
//            String nx;
//            String ny;
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryNxNy)) {
                preparedStatement.setString(1, level3);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        nx = resultSet.getString("nx");
                        ny = resultSet.getString("ny");
//                        System.out.println("nx: " + nx + ", ny: " + ny);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            e.getMessage();
        }
        // URL요청
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String uri = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
        String serviceKey = "cdLwxcHMoWudSBeScW577Us8fZoHm08TRN%2BEyK%2F7IUy0xrSFVwPwM6AZ%2FpkBqenL0rDpnkjdbFTs9epraZXifw%3D%3D";
        String baseDate = LocalDate.now().format(formatter);
        String baseTime = "0600";

        try {
            String response = ApiExplorer.get(uri, serviceKey, baseDate, baseTime, nx, ny);
//            System.out.println("Response: " + response);

            // 5. 파싱 (t1h) - Class DTO 만들고, Gson으로 파싱하기
            // T1H 데이터 추출 및 출력

            String responseBody = ApiExplorer.get(
                    uri,
                    serviceKey,
                    baseDate,
                    baseTime,
                    nx,
                    ny
            );
            Gson gson = new GsonBuilder().create();
            WeatherResponseDTO weatherResponseDTO = gson.fromJson(responseBody, WeatherResponseDTO.class);
            // 6. 콘솔에 현재 기온 출력하기

            System.out.println(level3 + "의 현재 온도는 " + weatherResponseDTO.response.body.items.item.get(3).obsrValue);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

