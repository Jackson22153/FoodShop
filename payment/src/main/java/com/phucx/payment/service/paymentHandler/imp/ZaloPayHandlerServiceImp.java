package com.phucx.payment.service.paymentHandler.imp;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.payment.constant.PaymentConstant;
import com.phucx.payment.constant.PaymentStatusConstant;
import com.phucx.payment.exception.PaymentNotFoundException;
import com.phucx.payment.model.PaymentDTO;
import com.phucx.payment.service.currency.CurrencyService;
import com.phucx.payment.service.payment.PaymentManagementService;
import com.phucx.payment.service.paymentHandler.ZaloPayHandlerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ZaloPayHandlerServiceImp implements ZaloPayHandlerService {
    @Autowired
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;
    @Value("${zalopay.key1}")
    private String key1;
    @Value("${zalopay.key2}")
    private String key2;
    @Value("${zalopay.paygate}")
    private String paygate;
    @Autowired
    private PaymentManagementService paymentManagementService;
    @Autowired
    private CurrencyService currencyService;

    public ZaloPayHandlerServiceImp(){
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String createPayment(PaymentDTO paymentDTO) {
        log.info("createPayment(paymentDTO={})", paymentDTO);

        String redirectUrl = paymentDTO.getBaseUrl() + PaymentConstant.ZALOPAY_SUCCESSFUL_URL + "?orderId=" + paymentDTO.getOrderID();
        try {
            Map<String, String> config = new HashMap<String, String>(){{
                put("app_id", "2553");
            }};

            Random rand = new Random();
            int random_id = rand.nextInt(1000000);
            final Map embed_data = new HashMap(){{
                put("redirecturl", redirectUrl);
            }};

            final Map[] item = {
                // new HashMap(){{}}
            };

            String value = currencyService.exchangeRate(paymentDTO.getAmount(), PaymentConstant.CURRENCY_VND);
            String amount = String.valueOf(value);

            Map<String, Object> params = new HashMap<String, Object>(){{
                put("app_id", config.get("app_id"));
                put("app_trans_id", getCurrentTimeString("yyMMdd") +"_"+ random_id);
                put("app_time", System.currentTimeMillis()); // miliseconds
                put("app_user", "user123");
                put("amount", amount);
                put("description", "Lazada - Payment for the order #"+random_id);
                put("bank_code", "");
                put("item", objectMapper.writeValueAsString(item));
                put("embed_data", objectMapper.writeValueAsString(embed_data));
                put("callback_url", paymentDTO.getBaseUrl() + PaymentConstant.ZALOPAY_CALLBACK_URL);
            }};
            
            // app_id +”|”+ app_trans_id +”|”+ appuser +”|”+ amount +"|" + app_time +”|”+ embed_data +"|" +item
            String data = params.get("app_id") +"|"+ params.get("app_trans_id") +"|"+ params.get("app_user") +"|"+ params.get("amount")
                    +"|"+ params.get("app_time") +"|"+ params.get("embed_data") +"|"+ params.get("item");
            String mac = this.hmacSha256(data, key1);
            params.put("mac", mac);

            MultiValueMap<String, String> bodyparams = new LinkedMultiValueMap<>();
            for(var entry: params.entrySet()){
                bodyparams.add(entry.getKey(), entry.getValue().toString());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyparams, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(paygate, entity, Map.class);
            Map<String, String> result = response.getBody();
            String payUrl = result.get("order_url");

            this.savePayment(paymentDTO);

            return payUrl;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        
        return null;
    }

    @Override
    public Boolean paymentSuccessfully(String orderID) {
        log.info("paymentSuccessfully(orderID={})", orderID);
        try {
            return paymentManagementService.updatePaymentStatusByOrderID(orderID, PaymentStatusConstant.SUCCESSFUL);
        } catch (PaymentNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        }
    }

    public String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    // hash 
    private String hmacSha256(String message, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hashBytes = mac.doFinal(message.getBytes("UTF-8"));
        return bytesToHex(hashBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public String callback(String jsonStr) {
        log.info("callback(jsonStr={})", jsonStr);
        Map<String, Object> result = new HashMap<>();
        try {
          TypeReference<Map<String, Object>> reference = new TypeReference<Map<String,Object>>() {};
          Map<String, Object> cbdata = objectMapper.readValue(jsonStr, reference);
          String dataStr = cbdata.get("data").toString();
          String reqMac = cbdata.get("mac").toString();

          String mac = this.hmacSha256(dataStr, key2);

          // kiểm tra callback hợp lệ (đến từ ZaloPay server)
          if (!reqMac.equals(mac)) {
              // callback không hợp lệ
              result.put("return_code", -1);
              result.put("return_message", "mac not equal");
          } else {
              // thanh toán thành công
              // merchant cập nhật trạng thái cho đơn hàng
            Map<String, Object> data = objectMapper.readValue(dataStr, reference);
              log.info("update order's status = success where app_trans_id = " + data.get("app_trans_id").toString());

              result.put("return_code", 1);
              result.put("return_message", "success");
          }
        } catch (Exception ex) {
            log.info("Erro: {}", ex.getMessage());
            //   result.put("return_code", 0); // ZaloPay server sẽ callback lại (tối đa 3 lần)
            //   result.put("return_message", ex.getMessage());
        }

        return result.toString();

    }

    // save payment to database
    private void savePayment(PaymentDTO paymentDTO){
        log.info("savePayment(payment={})", paymentDTO);
        LocalDateTime createdTime = LocalDateTime.now();
        // payment method
        String method = paymentDTO.getMethod();
        String state = PaymentStatusConstant.CREATED.name().toLowerCase();
        String paymentID = UUID.randomUUID().toString();
        // save payment
        Boolean result = paymentManagementService.savePayment(
            paymentID, createdTime, paymentDTO.getAmount(), state, 
            paymentDTO.getCustomerID(), paymentDTO.getOrderID(), method);
        if(!result){
            log.error("Error while saving payment: {}", paymentDTO);
        }
    }
    
}
