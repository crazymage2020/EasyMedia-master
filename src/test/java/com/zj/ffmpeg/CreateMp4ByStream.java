package com.zj.ffmpeg;

import org.bytedeco.javacpp.Loader;

import java.io.IOException;

/**
 * @author 小布
 * @version 1.0.0
 * @className CreateMp4.java
 * @createTime 2023年08月19日 15:25:00
 */
public class CreateMp4ByStream {
    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();

        String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
        ProcessBuilder pb = new ProcessBuilder(ffmpeg, "-i", "rtsp://admin:admin666@192.168.1.5:554/stream1", "-vcodec", "h264", "-t","30","/Users/xiaobu/Downloads/1.mp4");
        try {
            pb.inheritIO().start().waitFor();
            long nowTime = System.currentTimeMillis();
            //60S自动断开
            if (nowTime - beginTime >= 60*1000) {
                throw new RuntimeException("60秒时间到");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

}
