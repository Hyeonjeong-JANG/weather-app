package shop.mtcoding.weather;

import org.apache.http.Header;

import java.util.List;

public class WeatherResponseDTO {
    public Response response;

    public static class Response {
        public Body body;

        public static class Body {

            public String dataType;
            public Items items;
            public Integer pageNo;
            public Integer numOfRows;
            public Integer totalCount;

            public static class Items {
                public List<Item> item;

                public static class Item {
                    public String baseDate;
                    public String baseTime;
                    public String category;
                    public Integer nx;
                    public Integer ny;
                    public String obsrValue;
                }
            }
        }
    }
}
