package study.daydayup.wolf.business.trade.buy.biz.common.flow;

import org.springframework.stereotype.Component;
import study.daydayup.wolf.business.trade.buy.biz.common.AbstractTradeFlow;
import study.daydayup.wolf.business.trade.buy.biz.common.TradeFlow;
import study.daydayup.wolf.business.trade.buy.biz.common.TradeNode;
import study.daydayup.wolf.business.trade.buy.biz.common.node.*;

import java.util.List;

/**
 * study.daydayup.wolf.business.trade.buy.domain.entity.flow
 *
 * @author Wingle
 * @since 2019/10/5 10:56 AM
 **/
@Component
public class BuyFlow extends AbstractTradeFlow implements TradeFlow {


    @Override
    public List<TradeNode> buildConfirmFlow() {
        return null;
    }

    @Override
    public List<TradeNode> buildPreviewFlow() {
        return null;
    }

    @Override
    public List<TradeNode> buildPayFlow() {
        return null;
    }

    @Override
    public List<TradeNode> buildPayNotifyFlow() {
        return null;
    }
}