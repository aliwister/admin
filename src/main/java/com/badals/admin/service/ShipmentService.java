package com.badals.admin.service;

import com.badals.admin.domain.*;
import com.badals.admin.domain.enumeration.OrderState;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.pojo.DetrackDelivery;
import com.badals.admin.domain.pojo.DetrackItem;
import com.badals.admin.domain.pojo.DetrackSearch;
import com.badals.admin.domain.pojo.PaymentPojo;
import com.badals.admin.domain.projection.*;
import com.badals.admin.repository.*;
import com.badals.admin.repository.search.ShipmentSearchRepository;
import com.badals.admin.service.dto.*;
import com.badals.admin.service.errors.ShipmentNotReadyException;
import com.badals.admin.service.mapper.ItemIssuanceMapper;
import com.badals.admin.service.mapper.OrderMapper;
import com.badals.admin.service.mapper.ShipmentMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//import com.badals.admin.repository.search.ShipmentSearchRepository;

/**
 * Service Implementation for managing {@link Shipment}.
 */
@Service
@Transactional
public class ShipmentService {

    private final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentRepository shipmentRepository;
    private final ShipmentSearchRepository shipmentSearchRepository;
    private final ShipmentDocRepository shipmentDocRepository;

    private final ShipmentMapper shipmentMapper;

    private final PurchaseItemRepository purchaseItemRepository;

    private final PkgRepository pkgRepository;
    private final ShipmentItemRepository shipmentItemRepository;

    private final PurchaseShipmentRepository purchaseShipmentRepository;
    private final PackagingContentRepository packagingContentRepository;
    private final ShipmentReceiptRepository shipmentReceiptRepository;
    private final OrderItemRepository orderItemRepository;

    private final OrderShipmentRepository orderShipmentRepository;
    private final ItemIssuanceRepository itemIssuanceRepository;
    private final ItemIssuanceMapper itemIssuanceMapper;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    //private final ShiSmentSearchRepository shipmentSearchRepository;

    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentSearchRepository shipmentSearchRepository, ShipmentDocRepository shipmentDocRepository, ShipmentMapper shipmentMapper,/*, ShipmentSearchRepository shipmentSearchRepository*/PurchaseItemRepository purchaseItemRepository, PkgRepository pkgRepository, ShipmentItemRepository shipmentItemRepository, PurchaseShipmentRepository purchaseShipmentRepository, PackagingContentRepository packagingContentRepository, ShipmentReceiptRepository shipmentReceiptRepository, OrderItemRepository orderItemRepository, OrderShipmentRepository orderShipmentRepository, ItemIssuanceRepository itemIssuanceRepository, ItemIssuanceMapper itemIssuanceMapper, OrderRepository orderRepository, OrderService orderService) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentSearchRepository = shipmentSearchRepository;
        this.shipmentDocRepository = shipmentDocRepository;
        this.shipmentMapper = shipmentMapper;
        //this.shipmentSearchRepository = shipmentSearchRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.pkgRepository = pkgRepository;
        this.shipmentItemRepository = shipmentItemRepository;
        this.purchaseShipmentRepository = purchaseShipmentRepository;
        this.packagingContentRepository = packagingContentRepository;
        this.shipmentReceiptRepository = shipmentReceiptRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderShipmentRepository = orderShipmentRepository;
        this.itemIssuanceRepository = itemIssuanceRepository;
        this.itemIssuanceMapper = itemIssuanceMapper;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    /**
     * Save a shipment.
     *
     * @param shipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentDTO save(ShipmentDTO shipmentDTO) {
        log.debug("Request to save Shipment : {}", shipmentDTO);
        Shipment shipment = shipmentMapper.toEntity(shipmentDTO);
        shipment = shipmentRepository.save(shipment);
        ShipmentDTO result = shipmentMapper.toDto(shipment);
        shipmentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the shipments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShipmentDTO> findAll() {
        log.debug("Request to get all Shipments");
        return shipmentRepository.findAll().stream()
            .map(shipmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentDTO> findOne(Long id) {
        log.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findById(id)
            .map(shipmentMapper::toDto);
    }
    @Transactional(readOnly = true)
    public Optional<ShipmentDTO> findOneForDetails(Long id) {
        log.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findById(id)
            .map(shipmentMapper::toDtoDetails);
    }

    /**
     * Delete the shipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Shipment : {}", id);
        shipmentRepository.deleteById(id);
        //shipmentSearchRepository.deleteById(id);
    }

    public void acceptItem(Long shipmentItemId, Long packageId, BigDecimal accepted, BigDecimal rejected) throws ShipmentNotReadyException {
        ShipmentItem shipmentItem = shipmentItemRepository.findById(shipmentItemId).get();
        Shipment shipment = shipmentItem.getShipment();
        if(shipment.getShipmentStatus() != ShipmentStatus.ACCEPTED)
            throw new ShipmentNotReadyException("Must accept shipment first");

        Pkg pkg = prepItem(shipmentItemId, packageId, accepted.add(rejected));

        ShipmentReceipt shipmentReceipt = new ShipmentReceipt().accepted(accepted).receivedDate(Instant.now()).pkg(pkg).rejected(rejected).shipmentItem(shipmentItem);
        shipmentReceipt.setProductId(shipmentItem.getProductId());
        shipmentReceiptRepository.save(shipmentReceipt);

    }

    public void manualAccept(Long packageId, String sku, BigDecimal accepted, BigDecimal rejected) {
        Pkg pkg = pkgRepository.findById(packageId).get();
        ShipmentReceipt shipmentReceipt = new ShipmentReceipt().accepted(accepted).receivedDate(Instant.now()).pkg(pkg).rejected(rejected).sku(sku);
        shipmentReceiptRepository.save(shipmentReceipt);
    }

    public void acceptItemOld(Long shipmentId, Long pkgId, Long purchaseItemId, Long productId, Long merchantId, String description, BigDecimal quantity, BigDecimal accepted, BigDecimal rejected) {
        Shipment shipment = shipmentRepository.getOne(shipmentId);
        PurchaseItem purchaseItem = purchaseItemRepository.getOne(purchaseItemId);
        Pkg pkg = pkgRepository.getOne(pkgId);
        quantity = accepted.add(rejected);

        // Shipment Item
        ShipmentItem shipmentItem = new ShipmentItem().shipment(shipment).sequence(shipment.getShipmentItems().size()+1).description(description).quantity(quantity);
        shipmentItem.setProductId(productId);

        // Purchase Shipment
        PurchaseShipment purchaseShipment = new PurchaseShipment().quantity(quantity).shipmentItem(shipmentItem).purchaseItem(purchaseItem);

        // Packaging Content
        PackagingContent packagingContent = new PackagingContent().quantity(quantity).pkg(pkg).shipmentItem(shipmentItem);

        // Shipment Receipt
        ShipmentReceipt shipmentReceipt = new ShipmentReceipt().accepted(accepted).receivedDate(Instant.now()).pkg(pkg).rejected(rejected).shipmentItem(shipmentItem);
        shipmentReceipt.setProductId(productId);

        shipmentItemRepository.save(shipmentItem);
        purchaseShipmentRepository.save(purchaseShipment);
        packagingContentRepository.save(packagingContent);
        shipmentReceiptRepository.save(shipmentReceipt);
    }

    public ItemIssuanceDTO issueItem(Long orderItemId, Long productId, String description, BigDecimal quantity) throws Exception {
        // Is filled?
        if(orderItemRepository.checkFilled(orderItemId, quantity) > 0)
            throw new Exception("This item is already filled");


        // Find customer's pending shipment
        OrderItem orderItem = orderItemRepository.findForSorting(orderItemId);

        Customer customer = orderItem.getOrder().getCustomer();
        Long shipmentId = shipmentRepository.findCustomerShipmentIdNative(orderItem.getOrder().getId());
        Shipment shipment = null;
        if (shipmentId != null)
            shipment = shipmentRepository.findById(shipmentId).orElse(null);

        if(shipment == null) {
            shipment = new Shipment();
            //shipment = shipmentRepository.findById(shipmentId).orElse(new Shipment());
            shipment.setShipmentStatus(ShipmentStatus.ARRIVED);
            shipment.setCustomer(customer);
            shipment.setReference(orderItem.getOrder().getReference());
            shipment.setShipmentType(ShipmentType.CUSTOMER);
            shipment.addShipmentTracking(new ShipmentTracking().shipment(shipment).shipmentEventId(3001).eventDate(LocalDateTime.now()));

        }

        ShipmentItem shipmentItem = new ShipmentItem().shipment(shipment).sequence(shipment.getShipmentItems().size()+1).description(description).quantity(quantity);
        shipmentItem.setProductId(productId);

        // Order Shipment
        OrderShipment orderShipment = new OrderShipment().quantity(quantity).shipmentItem(shipmentItem);
        orderShipment.setOrderItemId(orderItemId);

        ItemIssuance issuance = new ItemIssuance().quantity(quantity).shipmentItem(shipmentItem).productId(productId);
        String details = (shipmentItem.getDescription()!= null && shipmentItem.getDescription().length() > 50)?shipmentItem.getDescription().substring(0, 50):shipmentItem.getDescription();
        shipment.addShipmentTracking(new ShipmentTracking().shipment(shipment).shipmentEventId(3001).details(details).eventDate(LocalDateTime.now()));

        shipmentRepository.save(shipment);
        shipmentSearchRepository.save(shipmentMapper.toDto(shipment));
        shipmentItemRepository.save(shipmentItem);
        orderShipmentRepository.save(orderShipment);
        issuance = itemIssuanceRepository.save(issuance);
        return itemIssuanceMapper.toDto(issuance);
    }

    public List<ShipmentDTO> findForShipmentList(List<ShipmentStatus> status, ShipmentType type) {
        List<Shipment> shipments = shipmentRepository.findForShipmentList(status, type);
        return shipments.stream().map(shipmentMapper::toDtoList).collect(Collectors.toList());
    }

    public List<SortQueue> findForSorting(String keyword) {
        return shipmentRepository.findForSorting(keyword);
    }

    public Pkg prepItem(Long shipmentItemId, Long packageId, BigDecimal quantity) {
        ShipmentItem item = shipmentItemRepository.findById(shipmentItemId).get();
        Pkg pkg = pkgRepository.findById(packageId).get();
        PackagingContent packagingContent = new PackagingContent().shipmentItem(item).pkg(pkg).quantity(quantity);
        packagingContentRepository.save(packagingContent);
        return pkg;
    }

    @Autowired
    OrderMapper orderMapper;

    public Optional<OrderDTO> getOrderForDetrack(String orderRef) {
        return orderRepository.findByReference(orderRef).map(orderMapper::toDto);
    }

    public String sendToDetrack(Long shipmentId, String orderRef, String name, String instructions, String date, String time, String assignTo) throws JsonProcessingException, ShipmentNotReadyException {

        Shipment shipment = shipmentRepository.findById(shipmentId).get();

        if(shipment.getPkgs() == null || shipment.getPkgs().size() == 0)
            throw new ShipmentNotReadyException("Shipment not ready. Must prepare shipment");

        Double d = shipment.getShipmentItems().stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum();
        Double d2 = shipment.getPkgs().stream().mapToDouble(x -> x.getPackagingContent().stream().mapToDouble(y -> y.getQuantity().doubleValue()).sum()).sum();

        System.out.println("shipment total = "+d);
        System.out.println("packages total = "+d2);

        if(Math.abs(d-d2) > .00001)
            throw new ShipmentNotReadyException("Shipment not ready. Must prepare shipment");

        OrderDTO order = getOrderForDetrack(orderRef).get();
        AddressDTO address = order.getDeliveryAddress();
        CustomerDTO customer = order.getCustomer();
        BigDecimal balance = orderRepository.getBalance(orderRef);



        // TODO Auto-generated method stub
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println(orderId);

        //String url = "http://buyth.at/-make";
        String url = "https://app.detrack.com/api/v1/deliveries/create.json";

        DetrackDelivery delivery = new DetrackDelivery();
        ArrayList<DetrackItem> items = new ArrayList<DetrackItem>();

        delivery.setDate(date);

        String mobile = address.getMobile();
        if(mobile == null && customer != null)
            mobile = customer.getMobile();



        mobile = mobile.replaceAll("[^\\d.]", "");

        if(mobile.length() > 8 && mobile.startsWith("00")) {
            mobile = mobile.substring(2);
        }

        if(!mobile.startsWith("968") || mobile.length() < 11)
            mobile = "968"+mobile;

        delivery.setPhone (mobile);
        delivery.setInstructions (instructions);
        delivery.set_do (shipmentId +"-"+orderRef);
        delivery.setAddress(address.getLine1() + " " + address.getLine2() + " " + address.getCity());
        delivery.setDeliver_to (address.getFirstName() + " " + address.getLastName());
        delivery.setDelivery_time (time);

        //delivery.setAssign_to (assignTo); //Disable assign to


        delivery.setSales_person ("Ali");
        delivery.setNotify_url("https://api.badals.com/detrack");
        delivery.setAtt_1("https://wa.me/"+mobile);
        delivery.setPay_amt(balance.setScale(2, RoundingMode.HALF_UP).toString());
        //if(balance != null)
         //   delivery.pay_amt = balance.toString();

        if (shipment.getShipmentItems() != null) {
            for(ShipmentItem product: shipment.getShipmentItems()) {
                DetrackItem item = new DetrackItem();
                item.setSku(product.getProductId());
                item.setDesc(product.getDescription());
                item.setQty(product.getQuantity());
                items.add(item);
            }
            delivery.setItems(items);
        }

        String json = mapper.writeValueAsString(delivery);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("key", "530e610169c28ce227a9716ba28a386cedc6d2dbefef67eb");
        log.info(json);
        params.add("json", "["+json+"]");
        params.add("url", url);
        ResponseEntity<String> response = (ResponseEntity<String>) restTemplate.postForEntity(url, params, String.class);

        shipment.setShipmentStatus(ShipmentStatus.SCHEDULED);
        shipment.setTrackingNum(shipmentId +"-"+orderRef);

        shipment.addShipmentTracking(new ShipmentTracking().shipment(shipment).shipmentEventId(3002).eventDate(LocalDateTime.now()));
        shipmentRepository.save(shipment);

        return response.getBody();
    }



    public void setStatus(Long shipmentId, ShipmentStatus status) {
        Shipment shipment = shipmentRepository.findById(shipmentId).get();
        shipment.setShipmentStatus(status);
        shipmentRepository.save(shipment);
    }

    public void updateOrder(String reference, OrderState delivered) {
    }

    public List<Inventory> getInventory() {
        return shipmentRepository.getInventory();
    }

    public List<OutstandingQueue> findOutstanding(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty())
            return this.shipmentRepository.findOutstanding(keyword);

        return shipmentRepository.findOutstanding();
    }

    public List<ShipmentDTO> findByRef(String id) {
        return shipmentRepository.findAllByReference(id).stream().map(shipmentMapper::toDto).collect(Collectors.toList());
    }

    public List<ShipQueue> getShipQueue() {
        return shipmentRepository.getShipQueue();
    }

    public List<PrepQueue> getPrepQueue(Long shipmentId, String keyword) {
        return shipmentRepository.getPrepQueue(shipmentId, keyword);
    }

    public ShipmentDTO acceptShipment(String trackingNum, PaymentPojo payment, String invoiceLink) throws Exception {
        Optional<Shipment> shipmentOptional = shipmentRepository.findByTrackingNum(trackingNum);
        if(!shipmentOptional.isPresent()) {
            throw new Exception("No shipment found");
        }
        Shipment shipment = shipmentOptional.get();
        if(shipment.getShipmentStatus() != ShipmentStatus.ACCEPTED) {
            shipment.setShipmentStatus(ShipmentStatus.ACCEPTED);
            shipment.addShipmentTracking(new ShipmentTracking().shipment(shipment).shipmentEventId(1102).eventDate(LocalDateTime.now()));
        }
        if(payment.getPrice() != null) {
            shipment.setDutiesTotal(payment.getPrice());
            shipmentRepository.addPayment(payment.getUserId(), trackingNum, shipment.getShipmentMethod()+"-"+payment.getInvoiceNum(), XeroAccount.CUSTOMS, payment.getPrice().getAmount());
        }
        if(invoiceLink != null) {
            ShipmentDoc doc = new ShipmentDoc();
            doc.setFileKey(invoiceLink);
            doc.setShipment(shipment);
            shipmentDocRepository.save(doc);
        }
        shipmentRepository.save(shipment);
        return shipmentMapper.toDto(shipment);
    }

    public String addItem(Long shipmentId, Long productId, Long purchaseItemId, String description, BigDecimal quantity) {
        // Shipment Item
        Shipment shipment = shipmentRepository.findById(shipmentId).get();
        ShipmentItem shipmentItem = new ShipmentItem().shipment(shipment).sequence(shipment.getShipmentItems().size()+1).description(description).quantity(quantity);
        shipmentItem.setProductId(productId);

        // Purchase Shipment
        PurchaseItem purchaseItem = purchaseItemRepository.getOne(purchaseItemId);
        PurchaseShipment purchaseShipment = new PurchaseShipment().quantity(quantity).shipmentItem(shipmentItem).purchaseItem(purchaseItem);

        shipmentItemRepository.save(shipmentItem);
        purchaseShipmentRepository.save(purchaseShipment);
        return "Done";
    }

    public void removeItem(Long shipmentItemId) {
        ShipmentItem shipmentItem= shipmentItemRepository.findById(shipmentItemId).get();

        packagingContentRepository.deleteByShipmentItem(shipmentItem);
        orderShipmentRepository.deleteByShipmentItem(shipmentItem);
        purchaseShipmentRepository.deleteByShipmentItem(shipmentItem);

        itemIssuanceRepository.deleteByShipmentItem(shipmentItem);
        shipmentReceiptRepository.deleteByShipmentItem(shipmentItem);

        shipmentItemRepository.delete(shipmentItem);
    }

    public void unpackItem(Long shipmentItemId) {
        ShipmentItem shipmentItem = shipmentItemRepository.findById(shipmentItemId).get();
        packagingContentRepository.deleteByShipmentItem(shipmentItem);
    }

    public byte[] downloadLabel(String _do) {
        String url = "https://app.detrack.com/api/v1/deliveries/label.pdf";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("key", "530e610169c28ce227a9716ba28a386cedc6d2dbefef67eb");

        //String date = "2017-05-02", _do = "43784-7468";
        String json = "{\"do\":\""+ _do +"\"}";

        params.add("json", json);
        params.add("url", url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.postForEntity(url, params, byte[].class);

        return response.getBody();
    }

    public void updateFromDetrack(String id) throws IOException { //String id) {
        //String id = "12852-3434295";
        Long shipmentId = Long.parseLong(id.split("-")[0]);
        String url = "https://app.detrack.com/api/v1/jobs/search.json";

        DetrackDelivery delivery = new DetrackDelivery();
        ArrayList<DetrackItem> items = new ArrayList<DetrackItem>();

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("key", "530e610169c28ce227a9716ba28a386cedc6d2dbefef67eb");

        params.add("json", "{\"q\": {\"do\": \"" + id + "\"}}");
        params.add("url", url);
        ResponseEntity<String> response = (ResponseEntity<String>) restTemplate.postForEntity(url, params, String.class);
        System.out.println(response.getBody());
        DetrackSearch search = mapper.readValue(response.getBody(), DetrackSearch.class);
        //System.out.println(search.getJobs().)
        System.out.println(search.getJobs().get(0).getStatus());

        if(search.getJobs().get(0).getStatus().equalsIgnoreCase(ShipmentStatus.DELIVERED.name())) {
            Shipment shipment = shipmentRepository.findById(shipmentId).get();
            shipment.setShipmentStatus(ShipmentStatus.DELIVERED);
            shipment.addShipmentTracking(new ShipmentTracking().shipment(shipment).shipmentEventId(4000).eventDate(LocalDateTime.now()));
            orderService.setStatus(shipment.getReference(), OrderState.DELIVERED);
            shipmentRepository.save(shipment);
        }
    }

    public List<UnshippedQueue> findUnshipped() {
        return shipmentRepository.findUnshipped();
    }

    public List<ShipQueue> getShipQueueByCustomerId(Long customerId) {
        return shipmentRepository.getShipQueueByCustomerId(customerId);
    }

     public List<ShipmentItemDetails> findItemsForShipmentDetails(Long id) {
        return shipmentRepository.findItemsForShipmentDetails(id);
    }

     public List<ShipmentItemDetails> findItemsInPkgForShipmentDetails(Long id) {
        return shipmentRepository.findItemsInPkgForShipmentDetails(id);
    }

    /**
     * Search for the shipment corresponding to the query.
     *
     * @return the list of entities.
     */
/*    @Transactional(readOnly = true)
    public List<ShipmentDTO> search(String query) {
        log.debug("Request to search Shipments for query {}", query);
        return StreamSupport
            .stream(shipmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(shipmentMapper::toDto)
            .collect(Collectors.toList());
    }*/

/*    public String sendToDetrackV2(Long shipmentId, String orderRef, String name, String instructions, String date, String time, String assignTo) throws JsonProcessingException, ShipmentNotReadyException {

        Shipment shipment = shipmentRepository.findById(shipmentId).get();

        if(shipment.getPkgs() == null || shipment.getPkgs().size() == 0)
            throw new ShipmentNotReadyException("Shipment not ready. Must prepare shipment");

        Double d = shipment.getShipmentItems().stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum();
        Double d2 = shipment.getPkgs().stream().mapToDouble(x -> x.getPackagingContent().stream().mapToDouble(y -> y.getQuantity().doubleValue()).sum()).sum();

        System.out.println("shipment total = "+d);
        System.out.println("packages total = "+d2);

        if(Math.abs(d-d2) > .00001)
            throw new ShipmentNotReadyException("Shipment not ready. Must prepare shipment");

        OrderDTO order = getOrderForDetrack(orderRef).get();
        AddressDTO address = order.getDeliveryAddress();
        CustomerDTO customer = order.getCustomer();
        //BigDecimal balance = orderRepository.getBalance(orderRef);




        //System.out.println(orderId);

        //String url = "http://buyth.at/-make";
        String url = "https://app.detrack.com/api/v2/dn/jobs";

        DetrackDeliveryV2 delivery = new DetrackDeliveryV2();
        ArrayList<DetrackItemV2> items = new ArrayList<DetrackItemV2>();

        delivery.setDate(date);

        String mobile = address.getMobile();
        if(mobile == null && customer != null)
            mobile = customer.getMobile();

        mobile = mobile.replaceAll("[^\\d.]", "");
        if(!mobile.startsWith("968") || mobile.length() < 11)
            mobile = "968"+mobile;

        delivery.setPhone_number (mobile);
        delivery.setInstructions (instructions);
        delivery.set_do (shipmentId +"-"+orderRef);
        delivery.setAddress(address.getLine1() + " " + address.getLine2() + " " + address.getCity());
        delivery.setDeliver_to_collect_from (address.getFirstName() + " " + address.getLastName());
        delivery.setDelivery_time (time);
        delivery.setAssign_to (assignTo);
        delivery.setSales_person ("Ali");
        delivery.setWebhook_url("https://webhook.site/54b0464c-6035-45f2-8c8f-43806f8620c4");
        //delivery.setNotify_url("https://api.badals.com/detrack");
        delivery.setAttachment_url("https://wa.me/"+mobile);

        LocalDateTime ldt = LocalDateTime.now();//.plusDays(1);
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        delivery.setDate(formmat1.format(ldt));
        //if(balance != null)
        //   delivery.pay_amt = balance.toString();

        if (shipment.getShipmentItems() != null) {
            for(ShipmentItem product: shipment.getShipmentItems()) {
                DetrackItemV2 item = new DetrackItemV2();
                item.setSku(product.getProductId());
                item.setDescription(product.getDescription());
                item.setQuantity(product.getQuantity().intValue());
                items.add(item);
            }
            delivery.setItems(items);
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(delivery);

        // TODO Auto-generated method stub
        Client client = ClientBuilder.newClient();
        log.info(json);
        Entity payload = Entity.json("{  \"data\":"+json+" }");
        //Entity payload = Entity.json("{  \"data\": {    \"id\": \"5b22055510c92b1a046ece04\",    \"type\": \"Delivery\",    \"primary_job_status\": \"dispatched\",    \"open_to_marketplace\": false,    \"marketplace_offer\": null,    \"do_number\": \"\",    \"attempt\": 1,    \"date\": \"2018-06-14\",    \"start_date\": \"2018-06-14\",    \"job_age\": 1,    \"job_release_time\": null,    \"job_time\": null,    \"time_window\": null,    \"job_received_date\": null,    \"tracking_number\": \"T0\",    \"order_number\": \"ORN12345678\",    \"job_type\": null,    \"job_sequence\": null,    \"job_fee\": null,    \"address_lat\": null,    \"address_lng\": null,    \"address\": \"Singapore 470140\",    \"company_name\": null,    \"address_1\": null,    \"address_2\": null,    \"address_3\": null,    \"postal_code\": \"470140\",    \"city\": null,    \"state\": null,    \"country\": null,    \"geocoded_lat\": 1.33668219002454,    \"geocoded_lng\": 103.910711702473,    \"billing_address\": null,    \"deliver_to_collect_from\": \"James Li\",    \"last_name\": null,    \"phone_number\": \"65432178\",    \"sender_phone_number\": null,    \"fax_number\": \"65432179\",    \"instructions\": null,    \"assign_to\": null,    \"notify_email\": \"test@example.com\",    \"webhook_url\": null,    \"zone\": null,    \"customer\": null,    \"account_number\": null,    \"job_owner\": null,    \"invoice_number\": null,    \"invoice_amount\": null,    \"payment_mode\": null,    \"payment_amount\": null,    \"group_name\": null,    \"vendor_name\": null,    \"shipper_name\": null,    \"source\": null,    \"weight\": null,    \"parcel_width\": null,    \"parcel_length\": null,    \"parcel_height\": null,    \"cubic_meter\": null,    \"boxes\": null,    \"cartons\": null,    \"pieces\": null,    \"envelopes\": null,    \"pallets\": null,    \"bins\": null,    \"trays\": null,    \"bundles\": null,    \"rolls\": null,    \"number_of_shipping_labels\": null,    \"attachment_url\": null,    \"detrack_number\": \"DET2000001\",    \"status\": \"dispatched\",    \"tracking_status\": \"Info received\",    \"reason\": null,    \"last_reason\": null,    \"received_by_sent_by\": null,    \"note\": null,    \"carrier\": null,    \"pod_lat\": \"\",    \"pod_lng\": \"\",    \"pod_address\": \"\",    \"address_tracked_at\": null,    \"arrived_lat\": null,    \"arrived_lng\": null,    \"arrived_address\": null,    \"arrived_at\": null,    \"texted_at\": null,    \"called_at\": null,    \"serial_number\": null,    \"signed_at\": null,    \"photo_1_at\": null,    \"photo_2_at\": null,    \"photo_3_at\": null,    \"photo_4_at\": null,    \"photo_5_at\": null,    \"signature_file_url\": null,    \"photo_1_file_url\": null,    \"photo_2_file_url\": null,    \"photo_3_file_url\": null,    \"photo_4_file_url\": null,    \"photo_5_file_url\": null,    \"actual_weight\": null,    \"temperature\": null,    \"hold_time\": null,    \"payment_collected\": null,    \"auto_reschedule\": null,    \"actual_crates\": null,    \"actual_pallets\": null,    \"actual_utilization\": null,    \"goods_service_rating\": null,    \"driver_rating\": null,    \"customer_feedback\": null,    \"eta_time\": null,    \"live_eta\": null,    \"depot\": null,    \"depot_contact\": null,    \"department\": null,    \"sales_person\": null,    \"identification_number\": null,    \"bank_prefix\": null,    \"run_number\": null,    \"pick_up_from\": null,    \"pick_up_time\": null,    \"pick_up_lat\": null,    \"pick_up_lng\": null,    \"pick_up_address\": null,    \"pick_up_address_1\": null,    \"pick_up_address_2\": null,    \"pick_up_address_3\": null,    \"pick_up_city\": null,    \"pick_up_state\": null,    \"pick_up_country\": null,    \"pick_up_postal_code\": null,    \"pick_up_zone\": null,    \"pick_up_assign_to\": null,    \"pick_up_reason\": null,    \"info_received_at\": \"2018-06-14T06:04:06.017Z\",    \"pick_up_at\": null,    \"scheduled_at\": null,    \"at_warehouse_at\": null,    \"out_for_delivery_at\": null,    \"head_to_pick_up_at\": null,    \"head_to_delivery_at\": null,    \"cancelled_at\": null,    \"pod_at\": null,    \"pick_up_failed_count\": null,    \"deliver_failed_count\": null,    \"job_price\": null,    \"insurance_price\": null,    \"insurance_coverage\": false,    \"total_price\": null,    \"payer_type\": null,    \"remarks\": null,    \"items_count\": 2,    \"service_type\": null,    \"warehouse_address\": null,    \"destination_time_window\": null,    \"door\": null,    \"time_zone\": null,    \"vehicle_type\": null,    \"created_at\": \"2018-06-14T06:04:06.017Z\",    \"items\": [      {        \"id\": \"5b22055510c92b1a046ece05\",        \"sku\": \"12345678\",        \"purchase_order_number\": null,        \"batch_number\": null,        \"expiry_date\": null,        \"description\": null,        \"comments\": null,        \"quantity\": 10,        \"unit_of_measure\": null,        \"checked\": false,        \"actual_quantity\": null,        \"inbound_quantity\": null,        \"unload_time_estimate\": null,        \"unload_time_actual\": null,        \"follow_up_quantity\": null,        \"follow_up_reason\": null,        \"rework_quantity\": null,        \"rework_reason\": null,        \"reject_quantity\": 0,        \"reject_reason\": null,        \"weight\": null,        \"serial_numbers\": [],        \"photo_url\": null      },      {        \"id\": \"5b22055510c92b1a046ece06\",        \"sku\": \"12387654\",        \"purchase_order_number\": null,        \"batch_number\": null,        \"expiry_date\": null,        \"description\": null,        \"comments\": null,        \"quantity\": 5,        \"unit_of_measure\": null,        \"checked\": false,        \"actual_quantity\": null,        \"inbound_quantity\": null,        \"unload_time_estimate\": null,        \"unload_time_actual\": null,        \"follow_up_quantity\": null,        \"follow_up_reason\": null,        \"rework_quantity\": null,        \"rework_reason\": null,        \"reject_quantity\": 0,        \"reject_reason\": null,        \"weight\": null,        \"serial_numbers\": [],        \"photo_url\": null      }    ]  }}");
        Response response = client.target("https://app.detrack.com/api/v2/dn/jobs").request(MediaType.APPLICATION_JSON_TYPE)
            .header("X-API-KEY", "530e610169c28ce227a9716ba28a386cedc6d2dbefef67eb")
            .post(payload);
        shipment.setShipmentStatus(ShipmentStatus.SCHEDULED);
        shipment.setTrackingNum(shipmentId +"-"+orderRef);


        shipmentRepository.save(shipment);

        return response.readEntity(String.class);
    }*/
}
