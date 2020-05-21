package com.ayang818.middleware.tracefilter.controller;

import com.ayang818.middleware.tracefilter.service.DataPuller;
import com.ayang818.middleware.tracefilter.utils.CastUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 杨丰畅
 * @description TODO
 * @date 2020/5/5 9:51
 **/
@RestController
public class ReadyController {

    @Autowired
    private DataPuller dataPuller;

    private static final Logger logger = LoggerFactory.getLogger(ReadyController.class);

    private static final ExecutorService SINGLE_TRHEAD = Executors.newSingleThreadExecutor();

    @RequestMapping(value = "ready")
    public String ready(HttpServletResponse response) {
        logger.info("数据过滤容器收到ready");
        return "suc";
    }

    @RequestMapping(value = "setParameter")
    public String setParamter(@RequestParam String port, HttpServletResponse response) {
        if (port != null) {
            logger.info("数据过滤容器获取到端口参数 {}", port);
            SINGLE_TRHEAD.execute(() -> dataPuller.pulldata(port));
        } else {
            logger.warn("未接收到数据源端口");
        }
        response.setHeader("status", "200");
        return "suc";
    }
}
