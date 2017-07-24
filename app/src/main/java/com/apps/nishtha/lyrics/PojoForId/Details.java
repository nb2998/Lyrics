package com.apps.nishtha.lyrics.PojoForId;

import com.apps.nishtha.lyrics.Message;

/**
 * Created by nishtha on 20/7/17.
 */

public class Details {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public Details(Message message) {

        this.message = message;
    }
}
