package shop.mtcoding.weather.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherDAO {

    private final Connection connection;

    public WeatherDAO(Connection connection) {
        this.connection = connection;
    }

    public List<String> 동찾기(String gu){

        List<String> dongs = new ArrayList<>();

        try {
            PreparedStatement pstmt = connection.prepareStatement("select dong from weather_tb where gu = ?");
            pstmt.setString(1, gu);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                dongs.add(rs.getString("dong"));
            }
            return dongs;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<String, String> 위경도찾기(String dong, String gu){

        Map<String, String> los = new HashMap<>();

        try {
            PreparedStatement pstmt = connection.prepareStatement("select nx, ny from weather_tb where dong = ? and gu = ?");
            pstmt.setString(1, dong);
            pstmt.setString(2, gu);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                los.put("nx", rs.getString("nx"));
                los.put("ny", rs.getString("ny"));
            }
            return los;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
