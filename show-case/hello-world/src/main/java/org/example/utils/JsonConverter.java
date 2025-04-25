package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.util.Collection;
import java.util.Objects;

@Slf4j
public class JsonConverter {
    private static final ObjectMapper mapper;
    static {
        mapper = (new ObjectMapper()).configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
    }

    public static String serializeObject(Object object) {
        if (Objects.isNull(object)) {
            return null;
        } else {
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonProcessingException var2) {
                log.error("[JsonConverter] serialize error:", var2);
                return null;
            }
        }
    }


    public static <T> T deserializeObject(String json, Class<T> classOfT) {
        try {
            if (!StringUtils.isBlank(json) && classOfT != null) {
                return mapper.readValue(json, classOfT);
            } else {
                log.warn("[JsonConverter] deserialize json cannot be null!");
                return null;
            }
        } catch (Exception var3) {
            if (String.class.equals(classOfT)) {
                return classOfT.cast(json);
            } else {
                log.error("[JsonConverter] deserialize error:", var3);
                return null;
            }
        }
    }

    public static <T> T deserializeObject(String json, JavaType type) {
        try {
            if (!StringUtils.isBlank(json) && type != null) {
                return mapper.readValue(json, type);
            } else {
                log.warn("[JsonConverter] deserialize json cannot be null!");
                return null;
            }
        } catch (Exception var3) {
            log.error("json deserialize error:", var3);
            return null;
        }
    }

    public static <T> T deserializeObject(byte[] json, JavaType type) {
        try {
            if (json != null && json.length != 0 && type != null) {
                return mapper.readValue(json, type);
            } else {
                log.warn("[JsonConverter] deserialize json byte cannot be null!");
                return null;
            }
        } catch (Exception var3) {
            log.error("[JsonConverter] deserialize error:", var3);
            return null;
        }
    }

    public static <T> T deserializeObject(String json, Class<?> parametrized, Class<?>... parameterClasses) {
        if (!StringUtils.isBlank(json) && parametrized != null && parameterClasses != null) {
            JavaType type = mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);

            try {
                return mapper.readValue(json, type);
            } catch (Exception var5) {
                log.error("[JsonConverter] deserialize error:", var5);
                return null;
            }
        } else {
            log.warn("[JsonConverter] deserialize json cannot be null!");
            return null;
        }
    }

    public static <T> T deserializeObject(byte[] json, Class<?> parametrized, Class<?>... parameterClasses) {
        if (json != null && json.length != 0 && parametrized != null && parameterClasses != null) {
            JavaType type = mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);

            try {
                return mapper.readValue(json, type);
            } catch (Exception var5) {
                log.error("[JsonConverter] deserialize error:", var5);
                return null;
            }
        } else {
            log.warn("[JsonConverter] deserialize json byte cannot be null!");
            return null;
        }
    }

    public static <T> T deserializeObject(String json, TypeReference<T> valueTypeRef) {
        if (StringUtils.isBlank(json)) {
            log.warn("[JsonConverter] deserialize json cannot be null!");
            return null;
        } else {
            try {
                return mapper.readValue(json, valueTypeRef);
            } catch (Exception var3) {
                log.error("[JsonConverter] deserialize error:", var3);
                return null;
            }
        }
    }

    public static <T> T deserializeObject(String json, Class<? extends Collection<?>> collectionClass, Class<?> elementClass) {
        if (StringUtils.isBlank(json)) {
            log.warn("[JsonConverter] deserialize json cannot be null!");
            return null;
        } else {
            try {
                CollectionType type = mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
                return mapper.readValue(json, type);
            } catch (Exception var4) {
                log.error("[JsonConverter] deserialize error:", var4);
                return null;
            }
        }
    }

    public static <T> T deserializeObject(byte[] json, Class<T> classOfT) {
        if (json != null && json.length != 0 && classOfT != null) {
            try {
                return mapper.readValue(json, classOfT);
            } catch (Exception var3) {
                if (String.class.equals(classOfT)) {
                    return classOfT.cast(json);
                } else {
                    log.error("[JsonConverter] deserialize error:", var3);
                    return null;
                }
            }
        } else {
            log.warn("[JsonConverter] deserialize json byte cannot be null!");
            return null;
        }
    }

    public static <T> T deserializeObject(JsonNode json, Class<T> classOfT) {
        try {
            return String.class.equals(classOfT) ? classOfT.cast(json.toString()) : mapper.treeToValue(json, classOfT);
        } catch (Exception var3) {
            if (String.class.equals(classOfT)) {
                return classOfT.cast(json);
            } else {
                log.error("[JsonConverter] deserialize error:", var3);
                return null;
            }
        }
    }

    public static JavaType getJavaType(Class<?> parametrized, Class<?>... parameterClasses) {
        return parameterClasses != null && parameterClasses.length != 0 ? mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses) : mapper.getTypeFactory().constructType(parametrized);
    }
}
