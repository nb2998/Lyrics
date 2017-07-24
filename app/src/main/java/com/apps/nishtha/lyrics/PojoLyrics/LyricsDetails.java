package com.apps.nishtha.lyrics.PojoLyrics;

/**
 * Created by nishtha on 21/7/17.
 */

public class LyricsDetails {
    private MessageLyrics message;

    public MessageLyrics getMessageLyrics() {
        return message;
    }

    public LyricsDetails(MessageLyrics message) {

        this.message = message;
    }
}
