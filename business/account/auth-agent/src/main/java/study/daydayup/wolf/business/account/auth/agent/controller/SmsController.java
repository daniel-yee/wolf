package study.daydayup.wolf.business.account.auth.agent.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.daydayup.wolf.business.account.api.dto.request.SmsCodeRequest;
import study.daydayup.wolf.business.account.api.dto.request.SmsRequest;
import study.daydayup.wolf.business.account.api.entity.license.OauthLicense;
import study.daydayup.wolf.business.account.api.service.auth.SmsAuthService;
import study.daydayup.wolf.business.account.auth.agent.Session;
import study.daydayup.wolf.business.account.auth.agent.config.AuthConfig;
import study.daydayup.wolf.framework.rpc.Result;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * study.daydayup.wolf.business.account.auth.agent.controller
 *
 * @author Wingle
 * @since 2019/12/4 5:44 下午
 **/
@RestController
public class SmsController extends AuthController {
    @Reference
    private SmsAuthService smsService;
    @Resource
    private Session session;
    @Resource
    private AuthConfig authConfig;

    @GetMapping("/auth/sms/login")
    public Result<OauthLicense> login(@Valid SmsRequest request) {
        return registerAndLogin(request);
    }

    @GetMapping("/auth/sms/registerAndLogin")
    public Result<OauthLicense> registerAndLogin(@Valid SmsRequest request) {
        if (isLogin()) {
            return Result.ok(getLicenseFromSession());
        }

        request.setEnv(null);
        request.setToken(session.getSessionId());

        String scope = formatScope(request.getScope(), request.getOrgId());
        request.setScope(scope);
        request.setExpiredIn(authConfig.getExpiredIn());
        request.setRefreshExpiredIn(authConfig.getRefreshExpiredIn());

        Result<OauthLicense> result = smsService.registerAndLogin(request);
        OauthLicense license = result.notNullData();

        saveLicenseToSession(license);
        return Result.ok(filterLicense(license));
    }

    @GetMapping("/auth/sms/code")
    public Result code(@Validated SmsCodeRequest request) {
        request.setEnv(null);
        request.setExpiredIn(authConfig.getCodeExpiredIn());

        //TODO: 防刷
        smsService.sendCode(request);

        return Result.ok();
    }

    @GetMapping("/auth/sms/voice")
    public Result voice(SmsCodeRequest request) {
        return Result.ok();
    }
}
