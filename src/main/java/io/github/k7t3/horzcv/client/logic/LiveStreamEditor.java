package io.github.k7t3.horzcv.client.logic;


import io.github.k7t3.horzcv.client.model.InputLiveStream;
import io.github.k7t3.horzcv.client.model.LiveStream;
import io.github.k7t3.horzcv.client.model.LiveStreamDetector;
import io.github.k7t3.horzcv.client.model.StreamingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ユーザーが入力するストリーミングを管理するクラス。
 */
public class LiveStreamEditor {

    private final List<InputLiveStream> streams = new ArrayList<>();

    private final Map<StreamingService, LiveStreamDetector> detectors;

    public LiveStreamEditor() {
        detectors = new HashMap<>();
    }

    public void registerDetector(StreamingService service, LiveStreamDetector detector) {
        detectors.put(service, detector);
    }

    /**
     * 空の要素を追加するメソッド。
     */
    public void add(InputLiveStream stream) {
        streams.add(stream);
    }

    /**
     * 既知のストリームリストから入力データを登録するメソッド。
     * @param streams 既知のストリーム
     */
    public void setAll(List<LiveStream> streams) {
        this.streams.clear();

        for (var s : streams) {
            var detector = detectors.get(s.service());
            if (detector != null) {
                var url = detector.construct(s.id());
                var stream = new InputLiveStream();
                stream.setService(s.service());
                stream.setUrl(url);
                this.streams.add(stream);
            }
        }
    }

    public List<InputLiveStream> getInputItems() {
        return streams;
    }

    /**
     * 入力要素を削除する
     * @param stream 入力要素
     */
    public void removed(InputLiveStream stream) {
        streams.remove(stream);
    }

    // 事前に入力内容が有効であるか検証しておくこと
    private LiveStream toLiveStream(InputLiveStream input) {
        var detector = detectors.get(input.getService());
        var id = detector.parseId(input.getUrl());
        return new LiveStream(input.getService(), id);
    }

    /**
     * 入力された情報のうち、有効な情報のリストを取得するメソッド。
     * @return 配信のリスト
     */
    public List<LiveStream> getLiveStreams() {
        return streams.stream()
                .filter(this::validate)
                .map(this::toLiveStream)
                .collect(Collectors.toList());
    }

    /**
     * 入力されたURLに適したサービスを探すメソッド。
     * <p>
     *     適したものが見つからないときはnullが返される。
     * </p>
     * @param url 入力されたURL
     * @return 適したサービス。あるいはnull
     */
    public StreamingService findSuitableService(String url) {
        return detectors.entrySet()
                .stream()
                .filter(e -> e.getValue().isValidURL(url))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * 入力された情報が正しい書式か検証するメソッド。
     * @param stream 入力された情報
     * @return 入力された内容が正しいときはtrue
     */
    public boolean validate(InputLiveStream stream) {
        var detector = detectors.get(stream.getService());
        if (detector == null) {
            return false;
        }
        return detector.isValidURL(stream.getUrl());
    }

}
