package com.zj.util;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.io.File;

/**
 * @author 小布
 * @version 1.0.0
 * @className JavaCvUtil.java
 * @createTime 2023年08月19日 11:10:00
 */
public class JavaCvUtil {
    public static String convertFile2File(File file,String destPath) {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file.getAbsolutePath());
        String fileName = null;
        String fileFullPathName = null;

        Frame captured_frame = null;

        FFmpegFrameRecorder recorder = null;

        try {
            frameGrabber.start();
            //获取转码后的视频名称
            fileName = file.getName().replace(file.getName().substring(file.getName().lastIndexOf(".")),".mp4");
            //更换转码后视频存储位置
            String name =destPath+ "/"+file.getName();
            fileFullPathName = name.replace(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")), ".mp4");
            //如果想把转码后的视频还是保存到原文件目录下
            //fileFullPathName = file.getAbsolutePath().replace(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")), ".mp4");
            recorder = new FFmpegFrameRecorder(fileFullPathName, frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), frameGrabber.getAudioChannels());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(frameGrabber.getFrameRate());
            recorder.setVideoBitrate(frameGrabber.getVideoBitrate());
            recorder.setAudioBitrate(192000);
            recorder.setAudioOptions(frameGrabber.getAudioOptions());
            recorder.setAudioQuality(0);
            recorder.setSampleRate(44100);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.start();
            while (true) {
                try {
                    captured_frame = frameGrabber.grabFrame();

                    if (captured_frame == null) {
                        System.out.println("!!! Failed cvQueryFrame");
                        break;
                    }
                    recorder.record(captured_frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            recorder.stop();
            recorder.release();
            frameGrabber.stop();
            frameGrabber.release();
            recorder.close();
            frameGrabber.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回转码后视频文件名称
        return fileName;
        //返回转码后视频全路径
        //return fileFullPathName;
    }

    public static String convertStream2File(String streamPath,String fileFullPathName) {
        long beginTime = System.currentTimeMillis();
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(streamPath);
        Frame captured_frame = null;
        FFmpegFrameRecorder recorder = null;
        try {
            frameGrabber.start();
            frameGrabber.getLengthInTime();
            //如果想把转码后的视频还是保存到原文件目录下
            recorder = new FFmpegFrameRecorder(fileFullPathName, frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), frameGrabber.getAudioChannels());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setOption("mp4", "10");
            recorder.setFrameRate(frameGrabber.getFrameRate());
            recorder.setVideoBitrate(frameGrabber.getVideoBitrate());
            recorder.setAudioBitrate(192000);
            recorder.setAudioOptions(frameGrabber.getAudioOptions());
            recorder.setAudioQuality(0);
            recorder.setSampleRate(44100);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.start();
            while (true) {
                try {
                    captured_frame = frameGrabber.grabFrame();

                    if (captured_frame == null) {
                        System.out.println("!!! Failed cvQueryFrame");
                        break;
                    }
                    recorder.record(captured_frame);
                    long nowTime = System.currentTimeMillis();
                    //60S自动断开
                    if (nowTime - beginTime >= 60*1000) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            recorder.stop();
            recorder.release();
            frameGrabber.stop();
            frameGrabber.release();
            recorder.close();
            frameGrabber.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回转码后视频文件名称
        return fileFullPathName;
        //返回转码后视频全路径
        //return fileFullPathName;
    }
}
