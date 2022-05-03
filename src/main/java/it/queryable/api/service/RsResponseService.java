package it.queryable.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.queryable.api.management.AppConstants;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class RsResponseService implements Serializable {


    @Context
    UriInfo ui;

    private static final long serialVersionUID = 1L;

    public static Response jsonResponse(Map<String, String> toJson, Status status) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = objectMapper.writeValueAsString(toJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.status(status).entity(jsonStr).build();
    }

    public static Response jsonResponse(Status status, String key, Object value) {
        Map<String, String> toJson = new HashMap<String, String>();
        toJson.put(key, value.toString());
        return jsonResponse(toJson, status);
    }

    public static Response jsonMessageResponse(Status status, Object object) {
        if (object instanceof Throwable) {
            Throwable t = (Throwable) object;
            return jsonResponse(status, AppConstants.JSON_GENERIC_MESSAGE_KEY, getErrorMessage(t));
        } else {
            return jsonResponse(status, AppConstants.JSON_GENERIC_MESSAGE_KEY, "" + object);

        }
    }

    public static Response jsonErrorMessageResponse(Object error) {
        if (error instanceof Throwable) {
            Throwable t = (Throwable) error;
            return jsonResponse(Status.INTERNAL_SERVER_ERROR, AppConstants.JSON_GENERIC_MESSAGE_KEY, getErrorMessage(t));
        } else {
            return jsonResponse(Status.INTERNAL_SERVER_ERROR, AppConstants.JSON_GENERIC_MESSAGE_KEY, "" + error);
        }
    }

    private static String getErrorMessage(Throwable t) {
        String exceptionClass = t.getClass().getCanonicalName();
        return t.getMessage() == null ?
                exceptionClass : MessageFormat.format("{0}: {1}", exceptionClass, t.getMessage());
    }

    @SuppressWarnings("unchecked")
    public <T> T cast(String key, Class<T> clazz) {
        String value = ui.getQueryParameters().getFirst(key);
        if (Long.class.equals(clazz)) {
            return (T) Long.valueOf(value);
        }
        if (Integer.class.equals(clazz)) {
            return (T) Integer.valueOf(value);
        }
        if (Boolean.class.equals(clazz)) {
            return (T) Boolean.valueOf(value);
        }
        return (T) value;
    }

    public String get(String key) {
        return ui.getQueryParameters().getFirst(key);
    }

    public String lowercase(String key) {
        return get(key) != null ? get(key).toLowerCase() : null;
    }

    public List<String> asList(String key) {
            String value = get(key);
            return Stream.of(value.split(",", -1))
                    .collect(Collectors.toList());
        }

        public List<Integer> asIntegerList(String key) {
            String value = get(key);
            return Stream.of(value.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }

        public List<Long> asLongList(String key) {
            String value = get(key);
            return Stream.of(value.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }

    public Integer _integer(String key) {
        String value = ui.getQueryParameters().getFirst(key);
        return Integer.valueOf(value);
    }

    public Long _long(String key) {
        String value = ui.getQueryParameters().getFirst(key);
        return Long.valueOf(value);
    }

    public Boolean _boolean(String key) {
        String value = ui.getQueryParameters().getFirst(key);
        return Boolean.valueOf(value);
    }

    protected final String likeParamToLowerCase(String value) {
        return "%" + get(value).toLowerCase() + "%";
    }


    protected boolean nn(String key) {
        return ui.getQueryParameters().containsKey(key)
                && ui.getQueryParameters().getFirst(key) != null
                && !ui.getQueryParameters().getFirst(key).trim().isEmpty();
    }

    protected String likeParam(String param) {
        return "%" + get(param) + "%";
    }

    protected String likeParamL(String param) {
        return "%" + get(param);
    }

    protected String likeParamR(String param) {
        return get(param) + "%";
    }

}