package build.dream.catering.models.eleme;

import build.dream.common.models.CateringBasicModel;
import build.dream.common.utils.ApplicationHandler;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class CancelOrderLiteModel extends CateringBasicModel {
    private static final String[] TYPES = {"others", "fakeOrder", "contactUserFailed", "foodSoldOut", "restaurantClosed", "distanceTooFar", "restaurantTooBusy", "forceRejectOrder", "deliveryFault", "notSatisfiedDeliveryRequirement"};
    @NotNull
    private BigInteger elemeOrderId;

    private String type;

    private String remark;

    public BigInteger getElemeOrderId() {
        return elemeOrderId;
    }

    public void setElemeOrderId(BigInteger elemeOrderId) {
        this.elemeOrderId = elemeOrderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(TYPES, type, "type");
    }
}
