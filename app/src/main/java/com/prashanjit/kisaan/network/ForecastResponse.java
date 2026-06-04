package com.prashanjit.kisaan.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {

    @SerializedName("list")
    private List<ForecastItem> list;

    public List<ForecastItem> getList() { return list; }

    public static class ForecastItem {

        @SerializedName("dt")
        private long dt;

        @SerializedName("main")
        private Main main;

        @SerializedName("weather")
        private List<Weather> weather;

        @SerializedName("sys")
        private Sys sys;

        public long getDt() { return dt; }
        public Main getMain() { return main; }
        public List<Weather> getWeather() { return weather; }
        public Sys getSys() { return sys; }

        public static class Main {
            @SerializedName("temp")
            private double temp;

            @SerializedName("humidity")
            private int humidity;

            public double getTemp() { return temp; }
            public int getHumidity() { return humidity; }
        }

        public static class Weather {
            @SerializedName("main")
            private String main;

            @SerializedName("icon")
            private String icon;

            public String getMain() { return main; }
            public String getIcon() { return icon; }
        }

        public static class Sys {
            @SerializedName("pod")
            private String pod;

            public String getPod() { return pod; }
        }
    }
}