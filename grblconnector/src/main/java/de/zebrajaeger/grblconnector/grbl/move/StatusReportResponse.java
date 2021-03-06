package de.zebrajaeger.grblconnector.grbl.move;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Created by Lars Brandt on 17.07.2017.
 */
public class StatusReportResponse implements Serializable {
    private String state;
    private Pos mpos, wco;

    private StatusReportResponse() {
    }

    public static StatusReportResponse of(String response) {
        if (response == null || StringUtils.isBlank(response)) {
            return null;
        }

        StatusReportResponse result = new StatusReportResponse();
        String raw = response.trim();
        if (raw.startsWith("<")) {
            raw = raw.substring(1);
        }
        if (raw.endsWith(">")) {
            raw = raw.substring(0, raw.length() - 1);
        }

        StringTokenizer st = new StringTokenizer(raw, "|");
        if (st.hasMoreTokens()) {
            result.state = st.nextToken();
        }

        while (st.hasMoreTokens()) {
            String[] parts = st.nextToken().split(":");
            if (parts.length == 2) {
                String key = parts[0];

                if ("MPos".equals(key)) {
                    result.mpos = Pos.of(parts[1]);
                } else if ("WCO".equals(key)) {
                    result.wco = Pos.of(parts[1]);
                }
            }
        }
        return result;
    }

    public String getState() {
        return state;
    }

    public Pos getMpos() {
        return mpos;
    }

    public Pos getWco() {
        return wco;
    }

    @Override
    public String toString() {
        return "StatusReportResponse{" +
                "state='" + state + '\'' +
                ", mpos=" + mpos +
                ", wco=" + wco +
                '}';
    }

}
