package study.daydayup.wolf.business.union.admin.controller.uc;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.daydayup.wolf.business.account.auth.agent.Session;
import study.daydayup.wolf.business.uc.agent.setting.CompanySettingAgent;
import study.daydayup.wolf.common.lang.ds.ObjectMap;

import javax.annotation.Resource;

/**
 * study.daydayup.wolf.business.union.admin.controller.uc
 *
 * @author Wingle
 * @since 2020/4/17 3:54 下午
 **/
@RestController
public class CompanySettingController {
    @Resource
    private CompanySettingAgent agent;
    @Resource
    private Session session;

    @GetMapping("/uc/company/setting/demo")
    public String demo() {
        Long orgId = session.get("orgId", Long.class);
        agent.init(orgId);

        agent.set("companyName", "test company name");
        agent.set("companyAge", 10);
        agent.save();

        ObjectMap map = agent.getAll();
        return JSON.toJSONString(map);
    }


}