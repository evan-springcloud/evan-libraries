package test.org.evan.libraries.rocketmq.support.model;

/**
 * @author Evan.Shen
 * @since 2019-09-01
 */
public class MessageStatBO {

    private String brokerName;
    private String topicName;
    private Integer count;

    public MessageStatBO(String brokerName, String topicName, Integer count) {
        this.brokerName = brokerName;
        this.topicName = topicName;
        this.count = count;
    }

    /**
     *
     */
    public String getTopicName() {
        return topicName;
    }

    /***/
    public void setTopicName(String topicName) {
        this.topicName = topicName;
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

    /**
     *
     */
    public String getBrokerName() {
        return brokerName;
    }

    /***/
    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }
}
