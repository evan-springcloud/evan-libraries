package test.org.evan.libraries.kafka.consumer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Evan.Shen
 * @since 2019-08-30
 */
@Slf4j
public class ListenterForDemo  {

//    private ConsumerDataStater consumerDataStater;
//
//    public ListenterForDemo(ConsumerDataStater consumerDataStater) {
//        this.consumerDataStater = consumerDataStater;
//    }
//
//    @Override
//    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//        //log.info(context.getMessageQueue() + "");
//        for (MessageExt e : msgs) {
//            Demo demo = JSON.parseObject(e.getBody(), Demo.class);
//
//            if(log.isTraceEnabled()){
//                log.trace(JSON.toJSONString(demo));
//            }
//
//            if (demo != null && demo.getId() != null) {
//                if (demo.getId() % 15 == 0) {
//                    try {
//                        Thread.sleep(1500); //模拟消费慢
//                        //log.info(">>>>>>>>>>>>>> stoping");
//                    } catch (InterruptedException ex) {
//                        //log.warn(ex.getMessage(),ex);
//                    }
//                }
//            }
//        }
//
//        consumerDataStater.stat(msgs,context);
//
//        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//    }
}
