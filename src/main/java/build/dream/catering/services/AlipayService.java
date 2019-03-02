package build.dream.catering.services;

import build.dream.catering.models.alipay.MarketingCardTemplateCreateModel;
import build.dream.common.api.ApiRest;
import build.dream.common.models.alipay.AlipayMarketingCardTemplateCreateModel;
import build.dream.common.utils.AlipayUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Map;

@Service
public class AlipayService {
    @Transactional(rollbackFor = Exception.class)
    public ApiRest marketingCardTemplateCreate(MarketingCardTemplateCreateModel marketingCardTemplateCreateModel) {
        BigInteger tenantId = marketingCardTemplateCreateModel.obtainTenantId();
        BigInteger branchId = marketingCardTemplateCreateModel.obtainBranchId();

        AlipayMarketingCardTemplateCreateModel alipayMarketingCardTemplateCreateModel = AlipayMarketingCardTemplateCreateModel.builder()
                .tenantId(tenantId.toString())
                .branchId(branchId.toString())
                .build();

        Map<String, Object> resultMap = AlipayUtils.alipayMarketingCardTemplateCreate(alipayMarketingCardTemplateCreateModel);
        String templateId = MapUtils.getString(resultMap, "template_id");
        return ApiRest.builder().build();
    }
}
