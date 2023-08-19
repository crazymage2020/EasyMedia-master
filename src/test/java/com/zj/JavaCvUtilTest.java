package com.zj;

import com.zj.util.JavaCvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;

/**
 * @author 小布
 * @version 1.0.0
 * @className Test1.java
 * @createTime 2023年08月19日 11:22:00
 */
@Slf4j
public class JavaCvUtilTest {
        public static void main(String[] args) throws java.lang.Exception {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("开始视频转换");

//            testFile2File();
            //原文件全路径
            String streamPath = "rtsp://admin:admin666@192.168.1.2:554/stream1";
            String fileFullPathName = "/Users/xiaobu/Downloads/tp-link.mp4";
            String convert = JavaCvUtil.convertStream2File(streamPath,fileFullPathName);
            log.info("【main】::convert ==> 【{}】", convert);
            stopWatch.stop();
            log.info("【main】::stopWatch.getTotalTimeSeconds() ==> 【{}】", stopWatch.getTotalTimeSeconds());
        }

    private static void testFile2File() {
        //原文件全路径
        String filePath = "/Users/xiaobu/Downloads/1.ts";
        String destPath = "/Users/xiaobu/Downloads";
        String convert = JavaCvUtil.convertFile2File(new File(filePath),destPath);
        System.out.println(convert);
    }
}
