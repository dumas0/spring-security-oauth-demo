package com.dumas.sso.mybatis.plus.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Collections;

/**
 * @author dumas
 * @date 2023/4/20
 */
public class MyBatisPlusGeneratorApplication {
    private static final String ROOT_PATH = "/sso-mybatis-plus-generator/dev/src";

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/sso-oauth?useUnicode=false&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&allowPublicKeyRetrieval=true&failOverReadOnly=false&serverTimezone=GMT%2B8", "root", "root")
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("dzma")  // 设置作者
                            .commentDate("yyyy-MM-dd hh:mm:ss")     // 注释日期
                            .outputDir(System.getProperty("user.dir") + ROOT_PATH + "/src/main/java")   // 制定输出目录
                            .disableOpenDir();      // 禁止打开输出目录
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.dumas.sso")   // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper"));     // 设置 mapperXml 生成路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude("sys_menu")  // 设置需要生成的表名
                            .addTablePrefix("tb_")  // 设置过滤表前缀
                            // Entity 策略配置
                            .entityBuilder()
                            .enableLombok()
                            .enableFileOverride()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            // Mapper 配置
                            .mapperBuilder()
                            .enableFileOverride()
                            // Service 策略配置
                            .serviceBuilder()
                            .enableFileOverride()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .controllerBuilder()
                            .enableFileOverride();
                })
                .execute();
    }
}