import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerMain {
  public static void main(String[] args) {
    Producer<Long, String> producer = ProducerCreator.createProducer();
    for (int index = 0; index < 1000; ++index) {
      ProducerRecord<Long, String> record = new ProducerRecord<>("vendor-in", "This is record " + index);
      try {
        RecordMetadata metadata = producer.send(record).get(1000, TimeUnit.SECONDS);
        System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
          + " with offset " + metadata.offset());
      } catch (ExecutionException | InterruptedException | TimeoutException e) {
        System.out.println("Error in sending record");
        System.out.println(e);
      }
    }
  }
}