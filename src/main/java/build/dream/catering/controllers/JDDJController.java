package build.dream.catering.controllers;

import build.dream.catering.constants.Constants;
import build.dream.catering.models.jddj.*;
import build.dream.catering.services.JDDJService;
import build.dream.common.annotations.ApiRestAction;
import build.dream.common.annotations.PermitAll;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/jddj")
public class JDDJController {
    @PermitAll
    @RequestMapping(value = "/test")
    @ResponseBody
    public String test() {
        return Constants.SUCCESS;
    }

    /**
     * 确认订单
     *
     * @return
     */
    @RequestMapping(value = "/confirmOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ConfirmOrderModel.class, serviceClass = JDDJService.class, serviceMethodName = "confirmOrder", error = "确认订单失败")
    public String confirmOrder() {
        return null;
    }

    /**
     * 取消订单
     *
     * @return
     */
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = CancelOrderModel.class, serviceClass = JDDJService.class, serviceMethodName = "cancelOrder", error = "取消订单失败")
    public String cancelOrder() {
        return null;
    }

    /**
     * 订单取消且退款接口
     * 1、商家自送订单在配送流程中，若用户拒收，商家可调用接口进行取消；
     * 2、非商家自送订单，调用接口取消失败，仅可用户进行取消；
     * 3、达达配送转商家自送的订单，若用户拒收，商家可调用接口进行取消；
     *
     * @return
     */
    @RequestMapping(value = "/cancelAndRefund", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = CancelAndRefundModel.class, serviceClass = JDDJService.class, serviceMethodName = "cancelAndRefund", error = "订单取消且退款失败")
    public String cancelAndRefund() {
        return null;
    }

    /**
     * 订单已打印接口
     *
     * @return
     */
    @RequestMapping(value = "/printOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = PrintOrderModel.class, serviceClass = JDDJService.class, serviceMethodName = "printOrder", error = "订单已打印失败")
    public String printOrder() {
        return null;
    }

    /**
     * 同意取消订单
     *
     * @return
     */
    @RequestMapping(value = "/agreeCancelOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = AgreeCancelOrderModel.class, serviceClass = JDDJService.class, serviceMethodName = "agreeCancelOrder", error = "同意取消订单失败")
    public String agreeCancelOrder() {
        return null;
    }

    /**
     * 不同意取消订单
     *
     * @return
     */
    @RequestMapping(value = "/disagreeCancelOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = DisagreeCancelOrderModel.class, serviceClass = JDDJService.class, serviceMethodName = "disagreeCancelOrder", error = "不同意取消订单失败")
    public String disagreeCancelOrder() {
        return null;
    }

    /**
     * 订单调整
     *
     * @return
     */
    @RequestMapping(value = "/adjustOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = AdjustOrderModel.class, serviceClass = JDDJService.class, serviceMethodName = "adjustOrder", error = "订单调整失败")
    public String adjustOrder() {
        return null;
    }

    /**
     * 拣货完成且众包配送
     *
     * @return
     */
    @RequestMapping(value = "/orderJDZBDelivery", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = OrderJDZBDeliveryModel.class, serviceClass = JDDJService.class, serviceMethodName = "orderJDZBDelivery", error = "拣货完成且众包配送失败")
    public String orderJDZBDelivery() {
        return null;
    }

    /**
     * 拣货完成且达达同城配送
     *
     * @return
     */
    @RequestMapping(value = "/orderDDTCDelivery", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = OrderDDTCDeliveryModel.class, serviceClass = JDDJService.class, serviceMethodName = "orderDDTCDelivery", error = "拣货完成且达达同城配送失败")
    public String orderDDTCDelivery() {
        return null;
    }
}