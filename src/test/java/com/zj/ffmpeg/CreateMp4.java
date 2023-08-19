package com.zj.ffmpeg;

import org.bytedeco.javacpp.Loader;

import java.io.IOException;

/**
 * @author 小布
 * @version 1.0.0
 * @className CreateMp4.java
 * @createTime 2023年08月19日 15:25:00
 */
public class CreateMp4 {
    public static void main(String[] args) {
        String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
        ProcessBuilder pb = new ProcessBuilder(ffmpeg, "-i", "/Users/xiaobu/Desktop/1.mp4", "-vcodec", "h264", "/Users/xiaobu/Downloads/1.mp4");
        try {
            pb.inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

}
