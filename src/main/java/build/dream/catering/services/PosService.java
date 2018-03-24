package build.dream.catering.services;

import build.dream.catering.constants.Constants;
import build.dream.catering.mappers.PosMapper;
import build.dream.catering.models.pos.OfflinePosModel;
import build.dream.catering.models.pos.OnlinePosModel;
import build.dream.common.api.ApiRest;
import build.dream.common.erp.catering.domains.Pos;
import build.dream.common.utils.SearchModel;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class PosService {
    @Autowired
    private PosMapper posMapper;

    /**
     * 上线POS
     *
     * @param onlinePosModel
     * @return
     */
    public ApiRest initPos(OnlinePosModel onlinePosModel) {
        BigInteger tenantId = onlinePosModel.getTenantId();
        BigInteger branchId = onlinePosModel.getBranchId();
        BigInteger userId = onlinePosModel.getUserId();
        String registrationId = onlinePosModel.getRegistrationId();
        String type = onlinePosModel.getType();
        String version = onlinePosModel.getVersion();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, tenantId);
        searchModel.addSearchCondition("branch_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, branchId);
        searchModel.addSearchCondition("user_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, userId);
        Pos pos = posMapper.find(searchModel);
        if (pos == null) {
            pos = new Pos();
            pos.setTenantId(tenantId);
            pos.setTenantCode(onlinePosModel.getTenantCode());
            pos.setBranchId(branchId);
            pos.setBranchCode(onlinePosModel.getBranchCode());
            pos.setUserId(userId);
            pos.setRegistrationId(registrationId);
            pos.setType(type);
            pos.setVersion(version);
            pos.setOnline(true);
            pos.setCreateUserId(userId);
            pos.setLastUpdateUserId(userId);
            pos.setLastUpdateRemark("POS不存在，新增POS并且设置为在线状态！");
            posMapper.insert(pos);
        } else {
            pos.setUserId(userId);
            pos.setRegistrationId(registrationId);
            pos.setType(type);
            pos.setVersion(version);
            pos.setOnline(true);
            pos.setLastUpdateUserId(userId);
            pos.setLastUpdateRemark("POS存在，设置为在线状态！");
            posMapper.update(pos);
        }
        return new ApiRest(pos, "上线POS成功！");
    }

    /**
     * 下线POS
     *
     * @param offlinePosModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest offlinePos(OfflinePosModel offlinePosModel) {
        BigInteger tenantId = offlinePosModel.getTenantId();
        BigInteger branchId = offlinePosModel.getBranchId();
        BigInteger userId = offlinePosModel.getUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, tenantId);
        searchModel.addSearchCondition("branch_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, branchId);
        searchModel.addSearchCondition("user_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, userId);

        Pos pos = posMapper.find(searchModel);
        Validate.notNull(pos, "POS不存在！");

        pos.setOnline(false);
        posMapper.update(pos);

        return new ApiRest(pos, "下线POS成功！");
    }
}
