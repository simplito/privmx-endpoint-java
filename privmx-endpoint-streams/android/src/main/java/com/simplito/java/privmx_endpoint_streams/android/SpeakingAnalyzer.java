package com.simplito.java.privmx_endpoint_streams.android;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SpeakingAnalyzer implements RmsObserver {

    static class Config {
        float rmsEmaAlpha;
        float noiseEmaAlpha;
        byte thresholdOffset;
        long activityWindowMs;
        long holdMs;

        Config(
                float rmsEmaAlpha,
                float noiseEmaAlpha,
                byte thresholdOffset,
                long activityWindowMs,
                long holdMs
        ) {
            this.rmsEmaAlpha = rmsEmaAlpha;
            this.noiseEmaAlpha = noiseEmaAlpha;
            this.thresholdOffset = thresholdOffset;
            this.activityWindowMs = activityWindowMs;
            this.holdMs = holdMs;
        }
    }

    static final Config DefaultConfig = new Config(
            0.2f,
            0.02f,
            (byte) 6,
            400L,
            200L
    );

    static class StreamSpeakingMeasurement {
        float emaRms = 0f;
        float noiseFloor = 0f;
        long activationThresholdTimestamp = 0;
        long activeToTimestamp = 0;
    }

    private final Config config;
    final Map<String, StreamSpeakingMeasurement> speakers = new HashMap<>();
    SpeakingAnalyzer(Config config){
        this.config = config;
    }

    @Override
    public void onRms(String streamId, byte rms, long timestamp) {
        StreamSpeakingMeasurement currentMeasurement = getStreamMeasurement(streamId);
        synchronized (currentMeasurement) {
            currentMeasurement.emaRms = config.rmsEmaAlpha * rms + (1 - config.rmsEmaAlpha) * currentMeasurement.emaRms;

            if (currentMeasurement.emaRms < currentMeasurement.noiseFloor + config.thresholdOffset) {
                currentMeasurement.noiseFloor =
                        config.noiseEmaAlpha * currentMeasurement.emaRms +
                                (1 - config.noiseEmaAlpha) * currentMeasurement.noiseFloor;
            }

            float adaptiveThreshold = currentMeasurement.noiseFloor + config.thresholdOffset;

            if (currentMeasurement.emaRms >= adaptiveThreshold) {
                if(timestamp > currentMeasurement.activationThresholdTimestamp) {
                    currentMeasurement.activeToTimestamp = timestamp + config.holdMs;
                }
            } else {
                if(timestamp > currentMeasurement.activeToTimestamp) {
                    currentMeasurement.activationThresholdTimestamp = timestamp + config.activityWindowMs;
                }
            }
        }
    }



    public Map<String, Long> getSpeakersInfo() {
        return speakers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, it -> it.getValue().activeToTimestamp == 0 ? -1: it.getValue().activeToTimestamp));
    }

    public SpeakingAnalyzer.StreamSpeakingMeasurement getStreamMeasurement(String streamId) {
        synchronized (speakers) {
            SpeakingAnalyzer.StreamSpeakingMeasurement state = speakers.get(streamId);
            if (state == null) {
                state = new SpeakingAnalyzer.StreamSpeakingMeasurement();
                speakers.put(streamId, state);
            }
            return state;
        }
    }
}
