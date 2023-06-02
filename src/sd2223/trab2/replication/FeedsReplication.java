package sd2223.trab2.replication;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.java.Result;

import java.util.List;

import static sd2223.trab2.api.java.Result.ErrorCode.NOT_FOUND;

public class FeedsReplication implements Feeds {
        private ReplicationManager replicationManager;
        private FeedsDatabase primaryDatabase;
        private FeedsDatabase secondaryDatabase;

        public FeedsReplication() {
            this.replicationManager = new ReplicationManager();
            this.primaryDatabase = new FeedsDatabase();
            this.secondaryDatabase = new FeedsDatabase();
        }

        @Override
        public Result<Long> postMessage(String user, String pwd, Message msg) {

            long sequenceNumber = primaryDatabase.generateSequenceNumber();


            primaryDatabase.postMessage(user, pwd, msg);


            String operation = "postMessage;" + user + ";" + pwd + ";" + msg.toString() + ";" + sequenceNumber;
            replicationManager.replicateOperation(operation);

            return Result.ok();
        }

        @Override
        public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) {

            primaryDatabase.removeFromPersonalFeed(user, mid, pwd);


            String operation = "removeFromPersonalFeed;" + user + ";" + mid + ";" + pwd;
            replicationManager.replicateOperation(operation);


            return Result.ok();
        }

        @Override
        public Result<Message> getMessage(String user, long mid) {
            Message message = primaryDatabase.getMessage(user, mid);
            if (message != null) {
                return Result.ok(message);
            } else {
                return Result.error( NOT_FOUND);
            }
        }

        @Override
        public Result<List<Message>> getMessages(String user, long time) {
            List<Message> messages = primaryDatabase.getMessages(user, time);
            return Result.ok(messages);
        }

        @Override
        public Result<Void> subUser(String user, String userSub, String pwd) {
            primaryDatabase.subUser(user, userSub, pwd);

            String operation = "subUser;" + user + ";" + userSub + ";" + pwd;
            replicationManager.replicateOperation(operation);

            return Result.ok();
        }

        @Override
        public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
            primaryDatabase.unsubscribeUser(user, userSub, pwd);

            String operation = "unsubscribeUser;" + user + ";" + userSub + ";" + pwd;
            replicationManager.replicateOperation(operation);

            return Result.ok();
        }

        @Override
        public Result<List<String>> listSubs(String user) {
            List<String> subscriptions = primaryDatabase.listSubs(user);
            return Result.ok(subscriptions);
        }

        @Override
        public Result<Void> deleteUserFeed(String user) {
            primaryDatabase.deleteUserFeed(user);

            String operation = "deleteUserFeed;" + user;
            replicationManager.replicateOperation(operation);

            return Result.ok();
        }

public void close() {

            replicationManager.close();
            primaryDatabase.close();
            secondaryDatabase.close();
        }
    }