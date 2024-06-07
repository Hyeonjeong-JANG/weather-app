package shop.mtcoding.weather._core.data;

public class WeatherVO {
    public String uri = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
    public String serviceKey = "wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D";
    public String baseDate;
    public String baseTime;
    public String nx;
    public String ny;

    public WeatherVO(String baseDate, String baseTime, String nx, String ny) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.nx = nx;
        this.ny = ny;
    }
}
