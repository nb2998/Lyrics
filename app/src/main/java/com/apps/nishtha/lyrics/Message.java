package com.apps.nishtha.lyrics;

import com.apps.nishtha.lyrics.PojoForId.Body;

/**
 * Created by nishtha on 20/7/17.
 */

public class Message {
    private Body body;

    public Body getBody() {
        return body;
    }

    public Message(Body body) {
        this.body = body;
    }
}
