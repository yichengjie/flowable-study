package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class LogUtil {

    private static final String TRACE_LOG = "[%s|%s] %s";
    private static final String LAMBDA_REGEX = "lambda$";

//    public static boolean isTimeOutException(HystrixRuntimeException ex) {
//        if (Objects.isNull(ex) || Objects.isNull(ex.getCause())) {
//            return false;
//        }
//        return (ex.getCause() instanceof TimeoutException);
//    }
//
//    public static String getFeignErrorMessage(HystrixRuntimeException ex) {
//        String message = ex.getMessage();
//        if (ex.getCause() instanceof FeignHttpException) {
//            FeignHttpException feignHttpException = (FeignHttpException) ex.getCause();
//            message = feignHttpException.getMessage();
//        }
//        return simpleFeignExceptionMessage(message);
//    }

    public static String simpleFeignExceptionMessage(String message) {
        if (StringUtils.isEmpty(message)) {
            return message;
        }
        String startPrefix = "caseBy=";
        String endPrefix = ",";

        if (message.indexOf(startPrefix) <= 0) {
            return message;
        }

        try {
            int firstPhase = message.indexOf(startPrefix) + startPrefix.length();
            int secondPhase = message.indexOf(endPrefix, firstPhase);

            if (secondPhase > firstPhase && message.length() > firstPhase) {
                return message.substring(firstPhase, secondPhase);
            }
        } catch (Exception exception) {
            log.error("[LogUtil|simpleFeignExceptionMessage] Trace simpleFeignExceptionMessage error : {}", message, exception);
        }
        return message;
    }
    private static String trimSimpleName(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }

        if (input.indexOf(LAMBDA_REGEX) == 0) {
            int firstPhase = input.indexOf("$") + 1;
            int secondPhase = input.lastIndexOf("$");
            if (secondPhase > firstPhase && input.length() > firstPhase) {
                input = input.substring(firstPhase, secondPhase);
            }
        }

        return input.indexOf("$") > -1 ? input.substring(0, input.indexOf("$")) : input;
    }

    public static String replaceHeader(Logger logger, String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }
        try {
            if (input.startsWith("[")) {
                int endPhase = input.indexOf("] ");
                if (endPhase > -1 && input.length() > endPhase + 1) {
                    return input.substring(endPhase + 1);
                }
            }
            return input;
        } catch (Exception e) {
            logger.error("[LogUtil|replaceHeader] Trace replaceHeader error : {}", input, e);
        }

        return input;
    }

    private static String produceWholeTraceLog(Logger logger, String traceLog) {
        if (Objects.isNull(logger) || StringUtils.isEmpty(traceLog)) {
            return traceLog;
        }
        // replace [XX|XX]
        traceLog = replaceHeader(logger, traceLog);

        try {
            String className = ClassUtils.getSimpleName(Thread.currentThread().getStackTrace()[3].getClassName());
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            return String.format(TRACE_LOG, trimSimpleName(className), trimSimpleName(methodName), traceLog);
        } catch (Exception e) {
            logger.error("[LogUtil|produceWholeTraceLog] Trace error : {}", traceLog, e);
            return traceLog;
        }
    }

    private static boolean checkAvailable(Logger logger, String traceLog) {
        return Objects.nonNull(logger) && StringUtils.isNotEmpty(traceLog);
    }

    public static void debug(Logger logger, String traceLog, Object... placeHolds) {
        if (!checkAvailable(logger, traceLog)) {
            return;
        }

        logger.debug(produceWholeTraceLog(logger, traceLog), formatPlaceHold(placeHolds));
    }

    public static void info(Logger logger, String traceLog, Object... placeHolds) {
        if (!checkAvailable(logger, traceLog)) {
            return;
        }

        logger.info(produceWholeTraceLog(logger, traceLog), formatPlaceHold(placeHolds));
    }

    public static void warn(Logger logger, String traceLog, Object... placeHolds) {
        if (!checkAvailable(logger, traceLog)) {
            return;
        }

        logger.warn(produceWholeTraceLog(logger, traceLog), formatPlaceHold(placeHolds));
    }

    public static void error(Logger logger, String traceLog, Object... placeHolds) {
        if (!checkAvailable(logger, traceLog)) {
            return;
        }

        logger.error(produceWholeTraceLog(logger, traceLog), placeHolds);
    }

    public static String[] formatPlaceHold(Object[] placeHolds) {
        if (Objects.isNull(placeHolds) || placeHolds.length == 0) {
            return new String[]{};
        }

        return Arrays.stream(placeHolds).map(
            obj -> {
                if (Objects.isNull(obj) || obj instanceof String) {
                    return obj;
                } else {
                    return JsonConverter.serializeObject(obj);
                }
            }
        ).toArray(String[]::new);
    }
}
