package top.crossoverjie.cicada.example.action;

import org.slf4j.Logger;
import top.crossoverjie.cicada.example.configuration.KafkaConfiguration;
import top.crossoverjie.cicada.example.configuration.RedisConfiguration;
import top.crossoverjie.cicada.example.enums.StatusEnum;
import top.crossoverjie.cicada.example.res.DemoResVO;
import top.crossoverjie.cicada.server.action.WorkAction;
import top.crossoverjie.cicada.server.action.param.Param;
import top.crossoverjie.cicada.server.action.res.WorkRes;
import top.crossoverjie.cicada.server.annotation.CicadaAction;
import top.crossoverjie.cicada.server.configuration.ApplicationConfiguration;
import top.crossoverjie.cicada.server.configuration.ConfigurationHolder;
import top.crossoverjie.cicada.server.context.CicadaContext;
import top.crossoverjie.cicada.server.util.LoggerBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static top.crossoverjie.cicada.server.configuration.ConfigurationHolder.getConfiguration;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/8/31 18:52
 * @since JDK 1.8
 */
@CicadaAction(value = "demoAction")
public class DemoAction implements WorkAction {


    private static final Logger LOGGER = LoggerBuilder.getLogger(DemoAction.class);

    private static AtomicLong index = new AtomicLong();

    @Override
    public void execute(CicadaContext context, Param paramMap) throws Exception {

        KafkaConfiguration configuration = (KafkaConfiguration) getConfiguration(KafkaConfiguration.class);
        RedisConfiguration redisConfiguration = (RedisConfiguration) ConfigurationHolder.getConfiguration(RedisConfiguration.class);
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) ConfigurationHolder.getConfiguration(ApplicationConfiguration.class);

        String brokerList = configuration.get("kafka.broker.list");
        String redisHost = redisConfiguration.get("redis.host");
        String port = applicationConfiguration.get("cicada.port");

        LOGGER.info("Configuration brokerList=[{}],redisHost=[{}] port=[{}]", brokerList, redisHost, port);

        String name = paramMap.getString("name");
        Integer id = paramMap.getInteger("id");
        LOGGER.info("name=[{}],id=[{}]", name, id);


        String url = context.request().getUrl();
        String method = context.request().getMethod();


        //业务操作
        selectDB();
        insertDB();

        DemoResVO demoResVO = new DemoResVO();
        demoResVO.setIndex(index.incrementAndGet());
        demoResVO.setMsg(url + " " + method);
        WorkRes<DemoResVO> res = new WorkRes();
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        res.setDataBody(demoResVO);

        context.json(res);
    }


    private void selectDB(){
        LOGGER.info("开始查询数据库");
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException",e);
        }

        LOGGER.info("查询数据库成功");
    }

    private void insertDB(){
        LOGGER.info("开始写入数据库");
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException",e);
        }

        LOGGER.info("开始写入数据库成功");
    }
}
