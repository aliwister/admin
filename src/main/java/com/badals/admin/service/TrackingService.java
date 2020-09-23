package com.badals.admin.service;

import com.badals.admin.domain.*;
import com.badals.admin.domain.enumeration.OrderState;
import com.badals.admin.domain.enumeration.ShipmentListView;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.pojo.*;

import com.badals.admin.domain.projection.*;

import com.badals.admin.repository.*;
import com.badals.admin.repository.search.ShipmentSearchRepository;
import com.badals.admin.service.dto.*;
import com.badals.admin.service.mapper.*;

import com.badals.admin.service.mutation.Message;
import com.univocity.parsers.csv.CsvRoutines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

//import com.badals.admin.repository.search.ShipmentSearchRepository;

/**
 * Service Implementation for managing {@link Shipment}.
 */
@Service
@Transactional
public class TrackingService {


    private final Logger log = LoggerFactory.getLogger(TrackingService.class);

    private final ShipmentRepository shipmentRepository;
    private final ShipmentSearchRepository shipmentSearchRepository;
    private final ShipmentTrackingRepository shipmentTrackingRepository;
    private final ShipmentEventRepository shipmentEventRepository;

    private final ShipmentMapper shipmentMapper;
    private final ShipmentItemMapper shipmentItemMapper;
    private final PurchaseShipmentMapper purchaseShipmentMapper;
    private final ShipmentTrackingMapper shipmentTrackingMapper;

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
    private final ShipmentDocRepository shipmentDocRepository;
    private final ShipmentDocMapper shipmentDocMapper;

    //private final ShiSmentSearchRepository shipmentSearchRepository;

    public TrackingService(ShipmentRepository shipmentRepository, ShipmentSearchRepository shipmentSearchRepository, ShipmentTrackingRepository shipmentTrackingRepository, ShipmentEventRepository shipmentEventRepository, ShipmentMapper shipmentMapper,/*, ShipmentSearchRepository shipmentSearchRepository*/ShipmentItemMapper shipmentItemMapper, PurchaseShipmentMapper purchaseShipmentMapper, ShipmentTrackingMapper shipmentTrackingMapper, PurchaseItemRepository purchaseItemRepository, PkgRepository pkgRepository, ShipmentItemRepository shipmentItemRepository, PurchaseShipmentRepository purchaseShipmentRepository, PackagingContentRepository packagingContentRepository, ShipmentReceiptRepository shipmentReceiptRepository, OrderItemRepository orderItemRepository, OrderShipmentRepository orderShipmentRepository, ItemIssuanceRepository itemIssuanceRepository, ItemIssuanceMapper itemIssuanceMapper, OrderRepository orderRepository, ShipmentDocRepository shipmentDocRepository, ShipmentDocMapper shipmentDocMapper) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentSearchRepository = shipmentSearchRepository;
        this.shipmentTrackingRepository = shipmentTrackingRepository;
        this.shipmentEventRepository = shipmentEventRepository;
        this.shipmentMapper = shipmentMapper;
        this.shipmentItemMapper = shipmentItemMapper;
        this.purchaseShipmentMapper = purchaseShipmentMapper;
        this.shipmentTrackingMapper = shipmentTrackingMapper;
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
        this.shipmentDocRepository = shipmentDocRepository;
        this.shipmentDocMapper = shipmentDocMapper;
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



    public List<ShipmentDTO> findForShipmentList(List<ShipmentStatus> status, ShipmentType type) {
        List<Shipment> shipments = shipmentRepository.findForShipmentList(status, type);
        return shipments.stream().map(shipmentMapper::toDtoList).collect(Collectors.toList());
    }

    public List<SortQueue> findForSorting(String keyword) {
        return shipmentRepository.findForSorting(keyword);
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


/*    @Transactional(readOnly = true)
    public List<ShipmentDTO> search(String query) {
        log.debug("Request to search Shipments for query {}", query);
        return StreamSupport
            .stream(shipmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(shipmentMapper::toDto)
            .collect(Collectors.toList());
    }*/

    private ShipmentTracking addTracking(Shipment shipment, ShipmentStatus status, Integer event, String details, LocalDateTime eventDate) {
        ShipmentTracking tracking = new ShipmentTracking().shipment(shipment).status(status).shipmentEventId(event).details(details).eventDate(eventDate);

        if(eventDate == null)
            tracking.setEventDate(LocalDateTime.now());

        if(event == 1001 && shipment.getActualShipDate() != null)
            tracking.setEventDate(shipment.getActualShipDate());

        shipmentTrackingRepository.save(tracking);
        return tracking;
    }

    private Shipment saveAndReset(Shipment shipment, Set<ShipmentItem> shipmentItems) {
        if (shipment != null) {

            shipment.getShipmentItems().clear();
            shipmentItems.forEach(x -> shipment.addShipmentItem(x));

            shipmentRepository.save(shipment);
        }
        return null;
    }

    private Shipment createShipment(String trackingNum, String orderId, String carrier, String shipDate, String shippingAddress) {
        Shipment shipment = new Shipment();
        shipment.setShipmentStatus(ShipmentStatus.IN_TRANSIT);

        LocalDate dateTime = LocalDate.parse(shipDate,
            DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        if(shippingAddress.endsWith(", OM")) {
            shipment.setShipmentType(ShipmentType.PURCHASE);
            shipment.addShipmentTracking(new ShipmentTracking().shipment(shipment).status(ShipmentStatus.IN_TRANSIT).shipmentEventId(1101).eventDate(dateTime.atStartOfDay()).details("Sent to " + shippingAddress.substring(shippingAddress.length() - 10) + " via " + carrier));
        }
        else {
            shipment.setShipmentType(ShipmentType.TRANSIT);
            shipment.addShipmentTracking(new ShipmentTracking().shipment(shipment).status(ShipmentStatus.IN_TRANSIT).shipmentEventId(1001).eventDate(dateTime.atStartOfDay()).details("Sent to " + shippingAddress.substring(shippingAddress.length() - 10) + " via " + carrier));
        }

            shipment.setMerchant(new Merchant(1L));
        shipment.setReference(orderId);
        shipment.setTrackingNum(trackingNum);
        shipment.setShipmentMethod(carrier);
        shipment.setPkgCount(1);


        shipment.setActualShipDate(dateTime.atStartOfDay());


        return shipment;
    }

    public synchronized Message processAmazonFile() throws IOException {
        final String fileName = "export.csv";
        //List<AmazonShipmentItem> beans = new CsvToBeanBuilder(new InputStreamReader(TrackingService.class.getClassLoader().getResourceAsStream(fileName))).withType(AmazonShipmentItem.class).build().parse();
        List<AmazonShipmentItem> beans = new CsvRoutines().parseAll(AmazonShipmentItem.class, TrackingService.class.getClassLoader().getResourceAsStream(fileName));
        beans.sort(Comparator.comparing(AmazonShipmentItem::getTrackingNum,Comparator.nullsLast(Comparator.naturalOrder())));
        beans.forEach(x -> System.out.println(x.getAsin() + " " + x.getPo() + " " + x.getCarrier() + " " + x.getTrackingNum()));


        Shipment shipment = null;
        Set shipmentItems = null;
        List purchaseShipments = null;

        for(AmazonShipmentItem item : beans) {
            String trackingNum = item.getTrackingNum();

            if (trackingNum == null || trackingNum.equals("N/A")) {
                shipment = saveAndReset(shipment, shipmentItems);

                continue;
            }

            if(trackingNum.length() < 6) {
                trackingNum = item.getOrderId();
            }

            if (shipment == null) {
                shipment = shipmentRepository.findByTrackingNum(trackingNum).orElse(createShipment(item.getTrackingNum(), item.getOrderId(), item.getCarrier(), item.getShipmentDate(), item.getShippingAddress()));
                if(shipment.getShipmentStatus() == ShipmentStatus.PROCESSING ||
                    shipment.getShipmentStatus() == ShipmentStatus.CLOSED ||
                    shipment.getShipmentStatus() == ShipmentStatus.RECEIVED
                ) {
                    shipment = null;
                    continue;
                }


                shipmentItems = new HashSet();
            }
            else if (shipment != null && shipment.getTrackingNum() != null && !shipment.getTrackingNum().equals(trackingNum)) {
                shipment = saveAndReset(shipment, shipmentItems);
                shipment = shipmentRepository.findByTrackingNum(trackingNum).orElse(createShipment(item.getTrackingNum(), item.getOrderId(), item.getCarrier(), item.getShipmentDate(), item.getShippingAddress()));
                if(shipment.getShipmentStatus() == ShipmentStatus.PROCESSING ||
                    shipment.getShipmentStatus() == ShipmentStatus.CLOSED ||
                    shipment.getShipmentStatus() == ShipmentStatus.RECEIVED ||
                    shipment.getShipmentStatus() == ShipmentStatus.ACCEPTED ||
                    shipment.getShipmentStatus() == ShipmentStatus.CANCELED

                ) {
                    shipment = null;
                    continue;
                }


                shipmentItems = new HashSet();
                //purchaseShipments = new ArrayList();
            }

            if(item.getOrderStatus().equals("Closed") && shipment.getShipmentStatus() != ShipmentStatus.PROCESSING) {
                shipment.setShipmentStatus(ShipmentStatus.PROCESSING);
                shipment.addShipmentTracking(new ShipmentTracking().shipment(shipment).status(ShipmentStatus.PROCESSING).shipmentEventId(1002).eventDate(LocalDateTime.now()));
            }

            String key = item.getAsin();
            Double itemQty = Double.parseDouble(item.getQuantity());
            CRC32 checksum = new CRC32();
            checksum.update(key.getBytes(),0,key.getBytes().length);
            long ref = checksum.getValue();

            ShipmentItem shipmentItem = new ShipmentItem().sequence(shipmentItems.size()+1).description(item.getTitle().substring(0,Math.min(255,item.getTitle().length()))).quantity(BigDecimal.valueOf(itemQty));
            shipmentItem.setProductId(ref);

            List<SortQueue> sortQueue = shipmentRepository.findForTransit(item.getPo(), item.getAsin());
            if (sortQueue.size() > 0) {
                SortQueue p = sortQueue.get(0);

                if(itemQty >= p.getQuantity().doubleValue()) {
                    PurchaseShipment purchaseShipment = new PurchaseShipment().quantity(p.getQuantity()).shipmentItem(shipmentItem).purchaseItem(purchaseItemRepository.getOne(p.getId()));
                    shipmentItem.addPurchaseShipment(purchaseShipment);
                    //purchaseShipmentRepository.save(purchaseShipment);
                }
            }
            shipmentItems.add(shipmentItem);
        }

        if(shipment != null) {
            shipment = saveAndReset(shipment, shipmentItems);

        }
        return new Message("DONE");
    }

    public List<ShipmentItemDTO> findByTrackingNums(List<String> trackingNums, boolean showClosed) {
        if(!showClosed)
            return shipmentRepository.findByTrackingNumsExclude(trackingNums, Arrays.asList(new ShipmentStatus[]{ShipmentStatus.CLOSED})).stream().map(shipmentItemMapper::toDto).collect(Collectors.toList());
        else
            return shipmentRepository.findByTrackingNums(trackingNums).stream().map(shipmentItemMapper::toDto).collect(Collectors.toList());

    }

    public ShipmentDTO createShipment(ShipmentDTO shipment, List<ShipmentItemDTO> shipmentItems, List<String> trackingNums) throws Exception {
        Shipment s = shipmentMapper.toEntity(shipment);
        if (shipmentItems != null)
            for (ShipmentItemDTO shipmentItem : shipmentItems) {
                ShipmentItem from = shipmentItemRepository.getOne(shipmentItem.getFrom());
                if (from == null) {
                    throw new Exception ("From shouldn't be null");
                }
                ShipmentItem si = shipmentItemMapper.toEntity(shipmentItem);
                for (PurchaseShipmentDTO purchaseShipment: shipmentItem.getPurchaseShipments()) {
                    PurchaseShipment ps = purchaseShipmentMapper.toEntity(purchaseShipment);
                    si.addPurchaseShipment(ps);
                }
                s.addShipmentItem(si);
            }
        if(trackingNums != null && trackingNums.size() > 0)
            shipmentRepository.setStatusMulti(ShipmentStatus.CLOSED, trackingNums);
        s = shipmentRepository.save(s);
        return shipmentMapper.toDto(s);
    }

    public List<ShipmentItemSummaryImpl> findCountByTrackingNums(List<String> trackingNums) {
        List<ShipmentItemSummary> counts = shipmentRepository.findCountByTrackingNums(trackingNums);
        List<ShipmentItemSummaryImpl> ret = trackingNums.stream().map(x -> new ShipmentItemSummaryImpl(x,0L)).collect(Collectors.toList());

        for(ShipmentItemSummary x: counts) {
            ret.get(trackingNums.indexOf(x.getTrackingNum())).total(x.getTotal()).status(x.getStatus()).processed(x.getProcessed()).id(x.getId()).reference(x.getReference());
        }
        return ret;
    }

    public List<ShipmentTrackingMap> track(String ref) {
        List<Tracking> tracking = shipmentRepository.trackByRef(ref);

        Map<Long, ShipmentTrackingPojo> map = new HashMap<Long, ShipmentTrackingPojo>();
        tracking.forEach( x -> {
            ItemPojo item = new ItemPojo(x.getDescription(), x.getImage(), x.getQuantity());
            if (map.containsKey(x.getId())) {
                map.get(x.getId()).addItem(item);
            } else {
                map.put(x.getId(), new ShipmentTrackingPojo(x.getId(), x.getStatus(), x.getDate(), x.getType(), x.getTrackingNum(), x.getCarrier(), item));
            }
        });
        Set<Long> shipmentIds = tracking.stream().map(Tracking::getId).collect(Collectors.toSet());

        if(shipmentIds.size() < 1)
            return null;

        List<ShipmentTracking> progress = shipmentRepository.trackingProgress(shipmentIds);
        List<ShipmentDoc> docs = shipmentDocRepository.findByShipmentIdIn(shipmentIds);

        progress.forEach(x -> map.get(x.getShipment().getId()).addProgress(shipmentTrackingMapper.toDto(x)));
        docs.forEach(x -> map.get(x.getShipment().getId()).addDoc(shipmentDocMapper.toDto(x)));



        List<ShipmentTrackingMap> track =  map.entrySet().stream().map(x -> new ShipmentTrackingMap(x.getKey(), x.getValue())).collect(Collectors.toList());
        return track;
    }

    public List<TrackingEvent> trackingEvents() {
        List<ShipmentEvent> e = shipmentEventRepository.findAll();
        List<TrackingEvent> ret = e.stream().map(x -> new TrackingEvent(x.getRef(), x.getDescription())).collect(Collectors.toList());
        return ret;
    }

    public Message addTrackingMulti(List<String> trackingNums, ShipmentStatus shipmentStatus, Integer trackingEvent, LocalDateTime eventDate, String details) {
        List<Shipment> shipments = shipmentRepository.findAllByTrackingNumIn(trackingNums);
        for (Shipment shipment : shipments) {
            shipment.setShipmentStatus(shipmentStatus);
            addTracking(shipment, shipmentStatus, trackingEvent, details, eventDate);
            shipmentRepository.save(shipment);
        }

        return new Message("Success");
    }

    public List<ShipmentList> shipmentList(ShipmentListView listName) {
        switch(listName) {
            case INCOMING:
                return shipmentRepository.incomingShipments();
            case ALL_PURCHASE:
                return shipmentRepository.unclosedPurchase();
            case UNCLOSED_TRANSIT:
                return shipmentRepository.shipQByTypeAndStatusNot(ShipmentType.TRANSIT.name(), ShipmentStatus.CLOSED.name());
            case CANCELLED_TRANSIT:
                return shipmentRepository.shipQByTypeAndStatus(ShipmentType.TRANSIT.name(), ShipmentStatus.CANCELED.name());
            case ALL_TRANSIT:
                return shipmentRepository.shipQByTypeAndStatusNot(ShipmentType.TRANSIT.name(), ShipmentStatus.CANCELED.name());
            case CUSTOMER_SCHEDULED:
                return shipmentRepository.shipQByTypeAndStatus(ShipmentType.CUSTOMER.name(), ShipmentStatus.SCHEDULED.name());
            case CUSTOMER_FAILED:
                return shipmentRepository.shipQByTypeAndStatus(ShipmentType.CUSTOMER.name(), ShipmentStatus.FAILED.name());
        }
        return shipmentRepository.shipQByTypeAndStatus(ShipmentType.PURCHASE.name(), ShipmentStatus.ACCEPTED.name());
    }

/*    public static void main(String args[] ) throws IOException {
        updateFromDetrack();
    }*/
}
/*

SELECT oi. *, si.*, s.* FROM shipment s
JOIN shipment_item si ON s.id = si.shipment_id
JOIN purchase_shipment ps ON ps.shipment_item_id = si.id
JOIN shop.purchase_item_order_item pioi ON pioi.purchase_item_id = ps.purchase_item_id
JOIN shop.order_item oi ON pioi.order_item_id = oi.id
JOIN shop.jhi_order o ON oi.order_id = o.id
WHERE o.reference = 7270143 AND NOT EXISTS (
	SELECT 1
	FROM order_shipment os
	WHERE os.order_item_id = oi.id
)
UNION
SELECT oi.`*`, si.*, s.* FROM shipment s
JOIN shipment_item si ON s.id = si.shipment_id
JOIN order_shipment os ON si.id = os.shipment_item_id
JOIN shop.order_item oi ON oi.id = os.order_item_id
WHERE s.reference = 9471072


 */
