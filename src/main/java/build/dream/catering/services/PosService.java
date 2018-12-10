package build.dream.catering.services;

import build.dream.catering.constants.Constants;
import build.dream.catering.models.pos.OfflinePosModel;
import build.dream.catering.models.pos.OnlinePosModel;
import build.dream.common.api.ApiRest;
import build.dream.common.catering.domains.Pos;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.ValidateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class PosService {
    /**
     * 上线POS
     *
     * @param onlinePosModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest onlinePos(OnlinePosModel onlinePosModel) {
        BigInteger tenantId = onlinePosModel.obtainTenantId();
        String tenantCode = onlinePosModel.obtainTenantCode();
        BigInteger branchId = onlinePosModel.obtainBranchId();
        String branchCode = onlinePosModel.obtainBranchCode();
        BigInteger userId = onlinePosModel.obtainUserId();
        String deviceId = onlinePosModel.getDeviceId();
        String type = onlinePosModel.getType();
        String version = onlinePosModel.getVersion();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        searchModel.addSearchCondition("branch_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
        searchModel.addSearchCondition("user_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, userId);
        Pos pos = DatabaseHelper.find(Pos.class, searchModel);
        if (pos == null) {
            pos = new Pos();
            pos.setTenantId(tenantId);
            pos.setTenantCode(tenantCode);
            pos.setBranchId(branchId);
            pos.setBranchCode(branchCode);
            pos.setUserId(userId);
            pos.setDeviceId(deviceId);
            pos.setType(type);
            pos.setVersion(version);
            pos.setOnline(true);
            pos.setCreatedUserId(userId);
            pos.setUpdatedUserId(userId);
            pos.setUpdatedRemark("POS不存在，新增POS并且设置为在线状态！");
            DatabaseHelper.insert(pos);
        } else {
            pos.setUserId(userId);
            pos.setDeviceId(deviceId);
            pos.setType(type);
            pos.setVersion(version);
            pos.setOnline(true);
            pos.setUpdatedUserId(userId);
            pos.setUpdatedRemark("POS存在，设置为在线状态！");
            DatabaseHelper.update(pos);
        }
        return ApiRest.builder().data(pos).message("上线POS成功！").successful(true).build();
    }

    /**
     * 下线POS
     *
     * @param offlinePosModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest offlinePos(OfflinePosModel offlinePosModel) {
        BigInteger tenantId = offlinePosModel.obtainTenantId();
        BigInteger branchId = offlinePosModel.obtainBranchId();
        BigInteger userId = offlinePosModel.obtainUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(Pos.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        searchModel.addSearchCondition(Pos.ColumnName.BRANCH_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
        searchModel.addSearchCondition(Pos.ColumnName.USER_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, userId);

        Pos pos = DatabaseHelper.find(Pos.class, searchModel);
        ValidateUtils.notNull(pos, "POS不存在！");

        pos.setDeviceId(Constants.VARCHAR_DEFAULT_VALUE);
        pos.setOnline(false);
        pos.setUpdatedRemark("下线POS");
        DatabaseHelper.update(pos);

        return ApiRest.builder().data(pos).message("下线POS成功！").build();
    }
}
