package sd2223.trab2.replication;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.java.Result;

import java.util.List;

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
            return null;
        }

        @Override
        public Result<List<Message>> getMessages(String user, long time) {
            return null;
        }

        @Override
        public Result<Void> subUser(String user, String userSub, String pwd) {
            return null;
        }

        @Override
        public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
            return null;
        }

        @Override
        public Result<List<String>> listSubs(String user) {
            return null;
        }

        @Override
        public Result<Void> deleteUserFeed(String user) {
            return null;
        }


        public void close() {

            replicationManager.close();
            primaryDatabase.close();
            secondaryDatabase.close();
        }
    }