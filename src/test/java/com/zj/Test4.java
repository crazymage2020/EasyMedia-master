package com.zj;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.bytedeco.javacv.Frame;

/**
 * @author 小布
 * @version 1.0.0  错误的用不了
 * @className Test4.java
 * @createTime 2023年08月19日 15:06:00
 */
public class Test4 {

    public static void main(String[] args) throws Exception {
        //开启日志
        FFmpegLogCallback.set();

        String inputUrl="rtsp://admin:admin666@192.168.1.2:554/stream1";

        //抽取视频和音频存储路径
        String outputVideoUrl="/Users/xiaobu/Downloads/test4.flv";
        //1.拉取视频
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputUrl);
        grabber.setOption("threads", "1");
        // 设置缓存大小，提高画质、减少卡顿花屏
        grabber.setOption("buffer_size", "1024000");
        // 读写超时，适用于所有协议的通用读写超时
        grabber.setOption("rw_timeout", "15000000");
        // 探测视频流信息，为空默认5000000微秒
        grabber.setOption("probesize","15000000");
        // 解析视频流信息，为空默认5000000微秒
        grabber.setOption("analyzeduration", "15000000");
        // rtmp拉流缓冲区，默认3000毫秒
        grabber.setOption("rtsp_buffer", "1000");

        grabber.start();

        //2.录制视频/音频
        FFmpegFrameRecorder recorder=new FFmpegFrameRecorder(outputVideoUrl,grabber.getImageWidth(),grabber.getImageHeight(),grabber.getAudioChannels());
        recorder.setFrameRate(25);// 设置帧率
        recorder.setGopSize(25);// 设置gop,与帧率相同，相当于间隔1秒chan's一个关键帧
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);  //压缩方式264
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); //视频源数据yuv
        recorder.setVideoOption("threads", "8"); //解码线程数
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC); //设置音频压缩方式
        recorder.setVideoBitrate(grabber.getVideoBitrate());
        recorder.setAudioBitrate(192000);
        recorder.setAudioQuality(0);
        recorder.setSampleRate(44100);
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setAudioOptions(grabber.getAudioOptions());
        recorder.setFormat("flv");
        recorder.start();

        //读取每一种数据，一帧数据可能是音频或视频
        Frame avFrame = grabber.grabFrame();
        while(true){
            if (avFrame == null) {
                System.out.println("!!! Failed cvQueryFrame");
                break;
            }
            recorder.record(avFrame); //录制
        }

        //关闭相关对象
        grabber.close();
        recorder.close();
    }
}
