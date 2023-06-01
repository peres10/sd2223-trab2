package sd2223.trab2.replication;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ReplicationManager {
    private KafkaProducer<String, String> producer;
    private String replicationTopic;

    public ReplicationManager() {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        producer = new KafkaProducer<>(props);


        replicationTopic = "replication_topic";
    }

    public void replicateOperation(String operation) {

        producer.send(new ProducerRecord<>(replicationTopic, operation));
    }

    // Outros métodos de replicação, se necessário

    public void close() {
        // Fechar o produtor Kafka
        producer.close();
    }
}

