package com.zj.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.util.StopWatch;

import java.io.IOException;

/**
 * @author 小布
 * @version 1.0.0
 * @className JavaCvUtil.java
 * @createTime 2023年08月19日 11:10:00
 */
@Slf4j
public class JavaCvUtil {
    /**
     * convertFileByApi
     *借助JavaCV和ffmpeg的api
     * @param sourcePath       sourcePath 可以是流地址或者文件地址
     * @param fileFullPathName fileFullPathName
     * @param duration         duration 录制时长 只针对视频流录制
     * @return java.lang.String
     * @author xiaobu
     * @date 2023/8/21 9:40
     */
    public static String convertStream2FileByApi(String sourcePath, String fileFullPathName, int duration) {
        long beginTime = System.currentTimeMillis();
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(sourcePath);
        Frame capturedFrame = null;
        FFmpegFrameRecorder recorder = null;
        try {
            frameGrabber.start();
            frameGrabber.getLengthInTime();
            //获取video得类型 如MP4等
            String videoType = fileFullPathName.substring(fileFullPathName.lastIndexOf(".") + 1);
            recorder = new FFmpegFrameRecorder(fileFullPathName, frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), frameGrabber.getAudioChannels());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat(videoType);
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
                    capturedFrame = frameGrabber.grabFrame();
                    if (capturedFrame == null) {
                        System.out.println("!!! Failed cvQueryFrame");
                        break;
                    }
                    recorder.record(capturedFrame);
                    long nowTime = System.currentTimeMillis();
                    long costTime = nowTime - beginTime;
                    //duration S自动断开
                    if (costTime >= duration * 1000L) {
                        log.info("【convertFileByApi】::costTime ==> 【{}】", costTime);
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


    /**
     * convertFile2FileByApi
     *
     * @author 小布
     * @date 2023/8/21 13:33
     * @param sourcePath sourcePath
     * @param fileFullPathName fileFullPathName
     * @param duration duration 录制时长（z）
     * @return java.lang.String
     */
    public static String convertFile2FileByApi(String sourcePath, String fileFullPathName, int duration) {
        long beginTime = System.currentTimeMillis();
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(sourcePath);
        Frame capturedFrame = null;
        FFmpegFrameRecorder recorder = null;
        try {
            frameGrabber.start();
            frameGrabber.setTimestamp(20 * 1000000);
            // 视频的时长 微秒
            long lengthInTime = frameGrabber.getLengthInTime();
            String format = String.format("视频长度:%s(S)",  lengthInTime / 1000 / 1000);
            System.out.println(format);
            //获取video得类型 如MP4等
            String videoType = fileFullPathName.substring(fileFullPathName.lastIndexOf(".") + 1);
            recorder = new FFmpegFrameRecorder(fileFullPathName, frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), frameGrabber.getAudioChannels());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat(videoType);
            recorder.setFrameRate(frameGrabber.getFrameRate());
            recorder.setVideoBitrate(frameGrabber.getVideoBitrate());
            recorder.setAudioBitrate(192000);
            recorder.setAudioOptions(frameGrabber.getAudioOptions());
            // Highest quality
            recorder.setAudioQuality(0);
            recorder.setSampleRate(44100);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.start();
            int count = 0;
            while (true) {
                try {
                    capturedFrame = frameGrabber.grabFrame();
                    if (capturedFrame == null) {
                        log.error("【convertFile2FileByApi】::【!!! Failed cvQueryFrame】");
                        break;
                    }
                    count++;
                    if (count > 1000) {
                        break;
                    }
                    recorder.record(capturedFrame);
                    long nowTime = System.currentTimeMillis();
                    long costTime = nowTime - beginTime;
                    //duration S自动断开
                    if (costTime >= duration * 1000L) {
                        log.info("【convertFileByApi】::costTime ==> 【{}】", costTime);
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

    /**
     * 基于JavaCV跨平台调用ffmpeg命令
     * duration 录制时长为多少秒的视频
     */
    public static String convertByCommand(String sourcePath, String destPath, String duration) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("开始执行基于JavaCV跨平台调用ffmpeg命令录制视频");
        try {
            String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
            ProcessBuilder pb = new ProcessBuilder(ffmpeg, "-i", sourcePath, "-vcodec", "h264", destPath);
            if (StrUtil.isNotBlank(duration)) {
                pb = new ProcessBuilder(ffmpeg, "-i", sourcePath, "-vcodec", "h264", "-t", duration, destPath);
            }
            pb.inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        log.info("【convertByFfmpegCommand】::stopWatch.getTotalTimeSeconds() ==> 【{}】", stopWatch.getTotalTimeSeconds());
        return destPath;

    }

}
