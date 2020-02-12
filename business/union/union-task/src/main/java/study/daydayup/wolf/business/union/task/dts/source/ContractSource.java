package study.daydayup.wolf.business.union.task.dts.source;

import org.springframework.stereotype.Component;
import study.daydayup.wolf.common.io.db.Table;
import study.daydayup.wolf.framework.dts.source.MysqlScanner;
import study.daydayup.wolf.framework.dts.source.Source;

import javax.annotation.Resource;

/**
 * study.daydayup.wolf.business.union.task.dts.source
 *
 * @author Wingle
 * @since 2020/2/8 8:27 下午
 **/
@Component
public class ContractSource implements Source {
    private static final String TABLE_CONTRACT = "contract";
    private static final String TABLE_STATE_LOG = "trade_state_log";

    @Resource
    private MysqlScanner mysqlScanner;

    public Table latest(long lastId) {
        return mysqlScanner.scan(TABLE_CONTRACT, lastId, "id, buyer_id, seller_id, tags created_at");
    }
}
