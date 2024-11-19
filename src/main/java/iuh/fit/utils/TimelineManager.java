package iuh.fit.utils;

import javafx.animation.Timeline;

import java.util.HashMap;
import java.util.Map;

public class TimelineManager {
    private static final TimelineManager INSTANCE = new TimelineManager();
    private final Map<String, Timeline> timelines;

    private TimelineManager() {
        timelines = new HashMap<>();
    }

    public static TimelineManager getInstance() {
        return INSTANCE;
    }

    public void addTimeline(String key, Timeline timeline) {
        if (timelines.containsKey(key)) {
            timelines.get(key).stop();
        }
        timelines.put(key, timeline);
    }

    public void removeTimeline(String key) {
        if (timelines.containsKey(key)) {
            timelines.get(key).stop();
            timelines.remove(key);
        }
    }

    public Timeline getTimeline(String key) {
        return timelines.get(key);
    }

    public void stopAllTimelines() {
        for (Timeline timeline : timelines.values()) {
            timeline.stop();
        }
        timelines.clear();
    }

    public boolean containsTimeline(String key) {
        return timelines.containsKey(key);
    }

    public void printAllTimelines() {
        System.out.println("Danh sách Timeline đang được quản lý:");
        if (timelines.isEmpty()) {
            System.out.println("Không có Timeline nào đang chạy.");
            return;
        }
        timelines.forEach((key, timeline) -> {
            System.out.println(" - Key: " + key + ", Status: " + timeline.getStatus());
        });
    }

}
