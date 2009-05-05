package org.broadleafcommerce.order.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.broadleafcommerce.offer.domain.Offer;
import org.broadleafcommerce.offer.domain.OfferAudit;
import org.broadleafcommerce.offer.domain.OfferAuditImpl;
import org.broadleafcommerce.offer.domain.OfferImpl;
import org.broadleafcommerce.util.money.Money;

@Entity
@DiscriminatorColumn(name = "TYPE")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_FULFILLMENT_GROUP_ITEM")
public class FulfillmentGroupItemImpl implements FulfillmentGroupItem, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FulfillmentGroupItemId", strategy = GenerationType.TABLE)
    @TableGenerator(name = "FulfillmentGroupItemId", table = "SEQUENCE_GENERATOR", pkColumnName = "ID_NAME", valueColumnName = "ID_VAL", pkColumnValue = "FulfillmentGroupItemImpl", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(targetEntity = FulfillmentGroupImpl.class)
    @JoinColumn(name = "FULFILLMENT_GROUP_ID")
    private FulfillmentGroup fulfillmentGroup;

    @OneToOne(targetEntity = OrderItemImpl.class)
    @JoinColumn(name = "ORDER_ID")
    private OrderItem orderItem;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "RETAIL_PRICE")
    private BigDecimal retailPrice;

    @Column(name = "SALE_PRICE")
    private BigDecimal salePrice;

    @Column(name = "PRICE")
    private BigDecimal price;

    //TODO does this work?? id is the primary key of OfferImpl. How does this help the ORM map the association back to FulfillmentGroupItem? This should be a many to many. Make sure to add a cascade annotation with delete_orphans as well.
    @OneToMany(mappedBy = "id", targetEntity = OfferImpl.class)
    private List<Offer> candidateOffers = new ArrayList<Offer>();

    //TODO does this work?? id is the primary key of OfferAuditImpl. How does this help the ORM map the association back to FulfillmentGroupItem? This should be a many to many. Make sure to add a cascade annotation with delete_orphans as well.
    @OneToMany(mappedBy = "id", targetEntity = OfferAuditImpl.class)
    private List<OfferAudit> appliedOffers = new ArrayList<OfferAudit>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FulfillmentGroup getFulfillmentGroup() {
        return fulfillmentGroup;
    }

    public void setFulfillmentGroup(FulfillmentGroup fulfillmentGroup) {
        this.fulfillmentGroup = fulfillmentGroup;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money getRetailPrice() {
        return retailPrice == null ? null : new Money(retailPrice);
    }

    public void setRetailPrice(Money retailPrice) {
        this.retailPrice = Money.toAmount(retailPrice);
    }

    public Money getSalePrice() {
        return salePrice == null ? null : new Money(salePrice);
    }

    public void setSalePrice(Money salePrice) {
        this.salePrice = Money.toAmount(salePrice);
    }

    public Money getPrice() {
        return price == null ? null : new Money(price);
    }

    public void setPrice(Money price) {
        this.price = Money.toAmount(price);
    }

    @Override
    public void addAppliedOffer(OfferAudit offer) {
        appliedOffers.add(offer);
    }

    @Override
    public void addCandidateOffer(Offer offer) {
        candidateOffers.add(offer);
    }

    @Override
    public List<OfferAudit> getAppliedOffers() {
        return appliedOffers;
    }

    @Override
    public List<Offer> getCandidateOffers() {
        return candidateOffers;
    }

    @Override
    public void removeAppliedOffer(OfferAudit offer) {
        appliedOffers.remove(offer);

    }

    @Override
    public void removeCandidateOffer(Offer offer) {
        candidateOffers.remove(offer);
    }

    @Override
    public void setAppliedOffers(List<OfferAudit> offers) {
        this.appliedOffers = offers;
    }

    @Override
    public void setCandidateOffers(List<Offer> offers) {
        this.candidateOffers = offers;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof FulfillmentGroupItemImpl)) return false;

        FulfillmentGroupItemImpl item = (FulfillmentGroupItemImpl) other;

        if (orderItem != null ? !orderItem.equals(item.orderItem) : item.orderItem != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderItem != null ? orderItem.hashCode() : 0;
        result *= 31;

        return result;
    }
}
