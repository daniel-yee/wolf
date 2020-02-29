package study.daydayup.wolf.business.pay.biz.dal.dao;
import org.apache.ibatis.annotations.Param;

import study.daydayup.wolf.business.pay.biz.dal.dataobject.PaymentDO;

public interface PaymentDAO {
    int deleteById(Long id);

    int insert(PaymentDO record);

    int insertSelective(PaymentDO record);

    PaymentDO selectById(Long id);

    int updateByIdSelective(PaymentDO record);

    int updateById(PaymentDO record);

    int updateByPaymentNo(@Param("updated")PaymentDO updated, @Param("paymentNo")String paymentNo);


}