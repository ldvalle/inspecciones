package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "spid",
        "eventDate",
        "event",
        "value"
})

public class ConsultaWOResultado {

    private long spid;
    private Date eventDate;
    private String event;
    private int value;

    public long getSpid() {
        return spid;
    }

    public void setSpid(long spid) {
        this.spid = spid;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /********/
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConsultaWOResponse{");
        sb.append(", spid=").append(spid);
        sb.append(", eventDate=").append(df.format(eventDate));
        sb.append(", event'").append(event).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    private static final SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
}
