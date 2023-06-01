package sd2223.trab2.replication;

import java.util.*;

import sd2223.trab2.api.Message;

    public class FeedsDatabase {
        private Map<String, List<Message>> userFeeds;
        private long sequenceNumber;

        private Map<String, List<String>> userSubs;

        public FeedsDatabase() {
            this.userFeeds = new HashMap<>();
            this.sequenceNumber = 0;
            this.userSubs = new HashMap<>();
        }

        public synchronized void postMessage(String user, String pwd, Message msg) {
            // Verificar autenticação do usuário e validar senha

            // Adicionar a mensagem ao feed do usuário
            List<Message> feed = userFeeds.getOrDefault(user, new ArrayList<>());
            feed.add(msg);
            userFeeds.put(user, feed);
        }

        public synchronized void removeFromPersonalFeed(String user, long mid, String pwd) {
            // Verificar autenticação do usuário e validar senha

            // Remover a mensagem do feed do usuário
            List<Message> feed = userFeeds.get(user);
            if (feed != null) {
                feed.removeIf(msg -> msg.getId() == mid);
            }
        }

        public synchronized Message getMessage(String user, long mid) {
            // Obter a mensagem específica do feed do usuário
            List<Message> feed = userFeeds.get(user);
            if (feed != null) {
                for (Message msg : feed) {
                    if (msg.getId() == mid) {
                        return msg;
                    }
                }
            }
            return null;
        }

        public synchronized List<Message> getMessages(String user, long time) {
            // Obter todas as mensagens no feed do usuário a partir de um determinado tempo
            List<Message> feed = userFeeds.get(user);
            if (feed != null) {
                List<Message> result = new ArrayList<>();
                for (Message msg : feed) {
                    if (msg.getCreationTime() >= time) {
                        result.add(msg);
                    }
                }
                return result;
            }
            return Collections.emptyList();
        }

        public synchronized void subUser(String user, String userSub, String pwd) {

            List<String> subs = userSubs.getOrDefault(user, new ArrayList<>());
            subs.add(userSub);
            userSubs.put(user, subs);
        }

        public synchronized void unsubscribeUser(String user, String userSub, String pwd) {

            List<String> subs = userSubs.get(user);
            if (subs != null) {
                subs.remove(userSub);
            }
        }

        public synchronized List<String> listSubs(String user) {

            return userSubs.getOrDefault(user, Collections.emptyList());
        }

        public synchronized void deleteUserFeed(String user) {
            userFeeds.remove(user);

            for (List<String> subs : userSubs.values()) {
                subs.remove(user);
            }
        }

        public synchronized long generateSequenceNumber() {

            return ++sequenceNumber;
        }

        public void close() {

        }
    }
