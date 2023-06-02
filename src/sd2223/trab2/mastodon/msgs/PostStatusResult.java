package sd2223.trab2.mastodon.msgs;

import sd2223.trab2.api.Message;
import sd2223.trab2.servers.Domain;

import java.time.Instant;

public record PostStatusResult(String id, String content, String created_at, MastodonAccount account) {

    public long getId(){
        return Long.valueOf(id);
    }

    long getCreationTime(){
        return Instant.parse(created_at).toEpochMilli();
    }

    public String getText(){
        return content.replaceAll("<[^>]*>", "");
    }

    public Message toMessage(){
        var m = new Message( getId(), account().username(), Domain.get(), getText());
        m.setCreationTime(getCreationTime());
        return m;
    }
}
