package com.apps.nishtha.lyrics.PojoLyrics;

/**
 * Created by nishtha on 21/7/17.
 */

public class MessageLyrics {
    private BodyLyrics body;

    public BodyLyrics getBodyLyrics() {
        return body;
    }

    public MessageLyrics(BodyLyrics body) {
        this.body = body;
    }
}
