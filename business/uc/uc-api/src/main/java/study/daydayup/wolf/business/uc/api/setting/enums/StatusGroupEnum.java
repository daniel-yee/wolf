package study.daydayup.wolf.business.uc.api.setting.enums;

import lombok.Getter;

/**
 * study.daydayup.wolf.business.trade.api.enums
 * range( 30 ~ 50 )
 * @author Wingle
 * @since 2019/10/5 11:07 AM
 **/
@Getter
public enum StatusGroupEnum  {
    TRADE_TAG(TradeTagEnum.class),
    CUSTOMER_TAG(CustomerTagEnum.class),
    CUSTOMER_AUTH(CustomerAuthEnum.class),
    CUSTOMER_INFO(CustomerInfoEnum.class)
    ;


    private Class<StatusEnum> group;
    StatusGroupEnum(Class<StatusEnum> group) {
        this.group = group;
    }
}
