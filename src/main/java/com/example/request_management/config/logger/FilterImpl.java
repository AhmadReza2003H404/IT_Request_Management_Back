package com.example.request_management.config.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

import java.util.List;

public class FilterImpl extends TurboFilter {
    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String s, Object[] objects, Throwable throwable) {
        var replay = FilterReply.NEUTRAL;
        if (List.of(Level.ERROR, Level.WARN).contains(level) && throwable != null) {
            ExceptionInquest exceptionInquest = DefaultExceptionInquest.getInstance();
            var writeLevel = exceptionInquest.getLogGrade(throwable);
            switch (writeLevel) {
                case FULL_STACK_TRACE:
                    break;
                case ERROR_MESSAGE:
                case ADDRESS:
                    replay = FilterReply.DENY;
                    var res = exceptionInquest.getLog(throwable);
                    logger.error(res);
                    break;
                case IGNORE:
                    logger.info("Ignored -> " + throwable.toString());
                    replay = FilterReply.DENY;
                    break;
            }
        }
        return replay;
    }
}