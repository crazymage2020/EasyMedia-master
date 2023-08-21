package com.zj;

import com.zj.util.JavaCvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * @author 小布
 * @version 1.0.0
 * @className Test1.java
 * @createTime 2023年08月19日 11:22:00
 */
@Slf4j
public class JavaCvUtilTest {
        public static void main(String[] args) throws java.lang.Exception {
            testConvertByCommand();
//            testConvertByApi();
        }

    private static void testConvertByCommand() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("开始Command视频转换");
        //原文件全路径
        String streamPath = "rtsp://admin:admin666@192.168.1.5:554/stream1";
        String fileFullPathName = "/Users/xiaobu/Downloads/tp-link.mp4";
        String convert = JavaCvUtil.convertByCommand(streamPath,fileFullPathName,"30");
        log.info("【main】::convert ==> 【{}】", convert);
        stopWatch.stop();
        log.info("【main】::stopWatch.getTotalTimeSeconds() ==> 【{}】", stopWatch.getTotalTimeSeconds());
    }
    private static void testConvertByApi() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("开始API视频转换");
        //原文件全路径
        String streamPath = "rtsp://admin:admin666@192.168.1.5:554/stream1";
        String fileFullPathName = "/Users/xiaobu/Downloads/tp-link.mp4";
        String convert = JavaCvUtil.convertStream2FileByApi(streamPath,fileFullPathName,30);
        log.info("【main】::convert ==> 【{}】", convert);
        stopWatch.stop();
        log.info("【main】::stopWatch.getTotalTimeSeconds() ==> 【{}】", stopWatch.getTotalTimeSeconds());
    }


}
