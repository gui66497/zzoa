package zzjz.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Bean;
import zzjz.bean.ResultCode;
import zzjz.service.BackUpService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author 彭鹏
 * @version 2017年12月5日15:39
 * @ClassName: BackUpRest
 * @Description: 备份还原rest
 */
@Component
@Path("backUp")
public class BackUpRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(BackUpRest.class);

    @Autowired
    BackUpService backUpService;

    /**
     * 数据库备份.
     *
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("backUp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> backUp() {
        LOGGER.info("开始调用/backUp/backUp");
        BaseResponse<String> response = new BaseResponse<>();
        Properties props = new Properties();
        try {
            props.load(BackUpRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }
        String mysqlPath = props.getProperty("mysqlPath");
        String mysqlBackPath = props.getProperty("mysqlBackPath");
        String jdbcUsername = props.getProperty("jdbc.username");
        String jdbcPassword = props.getProperty("jdbc.password");

        boolean rs = backUpService.mysqlBackUp(mysqlPath, mysqlBackPath, jdbcUsername, jdbcPassword);

        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("备份完成");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("备份异常");
        }
        LOGGER.info("调用完成/backUp/backUp");
        return response;
    }

    /**
     * 数据库还原.
     *
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("getFileList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Bean> getFileList() {
        LOGGER.info("开始调用/backUp/getFileList");
        BaseResponse<Bean> response = new BaseResponse<>();
        Properties props = new Properties();
        try {
            props.load(BackUpRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }
        String mysqlBackPath = props.getProperty("mysqlBackPath");

        List<Bean> list = backUpService.getFileList(mysqlBackPath);

        response.setData(list);
        LOGGER.info("调用完成/backUp/getFileList");
        return response;
    }

    /**
     * 数据库还原.
     *
     * @param fileName 文件名
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("{fileName}/reduce")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> reduce(@PathParam("fileName") String fileName) {
        LOGGER.info("开始调用/backUp/reduce");
        BaseResponse<String> response = new BaseResponse<>();
        Properties props = new Properties();
        try {
            props.load(BackUpRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }
        String mysqlPath = props.getProperty("mysqlPath");
        String mysqlBackPath = props.getProperty("mysqlBackPath");
        String jdbcUsername = props.getProperty("jdbc.username");
        String jdbcPassword = props.getProperty("jdbc.password");

        boolean rs = backUpService.mysqlReduce(fileName, mysqlPath, mysqlBackPath, jdbcUsername, jdbcPassword);

        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("还原完成");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("还原异常");
        }
        LOGGER.info("调用完成/backUp/reduce");
        return response;
    }


}
