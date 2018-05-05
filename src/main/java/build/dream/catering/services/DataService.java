package build.dream.catering.services;

import build.dream.catering.constants.Constants;
import build.dream.catering.mappers.*;
import build.dream.catering.utils.DingtalkUtils;
import build.dream.common.erp.catering.domains.*;
import build.dream.common.utils.CacheUtils;
import build.dream.common.utils.GsonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DataService {
    @Autowired
    private DietOrderMapper dietOrderMapper;
    @Autowired
    private DietOrderGroupMapper dietOrderGroupMapper;
    @Autowired
    private DietOrderDetailMapper dietOrderDetailMapper;
    @Autowired
    private DietOrderDetailGoodsFlavorMapper dietOrderDetailGoodsFlavorMapper;
    @Autowired
    private DietOrderActivityMapper dietOrderActivityMapper;
    @Autowired
    private DietOrderPaymentMapper dietOrderPaymentMapper;
    @Autowired
    private DataHandleHistoryMapper dataHandleHistoryMapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveDietOrder(String dietOrderData) {
        String signature = null;
        try {
            DigestUtils.md5Hex(dietOrderData);
            if (CacheUtils.exists(signature)) {
                return;
            }

            CacheUtils.setex(signature, signature, 30, TimeUnit.DAYS);
            DataHandleHistory dataHandleHistory = new DataHandleHistory();
            dataHandleHistory.setSignature(signature);
            dataHandleHistory.setDataType(Constants.DATA_TYPE_DIET_ORDER);
            dataHandleHistory.setDataContent(dietOrderData);
            dataHandleHistory.setHandleTime(new Date());
            dataHandleHistoryMapper.insert(dataHandleHistory);

            JSONObject dietOrderJsonObject = JSONObject.fromObject(dietOrderData);
            JSONArray dietOrderGroupJsonArray = dietOrderJsonObject.getJSONArray("dietOrderGroups");
            dietOrderJsonObject.remove("dietOrderGroups");

            DietOrder dietOrder = GsonUtils.fromJson(dietOrderJsonObject, DietOrder.class);
            dietOrderMapper.insert(dietOrder);

            BigInteger dietOrderId = dietOrder.getId();
            int size = dietOrderGroupJsonArray.size();
            for (int index = 0; index < size; index++) {
                JSONObject dietOrderGroupJsonObject = dietOrderGroupJsonArray.getJSONObject(index);
                JSONArray dietOrderDetailJsonArray = dietOrderGroupJsonObject.getJSONArray("dietOrderDetails");
                dietOrderGroupJsonObject.remove("dietOrderDetails");
                DietOrderGroup dietOrderGroup = GsonUtils.fromJson(dietOrderGroupJsonObject, DietOrderGroup.class);
                dietOrderGroup.setDietOrderId(dietOrderId);
                dietOrderGroupMapper.insert(dietOrderGroup);

                int dietOrderDetailJsonArraySize = dietOrderDetailJsonArray.size();
                for (int dietOrderDetailJsonArrayIndex = 0; dietOrderDetailJsonArrayIndex < dietOrderDetailJsonArraySize; dietOrderDetailJsonArrayIndex++) {
                    JSONObject dietOrderDetailJsonObject = dietOrderDetailJsonArray.getJSONObject(dietOrderDetailJsonArrayIndex);

                    JSONArray dietOrderDetailGoodsFlavorJsonArray = null;
                    if (dietOrderDetailJsonObject.containsKey("dietOrderDetailGoodsFlavors")) {
                        dietOrderDetailGoodsFlavorJsonArray = dietOrderDetailJsonObject.getJSONArray("dietOrderDetailGoodsFlavors");
                        dietOrderDetailJsonObject.remove("dietOrderDetailGoodsFlavors");
                    }

                    DietOrderDetail dietOrderDetail = GsonUtils.fromJson(dietOrderDetailJsonObject, DietOrderDetail.class);
                    dietOrderDetail.setDietOrderId(dietOrderId);
                    dietOrderDetail.setDietOrderGroupId(dietOrderGroup.getId());
                    dietOrderDetailMapper.insert(dietOrderDetail);

                    int dietOrderDetailGoodsFlavorJsonArraySize = dietOrderDetailGoodsFlavorJsonArray.size();
                    for (int dietOrderDetailGoodsFlavorJsonArrayIndex = 0; dietOrderDetailGoodsFlavorJsonArrayIndex < dietOrderDetailGoodsFlavorJsonArraySize; dietOrderDetailGoodsFlavorJsonArrayIndex++) {
                        JSONObject dietOrderDetailGoodsFlavorJsonObject = dietOrderDetailGoodsFlavorJsonArray.getJSONObject(dietOrderDetailGoodsFlavorJsonArrayIndex);
                        DietOrderDetailGoodsFlavor dietOrderDetailGoodsFlavor = GsonUtils.fromJson(dietOrderDetailGoodsFlavorJsonObject, DietOrderDetailGoodsFlavor.class);
                        dietOrderDetailGoodsFlavor.setDietOrderId(dietOrder.getId());
                        dietOrderDetailGoodsFlavor.setDietOrderGroupId(dietOrderGroup.getId());
                        dietOrderDetailGoodsFlavor.setDietOrderDetailId(dietOrderDetail.getId());
                        dietOrderDetailGoodsFlavorMapper.insert(dietOrderDetailGoodsFlavor);
                    }
                }
            }

            if (dietOrderJsonObject.containsKey("dietOrderActivities")) {
                JSONArray dietOrderActivityJsonArray = dietOrderJsonObject.getJSONArray("dietOrderActivities");
                int dietOrderActivityJsonArraySize = dietOrderActivityJsonArray.size();
                List<DietOrderActivity> dietOrderActivities = new ArrayList<DietOrderActivity>();
                for (int dietOrderActivityJsonArrayIndex = 0; dietOrderActivityJsonArrayIndex < dietOrderActivityJsonArraySize; dietOrderActivityJsonArrayIndex++) {
                    DietOrderActivity dietOrderActivity = GsonUtils.fromJson(dietOrderActivityJsonArray.getJSONObject(dietOrderActivityJsonArrayIndex), DietOrderActivity.class);
                    dietOrderActivity.setDietOrderId(dietOrderId);
                    dietOrderActivities.add(dietOrderActivity);
                }
                dietOrderActivityMapper.insertAll(dietOrderActivities);
            }

            JSONArray dietOrderPaymentJsonArray = dietOrderJsonObject.getJSONArray("dietOrderPayments");
            int dietOrderPaymentJsonArraySize = dietOrderPaymentJsonArray.size();
            List<DietOrderPayment> dietOrderPayments = new ArrayList<DietOrderPayment>();
            for (int dietOrderPaymentJsonArrayIndex = 0; dietOrderPaymentJsonArrayIndex < dietOrderPaymentJsonArraySize; dietOrderPaymentJsonArrayIndex++) {
                DietOrderPayment dietOrderPayment = GsonUtils.fromJson(dietOrderPaymentJsonArray.getJSONObject(dietOrderPaymentJsonArrayIndex), DietOrderPayment.class);
                dietOrderPayment.setDietOrderId(dietOrderId);
                dietOrderPayments.add(dietOrderPayment);
            }
            dietOrderPaymentMapper.insertAll(dietOrderPayments);
        } catch (Exception e) {
            if (StringUtils.isNotBlank(signature)) {
                CacheUtils.delete(signature);
            }
            DingtalkUtils.send(String.format(Constants.DINGTALK_ERROR_MESSAGE_FORMAT, "保存POS上传的订单数据失败", GsonUtils.toJson(dietOrderData), e.getClass().getName(), e.getMessage()));
        }
    }
}
