/*
 * Copyright 2025 k7t3
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.k7t3.horzcv.shared.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * ストリーマーの情報を検索したレスポンスデータ
 */
public class StreamerInfoResponse implements Serializable {

    public static final StreamerInfoResponse EMPTY = new StreamerInfoResponse(new StreamerInfo[0], false);

    private StreamerInfo[] infoArray;
    private boolean isIdentified;

    /**
     * GWTのシリアライゼーション用の空のコンストラクタ（必須）
     */
    public StreamerInfoResponse() {
        this.infoArray = new StreamerInfo[0];
        this.isIdentified = false;
    }

    /**
     * @param infoArray     検出できたストリーマーのリスト
     * @param isIdentified trueのとき、一意に特定できておりinfoListが一つのみ。それ以外はfalse
     */
    public StreamerInfoResponse(
            StreamerInfo[] infoArray,
            boolean isIdentified
    ) {
        setInfoArray(infoArray);
        setIdentified(isIdentified);
    }

    public static StreamerInfoResponse empty() {
        return EMPTY;
    }

    public static StreamerInfoResponse of(StreamerInfo info) {
        return new StreamerInfoResponse(info == null ? new StreamerInfo[0] : new StreamerInfo[]{info}, info != null);
    }

    public static StreamerInfoResponse of(List<StreamerInfo> infoList) {
        return new StreamerInfoResponse(infoList.toArray(new StreamerInfo[0]), false);
    }

    public void setInfoArray(StreamerInfo[] infoArray) {
        this.infoArray = infoArray != null ? Arrays.copyOf(infoArray, infoArray.length) : new StreamerInfo[0];
    }

    public boolean isEmpty() {
        return infoArray == null || infoArray.length == 0;
    }

    public StreamerInfo[] getInfoArray() {
        return infoArray;
    }

    public void setIdentified(boolean identified) {
        isIdentified = identified;
    }

    public boolean isIdentified() {
        return isIdentified;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StreamerInfoResponse) obj;
        return Arrays.equals(this.infoArray, that.infoArray) &&
                this.isIdentified == that.isIdentified;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(infoArray), isIdentified);
    }

    @Override
    public String toString() {
        return "StreamerInfoResponse[" +
                "infoList=" + Arrays.toString(infoArray) + ", " +
                "isIdentified=" + isIdentified + ']';
    }
}