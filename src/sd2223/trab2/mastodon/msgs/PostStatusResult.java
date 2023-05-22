package sd2223.trab2.mastodon.msgs;

import sd2223.trab2.api.Message;

import java.time.Instant;

public record PostStatusResult(String id, String content, String created_at, MastodonAccount account) {

    public long getId(){
        return Long.valueOf(id);
    }

    long getCreationTime(){
        return Instant.parse(created_at).toEpochMilli();
    }

    public String getText(){
        return content;
    }

    public Message toMessage(){
        //System.out.println("Account Id:"+account.id());
        //System.out.println("//");
        //System.out.println("Account username:"+account.username());
        var m = new Message( getId(), "todo", "todo", getText());
        m.setCreationTime(getCreationTime());
        return m;
    }
}
