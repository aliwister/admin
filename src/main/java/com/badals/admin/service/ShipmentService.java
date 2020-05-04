package com.badals.admin.service;

import com.badals.admin.domain.*;
import com.badals.admin.domain.enumeration.OrderState;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.pojo.DetrackDelivery;
import com.badals.admin.domain.pojo.DetrackItem;
import com.badals.admin.domain.projection.Inventory;
import com.badals.admin.domain.projection.OutstandingQueue;
import com.badals.admin.domain.projection.ShipQueue;
import com.badals.admin.domain.projection.SortQueue;
import com.badals.admin.repository.*;
//import com.badals.admin.repository.search.ShipmentSearchRepository;
import com.badals.admin.service.dto.*;
import com.badals.admin.service.mapper.ItemIssuanceMapper;
import com.badals.admin.service.mapper.OrderMapper;
import com.badals.admin.service.mapper.ShipmentMapper;
import com.badals.admin.web.rest.errors.ShipmentNotReadyException;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Shipment}.
 */
@Service
@Transactional
public class ShipmentService {

    private final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentRepository shipmentRepository;

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
    @Autowired private OrderRepository orderRepository;

    //private final ShiSmentSearchRepository shipmentSearchRepository;

    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper,/*, ShipmentSearchRepository shipmentSearchRepository*/PurchaseItemRepository purchaseItemRepository, PkgRepository pkgRepository, ShipmentItemRepository shipmentItemRepository, PurchaseShipmentRepository purchaseShipmentRepository, PackagingContentRepository packagingContentRepository, ShipmentReceiptRepository shipmentReceiptRepository, OrderItemRepository orderItemRepository, OrderShipmentRepository orderShipmentRepository, ItemIssuanceRepository itemIssuanceRepository, ItemIssuanceMapper itemIssuanceMapper) {
        this.shipmentRepository = shipmentRepository;
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
        //shipmentSearchRepository.save(shipment);
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

    public void acceptItem(Long shipmentId, Long pkgId, Long purchaseItemId, Long productId, Long merchantId, String description, BigDecimal quantity, BigDecimal accepted, BigDecimal rejected) {
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

    public ItemIssuanceDTO issueItem(Long orderItemId, Long productId, String description, BigDecimal quantity) {
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
            shipment.setShipmentStatus(ShipmentStatus.PENDING);
            shipment.setCustomer(customer);
            shipment.setReference(orderItem.getOrder().getReference());
            shipment.setShipmentType(ShipmentType.CUSTOMER);
            shipmentRepository.save(shipment);
        }

        ShipmentItem shipmentItem = new ShipmentItem().shipment(shipment).sequence(shipment.getShipmentItems().size()+1).description(description).quantity(quantity);
        shipmentItem.setProductId(productId);

        // Order Shipment
        OrderShipment orderShipment = new OrderShipment().quantity(quantity).shipmentItem(shipmentItem);
        orderShipment.setOrderItemId(orderItemId);

        ItemIssuance issuance = new ItemIssuance().quantity(quantity).shipmentItem(shipmentItem).productId(productId);

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

    public void prepItem(Long shipmentItemId, Long packageId, BigDecimal quantity) {
        ShipmentItem item = shipmentItemRepository.findById(shipmentItemId).get();
        Pkg pkg = pkgRepository.findById(packageId).get();
        PackagingContent packagingContent = new PackagingContent();
        packagingContent.setShipmentItem(item);

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

        if(Math.abs(d-d2) > .00001)
            throw new ShipmentNotReadyException("Shipment not ready. Must prepare shipment");

        OrderDTO order = getOrderForDetrack(orderRef).get();
        AddressDTO address = order.getDeliveryAddress();
        CustomerDTO customer = order.getCustomer();
        //BigDecimal balance = orderRepository.getBalance(orderRef);



        // TODO Auto-generated method stub
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println(orderId);

        //String url = "http://buyth.at/-make";
        String url = "https://app.detrack.com/api/v1/deliveries/create.json";

        DetrackDelivery delivery = new DetrackDelivery();
        ArrayList<DetrackItem> items = new ArrayList<DetrackItem>();

        delivery.setDate(date);
        delivery.setPhone (address.getMobile());

        if(address.getMobile() == null && customer != null)
            delivery.setPhone(customer.getMobile());

        delivery.setInstructions (instructions);
        delivery.set_do (shipmentId +"-"+orderRef);
        delivery.setAddress(address.getLine1() + " " + address.getLine2() + " " + address.getCity());
        delivery.setDeliver_to (address.getFirstName() + " " + address.getLastName());
        delivery.setDelivery_time (time);
        delivery.setAssign_to (assignTo);
        delivery.setSales_person ("Ali");
        delivery.setNotify_url("https://api.badals.com/detrack");

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
        params.add("json", "["+json+"]");
        params.add("url", url);
        ResponseEntity<String> response = (ResponseEntity<String>) restTemplate.postForEntity(url, params, String.class);

        shipment.setShipmentStatus(ShipmentStatus.SCHEDULED);
        shipment.setTrackingNum(shipmentId +"-"+orderRef);


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

    /**
     * Search for the shipment corresponding to the query.
     *
     * @param query the query of the search.
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
}
