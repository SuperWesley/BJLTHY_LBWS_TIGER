package com.bjlthy.lbss.tool;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;

import java.util.HashMap;
import java.util.Map;

public class LogbackUtil {
	
    public static void main(String[] args) {
    }

    public static Logger getLogger(String moduleName, String className) {
        Logger logger = LoggerBuilder.getLogger(moduleName, className);
        return logger;
    }

    private static class LoggerBuilder {
        private static final Map<String, Logger> maps = new HashMap<>();

        public static Logger getLogger(String moduleName, String className) {
            if (maps.containsKey(moduleName)) {
                return maps.get(moduleName);
            }
            synchronized (LoggerBuilder.class) {
                Logger build = build(moduleName, className);
                maps.put(moduleName, build);
                return build;
            }
        }

        private static Logger build(String moduleName, String className) {
            RollingFileAppender errorAppender = Appender.getAppender(moduleName, className, Level.ERROR);
            RollingFileAppender infoAppender = Appender.getAppender(moduleName, className, Level.INFO);
            RollingFileAppender warnAppender = Appender.getAppender(moduleName, className, Level.WARN);
//            RollingFileAppender debugAppender = Appender.getAppender(moduleName, className, Level.DEBUG);
            LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
            Logger logger = context.getLogger("FILE-" + moduleName);

            //设置不向上级打印信息
            logger.setAdditive(false);
            logger.addAppender(errorAppender);
            logger.addAppender(infoAppender);
            logger.addAppender(warnAppender);
//            logger.addAppender(debugAppender);

            return logger;
        }
    }

    private static class Appender {
        public static RollingFileAppender getAppender(String moduleName, String className, Level level) {
            // DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
            LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
            //这里是可以用来设置appender的，在xml配置文件里面，是这种形式：
            // <appender name="error" class="ch.qos.logback.core.rolling.RollingFileAppender">
            RollingFileAppender appender = new RollingFileAppender();
            //ConsoleAppender consoleAppender = new ConsoleAppender();
            
            //这里设置级别过滤器
            LevelFilter levelFilter = getLevelFilter(level);
            levelFilter.start();
            appender.addFilter(levelFilter);

            //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
            // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
            appender.setContext(context);
            //appender的name属性
            appender.setName("FILE-" + moduleName);
            //设置文件名
            appender.setAppend(true);

            appender.setPrudent(false);

            //设置文件创建时间及大小的类
            TimeBasedRollingPolicy policy = new TimeBasedRollingPolicy();

            /*日志路径*/
            String fp = null;
            try {
                fp = OptionHelper.substVars(PathUtil.logPath + moduleName + "/" + className + " %d{yyyy_MM_dd} .%i. " + level.levelStr.toLowerCase() + ".txt", context);
            } catch (ScanException e) {
                throw new RuntimeException(e);
            }
            policy.setFileNamePattern(fp);

            /*设置滚动*/
            SizeAndTimeBasedFNATP triggeringPolicy = new SizeAndTimeBasedFNATP();
            triggeringPolicy.setMaxFileSize(FileSize.valueOf("1MB"));

            policy.setTimeBasedFileNamingAndTriggeringPolicy(triggeringPolicy);

            /*最大保存日期*/
            policy.setMaxHistory(60);

            //设置父节点是appender
            policy.setParent(appender);
            //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
            // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
            policy.setContext(context);
            policy.start();

            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
            // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
            encoder.setContext(context);
            //设置格式
            encoder.setPattern("%d %p %thread (%file:%line\\)- %m%n");
            encoder.start();

            //加入下面两个节点
            appender.setRollingPolicy(policy);
            appender.setEncoder(encoder);
            appender.start();
            return appender;
        }

        private static LevelFilter getLevelFilter(Level level) {
            LevelFilter levelFilter = new LevelFilter();
            levelFilter.setLevel(level);
            levelFilter.setOnMatch(FilterReply.ACCEPT);
            levelFilter.setOnMismatch(FilterReply.DENY);
            return levelFilter;
        }
    }
}
