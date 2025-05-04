package rs.delimo.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.json.JsonWriter;
import org.springframework.boot.logging.structured.StructuredLogFormatter;

import java.io.IOException;

public class JsonLogger implements StructuredLogFormatter<ILoggingEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private final JsonWriter<ILoggingEvent> writer = JsonWriter.<ILoggingEvent>of(members -> {
        members.add("Level", e -> e.getLevel().toString());
        members.add("Message", ILoggingEvent::getFormattedMessage);
        members.add("timestamp", e -> String.valueOf(e.getTimeStamp()));
        members.add("class", e -> {
            StackTraceElement[] callerData = e.getCallerData();
            return (callerData != null && callerData.length > 0) ? callerData[0].getClassName() : "unknown";
        });
        members.add("method", e -> {
            StackTraceElement[] callerData = e.getCallerData();
            return (callerData != null && callerData.length > 0) ? callerData[0].getMethodName() : "unknown";
        });
        members.add("line", e -> {
            StackTraceElement[] callerData = e.getCallerData();
            return (callerData != null && callerData.length > 0) ? String.valueOf(callerData[0].getLineNumber()) : "-1";
        });
    }).withNewLineAtEnd();

    @Override
    public String format(ILoggingEvent event) {
        String json = this.writer.writeToString(event);
        try {
            Object obj = objectMapper.readValue(json, Object.class);
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            return json;
        }
    }
}
