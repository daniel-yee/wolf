package study.daydayup.wolf.business.uc.setting.api.service;

import study.daydayup.wolf.business.uc.setting.api.entity.AccountStatus;
import study.daydayup.wolf.framework.layer.domain.Service;
import study.daydayup.wolf.framework.rpc.Result;

/**
 * study.daydayup.wolf.business.uc.api.account.service
 *
 * @author Wingle
 * @since 2019/12/31 7:46 下午
 **/
public interface AccountStatusService extends Service {
    Result<AccountStatus> find(Long accountId);
    Result<Integer> save(AccountStatus accountStatus);
}
