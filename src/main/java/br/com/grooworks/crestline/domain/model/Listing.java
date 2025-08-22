package br.com.grooworks.crestline.domain.model;

import br.com.grooworks.crestline.domain.dto.ListingDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Base64;

@Entity
@Table(name = "List")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String streetAdress;
    private String city;
    private String state;
    private String zipCode;
    private String county;

    // Available Utilities
    private boolean electricity;
    private boolean water;
    private boolean sewer;
    private boolean naturalGas;
    private boolean highSpeedInternet;
    private boolean phoneService;

    // Land Features
    private boolean pavedRoadAccess;
    private boolean gatedCommunity;
    private boolean mountainViews;
    private boolean waterViews;
    private boolean wooded;
    private boolean openPasture;
    private boolean creekStream;
    private boolean pond;
    private boolean wellOnProperty;
    private boolean septicSystem;
    private boolean fenced;
    private boolean barnOutbuildings;
    private boolean mineralRights;
    private boolean huntingRights;

    // Description
    private String propertyDescription;

    // Financial Information
    private BigDecimal listingPrice;
    private BigDecimal pricePerAcre;
    private BigDecimal downPayment;
    private BigDecimal monthlyPayment;
    private String paymentTerms;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profileProperties;

    public Listing(ListingDTO dto) {
        createOrUpdate(dto);
    }

    public void createOrUpdate(ListingDTO dto) {
        this.streetAdress = dto.streetAdress();
        this.city = dto.city();
        this.state = dto.state();
        this.zipCode = dto.zipCode();
        this.county = dto.county();
        this.electricity = dto.electricity();
        this.water = dto.water();
        this.sewer = dto.sewer();
        this.naturalGas = dto.naturalGas();
        this.highSpeedInternet = dto.highSpeedInternet();
        this.phoneService = dto.phoneService();
        this.pavedRoadAccess = dto.pavedRoadAccess();
        this.gatedCommunity = dto.gatedCommunity();
        this.mountainViews = dto.mountainViews();
        this.waterViews = dto.waterViews();
        this.wooded = dto.wooded();
        this.openPasture = dto.openPasture();
        this.creekStream = dto.creekStream();
        this.pond = dto.pond();
        this.wellOnProperty = dto.wellOnProperty();
        this.septicSystem = dto.septicSystem();
        this.fenced = dto.fenced();
        this.barnOutbuildings = dto.barnOutbuildings();
        this.mineralRights = dto.mineralRights();
        this.huntingRights = dto.huntingRights();
        this.propertyDescription = dto.propertyDescription();
        this.listingPrice = dto.listingPrice();
        this.pricePerAcre = dto.pricePerAcre();
        this.downPayment = dto.downPayment();
        this.monthlyPayment = dto.monthlyPayment();
        this.paymentTerms = dto.paymentTerms();
        this.profileProperties = Base64.getDecoder().decode(dto.profileProperties().replaceAll("\\s+", ""));
    }
}
