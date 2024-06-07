package shop.mtcoding.weather._core.data;

import java.util.List;

public class WeatherDTO {
    public ResponseDTO response;

    public static class ResponseDTO {
        public Body body;

        public static class Body {
            public String dataType;
            public Items items;
            public int pageNo;
            public int numOfRows;
            public int totalCount;

            public static class Items {
                public List<Item> item;

                public static class Item {
                    public String baseDate;
                    public String baseTime;
                    public String category;
                    public int nx;
                    public int ny;
                    public String obsrValue;

                }
            }
        }
    }
}
