package com.badals.admin.service;

import com.badals.admin.domain.*;
import com.badals.admin.domain.enumeration.OrderState;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.pojo.AmazonShipmentItem;
import com.badals.admin.domain.pojo.ShipmentItemCountImpl;
import com.badals.admin.domain.projection.ShipmentItemCount;
import com.badals.admin.domain.projection.Inventory;
import com.badals.admin.domain.projection.OutstandingQueue;
import com.badals.admin.domain.projection.ShipQueue;
import com.badals.admin.domain.projection.SortQueue;
import com.badals.admin.repository.*;
import com.badals.admin.repository.search.ShipmentSearchRepository;
import com.badals.admin.service.dto.*;
import com.badals.admin.service.mapper.ItemIssuanceMapper;
import com.badals.admin.service.mapper.PurchaseShipmentMapper;
import com.badals.admin.service.mapper.ShipmentItemMapper;
import com.badals.admin.service.mapper.ShipmentMapper;

import com.badals.admin.service.mutation.Message;
import com.univocity.parsers.csv.CsvRoutines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
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

    private final ShipmentMapper shipmentMapper;
    private final ShipmentItemMapper shipmentItemMapper;
    private final PurchaseShipmentMapper purchaseShipmentMapper;

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

    public TrackingService(ShipmentRepository shipmentRepository, ShipmentSearchRepository shipmentSearchRepository, ShipmentMapper shipmentMapper,/*, ShipmentSearchRepository shipmentSearchRepository*/ShipmentItemMapper shipmentItemMapper, PurchaseShipmentMapper purchaseShipmentMapper, PurchaseItemRepository purchaseItemRepository, PkgRepository pkgRepository, ShipmentItemRepository shipmentItemRepository, PurchaseShipmentRepository purchaseShipmentRepository, PackagingContentRepository packagingContentRepository, ShipmentReceiptRepository shipmentReceiptRepository, OrderItemRepository orderItemRepository, OrderShipmentRepository orderShipmentRepository, ItemIssuanceRepository itemIssuanceRepository, ItemIssuanceMapper itemIssuanceMapper) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentSearchRepository = shipmentSearchRepository;
        this.shipmentMapper = shipmentMapper;
        this.shipmentItemMapper = shipmentItemMapper;
        this.purchaseShipmentMapper = purchaseShipmentMapper;
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

    private Shipment saveAndReset(Shipment shipment, Set<ShipmentItem> shipmentItems) {
        if (shipment != null) {

            shipment.getShipmentItems().clear();
            shipmentItems.forEach(x -> shipment.addShipmentItem(x));
            shipmentRepository.save(shipment);
            //shipmentSearchRepository.save(shipmentMapper.toDto(shipment));
        }
        return null;
    }

    private Shipment createShipment(String trackingNum, String orderId, String carrier) {
        Shipment shipment = new Shipment();
        shipment.setShipmentStatus(ShipmentStatus.PENDING);
        shipment.setShipmentType(ShipmentType.TRANSIT);
        shipment.setReference(orderId);
        shipment.setTrackingNum(trackingNum);
        shipment.setShipmentMethod(carrier);
        return shipment;
    }

    public Message processAmazonFile() throws IOException {
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
            if (shipment == null) {
                shipment = shipmentRepository.findByTrackingNum(trackingNum).orElse(createShipment(item.getTrackingNum(), item.getOrderId(), item.getCarrier()));
                shipmentItems = new HashSet();
            }
            else if (shipment != null && shipment.getTrackingNum() != null && !shipment.getTrackingNum().equals(trackingNum)) {
                shipment = saveAndReset(shipment, shipmentItems);
                shipment = shipmentRepository.findByTrackingNum(trackingNum).orElse(createShipment(item.getTrackingNum(), item.getOrderId(), item.getCarrier()));
                if(shipment.getShipmentStatus() == ShipmentStatus.SHIPPED ||
                    shipment.getShipmentStatus() == ShipmentStatus.DELIVERED ||
                    shipment.getShipmentStatus() == ShipmentStatus.RECEIVED
                )
                    continue;

                if(item.getShipmentStatus().equals("Closed")) {
                    shipment.setShipmentStatus(ShipmentStatus.SHIPPED);
                }
                shipmentItems = new HashSet();
                //purchaseShipments = new ArrayList();
            }
            String key = item.getAsin();
            Double itemQty = Double.parseDouble(item.getQuantity());
            CRC32 checksum = new CRC32();
            checksum.update(key.getBytes());
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
        return new Message("fuck amazon");
    }

    public List<ShipmentItemDTO> findByTrackingNums(List<String> trackingNums) {
        return shipmentRepository.findByTrackingNums(trackingNums).stream().map(shipmentItemMapper::toDto).collect(Collectors.toList());
    }

    public ShipmentDTO createShipment(ShipmentDTO shipment, List<ShipmentItemDTO> shipmentItems) throws Exception {
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
        s = shipmentRepository.save(s);
        return shipmentMapper.toDto(s);
    }

    public List<ShipmentItemCountImpl> findCountByTrackingNums(List<String> trackingNums) {
        List<ShipmentItemCount> counts = shipmentRepository.findCountByTrackingNums(trackingNums);
        List<ShipmentItemCountImpl> ret = trackingNums.stream().map(x -> new ShipmentItemCountImpl(x,0L)).collect(Collectors.toList());

        for(ShipmentItemCount x: counts) {
            ret.get(trackingNums.indexOf(x.getTrackingNum())).setCount(x.getCount());
        }
        return ret;
    }
}
