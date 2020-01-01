package study.daydayup.wolf.business.account.api.exception;

import study.daydayup.wolf.common.lang.exception.BusinessException;

/**
 * study.daydayup.wolf.business.account.auth.agent.exception
 *
 * @author Wingle
 * @since 2019/12/12 3:04 下午
 **/
public class AuthFailedException extends BusinessException {
    public AuthFailedException() {
        super(111004, "auth failed");
    }
}
