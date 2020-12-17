package com.spotify.api.model;

import com.spotify.api.model.base.BaseModel;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.hc.client5.http.utils.DateUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class Room extends BaseModel {

    private String name;
    private String id;
    private Date refreshDate;

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private String gmt;
    @Setter(AccessLevel.NONE) private long connectionTime;
    @Setter(AccessLevel.NONE) private long calculatedEndTime;
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private long durationMs;
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private Datezz formattedDates;

    public void setConnectionTime(Date date) {connectionTime = date.getTime();}
    public void setCalculatedEndTime(long milliseconds){ this.calculatedEndTime = new Date(this.connectionTime + milliseconds).getTime(); }
    public long getDurationMs(){ return Math.abs((connectionTime - calculatedEndTime)); }
    public Datezz getFormattedDates(){ return new Datezz().setStartTime(this.connectionTime).setEndTime(this.calculatedEndTime); }
    public String getGmt(){ return "+0";}

    @Getter
    class Datezz {
        private Date startTime;
        private Date endTime;

        public Datezz setStartTime(long startTime){
            this.startTime = new Date(startTime);
            return this;
        }

        public Datezz setEndTime(long endTime){
            this.endTime = new Date(endTime);
            return this;
        }
    }
}
