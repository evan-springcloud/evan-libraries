package test.org.evan.libraries.kafka.support.model;

/**
 * @author Evan.Shen
 * @since 2019-09-01
 */
public class MessageStatBO {
    private int partition;
    private String topic;
    private Integer count;

    public MessageStatBO(int partition, String topic) {
        this.partition = partition;
        this.topic = topic;
    }

    /**
     *
     */
    public int getPartition() {
        return partition;
    }

    /***/
    public void setPartition(int partition) {
        this.partition = partition;
    }

    /**
     *
     */
    public String getTopic() {
        return topic;
    }

    /***/
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     *
     */
    public Integer getCount() {
        return count;
    }

    /***/
    public void setCount(Integer count) {
        this.count = count;
    }
}
