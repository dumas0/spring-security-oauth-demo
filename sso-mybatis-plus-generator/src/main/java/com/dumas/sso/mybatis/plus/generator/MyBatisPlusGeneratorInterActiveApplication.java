package com.dumas.sso.mybatis.plus.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author dzma
 * @date 2023/4/20
 */
public class MyBatisPlusGeneratorInterActiveApplication {
    private static final String ROOT_PATH = "/sso-mybatis-plus-generator/dev/src";
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/sso-oauth?useUnicode=false&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&allowPublicKeyRetrieval=true&failOverReadOnly=false&serverTimezone=GMT%2B8", "root", "root")
                // 全局配置
                .globalConfig((scanner, builder) -> {
                    builder.author(scanner.apply("请输入作者名称："))  // 设置作者
                            .commentDate("yyyy-MM-dd hh:mm:ss")     // 注释日期
                            .outputDir(System.getProperty("user.dir") + ROOT_PATH + "/src/main/java")   // 制定输出目录
                            .disableOpenDir();      // 禁止打开输出目录
                })
                // 包配置
                .packageConfig((scanner, builder) -> {
                    builder.parent(scanner.apply("请输入包名："))   // 设置父包名
                            .moduleName(scanner.apply("请输入模块名："))   // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + ROOT_PATH + "/src/main/resources/mapper"));     // 设置 mapperXml 生成路径
                })
                // 策略配置
                .strategyConfig((scanner, builder) -> {
                    builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号隔开，所有输入 all：")))  // 设置需要生成的表名
                            .addTablePrefix(scanner.apply("请输入过滤表前缀："))  // 设置过滤表前缀
                            // Entity 策略配置
                            .entityBuilder()
                            .formatFileName("%sDO")
                            .enableLombok()
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .enableFileOverride()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            // Mapper 配置
                            .mapperBuilder()
                            .formatMapperFileName("%sMapper")
                            .enableFileOverride()
                            // Service 策略配置
                            .serviceBuilder()
                            .enableFileOverride()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .controllerBuilder()
                            .enableFileOverride();
                })
                // 使用 Freemarker 引擎模板，默认的是 Velocity 引擎模板
//                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
    /**
     * 处理ALL情况
     *
     * @param tables
     * @return
     */
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}