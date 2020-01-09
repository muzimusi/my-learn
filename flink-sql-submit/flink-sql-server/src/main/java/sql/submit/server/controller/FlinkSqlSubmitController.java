/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sql.submit.server.controller;


import sql.submit.server.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * flink sql submit controller
 */
@Api(tags = "FLINK_SQL_SUBMIT", position = 1)
@RestController
@RequestMapping("/flink/sql")
public class FlinkSqlSubmitController {

    private static final Logger logger = LoggerFactory.getLogger(FlinkSqlSubmitController.class);

    protected final List<String> logBuffer = Collections.synchronizedList(new ArrayList<>());

    @Value("${work.sql.dir}")
    private String WORK_SQL_DIR;

    @Value("${work.exec.dir}")
    private String WORK_EXEC_DIR;

    @Value("${work.lib.dir}")
    private String WORK_LIB_DIR;

    private String PATH_SEPARATOR="/";

    @Value("${sql.file.name}")
    private String SQL_FILE_NAME;

    @Value("${command.file.name}")
    private String COMMAND_FILE_NAME;

    @Value("${flink.bin.dir}")
    private String FLINK_BIN_DIR;

    @Value("${flink.command}")
    private String FLINK_COMMAND;

    /**
     * process
     */
    private Process process;

    /**
     * submit sql
     *
     * @param multipartFile flink sql
     * @return task list page
     */
    @ApiOperation(value = "file", notes = "flink sql")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "file", value = "RESOURCE_FILE", required = true, dataType = "MultipartFile")
//    })
    @PostMapping("/submit")
    @ResponseStatus(HttpStatus.OK)
//    public Result submitSql(@RequestParam(value = "sql", required = false, defaultValue = "flink sql") String sql) {
    public Result submitSql(@RequestParam(value = "file") MultipartFile multipartFile) {

        try {
            if (multipartFile.isEmpty()) {
                new Result(200, "上传失败，请选择文件");
            }
            logger.info("submitSql begin, Received sql-file : {}", multipartFile.getName());
            // write data to file
            String dir = "/usr/bin/hadoop/software/test/flinksql/sql";
            logger.info("dir:{},fileName:{}", WORK_SQL_DIR, SQL_FILE_NAME);
            String commandPath = saveFileAndCrateCommandFile(WORK_SQL_DIR, SQL_FILE_NAME, multipartFile);


            //init process builder
            ProcessBuilder processBuilder = new ProcessBuilder();
            // setting up a working directory
            processBuilder.directory(new File(WORK_EXEC_DIR));
            // merge error information to standard output stream
            processBuilder.redirectErrorStream(true);
            // setting up user to run commands
            processBuilder.command("sh", commandPath);

            process = processBuilder.start();

            BufferedReader inReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = inReader.readLine()) != null) {
                logger.info("result: {}", line);
            }

            return new Result(200, "ok");
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
            return new Result();
        }

    }

    private String saveFileAndCrateCommandFile(String dir, String fileName, MultipartFile multipartFile) throws IOException {
        String path = dir + "/" + fileName;
        File dest = new File(path);
        logger.info("dest file path : {}", dest.getPath());
        if (dest.exists()) {
            dest.delete();
        }
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dest);
        //#  /usr/bin/hadoop/software/test/flinksql/exec/submit.command;
        String commandPath = WORK_EXEC_DIR + PATH_SEPARATOR + COMMAND_FILE_NAME;
        logger.info("command file path : {}", commandPath);
        //# /usr/bin/hadoop/software/flink-1.9.1.1/bin/flink run -m yarn-cluster -d -p 3 /usr/bin/hadoop/software/test/flinksql/lib/flink-sql-submit.jar -w /usr/bin/hadoop/software/test/flinksql/sql/ -f data.sql;
        String exeCommand = FLINK_BIN_DIR + PATH_SEPARATOR + FLINK_COMMAND + " run -m yarn-cluster -d -p 3 " + WORK_LIB_DIR + PATH_SEPARATOR + "flink-sql-submit.jar -w " + WORK_SQL_DIR + PATH_SEPARATOR + " -f " + SQL_FILE_NAME;
        logger.info("command file content : {}", exeCommand);
        FileUtils.writeStringToFile(new File(commandPath), exeCommand,
                Charset.forName("UTF-8"));
        return commandPath;
    }

    private String createCommandAndFile(String dir, String fileName, String sql) throws IOException {
        String file = dir + "/" + fileName;
        logger.info("sql file: {}", file);
        FileUtils.writeStringToFile(new File(file), sql,
                Charset.forName("UTF-8"));
        String commandPath = "/usr/bin/hadoop/software/test/flinksql/exec/submit.command";
        String exeCommand = "/usr/bin/hadoop/software/flink-1.9.1.1/bin/flink run -d -p 3 /usr/bin/hadoop/software/test/flinksql/lib/flink-sql-submit.jar -w /usr/bin/hadoop/software/test/flinksql/sql/ -f data.sql";
        logger.info("command file : {}", exeCommand);
        FileUtils.writeStringToFile(new File(commandPath), exeCommand,
                Charset.forName("UTF-8"));
        return commandPath;
    }

}